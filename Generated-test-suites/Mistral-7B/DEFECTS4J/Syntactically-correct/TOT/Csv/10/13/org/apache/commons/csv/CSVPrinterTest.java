package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVPrinter.QUOTE_MINIMAL;
import static org.apache.commons.csv.CSVPrinter.QUOTE_NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Test class for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Tests the basic functionality of the {@link CSVPrinter}.
     */
    @org.junit.Test
    public void testBasicFunctionality() throws IOException, SQLException {

        // Create a byte array output stream and the CSVPrinter
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), DEFAULT);

        // Print some values
        printer.print("Hello World");
        printer.print(42);
        printer.print(3.14);
        printer.println();

        printer.printRecord(Arrays.asList("One", "Two", "Three"));
        printer.println();

        printer.printComment("This is a comment.");
        printer.println();

        printer.close();

        // Parse the output to make sure it matches the expected values
        CSVParser parser = new CSVParser(new StringReader(out.toString()));
        CSVRecord record = parser.getHeaderMap().isEmpty() ? parser.getNextRecord() : parser.getRecordMap().get("0");
        assertEquals("Hello World", record.get("0"));
        assertEquals("42", record.get("1"));
        assertEquals("3.14", record.get("2"));
        assertEquals(3, record.size());

        record = parser.getNextRecord();
        assertEquals("One", record.get("0"));
        assertEquals("Two", record.get("1"));
        assertEquals("Three", record.get("2"));
        assertEquals(3, record.size());

        assertNull(parser.getHeaderMap().get(""));
        assertEquals(2, parser.getRecordCount());

        // Test printing a record with no values
        printer = new CSVPrinter(new OutputStreamWriter(out), DEFAULT);
        printer.printRecord(Collections.emptyList());
        printer.println();
        parser = new CSVParser(new StringReader(out.toString()));
        record = parser.getNextRecord();
        assertNull(record);

        // Test quoting and escaping
        printer = new CSVPrinter(new OutputStreamWriter(out), new CSVFormat(',', Quote.ALL, Quote.ALL, null));
        printer.print("This, is a \"test\" with a 'quote' and a \"double quote\" and a backslash \\ and a tab \t and a newline \n");
        printer.println();
        parser = new CSVParser(new StringReader(out.toString()));
        record = parser.getNextRecord();
        assertEquals("This, is a \"test\" with a 'quote' and a \"double quote\" and a backslash \\ and a tab \\t and a newline \\n", record.get(0));

        // Test quoting and escaping with a different delimiter
        printer = new CSVPrinter(new OutputStreamWriter(out), new CSVFormat('\t', Quote.MINIMAL, null, null));
        printer.print("Line 1\tLine 2\t\"Field with quote\"\tField with double quote\tBackslash \\tNewline \nLine 2");
        printer.println();
        parser = new CSVParser(new StringReader(out.toString()));
        record = parser.getNextRecord();
        assertEquals("Line 1\tLine 2\t\"Field with quote\"\tField with double quote\tBackslash \\tNewline ", record.get(0));

        // Test quoting and escaping with no escaping
        printer = new CSVPrinter(new OutputStreamWriter(out), new CSVFormat('\t', Quote.NONE, null, null));
        printer.print("Line 1\tLine 2\t\"Field with quote\"\tField with double quote\tBackslash \\tNewline \nLine 2");
        printer.println();
        parser = new CSVParser(new StringReader(out.toString()));
        record = parser.getNextRecord();
        assertEquals("Line 1\tLine 2\tField with quote\tField with double quote\tBackslash\tNewline \nLine 2", record.get(0));

        // Test quoting and escaping with no quoting
        printer = new CSVPrinter(new OutputStreamWriter(out), new CSVFormat('\t', null, null, null));
        printer.print("Line 1\tLine 2\tField with quote\tField with double quote\tBackslash \\tNewline \nLine 2");
        printer.println();
        parser = new CSVParser(new StringReader(out.toString()));
        record = parser.getNextRecord();
        assertEquals("Line 1\tLine 2\tField with quote\tField with double quote\tBackslash\tNewline \nLine 2", record.get(0));

        // Test printing from a result set
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select 'foo' as bar, 'bar' as baz");
        printer = new CSVPrinter(new OutputStreamWriter(out), DEFAULT);
        printer.printRecords(resultSet);
        printer.close();
        parser = new CSVParser(new StringReader(out.toString()));
        record = parser.getNextRecord();
        assertEquals("foo", record.get("bar"));
        assertEquals("bar", record.get("baz"));
    }

    /**
     * Tests the functionality of the {@link CSVPrinter} when the output is a SQL ResultSet.
     */
    @org.junit.Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {

        // Create a SQL ResultSet and the CSVPrinter
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select 'foo' as bar, 42 as baz, '3.14' as baz2, null as baz3");
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(System.out), DEFAULT);

        // Print the records
        printer.printRecords(resultSet);

        // Verify the output
        CSVParser parser = new CSVParser(System.in);
        CSVRecord record = parser.getNextRecord();
        assertEquals("foo", record.get("bar"));
        assertEquals("42", record.get("baz"));
        assertEquals("3.14", record.get("baz2"));
        assertNull(record.get("baz3"));

        parser.nextRecord(); // skip the header
        record = parser.getNextRecord();
        assertEquals("foo", record.get("bar"));
        assertEquals("42", record.get("baz"));
        assertNull(record.get("baz2"));
        assertNull(record.get("baz3"));

        parser.close();
        statement.close();
    }

    /**
     * Tests if the CSVPrinter correctly handles empty records.
     */
    @org.junit.Test
    public void testEmptyRecords() throws IOException, SQLException {

        // Create a SQL ResultSet and the CSVPrinter
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select '' as bar, NULL as baz");
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(System.out), DEFAULT);

        // Print the records
        printer.printRecords(resultSet);

        // Verify the output
        CSVParser parser = new CSVParser(System.in);
        CSVRecord record = parser.getNextRecord();
        assertNull(record);

        parser.nextRecord(); // skip the header
        record = parser.getNextRecord();
        assertNull(record.get("bar"));
        assertNull(record.get("baz"));

        parser.close();
        statement.close();
    }

    /**
     * Tests if the CSVPrinter correctly handles empty lines.
     */
    @org.junit.Test
    public void testEmptyLines() throws IOException {

        // Create a byte array output stream and the CSVPrinter
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), DEFAULT);

        // Print empty lines
        printer.println();
        printer.println();
        printer.close();

        // Verify the output
        CSVParser parser = new CSVParser(new StringReader(out.toString()));
        CSVRecord record = parser.getNextRecord();
        assertNull(record);

        parser.nextRecord(); // skip the header
        record = parser.getNextRecord();
        assertNull(record);

        parser.nextRecord(); // skip the next empty record
        record = parser.getNextRecord();
        assertNull(record);

        parser.close();
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "sa", "");
    }
}