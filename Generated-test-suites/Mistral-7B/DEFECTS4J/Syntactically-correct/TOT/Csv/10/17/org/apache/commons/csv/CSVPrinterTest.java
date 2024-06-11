package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    @Test
    public void testPrintSimpleValues() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT);

        printer.print("John");
        printer.print("Doe");
        printer.println();

        printer.print(42);
        printer.print(3.14);
        printer.println();

        printer.print(true);
        printer.println();

        printer.close();

        final String result = output.toString();
        final String expected = "John,Doe\n" +
                "42,3.14\n" +
                "true\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintQuotedValues() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT.withQuoteAll(true));

        printer.print("\"John\"");
        printer.print("Doe");
        printer.println();

        printer.print("1,2,3");
        printer.println();

        printer.print("\"John,\"Do\"");
        printer.println();

        printer.print("\"John, Doe\"");
        printer.println();

        printer.close();

        final String result = output.toString();
        final String expected = "\"John\",\"Doe\"\n" +
                "1,\"2,3\"\n" +
                "\"John,\"Do\"\n" +
                "\"John, Doe\"\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintEmptyValues() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT);

        printer.print("");
        printer.println();

        printer.print(null);
        printer.println();

        printer.print("");
        printer.print("");
        printer.println();

        printer.close();

        final String result = output.toString();
        final String expected = ",,\n," +
                ",\n,,\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintAndEscapeSpecialChars() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT.withEscape('\\'));

        printer.print("John,Do\\e\"e\"");
        printer.println();

        printer.close();

        final String result = output.toString();
        final String expected = "John,Do\\e\\\"e\\\"\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintRecords() throws IOException {
        final StringWriter output = new StringWriter();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT);

        printer.printRecord( Arrays.asList("John", "Doe"));
        printer.printRecord( Arrays.asList(42, 3.14));
        printer.printRecord( Arrays.asList(true));
        printer.println();

        printer.close();

        final String result = output.toString();
        final String expected = "John,Doe\n" +
                "42,3.14\n" +
                "true\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordsWithEmptyValues() throws IOException {
        final StringWriter output = new StringWriter();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT);

        printer.printRecords( Arrays.asList( Arrays.asList("", ""), Arrays.asList(null, null), Arrays.asList("", "", "")));
        printer.println();

        printer.close();

        final String result = output.toString();
        final String expected = ",,\n," +
                ",\n,,\n," +
                ",,\n,,\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintComment() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT);

        printer.printComment("This is a comment");
        printer.println();

        printer.close();

        final String result = output.toString();
        final String expected = "\u0023This is a comment\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordsWithComplexValues() throws IOException, SQLException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(output, CSVFormat.DEFAULT);

        printer.printRecords( Arrays.asList(
                Arrays.asList("John", "\"Doe\\, Jr.\""),
                Arrays.asList(42, 3.14),
                Arrays.asList(true),
                Arrays.asList("", ""),
                Arrays.asList(null, null),
                Arrays.asList("John, Doe"),
                Arrays.asList("John Doe")
        ));

        printer.printComment("This is a comment");
        printer.println();

        printer.printRecords(new Object[][]{
                {"John", "\"Doe, Jr.\""},
                {42, 3.14},
                {true},
                {}, {}, {},
                {"John, Doe"},
                {"John Doe"}
        });

        printer.close();

        final String result = output.toString();
        final String expected = "John,\"Doe, Jr.\"\n" +
                "42,3.14\n" +
                "true\n" +
                ",,\n" +
                ",\n" +
                ",,\n" +
                "John, Doe\n" +
                "John Doe\n" +
                "\u0023This is a comment\n";

        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        new CSVPrinter(new StringWriter(), null);
    }

    @Test(expected = IOException.class)
    public void testPrintWithNullStream() throws IOException {
        new CSVPrinter(null, CSVFormat.DEFAULT);
    }
}