package org.apache.commons.csv;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testConstructorWithValidValuesAndMapping() {
        String[] validValues = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("Male", record.get(3));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));
        assertEquals(Arrays.asList("John", "Doe", "30", "Male"), Arrays.asList(record.iterator()));
        assertNull(record.getComment());
    }

    @Test
    public void testConstructorWithNullValuesAndMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(null, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[0], record.values());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("Gender"));
        assertNull(record.getComment());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithoutMapping() {
        String[] validValues = new String[]{"John", "Doe", "30", "Male"};

        CSVRecord record = new CSVRecord(validValues, null, null, 1);

        // This test should throw an IllegalStateException because no header mapping was provided
    }

    @Test
    public void testGetWithValidIndex() {
        String[] validValues = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("Male", record.get(3));
        assertNull(record.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        String[] validValues = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        record.get(validValues.length);

        // This test should throw an IndexOutOfBoundsException
    }

    @Test
    public void testGetWithValidName() {
        String[] validValues = new String[]{"Name:John,Age:30,Gender:Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        assertEquals("John", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("Male", record.get("Gender"));
        assertNull(record.get("Unknown"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidName() {
        String[] validValues = new String[]{"Name:John,Age:30,Gender:Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        record.get("InvalidName");

        // This test should throw an IllegalArgumentException
    }

    @Test
    public void testIsConsistent() {
        String[] validValues = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        assertTrue(record.isConsistent());

        String[] invalidValues = new String[]{"John", "Doe", "30"};
        CSVRecord invalidRecord = new CSVRecord(invalidValues, mapping, null, 1);

        assertFalse(invalidRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] validValues = new String[]{"Name:John,Age:30,Gender:Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        String[] validValues = new String[]{"Name:John,Age:30,Gender:Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));
        assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testIterator() {
        String[] validValues = new String[]{"Name:John,Age:30,Gender:Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        int i = 0;
        for (String value : record) {
            assertEquals(validValues[i], value);
            i++;
        }
    }

    @Test
    public void testComment() {
        String[] validValues = new String[]{"Name:John,Age:30,Gender:Male,Comment:Test Comment"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        mapping.put("Comment", 3);

        CSVRecord record = new CSVRecord(validValues, mapping, "Test Comment", 1);

        assertEquals("Test Comment", record.getComment());
    }

    @Test
    public void testRecordNumber() {
        String[] validValues = new String[]{"Name:John,Age:30,Gender:Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(validValues, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
    }
}