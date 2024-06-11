package org.apache.commons.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        String csv = "name,age;Alice,30;Bob,25;Charlie,22";
        parser = new CSVParser(new StringReader(csv), CSVFormat.RFC4180);
    }

    @Test
    public void testIterator() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Iterator<CSVRecord> iterator = parser.iterator();

        assertEquals(records.size(), iterator.size());
        for (CSVRecord record : records) {
            CSVRecord next = iterator.next();
            assertSame(record, next);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record = parser.nextRecord();
        assertNull(record);

        record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
        assertEquals(2, record.size());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get("name"));
        assertEquals(30, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());
        assertEquals(2, record.size());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Bob", record.get("name"));
        assertEquals(25, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(3, record.getRecordNumber());
        assertEquals(2, record.size());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Charlie", record.get("name"));
        assertEquals(22, record.getInteger("age"));

        record = parser.nextRecord();
        assertNull(record);
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord firstRecord = records.get(0);
        assertEquals(1, firstRecord.getRecordNumber());
        assertEquals(2, firstRecord.size());
        assertEquals("name", firstRecord.getHeaderMap().get("name"));
        assertEquals("Alice", firstRecord.get("name"));
        assertEquals(30, firstRecord.getInteger("age"));

        CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.getRecordNumber());
        assertEquals(2, secondRecord.size());
        assertEquals("name", secondRecord.getHeaderMap().get("name"));
        assertEquals("Bob", secondRecord.get("name"));
        assertEquals(25, secondRecord.getInteger("age"));

        CSVRecord thirdRecord = records.get(2);
        assertEquals(3, thirdRecord.getRecordNumber());
        assertEquals(2, thirdRecord.size());
        assertEquals("name", thirdRecord.getHeaderMap().get("name"));
        assertEquals("Charlie", thirdRecord.get("name"));
        assertEquals(22, thirdRecord.getInteger("age"));
    }

    @Test
    public void testEmptyCsvFile() throws IOException {
        String csv = "";
        parser = new CSVParser(new StringReader(csv), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testInvalidCsv() throws IOException {
        String csv = "name,ageAlice,30Bob,25Charlie,22";
        parser = new CSVParser(new StringReader(csv), CSVFormat.RFC4180);
        parser.nextRecord();
    }

    @Test
    public void testCustomFormat() throws IOException {
        String csv = "name,age;Alice,30;Bob,25;Charlie,22";
        parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT.withHeader());
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get("name"));
        assertEquals(30, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Bob", record.get("name"));
        assertEquals(25, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(3, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Charlie", record.get("name"));
        assertEquals(22, record.getInteger("age"));

        record = parser.nextRecord();
        assertNull(record);
    }

    @Test
    public void testCustomFormatWithComment() throws IOException {
        String csv = "name,age;#This is a comment;Alice,30;Bob,25;Charlie,22";
        parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT.withHeader());
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get("name"));
        assertEquals(30, record.getInteger("age"));
        assertNull(record.getComment());

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Bob", record.get("name"));
        assertEquals(25, record.getInteger("age"));
        assertEquals("This is a comment", record.getComment());

        record = parser.nextRecord();
        assertEquals(3, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Charlie", record.get("name"));
        assertEquals(22, record.getInteger("age"));
        assertNull(record.getComment());

        record = parser.nextRecord();
        assertNull(record);
    }

    @Test
    public void testCreateParserFromStream() throws IOException {
        String csv = "name,age;Alice,30;Bob,25;Charlie,22";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        parser = new CSVParser(inputStream, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get("name"));
        assertEquals(30, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Bob", record.get("name"));
        assertEquals(25, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(3, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Charlie", record.get("name"));
        assertEquals(22, record.getInteger("age"));

        record = parser.nextRecord();
        assertNull(record);

        parser.close();
        inputStream.close();
    }

    @Test
    public void testCreateParserFromFile() throws IOException {
        String fileName = "test.csv";
        String csv = "name,age;Alice,30;Bob,25;Charlie,22";
        writeToFile(fileName, csv);
        parser = new CSVParser(new FileReader(fileName), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get("name"));
        assertEquals(30, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Bob", record.get("name"));
        assertEquals(25, record.getInteger("age"));

        record = parser.nextRecord();
        assertEquals(3, record.getRecordNumber());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Charlie", record.get("name"));
        assertEquals(22, record.getInteger("age"));

        record = parser.nextRecord();
        assertNull(record);

        parser.close();
        deleteFile(fileName);
    }

    private void writeToFile(String fileName, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(content);
        }
    }

    private void deleteFile(String fileName) {
        File file = new File(fileName);
        file.delete();
    }
}