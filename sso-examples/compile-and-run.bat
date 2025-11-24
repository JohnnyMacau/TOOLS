@echo off
REM SSO Examples - Compilation and Execution Script for Windows

echo ==========================================
echo SSO Examples - Compile and Run Script
echo ==========================================
echo.

REM Check if Java is installed
where javac >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Java compiler (javac) not found!
    echo Please install JDK to compile these examples.
    exit /b 1
)

echo Java version:
java -version
echo.

REM Set classpath with JDBC drivers
set CLASSPATH=..\mssql-jdbc-9.2.1.jre8.jar;..\mssql-jdbc-7.4.1.jre8.jar;.

REM Compile SQL Server SSO Example
echo Compiling SQLServerSSOExample.java...
javac -cp "%CLASSPATH%" SQLServerSSOExample.java

if %errorlevel% equ 0 (
    echo + SQLServerSSOExample compiled successfully
) else (
    echo - Failed to compile SQLServerSSOExample
    exit /b 1
)

REM Compile OAuth2 Example
echo Compiling OAuth2Example.java...
javac OAuth2Example.java

if %errorlevel% equ 0 (
    echo + OAuth2Example compiled successfully
) else (
    echo - Failed to compile OAuth2Example
    exit /b 1
)

echo.
echo ==========================================
echo Compilation completed successfully!
echo ==========================================
echo.
echo To run the examples:
echo.
echo 1. SQL Server SSO Example:
echo    java -cp "%CLASSPATH%" SQLServerSSOExample
echo.
echo 2. OAuth2 Example:
echo    java OAuth2Example
echo.
echo Note: Make sure to configure the examples with your
echo actual credentials and endpoints before running.
echo.

pause
