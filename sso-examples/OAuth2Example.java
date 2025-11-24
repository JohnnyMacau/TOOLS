import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

/**
 * OAuth 2.0 SSO Implementation Example
 * 
 * This class demonstrates a basic OAuth 2.0 authentication flow:
 * 1. Authorization Request
 * 2. Authorization Grant
 * 3. Access Token Request
 * 4. Access Protected Resources
 */
public class OAuth2Example {
    
    // OAuth 2.0 Provider Configuration
    private String clientId;
    private String clientSecret;
    private String authorizationEndpoint;
    private String tokenEndpoint;
    private String redirectUri;
    private String scope;
    
    public OAuth2Example(String clientId, String clientSecret, 
                        String authorizationEndpoint, String tokenEndpoint,
                        String redirectUri, String scope) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }
    
    /**
     * Step 1: Generate authorization URL
     */
    public String getAuthorizationUrl() throws UnsupportedEncodingException {
        String state = generateRandomState();
        
        StringBuilder url = new StringBuilder(authorizationEndpoint);
        url.append("?response_type=code");
        url.append("&client_id=").append(URLEncoder.encode(clientId, "UTF-8"));
        url.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
        url.append("&scope=").append(URLEncoder.encode(scope, "UTF-8"));
        url.append("&state=").append(state);
        
        return url.toString();
    }
    
    /**
     * Step 2: Exchange authorization code for access token
     */
    public TokenResponse getAccessToken(String authorizationCode) throws IOException {
        URL url = new URL(tokenEndpoint);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            
            // Prepare POST data
            StringBuilder postData = new StringBuilder();
            postData.append("grant_type=authorization_code");
            postData.append("&code=").append(URLEncoder.encode(authorizationCode, "UTF-8"));
            postData.append("&client_id=").append(URLEncoder.encode(clientId, "UTF-8"));
            postData.append("&client_secret=").append(URLEncoder.encode(clientSecret, "UTF-8"));
            postData.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.toString().getBytes("UTF-8"));
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String response = readResponse(conn.getInputStream());
                return parseTokenResponse(response);
            } else {
                String error = readResponse(conn.getErrorStream());
                throw new IOException("Token request failed: " + error);
            }
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * Step 3: Refresh access token using refresh token
     */
    public TokenResponse refreshAccessToken(String refreshToken) throws IOException {
        URL url = new URL(tokenEndpoint);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            
            // Prepare POST data
            StringBuilder postData = new StringBuilder();
            postData.append("grant_type=refresh_token");
            postData.append("&refresh_token=").append(URLEncoder.encode(refreshToken, "UTF-8"));
            postData.append("&client_id=").append(URLEncoder.encode(clientId, "UTF-8"));
            postData.append("&client_secret=").append(URLEncoder.encode(clientSecret, "UTF-8"));
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.toString().getBytes("UTF-8"));
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String response = readResponse(conn.getInputStream());
                return parseTokenResponse(response);
            } else {
                String error = readResponse(conn.getErrorStream());
                throw new IOException("Token refresh failed: " + error);
            }
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * Step 4: Access protected resource with access token
     */
    public String accessProtectedResource(String resourceUrl, String accessToken) throws IOException {
        URL url = new URL(resourceUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return readResponse(conn.getInputStream());
            } else {
                String error = readResponse(conn.getErrorStream());
                throw new IOException("Resource access failed: " + error);
            }
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * Parse token response
     */
    private TokenResponse parseTokenResponse(String jsonResponse) {
        // Parse JSON response manually (without external JSON library dependency)
        // In production, use a proper JSON library like Gson or Jackson
        
        TokenResponse token = new TokenResponse();
        
        // Simple JSON parsing (for demonstration only)
        String[] parts = jsonResponse.split(",");
        for (String part : parts) {
            if (part.contains("access_token")) {
                token.accessToken = extractValue(part);
            } else if (part.contains("refresh_token")) {
                token.refreshToken = extractValue(part);
            } else if (part.contains("expires_in")) {
                token.expiresIn = Integer.parseInt(extractValue(part));
            } else if (part.contains("token_type")) {
                token.tokenType = extractValue(part);
            }
        }
        
        return token;
    }
    
    /**
     * Extract value from JSON key-value pair
     */
    private String extractValue(String pair) {
        int start = pair.indexOf("\":");
        if (start >= 0) {
            start += 2;
            int end = pair.indexOf("\"", start + 1);
            if (end > start) {
                return pair.substring(start + 1, end);
            }
        }
        return "";
    }
    
    /**
     * Read HTTP response
     */
    private String readResponse(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
    
    /**
     * Generate random state for CSRF protection
     */
    private String generateRandomState() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Token Response class
     */
    public static class TokenResponse {
        public String accessToken;
        public String refreshToken;
        public String tokenType;
        public int expiresIn;
        
        @Override
        public String toString() {
            return String.format("TokenResponse{accessToken='%s...', tokenType='%s', expiresIn=%d}",
                    accessToken != null ? accessToken.substring(0, Math.min(20, accessToken.length())) : "null",
                    tokenType, expiresIn);
        }
    }
    
    /**
     * Example usage
     */
    public static void main(String[] args) {
        // Example configuration for Google OAuth 2.0
        OAuth2Example oauth = new OAuth2Example(
            "YOUR_CLIENT_ID",
            "YOUR_CLIENT_SECRET",
            "https://accounts.google.com/o/oauth2/v2/auth",
            "https://oauth2.googleapis.com/token",
            "http://localhost:8080/oauth2/callback",
            "openid email profile"
        );
        
        try {
            // Step 1: Get authorization URL
            String authUrl = oauth.getAuthorizationUrl();
            System.out.println("Step 1: Visit this URL to authorize:");
            System.out.println(authUrl);
            System.out.println();
            
            // Step 2: After user authorization, you'll receive an authorization code
            // Uncomment and replace with actual code:
            // String authCode = "AUTHORIZATION_CODE_FROM_CALLBACK";
            // TokenResponse token = oauth.getAccessToken(authCode);
            // System.out.println("Step 2: Received access token: " + token);
            
            // Step 3: Use access token to access protected resources
            // String userInfo = oauth.accessProtectedResource(
            //     "https://www.googleapis.com/oauth2/v3/userinfo", 
            //     token.accessToken
            // );
            // System.out.println("Step 3: User info: " + userInfo);
            
        } catch (Exception e) {
            System.err.println("OAuth error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
