import org.apache.commons.csv.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CSVPrinterTest {

    private CSVFormat csvFormat;
    private CSVPrinter csvPrinter;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        csvFormat = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
        outputStream = new ByteArrayOutputStream();
        csvPrinter = new CSVPrinter(new StringWriter(outputStream), csvFormat);
    }

    @After
    public void tearDown() throws Exception {
        csvPrinter.close();
    }

    @Test
    public void testPrintString() throws IOException {
        csvPrinter.print("John Doe");
        csvPrinter.println();
        assertEquals("Name,Age\nJohn Doe\n", outputStream.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        csvPrinter.println();
        assertEquals("Name,Age\n,Age\n", outputStream.toString());
    }

    @Test
    public void testPrintCharSequence() throws IOException {
        csvPrinter.print("John Doe");
        csvPrinter.println();
        csvPrinter.print("Apache Commons CSV");
        csvPrinter.println();
        assertEquals("Name,Age\nJohn Doe\nName,Age\nApache Commons CSV\n", outputStream.toString());
    }

    @Test
    public void testPrintCharSequenceWithOffsetAndLength() throws IOException {
        String testString = "Apache Commons CSV";
        csvPrinter.print("Apache Commons CSV");
        csvPrinter.println();
        csvPrinter.print(testString, 0, 6);
        csvPrinter.println();
        csvPrinter.print(testString, 6, testString.length());
        csvPrinter.println();
        assertEquals("Name,Age\nApache Commons CSV\nName,Age\nCSV\n", outputStream.toString());
    }

    @Test
    public void testPrintQuotedString() throws IOException {
        csvPrinter.printQuoteMode(QuoteMode.QUOTES_MINIMAL);
        csvPrinter.print("John, \"Do\\\"e\"");
        csvPrinter.println();
        assertEquals("Name,Age\nJohn,\"Do\"e\"\n", outputStream.toString());
    }

    @Test
    public void testPrintQuotedStringWithEscape() throws IOException {
        csvPrinter.printQuoteMode(QuoteMode.QUOTES_ALL);
        csvPrinter.print("John, \"Do\\\"e\\\"e\"");
        csvPrinter.println();
        assertEquals("Name,Age\nJohn,\"Do\"\"e\"\"e\"\n", outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment.");
        csvPrinter.println();
        assertEquals("Name,Age\n# This is a comment.\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Name", "John Doe");
        csvPrinter.println();
        assertEquals("Name,Age\nName,John Doe\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordWithNullValue() throws IOException {
        csvPrinter.printRecord("Name", "John Doe", null);
        csvPrinter.println();
        assertEquals("Name,Age\nName,John Doe,\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordWithObjectArray() throws IOException {
        csvPrinter.printRecord("Name", new Object[]{"John", "Doe"});
        csvPrinter.println();
        assertEquals("Name,Age\nName,John\nName,Doe\n", outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        String[] records = {"John Doe", "Jane Doe", "Bob Smith"};
        csvPrinter.printRecords(records);
        csvPrinter.println();
        assertEquals("Name,Age\nName,John Doe\nName,Jane Doe\nName,Bob Smith\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        Iterable<String> records = () -> new String[]{"John Doe", "Jane Doe", "Bob Smith"};
        csvPrinter.printRecords(records);
        csvPrinter.println();
        assertEquals("Name,Age\nName,John Doe\nName,Jane Doe\nName,Bob Smith\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        String[][] records = {{"John Doe", "Age: 25"}, {"Jane Doe", "Age: 30"}, {"Bob Smith", "Age: 35"}};
        csvPrinter.printRecords(records);
        csvPrinter.println();
        assertEquals("Name,Age\nName,John Doe\nAge:,25\nName,Jane Doe\nAge:,30\nName,Bob Smith\nAge:,35\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws SQLException, IOException {
        String url = "jdbc:h2:mem:testdb";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE test (name VARCHAR(50), age INTEGER)");
            statement.executeUpdate("INSERT INTO test VALUES ('John Doe', 25), ('Jane Doe', 30), ('Bob Smith', 35)");

            csvPrinter.printRecords(statement.getResultSet());
            csvPrinter.println();

            statement.close();
            connection.close();
        }
        assertEquals("Name,Age\nName,John Doe\nAge:,25\nName,Jane Doe\nAge:,30\nName,Bob Smith\nAge:,35\n", outputStream.toString());
    }
}