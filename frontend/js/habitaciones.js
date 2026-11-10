/**
 * habitaciones.js - Consulta y gestión de habitaciones.
 *   - Gerente: Crear, listar, filtrar y eliminar habitaciones.
 *   - Recepcionista: Consultar disponibilidad y filtrar habitaciones.
 *
 * Consume: HabitacionRestController -> /api/habitaciones
 *   GET    /api/habitaciones        lista habitaciones
 *   POST   /api/habitaciones        crea (HabitacionRequestDTO)
 *   DELETE /api/habitaciones/{id}   elimina
 */

const RUTA_HABITACIONES = '/habitaciones';
let habitaciones = [];
let sesion = null;

async function cargarHabitaciones() {
    try {
        habitaciones = (await apiFetch(RUTA_HABITACIONES)) || [];
        renderizarHabitaciones();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

function renderizarHabitaciones() {
    const filtro = document.getElementById('filtroEstado').value;
    const cuerpo = document.getElementById('cuerpoTablaHabitaciones');
    const lista = habitaciones.filter(h => !filtro || normalizar(h.estado) === filtro);

    if (lista.length === 0) {
        cuerpo.innerHTML = '<tr><td colspan="6" class="tabla-vacia">No hay habitaciones para mostrar.</td></tr>';
        return;
    }

    const esGerente = sesion && sesion.rol === ROLES.GERENTE;

    cuerpo.innerHTML = lista.map(h => `
        <tr>
            <td><strong>${escapeHtml(h.numero)}</strong></td>
            <td>${escapeHtml(h.tipo)}</td>
            <td>${formatearMoneda(h.precio)}</td>
            <td>${escapeHtml(h.capacidad)} personas</td>
            <td><span class="badge estado-${escapeHtml(normalizar(h.estado))}">${escapeHtml(etiquetaEnum(h.estado))}</span></td>
            <td class="text-end">
                ${esGerente ? `<button class="btn btn-sm btn-outline-danger btn-eliminar" data-id="${escapeHtml(h.id)}">Eliminar</button>` : '<span class="text-muted">-</span>'}
            </td>
        </tr>`).join('');
}

async function guardarHabitacion(evento) {
    evento.preventDefault();
    const datos = {
        numero: document.getElementById('habNumero').value.trim(),
        tipo: document.getElementById('habTipo').value,
        precioBase: parseFloat(document.getElementById('habPrecio').value),
        capacidad: parseInt(document.getElementById('habCapacidad').value, 10)
    };

    if (!datos.numero || isNaN(datos.precioBase) || datos.precioBase <= 0 || isNaN(datos.capacidad) || datos.capacidad < 1) {
        mostrarAlerta('Completa los campos con valores válidos.', 'warning');
        return;
    }
    if (habitaciones.some(h => String(h.numero) === datos.numero)) {
        mostrarAlerta(`Ya existe la habitación número ${datos.numero}.`, 'warning');
        return;
    }

    const boton = document.getElementById('btnGuardarHabitacion');
    boton.disabled = true;
    try {
        await apiFetch(RUTA_HABITACIONES, { method: 'POST', body: datos });
        mostrarAlerta('Habitación registrada correctamente.', 'success');
        document.getElementById('formHabitacion').reset();
        await cargarHabitaciones();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    } finally {
        boton.disabled = false;
    }
}

async function eliminarHabitacion(id) {
    if (!confirm('¿Seguro que deseas eliminar esta habitación?')) return;
    try {
        await apiFetch(`${RUTA_HABITACIONES}/${encodeURIComponent(id)}`, { method: 'DELETE' });
        mostrarAlerta('Habitación eliminada.', 'success');
        await cargarHabitaciones();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    sesion = protegerPagina([ROLES.GERENTE, ROLES.RECEPCIONISTA]);
    if (!sesion) return;
    renderNavbar('habitaciones.html');

    // Si es recepcionista, ocultamos el formulario de alta (solo consulta)
    if (sesion.rol === ROLES.RECEPCIONISTA) {
        const colForm = document.getElementById('columnaFormularioHabitacion');
        if (colForm) colForm.classList.add('d-none');
        const colLista = document.getElementById('columnaListaHabitaciones');
        if (colLista) colLista.className = 'col-12';
        const thAcc = document.getElementById('encabezadoAccionesHabitacion');
        if (thAcc) thAcc.classList.add('d-none');
    } else {
        const form = document.getElementById('formHabitacion');
        if (form) form.addEventListener('submit', guardarHabitacion);
    }

    document.getElementById('filtroEstado').addEventListener('change', renderizarHabitaciones);
    document.getElementById('cuerpoTablaHabitaciones').addEventListener('click', e => {
        const boton = e.target.closest('.btn-eliminar');
        if (boton) eliminarHabitacion(boton.dataset.id);
    });
    cargarHabitaciones();
});
