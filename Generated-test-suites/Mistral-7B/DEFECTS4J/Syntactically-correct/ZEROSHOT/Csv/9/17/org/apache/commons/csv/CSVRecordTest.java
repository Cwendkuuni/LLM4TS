import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setup() throws IOException {
        format = new CSVFormat();
        parser = new CSVParser(new StringReader("name,age,comment\nAlice,30,none\nBob,25,test\nCharlie,45,another comment\n"), format);
    }

    @Test
    public void testNewInstance() {
        CSVRecord record = parser.nextRecord();
        CSVRecord expected = new CSVRecord(record.values(), null, null, record.getRecordNumber());
        assertEquals(expected, record);
    }

    @Test
    public void testGet() {
        CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertNull(record.get(2));
        assertEquals("none", record.getComment());
        assertEquals(1, record.getRecordNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNullMapping() {
        CSVRecord record = parser.nextRecord();
        record.get("unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownColumn() {
        CSVRecord record = parser.nextRecord();
        record.get(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInconsistentRecord() {
        CSVRecord record = parser.nextRecord();
        record.mapping = null;
        record.get("name");
    }

    @Test
    public void testGetEnum() {
        CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(Enum.ONE));
        assertEquals("Bob", record.get(Enum.TWO));
        assertEquals("Charlie", record.get(Enum.THREE));
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = parser.nextRecord();
        assertTrue(record.isConsistent());

        record.mapping = null;
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = parser.nextRecord();
        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertFalse(record.isMapped("comment"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = parser.nextRecord();
        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertFalse(record.isSet("comment"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = parser.nextRecord();
        assertEquals("none", record.getComment());
        assertEquals("test", parser.nextRecord().getComment());
        assertEquals("another comment", parser.nextRecord().getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());

        record = parser.nextRecord();
        assertEquals(3, record.getRecordNumber());
    }

    @Test
    public void testToString() {
        CSVRecord record = parser.nextRecord();
        List<String> expected = Arrays.asList("Alice", "30", null);
        assertEquals(expected.toString(), record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = parser.nextRecord();
        String[] values = record.values();
        assertArrayEquals(new String[]{"Alice", "30", null}, values);
    }

    @Test
    public void testPutIn() {
        CSVRecord record = parser.nextRecord();
        Map<String, String> map = new HashMap<>();
        map.putAll(record.putIn(map));
        Map<String, String> expected = new HashMap<>();
        expected.put("name", "Alice");
        expected.put("age", "30");
        expected.put("comment", null);
        assertEquals(expected, map);
    }

    @Test
    public void testSize() {
        CSVRecord record = parser.nextRecord();
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = parser.nextRecord();
        List<String> list = record.toList();
        List<String> expected = Arrays.asList("Alice", "30", null);
        assertEquals(expected, list);
    }

    @Test
    public void testToMap() {
        CSVRecord record = parser.nextRecord();
        Map<String, String> map = record.toMap();
        Map<String, String> expected = new HashMap<>();
        expected.put("name", "Alice");
        expected.put("age", "30");
        expected.put("comment", null);
        assertEquals(expected, map);
    }

    enum Enum {
        ONE,
        TWO,
        THREE
    }
}