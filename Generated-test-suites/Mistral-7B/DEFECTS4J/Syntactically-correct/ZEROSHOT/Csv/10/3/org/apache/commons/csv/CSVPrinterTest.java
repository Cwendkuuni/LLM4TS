package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.CSVPrinter.newInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVPrinter
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() throws Exception {
        outContent = new ByteArrayOutputStream();
        printer = newInstance(new StringWriter(outContent), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        printer.close();
    }

    /**
     * Test method for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)}
     */
    @Test
    public void testCSVPrinter() throws IOException {
        assertNull(printer.getOut());
        assertFalse(printer.isNewRecord());
        printer.flush();
        assertEquals("", outContent.toString());
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)}
     */
    @Test
    public void testPrint_Object() throws IOException {
        printer.print("Test");
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("Test", record.get(0));
    }

    /**
     * Test method for {@link CSVPrinter#print(CharSequence, int, int)}
     */
    @Test
    public void testPrint_CharSequence_int_int() throws IOException {
        printer.print("Test", 0, 4);
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("Test", record.get(0));
    }

    /**
     * Test method for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)}
     *
     * @throws SQLException
     */
    @Test
    public void testPrintAndQuote() throws IOException, SQLException {
        String query = "SELECT '0123ABC' as value from DUMMY";
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = TestUtils.getConnection().createStatement();
            resultSet = statement.executeQuery(query);

            printer.printRecord(resultSet);
            printer.flush();

            CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
            CSVRecord record = parser.getHeader();
            assertEquals("\"0123ABC\"", record.get(0).getRawText());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * Test method for {@link CSVPrinter#printAndEscape(CharSequence, int, int)}
     */
    @Test
    public void testPrintAndEscape() throws IOException {
        printer.printAndEscape("Test\\nTest\\rTest\\\"Test\\\\Test\"");
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("Test\\nTest\\rTest\\\\Test\\\"Test\\\\", record.get(0).getRawText());
    }

    /**
     * Test method for {@link CSVPrinter#printComment(String)}
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("TestComment");
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("# TestComment", record.get(0).getRawText());
    }

    /**
     * Test method for {@link CSVPrinter#println()}
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertTrue(record.isEmpty());
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Iterable)}
     */
    @Test
    public void testPrintRecord_Iterable() throws IOException {
        printer.printRecord(new String[]{"Test1", "Test2"});
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("Test1,Test2", record.getRawText());
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Object...)}
     */
    @Test
    public void testPrintRecord_Object() throws IOException {
        printer.printRecord(new Object[]{"Test1", "Test2"});
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("Test1,Test2", record.getRawText());
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)}
     */
    @Test
    public void testPrintRecords_Iterable() throws IOException {
        printer.printRecords(new String[][]{{"Test1", "Test2"}, {"Test3", "Test4"}});
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("Test1,Test2", record.get(0).getRawText());
        assertEquals("Test3,Test4", record.get(1).getRawText());
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Object[])}
     */
    @Test
    public void testPrintRecords_ObjectArray() throws IOException {
        printer.printRecords(new Object[][]{{"Test1", "Test2"}, {"Test3", "Test4"}});
        printer.flush();
        CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
        CSVRecord record = parser.getHeader();
        assertEquals("Test1,Test2", record.get(0).getRawText());
        assertEquals("Test3,Test4", record.get(1).getRawText());
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(ResultSet)}
     *
     * @throws SQLException
     */
    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        String query = "SELECT 'Test1', 'Test2' as value from DUMMY";
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = TestUtils.getConnection().createStatement();
            resultSet = statement.executeQuery(query);

            printer.printRecords(resultSet);
            printer.flush();

            CSVParser parser = CSVParser.parse(new StringReader(outContent.toString()));
            CSVRecord record = parser.getHeader();
            assertEquals("Test1,Test2", record.get(0).getRawText());
            assertEquals("Test1,Test2", record.get(1).getRawText());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * Test method for {@link CSVPrinter#isQuotingEnabled()}
     */
    @Test
    public void testIsQuotingEnabled() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        CSVPrinter printer = newInstance(new StringWriter(outContent), format);
        printer.printRecord(new String[]{"Test1", "Test2"});
        printer.flush();
        String content = outContent.toString();
        assertTrue(content.contains("\"Test1\""));
        assertTrue(content.contains("\"Test2\""));
    }

    /**
     * Test method for {@link CSVPrinter#isEscapingEnabled()}
     */
    @Test
    public void testIsEscapingEnabled() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withEscape('\u0001').withEscape('\u0002'));
        CSVPrinter printer = newInstance(new StringWriter(outContent), format);
        printer.print("Test\\u0001Test\\u0002");
        printer.flush();
        String content = outContent.toString();
        assertEquals("Test\\u0001Test\\u0002", content);
    }

    /**
     * Test method for {@link CSVPrinter#isCommentingEnabled()}
     */
    @Test
    public void testIsCommentingEnabled() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withComment('%'));
        CSVPrinter printer = newInstance(new StringWriter(outContent), format);
        printer.printComment("TestComment");
        printer.flush();
        String content = outContent.toString();
        assertEquals("% TestComment", content);
    }
}