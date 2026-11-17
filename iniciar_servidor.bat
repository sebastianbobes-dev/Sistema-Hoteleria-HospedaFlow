@echo off
title Servidor HospedaFlow
cd /d "%~dp0backend"
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.5.8-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo ========================================================
echo       INICIANDO SERVIDOR HOSPEDAFLOW (Spring Boot)
echo       Disponible en: http://localhost:8080
echo ========================================================
"C:\Users\AndresA\.m2\wrapper\dists\apache-maven-3.8.5-bin\5i5jha092a3i37g0paqnfr15e0\apache-maven-3.8.5\bin\mvn.cmd" -o spring-boot:run
pause
