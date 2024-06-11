import org.apache.commons.csv.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withHeader("Header1", "Header2"));
        printer = new CSVPrinter(new PrintStream(outputStream), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Value1");
        printer.print("Value2");
        printer.println();

        String expected = "Value1,Value2\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintQuoted() throws IOException {
        printer.printQuoted("\"Value1\"");
        printer.printQuoted("\"Value2\"");
        printer.println();

        String expected = "\"Value1\",\"Value2\"\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintQuotePolicy() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withHeader("Header1", "Header2")).withQuotePolicy(CSVFormat.QUOTE_MINIMAL);
        CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        printer.print("Value1");
        printer.print("Value2");
        printer.println();

        String expected = "Value1,Value2\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value with space");
        printer.println();

        expected = "\"Value with space\",\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value1,\"Value2\"");
        printer.println();

        expected = "Value1,\"Value2\"\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value1,\"Value with space\"");
        printer.println();

        expected = "Value1,\"Value with space\"\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value with space,\"Value2\"");
        printer.println();

        expected = "\"Value with space\",Value2\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value1,\"Value with space,newline\"");
        printer.println();

        expected = "Value1,\"Value with space,newline\"\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value1,\"Value with space,newline\"");
        printer.println();

        expected = "Value1,\"Value with space,newline\"\nValue1,\"Value with space,newline\"\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value1,\"Value1,newline\"");
        printer.println();

        // Test for null values
        printer.print(null);
        printer.print("Value2");
        printer.println();

        expected = "null,Value2\n";
        Assert.assertEquals(expected, outputStream.toString());

        printer.print("Value1");
        printer.print(null);
        printer.println();

        expected = "Value1,null\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new Object[]{"Value1", "Value2"});
        printer.println();

        String expected = "Value1,Value2\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String[][] data = new String[][]{
                {"1", "John Doe"},
                {"2", "Jane Doe"},
                {"3", "Bob Smith"}
        };

        printer.printRecords(data);

        String expected = "1,John Doe\n2,Jane Doe\n3,Bob Smith\n";
        Assert.assertEquals(expected, outputStream.toString());

        ResultSet resultSet = TestUtils.createMockResultSet(data);
        printer.printRecords(resultSet);

        expected = "1,John Doe\n2,Jane Doe\n3,Bob Smith\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();

        String expected = "\nThis is a comment\n";
        Assert.assertEquals(expected, outputStream.toString());
    }
}