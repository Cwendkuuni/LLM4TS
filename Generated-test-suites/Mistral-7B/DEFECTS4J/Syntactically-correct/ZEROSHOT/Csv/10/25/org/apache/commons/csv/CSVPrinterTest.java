import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        // Create test data
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(outStream, format);

        // Test print methods
        printer.print(123);
        printer.print(true);
        printer.print("Hello World");
        printer.printRecord(1, 2.3, "Three");
        printer.printRecord(new Object[]{1, 2.3, "Three"});
        printer.printRecords(new Object[][]{
                {1, 2.3, "Three"},
                {4, 5.6, "Six"}
        });

        // Test printComment method
        printer.printComment("This is a comment");

        // Test println and printRecord methods
        printer.println();
        printer.printRecord("Record 1", "Record 2");
        printer.println();

        // Test printComment and printRecord methods
        printer.printComment("Comment 1");
        printer.printRecord("Record 1", "Record 2");
        printer.println();

        // Test printComment and println methods
        printer.printComment("Comment 1");
        printer.println();

        // Test printComment, println, and printRecord methods
        printer.printComment("Comment 1");
        printer.println();
        printer.printRecord("Record 1", "Record 2");

        // Test close method
        printer.close();

        // Verify output
        String expectedOutput = "123\ntrue\nHello World\n1,2.3,Three\n1,2.3,Three\n1,2.3,Three\n4,5.6,Six\n\nRecord 1,Record 2\n\nComment 1\nRecord 1,Record 2\n\nComment 1\nRecord 1,Record 2\n";
        String actualOutput = outStream.toString();
        Assert.assertEquals(expectedOutput, actualOutput);

        // Test printRecords method using JDBC result set
        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        String query = "SELECT id, name, age FROM TEST WHERE id IN (1, 2)";
        String driver = "org.h2.Driver";

        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            printer = new CSVPrinter(outStream, format);
            printer.printRecords(resultSet);
            printer.close();

            expectedOutput = "1,name,age\n1,John Doe,30\n2,Jane Doe,25\n";
            actualOutput = outStream.toString();
            Assert.assertEquals(expectedOutput, actualOutput);
        }
    }
}