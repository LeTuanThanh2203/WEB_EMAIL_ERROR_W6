
package controller;

import java.io.IOException;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.util.MailUtilGmail;
import murach.business.User;
import murach.data.UserDB_1;
//import murach.util.MailUtilLocal;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[EMAIL_DEBUG] START REQUEST");
        String url = "/index.html";
        String message = "";

        // Get current action
        String action = request.getParameter("action");

        if (action == null) {
            action = "join";
        }

        // Join page
        if (action.equals("join")) {
            url = "/index.jsp";
        }

        // Add new user
        else if (action.equals("add")) {

            // Get parameters from request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // Create User object
            User user = new User(firstName, lastName, email);

            // Check if email already exists
            System.out.println("[EMAIL_DEBUG] BEFORE emailExists");
            boolean exists = UserDB_1.emailExists(user.getEmail());
            System.out.println("[EMAIL_DEBUG] AFTER emailExists: " + exists);
            if (exists) {

                message = "This email address already exists.<br>"
                        + "Please enter another email address.";

                url = "/index.jsp";
            }

            else {

                // Insert user into database
                System.out.println("[EMAIL_DEBUG] BEFORE INSERT");
                int rowAffected = UserDB_1.insert(user);
                System.out.println("[EMAIL_DEBUG] AFTER INSERT: " + rowAffected);

                if (rowAffected > 0) {
                    // Send confirmation email
                    try {

                        String fromEmail = System.getenv("SENDER_EMAIL");
                        if (fromEmail == null || fromEmail.isEmpty()) {
                            fromEmail = "tuanthanhlvq413@gmail.com"; // Email đã verify trên Brevo Senders
                        }
                        long beforeMail = System.currentTimeMillis();
                        System.out.println("[EMAIL_DEBUG] BEFORE SEND MAIL");
                        MailUtilGmail.sendMail(
                                user.getEmail(),
                                fromEmail,
                                "Welcome to our Email List",
                                "<h1>Welcome " + user.getFirstName() + "!</h1>",
                                true
                        );
                        System.out.println("[EMAIL_DEBUG] AFTER SEND MAIL - elapsed=" + (System.currentTimeMillis() - beforeMail) + " ms");

                        message = "Registration successful. "
                                + "A confirmation email has been sent.";

                    } catch (MessagingException e) {
                        System.out.println("[EMAIL_DEBUG] ERROR: " + e.getMessage());
                        message = "Registration successful, "
                                + "but the confirmation email could not be sent.";

                        e.printStackTrace();
                    }

                    url = "/thanks.jsp";
                } else {
                    message = "Error occurred while saving data. Registration failed.";
                    url = "/index.jsp";
                }
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        System.out.println("[EMAIL_DEBUG] END REQUEST");
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
