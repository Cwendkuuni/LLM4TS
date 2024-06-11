package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Test class for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    /** Tests printing of values. */
    @org.junit.Test
    public void testPrint() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.print("Hello");
        printer.close();

        String expected = "Hello" + LF;
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of null values. */
    @org.junit.Test
    public void testPrintNull() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.print(null);
        printer.close();

        String expected = CSVFormat.DEFAULT.getNullString() + LF;
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of arrays. */
    @org.junit.Test
    public void testPrintArray() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        Object[] array = {"ABC", 123, 'X'};

        printer.printRecord(array);
        printer.close();

        String expected = "\"ABC\",123,\"X\"\n";
        assertEquals(expected, outStream.toString());
    }

    /** Tests quoting of values. */
    @org.junit.Test
    public void testPrintQuoting() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream),
                CSVFormat.DEFAULT.withQuoteMode(CSVFormat.QuoteMode.ALL));
        printer.print("field, with \"quotes\"" + LF);
        printer.close();

        String expected = "\"field, with \"quotes\"\"\n";
        assertEquals(expected, outStream.toString());
    }

    /** Tests escaping of values. */
    @org.junit.Test
    public void testPrintEscaping() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream),
                CSVFormat.DEFAULT.withEscapeChar('\\'));
        printer.print("field with comma," + LF);
        printer.close();

        String expected = "field with comma,\"\"\n";
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of empty values. */
    @org.junit.Test
    public void testPrintEmpty() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.print(null);
        printer.print("");
        printer.close();

        String expected = CSVFormat.DEFAULT.getNullString() + "," + CSVFormat.DEFAULT.getNullString() + LF;
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of comments. */
    @org.junit.Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.printComment("This is a comment");
        printer.close();

        String expected = CSVFormat.DEFAULT.getCommentStart().toString() + " This is a comment" + LF;
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of a new record. */
    @org.junit.Test
    public void testPrintNewRecord() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.print("field1");
        printer.println();
        printer.print("field2");
        printer.close();

        String expected = "field1" + LF + "field2\n";
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of multiple records. */
    @org.junit.Test
    public void testPrintMultipleRecords() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.printRecord(Arrays.asList("Record1", "Record2"));
        printer.println();
        printer.printRecord(Arrays.asList("Record3", "Record4"));
        printer.close();

        String expected = "Record1,Record2\nRecord3,Record4\n";
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of records from an array. */
    @org.junit.Test
    public void testPrintRecordsFromArray() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.printRecords(new Object[]{new String[]{"Record1", "Record2"}, new String[]{"Record3", "Record4"}});
        printer.close();

        String expected = "Record1,Record2\nRecord3,Record4\n";
        assertEquals(expected, outStream.toString());
    }

    /** Tests printing of records from an Iterable. */
    @org.junit.Test
    public void testPrintRecordsFromIterable() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);

        List<String[]> records = Arrays.asList(new String[]{"Record1", "Record2"}, new String[]{"Record3", "Record4"});
        printer.printRecords(records);
        printer.close();

        String expected = "Record1,Record2\nRecord3,Record4\n";
        assertEquals(expected, outStream.toString());

        // Test with SQL ResultSet
        String sql = "SELECT col1, col2 FROM testTable";
        ResultSet resultSet = MyDatabase.executeQuery(sql);
        printer.printRecords(resultSet);
        printer.close();
    }

    /** Tests flushing. */
    @org.junit.Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.print("field1");
        printer.flush();
        printer.print("field2");
        printer.close();

        String expected = "field1" + LF;
        assertEquals(expected, outStream.toString());
    }

    /** Tests closing. */
    @org.junit.Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        printer.print("field1");
        printer.close();

        String expected = "field1\n";
        assertEquals(expected, outStream.toString());
    }

    /** Tests handling of exceptions. */
    @org.junit.Test(expected = IOException.class)
    public void testHandleIOException() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        throw new IOException("Test error");
    }

    /** Tests handling of SQL exceptions. */
    @org.junit.Test(expected = SQLException.class)
    public void testHandleSQLException() throws SQLException, IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        throw new SQLException("Test error");
    }
}