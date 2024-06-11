package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.SP;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test printing string values.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintStringValues() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print("Hello");
            csvPrinter.print("World");
            csvPrinter.println();
        }

        final String expected = "Hello" + SP + "World" + CR;
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing null values.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintNullValues() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print(null);
            csvPrinter.print("Value");
            csvPrinter.println();
        }

        final String expected = CSVFormat.DEFAULT.getNullString() + SP + "Value" + CR;
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test escaping values.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testEscapeValues() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT.withEscape('\\'))) {
            csvPrinter.print("Hello, World!");
            csvPrinter.println();
        }

        final String expected = "Hello, World!\r\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test quoting values.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testQuoteValues() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT.withQuote('"'))) {
            csvPrinter.print("Hello, World!");
            csvPrinter.println();
        }

        final String expected = "\"" + "Hello, World!" + "\"" + CR;
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing multiple records.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintMultipleRecords() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print("First");
            csvPrinter.print("record");
            csvPrinter.println();
            csvPrinter.print("Second");
            csvPrinter.print("record");
            csvPrinter.println();
        }

        final String expected = "First" + SP + "record" + CR + "Second" + SP + "record" + CR;
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing comment.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintComment() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT.withComment("//"))) {
            csvPrinter.printComment("This is a comment.");
            csvPrinter.println();
            csvPrinter.print("This is a value.");
            csvPrinter.println();
        }

        final String expected = "// This is a comment.\r\n" + "This is a value.\r\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing record separator.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintRecordSeparator() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print("Value 1");
            csvPrinter.print(CSVFormat.DEFAULT.getRecordSeparator());
            csvPrinter.print("Value 2");
            csvPrinter.println();
        }

        final String expected = "Value 1\r\n" + CSVFormat.DEFAULT.getRecordSeparator() + "Value 2" + CR;
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing single and multiple line comments.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintComments() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT.withComment("//"))) {
            csvPrinter.printComment("Single line comment.");
            csvPrinter.println();
            csvPrinter.printComment("Multi line comment.\r\nThis is part one.");
            csvPrinter.printComment("This is part two.");
            csvPrinter.println();
            csvPrinter.print("Value");
            csvPrinter.println();
        }

        final String expected = "// Single line comment.\r\n" + "// Multi line comment.\r\n// This is part one.\r\n// This is part two.\r\nValue\r\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test closing printer.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testClosePrinter() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print("Hello");
            csvPrinter.print("World");
            csvPrinter.println();
            csvPrinter.close();
        }

        final String expected = "Hello" + SP + "World" + CR;
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Test printing with different quoting policies.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testQuotePolicies() throws IOException {
        final ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter1 = new CSVPrinter(new PrintWriter(outputStream1), CSVFormat.DEFAULT.withQuote(Quote.MINIMAL))) {
            csvPrinter1.print("Hello World!");
            csvPrinter1.println();
        }

        final ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter2 = new CSVPrinter(new PrintWriter(outputStream2), CSVFormat.DEFAULT.withQuote(Quote.NON_NUMERIC))) {
            csvPrinter2.print("Hello World!");
            csvPrinter2.println();
        }

        final ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter3 = new CSVPrinter(new PrintWriter(outputStream3), CSVFormat.DEFAULT.withQuote(Quote.ALL))) {
            csvPrinter3.print("Hello World!");
            csvPrinter3.println();
        }

        final String outputStream1String = outputStream1.toString();
        final String outputStream2String = outputStream2.toString();
        final String outputStream3String = outputStream3.toString();

        final String expected1 = "Hello World!";
        final String expected2 = "\"" + "Hello World!" + "\"" + CR;
        final String expected3 = "\"" + "Hello World!" + "\"" + CR;

        assertEquals(expected1, outputStream1String);
        assertEquals(expected2, outputStream2String);
        assertEquals(expected3, outputStream3String);
    }

    /**
     * Test when an illegal argument is passed to the constructor.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    /**
     * Test when an illegal argument is passed to the print method with a null value.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintNullValuesException() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print(null);
        }

        assertThrows(NullPointerException.class, () -> outputStream.toString());
    }

    /**
     * Test when an illegal argument is passed to the print method with an invalid character.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPrintInvalidCharacterException() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print((char) 0);
        }

        assertThrows(IllegalArgumentException.class, () -> outputStream.toString());
    }

    /**
     * Test parsing the generated CSV data.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParsePrintedData() throws IOException, SQLException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.print("Hello");
            csvPrinter.print("World");
            csvPrinter.println();
            csvPrinter.print("1");
            csvPrinter.print("Two");
            csvPrinter.println();
            csvPrinter.printComment("This is a comment.");
            csvPrinter.println();
            csvPrinter.print("Value");
            csvPrinter.println();
            csvPrinter.printRecords(new Object[]{"First", "record"});
            csvPrinter.println();
            csvPrinter.close();
        }

        final String csvData = outputStream.toString();

        try (final CSVParser csvParser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT)) {
            final CSVRecord record1 = csvParser.getRecord();
            assertEquals("Hello", record1.get(0));
            assertEquals("World", record1.get(1));

            final CSVRecord record2 = csvParser.getRecord();
            assertEquals("1", record2.get(0));
            assertEquals("Two", record2.get(1));

            final CSVRecord recordComment = csvParser.getRecord();
            assertFalse(recordComment.isPresent());

            final CSVRecord record3 = csvParser.getRecord();
            assertEquals("Value", record3.get(0));

            final CSVRecord record4 = csvParser.getRecord();
            assertEquals("First", record4.get(0));
            assertEquals("record", record4.get(1));
        }
    }
}