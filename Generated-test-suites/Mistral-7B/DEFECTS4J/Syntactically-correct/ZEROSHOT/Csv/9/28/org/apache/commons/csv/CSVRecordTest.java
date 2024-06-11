import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    private CSVFormat format;
    private List<CSVRecord> records;

    @Before
    public void setUp() {
        format = new CSVFormat(' ', true);
        try (CSVParser parser = new CSVParser(new StringReader("name,age,comment\nAlice,25,comment1\nBob,30,comment2\n"), format)) {
            records = parser.getRecords();
        }
    }

    @After
    public void tearDown() {
        records = null;
        format = null;
    }

    @Test
    public void testGetRecordNumber() {
        for (CSVRecord record : records) {
            assertEquals(Long.parseLong(record.get(0)), record.getRecordNumber());
        }
    }

    @Test
    public void testGetMapping() {
        CSVRecord record = records.get(0);
        Map<String, Integer> mapping = record.mapping;
        assertEquals(2, mapping.size());
        assertEquals("name", mapping.keySet().iterator().next().toString());
        assertEquals(0, mapping.get("name").intValue());
        assertEquals("age", mapping.get(1).toString());
        assertEquals(1, mapping.get("age").intValue());
    }

    @Test
    public void testGetValues() {
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"Alice", "25", "comment1"}, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = records.get(0);
        assertEquals("comment1", record.getComment());
        record = records.get(1);
        assertNull(record.getComment());
    }

    @Test
    public void testIsConsistent() {
        for (CSVRecord record : records) {
            assertTrue(record.isConsistent());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidColumnName() {
        CSVRecord record = records.get(0);
        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMissingColumn() {
        CSVRecord record = records.get(0);
        record.get(3);
    }

    @Test
    public void testGetEnum() {
        CSVRecord record = records.get(0);
        assertEquals("Alice", record.get(CSVRecord.Column.NAME.name()));
        assertEquals("25", record.get(CSVRecord.Column.AGE.name()));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = records.get(0);
        assertEquals("Alice", record.get(0));
        assertEquals("25", record.get(1));
        assertEquals("comment1", record.get(2));
    }

    @Test
    public void testGetByIndexOutOfBounds() {
        CSVRecord record = records.get(0);
        assertNull(record.get(3));
    }

    @Test
    public void testGetEmptyRecord() {
        CSVRecord emptyRecord = new CSVRecord(new String[0], null, null, 0L);
        assertFalse(emptyRecord.isConsistent());
        assertNull(emptyRecord.getComment());
        assertNull(emptyRecord.get(0));
    }

    @Test
    public void testPutIn() {
        CSVRecord record = records.get(0);
        Map<String, String> map = new HashMap<>();
        map.putAll(record.putIn(map));
        assertEquals(2, map.size());
        assertEquals("Alice", map.get("name"));
        assertEquals("25", map.get("age"));
        assertEquals("comment1", map.get("comment"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByMissingColumnName() {
        CSVRecord record = records.get(0);
        record.get("invalidName");
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = records.get(0);
        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertFalse(record.isMapped("invalidName"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = records.get(0);
        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertFalse(record.isSet("invalidName"));
    }

    @Test
    public void testSize() {
        CSVRecord record = records.get(0);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = records.get(0);
        List<String> list = record.toList();
        assertEquals("Alice", list.get(0));
        assertEquals("25", list.get(1));
        assertEquals("comment1", list.get(2));
    }

    @Test
    public void testToString() {
        CSVRecord record = records.get(0);
        assertEquals("[Alice, 25, comment1]", record.toString());
    }
}

enum Column {
    NAME, AGE
}