import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        printer = new CSVPrinter(printStream, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
        outputStream.close();
    }

    @Test
    public void shouldPrintEmptyRecord() throws IOException {
        printer.println();
        printer.flush();
        final String expected = "\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintSingleValue() throws IOException {
        printer.print("Test");
        printer.println();
        printer.flush();
        final String expected = "Test\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintMultipleValues() throws IOException {
        printer.print("Test1");
        printer.print(',');
        printer.print("Test2");
        printer.println();
        printer.flush();
        final String expected = "Test1,Test2\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintQuotedValue() throws IOException {
        printer.setQuotePolicy(Quote.MINIMAL);
        printer.print("Test,Test1,Test\"2\"");
        printer.println();
        printer.flush();
        final String expected = "Test,\"Test,Test1,Test\"2\"\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintEscapedValue() throws IOException {
        printer.setEscapeMode(CSVFormat.ESCAPE_ALL);
        printer.print("Test\n");
        printer.println();
        printer.flush();
        final String expected = "Test\\n\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintComment() throws IOException {
        printer.printComment("Test Comment");
        printer.flush();
        final String expected = "# Test Comment\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintRecords() throws IOException {
        List<String> values = Arrays.asList("Test1", "Test2");
        printer.printRecords(values);
        printer.flush();
        final String expected = "Test1,Test2\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintRecordsWithObjects() throws IOException {
        List<Object> values = Arrays.asList("Test1", 123, 'A');
        printer.printRecords(values);
        printer.flush();
        final String expected = "Test1,123,A\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintRecordsWithIterable() throws IOException {
        List<String> values = Arrays.asList("Test1", "Test2");
        printer.printRecords(Collections.singletonList(values));
        printer.flush();
        final String expected = "Test1,Test2\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintRecordsWithObjectArray() throws IOException {
        Object[] values = new Object[]{"Test1", "Test2"};
        printer.printRecords(values);
        printer.flush();
        final String expected = "Test1,Test2\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void shouldPrintRecordsWithJDBCResultSet() throws IOException, SQLException {
        final String query = "SELECT * FROM test_table";
        // Assuming test_table has the following records: Test1, Test2, Test3; Test4, Test5; Test6, Test7
        printer.printRecords(DatabaseUtils.executeQuery(query));
        printer.flush();
        final String expected = "Test1,Test2\nTest4,Test5\nTest6,Test7\n";
        assertEquals(expected, outputStream.toString());
    }
}