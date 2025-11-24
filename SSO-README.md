# Single Sign-On (SSO) Implementation Guide

## Overview

This directory contains resources and examples for implementing Single Sign-On (SSO) authentication in your applications.

## What is SSO?

Single Sign-On (SSO) is an authentication process that allows users to access multiple applications with one set of login credentials. This improves security and user experience by:

- Reducing password fatigue
- Minimizing time spent re-entering credentials
- Simplifying password management
- Improving security through centralized authentication

## SSO Methods Supported

### 1. OAuth 2.0
OAuth 2.0 is an authorization framework that enables applications to obtain limited access to user accounts on an HTTP service.

**Common Providers:**
- Google OAuth
- Microsoft Azure AD
- GitHub OAuth
- Facebook Login

### 2. SAML 2.0
Security Assertion Markup Language (SAML) is an XML-based open standard for exchanging authentication and authorization data between parties.

**Common Providers:**
- Okta
- OneLogin
- Azure AD
- ADFS

### 3. OpenID Connect (OIDC)
OpenID Connect is an identity layer on top of OAuth 2.0, allowing clients to verify the identity of end-users.

### 4. Windows Integrated Authentication
Native Windows authentication using Kerberos or NTLM protocols.

## SQL Server SSO Integration

This repository includes JDBC drivers for SQL Server. Here's how to implement SSO with SQL Server:

### Using Windows Integrated Authentication

```java
// Connection string for Windows Authentication
String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
    "databaseName=YourDatabase;" +
    "integratedSecurity=true;";

Connection conn = DriverManager.getConnection(connectionUrl);
```

### Using Azure Active Directory Authentication

```java
// Connection string for Azure AD Authentication
String connectionUrl = "jdbc:sqlserver://your-server.database.windows.net:1433;" +
    "databaseName=YourDatabase;" +
    "authentication=ActiveDirectoryIntegrated;";

Connection conn = DriverManager.getConnection(connectionUrl);
```

## Configuration Files

- `sso-config/oauth2-config.json` - OAuth 2.0 configuration
- `sso-config/saml-config.xml` - SAML 2.0 configuration
- `sso-examples/` - Java code examples

## Getting Started

1. Choose your SSO method (OAuth 2.0, SAML, etc.)
2. Configure your identity provider
3. Update the configuration files with your provider details
4. Implement the authentication flow using the provided examples
5. Test the integration

## Security Best Practices

1. **Always use HTTPS** - Never send credentials over unencrypted connections
2. **Store secrets securely** - Use environment variables or secure vaults
3. **Implement token refresh** - Handle token expiration gracefully
4. **Validate tokens** - Always verify token signatures and claims
5. **Use short-lived tokens** - Minimize the window of vulnerability
6. **Implement logout** - Ensure proper session termination
7. **Monitor and log** - Track authentication attempts and failures

## Dependencies

This repository includes:
- `mssql-jdbc-9.2.1.jre8.jar` - Latest MSSQL JDBC driver with SSO support
- `mssql-jdbc-7.4.1.jre8.jar` - Legacy MSSQL JDBC driver
- `sqljdbc4-4.2.jar` - Older JDBC driver (limited SSO support)

## Resources

- [OAuth 2.0 Specification](https://oauth.net/2/)
- [SAML 2.0 Specification](http://docs.oasis-open.org/security/saml/)
- [OpenID Connect](https://openid.net/connect/)
- [Microsoft SQL Server Authentication](https://docs.microsoft.com/en-us/sql/connect/jdbc/connecting-to-an-azure-sql-database)

## Support

For issues or questions about SSO implementation, please refer to your identity provider's documentation or contact your system administrator.
