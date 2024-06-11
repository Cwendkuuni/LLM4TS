package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        final String[] values = new String[] { "Alice", "25", "female" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Alice", record.get("Name"));
        assertEquals("25", record.get("Age"));
        assertEquals("female", record.get("Gender"));
        assertEquals("Alice", record.values()[0]);
        assertEquals("25", record.values()[1]);
        assertEquals("female", record.values()[2]);
        assertEquals("Name: Alice, Age: 25, Gender: female", record.toString());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        final String[] values = new String[0];
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.get("Name"));
        assertNull(record.get("Age"));
        assertNull(record.get("Gender"));
        assertSame(Collections.emptyList(), record.toList());
        assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithMissingMapping() {
        final String[] values = new String[] { "Alice", "25", "female" };
        final Map<String, Integer> mapping = null;

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidMapping() {
        final String[] values = new String[] { "Alice", "25", "female" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 1);
        mapping.put("Age", 0);
        mapping.put("Gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithMisalignedMappingAndValues() {
        final String[] values = new String[] { "Alice", "25", "female" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 2);
        mapping.put("Age", 0);
        mapping.put("Gender", 1);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInconsistentMappingAndValues() {
        final String[] values = new String[] { "Alice", "25", "female" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 2);
        mapping.put("Age", 0);
        mapping.put("Gender", 1);
        mapping.put("Height", 3);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testCSVRecordWithNullString() {
        final String[] values = new String[] { "Alice", null, "female" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(null, record.get("Age"));
        assertEquals(null, record.values()[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithNullMapping() {
        final String[] values = new String[] { "Alice", "25", "female" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNullValues() {
        final String[] values = null;
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
    }
}