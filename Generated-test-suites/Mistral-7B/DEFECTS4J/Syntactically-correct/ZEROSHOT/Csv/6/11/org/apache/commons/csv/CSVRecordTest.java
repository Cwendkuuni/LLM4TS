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

    private CSVParser parser;

    @Before
    public void setup() throws IOException {
        String csv = "name,age,comment\nAlice,31,Single\nBob,27,Married\n";
        parser = new CSVParser(new StringReader(csv), new CSVFormat());
    }

    @Test
    public void testGet_Enum() {
        CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(Enum.NAME));
    }

    @Test
    public void testGet_Int() {
        CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(0));
        assertEquals("31", record.get(1));
    }

    @Test
    public void testGet_String() {
        CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get("name"));
        assertEquals("31", record.get("age"));
        assertNull(record.get("unknown"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = parser.nextRecord();
        assertEquals("Single", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = parser.nextRecord();
        assertTrue(record.isConsistent());

        record = parser.nextRecord();
        record.mapping = null;
        assertFalse(record.isConsistent());

        record.mapping = new HashMap<>();
        record.values = new String[2];
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = parser.nextRecord();
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = parser.nextRecord();
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("unknown"));
    }

    @Test
    public void testPutIn() {
        CSVRecord record = parser.nextRecord();
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("Alice", map.get("name"));
        assertEquals("31", map.get("age"));
        assertNull(map.get("comment"));
        assertNull(map.get("recordNumber"));
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
        assertArrayEquals(new String[]{"Alice", "31", "Single"}, list.toArray());
    }

    @Test
    public void testToMap() {
        CSVRecord record = parser.nextRecord();
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertSame(record.get("name"), map.get("name"));
        assertSame(record.get("age"), map.get("age"));
        assertSame(record.getComment(), map.get("comment"));
    }

    @Test
    public void testToString() {
        CSVRecord record = parser.nextRecord();
        assertEquals("[Alice, 31, Single]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = parser.nextRecord();
        String[] values = record.values();
        assertArrayEquals(new String[]{"Alice", "31", "Single"}, values);
    }

}