package org.apache.commons.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the CSVPrinter class.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    /**
     * Test printing a single string value to ensure proper quoting and escaping.
     */
    @Test
    public void testPrintString() throws IOException {
        printer.print("a\"b'c\\e\f\"r\n\\t\\n\"\n");
        printer.flush();
        assertEquals("\"a\"\"b\'c\\\\e\\\\f\\\"r\\\\n\\\\t\\\\n\"\\n", out.toString());
    }

    /**
     * Test printing multiple string values to ensure proper quoting and escaping for all values, as well as record
     * separators.
     */
    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecord(new Object[]{"a\"b'c\\e\f\"r\n\\t\\n\"\n", "d\"e'f\\e[z\\x12\\r]\\\n\\\"", "g"});
        printer.flush();
        assertEquals("\"a\"\"b\'c\\\\e\\\\f\\\"r\\\\n\\\\t\\\\n\"\"d\"\"e\'f\\\\e[z\\\\x12\\\\r]\\\\n\\\"g\",", out.toString());
    }

    /**
     * Test printing a null value to ensure proper handling.
     */
    @Test
    public void testPrintNull() throws IOException {
        printer.printRecord(new Object[]{null});
        printer.flush();
        assertEquals("\",", out.toString());
    }

    /**
     * Test printing a single integer value to ensure proper handling.
     */
    @Test
    public void testPrintInt() throws IOException {
        printer.print(12345);
        printer.flush();
        assertEquals("12345", out.toString());
    }

    /**
     * Test printing a single long value to ensure proper handling.
     */
    @Test
    public void testPrintLong() throws IOException {
        printer.print(Long.MAX_VALUE);
        printer.flush();
        assertEquals(Long.MAX_VALUE + "", out.toString());
    }

    /**
     * Test printing a single double value to ensure proper handling.
     */
    @Test
    public void testPrintDouble() throws IOException {
        printer.print(3.14159265358979323846);
        printer.flush();
        assertEquals("3.14159265358979323846", out.toString());
    }

    /**
     * Test printing a single boolean value to ensure proper handling.
     */
    @Test
    public void testPrintBoolean() throws IOException {
        printer.print(true);
        printer.flush();
        assertEquals("true", out.toString());
    }

    /**
     * Test escaping delimiters and quotes.
     */
    @Test
    public void testEscapeDelimitersAndQuotes() throws IOException {
        printer.setEscapeMode(CSVFormat.ESCAPE_DEFAULT);
        printer.print("\"\"a\"b\"c\");");
        printer.flush();
        assertEquals("\"\"\"a\"\"b\"\"c\\\"\",", out.toString());
    }

    /**
     * Test printing a comment.
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment.");
        printer.flush();
        assertEquals("#This is a comment.\n", out.toString());
    }

    /**
     * Test the printer's ability to handle various quote policies.
     */
    @Test
    public void testQuotePolicies() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(CSVFormat.QuotePolicy.ALL));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new Object[]{"a", "b"});
        printer.flush();
        assertEquals("\"a\",\"b\"", out.toString());

        printer.close();

        printer = new CSVPrinter(out, format);
        printer.printRecord(new Object[]{1, 2});
        printer.flush();
        assertEquals("1,2", out.toString());
    }

    /**
     * Test printing a record with quoting disabled.
     */
    @Test
    public void testPrintRecordWithoutQuotes() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(CSVFormat.QuotePolicy.NONE));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new Object[]{"a", "b"});
        printer.flush();
        assertEquals("a,b", out.toString());
    }

    /**
     * Test printing an array of records.
     */
    @Test
    public void testPrintRecordsArray() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(new Object[][]{
            new Object[]{"a", "b"},
            new Object[]{"c", "d"},
            new Object[]{"e", "f"},
            new Object[]{"g", "h"}
        });
        printer.flush();
        assertEquals("\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\"\n", out.toString());
    }

    /**
     * Test printing an iterable of records.
     */
    @Test
    public void testPrintRecordsIterable() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(java.util.Arrays.asList(new Object[][]{
            new Object[]{"a", "b"},
            new Object[]{"c", "d"},
            new Object[]{"e", "f"},
            new Object[]{"g", "h"}
        }));
        printer.flush();
        assertEquals("\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\"\n", out.toString());
    }

    /**
     * Test printing a JDBC result set.
     */
    @Test
    public void testPrintRecordsJDBC() throws SQLException, IOException {
        String query = "SELECT * FROM test_table";
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        ResultSet resultSet = // RS obtained from a data source
                statement.executeQuery(query);
        printer.printRecords(resultSet);
        printer.flush();
        assertEquals("column1,column2,column3\nvalue1_1,value1_2,value1_3\nvalue2_1,value2_2,value2_3\n...\n", out.toString());
    }
}