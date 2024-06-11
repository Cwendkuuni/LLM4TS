package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(outputStream),
                new CSVFormat(CSVFormat.DEFAULT.withQuote('"'), '\n'));
    }

    /**
     * Test printing a single string.
     *
     * @throws IOException
     */
    @Test
    public void testPrintString() throws IOException {
        printer.print("Hello, world!");
        final String expected = "Hello, world!\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing null value.
     *
     * @throws IOException
     */
    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        final String expected = "";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing iterable values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintIterable() throws IOException {
        printer.printRecord(new String[]{"String 1", "String 2", "String 3"});
        final String expected = "String 1,String 2,String 3\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing ResultSet values.
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testPrintResultSet() throws SQLException, IOException {
        final String[] data = {"Name", "Age", "Country"};
        final Object[][] records = {{"John Doe", 30, "USA"}, {"Jane Doe", 25, "Canada"}};

        printer.printRecords(data, records);

        final String expected = "Name,Age,Country\n"
                + "John Doe,30,USA\n"
                + "Jane Doe,25,Canada\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test escaping characters.
     *
     * @throws IOException
     */
    @Test
    public void testEscapeChars() throws IOException {
        printer = new CSVPrinter(new OutputStreamWriter(outputStream),
                new CSVFormat(CSVFormat.DEFAULT.withQuote('"'), '\n')
                        .withEscape('\\'));

        printer.print("\"Test\"\n");
        final String expected = "\"Test\"\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test quoting characters.
     *
     * @throws IOException
     */
    @Test
    public void testQuoteChars() throws IOException {
        printer = new CSVPrinter(new OutputStreamWriter(outputStream),
                new CSVFormat(CSVFormat.DEFAULT.withQuote('"'), '\n')
                        .withQuotePolicy(Quote.ALL));

        printer.print("\"Test\"\n");
        final String expected = "\"Test\"\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing empty record.
     *
     * @throws IOException
     */
    @Test
    public void testPrintEmptyRecord() throws IOException {
        printer.println();
        final String expected = "\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing comment.
     *
     * @throws IOException
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        final String expected = "#This is a comment\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test closing the printer.
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        printer.close();
        final String expected = "";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test handling invalid format.
     *
     * @throws IOException
     */
    @Test
    public void testInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CSVPrinter(new OutputStreamWriter(outputStream), null);
        });
    }
}