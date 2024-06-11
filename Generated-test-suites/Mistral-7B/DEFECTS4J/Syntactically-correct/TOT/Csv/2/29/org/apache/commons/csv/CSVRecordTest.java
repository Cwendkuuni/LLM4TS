package org.apache.commons.csv;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstruction() {
        String[] values = new String[]{"John Doe", "123 Main St.", "Sales", "555-1234"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Address", 1);
        mapping.put("Department", 2);
        mapping.put("Phone", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.size());
        assertEquals("John Doe", record.get(0));
        assertEquals("123 Main St.", record.get(1));
        assertEquals("Sales", record.get(2));
        assertEquals("555-1234", record.get(3));
        assertEquals(null, record.get("NonExistentColumn"));
        assertEquals(1, record.getRecordNumber());
        assertEquals(null, record.getComment());
    }

    @Test
    public void testCSVRecordGetConsistent() {
        String[] values = new String[]{"John Doe", "123 Main St.", "Sales", "555-1234"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Address", 1);
        mapping.put("Department", 2);
        mapping.put("Phone", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);

        assertTrue(emptyRecord.isConsistent());

        String[] inconsistentValues = new String[]{"John Doe", "123 Main St.", "Sales"};
        CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, mapping, null, 1);

        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[]{"John Doe", "123 Main St.", "Sales", "555-1234"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // This test should throw an IllegalStateException as no header mapping was provided
        String nonExistentValue = record.get("NonExistentColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInconsistentMapping() {
        String[] values = new String[]{"John Doe", "123 Main St.", "Sales", "555-1234"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Address", 2); // Incorrect mapping

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // This test should throw an IllegalArgumentException as the mapping is inconsistent
        String incorrectValue = record.get("Name");
    }

    @Test
    public void testCSVRecordIteration() {
        String[] values = new String[]{"John Doe", "123 Main St.", "Sales", "555-1234"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Address", 1);
        mapping.put("Department", 2);
        mapping.put("Phone", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = record.iterator();

        assertEquals("John Doe", iterator.next());
        assertEquals("123 Main St.", iterator.next());
        assertEquals("Sales", iterator.next());
        assertEquals("555-1234", iterator.next());
        assertFalse(iterator.hasNext());
    }
}