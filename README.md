# TOOLS Repository

A collection of development tools, libraries, and Single Sign-On (SSO) implementation examples.

## Contents

### 1. SSO Implementation (`/sso-examples`, `/sso-config`)

Complete Single Sign-On implementation examples and configuration templates.

**Features:**
- OAuth 2.0 authentication examples
- SAML 2.0 configuration templates
- SQL Server SSO integration
- Azure Active Directory integration
- Support for multiple identity providers (Google, Microsoft, GitHub, Okta)

**Quick Start:**
```bash
cd sso-examples
./compile-and-run.sh  # Linux/Mac
# or
compile-and-run.bat   # Windows
```

See [SSO-README.md](SSO-README.md) for detailed SSO documentation.

### 2. JDBC Drivers

SQL Server JDBC drivers for database connectivity with SSO support:

- `mssql-jdbc-9.2.1.jre8.jar` - Latest Microsoft SQL Server JDBC Driver
  - Full SSO support including Azure AD authentication
  - Windows Integrated Authentication
  - Managed Service Identity (MSI) support
  
- `mssql-jdbc-7.4.1.jre8.jar` - Legacy Microsoft SQL Server JDBC Driver
  - Basic SSO support
  
- `sqljdbc4-4.2.jar` - Older SQL Server JDBC Driver (v4.2)
- `sqljdbc4-4.0.jar` - Older SQL Server JDBC Driver (v4.0)

### 3. Java Development Tools

- `lombok.jar` - Project Lombok for reducing boilerplate code
  - Automatic getter/setter generation
  - Builder pattern support
  - Logging annotations

### 4. Development Notes

- `note.txt` - SQL query examples and tips
  - SQL Server window functions
  - Oracle recursive queries
  - Data pagination techniques

### 5. Notepad++ (`/notepad++`)

Notepad++ text editor with plugins and configurations.

## SSO Implementation Guide

### Supported Authentication Methods

1. **OAuth 2.0**
   - Google OAuth
   - Microsoft Azure AD
   - GitHub OAuth
   - Custom OAuth providers

2. **SAML 2.0**
   - Okta
   - OneLogin
   - Azure AD SAML
   - ADFS

3. **SQL Server SSO**
   - Windows Integrated Authentication
   - Azure Active Directory
   - Managed Service Identity

### Configuration Files

Located in `/sso-config`:
- `oauth2-config.json` - OAuth 2.0 provider configurations
- `saml-config.xml` - SAML 2.0 identity provider settings

### Code Examples

Located in `/sso-examples`:
- `SQLServerSSOExample.java` - SQL Server authentication methods
- `OAuth2Example.java` - OAuth 2.0 flow implementation
- Compilation scripts for Windows and Unix/Linux

## Getting Started with SSO

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- SQL Server (for database SSO examples)
- Identity Provider account (Azure AD, Google, etc.)

### Setup Steps

1. **Clone or download this repository**

2. **Configure your identity provider:**
   - Register your application
   - Obtain client ID and secret
   - Set up redirect URIs

3. **Update configuration files:**
   ```bash
   # Edit OAuth configuration
   nano sso-config/oauth2-config.json
   
   # Edit SAML configuration
   nano sso-config/saml-config.xml
   ```

4. **Compile and run examples:**
   ```bash
   cd sso-examples
   ./compile-and-run.sh
   ```

5. **Follow the instructions in the examples**

## SQL Server SSO Example

```java
// Windows Integrated Authentication
String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
    "databaseName=YourDB;" +
    "integratedSecurity=true;";
Connection conn = DriverManager.getConnection(connectionUrl);

// Azure AD Authentication
String connectionUrl = "jdbc:sqlserver://your-server.database.windows.net:1433;" +
    "databaseName=YourDB;" +
    "authentication=ActiveDirectoryIntegrated;";
Connection conn = DriverManager.getConnection(connectionUrl);
```

## OAuth 2.0 Example Flow

1. **Authorization Request** - Redirect user to identity provider
2. **User Authentication** - User logs in at identity provider
3. **Authorization Grant** - Receive authorization code
4. **Token Exchange** - Exchange code for access token
5. **Access Resources** - Use token to access protected resources

## Security Best Practices

✅ **Always use HTTPS** in production  
✅ **Store secrets securely** (environment variables, vaults)  
✅ **Implement token refresh** for long-lived sessions  
✅ **Validate all tokens** and verify signatures  
✅ **Use short-lived tokens** to minimize risk  
✅ **Implement proper logout** functionality  
✅ **Monitor and log** authentication events  

## Resources

- [OAuth 2.0 Specification](https://oauth.net/2/)
- [SAML 2.0 Documentation](http://docs.oasis-open.org/security/saml/)
- [Microsoft SQL Server JDBC Documentation](https://docs.microsoft.com/en-us/sql/connect/jdbc/)
- [Azure Active Directory Documentation](https://docs.microsoft.com/en-us/azure/active-directory/)

## Support

For issues or questions:
1. Check the [SSO-README.md](SSO-README.md) for detailed documentation
2. Review the code examples in `/sso-examples`
3. Consult your identity provider's documentation

## License

This is a tools repository. Individual tools and libraries may have their own licenses:
- Microsoft JDBC Drivers: [Microsoft JDBC License](https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server)
- Lombok: [MIT License](https://projectlombok.org/credits)
- Notepad++: [GPL License](https://github.com/notepad-plus-plus/notepad-plus-plus)

## Contributing

This is a personal tools repository. Feel free to fork and adapt for your own use.

---

**Note:** Make sure to replace placeholder values (YOUR_CLIENT_ID, YOUR_CLIENT_SECRET, etc.) with actual values from your identity provider before using the examples.
