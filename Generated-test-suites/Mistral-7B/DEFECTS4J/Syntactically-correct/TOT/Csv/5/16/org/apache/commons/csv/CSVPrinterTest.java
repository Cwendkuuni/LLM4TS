package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test a printRecord call with simple values.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordSimpleValues() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.printRecord(Arrays.asList("Value1", "Value2", "Value3"));

        // Then
        final String expected = "Value1,Value2,Value3\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecord call with a null value.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordNullValue() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.printRecord(Arrays.asList("Value1", null, "Value3"));

        // Then
        final String expected = "Value1,null,Value3\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecord call with a number value.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordNumberValue() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.AUTO);
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.printRecord(Arrays.asList("Value1", 123, "Value3"));

        // Then
        final String expected = "Value1,123,Value3\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecord call with a value longer than the default maximum line length.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordLongValue() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final int maxLineLength = 30;
        final CSVPrinter printer = new CSVPrinter(outContent, format);
        final String longValue = String.join("", Arrays.asList(new String[maxLineLength + 5]));

        // When
        printer.printRecord(Arrays.asList("Value1", longValue, "Value3"));

        // Then
        assertThrows(IOException.class, () -> {
            // This test will fail as it is expected to throw an IOException due to the line length limitation
            final String expected = "Value1," + longValue + ",Value3\n";
            assertEquals(expected, outContent.toString());
        });

        // Clean up
        printer.close();
    }

    /**
     * Test a printComment call.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintComment() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.printComment("This is a comment");

        // Then
        final String expected = "# This is a comment\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a println call.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintln() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.println();

        // Then
        final String expected = "\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a println call with a record separator.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintlnRecordSeparator() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', null, null, QuoteMode.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.println();

        // Then
        final String expected = "\n";
        assertEquals(expected, outContent.toString());

        // When
        printer.printlnRecord(Arrays.asList("Value1"));

        // Then
        final String recordSeparator = format.getRecordSeparator();
        final String expectedRecord = "Value1" + recordSeparator;
        assertEquals(expectedRecord, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecord call with a null iterable.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordNullIterable() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.printRecords(null);

        // Then
        assertEquals("", outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecords call with an empty iterable.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsEmptyIterable() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        printer.printRecords(Arrays.asList());

        // Then
        final String expected = "\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecords call with an iterable of single objects.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsSingleObjectIterable() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        List<Object> values = new ArrayList<>();
        values.add("Value1");
        printer.printRecords(values);

        // Then
        final String expected = "Value1\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecords call with an iterable of arrays.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsArrayIterable() throws IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        List<Object> values = new ArrayList<>();
        values.add(new Object[]{"Value1", "Value2"});
        printer.printRecords(values);

        // Then
        final String expected = "Value1,Value2\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
    }

    /**
     * Test a printRecords call with a JDBC result set.
     *
     * @throws IOException
     *             If an I/O error occurs
     * @throws SQLException if a database access error occurs
     */
    @org.junit.Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        // Given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outContent, format);

        // When
        final ResultSet resultSet = getSampleResultSet();
        printer.printRecords(resultSet);

        // Then
        final String expected = "Value1,Value2,Value3\n" +
                "Value4,Value5,Value6\n";
        assertEquals(expected, outContent.toString());

        // Clean up
        printer.close();
        resultSet.close();
    }

    /**
     * Gets a sample JDBC result set.
     *
     * @return a sample result set
     * @throws SQLException if a database access error occurs
     */
    private ResultSet getSampleResultSet() throws SQLException {
        final String sql = "SELECT 'Value1', 'Value2', 'Value3' FROM dummy";

        final java.sql.Connection connection = null; // replace with actual connection
        final java.sql.Statement statement = connection.createStatement();
        statement.execute(sql);
        return statement.getResultSet();
    }
}