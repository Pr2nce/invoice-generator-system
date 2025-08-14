import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UpdateItemsServlet")
public class UpdateItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject",
                    "root", "prince");

            String action = request.getParameter("action");
            String itemName = request.getParameter("itemName");

            if ("add".equals(action)) {
                double price = Double.parseDouble(request.getParameter("price"));
                int availableQuantity = Integer.parseInt(request.getParameter("availableQuantity"));
                addNewItem(connection, username, itemName, price, availableQuantity);
            } else if ("update".equals(action)) {
                double price = Double.parseDouble(request.getParameter("price"));
                int availableQuantity = Integer.parseInt(request.getParameter("availableQuantity"));
                updateExistingItem(connection, username, itemName, price, availableQuantity);
            } else if ("delete".equals(action)) {
                deleteExistingItem(connection, username, itemName);
            }
            
            connection.close();
            response.sendRedirect("FetchItemsServlet");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        }
    }

    private void addNewItem(Connection connection, String userEmail, String itemName, double price, int availableQuantity) throws SQLException {
        String query = "INSERT INTO user_items (user_email, itemName, price, availableQuantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userEmail);
            statement.setString(2, itemName);
            statement.setDouble(3, price);
            statement.setInt(4, availableQuantity);
            statement.executeUpdate();
        }
    }

    private void updateExistingItem(Connection connection, String userEmail, String itemName, double price, int availableQuantity) throws SQLException {
        String query = "UPDATE user_items SET price = ?, availableQuantity = ? WHERE user_email = ? AND itemName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, price);
            statement.setInt(2, availableQuantity);
            statement.setString(3, userEmail);
            statement.setString(4, itemName);
            statement.executeUpdate();
        }
    }

    private void deleteExistingItem(Connection connection, String userEmail, String itemName) throws SQLException {
        String query = "DELETE FROM user_items WHERE user_email = ? AND itemName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userEmail);
            statement.setString(2, itemName);
            statement.executeUpdate();
        }
    }
}