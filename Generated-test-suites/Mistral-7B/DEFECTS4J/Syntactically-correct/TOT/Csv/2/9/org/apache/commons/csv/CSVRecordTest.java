package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * CSVRecordTest.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)} .
     */
    @Test
    public void testCSVRecord() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertEquals(3, record.size());
        assertNull(record.getComment());
        assertArrayEquals(values, record.values());
        // get by index
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        // get by name
        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        // get by non-existent name
        assertNull(record.get("InvalidName"));
        // isConsistent
        assertTrue(record.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} .
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        // test exists
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        // test non-existent
        assertFalse(record.isMapped("InvalidName"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)} .
     */
    @Test
    public void testIsSet() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        // test exists and set
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        // test exists but not set
        assertFalse(record.isSet("InvalidName"));
        // test non-existent
        assertFalse(record.isSet("InvalidName"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()} .
     */
    @Test
    public void testIterator() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> valuesList = Arrays.asList(record.values());
        Iterator<String> iterator = record.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(valuesList.get(i++), iterator.next());
        }
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()} .
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
}