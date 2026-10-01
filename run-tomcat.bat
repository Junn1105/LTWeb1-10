@echo off
setlocal

set "PROJECT_DIR=%~dp0"
set "MAVEN_CMD=D:\LT Web\apache-maven-3.9.11-bin\apache-maven-3.9.11\bin\mvn.cmd"
set "CATALINA_HOME=D:\LT Web\apache-tomcat-10.1.44\apache-tomcat-10.1.44"
set "JAVA_HOME=D:\LT Web\jdk-25\jdk-25.0.2(1)"
set "JRE_HOME=%JAVA_HOME%"
set "CATALINA_OPTS=-Djava.library.path=D:\LT Web\sqlserver-auth"

echo [1/3] Dong goi project...
call "%MAVEN_CMD%" -B -ntp -f "%PROJECT_DIR%pom.xml" clean package
if errorlevel 1 (
    echo Dong goi that bai. Hay xem loi phia tren.
    pause
    exit /b 1
)

echo [2/3] Trien khai BTGiuaKy.war vao Tomcat...
copy /Y "%PROJECT_DIR%target\BTGiuaKy.war" "%CATALINA_HOME%\webapps\BTGiuaKy.war" >nul
if errorlevel 1 (
    echo Khong the chep file WAR vao Tomcat.
    pause
    exit /b 1
)

echo [3/3] Khoi dong Tomcat bang JDK 25...
echo Giu cua so nay mo trong khi su dung website. Nhan Ctrl+C de dung.
echo Khi thay dong "Server startup", mo: http://localhost:8080/BTGiuaKy/home
call "%CATALINA_HOME%\bin\catalina.bat" run
endlocal
