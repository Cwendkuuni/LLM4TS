package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.TYPE;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testSimpleCSV() throws IOException {
        // Given
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles\n";
        CSVFormat format = new CSVFormat('\t', '\n');

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getIntValue());
        assertEquals("New York", firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.getHeader());
        assertEquals(3, secondRecord.size());
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getIntValue());
        assertEquals("Los Angeles", secondRecord.get(2));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom format.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCustomCSVFormat() throws IOException {
        // Given
        String csvContent = "Name;Age;City\nJohn Doe;30;New York\nJane Doe;25;Los Angeles\n";
        CSVFormat format = new CSVFormat(',', ';');

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getIntValue());
        assertEquals("New York", firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.getHeader());
        assertEquals(3, secondRecord.size());
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getIntValue());
        assertEquals("Los Angeles", secondRecord.get(2));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a missing header.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testMissingHeader() throws IOException {
        // Given
        String csvContent = "John Doe,30,New York\nJane Doe,25,Los Angeles\n";
        CSVFormat format = new CSVFormat();

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertNull(firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getIntValue());
        assertEquals("New York", firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertNull(secondRecord.getHeader());
        assertEquals(3, secondRecord.size());
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getIntValue());
        assertEquals("Los Angeles", secondRecord.get(2));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom delimiter.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCustomDelimiter() throws IOException {
        // Given
        String csvContent = "Name|Age|City\nJohn Doe||New York\nJane Doe|25|Los Angeles\n";
        CSVFormat format = new CSVFormat('|');

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertNull(firstRecord.get(1));
        assertEquals("New York", firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.getHeader());
        assertEquals(3, secondRecord.size());
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getIntValue());
        assertEquals("Los Angeles", secondRecord.get(2));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a comment.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCSVWithComment() throws IOException {
        // Given
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\n# This is a comment\nJane Doe,25,Los Angeles\n";
        CSVFormat format = new CSVFormat();

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getIntValue());
        assertEquals("New York", firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.getHeader());
        assertEquals(3, secondRecord.size());
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getIntValue());
        assertEquals("Los Angeles", secondRecord.get(2));

        String comment = parser.getRecords().get(1).getComment();
        assertEquals("# This is a comment", comment);

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom header.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCustomHeader() throws IOException {
        // Given
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles\n";
        CSVFormat format = new CSVFormat(null, null, new String[]{"Name", "Age", "Location"});

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "Location"}, firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getIntValue());
        assertEquals("New York", firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "Location"}, secondRecord.getHeader());
        assertEquals(3, secondRecord.size());
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getIntValue());
        assertEquals("Los Angeles", secondRecord.get(2));

        parser.close();
    }

    /**
     * Test parsing an empty CSV file.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testEmptyCSVFile() throws IOException {
        // Given
        String csvContent = "";
        CSVFormat format = new CSVFormat();

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());

        parser.close();
    }

    /**
     * Test parsing a malformed CSV file.
     *
     * @throws IOException on error
     */
    @org.junit.Test(expected = IOException.class)
    public void testMalformedCSVFile() throws IOException {
        // Given
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,\n";
        CSVFormat format = new CSVFormat();

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getIntValue());
        assertTrue(firstRecord.get(2) instanceof CSVRecord.NullToken);

        parser.close();

        // And the method should throw an exception when trying to parse the next record
        List<CSVRecord> records2 = parser.getRecords();
        fail();
    }

    /**
     * Test parsing a CSV file with a malformed header.
     *
     * @throws IOException on error
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testMalformedHeader() throws IOException {
        // Given
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles\n";
        CSVFormat format = new CSVFormat(',', null, new String[]{"Name", "Age", "City", null});

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        fail();
    }

    /**
     * Test parsing a CSV file with a custom header and a comment.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCSVWithCustomHeaderAndComment() throws IOException {
        // Given
        String csvContent = "Name,Age,City\n# This is a comment\nJohn Doe,30,New York\nJane Doe,25,Los Angeles\n";
        CSVFormat format = new CSVFormat(null, null, new String[]{"Name", "Age", "Location"});

        // When
        CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "Location"}, firstRecord.getHeader());
        assertEquals(3, firstRecord.size());
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getIntValue());
        assertEquals("New York", firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "Location"}, secondRecord.getHeader());
        assertEquals(3, secondRecord.size());
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getIntValue());
        assertEquals("Los Angeles", secondRecord.get(2));

        String comment = parser.getRecords().get(0).getComment();
        assertEquals("# This is a comment", comment);

        parser.close();
    }

}