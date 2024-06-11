package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the CSVRecord class
 *
 * @author John Doe
 * @version 1.0
 */
public class CSVRecordTest {

    private CSVRecord record;

    /**
     * Sets up the test environment
     */
    @Before
    public void setUp() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        record = new CSVRecord(values, mapping, null, 1);
    }

    /**
     * Test method for the constructor.
     * <p>
     * Tests creating a new CSVRecord instance with valid arguments
     */
    @Test
    public void testCSVRecordConstruction() {
        String[] expectedValues = new String[]{"Name", "Age", "City"};
        assertArrayEquals(expectedValues, record.values());
        assertFalse(record.isConsistent()); // since no mapping is provided
        assertNull(record.get("Name"));
        assertNull(record.get("Age"));
        assertNull(record.get("City"));
    }

    /**
     * Test method for the get(int) method.
     * <p>
     * Tests getting values by index
     */
    @Test
    public void testGetInt() {
        String name = "John";
        String age = "30";
        String city = "New York";

        String[] newValues = new String[]{name, age, city};
        Map<String, Integer> newMapping = new HashMap<>();
        newMapping.put("Name", 0);
        newMapping.put("Age", 1);
        newMapping.put("City", 2);
        CSVRecord newRecord = new CSVRecord(newValues, newMapping, null, 1);

        record.values = newValues;
        record.mapping = newMapping;

        assertEquals("John", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("New York", record.get(2));
    }

    /**
     * Test method for the get(String) method.
     * <p>
     * Tests getting values by name
     */
    @Test
    public void testGetString() {
        String name = "John";
        String age = "30";
        String city = "New York";

        record.mapping.put("Name", 0);
        record.mapping.put("Age", 1);
        record.mapping.put("City", 2);

        assertEquals(name, record.get("Name"));
        assertEquals(age, record.get("Age"));
        assertNull(record.get("Unknown"));
    }

    /**
     * Test method for the isConsistent() method.
     * <p>
     * Tests checking the consistency of the record
     */
    @Test
    public void testIsConsistent() {
        record.mapping = null;
        assertFalse(record.isConsistent());

        record.mapping = new HashMap<>();
        record.mapping.put("Name", 0);
        record.mapping.put("Age", 1);
        record.mapping.put("City", 2);

        String[] newValues = new String[]{"Name", "Age", "City", "Invalid"};
        record.values = newValues;

        assertFalse(record.isConsistent());

        record.values = new String[]{"Name", "Age", "City"};
        assertTrue(record.isConsistent());
    }

    /**
     * Test method for the isMapped(String) method.
     * <p>
     * Tests checking whether a given column is mapped
     */
    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("Name"));
        assertFalse(record.isMapped("Unknown"));
    }

    /**
     * Test method for the isSet(String) method.
     * <p>
     * Tests checking whether a given column is mapped and has a value
     */
    @Test
    public void testIsSet() {
        assertFalse(record.isSet("Unknown"));
        assertTrue(record.isSet("Name"));
    }

    /**
     * Test method for the iterator() method.
     * <p>
     * Tests iterating through the values of a CSVRecord
     */
    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();

        assertEquals("Name", iterator.next());
        assertEquals("Age", iterator.next());
        assertEquals("City", iterator.next());

        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for the values() method.
     * <p>
     * Tests getting the raw values array of the CSVRecord
     */
    @Test
    public void testValues() {
        String[] expectedValues = new String[]{"Name", "Age", "City"};
        assertArrayEquals(expectedValues, record.values());
    }

    /**
     * Test method for the getComment() method.
     * <p>
     * Tests getting the comment for a CSVRecord (if any)
     */
    @Test
    public void testGetComment() {
        String comment = "This is a test record";
        record.comment = comment;
        assertEquals(comment, record.getComment());
    }

    /**
     * Test method for the getRecordNumber() method.
     * <p>
     * Tests getting the record number for a CSVRecord
     */
    @Test
    public void testGetRecordNumber() {
        long recordNumber = 12345L;
        record.recordNumber = recordNumber;
        assertEquals(recordNumber, record.getRecordNumber());
    }

    /**
     * Test method for the size() method.
     * <p>
     * Tests getting the size of a CSVRecord
     */
    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    /**
     * Test method for the toString() method.
     * <p>
     * Tests converting a CSVRecord to a string representation
     */
    @Test
    public void testToString() {
        assertEquals("[Name, Age, City]", record.toString());
    }
}