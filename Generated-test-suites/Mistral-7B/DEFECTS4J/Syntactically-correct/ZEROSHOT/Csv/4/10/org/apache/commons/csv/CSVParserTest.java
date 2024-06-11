package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.TOKEN;
import static org.apache.commons.csv.CSVRecord.INVALID_RECORD;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() {
        format = new CSVFormatBuilder()
                .setHeaderNames(new String[]{"Name", "Age"})
                .build();
    }

    @Test
    public void testParseFile() throws IOException {
        final String fileName = "test.csv";
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";

        File file = new File(fileName);
        try (CSVParser parser = CSVParser.parse(file, format)) {
            assertSame(format, parser.getFormat());
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertArrayEquals(new String[]{"Alice", "30"}, record.toArray());
            record = records.get(1);
            assertArrayEquals(new String[]{"Bob", "25"}, record.toArray());
        }

        assertTrue(file.delete());
    }

    @Test
    public void testParseString() throws IOException {
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";

        try (CSVParser parser = CSVParser.parse(csvContent, format)) {
            assertSame(format, parser.getFormat());
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertArrayEquals(new String[]{"Alice", "30"}, record.toArray());
            record = records.get(1);
            assertArrayEquals(new String[]{"Bob", "25"}, record.toArray());
        }
    }

    @Test
    public void testParseUrl() throws IOException {
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";
        InputStream is = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        URL url = new URL("http://example.com/test.csv");

        try (CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, format)) {
            assertSame(format, parser.getFormat());
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertArrayEquals(new String[]{"Alice", "30"}, record.toArray());
            record = records.get(1);
            assertArrayEquals(new String[]{"Bob", "25"}, record.toArray());
        }
    }

    @Test
    public void testParseReader() throws IOException {
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";

        try (Reader reader = new ByteArrayReader(csvContent.getBytes(StandardCharsets.UTF_8))) {
            try (CSVParser parser = CSVParser.parse(reader, format)) {
                assertSame(format, parser.getFormat());
                List<CSVRecord> records = parser.getRecords();
                assertEquals(2, records.size());
                CSVRecord record = records.get(0);
                assertArrayEquals(new String[]{"Alice", "30"}, record.toArray());
                record = records.get(1);
                assertArrayEquals(new String[]{"Bob", "25"}, record.toArray());
            }
        }
    }

    @Test
    public void testGetRecord() throws IOException {
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";

        try (CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), format)) {
            CSVRecord record = parser.getRecord();
            assertNull(record);
            record = parser.nextRecord();
            assertArrayEquals(new String[]{"Alice", "30"}, record.toArray());
            record = parser.getRecord();
            assertArrayEquals(new String[]{"Bob", "25"}, record.toArray());
            record = parser.getRecord();
            assertNull(record);
        }
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";

        try (CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            CSVRecord record = records.get(0);
            assertArrayEquals(new String[]{"Alice", "30"}, record.toArray());
            record = records.get(1);
            assertArrayEquals(new String[]{"Bob", "25"}, record.toArray());
        }
    }

    @Test
    public void testIterateRecords() throws IOException {
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";

        try (CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), format)) {
            CSVRecordIterator it = parser.iterator();
            assertFalse(it.hasNext());
            it.next();
            CSVRecord record = it.next();
            assertArrayEquals(new String[]{"Alice", "30"}, record.toArray());
            record = it.next();
            assertArrayEquals(new String[]{"Bob", "25"}, record.toArray());
            assertFalse(it.hasNext());
        }
    }

    @Test
    public void testCloseParser() throws IOException {
        String csvContent = "Name,Age\nAlice,30\nBob,25\n";

        try (CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            parser.close();
            try {
                records = parser.getRecords();
                fail();
            } catch (IOException e) {
                // expected
            }
        }
    }

    @Test
    public void testParseInvalidCsv() throws IOException {
        String csvContent = "Name,Age\nAlice,30 Apple";

        try (CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), format)) {
            try {
                parser.nextRecord();
                fail();
            } catch (IOException e) {
                // expected
            }
            assertEquals(INVALID_RECORD, parser.getRecord());
        }
    }
}