/**
 * reservas.js - Registro, consulta, confirmación y cancelación de reservas.
 * Consume: ReservaRestController -> /api/reservas
 *   GET /api/reservas                 lista reservas
 *   POST /api/reservas                registra (ReservaRequestDTO)
 *   PUT /api/reservas/{id}/confirmar  confirma
 *   PUT /api/reservas/{id}/cancelar   cancela
 * ReservaRequestDTO: { codigoReserva, fechaEntrada, fechaSalida, huespedId, habitacionId, ofertaId }
 * La entidad Reserva responde con: { id, codigoReserva, fechaEntrada, fechaSalida, estado,
 *   huesped, habitacionAsignada, ofertaAplicada, precioTotal }
 *
 * También consulta /api/habitaciones, /api/ofertas y /api/usuarios para llenar los selectores.
 */

const RUTA_RESERVAS = '/reservas';
let reservas = [];
let sesion = null;

/** Genera un código legible y único, p. ej. RES-LZ4K9A. */
function generarCodigoReserva() {
    return 'RES-' + Date.now().toString(36).toUpperCase().slice(-6);
}

async function cargarSelectores() {
    try {
        const [habitaciones, ofertas, usuarios] = await Promise.all([
            apiFetch('/habitaciones'),
            apiFetch('/ofertas'),
            esPersonal(sesion) ? apiFetch('/usuarios') : Promise.resolve([])
        ]);

        const selHab = document.getElementById('resHabitacion');
        const libres = (habitaciones || []).filter(h => normalizar(h.estado) === 'DISPONIBLE');
        selHab.innerHTML = libres.length
            ? libres.map(h => `<option value="${escapeHtml(h.id)}">Hab. ${escapeHtml(h.numero)} - ${escapeHtml(h.tipo)} (${formatearMoneda(h.precio)}/noche)</option>`).join('')
            : '<option value="">No hay habitaciones disponibles</option>';

        const selOferta = document.getElementById('resOferta');
        const vigentes = (ofertas || []).filter(o => o.activa);
        selOferta.innerHTML = '<option value="">Sin oferta</option>' +
            vigentes.map(o => `<option value="${escapeHtml(o.id)}">${escapeHtml(o.titulo)} (${escapeHtml(o.descuento)}%)</option>`).join('');

        const selHuesped = document.getElementById('resHuesped');
        if (esPersonal(sesion)) {
            const huespedes = (usuarios || []).filter(u => normalizar(u.rol) === ROLES.HUESPED);
            selHuesped.innerHTML = huespedes.length
                ? huespedes.map(u => `<option value="${escapeHtml(u.id)}">${escapeHtml(u.nombre)} (${escapeHtml(u.email ?? u.correo)})</option>`).join('')
                : '<option value="">No hay huéspedes registrados</option>';
        }
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

async function cargarReservas() {
    try {
        reservas = (await apiFetch(RUTA_RESERVAS)) || [];
        renderizarReservas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

function renderizarReservas() {
    const filtro = document.getElementById('filtroEstadoReserva').value;
    const cuerpo = document.getElementById('cuerpoTablaReservas');

    // Un huésped solo ve sus propias reservas
    let lista = esPersonal(sesion) ? reservas : reservas.filter(r => r.huesped && r.huesped.id === sesion.id);
    lista = lista.filter(r => !filtro || normalizar(r.estado) === filtro);

    if (lista.length === 0) {
        cuerpo.innerHTML = '<tr><td colspan="9" class="tabla-vacia">No hay reservas para mostrar.</td></tr>';
        return;
    }

    cuerpo.innerHTML = lista.map(r => {
        const estado = normalizar(r.estado);
        const botones = [];
        if (esPersonal(sesion) && estado === 'PENDIENTE') {
            botones.push(`<button class="btn btn-sm btn-success btn-confirmar" data-id="${escapeHtml(r.id)}">Confirmar</button>`);
        }
        if (estado !== 'CANCELADA') {
            botones.push(`<button class="btn btn-sm btn-outline-danger btn-cancelar" data-id="${escapeHtml(r.id)}">Cancelar</button>`);
        }
        return `
        <tr>
            <td>${escapeHtml(r.codigoReserva)}</td>
            <td>${escapeHtml(r.huesped ? r.huesped.nombre : '-')}</td>
            <td>${escapeHtml(r.habitacionAsignada ? r.habitacionAsignada.numero : '-')}</td>
            <td>${escapeHtml(formatearFecha(r.fechaEntrada))}</td>
            <td>${escapeHtml(formatearFecha(r.fechaSalida))}</td>
            <td>${escapeHtml(r.ofertaAplicada ? r.ofertaAplicada.titulo : '-')}</td>
            <td>${formatearMoneda(r.precioTotal)}</td>
            <td><span class="badge estado-${escapeHtml(estado)}">${escapeHtml(etiquetaEnum(estado))}</span></td>
            <td class="text-end text-nowrap">${botones.join(' ')}</td>
        </tr>`;
    }).join('');
}

async function guardarReserva(evento) {
    evento.preventDefault();
    const huespedId = esPersonal(sesion)
        ? parseInt(document.getElementById('resHuesped').value, 10)
        : sesion.id;
    const habitacionId = parseInt(document.getElementById('resHabitacion').value, 10);
    const ofertaTexto = document.getElementById('resOferta').value;
    const fechaEntrada = document.getElementById('resFechaEntrada').value;
    const fechaSalida = document.getElementById('resFechaSalida').value;

    if (isNaN(huespedId)) { mostrarAlerta('Selecciona un huésped.', 'warning'); return; }
    if (isNaN(habitacionId)) { mostrarAlerta('Selecciona una habitación disponible.', 'warning'); return; }
    if (!fechaEntrada || !fechaSalida) { mostrarAlerta('Indica las fechas de entrada y salida.', 'warning'); return; }
    if (fechaEntrada < hoyISO()) { mostrarAlerta('La fecha de entrada no puede ser anterior a hoy.', 'warning'); return; }
    if (fechaEntrada >= fechaSalida) { mostrarAlerta('La fecha de entrada debe ser anterior a la de salida.', 'warning'); return; }

    const datos = {
        codigoReserva: generarCodigoReserva(),
        fechaEntrada,
        fechaSalida,
        huespedId,
        habitacionId,
        ofertaId: ofertaTexto ? parseInt(ofertaTexto, 10) : null
    };

    const boton = document.getElementById('btnGuardarReserva');
    boton.disabled = true;
    try {
        await apiFetch(RUTA_RESERVAS, { method: 'POST', body: datos });
        mostrarAlerta('Reserva registrada correctamente.', 'success');
        document.getElementById('formReserva').reset();
        await cargarReservas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    } finally {
        boton.disabled = false;
    }
}

async function cambiarEstadoReserva(id, accion, mensajeOk) {
    try {
        await apiFetch(`${RUTA_RESERVAS}/${encodeURIComponent(id)}/${accion}`, { method: 'PUT' });
        mostrarAlerta(mensajeOk, 'success');
        await cargarReservas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    sesion = protegerPagina();
    if (!sesion) return;
    renderNavbar('reservas.html');

    // El huésped reserva a su nombre: se oculta el selector de huésped
    if (!esPersonal(sesion)) {
        document.getElementById('grupoHuesped').classList.add('d-none');
        document.getElementById('tituloListaReservas').textContent = 'Mis reservas';
    } else {
        document.getElementById('tituloListaReservas').textContent = 'Reservas registradas';
    }
    document.getElementById('resFechaEntrada').min = hoyISO();

    document.getElementById('formReserva').addEventListener('submit', guardarReserva);
    document.getElementById('filtroEstadoReserva').addEventListener('change', renderizarReservas);
    document.getElementById('cuerpoTablaReservas').addEventListener('click', e => {
        const confirmar = e.target.closest('.btn-confirmar');
        const cancelar = e.target.closest('.btn-cancelar');
        if (confirmar) {
            cambiarEstadoReserva(confirmar.dataset.id, 'confirmar', 'Reserva confirmada.');
        } else if (cancelar && confirm('¿Seguro que deseas cancelar esta reserva?')) {
            cambiarEstadoReserva(cancelar.dataset.id, 'cancelar', 'Reserva cancelada.');
        }
    });

    cargarSelectores();
    cargarReservas();
});

