package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVParserBuilder;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Unit tests for CSVParser class.
 */
public class CSVParserTest {

    /**
     * Test parsing a CSV file with a valid format.
     */
    @Test
    public void testParseValidFormat() throws IOException {
        String csvContent = "name,age\nJohn Doe,35\nMary Smith,28";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            assertEquals(2, map.size());
            assertTrue(map.containsKey("name"));
            assertTrue(map.containsKey("age"));
            assertNotNull(map.get("name"));
            assertNotNull(map.get("age"));
        }

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @Test
    public void testParseCustomFormat() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,35\nMary Smith,28";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                new CSVFormatBuilder().setHeaderNames(new String[]{"Name", "Age"}).build());
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            assertEquals(2, map.size());
            assertTrue(map.containsKey("Name"));
            assertTrue(map.containsKey("Age"));
            assertNotNull(map.get("Name"));
            assertNotNull(map.get("Age"));
        }

        parser.close();
    }

    /**
     * Test parsing a CSV file with an empty format.
     */
    @Test
    public void testParseEmptyFormat() throws IOException {
        String csvContent = "name,age\nJohn Doe,35\nMary Smith,28";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                CSVFormat.EMPTY);
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertNotNull(records);
        assertEquals(0, records.size());

        parser.close();
    }

    /**
     * Test parsing a CSV file with an invalid format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() throws IOException {
        String csvContent = "name,age\nJohn Doe,35\nMary Smith,28";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)), null);
    }

    /**
     * Test parsing a CSV file with no header.
     */
    @Test
    public void testParseNoHeader() throws IOException {
        String csvContent = "35,John Doe\n28,Mary Smith";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT.withHeaderCount(0));
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            assertEquals(2, map.size());
            assertTrue(map.containsKey(0));
            assertTrue(map.containsKey(1));
            assertNotNull(map.get(0));
            assertNotNull(map.get(1));
        }

        parser.close();
    }

    /**
     * Test parsing a CSV file with empty lines.
     */
    @Test
    public void testParseEmptyLines() throws IOException {
        String csvContent = "name,age\nJohn Doe,35\n\nMary Smith,28";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            assertEquals(2, map.size());
            assertTrue(map.containsKey("name"));
            assertTrue(map.containsKey("age"));
            assertNotNull(map.get("name"));
            assertNotNull(map.get("age"));
        }

        parser.close();
    }

    /**
     * Test parsing a CSV file with comments.
     */
    @Test
    public void testParseWithComments() throws IOException {
        String csvContent = "name,age\nJohn Doe,35\n-- This is a comment\nMary Smith,28";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            assertEquals(2, map.size());
            assertTrue(map.containsKey("name"));
            assertTrue(map.containsKey("age"));
            assertNotNull(map.get("name"));
            assertNotNull(map.get("age"));
            assertEquals("-- This is a comment", record.getComment());
        }

        parser.close();
    }

    /**
     * Test parsing a CSV file with missing values.
     */
    @Test
    public void testParseMissingValues() throws IOException {
        String csvContent = "name,age\nJohn Doe,,Mary Smith,28";

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Map<String, Object> map = record.toMap();
        assertEquals(3, map.size());
        assertTrue(map.containsKey("name"));
        assertTrue(map.containsKey("age"));
        assertTrue(map.containsKey(""));
        assertNull(map.get("age"));
        assertEquals("", record.get(1));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom header map.
     */
    @Test
    public void testParseCustomHeaderMap() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,35\nMary Smith,28";

        Map<String, Integer> headerMap = Collections.singletonMap("Name", 0);
        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT);
        parser.setHeaderMap(headerMap);
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            assertEquals(2, map.size());
            assertTrue(map.containsKey("Name"));
            assertTrue(map.containsKey("Age"));
            assertNotNull(map.get("Name"));
            assertNotNull(map.get("Age"));
            assertEquals("Name", map.get("Name").toString());
        }

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom format and custom header map.
     */
    @Test
    public void testParseCustomFormatAndHeaderMap() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,35\nMary Smith,28";

        Map<String, Integer> headerMap = new HashMap<>();
        headerMap.put("Name", 0);
        headerMap.put("Age", 1);

        CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(new String[]{"Name", "Age"})
                .setHeaderMap(headerMap)
                .build();

        CSVParser parser = parse(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)), format);
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(parser);
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            assertEquals(2, map.size());
            assertTrue(map.containsKey("Name"));
            assertTrue(map.containsKey("Age"));
            assertNotNull(map.get("Name"));
            assertNotNull(map.get("Age"));
            assertEquals("Name", map.get("Name").toString());
            assertEquals("Age", map.get("Age").toString());
        }

        parser.close();
    }
}