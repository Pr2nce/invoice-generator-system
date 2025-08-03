import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

@WebServlet("/FetchItemsServlet")
public class FetchItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject",
                    "root", "prince");

            // Fetch items from the database
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM items");

            // Create a list to hold Item objects
            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                Item item = new Item();
                item.setName(resultSet.getString("itemName"));
                item.setPrice(resultSet.getDouble("price"));
                item.setAvailableQuantity(resultSet.getInt("availableQuantity"));
                items.add(item);
            }

            // Set the list of items and other parameters as request attributes
            request.setAttribute("items", items);
            
            // Forward the request to your new JSP page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/items.jsp");
            dispatcher.forward(request, response);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}