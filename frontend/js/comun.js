/**
 * comun.js - Utilidades compartidas del frontend de HospedaFlow.
 * Contiene: configuración de la API, wrapper de fetch (async/await), alertas
 * dinámicas, manejo de sesión (sessionStorage) y barra de navegación por rol.
 *
 * Es un archivo auxiliar (permitido por la sección 4 del documento técnico)
 * que evita repetir código en habitaciones.js, reservas.js, ofertas.js y perfiles.js.
 */

// Si el HTML lo sirve Spring Boot (puerto 8080) se usa ruta relativa. Si se abre
// con otro servidor (p. ej. Live Server) se apunta directo al backend; los
// controladores ya incluyen @CrossOrigin.
const API_BASE = window.location.port === '8080' ? '/api' : 'http://localhost:8080/api';

/** Valores del enum RolUsuario del backend (se serializan en MAYÚSCULAS). */
const ROLES = { GERENTE: 'GERENTE', RECEPCIONISTA: 'RECEPCIONISTA', HUESPED: 'HUESPED' };

const ETIQUETAS_ROL = { GERENTE: 'Gerente', RECEPCIONISTA: 'Recepcionista', HUESPED: 'Huésped' };

/** Normaliza un rol o estado del backend para comparaciones seguras. */
function normalizar(valor) {
    return String(valor ?? '').trim().toUpperCase();
}

/** "DISPONIBLE" -> "Disponible" */
function etiquetaEnum(valor) {
    const t = String(valor ?? '').toLowerCase();
    return t.charAt(0).toUpperCase() + t.slice(1);
}

/**
 * Wrapper de fetch con JSON. Envía 'Content-Type': 'application/json'.
 * Los endpoints responden JSON en éxito y texto plano en los mensajes de
 * error (400 Bad Request), por eso se lee primero como texto.
 */
async function apiFetch(ruta, opciones = {}) {
    const config = {
        method: opciones.method || 'GET',
        headers: { 'Content-Type': 'application/json' }
    };
    if (opciones.body !== undefined) {
        config.body = JSON.stringify(opciones.body);
    }

    let respuesta;
    try {
        respuesta = await fetch(API_BASE + ruta, config);
    } catch (e) {
        throw new Error('No se pudo conectar con el servidor. Verifica que el backend esté encendido.');
    }

    const texto = await respuesta.text();
    let datos = null;
    if (texto) {
        try { datos = JSON.parse(texto); } catch (e) { datos = texto; }
    }

    if (!respuesta.ok) {
        const detalle = datos && typeof datos === 'object'
            ? (datos.message || datos.mensaje || datos.error)
            : datos;
        throw new Error(detalle || `Error ${respuesta.status} al comunicarse con el servidor.`);
    }
    return datos;
}

/** Muestra una alerta Bootstrap que desaparece sola. tipo: success | danger | warning | info */
function mostrarAlerta(mensaje, tipo = 'success') {
    let contenedor = document.getElementById('contenedorAlertas');
    if (!contenedor) {
        contenedor = document.createElement('div');
        contenedor.id = 'contenedorAlertas';
        document.body.appendChild(contenedor);
    }
    const alerta = document.createElement('div');
    alerta.className = `alert alert-${tipo} alert-dismissible fade show shadow`;
    alerta.setAttribute('role', 'alert');
    alerta.innerHTML = '<span></span><button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>';
    alerta.querySelector('span').textContent = mensaje;
    contenedor.appendChild(alerta);
    setTimeout(() => {
        alerta.classList.remove('show');
        setTimeout(() => alerta.remove(), 200);
    }, 4500);
}

/** Escapa texto para insertarlo de forma segura en innerHTML. */
function escapeHtml(valor) {
    return String(valor ?? '').replace(/[&<>"']/g, c => (
        { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]
    ));
}

function formatearMoneda(valor) {
    const n = Number(valor);
    return isNaN(n) ? '-' : '$' + n.toFixed(2);
}

/**
 * Convierte una fecha del backend a "yyyy-MM-dd".
 * Jackson puede enviar LocalDate como texto ISO o como arreglo [año, mes, día].
 */
function formatearFecha(valor) {
    if (!valor) return '-';
    if (Array.isArray(valor)) {
        const [a, m, d] = valor;
        return `${a}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
    }
    return String(valor).substring(0, 10);
}

/** Fecha de hoy en formato yyyy-MM-dd (hora local). */
function hoyISO() {
    const h = new Date();
    return `${h.getFullYear()}-${String(h.getMonth() + 1).padStart(2, '0')}-${String(h.getDate()).padStart(2, '0')}`;
}

/* ----------------------------- Sesión ----------------------------- */

function guardarSesion(usuario) {
    sessionStorage.setItem('hospedaflow_usuario', JSON.stringify(usuario));
}

function obtenerSesion() {
    try { return JSON.parse(sessionStorage.getItem('hospedaflow_usuario')); }
    catch (e) { return null; }
}

function cerrarSesion() {
    sessionStorage.removeItem('hospedaflow_usuario');
    window.location.href = 'index.html';
}

/** Redirige al login si no hay sesión o si el rol no tiene acceso a la página. */
function protegerPagina(rolesPermitidos) {
    const sesion = obtenerSesion();
    if (!sesion || (rolesPermitidos && !rolesPermitidos.includes(sesion.rol))) {
        window.location.href = 'index.html';
        return null;
    }
    return sesion;
}

/** Indica si el usuario en sesión es personal del hotel (Gerente o Recepcionista). */
function esPersonal(sesion) {
    return !!sesion && (sesion.rol === ROLES.GERENTE || sesion.rol === ROLES.RECEPCIONISTA);
}

/* ------------------------- Barra de navegación ------------------------- */

function renderNavbar(paginaActiva) {
    const contenedor = document.getElementById('barraNavegacion');
    const sesion = obtenerSesion();
    if (!contenedor || !sesion) return;

    const enlaces = [];
    if (sesion.rol === ROLES.GERENTE) {
        enlaces.push(['habitaciones.html', 'Habitaciones'], ['personal.html', 'Recepcionistas']);
    } else if (sesion.rol === ROLES.RECEPCIONISTA) {
        enlaces.push(['habitaciones.html', 'Habitaciones']);
    }
    enlaces.push(['reservas.html', 'Reservas'], ['ofertas.html', 'Ofertas']);

    const items = enlaces.map(([href, texto]) => `
        <li class="nav-item">
            <a class="nav-link ${href === paginaActiva ? 'active' : ''}" href="${href}">${texto}</a>
        </li>`).join('');

    contenedor.innerHTML = `
    <nav class="navbar navbar-expand-lg navbar-dark bg-hospeda">
        <div class="container">
            <a class="navbar-brand fw-bold" href="reservas.html">HospedaFlow</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="menuPrincipal">
                <ul class="navbar-nav me-auto">${items}</ul>
                <span class="navbar-text me-3" id="textoUsuario">${escapeHtml(sesion.nombre)} (${escapeHtml(ETIQUETAS_ROL[sesion.rol] || sesion.rol)})</span>
                <button class="btn btn-outline-light btn-sm" id="btnCerrarSesion">Cerrar sesión</button>
            </div>
        </div>
    </nav>`;
    document.getElementById('btnCerrarSesion').addEventListener('click', cerrarSesion);
}

