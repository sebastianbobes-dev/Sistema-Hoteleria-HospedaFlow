# HospedaFlow

## Ejecutar la aplicación

Se requiere Java 25 o superior y Maven. Desde la carpeta `backend`, ejecuta:

```powershell
mvn spring-boot:run
```

Abre `http://localhost:8080` para ver la página y comprobar la conexión. El backend expone `GET http://localhost:8080/api/health`, que responde con el estado del servicio en JSON.
