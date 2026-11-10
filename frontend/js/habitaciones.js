/**
 * habitaciones.js - Gestión de habitaciones (solo Gerente).
 * Consume: HabitacionRestController -> /api/habitaciones
 *   GET    /api/habitaciones        lista habitaciones
 *   POST   /api/habitaciones        crea (HabitacionRequestDTO)
 *   DELETE /api/habitaciones/{id}   elimina
 * HabitacionRequestDTO: { numero, tipo, precioBase, capacidad }
 * La entidad Habitacion responde con: { id, numero, tipo, precio, capacidad, estado }
 */

const RUTA_HABITACIONES = '/habitaciones';
let habitaciones = [];

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

    cuerpo.innerHTML = lista.map(h => `
        <tr>
            <td>${escapeHtml(h.numero)}</td>
            <td>${escapeHtml(h.tipo)}</td>
            <td>${formatearMoneda(h.precio)}</td>
            <td>${escapeHtml(h.capacidad)}</td>
            <td><span class="badge estado-${escapeHtml(normalizar(h.estado))}">${escapeHtml(etiquetaEnum(h.estado))}</span></td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-danger btn-eliminar" data-id="${escapeHtml(h.id)}">Eliminar</button>
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
    // Validación rápida de duplicados en el cliente (el backend también valida)
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
    if (!protegerPagina([ROLES.GERENTE])) return;
    renderNavbar('habitaciones.html');

    document.getElementById('formHabitacion').addEventListener('submit', guardarHabitacion);
    document.getElementById('filtroEstado').addEventListener('change', renderizarHabitaciones);
    document.getElementById('cuerpoTablaHabitaciones').addEventListener('click', e => {
        const boton = e.target.closest('.btn-eliminar');
        if (boton) eliminarHabitacion(boton.dataset.id);
    });
    cargarHabitaciones();
});

