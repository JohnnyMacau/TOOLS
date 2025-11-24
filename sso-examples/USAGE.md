# SSO Examples Usage Guide

This guide provides step-by-step instructions for using the SSO examples in this repository.

## Quick Start

### 1. Compile the Examples

**On Linux/Mac:**
```bash
cd sso-examples
./compile-and-run.sh
```

**On Windows:**
```cmd
cd sso-examples
compile-and-run.bat
```

### 2. Configure Your Identity Provider

Before running the examples, you need to configure your identity provider credentials.

## SQL Server SSO Example

### Prerequisites
- SQL Server instance (local or Azure)
- Appropriate authentication method configured on the server

### Configuration

Edit `SQLServerSSOExample.java` and update these variables:

```java
String serverName = "your-server-name";  // e.g., "localhost" or "myserver.database.windows.net"
String databaseName = "your-database";   // e.g., "AdventureWorks"
```

### Usage Examples

#### Windows Integrated Authentication (On-Premises SQL Server)

Uncomment these lines in the `main` method:

```java
conn = connectWithWindowsAuth(serverName, databaseName);
```

**Requirements:**
- Windows operating system
- Current Windows user has access to the SQL Server
- `mssql-jdbc` driver in classpath

**Run:**
```bash
java -cp "../mssql-jdbc-9.2.1.jre8.jar:." SQLServerSSOExample
```

#### Azure Active Directory Integrated

Uncomment these lines:

```java
conn = connectWithAzureADIntegrated(serverName, databaseName);
```

**Requirements:**
- Azure SQL Database
- User logged into Azure AD on the machine
- Microsoft Authentication Library installed

#### Azure Active Directory Password

Uncomment and configure:

```java
String username = "user@yourdomain.com";
String password = "your-password";
conn = connectWithAzureADPassword(serverName, databaseName, username, password);
```

**Note:** Not recommended for production. Use token-based authentication instead.

#### Azure Active Directory Access Token

Uncomment and configure:

```java
String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGc..."; // Your Azure AD token
conn = connectWithAccessToken(serverName, databaseName, accessToken);
```

**Requirements:**
- Valid Azure AD access token
- Token must have appropriate database permissions

**To get an access token:**
```bash
# Using Azure CLI
az account get-access-token --resource https://database.windows.net/
```

#### Azure Managed Service Identity (MSI)

Uncomment:

```java
conn = connectWithMSI(serverName, databaseName);
```

**Requirements:**
- Running on Azure VM, App Service, or Function
- Managed Identity enabled and granted database access

## OAuth 2.0 Example

### Prerequisites
- OAuth 2.0 provider account (Google, Microsoft, GitHub, etc.)
- Registered application with client ID and secret

### Configuration

Edit `OAuth2Example.java` and update the provider configuration:

```java
OAuth2Example oauth = new OAuth2Example(
    "YOUR_CLIENT_ID",           // From your OAuth provider
    "YOUR_CLIENT_SECRET",       // From your OAuth provider
    "https://..../authorize",   // Authorization endpoint
    "https://..../token",       // Token endpoint
    "http://localhost:8080/callback", // Your callback URL
    "openid email profile"      // Requested scopes
);
```

### Provider-Specific Configurations

#### Google OAuth 2.0

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a project and enable Google+ API
3. Create OAuth 2.0 credentials
4. Add authorized redirect URI: `http://localhost:8080/oauth2/callback`

```java
OAuth2Example oauth = new OAuth2Example(
    "123456789.apps.googleusercontent.com",
    "GOCSPX-xxxxxxxxxxxxx",
    "https://accounts.google.com/o/oauth2/v2/auth",
    "https://oauth2.googleapis.com/token",
    "http://localhost:8080/oauth2/callback",
    "openid email profile"
);
```

#### Microsoft Azure AD

1. Go to [Azure Portal](https://portal.azure.com/)
2. Register an application in Azure AD
3. Add redirect URI
4. Generate client secret

```java
OAuth2Example oauth = new OAuth2Example(
    "your-client-id",
    "your-client-secret",
    "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
    "https://login.microsoftonline.com/common/oauth2/v2.0/token",
    "http://localhost:8080/oauth2/callback",
    "openid email profile User.Read"
);
```

#### GitHub OAuth

1. Go to Settings → Developer settings → OAuth Apps
2. Create new OAuth App
3. Set callback URL

```java
OAuth2Example oauth = new OAuth2Example(
    "your-github-client-id",
    "your-github-client-secret",
    "https://github.com/login/oauth/authorize",
    "https://github.com/login/oauth/access_token",
    "http://localhost:8080/oauth2/callback",
    "user:email"
);
```

### Running the OAuth Example

1. **Compile and run:**
   ```bash
   java OAuth2Example
   ```

2. **Copy the authorization URL** from the output

3. **Open in browser** and authorize the application

4. **Copy the authorization code** from the callback URL

5. **Uncomment the token exchange code** and add your authorization code:
   ```java
   String authCode = "PASTE_YOUR_CODE_HERE";
   TokenResponse token = oauth.getAccessToken(authCode);
   ```

6. **Use the access token** to call APIs

## Security Considerations

### DO:
✅ Store credentials in environment variables
✅ Use HTTPS in production
✅ Implement token refresh
✅ Validate all tokens
✅ Log authentication failures
✅ Use short-lived tokens

### DON'T:
❌ Hard-code credentials in source code
❌ Commit secrets to version control
❌ Use HTTP in production
❌ Share access tokens
❌ Store passwords in plain text

## Troubleshooting

### SQL Server Connection Issues

**Problem:** "Login failed for user"
- **Solution:** Check SQL Server authentication settings and user permissions

**Problem:** "integratedSecurity not supported"
- **Solution:** Install Microsoft Authentication Library

**Problem:** "Cannot find sqljdbc_auth.dll"
- **Solution:** Ensure JDBC driver DLL is in system PATH

### OAuth Issues

**Problem:** "invalid_client"
- **Solution:** Verify client ID and secret are correct

**Problem:** "redirect_uri_mismatch"
- **Solution:** Ensure redirect URI matches exactly in provider settings

**Problem:** "invalid_grant"
- **Solution:** Authorization code expired or already used. Get a new code.

## Examples of Complete Flows

### Example 1: Azure SQL with Azure AD Token

```bash
# 1. Get Azure AD token
TOKEN=$(az account get-access-token --resource https://database.windows.net/ --query accessToken -o tsv)

# 2. Update Java code with token
# 3. Compile and run
java -cp "../mssql-jdbc-9.2.1.jre8.jar:." SQLServerSSOExample
```

### Example 2: OAuth 2.0 with Google

```bash
# 1. Configure Google OAuth in code
# 2. Compile
javac OAuth2Example.java

# 3. Run and get authorization URL
java OAuth2Example

# 4. Visit URL in browser, authorize, copy code
# 5. Update code with authorization code
# 6. Run again to get access token
```

## Additional Resources

- [Azure AD Authentication](https://docs.microsoft.com/en-us/azure/azure-sql/database/authentication-aad-overview)
- [OAuth 2.0 Flows](https://oauth.net/2/)
- [SQL Server JDBC Driver](https://docs.microsoft.com/en-us/sql/connect/jdbc/)

## Support

For detailed documentation, see:
- [SSO-README.md](../SSO-README.md) - General SSO documentation
- [README.md](../README.md) - Repository overview

For provider-specific issues, consult your identity provider's documentation.
