import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outputStream), DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        printer.close();
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("Test");
        printer.flush();
        String result = outputStream.toString();
        assertEquals("Test", result.split(System.lineSeparator().replaceAll("\\s+", ""))[0]);
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print("Test 1");
        printer.print("Test 2");
        printer.println();
        printer.print("Test 3");
        printer.print("Test 4");
        printer.println();
        printer.flush();
        String result = outputStream.toString();

        String[] expected = {"Test 1", "Test 2", "", "", "Test 3", "Test 4", ""};
        assertEquals(expected.length, result.split(System.lineSeparator().replaceAll("\\s+", "")).length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.split(System.lineSeparator().replaceAll("\\s+", ""))[i]);
        }
    }

    @Test
    public void testPrintQuotedValues() throws IOException {
        printer.printQuotePolicy(Quote.ALWAYS);
        printer.print("\"Test\"");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("\"Test\"", result.split(System.lineSeparator().replaceAll("\\s+", ""))[0]);
    }

    @Test
    public void testPrintEscapedValues() throws IOException {
        printer.printEscape("\\\"");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("\"", result.split(System.lineSeparator().replaceAll("\\s+", ""))[0]);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("# This is a comment", result.split(System.lineSeparator().replaceAll("\\s+", ""))[0]);
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new String[]{"Test 1", "Test 2"});
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        String[] expected = {"Test 1", "Test 2", ""};
        assertEquals(expected.length, result.split(System.lineSeparator().replaceAll("\\s+", "")).length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.split(System.lineSeparator().replaceAll("\\s+", ""))[i]);
        }
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        try (Statement statement = new java.sql.Statement(new org.h2.Driver().connect(url, null))){
            statement.execute("CREATE TABLE TEST (ID INT PRIMARY KEY, NAME VARCHAR(255))");
            statement.execute("INSERT INTO TEST VALUES (1, 'Test 1'), (2, 'Test 2'), (3, 'Test 3')");

            printer.printRecords(statement.executeQuery("SELECT * FROM TEST"));
            printer.close();
            String result = outputStream.toString();

            String[] expected = {"ID,NAME", "", "", "1,Test 1", "", "", "2,Test 2", "", "", "3,Test 3", "", ""};
            assertEquals(expected.length, result.split(System.lineSeparator().replaceAll("\\s+", "")).length);
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], result.split(System.lineSeparator().replaceAll("\\s+", ""))[i]);
            }
        }
    }

    @Test
    public void testNewRecordAfterPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();
        printer.print("Test");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        String[] expected = {"# This is a comment", "", "Test", ""};
        assertEquals(expected.length, result.split(System.lineSeparator().replaceAll("\\s+", "")).length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.split(System.lineSeparator().replaceAll("\\s+", ""))[i]);
        }
    }

    @Test
    public void testHasNewRecordAfterPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();
        boolean newRecord = printer.hasNewRecord();
        printer.print("Test");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        String[] expected = {"# This is a comment", "", "Test", ""};
        assertEquals(expected.length, result.split(System.lineSeparator().replaceAll("\\s+", "")).length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.split(System.lineSeparator().replaceAll("\\s+", ""))[i]);
        }
        assertFalse(newRecord);
    }

    @Test
    public void testHasNewRecordAfterPrintRecord() throws IOException {
        printer.printRecord(new String[]{"Test 1", "Test 2"});
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        String[] expected = {"Test 1", "Test 2", ""};
        assertEquals(expected.length, result.split(System.lineSeparator().replaceAll("\\s+", "")).length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.split(System.lineSeparator().replaceAll("\\s+", ""))[i]);
        }
        assertTrue(printer.hasNewRecord());
    }
}