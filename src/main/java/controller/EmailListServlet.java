
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
            if (UserDB_1.emailExists(user.getEmail())) {

                message = "This email address already exists.<br>"
                        + "Please enter another email address.";

                url = "/index.jsp";
            }

            else {

                // Insert user into database
                UserDB_1.insert(user);

                // Send confirmation email
                try {

                    MailUtilGmail.sendMail(
                            user.getEmail(),
                            System.getenv("GMAIL_USERNAME"),
                            "Welcome to our Email List",
                            "<h1>Welcome " + user.getFirstName() + "!</h1>"
                                    + "<p>Thank you for joining our email list.</p>"
                                    + "<p>Your email address is: "
                                    + user.getEmail()
                                    + "</p>",
                            true
                    );

                    message = "Registration successful. "
                            + "A confirmation email has been sent.";

                } catch (MessagingException e) {

                    message = "Registration successful, "
                            + "but the confirmation email could not be sent.";

                    e.printStackTrace();
                }

                url = "/thanks.jsp";
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
