import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        String[] expectedValues = new String[]{"value1", "value2", "value3", "value4"};
        Map<String, Integer> expectedMapping = new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
            put("name4", 3);
        }};
        verifyCSVRecord(record, expectedValues, expectedMapping, null);
    }

    @Test
    public void testCSVRecordGetValueByEnum() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        assertEquals("value1", record.get(Enum.ONE));
        assertEquals("value2", record.get(Enum.TWO));
        assertEquals("value3", record.get(Enum.THREE));
        assertEquals("value4", record.get(Enum.FOUR));
    }

    @Test
    public void testCSVRecordGetValueByName() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
        assertEquals("value4", record.get("name4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetValueByNameWithInvalidName() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        record.get("invalidName");
    }

    @Test
    public void testCSVRecordGetValueByIndex() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
        assertEquals("value4", record.get(3));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCSVRecordGetValueByIndexOutOfBounds() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        record.get(4);
    }

    @Test
    public void testCSVRecordGetComment() {
        String csv = "name1,name2,name3,name4,comment\n" +
                "value1,value2,value3,value4,some comment";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        assertEquals("some comment", record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecordGetRecordNumber() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4\n" +
                "value5,value6,value7,value8";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(1, record1.getRecordNumber());
        assertEquals(2, record2.getRecordNumber());
    }

    @Test
    public void testCSVRecordIsConsistent() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4\n" +
                "value1,value2,value3,value5";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertTrue(record1.isConsistent());
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testCSVRecordIsMapped() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertTrue(record.isMapped("name4"));
        assertFalse(record.isMapped("nonExistentName"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertTrue(record.isSet("name4"));
        assertFalse(record.isSet("nonExistentName"));
    }

    @Test
    public void testCSVRecordIterator() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        List<String> values = Arrays.asList(record.values());
        Iterator<String> iterator = record.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(values.get(i++), iterator.next());
        }
    }

    @Test
    public void testCSVRecordPutInMap() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        Map<String, String> map = new HashMap<>();
        map.putAll(record.putIn(map));
        List<String> values = Arrays.asList(record.values());
        for (Entry<String, String> entry : map.entrySet()) {
            assertEquals(values.get(entry.getKey().charAt(0) - 'A'), entry.getValue());
        }
    }

    @Test
    public void testCSVRecordSize() {
        String csv = "name1,name2,name3,name4\n" +
                "value1,value2,value3,value4";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);
        assertEquals(4, record.size());
    }

    private List<CSVRecord> parseCSV(String csv) {
        try (CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT)) {
            return parser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyCSVRecord(CSVRecord record, String[] expectedValues, Map<String, Integer> expectedMapping, String comment) {
        Map<String, Integer> actualMapping = record.mapping;
        assertArrayEquals(expectedValues, record.values());
        assertArrayEquals(expectedMapping.keySet().toArray(), actualMapping.keySet().toArray());
        for (Map.Entry<String, Integer> entry : expectedMapping.entrySet()) {
            assertEquals(entry.getValue(), actualMapping.get(entry.getKey()));
        }
        assertEquals(comment, record.comment);
    }

    enum Enum {
        ONE(0), TWO(1), THREE(2), FOUR(3);

        private final int value;

        Enum(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}