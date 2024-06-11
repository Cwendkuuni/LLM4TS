package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVPrinter.Quote;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.DefaultCSVFormat;
import org.apache.commons.csv.QuoteMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        format = new DefaultCSVFormat(CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL));
    }

    @After
    public void tearDown() throws IOException {
        if (printer != null) {
            printer.close();
        }
    }

    @Test
    public void testPrint() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.print("Hello");
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("\"Hello\"", output);
    }

    @Test
    public void testPrintEmpty() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.print("");
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("\"\"", output);
    }

    @Test
    public void testPrintNull() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.print(null);
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("", output);
    }

    @Test
    public void testPrintQuotedString() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.print("Hello\"World");
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("\"Hello\"\"World\"", output);
    }

    @Test
    public void testPrintQuotedEmptyString() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.print("\"\"");
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("\"\"", output);
    }

    @Test
    public void testPrintQuotedNull() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.print(null);
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("\"\"", output);
    }

    @Test
    public void testPrintEmptyLine() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.println();
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("", output);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.printComment("This is a comment");
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("#This is a comment" + CR + LF, output);
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.printRecord(new Object[]{"Hello", "World"});
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("\"Hello\",\"World\"", output);
    }

    @Test
    public void testPrintRecordWithComment() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.printComment("This is a comment");
        printer.printRecord(new Object[]{"Hello", "World"});
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("#This is a comment" + CR + LF + "\"Hello\",\"World\"", output);
    }

    @Test
    public void testPrintRecordWithNullValue() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.printRecord(new Object[]{"Hello", null, "World"});
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("\"Hello\",\"\",\"World\"", output);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        printer = new CSVPrinter(outputStream, format);
        printer.printRecords(CSVTestData.getRecords("TestData.csv"));
        printer.close();
        final String output = outputStream.toString();
        final CSVRecordIterator it = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(output.getBytes());
        CSVRecord record;
        while ((record = it.nextRecord()) != null) {
            System.out.println(record.getValues());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        printer = new CSVPrinter(outputStream, null);
    }

    @Test(expected = IOException.class)
    public void testInvalidOut() throws IOException {
        printer = new CSVPrinter(null, format);
    }

    @Test(expected = IOException.class)
    public void testPrintNullFormat() throws IOException {
        printer = new CSVPrinter(outputStream, null);
    }

    @Test(expected = IOException.class)
    public void testPrintWithoutQuoteAndEscape() throws IOException {
        printer = new CSVPrinter(outputStream, format.withQuoteMode(null).withEscapeMode(null));
        printer.print("Hello\nWorld");
        printer.flush();
        final String output = outputStream.toString();
        assertEquals("Hello\nWorld", output);
    }

    @Test(expected = IOException.class)
    public void testPrintEscapedCharacter() throws IOException {
        printer = new CSVPrinter(outputStream, format);
        printer.print(format.getEscape().charValue());
        printer.flush();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidQuotePolicy() throws IOException {
        printer = new CSVPrinter(outputStream, format.withQuotePolicy(null));
    }
}