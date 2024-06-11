package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)} .
     */
    @org.junit.Test
    public void testCSVRecord() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Email", 1);
            put("Age", 2);
            put("City", 3);
        }};
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(values, record.values());
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Unknown"));
        assertNull(record.get("Unknown"));
        assertTrue(record.isMapped("Name"));
        assertEquals("John Doe", record.get("Name"));
        assertEquals("john.doe@example.com", record.get("Email"));
        assertEquals("45", record.get("Age"));
        assertEquals("New York", record.get("City"));
        assertFalse(record.isSet("Unknown"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Email"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
    }

    /**
     * Test method for {@link CSVRecord#get(int)} .
     */
    @org.junit.Test
    public void testGetInt() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("John Doe", record.get(0));
        assertEquals("john.doe@example.com", record.get(1));
        assertEquals("45", record.get(2));
        assertEquals("New York", record.get(3));
    }

    /**
     * Test method for {@link CSVRecord#get(String)} .
     */
    @org.junit.Test
    public void testGetString() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Email", 1);
            put("Age", 2);
            put("City", 3);
        }};
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.get("Unknown"));
        assertEquals("John Doe", record.get("Name"));
        assertEquals("john.doe@example.com", record.get("Email"));
        assertEquals("45", record.get("Age"));
        assertEquals("New York", record.get("City"));
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} .
     */
    @org.junit.Test
    public void testIsConsistent() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        assertTrue(record.isConsistent());

        final String[] valuesInconsistent = new String[]{"John Doe", "john.doe@example.com"};
        final CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, null, null, 1);

        assertFalse(recordInconsistent.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} .
     */
    @org.junit.Test
    public void testIsMapped() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Email", 1);
            put("Age", 2);
            put("City", 3);
        }};
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isMapped("Unknown"));
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Email"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)} .
     */
    @org.junit.Test
    public void testIsSet() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Email", 1);
            put("Age", 2);
            put("City", 3);
        }};
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isSet("Unknown"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Email"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()} .
     */
    @org.junit.Test
    public void testIterator() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        final Iterator<String> iterator = record.iterator();

        assertEquals("John Doe", iterator.next());
        assertEquals("john.doe@example.com", iterator.next());
        assertEquals("45", iterator.next());
        assertEquals("New York", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link CSVRecord#values()} .
     */
    @org.junit.Test
    public void testValues() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        assertArrayEquals(values, record.values());
    }

    /**
     * Test method for {@link CSVRecord#getComment()} .
     */
    @org.junit.Test
    public void testGetComment() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        assertNull(record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()} .
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#size()} .
     */
    @org.junit.Test
    public void testSize() {
        final String[] values = new String[]{"John Doe", "john.doe@example.com", "45", "New York"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(4, record.size());
    }
}