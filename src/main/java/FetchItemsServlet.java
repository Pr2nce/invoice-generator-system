import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FetchItemsServlet")
public class FetchItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String username = null;
        if (session != null && session.getAttribute("username") != null) {
            username = (String) session.getAttribute("username");
        } else {
            // For guests, we can show a general message or an empty list
            // For this implementation, we will show an empty list
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject",
                    "root", "prince");

            List<Item> items = new ArrayList<>();
            if (username != null) {
                // Fetch items for the logged-in user
                String query = "SELECT * FROM user_items WHERE user_email = ?";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setString(1, username);
                    try (ResultSet resultSet = ps.executeQuery()) {
                        while (resultSet.next()) {
                            Item item = new Item();
                            item.setName(resultSet.getString("itemName"));
                            item.setPrice(resultSet.getDouble("price"));
                            item.setAvailableQuantity(resultSet.getInt("availableQuantity"));
                            items.add(item);
                        }
                    }
                }
            }
            
            request.setAttribute("items", items);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/items.jsp");
            dispatcher.forward(request, response);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}