package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /** The constant format to use for testing. */
    private static final CSVFormat FORMAT = new CSVFormat(',', Quote.MINIMAL);

    /**
     * Test printing a single value.
     */
    @org.junit.Test
    public void testSingleValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print("hello");
            printer.flush();
            final String result = new String(out.toByteArray());
            assertEquals("hello", result);
        }
    }

    /**
     * Test printing multiple values.
     */
    @org.junit.Test
    public void testMultipleValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print("hello");
            printer.print("world");
            printer.println();
            printer.print("foo");
            printer.print("bar");
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "hello,world\nfoo,bar\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a null value.
     */
    @org.junit.Test
    public void testNullValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print(null);
            printer.flush();
            final String result = new String(out.toByteArray());
            assertNull(result);
        }
    }

    /**
     * Test printing a null value with quotes.
     */
    @org.junit.Test
    public void testNullValueWithQuotes() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print(null);
            printer.print("\"\"");
            printer.print("hello");
            printer.println();
            printer.print("foo");
            printer.print("\"\",\"\"");
            printer.print("bar");
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "\"\",hello\nfoo,\"\",bar\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a value that requires no quoting.
     */
    @org.junit.Test
    public void testNoQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print("12345");
            printer.println();
            printer.print("67890");
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "12345\n67890\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a value that requires quoting.
     */
    @org.junit.Test
    public void testQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print("hello world");
            printer.println();
            printer.print("\"hello world\"");
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "hello world\n\"hello world\"\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a value that requires quoting and escaping.
     */
    @org.junit.Test
    public void testQuotingAndEscaping() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print("\"hello world\"");
            printer.println();
            printer.print("\"hello,\"world\"");
            printer.println();
            printer.print("\"hello world\"\n");
            printer.println();
            printer.print("\"hello world\"\r");
            printer.println();
            printer.print("\"hello world\"\"");
            printer.println();
            printer.print("\"hello world\\\"\"");
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "\"hello world\"\n\"hello,\"world\"\n\"hello world\"\n\"hello world\"\r\n\"hello world\"\"\n\"hello world\\\"\"\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a value that requires quoting but no escaping.
     */
    @org.junit.Test
    public void testQuotingNoEscaping() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.print("hello,world");
            printer.println();
            printer.print("\"hello,world\"");
            printer.println();
            printer.print("\"hello,world\"\n");
            printer.println();
            printer.print("\"hello,world\"\r");
            printer.println();
            printer.print("\"hello,world\"\"");
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "hello,world\n\"hello,world\"\nhello,world\nhello,world\r\n\"hello,world\"\"\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a comment.
     */
    @org.junit.Test
    public void testComment() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            printer.printComment("# This is a comment");
            printer.println();
            printer.printComment("# Another comment");
            printer.println();
            printer.printComment("# A comment with CR-LF");
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "# This is a comment\n# Another comment\n# A comment with CR-LF\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a record from an iterator.
     */
    @org.junit.Test
    public void testRecordFromIterator() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            final String[] values = {"hello", "'foo'", "123"};
            printer.printRecord(values);
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "hello,foo,123\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a record from an array.
     */
    @org.junit.Test
    public void testRecordFromArray() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            final Object[] values = {"hello", "'foo'", "123"};
            printer.printRecord(values);
            printer.println();
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "hello,foo,123\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Test printing a record from a result set.
     */
    @org.junit.Test
    public void testRecordFromResultSet() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), FORMAT)) {
            final ResultSet rs = getTestResultSet();
            printer.printRecords(rs);
            printer.close();

            final String result = new String(out.toByteArray());
            final String expected = "foo,bar\nqux,quux\n";
            assertEquals(expected, result);
        }
    }

    /**
     * Returns a test result set.
     *
     * @return the test result set
     * @throws SQLException if a database access error occurs
     */
    private static ResultSet getTestResultSet() throws SQLException {
        final java.sql.Connection connection = getTestConnection();
        try (final java.sql.Statement statement = connection.createStatement();
                final ResultSet rs = statement.executeQuery("SELECT foo, bar FROM test_values")) {
            return rs;
        }
    }

    /**
     * Returns a test connection.
     *
     * @return the test connection
     * @throws SQLException if a database access error occurs
     */
    private static java.sql.Connection getTestConnection() throws SQLException {
        final java.sql.DriverManager manager = new java.sql.JDBCDriver();
        final java.sql.Connection connection = manager.getConnection("jdbc:default:connection:");
        return connection;
    }
}