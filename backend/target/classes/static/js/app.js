const connectionStatus = document.querySelector("#connection-status");

fetch("/api/health")
    .then((response) => {
        if (!response.ok) {
            throw new Error(`El servidor respondió con estado ${response.status}`);
        }
        return response.json();
    })
    .then((health) => {
        connectionStatus.textContent = `${health.application}: ${health.status}`;
    })
    .catch((error) => {
        connectionStatus.textContent = `Error de conexión: ${error.message}`;
    });
