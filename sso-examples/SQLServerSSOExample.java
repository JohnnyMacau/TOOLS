import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQL Server SSO Authentication Examples
 * 
 * This class demonstrates various SSO authentication methods for SQL Server:
 * 1. Windows Integrated Authentication
 * 2. Azure Active Directory Authentication
 * 3. Access Token Authentication
 */
public class SQLServerSSOExample {

    /**
     * Example 1: Windows Integrated Authentication
     * Uses the current Windows user's credentials for authentication
     */
    public static Connection connectWithWindowsAuth(String serverName, String databaseName) 
            throws SQLException {
        // Connection string for Windows Integrated Authentication
        String connectionUrl = String.format(
            "jdbc:sqlserver://%s:1433;" +
            "databaseName=%s;" +
            "integratedSecurity=true;" +
            "encrypt=true;" +
            "trustServerCertificate=false;",
            serverName, databaseName
        );
        
        System.out.println("Connecting with Windows Integrated Authentication...");
        return DriverManager.getConnection(connectionUrl);
    }

    /**
     * Example 2: Azure Active Directory Integrated Authentication
     * Uses Azure AD credentials of the current user
     */
    public static Connection connectWithAzureADIntegrated(String serverName, String databaseName) 
            throws SQLException {
        String connectionUrl = String.format(
            "jdbc:sqlserver://%s.database.windows.net:1433;" +
            "databaseName=%s;" +
            "authentication=ActiveDirectoryIntegrated;" +
            "encrypt=true;" +
            "trustServerCertificate=false;",
            serverName, databaseName
        );
        
        System.out.println("Connecting with Azure AD Integrated Authentication...");
        return DriverManager.getConnection(connectionUrl);
    }

    /**
     * Example 3: Azure Active Directory Password Authentication
     * Uses Azure AD username and password
     */
    public static Connection connectWithAzureADPassword(
            String serverName, String databaseName, String username, String password) 
            throws SQLException {
        String connectionUrl = String.format(
            "jdbc:sqlserver://%s.database.windows.net:1433;" +
            "databaseName=%s;" +
            "authentication=ActiveDirectoryPassword;" +
            "user=%s;" +
            "password=%s;" +
            "encrypt=true;" +
            "trustServerCertificate=false;",
            serverName, databaseName, username, password
        );
        
        System.out.println("Connecting with Azure AD Password Authentication...");
        return DriverManager.getConnection(connectionUrl);
    }

    /**
     * Example 4: Azure Active Directory Access Token Authentication
     * Uses an Azure AD access token
     */
    public static Connection connectWithAccessToken(
            String serverName, String databaseName, String accessToken) 
            throws SQLException {
        String connectionUrl = String.format(
            "jdbc:sqlserver://%s.database.windows.net:1433;" +
            "databaseName=%s;" +
            "accessToken=%s;" +
            "encrypt=true;" +
            "trustServerCertificate=false;",
            serverName, databaseName, accessToken
        );
        
        System.out.println("Connecting with Azure AD Access Token...");
        return DriverManager.getConnection(connectionUrl);
    }

    /**
     * Example 5: Azure Active Directory MSI (Managed Service Identity)
     * Uses Azure Managed Identity for authentication
     */
    public static Connection connectWithMSI(String serverName, String databaseName) 
            throws SQLException {
        String connectionUrl = String.format(
            "jdbc:sqlserver://%s.database.windows.net:1433;" +
            "databaseName=%s;" +
            "authentication=ActiveDirectoryMSI;" +
            "encrypt=true;" +
            "trustServerCertificate=false;",
            serverName, databaseName
        );
        
        System.out.println("Connecting with Azure AD Managed Service Identity...");
        return DriverManager.getConnection(connectionUrl);
    }

    /**
     * Test the connection and execute a simple query
     */
    public static void testConnection(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String query = "SELECT SYSTEM_USER AS CurrentUser, CURRENT_USER AS DatabaseUser";
            ResultSet rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                System.out.println("Connected successfully!");
                System.out.println("System User: " + rs.getString("CurrentUser"));
                System.out.println("Database User: " + rs.getString("DatabaseUser"));
            }
            
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error testing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main method demonstrating usage
     */
    public static void main(String[] args) {
        // Configuration
        String serverName = "your-server";  // or "your-server.database.windows.net" for Azure
        String databaseName = "your-database";
        
        Connection conn = null;
        
        try {
            // Load the JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Example 1: Windows Integrated Authentication (for on-premises SQL Server)
            // Uncomment to use:
            // conn = connectWithWindowsAuth(serverName, databaseName);
            
            // Example 2: Azure AD Integrated Authentication
            // Uncomment to use:
            // conn = connectWithAzureADIntegrated(serverName, databaseName);
            
            // Example 3: Azure AD Password Authentication
            // Uncomment to use:
            // String username = "user@yourdomain.com";
            // String password = "your-password";
            // conn = connectWithAzureADPassword(serverName, databaseName, username, password);
            
            // Example 4: Azure AD Access Token
            // Uncomment to use:
            // String accessToken = "your-access-token";
            // conn = connectWithAccessToken(serverName, databaseName, accessToken);
            
            // Example 5: Azure AD Managed Service Identity
            // Uncomment to use:
            // conn = connectWithMSI(serverName, databaseName);
            
            // Test the connection
            if (conn != null) {
                testConnection(conn);
            } else {
                System.out.println("Please uncomment one of the connection methods to test.");
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found!");
            System.err.println("Make sure mssql-jdbc jar is in your classpath.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the connection
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
