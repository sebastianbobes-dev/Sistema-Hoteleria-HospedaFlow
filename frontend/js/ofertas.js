/**
 * ofertas.js - Promociones y descuentos.
 * Consume: OfertaRestController -> /api/ofertas
 *   GET    /api/ofertas        lista ofertas (todos los roles)
 *   POST   /api/ofertas        crea (OfertaRequestDTO) - solo Gerente
 *   DELETE /api/ofertas/{id}   elimina - solo Gerente
 * OfertaRequestDTO: { titulo, descripcion, porcentajeDescuento, fechaInicio, fechaFin, activa, habitacionId }
 * La entidad Oferta responde con: { id, titulo, descripcion, descuento, fechaInicio, fechaFin,
 *   activa, habitacionEnOferta }
 */

const RUTA_OFERTAS = '/ofertas';
let ofertas = [];
let esGerente = false;

/** Una oferta está vigente si está activa y hoy cae dentro de su rango de fechas. */
function estaVigente(oferta) {
    const hoy = hoyISO();
    return !!oferta.activa &&
        formatearFecha(oferta.fechaInicio) <= hoy &&
        hoy <= formatearFecha(oferta.fechaFin);
}

async function cargarOfertas() {
    try {
        ofertas = (await apiFetch(RUTA_OFERTAS)) || [];
        renderizarOfertas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

async function cargarHabitacionesSelector() {
    try {
        const habitaciones = (await apiFetch('/habitaciones')) || [];
        const sel = document.getElementById('ofHabitacion');
        sel.innerHTML = '<option value="">Sin habitación específica</option>' +
            habitaciones.map(h => `<option value="${escapeHtml(h.id)}">Hab. ${escapeHtml(h.numero)} - ${escapeHtml(h.tipo)}</option>`).join('');
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

function renderizarOfertas() {
    const cuerpo = document.getElementById('cuerpoTablaOfertas');
    const columnas = esGerente ? 7 : 6;

    if (ofertas.length === 0) {
        cuerpo.innerHTML = `<tr><td colspan="${columnas}" class="tabla-vacia">No hay ofertas para mostrar.</td></tr>`;
        return;
    }

    cuerpo.innerHTML = ofertas.map(o => {
        const estado = estaVigente(o) ? 'VIGENTE' : 'INACTIVA';
        const accion = esGerente
            ? `<td class="text-end"><button class="btn btn-sm btn-outline-danger btn-eliminar" data-id="${escapeHtml(o.id)}">Eliminar</button></td>`
            : '';
        return `
        <tr>
            <td>${escapeHtml(o.titulo)}</td>
            <td>${escapeHtml(o.descripcion || '-')}</td>
            <td>${escapeHtml(o.descuento)}%</td>
            <td>${escapeHtml(formatearFecha(o.fechaInicio))} a ${escapeHtml(formatearFecha(o.fechaFin))}</td>
            <td>${escapeHtml(o.habitacionEnOferta ? o.habitacionEnOferta.numero : '-')}</td>
            <td><span class="badge estado-${estado}">${etiquetaEnum(estado)}</span></td>
            ${accion}
        </tr>`;
    }).join('');
}

async function guardarOferta(evento) {
    evento.preventDefault();
    const habitacionTexto = document.getElementById('ofHabitacion').value;
    const datos = {
        titulo: document.getElementById('ofTitulo').value.trim(),
        descripcion: document.getElementById('ofDescripcion').value.trim(),
        porcentajeDescuento: parseFloat(document.getElementById('ofDescuento').value),
        fechaInicio: document.getElementById('ofFechaInicio').value,
        fechaFin: document.getElementById('ofFechaFin').value,
        activa: true,
        habitacionId: habitacionTexto ? parseInt(habitacionTexto, 10) : null
    };

    if (!datos.titulo) { mostrarAlerta('El título es obligatorio.', 'warning'); return; }
    if (isNaN(datos.porcentajeDescuento) || datos.porcentajeDescuento <= 0 || datos.porcentajeDescuento > 100) {
        mostrarAlerta('El descuento debe estar entre 1 y 100.', 'warning');
        return;
    }
    if (!datos.fechaInicio || !datos.fechaFin) { mostrarAlerta('Indica las fechas de vigencia.', 'warning'); return; }
    if (datos.fechaInicio > datos.fechaFin) {
        mostrarAlerta('La fecha de inicio no puede ser posterior a la fecha de fin.', 'warning');
        return;
    }

    const boton = document.getElementById('btnGuardarOferta');
    boton.disabled = true;
    try {
        await apiFetch(RUTA_OFERTAS, { method: 'POST', body: datos });
        mostrarAlerta('Oferta creada correctamente.', 'success');
        document.getElementById('formOferta').reset();
        await cargarOfertas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    } finally {
        boton.disabled = false;
    }
}

async function eliminarOferta(id) {
    if (!confirm('¿Seguro que deseas eliminar esta oferta?')) return;
    try {
        await apiFetch(`${RUTA_OFERTAS}/${encodeURIComponent(id)}`, { method: 'DELETE' });
        mostrarAlerta('Oferta eliminada.', 'success');
        await cargarOfertas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const sesion = protegerPagina();
    if (!sesion) return;
    renderNavbar('ofertas.html');
    esGerente = sesion.rol === ROLES.GERENTE;

    if (esGerente) {
        document.getElementById('formOferta').addEventListener('submit', guardarOferta);
        document.getElementById('cuerpoTablaOfertas').addEventListener('click', e => {
            const boton = e.target.closest('.btn-eliminar');
            if (boton) eliminarOferta(boton.dataset.id);
        });
        cargarHabitacionesSelector();
    } else {
        // Recepcionista y huésped solo consultan
        document.getElementById('columnaFormularioOferta').classList.add('d-none');
        document.getElementById('columnaListaOfertas').className = 'col-12';
        document.getElementById('encabezadoAccionesOferta').remove();
    }
    cargarOfertas();
});

