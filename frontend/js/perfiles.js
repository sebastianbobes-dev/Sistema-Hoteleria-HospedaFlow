/**
 * perfiles.js - Login, selección de rol, registro de huéspedes y
 * gestión de recepcionistas por parte del Gerente.
 *
 * Consume: PerfilRestController -> /api/usuarios
 *   GET    /api/usuarios            lista todos los usuarios
 *   POST   /api/usuarios            crea un usuario (UsuarioRequestDTO)
 *   DELETE /api/usuarios/{id}       elimina un usuario
 *   POST   /api/usuarios/login      valida credenciales
 */

const RUTA_PERFILES = '/usuarios';

/* ----------------------------- Navegación y Login ----------------------------- */

function redirigirSegunRol(usuario) {
    window.location.href = usuario.rol === ROLES.GERENTE ? 'habitaciones.html' : 'reservas.html';
}

async function iniciarSesion(evento) {
    evento.preventDefault();
    const rol = document.getElementById('loginRol').value;
    const email = document.getElementById('loginEmail').value.trim().toLowerCase();
    const password = document.getElementById('loginPassword').value;

    if (!email || !password) {
        mostrarAlerta('Ingresa tu correo y contraseña.', 'warning');
        return;
    }

    const boton = document.getElementById('btnLogin');
    boton.disabled = true;
    try {
        const usuario = await apiFetch(RUTA_PERFILES + '/login', {
            method: 'POST',
            body: { email, password, rol }
        });
        const sesion = { id: usuario.id, nombre: usuario.nombre, email: usuario.email ?? usuario.correo, rol: normalizar(usuario.rol) };
        guardarSesion(sesion);
        redirigirSegunRol(sesion);
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    } finally {
        boton.disabled = false;
    }
}

/* ----------------------------- Registro de Huéspedes (Público) ----------------------------- */

async function registrarHuesped(evento) {
    evento.preventDefault();
    const datos = {
        nombre: document.getElementById('regNombre').value.trim(),
        email: document.getElementById('regEmail').value.trim(),
        telefono: document.getElementById('regTelefono').value.trim(),
        password: document.getElementById('regPassword').value,
        rol: ROLES.HUESPED
    };

    if (!datos.nombre || !datos.email || !datos.telefono || datos.password.length < 4) {
        mostrarAlerta('Completa todos los campos (contraseña mínimo 4 caracteres).', 'warning');
        return;
    }

    const boton = document.getElementById('btnRegistrar');
    boton.disabled = true;
    try {
        await apiFetch(RUTA_PERFILES, { method: 'POST', body: datos });
        mostrarAlerta('Cuenta creada. Ya puedes iniciar sesión.', 'success');
        document.getElementById('formRegistro').reset();
        document.getElementById('loginEmail').value = datos.email;
        bootstrap.Tab.getOrCreateInstance(document.getElementById('tabLogin')).show();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    } finally {
        boton.disabled = false;
    }
}

/* ----------------------------- Gestión de Recepcionistas (Solo Gerente) ----------------------------- */

let recepcionistas = [];

async function cargarRecepcionistas() {
    try {
        const usuarios = (await apiFetch(RUTA_PERFILES)) || [];
        recepcionistas = usuarios.filter(u => normalizar(u.rol) === ROLES.RECEPCIONISTA);
        renderizarRecepcionistas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

function renderizarRecepcionistas() {
    const cuerpo = document.getElementById('cuerpoTablaRecepcionistas');
    if (!cuerpo) return;

    if (recepcionistas.length === 0) {
        cuerpo.innerHTML = '<tr><td colspan="5" class="tabla-vacia">No hay recepcionistas registrados.</td></tr>';
        return;
    }

    cuerpo.innerHTML = recepcionistas.map(u => `
        <tr>
            <td><strong>${escapeHtml(u.nombre)}</strong></td>
            <td>${escapeHtml(u.email ?? u.correo)}</td>
            <td>${escapeHtml(u.telefono)}</td>
            <td><span class="badge bg-primary">Recepcionista</span></td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-danger btn-eliminar-rec" data-id="${escapeHtml(u.id)}">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function guardarRecepcionista(evento) {
    evento.preventDefault();
    const datos = {
        nombre: document.getElementById('recNombre').value.trim(),
        email: document.getElementById('recEmail').value.trim(),
        telefono: document.getElementById('recTelefono').value.trim(),
        password: document.getElementById('recPassword').value,
        rol: ROLES.RECEPCIONISTA
    };

    if (!datos.nombre || !datos.email || !datos.telefono || datos.password.length < 4) {
        mostrarAlerta('Completa todos los campos (contraseña mínimo 4 caracteres).', 'warning');
        return;
    }

    const boton = document.getElementById('btnGuardarRecepcionista');
    boton.disabled = true;
    try {
        await apiFetch(RUTA_PERFILES, { method: 'POST', body: datos });
        mostrarAlerta('Recepcionista registrado con éxito.', 'success');
        document.getElementById('formRecepcionista').reset();
        await cargarRecepcionistas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    } finally {
        boton.disabled = false;
    }
}

async function eliminarRecepcionista(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar este recepcionista?')) return;
    try {
        await apiFetch(`${RUTA_PERFILES}/${encodeURIComponent(id)}`, { method: 'DELETE' });
        mostrarAlerta('Recepcionista eliminado correctamente.', 'success');
        await cargarRecepcionistas();
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    }
}

/* ----------------------------- Inicialización ----------------------------- */

document.addEventListener('DOMContentLoaded', () => {
    // Si estamos en la vista de personal (personal.html)
    const formRec = document.getElementById('formRecepcionista');
    if (formRec) {
        const sesion = protegerPagina([ROLES.GERENTE]);
        if (!sesion) return;
        renderNavbar('personal.html');
        formRec.addEventListener('submit', guardarRecepcionista);

        const cuerpo = document.getElementById('cuerpoTablaRecepcionistas');
        if (cuerpo) {
            cuerpo.addEventListener('click', e => {
                const btn = e.target.closest('.btn-eliminar-rec');
                if (btn) eliminarRecepcionista(btn.dataset.id);
            });
        }

        cargarRecepcionistas();
        return;
    }

    // Si estamos en el login/registro (index.html)
    const formLogin = document.getElementById('formLogin');
    if (formLogin) {
        const sesion = obtenerSesion();
        if (sesion) { redirigirSegunRol(sesion); return; }
        formLogin.addEventListener('submit', iniciarSesion);
        const formReg = document.getElementById('formRegistro');
        if (formReg) formReg.addEventListener('submit', registrarHuesped);
    }
});
