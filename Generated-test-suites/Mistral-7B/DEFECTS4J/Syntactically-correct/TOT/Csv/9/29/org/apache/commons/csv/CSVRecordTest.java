package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}
     * with a consistent record.
     */
    @org.junit.Test
    public void testConsistentRecord() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("name1", record.get("name1"));
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get("name2"));
        assertEquals("value2", record.get(1));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertFalse(record.isMapped("nonExistentName"));
        assertSame(values, record.values());
        assertEquals(Arrays.toString(values), record.toString());
    }

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}
     * with an inconsistent record.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInconsistentRecord() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        new CSVRecord(values, mapping, null, 2L); // record has 3 values
    }

    /**
     * Test method for {@link CSVRecord#get(Enum)}
     */
    @org.junit.Test
    public void testGetEnum() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", record.get(Enum.ONE));
        assertEquals("value1", record.get(new Integer(0)));
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with a mapped value.
     */
    @org.junit.Test
    public void testGetMappedValue() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", record.get("name1"));
        assertEquals("value1", record.get(0));
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with a non-mapped value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetNonMappedValue() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        record.get("nonExistentColumnName");
    }

    /**
     * Test method for {@link CSVRecord#get(int)} with a valid index.
     */
    @org.junit.Test
    public void testGetValidIndex() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", record.get(0));
        assertEquals("value1", record.get("name1"));
    }

    /**
     * Test method for {@link CSVRecord#get(int)} with an invalid index.
     */
    @org.junit.Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        record.get(2);
    }

    /**
     * Test method for {@link CSVRecord#getComment()}.
     */
    @org.junit.Test
    public void testGetComment() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);

        assertEquals("comment", record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()}.
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1L, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}.
     */
    @org.junit.Test
    public void testIsConsistent() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertFalse(record.isConsistent());

        mapping.remove("name3");
        assertTrue(record.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} with a mapped value.
     */
    @org.junit.Test
    public void testIsMapped() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isMapped("name1"));
        assertFalse(record.isMapped("nonExistentName"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}.
     */
    @org.junit.Test
    public void testIsSet() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertFalse(record.isSet("nonExistentName"));
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}
     */
    @org.junit.Test
    public void testIterator() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        final List<String> expected = Arrays.asList(values);
        final Iterator<String> iterator = record.iterator();
        for (int i = 0; i < values.length; i++) {
            assertEquals(expected.get(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)} with a new map.
     */
    @org.junit.Test
    public void testPutInMap() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, String> result = new HashMap<>();
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        final Map<String, String> expected = new HashMap<>();
        expected.put("name1", "value1");
        expected.put("name2", "value2");
        assertSame(expected, record.putIn(result));
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)} with an existing map.
     */
    @org.junit.Test
    public void testPutInMapExisting() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, String> result = new HashMap<>();
        result.put("name1", "value1");
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        final Map<String, String> expected = new HashMap<>();
        expected.put("name1", "value1");
        expected.put("name2", "value2");
        assertSame(expected, record.putIn(result));
    }

    /**
     * Test method for {@link CSVRecord#size()}.
     */
    @org.junit.Test
    public void testSize() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(values.length, record.size());
    }

    /**
     * Test method for {@link CSVRecord#toList()}.
     */
    @org.junit.Test
    public void testToList() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        final List<String> expected = Arrays.asList(values);
        assertSame(expected, record.toList());
    }

    /**
     * Test method for {@link CSVRecord#toMap()}.
     */
    @org.junit.Test
    public void testToMap() {
        final String[] values = new String[]{"name1", "value1", "value2"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        final Map<String, String> expected = new HashMap<>();
        expected.put("name1", "value1");
        expected.put("name2", "value2");
        assertSame(expected, record.toMap());
    }
}