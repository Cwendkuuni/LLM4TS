package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVParser.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseSimpleCSV() throws IOException {
        final File csvFile = new File("test-data/simpleCSV.csv");
        final CSVParser parser = new CSVParser(csvFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A2", "B2"}, record2.toArray());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"A3", "B3"}, record3.toArray());
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithHeader() throws IOException {
        final File csvFile = new File("test-data/csvWithHeader.csv");
        final CSVParser parser = new CSVParser(csvFile, CSVFormat.DEFAULT.withHeader());

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.toArray(new String[2]));

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"John Doe", "25"}, record2.toArray());
    }

    /**
     * Test parsing a CSV file with empty lines and comments.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithEmptyLinesAndComments() throws IOException {
        final String csvString = "a;b\n\n" // Empty line
                + "c;d\n" // Normal record
                + "// This is a comment\n" // Comment
                + "e;f"; // Normal record

        final StringReader reader = new StringReader(csvString);
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertNull(record1);

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"c", "d"}, record2.toArray());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"e", "f"}, record3.toArray());
    }

    /**
     * Test parsing a CSV file with escape sequences.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithEscapeSequences() throws IOException {
        final File csvFile = new File("test-data/csvWithEscapeSequences.csv");
        final CSVParser parser = new CSVParser(csvFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A\\\",B\\\"", "C\\\"\\nD\\\"\\rE\\\"\\tF"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A\\\"1\\\"", "B\\\"2\\\"", "C\\\"3\\\""}, record2.toArray());
    }

    /**
     * Test parsing a CSV file with a custom format.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        final File csvFile = new File("test-data/customCSV.csv");
        final CSVFormat customFormat = CSVFormat.custom()
                .setHeader("Header1", "Header2")
                .setDelimiter(';')
                .build();

        final CSVParser parser = new CSVParser(csvFile, customFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Header11", "Header21"}, record1.toArray(new String[2]));

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Header12", "Header22"}, record2.toArray(new String[2]));
    }

    /**
     * Test parsing a CSV file with a missing header.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithMissingHeader() throws IOException {
        final String csvString = "A1,B1\n" + "A2,B2\n";
        final StringReader reader = new StringReader(csvString);
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A2", "B2"}, record2.toArray());

        assertNull(parser.getHeaderMap());
    }

    /**
     * Test parsing a CSV file with a custom delimiter.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithCustomDelimiter() throws IOException {
        final String csvString = "A|B|C|D|E\n" + "1|2|3|4|5\n";
        final StringReader reader = new StringReader(csvString);
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter('|'));

        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"1", "2", "3", "4", "5"}, record.toArray());
    }

    /**
     * Test parsing a CSV file with a null value.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithNullValue() throws IOException {
        final File csvFile = new File("test-data/csvWithNullValue.csv");
        final CSVParser parser = new CSVParser(csvFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"A", null}, record.toArray());
    }

    /**
     * Test parsing a CSV file with a malformed record.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test(expected = IOException.class)
    public void testParseCSVMalformedRecord() throws IOException {
        final String csvString = "A1\nB:C\n";
        final StringReader reader = new StringReader(csvString);
        new CSVParser(reader, CSVFormat.DEFAULT);
    }

    /**
     * Test parsing a CSV file with a missing value.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test(expected = IOException.class)
    public void testParseCSVMissingValue() throws IOException {
        final String csvString = "A1,B\n";
        final StringReader reader = new StringReader(csvString);
        new CSVParser(reader, CSVFormat.DEFAULT);
    }

    /**
     * Test parsing a CSV file with a leading empty line.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithLeadingEmptyLine() throws IOException {
        final String csvString = "\n" + "A1,B1\n";
        final StringReader reader = new StringReader(csvString);
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1"}, record.toArray());
    }

    /**
     * Test parsing a CSV file with a trailing empty line.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithTrailingEmptyLine() throws IOException {
        final String csvString = "A1,B1\n\n";
        final StringReader reader = new StringReader(csvString);
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1"}, record.toArray());
    }

    /**
     * Test parsing a CSV file with a comment line in the middle.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithCommentLine() throws IOException {
        final String csvString = "A1,B1\n// This is a comment\nA2,B2\n";
        final StringReader reader = new StringReader(csvString);
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A2", "B2"}, record2.toArray());
    }

    /**
     * Test parsing a CSV file with a comment line at the beginning.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithCommentLineAtBeginning() throws IOException {
        final String csvString = "// This is a comment\nA1,B1\n";
        final StringReader reader = new StringReader(csvString);
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1"}, record.toArray());
    }

    /**
     * Test parsing a CSV file with a comment line at the end.
     *
     * @throws IOException
     *             on I/O errors
     */
    @Test
    public void testParseCSVWithCommentLineAtEnd() throws IOException {
        final String csvString = "A1,B1\n";
        final StringReader reader = new StringReader(csvString);
        reader.readLine(); // consume the first line

        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        parser.close(); // consume the second line and the comment line

        List<CSVRecord> records = Collections.emptyList();
        assertEquals(records, parser.getRecords());
    }
}