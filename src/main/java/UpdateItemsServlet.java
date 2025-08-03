
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

@WebServlet("/UpdateItemsServlet")
public class UpdateItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head>");
        out.println("<style>");
        out.println("body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }");
        out.println("h2 { color: #333; }");
        out.println("h3 { color: #333; margin-top: 20px; }");
        out.println("ul { list-style-type: none; padding: 0; }");
        out.println("li { border-bottom: 1px solid #ddd; margin-bottom: 10px; padding-bottom: 10px; }");
        out.println("form { margin-top: 20px; }");
        out.println("input[type='radio'], input[type='text'], input[type='number'] { margin-bottom: 10px; }");
        out.println("input[type='submit'] { background-color: #4caf50; color: white; padding: 10px 15px; cursor: pointer; border: none; }");
        out.println("</style>");
        out.println("</head><body>");

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject",
                    "root", "prince");

            // Fetch items from the database
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM items");

            // Display items in HTML
            out.println("<h2>Update Items</h2>");

            // Display current items
            out.println("<h3>Current Items:</h3>");
            out.println("<ul>");
            while (resultSet.next()) {
                String itemName = resultSet.getString("itemName");
                double price = resultSet.getDouble("price");
                int availableQuantity = resultSet.getInt("availableQuantity");

                out.println("<li>" + itemName + " - $" + price + " - Available Quantity: " + availableQuantity + "</li>");
            }
            out.println("</ul>");

            // Provide choices to the user
            out.println("<h3>Choose an action:</h3>");
            out.println("<form action='UpdateItemsServlet' method='post'>"
                    + "<input type='radio' name='action' value='update' checked> Update Existing Item<br>"
                    + "<input type='radio' name='action' value='delete'> Delete Existing Item<br>"
                    + "<input type='radio' name='action' value='add'> Add New Item<br>"
                    + "Item Name: <input type='text' name='itemName' required><br>"
                    + "Price: <input type='number' name='price' min='0' step='0.01' required><br>"
                    + "Available Quantity: <input type='number' name='availableQuantity' min='0' required><br>"
                    + "<input type='submit' value='Submit'>"
                    + "</form>");
            out.println("<form action='FetchItemsServlet'>"
                    + "<input type='submit' value='Back to Available Items'>"
                    + "</form>");
            

            out.println("</body></html>");

            // Close the connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject",
                    "root", "prince");

            // Retrieve form data
            String action = request.getParameter("action");
            String itemName = request.getParameter("itemName");
            double price = Double.parseDouble(request.getParameter("price"));
            int availableQuantity = Integer.parseInt(request.getParameter("availableQuantity"));

            // Perform the selected action
            switch (action) {
                case "update":
                    // Check if the item exists before updating
                    if (checkItemExists(connection, itemName)) {
                        updateExistingItem(connection, itemName, price, availableQuantity);
                        out.println("Item '" + itemName + "' has been updated.");
                    } else {
                        out.println("Item '" + itemName + "' does not exist. Please choose a valid item.");
                    }
                    break;
                case "delete":
                    // Check if the item exists before deleting
                    if (checkItemExists(connection, itemName)) {
                        deleteExistingItem(connection, itemName);
                        out.println("Item '" + itemName + "' has been deleted.");
                    } else {
                        out.println("Item '" + itemName + "' does not exist. Please choose a valid item.");
                    }
                    break;
                case "add":
                    // Directly add a new item without checking for existence
                    addNewItem(connection, itemName, price, availableQuantity);
                    out.println("Item '" + itemName + "' has been added.");
                    break;
                default:
                    out.println("Invalid action.");
            }

            // Close the connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkItemExists(Connection connection, String itemName) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM items WHERE itemName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, itemName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("count") > 0;
            }
        }
    }

    private void updateExistingItem(Connection connection, String itemName, double price, int availableQuantity) throws SQLException {
        String query = "UPDATE items SET price = ?, availableQuantity = ? WHERE itemName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, price);
            statement.setInt(2, availableQuantity);
            statement.setString(3, itemName);
            statement.executeUpdate();
        }
    }

    private void deleteExistingItem(Connection connection, String itemName) throws SQLException {
        String query = "DELETE FROM items WHERE itemName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, itemName);
            statement.executeUpdate();
        }
    }

    private void addNewItem(Connection connection, String itemName, double price, int availableQuantity) throws SQLException {
        String query = "INSERT INTO items (itemName, price, availableQuantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, itemName);
            statement.setDouble(2, price);
            statement.setInt(3, availableQuantity);

            System.out.println("SQL Query: " + statement.toString()); // Print the SQL query

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it for further investigation
        }
    }
}
