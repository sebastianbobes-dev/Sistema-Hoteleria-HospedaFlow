/**
 * perfiles.js - Login, selección de rol y registro de huéspedes.
 * Consume: PerfilRestController -> /api/usuarios  (POST, POST /login)
 * Envía UsuarioRequestDTO: { nombre, email, password, telefono, rol }
 *
 * Login: POST /api/usuarios/login { email, password, rol } -> el backend valida las
 * credenciales (401 si son incorrectas) y devuelve el usuario sin contraseña.
 */

const RUTA_PERFILES = '/usuarios';

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
        // La validación la hace el backend (POST /api/usuarios/login); responde 401 si falla
        // y nunca devuelve la contraseña.
        const usuario = await apiFetch(RUTA_PERFILES + '/login', {
            method: 'POST',
            body: { email, password, rol }
        });
        // La contraseña no se conserva en la sesión del navegador
        const sesion = { id: usuario.id, nombre: usuario.nombre, email: usuario.email ?? usuario.correo, rol: normalizar(usuario.rol) };
        guardarSesion(sesion);
        redirigirSegunRol(sesion);
    } catch (e) {
        mostrarAlerta(e.message, 'danger');
    } finally {
        boton.disabled = false;
    }
}

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

document.addEventListener('DOMContentLoaded', () => {
    const sesion = obtenerSesion();
    if (sesion) { redirigirSegunRol(sesion); return; }
    document.getElementById('formLogin').addEventListener('submit', iniciarSesion);
    document.getElementById('formRegistro').addEventListener('submit', registrarHuesped);
});

