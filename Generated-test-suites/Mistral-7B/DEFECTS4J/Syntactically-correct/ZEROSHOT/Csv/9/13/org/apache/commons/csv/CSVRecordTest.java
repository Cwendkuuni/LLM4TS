import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    private static final String CSV_DATA = "name,age,comment\nAlice,25,girl\nBob,30,man\nComment1,35,male\nComment2,40,female";

    private CSVParser parser;
    private CSVFormat format;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormat();
        parser = new CSVParser(new StringReader(CSV_DATA), format);
    }

    @After
    public void tearDown() throws IOException {
        parser.close();
    }

    @Test
    public void testNewInstance() {
        CSVRecord record = parser.getRecord();
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testGetEmptyRecord() {
        CSVRecord record = parser.getRecord();
        assertTrue(record.isEmpty());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testGetRecordWithValues() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        String name = record.get("name");
        int age = Integer.parseInt(record.get("age"));
        String comment = record.getComment();
        assertEquals("Alice", name);
        assertEquals(25, age);
        assertEquals("girl", comment);
    }

    @Test
    public void testGetRecordWithEmptyComment() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        String comment = record.getComment();
        assertNull(comment);
    }

    @Test
    public void testGetRecordWithNoName() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        String name = record.get("nonExistentName");
        assertNull(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordWithNoMapping() {
        CSVRecord record = parser.getRecord();
        record.mapping = null;
        record.get("name");
    }

    @Test
    public void testGetRecordWithNoValue() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        String value = record.get("nonExistentColumn");
        assertNull(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordWithInvalidIndex() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        record.get(record.values().length);
    }

    @Test
    public void testGetRecordWithComment() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        String comment = record.getComment();
        assertEquals("Comment1", comment);
    }

    @Test
    public void testGetRecordWithRecordNumber() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        long recordNumber = record.getRecordNumber();
        assertEquals(2, recordNumber);
    }

    @Test
    public void testGetRecordWithIsConsistent() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGetRecordWithIsMapped() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("nonExistentColumn"));
    }

    @Test
    public void testGetRecordWithIsSet() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("nonExistentColumn"));
    }

    @Test
    public void testGetRecordWithSize() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        assertEquals(3, record.size());
    }

    @Test
    public void testGetRecordWithToList() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        List<String> list = record.toList();
        assertEquals(Arrays.asList("name", "age", "comment"), list);
    }

    @Test
    public void testGetRecordWithToMap() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        Map<String, String> map = record.toMap();
        assertEquals(Arrays.asList("name", "age", "comment"), map.keySet());
        assertEquals("Alice", map.get("name"));
        assertEquals("25", map.get("age"));
        assertNull(map.get("nonExistentColumn"));
    }

    @Test
    public void testToString() {
        CSVRecord record = parser.getRecord();
        record = parser.getRecord();
        String toString = record.toString();
        List<String> expected = Arrays.asList("Alice", "25", "girl");
        assertEquals(expected.toString(), toString);
    }
}