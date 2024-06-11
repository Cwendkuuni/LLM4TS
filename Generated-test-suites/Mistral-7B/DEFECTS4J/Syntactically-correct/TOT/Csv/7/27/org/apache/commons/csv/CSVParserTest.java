package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.builder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Tests for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parse method with a simple CSV file.
     * @throws IOException
     */
    @Test
    public void testParseSimpleCSV() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/simple.csv"), DEFAULT).getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals(Arrays.asList("A1", "B1"), record1.values());
        assertEquals("A1,B1", record1.getRecordString());
        CSVRecord record2 = records.get(1);
        assertEquals(Arrays.asList("A2", "B2"), record2.values());
        assertEquals("A2,B2", record2.getRecordString());
    }

    /**
     * Test parse method with a CSV file with header.
     * @throws IOException
     */
    @Test
    public void testParseCSVWithHeader() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/csvWithHeader.csv"), DEFAULT).getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Map<String, Integer> headerMap = record1.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));
        assertEquals(Arrays.asList("Alice", "30"), record1.values());
        CSVRecord record2 = records.get(1);
        assertEquals(Arrays.asList("Bob", "25"), record2.values());
    }

    /**
     * Test parse method with a CSV file with comment.
     * @throws IOException
     */
    @Test
    public void testParseCSVWithComment() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/csvWithComment.csv"), DEFAULT).getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals(Arrays.asList("A1", "B1"), record1.values());
        assertNull(record1.getComment());
        CSVRecord record2 = records.get(1);
        assertEquals(Arrays.asList("A2", "B2"), record2.values());
        assertEquals("This is a comment", record2.getComment());
    }

    /**
     * Test iterate method.
     * @throws IOException
     */
    @Test
    public void testIterate() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/simple.csv"), DEFAULT).getRecords();
        Iterator<CSVRecord> iterator = records.iterator();
        CSVRecord record1 = iterator.next();
        assertEquals(Arrays.asList("A1", "B1"), record1.values());
        CSVRecord record2 = iterator.next();
        assertEquals(Arrays.asList("A2", "B2"), record2.values());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test iterator with empty CSV file.
     * @throws IOException
     */
    @Test
    public void testIterateEmptyCSV() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/empty.csv"), DEFAULT).getRecords();
        Iterator<CSVRecord> iterator = records.iterator();
        assertFalse(iterator.hasNext());
    }

    /**
     * Test close method.
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/simple.csv"), DEFAULT).getRecords();
        CSVParser parser = new CSVParser(new ByteArrayInputStream(new byte[0]), DEFAULT);
        parser.close();
        try {
            parser.nextRecord();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Stream closed"));
        }
        records.clear();
        records = parse(new ByteArrayInputStream(new byte[0]), DEFAULT).getRecords();
        parser = new CSVParser(new ByteArrayInputStream(new byte[0]), DEFAULT);
        parser.close();
        assertFalse(parser.hasNext());
    }

    /**
     * Test parse method with invalid CSV file.
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        parse(new File("test-files/invalid.csv"), DEFAULT).getRecords();
    }

    /**
     * Test getRecords method with empty string.
     * @throws IOException
     */
    @Test
    public void testGetRecordsEmptyString() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
        List<CSVRecord> records = parse(inputStream, DEFAULT).getRecords();
        assertEquals(Collections.emptyList(), records);
        inputStream.close();
    }

    /**
     * Test getRecords method with null input stream.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordsNullInputStream() throws IOException {
        parse(null, DEFAULT).getRecords();
    }

    /**
     * Test getRecords method with null CSVFormat.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordsNullCSVFormat() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/simple.csv"), null).getRecords();
        assertSame(Collections.emptyList(), records);
    }

    /**
     * Test getRecords method with custom CSVFormat.
     * @throws IOException
     */
    @Test
    public void testGetRecordsCustomCSVFormat() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/simple.csv"), CSVFormat.EXCEL).getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals(Arrays.asList("A1", "B1"), record1.values());
        assertEquals(builder().withHeader(new String[]{"Column1", "Column2"}).build(), record1.getHeaderMap());
        assertEquals("A1,B1", record1.getRecordString());
        CSVRecord record2 = records.get(1);
        assertEquals(Arrays.asList("A2", "B2"), record2.values());
        assertEquals(builder().withHeader(new String[]{"Column1", "Column2"}).build(), record2.getHeaderMap());
        assertEquals("A2,B2", record2.getRecordString());
    }

    /**
     * Test getRecords method with empty CSV file and custom CSVFormat.
     * @throws IOException
     */
    @Test
    public void testGetRecordsEmptyCSVWithCustomCSVFormat() throws IOException {
        List<CSVRecord> records = parse(new File("test-files/empty.csv"), CSVFormat.EXCEL).getRecords();
        assertEquals(Collections.emptyList(), records);
    }
}