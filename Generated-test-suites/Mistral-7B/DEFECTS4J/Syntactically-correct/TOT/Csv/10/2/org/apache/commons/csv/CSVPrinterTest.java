package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVPrinter.quote;
import static org.apache.commons.csv.CSVPrinter.quotePolicy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test construction.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testConstruction() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        new CSVPrinter(new OutputStreamWriter(out), format);
        assertNotNull(out.toString());
    }

    /**
     * Tests printing a single value.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrint() throws IOException {
        testPrint(new CSVPrinter(new StringWriter(), DEFAULT), "value");
    }

    /**
     * Tests printing a single value with quote.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintQuotedValue() throws IOException {
        testPrint(new CSVPrinter(new StringWriter(), DEFAULT.withQuote('"')), "\"value\"");
    }

    /**
     * Tests printing a single null value.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintNullValue() throws IOException {
        testPrint(new CSVPrinter(new StringWriter(), DEFAULT), null);
    }

    /**
     * Tests printing multiple values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintMultipleValues() throws IOException {
        testPrintMultipleValues(new CSVPrinter(new StringWriter(), DEFAULT), "value1", "value2", "value3");
    }

    /**
     * Tests printing multiple quoted values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintQuotedMultipleValues() throws IOException {
        testPrintMultipleValues(new CSVPrinter(new StringWriter(), DEFAULT.withQuote('"')),
                "\"value1\"", "\"value2\"", "\"value3\"");
    }

    /**
     * Tests printing multiple null values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintNullMultipleValues() throws IOException {
        testPrintMultipleValues(new CSVPrinter(new StringWriter(), DEFAULT), null, null, null);
    }

    /**
     * Tests printing multiple values and quotes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintQuotedMultipleValuesWithQuotes() throws IOException {
        testPrintMultipleValues(new CSVPrinter(new StringWriter(), DEFAULT.withQuote('"')),
                "\"value1\"", "\"value2\"" + quote('"'), "value3");
    }

    /**
     * Tests printing empty values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintEmptyValues() throws IOException {
        testPrintEmptyValues(new CSVPrinter(new StringWriter(), DEFAULT), "");
    }

    /**
     * Tests printing empty values and quotes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintQuotedEmptyValues() throws IOException {
        testPrintEmptyValues(new CSVPrinter(new StringWriter(), DEFAULT.withQuote('"')), quote('') + "");
    }

    /**
     * Tests printing record separator.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordSeparator() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT.withRecordSeparator("\r\n");
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        printer.printRecord(new String[]{"value1", "value2"});
        printer.close();
        final String csv = out.toString();
        assertEquals("\r\nvalue1\r\nvalue2\r\n", csv);
    }

    /**
     * Tests printing comment.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintComment() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT.withCommentingEnabled();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        printer.printComment("This is a test comment");
        printer.close();
        final String csv = out.toString();
        final String commentStart = format.getCommentStart().toString();
        assertTrue(csv.startsWith(commentStart + " " + "This is a test comment\r\n"));
    }

    /**
     * Tests printing record.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecord() throws IOException {
        testPrintRecord(new CSVPrinter(new StringWriter(), DEFAULT), new String[]{"value1", "value2"});
    }

    /**
     * Tests printing records.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecords() throws IOException {
        testPrintRecords(new CSVPrinter(new StringWriter(), DEFAULT), new String[][]{{"value1", "value2"}, {"value3", "value4"}});
    }

    /**
     * Tests printing records with quoted values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsQuotedValues() throws IOException {
        testPrintRecords(new CSVPrinter(new StringWriter(), DEFAULT.withQuote('"')),
                new String[][]{{"\"value1\"", "\"value2\""}, {"\"value3\"", "\"value4\""}});
    }

    /**
     * Tests printing records with null values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsNullValues() throws IOException {
        testPrintRecords(new CSVPrinter(new StringWriter(), DEFAULT), new String[][]{{null, null}});
    }

    /**
     * Tests printing records with null and quoted values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsQuotedNullValues() throws IOException {
        testPrintRecords(new CSVPrinter(new StringWriter(), DEFAULT.withQuote('"')),
                new String[][]{{quote(null), quote(null)}});
    }

    /**
     * Tests printing records with empty values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsEmptyValues() throws IOException {
        testPrintRecords(new CSVPrinter(new StringWriter(), DEFAULT), new String[][]{{"", ""}});
    }

    /**
     * Tests printing records with empty and quoted values.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsQuotedEmptyValues() throws IOException {
        testPrintRecords(new CSVPrinter(new StringWriter(), DEFAULT.withQuote('"')),
                new String[][]{{quote(""), quote("")}});
    }

    /**
     * Tests printing records with comments.
     *
     * @throws IOException
     *             if an I/O error occurs
     * @throws SQLException if a database access error occurs
     */
    @org.junit.Test
    public void testPrintRecordsWithComments() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT.withCommentingEnabled();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        final Statement statement = createStatement();
        try {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM test_records");
            printer.printRecords(resultSet);
            printer.close();
            final String csv = out.toString();
            final CSVParser parser = new CSVParser(new StringReader(csv), format);
            final CSVRecord record;
            final int recordNumber = 0;
            while ((record = parser.nextRecord()) != null) {
                final String comment = record.getRecordNumberComment();
                assertEquals("Test record " + recordNumber, comment);
                recordNumber++;
            }
        } finally {
            statement.close();
        }
    }

    /**
     * Tests quoted policy.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testQuotePolicy() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(quotePolicy(Quote.MINIMAL)));
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        final String testValue = "test";

        printer.print(testValue);
        printer.close();
        final String csv = out.toString();
        final CSVParser parser = new CSVParser(new StringReader(csv), format);
        final CSVRecord record = parser.nextRecord();
        assertEquals(testValue, record.get(0));

        printer = new CSVPrinter(new OutputStreamWriter(out), format.withQuotePolicy(quotePolicy(Quote.ALL)));
        printer.print(testValue);
        printer.close();
        final String csv2 = out.toString();
        final CSVParser parser2 = new CSVParser(new StringReader(csv2), format);
        final CSVRecord record2 = parser2.nextRecord();
        assertTrue(record2.get(0).startsWith(quote(testValue)));
        assertTrue(record2.get(0).endsWith(quote(testValue)));
    }

    private void testPrint(CSVPrinter printer, String value) throws IOException {
        printer.print(value);
        printer.println();
        printer.close();
    }

    private void testPrintMultipleValues(CSVPrinter printer, String... values) throws IOException {
        printer.printRecord(values);
        printer.println();
        printer.close();
    }

    private void testPrintEmptyValues(CSVPrinter printer, String emptyValue) throws IOException {
        printer.printEmptyValues(emptyValue);
        printer.println();
        printer.close();
    }

    private void testPrintRecord(CSVPrinter printer, String[] record) throws IOException {
        printer.printRecord(record);
        printer.println();
        printer.close();
    }

    private void testPrintRecords(CSVPrinter printer, String[][] records) throws IOException {
        printer.printRecords(records);
        printer.close();
    }

    private Statement createStatement() throws SQLException {
        final String url = "jdbc:hsqldb:mem:testdb";
        return DriverManager.getConnection(url).createStatement();
    }
}