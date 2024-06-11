package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    @org.junit.Test
    public void testCSVRecord() {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Paris\n";

        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(csv);

        // Test constructor with header mapping
        CSVRecord record1 = parser.nextRecord();
        CSVRecord expectedRecord1 = new CSVRecord(new String[]{"John Doe", "30", "New York"}, new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }}, null, 1);

        assertEquals(expectedRecord1, record1);

        // Test getter methods
        assertEquals("John Doe", record1.get(0));
        assertEquals("30", record1.get(1));
        assertEquals("New York", record1.get(2));
        assertEquals("New York", record1.get("City"));
        assertEquals(1, record1.getRecordNumber());
        assertTrue(record1.isConsistent());

        // Test getter methods with wrong index or name
        assertNull(record1.get(3));
        assertNull(record1.get("Unknown"));

        // Test get method with Enum
        assertEquals("30", record1.get(CSVRecordTest.Age.class));

        // Test iterator and List methods
        List<String> list = record1.toList();
        Iterator<String> iterator = record1.iterator();
        assertEquals(Arrays.asList("John Doe", "30", "New York"), list);
        assertTrue(iterator.hasNext());
        assertEquals("John Doe", iterator.next());

        // Test putIn method
        Map<String, String> map = new HashMap<>();
        record1.putIn(map);
        assertEquals(Arrays.asList("John Doe", "30", "New York"), map.values());

        // Test size method
        assertEquals(3, record1.size());

        // Test isMapped method
        assertTrue(record1.isMapped("Name"));
        assertFalse(record1.isMapped("Unknown"));

        // Test isSet method
        assertTrue(record1.isSet("Name"));
        assertTrue(record1.isSet("Age"));
        assertFalse(record1.isSet("Unknown"));

        // Test toString method
        assertEquals("[John Doe, 30, New York]", record1.toString());

        // Test values method
        String[] values = record1.values();
        assertEquals(3, values.length);
        assertEquals("John Doe", values[0]);
        assertEquals("30", values[1]);
        assertEquals("New York", values[2]);

        // Test comment
        CSVRecord record2 = parser.nextRecord();
        CSVRecord expectedRecord2 = new CSVRecord(null, null, "Hi there!", 2);
        assertEquals(expectedRecord2, record2);
        assertEquals("Hi there!", record2.getComment());

        // Test empty record
        CSVRecord record3 = parser.nextRecord();
        CSVRecord expectedRecord3 = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 0);
        assertEquals(expectedRecord3, record3);
        assertFalse(record3.isConsistent());
        assertEquals(0, record3.getRecordNumber());

        // Test inconsistent record
        CSVRecord record4 = parser.nextRecord();
        CSVRecord expectedRecord4 = new CSVRecord(new String[]{"John Doe", "30"}, null, null, 1);
        assertEquals(expectedRecord4, record4);
        assertFalse(record4.isConsistent());
        assertEquals(1, record4.getRecordNumber());
    }

    enum Age { Age }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String csv = "30,New York\nJohn Doe,25,Paris";

        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(csv);

        CSVRecord record = parser.nextRecord();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidIndex() {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Paris";

        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(csv);

        CSVRecord record = parser.nextRecord();

        record.get(3);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidName() {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Paris";

        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(csv);

        CSVRecord record = parser.nextRecord();

        record.get("Unknown");
    }
}