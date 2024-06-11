package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with no header.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "a,b\n1,2\n3,4";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the parsed records
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"1", "2"}, record1.values());
        assertArrayEquals(new String[]{"3", "4"}, record2.values());

        // Check the header map
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNull(headerMap);

        // Check the final position in the input stream
        assertTrue(parser.isClosed());
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseCSVWithHeader() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the parsed records
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"John"}, record1.getNames());
        assertArrayEquals(new String[]{"30"}, record1.getValues());
        assertArrayEquals(new String[]{"Mary"}, record2.getNames());
        assertArrayEquals(new String[]{"25"}, record2.getValues());

        // Check the header map
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Map<String, Integer> expectedHeaderMap = new LinkedHashMap<String, Integer>();
        expectedHeaderMap.put("Name", 0);
        expectedHeaderMap.put("Age", 1);
        assertArrayEquals(expectedHeaderMap.toArray(new Map.Entry[expectedHeaderMap.size()][]), headerMap.entrySet()
                .toArray());

        // Check the final position in the input stream
        assertTrue(parser.isClosed());
    }

    /**
     * Test parsing a CSV file with empty rows.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseCSVWithEmptyRows() throws IOException {
        String csvData = "Name,Age\nJohn,30\n\nMary,25";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the parsed records
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"John"}, record1.getNames());
        assertArrayEquals(new String[]{"30"}, record1.getValues());
        assertArrayEquals(new String[]{"Mary"}, record2.getNames());
        assertArrayEquals(new String[]{}, record2.getValues());

        // Check the final position in the input stream
        assertTrue(parser.isClosed());
    }

    /**
     * Test parsing a CSV file with a comment.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseCSVWithComment() throws IOException {
        String csvData = "Name,Age\nJohn,30# This is a comment\nMary,25";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the parsed records
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"John"}, record1.getNames());
        assertArrayEquals(new String[]{"30"}, record1.getValues());
        assertArrayEquals(new String[]{"Mary"}, record2.getNames());
        assertArrayEquals(new String[]{"25"}, record2.getValues());
        assertFalse(record2.isCommented());

        // Check the comment
        String comment = record1.getComment();
        assertEquals(" This is a comment", comment);

        // Check the final position in the input stream
        assertTrue(parser.isClosed());
    }

    /**
     * Test parsing a CSV file with invalid data.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary Age:25";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        parser.getRecords();
    }

    /**
     * Test parsing a CSV file with a missing header.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseCSVWithoutHeader() throws IOException {
        String csvData = "1,2\n3,4";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the parsed records
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"1"}, record1.getValues());
        assertArrayEquals(new String[]{"2"}, record1.getValues());
        assertArrayEquals(new String[]{"3"}, record2.getValues());
        assertArrayEquals(new String[]{"4"}, record2.getValues());

        // Check the header map
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNull(headerMap);

        // Check the final position in the input stream
        assertTrue(parser.isClosed());
    }

    /**
     * Test parsing a CSV file with a missing record.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test(expected = NoSuchElementException.class)
    public void testParseCSVWithMissingRecord() throws IOException {
        String csvData = "Name,Age\nJohn,30";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        parser.getRecords().get(1);
    }

    /**
     * Test closing the parser after all records have been parsed.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseCSVClose() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25";
        CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the parsed records
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"John"}, record1.getNames());
        assertArrayEquals(new String[]{"30"}, record1.getValues());
        assertArrayEquals(new String[]{"Mary"}, record2.getNames());
        assertArrayEquals(new String[]{"25"}, record2.getValues());

        // Check the final position in the input stream
        assertTrue(parser.isClosed());

        // Try closing again, should do nothing
        parser.close();
    }
}