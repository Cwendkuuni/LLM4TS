package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.SP;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVFormat.QUOTE_MINIMAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecord.Type;
import org.apache.commons.csv.QuoteMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    /** The test printer. */
    private CSVPrinter printer;

    /** The test output. */
    private StringWriter out;

    /** The test format. */
    private CSVFormat format;

    /**
     * Sets up.
     *
     * @throws IOException
     *             If initialization fails
     */
    @Before
    public void setUp() throws IOException {
        this.out = new StringWriter();
        this.format = new CSVFormat(',', QuoteMode.MINIMAL);
        this.format.setHeader("Name", "Age", "City");
        this.format.setCommentCharacter(COMMENT);
        // A simple no-op printer for testing
        this.printer = new CSVPrinter(this.out, this.format);
    }

    /**
     * Tears down.
     */
    @After
    public void tearDown() throws IOException {
        this.printer.close();
    }

    /**
     * Tests creating a new printer.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testNewInstance() throws IOException {
        final CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), DEFAULT);
        assertNotNull(printer);
        printer.close();
    }

    /**
     * Tests printing a single value.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintSimpleValue() throws IOException {
        this.printer.print("Hello");
        assertEquals("Hello", this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing a null value.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintNullValue() throws IOException {
        this.printer.print(null);
        assertEquals("", this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing a list of values.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintValues() throws IOException {
        this.printer.printRecord(Arrays.asList("Alice", 25, "New York"));
        assertEquals("Alice,25,New York" + LF, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing a list of null values.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintNullValues() throws IOException {
        this.printer.printRecord(Arrays.asList(null, null, null));
        assertEquals(LF + LF, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing a list of mixed values.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintMixedValues() throws IOException {
        this.printer.printRecord(Arrays.asList("Alice", 25, null, "Paris"));
        assertEquals("Alice,25,null,Paris" + LF, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing a record with a comment.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintRecordWithComment() throws IOException {
        this.printer.printComment("The first record");
        this.printer.println();
        this.printer.printRecord(Arrays.asList("Alice", 25, "New York"));
        assertEquals("The first record" + LF + "Alice,25,New York" + LF, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing multiple records.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintMultipleRecords() throws IOException {
        this.printer.printRecord(Arrays.asList("Alice", 25, "New York"));
        this.printer.printRecord(Arrays.asList("Bob", 30, "Los Angeles"));
        this.printer.printRecord(Arrays.asList("Charlie", null, "Chicago"));
        assertEquals(
                "Alice,25,New York" + LF + "Bob,30,Los Angeles" + LF + "Charlie,null,Chicago" + LF,
                this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing multiple records with different formats.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintMultipleRecordsWithDifferentFormats() throws IOException {
        this.format = new CSVFormat(',', QuoteMode.ALL);
        this.format.setRecordSeparator(CR + LF);
        this.format.setHeader("Name", "Age", "City");
        this.printer = new CSVPrinter(this.out, this.format);

        this.printer.printRecord(Arrays.asList("Alice", 25, "New York"));
        this.printer.printRecord(Arrays.asList("Bob", 30, "Los Angeles"));
        this.printer.printRecord(Arrays.asList("Charlie", null, "Chicago"));
        final String expected = "Name,Age,City" + CR + "Alice,25,New York" + CR + "Bob,30,Los Angeles" + CR
                + "Charlie,null,Chicago" + CR;
        assertEquals(expected, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing multiple records with escape characters.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintMultipleRecordsWithEscapeCharacters() throws IOException {
        this.format = new CSVFormat(';', QuoteMode.MINIMAL);
        this.format.setEscapeCharacter('\\');
        this.format.setRecordSeparator(CR + LF);
        this.format.setHeader("Name", "Age", "City");
        this.printer = new CSVPrinter(this.out, this.format);

        this.printer.printRecord(Arrays.asList("Alice,25,New York", "\"Hello, World!\"", "Bob\\'s Place"));
        this.printer.printRecord(Arrays.asList("\"Bob\",30,\"Los Angeles\""));
        this.printer.printRecord(Arrays.asList("Charlie,null,\"Chicago\""));
        final String expected = "Name,Age,City" + CR + "Alice,25,New York,\"Hello, World!\",Bob\'s Place" + CR
                + "Bob,\"Bob\",30,\"Los Angeles\"" + CR + "Charlie,null,\"Chicago\"" + CR;
        assertEquals(expected, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing multiple records with escapes in quotes.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintMultipleRecordsWithEscapesInQuotes() throws IOException {
        this.format = new CSVFormat(';', QuoteMode.MINIMAL);
        this.format.setEscapeCharacter('\\');
        this.format.setRecordSeparator(CR + LF);
        this.format.setHeader("Name", "Age", "City");
        this.printer = new CSVPrinter(this.out, this.format);

        this.printer.printRecord(Arrays.asList("Alice,25,New York", "\"Alice,25,New York\"", "\"Bob\\'s Place\""));
        this.printer.printRecord(Arrays.asList("\"Bob, 30, Los Angeles\"", "30", "\"Los Angeles\""));
        this.printer.printRecord(Arrays.asList("Charlie,null,\"Chicago\""));
        final String expected = "Name,Age,City" + CR + "Alice,25,New York,\"Alice,25,New York\",\"Bob\'s Place\"" + CR
                + "Bob, 30, Los Angeles,30,Los Angeles" + CR + "Charlie,null,\"Chicago\"" + CR;
        assertEquals(expected, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing multiple records with comments.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintMultipleRecordsWithComments() throws IOException {
        this.format = new CSVFormat(',', QuoteMode.MINIMAL);
        this.format.setCommentCharacter('#');
        this.format.setRecordSeparator(CR + LF);
        this.format.setHeader("Name", "Age", "City");
        this.printer = new CSVPrinter(this.out, this.format);

        this.printer.printComment("First comment");
        this.printer.println();
        this.printer.printRecord(Arrays.asList("Alice,25,New York", "# A second comment"));
        this.printer.printComment("Third comment");
        this.printer.println();
        this.printer.printRecord(Arrays.asList("Bob,30,Los Angeles"));
        final String expected = "# First comment" + LF + "Name,Age,City" + LF + "Alice,25,New York,# A second comment" + LF
                + "# Third comment" + LF + "Bob,30,Los Angeles" + LF;
        assertEquals(expected, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing multiple records with quoting and comments.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintMultipleRecordsWithQuotingAndComments() throws IOException {
        this.format = new CSVFormat(',', QuoteMode.ALL);
        this.format.setCommentCharacter('#');
        this.format.setRecordSeparator(CR + LF);
        this.format.setHeader("Name", "Age", "City");
        this.printer = new CSVPrinter(this.out, this.format);

        this.printer.printComment("First comment");
        this.printer.println();
        this.printer.printRecord(Arrays.asList("\"Alice\",25,\"New York\"", "# A second comment"));
        this.printer.printComment("Third comment");
        this.printer.println();
        this.printer.printRecord(Arrays.asList("Bob\",30,\"Los Angeles\""));
        final String expected = "# First comment" + LF + "Name,Age,City" + LF + "\"Alice\",25,\"New York\"# A second comment" + LF
                + "# Third comment" + LF + "Bob\",30,\"Los Angeles\"" + LF;
        assertEquals(expected, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests printing a record with a large number.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testPrintLargeRecord() throws IOException {
        this.format = new CSVFormat(',', QuoteMode.MINIMAL);
        this.format.setRecordSeparator(CR + LF);
        this.format.setHeader("Name", "Age", "City");
        this.printer = new CSVPrinter(this.out, this.format);

        this.printer.printRecord(Collections.nCopies(1000, Arrays.asList("Record " + Integer.toString(i), i)));
        final String expected = "Name,Age,City" + CR;
        for (int i = 0; i < 1000; i++) {
            expected += "Record " + Integer.toString(i) + "," + i + "," + CR;
        }
        assertEquals(expected, this.out.toString());
        this.printer.close();
    }

    /**
     * Tests parsing a simple record set.
     *
     * @throws IOException
     *             If an error occurs
     */
    @Test
    public void testParseSimpleRecordSet() throws IOException {
        this.printer.printRecord(Arrays.asList("Alice", 25, "New York"));
        this.printer.printRecord(Arrays.asList("Bob", 30, "Los Angeles"));
        this.printer.close();

        // Test parsing the record set
        final CSVParser parser = new CSVParser(new StringReader(this.out.toString()));
        List<CSVRecord> records = parser.getRecords();
        List<CSVRecord> expectedRecords = Arrays.asList(
                new CSVRecord(Type.STRING, "Alice"), new CSVRecord(Type.INTEGER, 25), new CSVRecord(Type.STRING, "New York"),
                new CSVRecord(Type.STRING, "Bob"), new CSVRecord(Type.INTEGER, 30), new CSVRecord(Type.STRING, "Los Angeles"));
        assertEquals(expectedRecords, records);
    }
}