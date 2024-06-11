package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.DriverManager;
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
 * Test cases for CSVPrinter class.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream output;
    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        output = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(output), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    /**
     * Test method for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)} with basic usage.
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void testBasicPrinting() throws IOException, SQLException {
        // Given
        final String expected = "value1,value2\nvalue3,value4\n";

        // When
        printer.printRecord(Arrays.asList("value1", "value2"));
        printer.printRecord(Arrays.asList("value3", "value4"));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with String values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintString() throws IOException {
        // Given

        // When
        printer.print("String Value");
        printer.flush();

        // Then
        final String expected = "\"String Value\"";
        final String result = output.toString();
        assertEquals(expected, result.substring(result.indexOf(expected), result.length()));
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with null values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintNull() throws IOException {
        // Given

        // When
        printer.print(null);
        printer.flush();

        // Then
        final String expected = CSVPrinter.CSVFormat.DEFAULT.getNullString();
        final String result = output.toString();
        assertEquals(expected, result.substring(result.indexOf(expected), result.length()));
    }

    /**
     * Test method for {@link CSVPrinter#printComment(String)} with simple comments.
     *
     * @throws IOException
     */
    @Test
    public void testPrintComment() throws IOException {
        // Given
        printer.printRecord(Arrays.asList("value1"));
        printer.println();

        // When
        printer.printComment("This is a comment");
        printer.println();

        // Then
        final String expected = "value1\n# This is a comment\n";
        final String result = output.toString();
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVPrinter#println()} with empty record.
     *
     * @throws IOException
     */
    @Test
    public void testPrintEmptyRecord() throws IOException {
        // Given
        printer.printRecord(Collections.emptyList());

        // When
        printer.println();

        // Then
        final String expected = "\n";
        final String result = output.toString();
        assertEquals(expected, result.substring(result.length() - expected.length(), result.length()));
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)} with an empty Iterable.
     *
     * @throws IOException
     */
    @Test
    public void testPrintEmptyIterable() throws IOException {
        // Given

        // When
        printer.printRecords(Collections.emptyList());

        // Then
        assertTrue(output.toString().isEmpty());
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Object[])}.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsArray() throws IOException {
        // Given
        final String expected = "value1,value2,value3\nvalue4,value5,value6\n";

        // When
        printer.printRecord(Arrays.asList("value1", "value2", "value3"));
        printer.println();
        printer.printRecord(Arrays.asList("value4", "value5", "value6"));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Object[])}.
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testPrintRecordsArrayWithJDBCResultSet() throws IOException, SQLException {
        // Given
        final String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE test (id INT PRIMARY KEY, value TEXT)");
            statement.executeUpdate("INSERT INTO test (id, value) VALUES (1, 'value1')");
            statement.executeUpdate("INSERT INTO test (id, value) VALUES (2, 'value2')");
            ResultSet resultSet = statement.executeQuery("SELECT id, value FROM test");

            // When
            printer.printRecords(resultSet);
            printer.flush();

            // Then
            final String expected = "id,value\n1,value1\n2,value2\n";
            final String result = output.toString();
            assertEquals(expected, result);
        }
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)} with an Iterable containing an empty Iterable.
     *
     * @throws IOException
     */
    @Test
    public void testPrintIterableWithEmptyIterableInside() throws IOException {
        // Given
        final String input = "input";

        // When
        printer.printRecords(Arrays.asList(Arrays.asList(input), Collections.emptyList()));

        // Then
        assertTrue(output.toString().isEmpty());
    }

    /**
     * Test edge case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)} with null values.
     *
     * @throws IOException
     */
    @Test
    public void testEdgeCasePrintNullValues() throws IOException {
        // Given
        final String expected = "null,null\n";

        // When
        printer.printRecord(Arrays.asList(null, null));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertEquals(expected, result);
    }

    /**
     * Test edge case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)} with empty values.
     *
     * @throws IOException
     */
    @Test
    public void testEdgeCasePrintEmptyValues() throws IOException {
        // Given
        final String expected = "\"\",\"\"\n";

        // When
        printer.printRecord(Arrays.asList(CSVPrinter.CSVFormat.DEFAULT.getNullString(), CSVPrinter.CSVFormat.DEFAULT.getNullString()));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertEquals(expected, result);
    }

    /**
     * Test edge case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)} with an empty CharSequence.
     *
     * @throws IOException
     */
    @Test
    public void testEdgeCasePrintEmptyCharSequence() throws IOException {
        // Given
        final String expected = "\"\"\n";

        // When
        printer.printRecord(Arrays.asList(CharSequence.class.cast(new String[] { "" })));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertEquals(expected, result);
    }

    /**
     * Test edge case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)} with an empty Object.
     *
     * @throws IOException
     */
    @Test
    public void testEdgeCasePrintEmptyObject() throws IOException {
        // Given
        final String expected = "\"\"\n";

        // When
        printer.printRecord(Arrays.asList(null));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertEquals(expected, result);
    }

    /**
     * Test edge case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)} with a long String.
     *
     * @throws IOException
     */
    @Test
    public void testEdgeCasePrintLongString() throws IOException {
        // Given
        final String longString = new String(new char[1000]);

        // When
        printer.printRecord(Arrays.asList(longString));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertFalse(result.isEmpty());
    }

    /**
     * Test edge case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)} with a large number.
     *
     * @throws IOException
     */
    @Test
    public void testEdgeCasePrintLargeNumber() throws IOException {
        // Given
        final long largeNumber = Long.MAX_VALUE;

        // When
        printer.printRecord(Arrays.asList(largeNumber));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertFalse(result.isEmpty());
    }

    /**
     * Test edge case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)} with a long String and an empty String.
     *
     * @throws IOException
     */
    @Test
    public void testEdgeCasePrintLongStringAndEmptyString() throws IOException {
        // Given
        final String longString = new String(new char[1000]);

        // When
        printer.printRecord(Arrays.asList(longString, ""));
        printer.println();
        printer.flush();

        // Then
        final String result = output.toString();
        assertFalse(result.isEmpty());
    }
}