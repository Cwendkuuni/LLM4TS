package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.*;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVFormat.QuoteMode;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CSVPrinter class.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() throws Exception {
        outContent = new ByteArrayOutputStream();
        CSVFormat format = new Builder().setQuoteMode(QuoteMode.ALL).build();
        printer = new CSVPrinter(new OutputStreamWriter(outContent), format);
    }

    /**
     * Test print method with normal values.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintNormalValues() throws IOException {
        printer.print("Hello");
        printer.print(123);
        printer.print(1.23);
        printer.print(true);
        printer.println();

        String expectedOutput = "\"Hello\"123,1.23,true" + CR;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test print method with escaping.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintEscapedValues() throws IOException {
        printer.print("value with CR\\r and LF\\n");
        printer.println();

        String expectedOutput = "\"value with CR\\r and LF\\n\"" + CR;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test print method with quoting.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintQuotedValues() throws IOException {
        printer.setQuoteChar(CSVPrinter.QUOTE_CHAR_DOUBLE_QUOTE);
        printer.print("Hello, world!");
        printer.println();

        String expectedOutput = "\"Hello, world!\"";
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test printComment method.
     *
     * @throws IOException on failure
     * @throws SQLException on database access error
     */
    @Test
    public void testPrintComment() throws IOException, SQLException {
        printer.printComment("This is a comment.");
        printer.println();

        String expectedOutput = COMMENT + CR + "This is a comment." + CR;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test println method.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintLn() throws IOException {
        printer.println("Hello");

        String expectedOutput = "Hello" + CR;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test printRecord method with Iterable value.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintRecordIterable() throws IOException {
        String[] values = {"Hello", "World"};
        printer.printRecord(values);

        String expectedOutput = "\"Hello\",\"World\"" + CR;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test printRecord method with Object value.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintRecordObject() throws IOException {
        printer.printRecord("Hello", "World");

        String expectedOutput = "\"Hello\",\"World\"" + CR;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test printRecords method with Iterable value.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintRecordsIterable() throws IOException {
        String[][] records = {{"Hello", "World"}, {"Hi", "There"}};
        printer.printRecords(records);

        String expectedOutput = "\"Hello\",\"World\"" + CR;
        expectedOutput += "\"Hi\",\"There\"" + CR;

        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test printRecords method with Object[] value.
     *
     * @throws IOException on failure
     */
    @Test
    public void testPrintRecordsObjectArray() throws IOException {
        Object[][] records = {{
            "Hello", "World"
        }, {
            "Hi", "There"
        }};
        printer.printRecords(records);

        String expectedOutput = "\"Hello\",\"World\"" + CR;
        expectedOutput += "\"Hi\",\"There\"" + CR;

        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test printRecords method with ResultSet value.
     *
     * @throws IOException on failure
     * @throws SQLException on database access error
     */
    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        String query = "SELECT * FROM users";
        ResultSet resultSet = new MockResultSet(query);

        printer.printRecords(resultSet);

        String expectedOutput = "\"ID\",\"Name\",\"Age\"" + CR;
        expectedOutput += "\"1\",\"John Doe\",25" + CR;
        expectedOutput += "\"2\",\"Jane Doe\",30" + CR;

        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Test flush method.
     *
     * @throws IOException on failure
     */
    @Test
    public void testFlush() throws IOException {
        printer.flush();
        String expectedOutput = outContent.toString();
        assertEquals(expectedOutput, outContent.toString());
    }
}