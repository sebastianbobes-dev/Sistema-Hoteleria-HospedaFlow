/**
 * app.js - Indicador de conexión con el backend en la pantalla de inicio.
 * Consume: GET /api/health (opcional). Si el endpoint no existe o el servidor no
 * responde, solo se muestra el estado como no disponible, sin romper el login.
 * Requiere comun.js (API_BASE).
 */

async function comprobarConexion() {
    const estado = document.getElementById('connection-status');
    if (!estado) return;

    try {
        const respuesta = await fetch(API_BASE + '/health', {
            headers: { 'Content-Type': 'application/json' }
        });
        if (!respuesta.ok) {
            throw new Error(`Estado ${respuesta.status}`);
        }
        const salud = await respuesta.json();
        estado.textContent = `${salud.application}: ${salud.status}`;
        estado.classList.add('text-success');
    } catch (e) {
        estado.textContent = 'No disponible';
        estado.classList.add('text-danger');
    }
}

document.addEventListener('DOMContentLoaded', comprobarConexion);
