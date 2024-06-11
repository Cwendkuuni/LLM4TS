package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test case for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testCSVPrinter() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        printer.println();
        printer.print("test");
        printer.print(3);
        printer.print(true);
        printer.println();
        printer.close();

        String expected = "test\n3,true\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#print(Object)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrint() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.print("test");
        printer.print(null);
        printer.println();

        String expected = "test\nnull\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintAndQuote() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat('"', '\\', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.print("test \"test \" test\"");
        printer.println();

        String expected = "\"test \"\"test \"\"test\"\"\n";
        String actual = out.toString();
        assertEquals(expected, actual);

        printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.print("test \ntest");
        printer.println();

        expected = "\"test \\n\"\"test\"\"\n";
        actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#printAndEscape(CharSequence, int, int)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintAndEscape() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.print("test\n");
        printer.println();

        String expected = "test\\n\n";
        String actual = out.toString();
        assertEquals(expected, actual);

        printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.print("test\"");
        printer.println();

        expected = "test\"\\\"\n";
        actual = out.toString();
        assertEquals(expected, actual);

        printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.print("test\\");
        printer.println();

        expected = "test\\\\\n";
        actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#flush()}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.print("test");
        printer.flush();

        String expected = "test\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#close()}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        printer.close();

        assertThrows(IOException.class, () -> printer.println());
    }

    /**
     * Test case for {@link CSVPrinter#printComment(String)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat('"', '\\', Quote.MINIMAL).withCommentingEnabled();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.printComment("test comment");
        printer.println();

        String expected = "test comment\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for
     * {@link CSVPrinter#println()}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintln() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.println();

        String expected = "\n\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#printRecord(Iterable)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordIterable() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.printRecord(new Object[] { "test1", "test2" });
        printer.println();

        String expected = "test1,test2\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#printRecord(Object[])}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordObjectArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.printRecord(new String[] { "test1", "test2" });
        printer.println();

        String expected = "test1,test2\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#printRecords(Iterable)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     * @throws SQLException
     *             If a database access error occurs
     */
    @org.junit.Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        Statement statement = TestUtils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM TEST");

        printer.println();
        printer.printRecords(resultSet);
        printer.println();

        CSVParser parser = TestUtils.getParser(out.toString());
        CSVRecord record;
        while ((record = parser.getNextRecord()) != null) {
            System.out.println(record);
        }
    }

    /**
     * Test case for {@link CSVPrinter#printRecords(Object[])}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsObjectArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.println();
        printer.printRecords(new String[][] { { "test1", "test2" }, { "test3", "test4" } });
        printer.println();

        String expected = "test1,test2\n test3,test4\n";
        String actual = out.toString();
        assertEquals(expected, actual);
    }
}