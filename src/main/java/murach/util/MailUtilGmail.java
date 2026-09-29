package murach.util;

import jakarta.mail.MessagingException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MailUtilGmail {

    public static void sendMail(
            String to,
            String from,
            String subject,
            String body,
            boolean bodyIsHTML) throws MessagingException {

        long startTime = System.currentTimeMillis();
        System.out.println("[BREVO_API] START sendMail");

        String apiKey = System.getenv("BREVO_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new MessagingException("BREVO_API_KEY is not configured");
        }

        try {
            // Escape strings for JSON
            String safeTo = escapeJson(to);
            String safeFrom = escapeJson(from);
            String safeSubject = escapeJson(subject);
            String safeBody = escapeJson(body);

            String contentKey = bodyIsHTML ? "htmlContent" : "textContent";

            String jsonPayload = "{"
                    + "\"sender\": {"
                    + "\"name\": \"Email List\","
                    + "\"email\": \"" + safeFrom + "\""
                    + "},"
                    + "\"to\": ["
                    + "{"
                    + "\"email\": \"" + safeTo + "\","
                    + "\"name\": \"" + safeTo + "\""
                    + "}"
                    + "],"
                    + "\"subject\": \"" + safeSubject + "\","
                    + "\"" + contentKey + "\": \"" + safeBody + "\""
                    + "}";

            System.out.println("[BREVO_API] API request started");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[BREVO_API] API response received, status=" + response.statusCode());

            if (response.statusCode() == 201) {
                String resBody = response.body();
                String messageId = extractMessageId(resBody);
                System.out.println("[BREVO_API] Email accepted by Brevo, messageId=" + messageId);
            } else {
                throw new MessagingException("Brevo API error: status=" + response.statusCode() + ", response=" + response.body());
            }

            System.out.println("[BREVO_API] END sendMail, elapsed=" + (System.currentTimeMillis() - startTime) + " ms");
        } catch (MessagingException e) {
            System.out.println("[BREVO_API] ERROR sendMail: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("[BREVO_API] ERROR sendMail: " + e.getMessage());
            throw new MessagingException("Failed to send email via Brevo REST API", e);
        }
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String extractMessageId(String json) {
        if (json == null) return "";
        int idx = json.indexOf("\"messageId\"");
        if (idx != -1) {
            int start = json.indexOf("\"", idx + 11);
            if (start != -1) {
                int end = json.indexOf("\"", start + 1);
                if (end != -1) {
                    return json.substring(start + 1, end);
                }
            }
        }
        return "unknown";
    }
}