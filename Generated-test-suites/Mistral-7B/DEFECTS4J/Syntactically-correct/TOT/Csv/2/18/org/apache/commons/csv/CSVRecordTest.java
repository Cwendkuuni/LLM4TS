package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)} with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertEquals(3, record.size());
        assertEquals("Column1", record.get(0));
        assertEquals("Column2", record.get(1));
        assertEquals("Column3", record.get(2));
        assertTrue(record.isConsistent());
        assertFalse(record.isSet("Column4"));

        List<String> values = Arrays.asList(record.values());
        assertEquals(3, values.size());
        assertEquals("Column1", values.get(0));
        assertEquals("Column2", values.get(1));
        assertEquals("Column3", values.get(2));
    }

    /**
     * Test method for {@link CSVRecord#get(int)} with valid arguments.
     */
    @Test
    public void testValidGetByIndex() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertEquals("Column1", record.get(0));
        assertEquals("Column2", record.get(1));
        assertEquals("Column3", record.get(2));
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with valid arguments.
     */
    @Test
    public void testValidGetByName() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertEquals("Column1", record.get("Column1"));
        assertEquals("Column2", record.get("Column2"));
        assertEquals("Column3", record.get("Column3"));
        assertNull(record.get("Column4"));
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} with valid arguments.
     */
    @Test
    public void testConsistent() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertTrue(record.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} with valid arguments.
     */
    @Test
    public void testIsMapped() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertTrue(record.isMapped("Column1"));
        assertTrue(record.isMapped("Column2"));
        assertTrue(record.isMapped("Column3"));
        assertFalse(record.isMapped("Column4"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)} with valid arguments.
     */
    @Test
    public void testIsSet() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertFalse(record.isSet("Column1"));
        assertFalse(record.isSet("Column2"));
        assertFalse(record.isSet("Column3"));
        assertFalse(record.isSet("Column4"));

        String[] valuesWithNull = new String[]{"Column1", "Column2", null};
        CSVRecord recordWithNull = new CSVRecord(valuesWithNull, validMapping, null, 1);

        assertFalse(recordWithNull.isSet("Column1"));
        assertFalse(recordWithNull.isSet("Column2"));
        assertFalse(recordWithNull.isSet("Column3"));
        assertFalse(recordWithNull.isSet("Column4"));

        String[] valuesWithEmpty = new String[]{"Column1", "", "Column3"};
        CSVRecord recordWithEmpty = new CSVRecord(valuesWithEmpty, validMapping, null, 1);

        assertFalse(recordWithEmpty.isSet("Column1"));
        assertFalse(recordWithEmpty.isSet("Column2"));
        assertTrue(recordWithEmpty.isSet("Column3"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()} with valid arguments.
     */
    @Test
    public void testIterator() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        Iterator<String> iterator = record.iterator();

        assertEquals("Column1", iterator.next());
        assertEquals("Column2", iterator.next());
        assertEquals("Column3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link CSVRecord#values()} with valid arguments.
     */
    @Test
    public void testValues() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};

        CSVRecord record = new CSVRecord(validValues, null, null, 1);

        String[] values = record.values();

        assertEquals(3, values.length);
        assertEquals("Column1", values[0]);
        assertEquals("Column2", values[1]);
        assertEquals("Column3", values[2]);
    }

    /**
     * Test method for {@link CSVRecord#getComment()} with valid arguments.
     */
    @Test
    public void testGetComment() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertNull(record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()} with valid arguments.
     */
    @Test
    public void testGetRecordNumber() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);
        validMapping.put("Column3", 2);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#toString()} with valid arguments.
     */
    @Test
    public void testToString() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};

        CSVRecord record = new CSVRecord(validValues, null, null, 1);

        assertEquals("[Column1, Column2, Column3]", record.toString());
    }

    /**
     * Test method for {@link CSVRecord#get(int)} with invalid arguments.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndexOutOfBounds() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};

        CSVRecord record = new CSVRecord(validValues, null, null, 1);

        record.get(3);
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with invalid arguments.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};

        CSVRecord record = new CSVRecord(validValues, null, null, 1);

        record.get("Column4");
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} with invalid arguments.
     */
    @Test
    public void testInconsistent() {
        String[] validValues = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> validMapping = new HashMap<>();
        validMapping.put("Column1", 0);
        validMapping.put("Column2", 1);

        CSVRecord record = new CSVRecord(validValues, validMapping, null, 1);

        validMapping.put("Column4", 3);

        assertFalse(record.isConsistent());
    }
}