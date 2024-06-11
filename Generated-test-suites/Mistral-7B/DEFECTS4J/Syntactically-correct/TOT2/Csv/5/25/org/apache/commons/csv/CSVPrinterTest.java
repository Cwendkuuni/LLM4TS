package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Tests for the {@link CSVPrinter} class.
 */
public final class CSVPrinterTest {

    /**
     * Test method for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)}.
     */
    @org.junit.Test
    public void testCSVPrinterConstruction() {
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final Appendable out = new StringWriter();
        final CSVPrinter printer = new CSVPrinter(out, format);
        assertNotNull(printer);
        assertEquals(out, printer.getOut());
        assertEquals(format, printer.getFormat());
    }

    /**
     * Test method for {@link CSVPrinter#close()}.
     */
    @org.junit.Test
    public void testCSVPrinterClose() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);
        printer.close();
        assertNotNull(printer.getOut());
        assertTrue(printer.getOut() instanceof Closeable);
        assertTrue(printer.getOut() instanceof Flushable);
        assertFalse(printer.getOut() instanceof CSVPrinter);
    }

    /**
     * Test method for {@link CSVPrinter#flush()}.
     */
    @org.junit.Test
    public void testCSVPrinterFlush() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);
        printer.flush();
        assertNotNull(printer.getOut());
        assertTrue(printer.getOut() instanceof Closeable);
        assertTrue(printer.getOut() instanceof Flushable);
        assertFalse(printer.getOut() instanceof CSVPrinter);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintObject() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        printer.print(null);
        printer.print("Hello, World!");
        printer.print(12345);
        printer.print(true);
        printer.print(new ArrayList<>());
        printer.print(List.of("Hello", "World"));
        printer.print(Arrays.asList("Hello", "World"));

        printer.close();
        final String expectedOutput = "\"\",Hello, World!\",12345,true,\"[\\],\"[Hello, World]\",\"[Hello, World]\"\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintAndQuote() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        printer.printAndQuote(null, null, 0, 0);
        printer.printAndQuote("Hello, World!", "Hello, World!", 0, 13);
        printer.printAndQuote("Hello, World!\n", "Hello, World!\n", 0, 14);
        printer.printAndQuote("Hello, World!\"", "Hello, World!\"", 0, 15);
        printer.printAndQuote("Hello, World!\"\n", "Hello, World!\"\n", 0, 16);
        printer.printAndQuote("Hello, World!\"Hello, World!", "Hello, World!\"Hello, World!", 0, 20);

        printer.close();
        final String expectedOutput = "\"\",\"Hello, World!\",\"Hello, World!\",\"Hello, World!\",\"Hello, World!\\n\",\"Hello, World!\\\"\",\"Hello, World!\\\"Hello, World!\"\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printAndEscape(CharSequence, int, int)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintAndEscape() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        printer.printAndEscape(null, 0, 0);
        printer.printAndEscape("", 0, 0);
        printer.printAndEscape("Hello, World!", 0, 13);
        printer.printAndEscape("Hello, World!\n", 0, 14);
        printer.printAndEscape("Hello, World!\"", 0, 15);
        printer.printAndEscape("Hello, World!\"\n", 0, 16);
        printer.printAndEscape("Hello, World!\"Hello, World!", 0, 20);

        printer.close();
        final String expectedOutput = "\"\",\"\",\"Hello, World!\",\"Hello, World!\",\"Hello, World!\\n\",\"Hello, World!\\\"\",\"Hello, World!\\\"Hello, World!\"\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#print(Object, CharSequence, int, int)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrint() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        printer.print('A', null, 0, 1);
        printer.print("Hello, World!", null, 0, 13);
        printer.print("Hello, World!\n", null, 0, 14);
        printer.print("Hello, World!\"", null, 0, 15);
        printer.print("Hello, World!\"\n", null, 0, 16);
        printer.print("Hello, World!\"Hello, World!", null, 0, 20);
        printer.print(12345, null, 0, 5);
        printer.print(true, null, 0, 4);
        printer.print(new ArrayList<>(), null, 0, 4);
        printer.print(List.of("Hello", "World"), null, 0, 9);
        printer.print(Arrays.asList("Hello", "World"), null, 0, 9);

        printer.close();
        final String expectedOutput = "A,\"Hello, World!\",\"Hello, World!\",\"Hello, World!\\n\",\"Hello, World!\\\"\",\"Hello, World!\\\"Hello, World!\",12345,true,[ ],[Hello, World],[Hello, World]";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printComment(String)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintComment() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        printer.printComment("# This is a comment\n");
        printer.printRecord(Arrays.asList("A", "B"));
        printer.println();
        printer.printComment("# This is another comment\n");
        printer.printRecord(Arrays.asList("C", "D"));

        printer.close();
        final String expectedOutput = "# This is a comment\nA,B\n# This is another comment\nC,D\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#println()}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintln() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        printer.printRecord(Arrays.asList("A", "B"));
        printer.println();
        printer.printRecord(Arrays.asList("C", "D"));
        printer.println();
        printer.printComment("# This is a comment\n");
        printer.println();
        printer.printRecord(Arrays.asList("E", "F"));
        printer.println();

        printer.close();
        final String expectedOutput = "A,B\nC,D\n# This is a comment\nE,F\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Iterable)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintRecordIterable() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        final List<String> values = Arrays.asList("A", "B");
        printer.printRecord(values);
        printer.println();
        printer.printRecord(Arrays.asList("C", "D"));
        printer.println();

        printer.close();
        final String expectedOutput = "A,B\nC,D\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Object[])}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintRecordObjectArray() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        final Object[] values = new Object[] {"A", "B"};
        printer.printRecord(values);
        printer.println();
        printer.printRecord(new Object[] {"C", "D"});
        printer.println();

        printer.close();
        final String expectedOutput = "A,B\nC,D\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintRecordsIterable() throws IOException, SQLException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        final List<List<String>> records = Arrays.asList(Arrays.asList("A0", "B0"), Arrays.asList("A1", "B1"), Arrays.asList("A2", "B2"));
        printer.printRecords(records);

        printer.close();
        final String expectedOutput = "A0,B0\nA1,B1\nA2,B2\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Object[])}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintRecordsObjectArray() throws IOException, SQLException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        final Object[][] records = new Object[][] {{"A0", "B0"}, {"A1", "B1"}, {"A2", "B2"}};
        printer.printRecords(records);

        printer.close();
        final String expectedOutput = "A0,B0\nA1,B1\nA2,B2\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(ResultSet)}.
     */
    @org.junit.Test
    public void testCSVPrinterPrintRecordsResultSet() throws IOException, SQLException {
        final StringWriter stringWriter = new StringWriter();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        final CSVPrinter printer = new CSVPrinter(stringWriter, format);

        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("A0", "A1", "A2");
        when(resultSet.getString(2)).thenReturn("B0", "B1", "B2");

        printer.printRecords(resultSet);

        printer.close();
        final String expectedOutput = "A0,B0\nA1,B1\nA2,B2\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }
}