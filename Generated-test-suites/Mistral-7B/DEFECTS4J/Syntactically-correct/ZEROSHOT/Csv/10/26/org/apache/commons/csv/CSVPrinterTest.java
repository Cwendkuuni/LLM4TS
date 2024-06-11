import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        // Test constructor
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL));
        CSVPrinter printer = new CSVPrinter(outputStream, format);

        // Test print methods
        printer.print("Hello");
        printer.print(123);
        printer.print(true);
        printer.print(null);
        printer.printRecord(new String[]{"Hello", "World", "123", null});
        printer.printComment("This is a comment");
        printer.println();
        printer.printRecord(new String[]{"Hello", "World"});
        printer.flush();

        String expectedOutput = "\"Hello\"123true\n" +
                "\"Hello\",\"World\",123,null\n" +
                "# This is a comment\n";
        Assert.assertEquals(expectedOutput, outputStream.toString());

        // Test print methods with SQL ResultSet
        ResultSet resultSet = ResultSetCreator.newInstance().sqlQuery("SELECT 'Hello', 123, true, null").resultSet();
        printer = new CSVPrinter(outputStream, format);
        printer.printRecords(resultSet);
        printer.flush();

        expectedOutput = "\"Hello\"123true\n" +
                "\"Hello\",\"World\",123,null\n";
        Assert.assertEquals(expectedOutput, outputStream.toString());

        // Test close and flush methods
        printer.close();
        printer = new CSVPrinter(outputStream, format);
        printer.print("Hello");
        printer.flush();
        printer.close();
        Assert.assertEquals(expectedOutput, outputStream.toString());
        Assert.assertNull(outputStream.toString());
    }
}