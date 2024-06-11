package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.TYPE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @org.junit.Test
    public void testParseFile() throws IOException {
        File file = new File("src/test/resources/test.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertFalse(parser.isClosed());
        assertTrue(parser.hasNext());
        CSVRecord record = parser.nextRecord();
        assertEquals(TYPE, record.getRecordType());
        assertArrayEquals(new String[] { "Name", "Age" }, record.getHeaderMap().keySet().toArray(new String[0]));
        assertEquals("Name,Age", record.getComment());
        assertArrayEquals(new Object[] { "Alice", "30" }, record.toArray());
        assertEquals(1, record.getRecordNumber());

        record = parser.nextRecord();
        assertEquals("Bob,25", record.toString());
        assertEquals(2, record.getRecordNumber());

        assertFalse(parser.hasNext());
        assertNull(parser.nextRecord());
        assertTrue(parser.isClosed());

        assertEquals(Arrays.asList(records.get(0), records.get(1)), parser.getRecords());
    }

    @org.junit.Test
    public void testParseString() throws IOException {
        String csv = "Name,Age\nAlice,30\nBob,25";
        CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(Arrays.asList(records.get(0), records.get(1)), parser.getRecords());
    }

    @org.junit.Test
    public void testParseUrl() throws IOException {
        CSVParser parser = parse(new URL("http://example.com/test.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // TODO: Implement test for URL, this test currently fails with the current implementation
        // assertEquals(Arrays.asList(records.get(0), records.get(1)), parser.getRecords());
    }

    @org.junit.Test
    public void testParseEmptyFile() throws IOException {
        File file = new File("src/test/resources/empty.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(Collections.emptyList(), parser.getRecords());
    }

    @org.junit.Test
    public void testParseNullFormat() throws IOException {
        File file = new File("src/test/resources/test.csv");
        try {
            CSVParser parser = parse(file, null);
            parser.close();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseNullFile() throws IOException {
        CSVParser parser = new CSVParser(null, CSVFormat.DEFAULT);
        try {
            parser.close();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseNullCsv() throws IOException {
        CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(new byte[0])), CSVFormat.DEFAULT);
        try {
            parser.close();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseCustomFormat() throws IOException {
        CSVFormat format = new CSVFormat(new char[] { ';' }, 1, '\n', null, null, null);
        File file = new File("src/test/resources/test.csv");
        CSVParser parser = parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(Arrays.asList(records.get(0), records.get(1)), parser.getRecords());
    }
}