#!/bin/bash

# SSO Examples - Compilation and Execution Script

echo "=========================================="
echo "SSO Examples - Compile and Run Script"
echo "=========================================="
echo ""

# Check if Java is installed
if ! command -v javac &> /dev/null; then
    echo "Error: Java compiler (javac) not found!"
    echo "Please install JDK to compile these examples."
    exit 1
fi

echo "Java version:"
java -version
echo ""

# Set classpath with JDBC drivers
CLASSPATH="../mssql-jdbc-9.2.1.jre8.jar:../mssql-jdbc-7.4.1.jre8.jar:."

# Compile SQL Server SSO Example
echo "Compiling SQLServerSSOExample.java..."
javac -cp "$CLASSPATH" SQLServerSSOExample.java

if [ $? -eq 0 ]; then
    echo "✓ SQLServerSSOExample compiled successfully"
else
    echo "✗ Failed to compile SQLServerSSOExample"
    exit 1
fi

# Compile OAuth2 Example
echo "Compiling OAuth2Example.java..."
javac OAuth2Example.java

if [ $? -eq 0 ]; then
    echo "✓ OAuth2Example compiled successfully"
else
    echo "✗ Failed to compile OAuth2Example"
    exit 1
fi

echo ""
echo "=========================================="
echo "Compilation completed successfully!"
echo "=========================================="
echo ""
echo "To run the examples:"
echo ""
echo "1. SQL Server SSO Example:"
echo "   java -cp \"$CLASSPATH\" SQLServerSSOExample"
echo ""
echo "2. OAuth2 Example:"
echo "   java OAuth2Example"
echo ""
echo "Note: Make sure to configure the examples with your"
echo "actual credentials and endpoints before running."
echo ""
