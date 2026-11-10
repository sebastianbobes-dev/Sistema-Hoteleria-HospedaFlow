# -*- coding: utf-8 -*-
"""
generar_pdf.py - Generador del documento PDF de Defensa Técnica para HospedaFlow.
Utiliza PyMuPDF (Story) para compilar un PDF profesional con diseño visual estilizado.
"""

import sys
import os
import pymupdf

HTML_CONTENT = """<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8">
<style>
  @page {
    margin: 28pt 36pt 32pt 36pt;
  }
  body {
    font-family: 'Helvetica', 'Arial', sans-serif;
    color: #1a202c;
    font-size: 8.5pt;
    line-height: 1.35;
  }
  
  /* Portada / Header */
  .header-box {
    background-color: #0f2b48;
    color: #ffffff;
    padding: 16pt 20pt;
    border-radius: 6pt;
    margin-bottom: 14pt;
  }
  .header-title {
    font-size: 18pt;
    font-weight: bold;
    margin: 0 0 4pt 0;
    color: #ffffff;
    letter-spacing: 0.5px;
  }
  .header-sub {
    font-size: 11pt;
    color: #90cdf4;
    margin: 0 0 10pt 0;
    font-weight: bold;
  }
  .meta-grid {
    font-size: 8pt;
    color: #e2e8f0;
    line-height: 1.4;
  }
  .meta-highlight {
    color: #fbd38d;
    font-weight: bold;
  }

  /* Encabezados */
  h1 {
    color: #0f2b48;
    font-size: 12.5pt;
    border-bottom: 2pt solid #2b6cb0;
    padding-bottom: 3pt;
    margin-top: 14pt;
    margin-bottom: 8pt;
  }
  h2 {
    color: #2b6cb0;
    font-size: 10.5pt;
    margin-top: 10pt;
    margin-bottom: 5pt;
  }
  h3 {
    color: #2d3748;
    font-size: 9pt;
    margin-top: 8pt;
    margin-bottom: 4pt;
    font-weight: bold;
  }

  p {
    margin: 0 0 6pt 0;
    text-align: justify;
  }

  /* Tablas de Historias */
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 6pt;
    margin-bottom: 10pt;
    font-size: 7.5pt;
  }
  th {
    background-color: #1a365d;
    color: #ffffff;
    text-align: left;
    padding: 5pt 6pt;
    font-weight: bold;
    border: 0.5pt solid #1a365d;
  }
  td {
    padding: 4.5pt 6pt;
    border: 0.5pt solid #cbd5e0;
    vertical-align: top;
  }
  tr:nth-child(even) td {
    background-color: #f7fafc;
  }
  
  /* Badges para métodos HTTP */
  .badge {
    display: inline-block;
    padding: 1pt 4pt;
    border-radius: 3pt;
    font-weight: bold;
    font-size: 7pt;
    color: #ffffff;
  }
  .badge-post { background-color: #2f855a; }
  .badge-get  { background-color: #2b6cb0; }
  .badge-put  { background-color: #c05621; }
  .badge-del  { background-color: #c53030; }

  /* Cajas de notas y destacados */
  .callout {
    background-color: #ebf8ff;
    border-left: 3pt solid #3182ce;
    padding: 6pt 10pt;
    margin: 8pt 0;
    border-radius: 0 4pt 4pt 0;
    font-size: 8pt;
  }
  .callout-title {
    font-weight: bold;
    color: #2b6cb0;
    margin-bottom: 2pt;
  }

  .box-layer {
    background-color: #edf2f7;
    border: 0.8pt solid #cbd5e0;
    border-radius: 4pt;
    padding: 6pt 8pt;
    margin-bottom: 6pt;
  }
  .box-layer-title {
    font-weight: bold;
    color: #1a365d;
    font-size: 8.5pt;
    margin-bottom: 2pt;
  }

  ul, ol {
    margin: 0 0 6pt 16pt;
    padding: 0;
  }
  li {
    margin-bottom: 2.5pt;
  }

  code {
    font-family: 'Courier', monospace;
    background-color: #edf2f7;
    padding: 1pt 3pt;
    border-radius: 2pt;
    font-size: 7.5pt;
    color: #805ad5;
  }

  .step-num {
    font-weight: bold;
    color: #2b6cb0;
  }

  .footer-note {
    font-size: 7pt;
    color: #718096;
    text-align: center;
    border-top: 0.5pt solid #e2e8f0;
    padding-top: 4pt;
    margin-top: 14pt;
  }
</style>
</head>
<body>

<!-- ENCABEZADO / PORTADA -->
<div class="header-box">
  <div class="header-title">HospedaFlow - Sistema de Gestión Hotelera</div>
  <div class="header-sub">Documento Guía y Resumen Técnico para Defensa de Proyecto</div>
  <div class="meta-grid">
    <strong>Asignatura:</strong> Desarrollo de Sistemas Web / Ingeniería de Software &bull; <strong>Sprint:</strong> Sprint 1 (Etapa 3: Ejecución)<br>
    <strong>Rol de Defensa:</strong> <span class="meta-highlight">Desarrollador 4 (Frontend UI/UX & Asynchronous Frontend Developer)</span><br>
    <strong>Arquitectura:</strong> Spring Boot REST API + Jackson JSON Persistence + Vanilla JS / Bootstrap 5 SPA
  </div>
</div>

<!-- SECCIÓN 1: MAPEO DE HISTORIAS DE USUARIO -->
<h1>1. Mapeo de Historias de Usuario (Etapa 3 - Sprint 1) en GitHub</h1>
<p>
El tablero del proyecto en Trello organiza la <strong>Etapa 3: Ejecución (Sprint 1)</strong> en 15 historias de usuario fundamentales para el core del hotel (Habitaciones, Reservas, Perfiles y Ofertas). A continuación se documenta la correspondencia exacta entre cada historia y los componentes desarrollados en el repositorio de GitHub:
</p>

<h2>1.1 Módulo Habitaciones</h2>
<table>
  <tr>
    <th style="width: 20%;">Historia Trello</th>
    <th style="width: 25%;">Frontend (Vista y JS)</th>
    <th style="width: 25%;">Controlador REST (API)</th>
    <th style="width: 30%;">Servicio y Persistencia</th>
  </tr>
  <tr>
    <td><strong>Historia Crear Habitaciones</strong></td>
    <td><code>habitaciones.html</code><br><code>formHabitacion</code> &rarr; <code>guardarHabitacion()</code> en <code>habitaciones.js</code></td>
    <td><span class="badge badge-post">POST</span> <code>/api/habitaciones</code><br><code>HabitacionRestController.crear()</code> (Recibe <code>HabitacionRequestDTO</code>)</td>
    <td><code>HabitacionService.crearHabitacion()</code><br>Valida número único y persiste en <code>habitaciones.json</code> vía <code>HabitacionJsonRepository</code>.</td>
  </tr>
  <tr>
    <td><strong>Historia Consulta de Disponibilidad de habitaciones</strong></td>
    <td><code>habitaciones.html</code> (filtro por estado) y selector dinámico en <code>reservas.html</code>. <code>cargarHabitaciones()</code></td>
    <td><span class="badge badge-get">GET</span> <code>/api/habitaciones?disponibles=true</code><br><code>HabitacionRestController.listar()</code></td>
    <td><code>HabitacionService.consultarDisponibilidad()</code><br>Filtra aquellas habitaciones con <code>EstadoHabitacion.DISPONIBLE</code>.</td>
  </tr>
  <tr>
    <td><strong>Historia eliminar Habitaciones</strong></td>
    <td><code>habitaciones.html</code><br>Botón "Eliminar" en tabla &rarr; <code>eliminarHabitacion()</code> en <code>habitaciones.js</code></td>
    <td><span class="badge badge-del">DELETE</span> <code>/api/habitaciones/{id}</code><br><code>HabitacionRestController.eliminar()</code></td>
    <td><code>HabitacionService.eliminarHabitacion()</code><br>Elimina la entidad por identificador numérico en el repositorio JSON.</td>
  </tr>
</table>

<h2>1.2 Módulo Reservas</h2>
<table>
  <tr>
    <th style="width: 20%;">Historia Trello</th>
    <th style="width: 25%;">Frontend (Vista y JS)</th>
    <th style="width: 25%;">Controlador REST (API)</th>
    <th style="width: 30%;">Servicio y Persistencia</th>
  </tr>
  <tr>
    <td><strong>Historia Registro de reserva de habitacion</strong></td>
    <td><code>reservas.html</code><br><code>formReserva</code> &rarr; <code>guardarReserva()</code> en <code>reservas.js</code></td>
    <td><span class="badge badge-post">POST</span> <code>/api/reservas</code><br><code>ReservaRestController.registrar()</code> (Recibe <code>ReservaRequestDTO</code>)</td>
    <td><code>ReservaService.registrarReserva()</code><br>Asigna estado <code>PENDIENTE</code>, valida fechas, resuelve huésped/habitación y guarda en <code>reservas.json</code>.</td>
  </tr>
  <tr>
    <td><strong>Historia Asignar Habitacion</strong></td>
    <td><code>reservas.html</code><br>Selector <code>resHabitacion</code> o acción en fila &rarr; <code>reservas.js</code></td>
    <td><span class="badge badge-put">PUT</span> <code>/api/reservas/{id}/habitacion/{habId}</code><br><code>ReservaRestController.asignarHabitacion()</code></td>
    <td><code>ReservaService.asignarHabitacion()</code><br>Vincula la habitación y cambia su estado a <code>OCUPADA</code>.</td>
  </tr>
  <tr>
    <td><strong>Historia Modificacion de fechas de reserva</strong></td>
    <td><code>reservas.html</code><br>Botón "Modificar fechas" &rarr; modal/prompt &rarr; <code>modificarFechas()</code></td>
    <td><span class="badge badge-put">PUT</span> <code>/api/reservas/{id}/fechas</code><br><code>ReservaRestController.modificarFechas()</code></td>
    <td><code>ReservaService.modificarFechas()</code><br>Valida que <code>fechaEntrada &lt; fechaSalida</code> y recalcula el monto final total.</td>
  </tr>
  <tr>
    <td><strong>Historia Confirmar reserva de habitacion</strong></td>
    <td><code>reservas.html</code><br>Botón "Confirmar" &rarr; <code>confirmarReserva()</code> en <code>reservas.js</code></td>
    <td><span class="badge badge-put">PUT</span> <code>/api/reservas/{id}/confirmar</code><br><code>ReservaRestController.confirmar()</code></td>
    <td><code>ReservaService.confirmarReserva()</code><br>Transiciona el estado de la reserva de <code>PENDIENTE</code> a <code>CONFIRMADA</code>.</td>
  </tr>
  <tr>
    <td><strong>Historia Cancelacion de reserva de habitacion</strong></td>
    <td><code>reservas.html</code><br>Botón "Cancelar" &rarr; <code>cancelarReserva()</code> en <code>reservas.js</code></td>
    <td><span class="badge badge-put">PUT</span> <code>/api/reservas/{id}/cancelar</code><br><code>ReservaRestController.cancelar()</code></td>
    <td><code>ReservaService.cancelarReserva()</code><br>Establece estado <code>CANCELADA</code> y libera la habitación vinculada a <code>DISPONIBLE</code>.</td>
  </tr>
</table>

<h2>1.3 Módulo Perfiles y Control de Acceso</h2>
<table>
  <tr>
    <th style="width: 20%;">Historia Trello</th>
    <th style="width: 25%;">Frontend (Vista y JS)</th>
    <th style="width: 25%;">Controlador REST (API)</th>
    <th style="width: 30%;">Servicio y Persistencia</th>
  </tr>
  <tr>
    <td><strong>Historia Crear Perfil / Creacion de perfil</strong></td>
    <td><code>index.html</code> (Huéspedes) y <code>personal.html</code> (Recepcionistas).<br><code>perfiles.js</code></td>
    <td><span class="badge badge-post">POST</span> <code>/api/usuarios</code><br><code>PerfilRestController.crear()</code> (Recibe <code>UsuarioRequestDTO</code>)</td>
    <td><code>PerfilService.crearPerfilUsuario()</code><br>Instancia la subclase concreta (<code>Huesped</code>, <code>Recepcionista</code>, <code>Gerente</code>) y almacena en <code>usuarios.json</code>.</td>
  </tr>
  <tr>
    <td><strong>Historia Asignar rol</strong></td>
    <td><code>personal.html</code> y formularios de registro.<br><code>perfiles.js</code></td>
    <td><span class="badge badge-put">PUT</span> <code>/api/usuarios/{id}/rol</code><br><code>PerfilRestController.asignarRol()</code></td>
    <td><code>PerfilService.asignarRol()</code><br>Actualiza el enum <code>RolUsuario</code> del usuario respetando el modelo jerárquico.</td>
  </tr>
  <tr>
    <td><strong>Historia Consultar perfiles</strong></td>
    <td><code>personal.html</code><br>Tabla de recepcionistas activos &rarr; <code>cargarRecepcionistas()</code></td>
    <td><span class="badge badge-get">GET</span> <code>/api/usuarios</code><br><code>PerfilRestController.listar()</code></td>
    <td><code>PerfilService.consultarUsuarios()</code><br>Retorna el listado de usuarios con contraseñas filtradas por seguridad.</td>
  </tr>
  <tr>
    <td><strong>Historia Borrar perfil</strong></td>
    <td><code>personal.html</code><br>Botón "Eliminar" en tabla &rarr; <code>eliminarRecepcionista()</code></td>
    <td><span class="badge badge-del">DELETE</span> <code>/api/usuarios/{id}</code><br><code>PerfilRestController.eliminar()</code></td>
    <td><code>PerfilService.eliminarPerfil()</code><br>Remueve al usuario por ID del repositorio <code>UsuarioJsonRepository</code>.</td>
  </tr>
</table>

<h2>1.4 Módulo Ofertas</h2>
<table>
  <tr>
    <th style="width: 20%;">Historia Trello</th>
    <th style="width: 25%;">Frontend (Vista y JS)</th>
    <th style="width: 25%;">Controlador REST (API)</th>
    <th style="width: 30%;">Servicio y Persistencia</th>
  </tr>
  <tr>
    <td><strong>Historia Crear Oferta</strong></td>
    <td><code>ofertas.html</code><br><code>formOferta</code> &rarr; <code>guardarOferta()</code> en <code>ofertas.js</code></td>
    <td><span class="badge badge-post">POST</span> <code>/api/ofertas</code><br><code>OfertaRestController.crear()</code> (Recibe <code>OfertaRequestDTO</code>)</td>
    <td><code>OfertaService.crearOferta()</code><br>Valida porcentaje y vigencia de fechas; persiste en <code>ofertas.json</code>.</td>
  </tr>
  <tr>
    <td><strong>Historia Modificar Oferta</strong></td>
    <td><code>ofertas.html</code><br>Edición rápida o formulario &rarr; <code>editarOferta()</code></td>
    <td><span class="badge badge-put">PUT</span> <code>/api/ofertas/{id}</code><br><code>OfertaRestController.modificar()</code></td>
    <td><code>OfertaService.modificarOferta()</code><br>Actualiza porcentaje de descuento, fechas de validez o estado activo.</td>
  </tr>
  <tr>
    <td><strong>Historia Eliminar Oferta</strong></td>
    <td><code>ofertas.html</code><br>Botón "Eliminar" en tarjeta &rarr; <code>eliminarOferta()</code></td>
    <td><span class="badge badge-del">DELETE</span> <code>/api/ofertas/{id}</code><br><code>OfertaRestController.eliminar()</code></td>
    <td><code>OfertaService.eliminarOferta()</code><br>Remueve la oferta de la persistencia JSON.</td>
  </tr>
</table>

<!-- SECCIÓN 2: EXPLICACIÓN DEL FLUJO DEL CÓDIGO -->
<h1>2. Explicación del Flujo del Código (Arquitectura y Ciclo de Vida)</h1>
<p>
HospedaFlow adopta una <strong>Arquitectura en Capas estricta (Separation of Concerns)</strong> bajo el patrón <strong>MVC Desacoplado con API REST</strong>. El Frontend y el Backend están completamente separados y se comunican exclusivamente mediante el protocolo HTTP con cargas útiles en formato JSON estándar.
</p>

<h2>2.1 Las 4 Capas del Sistema</h2>

<div class="box-layer">
  <div class="box-layer-title">Capa 1: Cliente / Frontend SPA Asíncrono (HTML5, Bootstrap 5, Vanilla JS)</div>
  <p>
    Construida con JavaScript nativo moderno (<code>ES6+</code>) sin frameworks pesados. Todo el intercambio de información con el servidor se realiza de manera <strong>asíncrona</strong> mediante <code>async/await</code> y la API estándar <code>fetch()</code> centralizada en el helper <code>apiFetch()</code> en <code>comun.js</code>. La interfaz <strong>nunca recarga la página por completo</strong>: intercepta eventos con <code>evento.preventDefault()</code> y actualiza únicamente los fragmentos del DOM necesarios (tablas, alertas flotantes, contadores), brindando una experiencia de usuario rápida y moderna.
  </p>
</div>

<div class="box-layer">
  <div class="box-layer-title">Capa 2: Controladores REST (Spring Boot RestControllers)</div>
  <p>
    Punto de entrada de las peticiones HTTP (<code>@RestController</code>, <code>@RequestMapping</code>). <strong>No contienen lógica de negocio ni cálculos matemáticos</strong>. Su única responsabilidad es recibir el JSON de la solicitud, deserializarlo en objetos de transferencia de datos (<strong>DTOs</strong> como <code>ReservaRequestDTO</code>, <code>HabitacionRequestDTO</code>), invocar al servicio correspondiente y retornar la respuesta empaquetada en un <code>ResponseEntity</code> con el código HTTP semántico correspondiente (<code>200 OK</code>, <code>201 CREATED</code>, <code>400 BAD REQUEST</code>).
  </p>
</div>

<div class="box-layer">
  <div class="box-layer-title">Capa 3: Lógica de Negocio y Dominio (Spring Services)</div>
  <p>
    Clases anotadas con <code>@Service</code> inyectadas con <code>@Autowired</code>. Es el corazón del sistema donde residen las reglas de negocio del hotel: validación de unicidad de habitaciones, control de estados (<code>DISPONIBLE</code>, <code>OCUPADA</code>, <code>MANTENIMIENTO</code>), verificación de solapamiento de fechas, aplicación de descuentos de ofertas vigentes y transiciones de estados de reservas (<code>PENDIENTE</code> &rarr; <code>CONFIRMADA</code> o <code>CANCELADA</code>).
  </p>
</div>

<div class="box-layer">
  <div class="box-layer-title">Capa 4: Persistencia Ligera en Archivos JSON (JsonRepositories)</div>
  <p>
    En cumplimiento de los requerimientos de persistencia ligera sin bases de datos SQL pesadas, el sistema implementa repositorios basados en Jackson (<code>ObjectMapper</code>). Los repositorios leen y escriben atómicamente en colecciones de objetos serializadas en archivos JSON (<code>habitaciones.json</code>, <code>reservas.json</code>, <code>usuarios.json</code>, <code>ofertas.json</code>), asignando llaves primarias autoincrementales de forma determinista.
  </p>
</div>

<h2>2.2 Ciclo de Vida Paso a Paso de una Petición (Ejemplo: Registrar Reserva)</h2>
<ol>
  <li><span class="step-num">Acción del Usuario en Frontend:</span> El usuario completa el formulario en <code>reservas.html</code> seleccionando fecha de llegada, fecha de salida, habitación y oferta, y hace clic en "Guardar reserva".</li>
  <li><span class="step-num">Captura del Evento JS:</span> <code>reservas.js</code> intercepta el submit con <code>evento.preventDefault()</code>, realiza validaciones locales (campos obligatorios) y empaqueta el cuerpo JSON correspondiente.</li>
  <li><span class="step-num">Llamada Asíncrona (apiFetch):</span> Se dispara un <code>fetch('http://localhost:8080/api/reservas', { method: 'POST', body: ... })</code> con cabeceras <code>Content-Type: application/json</code>.</li>
  <li><span class="step-num">Enrutamiento y DTO en Backend:</span> Spring Boot enruta la solicitud al método <code>ReservaRestController.registrar()</code>. Jackson convierte automáticamente el JSON en una instancia de <code>ReservaRequestDTO</code>.</li>
  <li><span class="step-num">Ejecución de Lógica en Servicio:</span> El controlador delega a <code>ReservaService.registrarReserva()</code>, el cual verifica que el huésped exista, que la habitación esté disponible y que la fecha de salida sea posterior a la de entrada.</li>
  <li><span class="step-num">Persistencia en Disco:</span> <code>ReservaJsonRepository</code> lee <code>reservas.json</code>, calcula el siguiente ID secuencial, añade la nueva reserva a la lista y reescribe el archivo JSON con formato indentado.</li>
  <li><span class="step-num">Respuesta HTTP:</span> El controlador responde con estado <code>201 CREATED</code> y el cuerpo JSON de la entidad creada.</li>
  <li><span class="step-num">Actualización Dinámica del DOM:</span> La promesa en <code>reservas.js</code> se resuelve con éxito, invoca a <code>mostrarAlerta('Reserva creada con éxito', 'success')</code>, limpia el formulario y vuelve a consultar la tabla de reservas en segundo plano sin refrescar la página.</li>
</ol>

<h2>2.3 Mecanismos Transversales Clave para la Defensa</h2>

<div class="callout">
  <div class="callout-title">1. Manejo Centralizado de Excepciones (GlobalExceptionHandler.java)</div>
  Cualquier error de negocio (por ejemplo, intentar reservar con fechas inválidas o registrar una habitación duplicada) dispara una excepción <code>IllegalArgumentException</code> o <code>IllegalStateException</code> en el servicio. El componente <code>GlobalExceptionHandler</code> captura la excepción con <code>@ExceptionHandler</code> y devuelve un código HTTP <code>400 Bad Request</code> con el mensaje amigable en formato JSON. Esto impide que el servidor colapse o devuelva errores 500 no controlados.
</div>

<div class="callout">
  <div class="callout-title">2. Seguridad y Control de Acceso Basado en Roles (RBAC)</div>
  En el Frontend, el módulo <code>comun.js</code> gestiona la sesión mediante <code>sessionStorage</code>. Cada vista invoca a <code>protegerPagina([ROLES.GERENTE, ...])</code>: si el usuario no tiene una sesión activa o no posee el rol necesario, es redirigido inmediatamente a <code>index.html</code>. Adicionalmente, la función <code>renderNavbar()</code> construye dinámicamente los menús según el rol autenticado (Gerente tiene acceso a Personal y Habitaciones completas; Recepcionista solo a Habitaciones en modo consulta y Reservas).
</div>

<div class="callout">
  <div class="callout-title">3. Protección de Credenciales (JacksonConfig.java)</div>
  Para evitar fugas de información sensible, la configuración de serialización de Jackson está configurada para excluir la contraseña (<code>password</code> / <code>contrasena</code>) de las respuestas JSON generadas hacia el cliente al consultar usuarios, protegiendo los datos de los empleados y huéspedes.
</div>

<!-- SECCIÓN 3: PREGUNTAS CLAVE PARA LA DEFENSA -->
<h1>3. Preguntas Frecuentes de la Defensa y Cómo Responderlas</h1>

<h3>¿Por qué se usan DTOs en lugar de recibir directamente las entidades en los controladores?</h3>
<p>
<strong>Respuesta:</strong> Porque los DTOs (<em>Data Transfer Objects</em>) aíslan el contrato de la API del modelo interno de persistencia. Permiten recibir IDs foráneos sencillos (como <code>huespedId</code> o <code>habitacionId</code>) desde el cliente en lugar de objetos anidados complejos, mejorando la seguridad, evitando ciclos de serialización infinita y permitiendo validar entradas de forma independiente.
</p>

<h3>¿Cómo garantiza el Frontend que la aplicación sea asíncrona?</h3>
<p>
<strong>Respuesta:</strong> Al no utilizar recargas de página tradicionales (envíos síncronos de formularios HTML). Se intercepta cada acción con escuchadores de eventos JavaScript (<code>addEventListener</code>), se ejecutan llamadas HTTP en segundo plano con <code>fetch</code> y <code>async/await</code>, y se manipula el árbol DOM de forma reactiva en base a la respuesta obtenida.
</p>

<h3>¿Dónde se validan las reglas de negocio, en el cliente o en el servidor?</h3>
<p>
<strong>Respuesta:</strong> Se implementa <strong>validación en doble capa</strong>. En el cliente se realizan validaciones preliminares de UX (campos requeridos, formato mínimo de contraseña) para retroalimentar al usuario al instante. Sin embargo, <strong>la validación autoritativa e inmutable reside en los Servicios de Spring Boot</strong> (<code>HabitacionService</code>, <code>ReservaService</code>), garantizando la integridad de los datos incluso si se realizan peticiones externas a la API.
</p>

<div class="footer-note">
  HospedaFlow &bull; Proyecto Hotelero Sprint 1 &bull; Documento generado para Defensa Técnica
</div>

</body>
</html>
"""

def generar_pdf():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    output_filename = "HospedaFlow_Guia_Defensa_Etapa3.pdf"
    output_path = os.path.join(script_dir, output_filename)

    # Dimensiones A4 en puntos (595 x 842)
    rect = pymupdf.paper_rect("a4")
    # Márgenes: izquierda 32, arriba 32, derecha 32, abajo 32
    margin_x = 32
    margin_y = 32
    where = rect + (margin_x, margin_y, -margin_x, -margin_y)

    story = pymupdf.Story(html=HTML_CONTENT)
    writer = pymupdf.DocumentWriter(output_path)

    more = 1
    page_num = 1
    while more:
        dev = writer.begin_page(rect)
        more, filled = story.place(where)
        story.draw(dev)
        writer.end_page()
        page_num += 1

    writer.close()
    
    # Abrir para verificar páginas
    doc = pymupdf.open(output_path)
    total_pages = doc.page_count
    doc.close()
    
    print(f"PDF generado exitosamente en: {output_path}")
    print(f"Total de paginas generadas: {total_pages}")
    return output_path

if __name__ == "__main__":
    generar_pdf()
