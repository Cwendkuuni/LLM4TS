package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(java.io.File, CSVFormat)}
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseFile() throws IOException {
        File testFile = new File("CSVParserTest.csv");
        try (CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(5, records.size());
            CSVRecord record = records.get(0);
            assertEquals(Arrays.asList("Name", "Age", "City"), record.getHeaderMap().values());
            assertEquals(Arrays.asList("John", "30", "New York"), record.getValues());
        }
        assertTrue(testFile.delete());
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)}
     */
    @org.junit.Test
    public void testParseString() {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (CSVParser parser = CSVParser.parse(testString, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertEquals(Arrays.asList("Name", "Age", "City"), record.getHeaderMap().values());
            assertEquals(Arrays.asList("John", "30", "New York"), record.getValues());
        }
    }

    /**
     * Test method for {@link CSVParser#parse(URL, java.nio.charset.Charset, CSVFormat)}
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseURL() throws IOException {
        String testURLString = "http://example.com/test.csv";
        try (URL testURL = new URL(testURLString)) {
            try (InputStreamReader reader = new InputStreamReader(testURL.openStream(), StandardCharsets.UTF_8);
                 CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT)) {
                List<CSVRecord> records = parser.getRecords();
                assertEquals(5, records.size());
                CSVRecord record = records.get(0);
                assertEquals(Arrays.asList("Name", "Age", "City"), record.getHeaderMap().values());
                assertEquals(Arrays.asList("John", "30", "New York"), record.getValues());
            }
        }
    }

    /**
     * Test method for {@link CSVParser#getRecords()}
     */
    @org.junit.Test
    public void testGetRecords() throws IOException {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (CSVParser parser = CSVParser.parse(testString, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertEquals(Arrays.asList("Name", "Age", "City"), record.getHeaderMap().values());
            assertEquals(Arrays.asList("John", "30", "New York"), record.getValues());
        }
    }

    /**
     * Test method for {@link CSVParser#getHeaderMap()}
     */
    @org.junit.Test
    public void testGetHeaderMap() throws IOException {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (CSVParser parser = CSVParser.parse(testString, CSVFormat.DEFAULT)) {
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertEquals(3, headerMap.size());
            assertEquals("Name", headerMap.get(0));
            assertEquals("Age", headerMap.get(1));
            assertEquals("City", headerMap.get(2));
        }
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()}
     */
    @org.junit.Test
    public void testGetRecordNumber() throws IOException {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (CSVParser parser = CSVParser.parse(testString, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                assertEquals(Long.valueOf(i + 1), record.getRecordNumber());
            }
            assertNull(parser.getRecordNumber());
        }
    }

    /**
     * Test method for {@link CSVParser#iterator()}
     */
    @org.junit.Test
    public void testIterator() throws IOException {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (CSVParser parser = CSVParser.parse(testString, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = new ArrayList<>(parser.getRecords());
            Iterator<CSVRecord> iterator = parser.iterator();
            for (CSVRecord record : iterator) {
                assertEquals(record, records.remove(0));
            }
            assertTrue(iterator.hasNext());
            assertFalse(parser.hasNext());
        }
    }

    /**
     * Test method for {@link CSVParser#close()}
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (CSVParser parser = CSVParser.parse(testString, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            Iterator<CSVRecord> iterator = parser.iterator();
            CSVRecord record = iterator.next();
            assertTrue(parser.hasNext());
            parser.close();
            assertFalse(parser.hasNext());
            assertFalse(iterator.hasNext());
        }
    }

    /**
     * Test method for {@link CSVParser#parse(java.io.Reader)}
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseReader() throws IOException {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(testString.getBytes());
             InputStreamReader testReader = new InputStreamReader(testInputStream, StandardCharsets.UTF_8);
             CSVParser parser = CSVParser.parse(testReader, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertEquals(Arrays.asList("Name", "Age", "City"), record.getHeaderMap().values());
            assertEquals(Arrays.asList("John", "30", "New York"), record.getValues());
        }
    }

    /**
     * Test method for {@link CSVParser#parse(java.io.File)}
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseFileWithoutFormat() throws IOException {
        File testFile = new File("CSVParserTest.csv");
        try (CSVParser parser = CSVParser.parse(testFile)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(5, records.size());
            CSVRecord record = records.get(0);
            assertEquals(Collections.emptyMap(), record.getHeaderMap());
        }
        assertTrue(testFile.delete());
    }

    /**
     * Test method for {@link CSVParser#parse(String)}
     */
    @org.junit.Test
    public void testParseStringWithoutFormat() {
        String testString = "Name,Age,City\nJohn,30,New York\nJane,25,Georgia";
        try (CSVParser parser = CSVParser.parse(testString)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertEquals(Collections.emptyMap(), record.getHeaderMap());
        }
    }

    /**
     * Test method for {@link CSVParser#parse(java.net.URL, java.nio.charset.Charset)}
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseURLWithoutFormat() throws IOException {
        String testURLString = "http://example.com/test.csv";
        try (URL testURL = new URL(testURLString)) {
            try (InputStreamReader reader = new InputStreamReader(testURL.openStream(), StandardCharsets.UTF_8);
                 CSVParser parser = CSVParser.parse(reader)) {
                List<CSVRecord> records = parser.getRecords();
                assertEquals(5, records.size());
                CSVRecord record = records.get(0);
                assertEquals(Collections.emptyMap(), record.getHeaderMap());
            }
        }
    }
}