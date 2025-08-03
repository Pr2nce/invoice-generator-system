import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PlaceOrderServlet")
public class PlaceOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"invoice.pdf\"");

        String[] selectedItems = request.getParameterValues("item");
        String customerName = request.getParameter("customerName");
        String phoneNumber = request.getParameter("phoneNumber");

        double taxRate = 0.0;
        double discountRate = 0.0;
        try {
            taxRate = Double.parseDouble(request.getParameter("taxRate"));
            discountRate = Double.parseDouble(request.getParameter("discountRate"));
        } catch (NumberFormatException | NullPointerException e) {
            // Handle cases where rates are not provided or are invalid
            System.err.println("Tax or discount rate invalid. Using default 0. " + e.getMessage());
        }

        if (selectedItems == null || selectedItems.length == 0) {
            response.setContentType("text/html");
            response.getWriter().println("<h1>No items selected.</h1>");
            return;
        }

        double subtotal = 0.0;
        UUID transactionId = UUID.randomUUID();
        DecimalFormat df = new DecimalFormat("0.00");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "prince")) {
            // Create order and customer tables if they don't exist
            createOrderTable(connection);
            createCustomerInfoTable(connection);

            // Save customer information
            saveCustomerInfo(connection, transactionId.toString(), customerName, phoneNumber);

            // Calculate subtotal and save order data
            for (String itemName : selectedItems) {
                int quantity = Integer.parseInt(request.getParameter("quantity_" + itemName));
                double price = getPrice(connection, itemName);
                double itemTotal = price * quantity;
                subtotal += itemTotal;
                saveOrderData(connection, itemName, price, quantity, transactionId.toString());
            }

            double taxAmount = (subtotal * taxRate) / 100;
            double discountAmount = (subtotal * discountRate) / 100;
            double finalTotal = subtotal + taxAmount - discountAmount;

            // PDF Generation
            OutputStream out = response.getOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Add content to the PDF
            document.add(new Paragraph("Invoice"));
            document.add(new Paragraph("Invoice Number: " + transactionId.toString()));
            document.add(new Paragraph("Date: " + new Date().toString()));
            document.add(new Paragraph("------------------------------------"));
            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("Phone Number: " + phoneNumber));
            document.add(new Paragraph("------------------------------------"));

            PdfPTable table = new PdfPTable(3);
            table.addCell("Item");
            table.addCell("Quantity");
            table.addCell("Total Price");

            for (String itemName : selectedItems) {
                int quantity = Integer.parseInt(request.getParameter("quantity_" + itemName));
                double price = getPrice(connection, itemName);
                double itemTotal = price * quantity;

                table.addCell(itemName);
                table.addCell(String.valueOf(quantity));
                table.addCell("$" + df.format(itemTotal));
            }

            document.add(table);
            document.add(new Paragraph("------------------------------------"));
            document.add(new Paragraph("Subtotal: $" + df.format(subtotal)));
            document.add(new Paragraph("Tax (" + df.format(taxRate) + "%): $" + df.format(taxAmount)));
            document.add(new Paragraph("Discount (" + df.format(discountRate) + "%): $" + df.format(discountAmount)));
            document.add(new Paragraph("Final Total: $" + df.format(finalTotal)));
            document.add(new Paragraph("------------------------------------"));

            document.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("<h1>An error occurred: " + e.getMessage() + "</h1>");
        }
    }
    
    private double getPrice(Connection connection, String itemName) throws SQLException {
        String query = "SELECT price FROM items WHERE itemName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, itemName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("price");
                }
            }
        }
        return 0.0;
    }

    private void saveOrderData(Connection connection, String itemName, double price, int quantity, String transactionId) throws SQLException {
        String insertOrderQuery = "INSERT INTO `order` (item, price, quantity, transaction_id) VALUES (?, ?, ?, ?)";
        String updateQuantityQuery = "UPDATE items SET availableQuantity = availableQuantity - ? WHERE itemName = ?";

        try (PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderQuery);
             PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantityQuery)) {

            insertOrderStatement.setString(1, itemName);
            insertOrderStatement.setDouble(2, price);
            insertOrderStatement.setInt(3, quantity);
            insertOrderStatement.setString(4, transactionId);
            insertOrderStatement.executeUpdate();

            updateQuantityStatement.setInt(1, quantity);
            updateQuantityStatement.setString(2, itemName);
            updateQuantityStatement.executeUpdate();
        }
    }

    private void saveCustomerInfo(Connection connection, String transactionId, String customerName, String phoneNumber) throws SQLException {
        String insertCustomerInfoQuery = "INSERT INTO `order_customer_info` (transaction_id, customer_name, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement insertCustomerInfoStatement = connection.prepareStatement(insertCustomerInfoQuery)) {
            insertCustomerInfoStatement.setString(1, transactionId);
            insertCustomerInfoStatement.setString(2, customerName);
            insertCustomerInfoStatement.setString(3, phoneNumber);
            insertCustomerInfoStatement.executeUpdate();
        }
    }

    private void createOrderTable(Connection connection) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS `order` ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "item VARCHAR(255) NOT NULL, "
                + "price DOUBLE NOT NULL, "
                + "quantity INT NOT NULL, "
                + "transaction_id VARCHAR(36) NOT NULL)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }

    private void createCustomerInfoTable(Connection connection) throws SQLException {
        String createCustomerInfoTableQuery = "CREATE TABLE IF NOT EXISTS `order_customer_info` ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "transaction_id VARCHAR(36) NOT NULL, "
                + "customer_name VARCHAR(255) NOT NULL, "
                + "phone_number VARCHAR(15) NOT NULL)";
        try (PreparedStatement customerInfoStatement = connection.prepareStatement(createCustomerInfoTableQuery)) {
            customerInfoStatement.executeUpdate();
        }
    }
}