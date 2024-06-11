import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws SQLException, IOException {
        // Testing printRecord method
        CSVFormat format = new CSVFormat();
        format.setRecordSeparator("\n");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);

        Object[] record = {"John Doe", 25, "jdoe@example.com"};
        printer.printRecord(record);
        String expectedOutput = "\"John Doe\",25,\"jdoe@example.com\"\n";
        Assert.assertEquals(expectedOutput, outputStream.toString());

        // Testing printComment method
        printer.printComment("This is a comment");
        String expectedCommentOutput = "// This is a comment\n";
        Assert.assertEquals(expectedCommentOutput, outputStream.toString());

        // Testing println method
        printer.println();
        expectedOutput += "\"FirstName\",age,\"email\"\n";
        Assert.assertEquals(expectedOutput, outputStream.toString());

        // Testing print method for String
        printer.print("Hello");
        expectedOutput += "\"Hello\"";
        Assert.assertEquals(expectedOutput, outputStream.toString());

        // Testing print method for Integer
        printer.print(123);
        expectedOutput += ",123";
        Assert.assertEquals(expectedOutput, outputStream.toString());

        // Testing print method for special characters
        printer.print("\n");
        expectedOutput += "\"\\n\"";
        Assert.assertEquals(expectedOutput, outputStream.toString());

        printer.close();

        // Testing printRecords method with Iterable
        format = new CSVFormat();
        format.setRecordSeparator("\n");
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);

        Iterable<Object[]> records = Arrays.asList(new Object[][]{{"Alice", 30, "alice@example.com"}, {"Bob", 25, "bob@example.com"}});
        printer.printRecords(records);
        String expectedRecordsOutput = "\"Alice\",30,\"alice@example.com\"\n\"Bob\",25,\"bob@example.com\"\n";
        Assert.assertEquals(expectedRecordsOutput, outputStream.toString());

        // Testing printRecords method with ResultSet
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String driver = "org.h2.Driver";
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE mytable (name VARCHAR(50), age INT, email VARCHAR(50))";
            statement.executeUpdate(sql);
            statement.executeUpdate("INSERT INTO mytable VALUES ('Alice', 30, 'alice@example.com')");
            statement.executeUpdate("INSERT INTO mytable VALUES ('Bob', 25, 'bob@example.com')");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM mytable");
            printer.printRecords(resultSet);
            expectedRecordsOutput = "\"Alice\",30,\"alice@example.com\"\n\"Bob\",25,\"bob@example.com\"\n";
            Assert.assertEquals(expectedRecordsOutput, outputStream.toString());
        }

        printer.close();
    }
}