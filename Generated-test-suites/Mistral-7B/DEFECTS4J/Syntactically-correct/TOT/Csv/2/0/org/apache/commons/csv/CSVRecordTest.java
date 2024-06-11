package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CSVRecordTest.java
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Testing get method with valid index
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("alice@example.com", record.get(2));

        // Testing get method with valid name
        assertEquals("Alice", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("alice@example.com", record.get("Email"));

        // Testing isConsistent method
        assertTrue(record.isConsistent());

        // Testing isMapped method
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Email"));

        // Testing isSet method
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Email"));

        // Testing size method
        assertEquals(3, record.size());

        // Testing iterator method
        Iterator<String> iterator = record.iterator();
        String actual[] = new String[3];
        int i = 0;
        while (iterator.hasNext()) {
            actual[i++] = iterator.next();
        }
        assertArrayEquals(values, actual);
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Testing get method with valid index
        assertNull(record.get(0));

        // Testing get method with valid name
        assertNull(record.get("Name"));

        // Testing isConsistent method
        assertTrue(record.isConsistent());

        // Testing isMapped method
        assertFalse(record.isMapped("Name"));

        // Testing isSet method
        assertFalse(record.isSet("Name"));

        // Testing size method
        assertEquals(0, record.size());

        // Testing iterator method
        Iterator<String> iterator = record.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInconsistentValues() {
        String[] values = new String[]{"Alice", "30", "alice@example.com", null};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(3);
    }
}