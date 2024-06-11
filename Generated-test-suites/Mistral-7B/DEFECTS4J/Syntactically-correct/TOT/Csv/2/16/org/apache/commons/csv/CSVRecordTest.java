package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        record = new CSVRecord(values, mapping, null, 1);
    }

    /**
     * Test get method with valid index.
     */
    @Test
    public void testGetValidIndex() {
        String expected = "Alice";
        String actual = record.get(0);
        assertEquals(expected, actual);
    }

    /**
     * Test get method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        record.get(values.length);
    }

    /**
     * Test get method with valid name.
     */
    @Test
    public void testGetValidName() {
        String expected = "Alice";
        String actual = record.get("Name");
        assertEquals(expected, actual);
    }

    /**
     * Test get method with invalid name.
     */
    @Test
    public void testGetInvalidName() {
        String expected = null;
        String actual = record.get("InvalidName");
        assertNull(actual);
    }

    /**
     * Test isConsistent method with valid mapping.
     */
    @Test
    public void testIsConsistentValidMapping() {
        assertTrue(record.isConsistent());
    }

    /**
     * Test isConsistent method with invalid mapping.
     */
    @Test
    public void testIsConsistentInvalidMapping() {
        Map<String, Integer> invalidMapping = new HashMap<>();
        invalidMapping.put("Name", 0);
        invalidMapping.put("Age", 2);
        CSVRecord invalidRecord = new CSVRecord(values, invalidMapping, null, 1);
        assertFalse(invalidRecord.isConsistent());
    }

    /**
     * Test isMapped method with valid name.
     */
    @Test
    public void testIsMappedValidName() {
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Email"));
    }

    /**
     * Test isMapped method with invalid name.
     */
    @Test
    public void testIsMappedInvalidName() {
        assertFalse(record.isMapped("InvalidName"));
    }

    /**
     * Test isSet method with valid name and valid index.
     */
    @Test
    public void testIsSetValidNameAndIndex() {
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("InvalidName"));
    }

    /**
     * Test isSet method with valid name and invalid index.
     */
    @Test
    public void testIsSetValidNameAndInvalidIndex() {
        assertFalse(record.isSet("Name", 3));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        List<String> expected = Arrays.asList(values);
        List<String> actual = Arrays.asList(record.iterator().toArray());
        assertEquals(expected, actual);
    }

    /**
     * Test values method.
     */
    @Test
    public void testValues() {
        String[] expected = values;
        String[] actual = record.values();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test getComment method with null comment.
     */
    @Test
    public void testGetCommentNullComment() {
        assertNull(record.getComment());
    }

    /**
     * Test getComment method with comment.
     */
    @Before
    public void setupWithComment() {
        String comment = "Comment for record";
        record = new CSVRecord(values, mapping, comment, 1);
    }

    /**
     * Test getComment method with comment.
     */
    @Test
    public void testGetCommentWithComment() {
        String expected = "Comment for record";
        String actual = record.getComment();
        assertEquals(expected, actual);
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        long expected = 1;
        long actual = record.getRecordNumber();
        assertEquals(expected, actual);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        int expected = values.length;
        int actual = record.size();
        assertEquals(expected, actual);
    }
}