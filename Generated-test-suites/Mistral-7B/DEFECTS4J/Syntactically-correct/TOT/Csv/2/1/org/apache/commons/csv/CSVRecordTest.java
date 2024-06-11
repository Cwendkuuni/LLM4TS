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
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}.
     */
    @org.junit.Test
    public void testCSVRecord() {
        // Test with valid data
        final String[] values = new String[] { "John", "Doe", "30", "Male" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
        assertArrayEquals(new String[] { "John", "Doe", "30", "Male" }, record.values());
        assertNull(record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertEquals(4, record.size());
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("Male", record.get(3));
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));

        // Test with empty data
        final CSVRecord emptyRecord = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1);
        assertTrue(emptyRecord.isConsistent());
        assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, emptyRecord.values());
        assertNull(emptyRecord.getComment());
        assertEquals(1, emptyRecord.getRecordNumber());
        assertEquals(0, emptyRecord.size());

        // Test with inconsistent data
        final String[] inconsistentValues = new String[] { "John", "Doe", "30" };
        final CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, mapping, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
        assertArrayEquals(new String[] { "John", "Doe", "30" }, inconsistentRecord.values());
        assertNull(inconsistentRecord.getComment());
        assertEquals(1, inconsistentRecord.getRecordNumber());
        assertEquals(3, inconsistentRecord.size());
    }

    /**
     * Test method for {@link CSVRecord#get(String)}.
     */
    @org.junit.Test
    public void testGet() {
        final String[] values = new String[] { "John", "Doe", "30", "Male" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test with valid name
        assertEquals("John", record.get("Name"));

        // Test with null name
        assertNull(record.get(null));

        // Test with invalid name
        assertNull(record.get("InvalidName"));
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}.
     */
    @org.junit.Test
    public void testIsConsistent() {
        final String[] values = new String[] { "John", "Doe", "30", "Male" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        // Test with empty values
        final CSVRecord emptyRecord = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1);
        assertTrue(emptyRecord.isConsistent());

        // Test with inconsistent mapping size
        final String[] inconsistentValues = new String[] { "John", "Doe", "30" };
        final CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, null, null, 1);
        assertFalse(inconsistentRecord.isConsistent());

        // Test with inconsistent mapping values size
        final String[] valuesWithInvalidSize = new String[] { "John", "Doe", "30", "Male", "InvalidValue" };
        final Map<String, Integer> mappingWithInvalidSize = new HashMap<>();
        mappingWithInvalidSize.put("Name", 0);
        mappingWithInvalidSize.put("Age", 2);
        mappingWithInvalidSize.put("Gender", 3);
        mappingWithInvalidSize.put("InvalidColumn", 5);
        final CSVRecord recordWithInvalidSize = new CSVRecord(valuesWithInvalidSize, mappingWithInvalidSize, null, 1);
        assertFalse(recordWithInvalidSize.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}.
     */
    @org.junit.Test
    public void testIsMapped() {
        final String[] values = new String[] { "John", "Doe", "30", "Male" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test with valid name
        assertTrue(record.isMapped("Name"));

        // Test with null name
        assertFalse(record.isMapped(null));

        // Test with invalid name
        assertFalse(record.isMapped("InvalidName"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}.
     */
    @org.junit.Test
    public void testIterator() {
        final String[] values = new String[] { "John", "Doe", "30", "Male" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test with valid iterator
        final Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("John", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Doe", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("30", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Male", iterator.next());
        assertFalse(iterator.hasNext());

        // Test with empty iterator
        final CSVRecord emptyRecord = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1);
        final Iterator<String> emptyIterator = emptyRecord.iterator();
        assertFalse(emptyIterator.hasNext());
    }
}