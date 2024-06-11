package org.apache.commons.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outStream;

    @Before
    public void setup() throws IOException {
        outStream = new ByteArrayOutputStream();
        StringWriter sw = new StringWriter(outStream);
        printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
    }

    @After
    public void teardown() throws IOException {
        printer.close();
    }

    @Test
    public void testBasicUsage() throws IOException {
        printer.printRecord(Arrays.asList("apple", "banana", "orange"));
        printer.println();
        printer.printRecord(Arrays.asList("cat", "dog", "elephant"));
        printer.flush();

        String output = outStream.toString();
        String expected = "apple,banana,orange\ncat,dog,elephant\n";
        assertEquals(expected, output);
    }

    @Test
    public void testComment() throws IOException {
        printer.printComment("# This is a comment.");
        printer.println();
        printer.printRecord(Arrays.asList("apple", "banana", "orange"));
        printer.flush();

        String output = outStream.toString();
        String expected = "# This is a comment.\napple,banana,orange\n";
        assertEquals(expected, output);
    }

    @Test
    public void testNull() throws IOException {
        printer.printRecord(Arrays.asList("apple", null, "orange"));
        printer.println();
        printer.printRecord(Arrays.asList(null, "banana", "orange"));
        printer.flush();

        String output = outStream.toString();
        String expected = "\"\",\"\",\"orange\"\nnull,\"banana\",\"orange\"\n";
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        new CSVPrinter(outStream, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOut() throws IOException {
        new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IOException.class)
    public void testPrintNullAfterFlush() throws IOException {
        printer.flush();
        printer.print(null);
    }

    @Test(expected = IOException.class)
    public void testPrintAfterClose() throws IOException {
        printer.close();
        printer.print("apple");
    }

    @Test
    public void testEscapeCharacters() throws IOException {
        printer.setEscapeChar('\u0011');
        printer.printRecord(Arrays.asList("apple\u0011banana\u0011orange"));
        printer.println();
        printer.printRecord(Arrays.asList("cat\u0011dog\u0011elephant"));
        printer.flush();

        String output = outStream.toString();
        String expected = "apple\\u0011banana\\u0011orange\ncat\\u0011dog\\u0011elephant\n";
        assertEquals(expected, output);
    }

    @Test
    public void testQuote() throws IOException {
        printer.setQuoteChar('|');
        printer.printRecord(Arrays.asList("\"apple\",\"banana\",\"orange\""));
        printer.println();
        printer.printRecord(Arrays.asList("|cat|,|dog|,|elephant|"));
        printer.flush();

        String output = outStream.toString();
        String expected = "\"|apple|,|banana|,|orange|\"\n|cat|,|dog|,|elephant|\n";
        assertEquals(expected, output);
    }

    @Test
    public void testRecordSeparator() throws IOException {
        printer.setRecordSeparator("---\n");
        printer.printRecord(Arrays.asList("apple", "banana", "orange"));
        printer.flush();

        String output = outStream.toString();
        String expected = "apple,banana,orange\n---\n";
        assertEquals(expected, output);
    }

    @Test
    public void testCommentCharacter() throws IOException {
        printer.setCommentStart('#');
        printer.printComment("# This is a comment.");
        printer.println();
        printer.printRecord(Arrays.asList("apple", "banana", "orange"));
        printer.flush();

        String output = outStream.toString();
        String expected = "# This is a comment.\napple,banana,orange\n";
        assertEquals(expected, output);
    }

}