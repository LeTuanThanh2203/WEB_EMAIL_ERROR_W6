package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import murach.data.SQLGatewayDB;

import java.io.IOException;

@WebServlet("/sqlGateway")
public class SQLGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement =
                request.getParameter("sqlStatement");

        if (sqlStatement == null ||
                sqlStatement.trim().isEmpty()) {

            sqlStatement = "SELECT * FROM users";
        }

        String sqlResult = SQLGatewayDB.executeSQL(sqlStatement);

        HttpSession session =
                request.getSession();

        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        getServletContext()
                .getRequestDispatcher("/sql.jsp")
                .forward(request, response);
    }
}