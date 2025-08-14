import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FetchUserHistoryServlet")
public class FetchUserHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            // User is not logged in, redirect to login page
            response.sendRedirect("Login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "prince");

            // Fetch transaction IDs for the logged-in user
            String getTransactionIdsQuery = "SELECT transaction_id FROM order_customer_info WHERE customer_name = ?";
            List<String> transactionIds = new ArrayList<>();
            try (PreparedStatement ps = connection.prepareStatement(getTransactionIdsQuery)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        transactionIds.add(rs.getString("transaction_id"));
                    }
                }
            }

            List<OrderDetails> userOrders = new ArrayList<>();

            if (!transactionIds.isEmpty()) {
                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < transactionIds.size(); i++) {
                    placeholders.append("?");
                    if (i < transactionIds.size() - 1) {
                        placeholders.append(",");
                    }
                }
                String finalQuery = "SELECT * FROM `order` WHERE transaction_id IN (" + placeholders.toString() + ")";

                try (PreparedStatement ps = connection.prepareStatement(finalQuery)) {
                    for (int i = 0; i < transactionIds.size(); i++) {
                        ps.setString(i + 1, transactionIds.get(i));
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            OrderDetails order = new OrderDetails();
                            order.setItem(rs.getString("item"));
                            order.setPrice(rs.getDouble("price"));
                            order.setQuantity(rs.getInt("quantity"));
                            order.setTransactionId(rs.getString("transaction_id"));
                            userOrders.add(order);
                        }
                    }
                }
            }

            request.setAttribute("userOrders", userOrders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/history.jsp");
            dispatcher.forward(request, response);
            
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("An error occurred while fetching your history: " + e.getMessage());
        }
    }
}