import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String csvData = "name,age,comment\nJohn Doe,30,Some comment\nJane Doe,25\n";

        CSVFormat format = CSVFormat.RFC4180.withHeader();
        CSVParser parser = format.newParser(new StringReader(csvData));
        CSVRecord record = parser.nextRecord();

        CSVRecord testRecord = new CSVRecord(record.values(), record.mapping, record.comment, record.recordNumber());

        assertEquals(record.get(CSVRecord.class.getDeclaredField("comment")), testRecord.getComment());
        assertEquals(record.get(Integer.class), testRecord.get(Integer.class, 0));
        assertEquals(record.get(String.class, "name"), testRecord.get("name"));
        assertEquals(record.getRecordNumber(), testRecord.getRecordNumber());
        assertEquals(record.isConsistent(), testRecord.isConsistent());
        assertEquals(record.isMapped("name"), testRecord.isMapped("name"));
        assertEquals(record.isSet("name"), testRecord.isSet("name"));

        Map<String, String> map = testRecord.toMap();
        List<String> list = testRecord.toList();

        assertEquals(record.size(), map.size());
        assertEquals(record.size(), list.size());

        assertEquals(record.toString(), list.toString());
        assertEquals(record.toString(), map.toString());

        assertFalse(map.isEmpty());
        assertFalse(list.isEmpty());

        List<String> values = testRecord.values();
        Map<String, Integer> headerMapping = record.mapping;

        for (int i = 0; i < record.size(); i++) {
            assertEquals(values.get(i), map.get(headerMapping.get(i).toString()));
        }
    }

    @Test
    public void testEmptyCSVRecord() {
        CSVRecord testRecord = new CSVRecord(new String[0], null, null, -1L);

        assertTrue(testRecord.isConsistent());
        assertFalse(testRecord.isMapped("name"));
        assertFalse(testRecord.isSet("name"));
        assertNull(testRecord.getComment());
        assertNull(testRecord.getRecordNumber());
        assertFalse(testRecord.iterator().hasNext());
        assertEquals(Collections.emptyList(), testRecord.toList());
        assertEquals(Collections.emptyMap(), testRecord.toMap());
        assertEquals("[]", testRecord.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutHeaderMapping() {
        String csvData = "name,age,comment\nJohn Doe,30,Some comment\nJane Doe,25\n";
        CSVFormat format = CSVFormat.RFC4180.withoutHeader();
        CSVParser parser = format.newParser(new StringReader(csvData));
        CSVRecord record = parser.nextRecord();

        CSVRecord testRecord = new CSVRecord(record.values(), null, record.comment, record.recordNumber());

        testRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordIndexOutOfBounds() {
        String csvData = "name,age,comment\nJohn Doe,30,Some comment\nJane Doe,25\n";
        CSVFormat format = CSVFormat.RFC4180.withHeader();
        CSVParser parser = format.newParser(new StringReader(csvData));
        CSVRecord record = parser.nextRecord();

        CSVRecord testRecord = new CSVRecord(record.values(), record.mapping, record.comment, record.recordNumber());

        testRecord.get(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordMappedColumnIndexOutOfBounds() {
        String csvData = "name,age,comment\nJohn Doe,30,Some comment\nJane Doe,25\n";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("comment", 2);

        CSVRecord testRecord = new CSVRecord(new String[3], mapping, null, 0L);

        testRecord.get("name");
        testRecord.get(5);
        testRecord.get("comment");

        testRecord.get(3);
    }
}