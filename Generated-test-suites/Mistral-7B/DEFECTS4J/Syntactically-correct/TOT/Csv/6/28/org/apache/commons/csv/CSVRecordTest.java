package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testEmptyRecord() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testSimpleRecord() {
        Map<String, Integer> headerMapping = new HashMap<>();
        headerMapping.put("Name", 0);
        headerMapping.put("Age", 1);

        String[] values = new String[]{"John Doe", "30"};
        CSVRecord record = new CSVRecord(values, headerMapping, null, 1);

        assertEquals(2, record.size());
        assertEquals("John Doe", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("John Doe", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("Name=Name,Age=Age", record.toString());
        assertSame(headerMapping, record.toMap());
    }

    @Test
    public void testRecordWithMismatchedMapping() {
        Map<String, Integer> headerMapping = new HashMap<>();
        headerMapping.put("Name", 0);
        headerMapping.put("Age", 1);

        String[] values = new String[]{"John Doe", "30", "Invalid"};
        CSVRecord record = new CSVRecord(values, headerMapping, null, 1);

        assertEquals(3, record.size());
        assertEquals("John Doe", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("Invalid", record.get(2));

        try {
            record.get("Invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRecordWithoutMapping() {
        String[] values = new String[]{"Name=John Doe, Age=30, Comment=Great!"};
        CSVRecord record = new CSVRecord(values, null, "Great!", 1);

        assertEquals(3, record.size());
        assertEquals("John Doe", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("Great!", record.getComment());
    }

    @Test
    public void testRecordWithNullValues() {
        Map<String, Integer> headerMapping = new HashMap<>();
        headerMapping.put("Name", 0);
        headerMapping.put("Age", 1);

        String[] values = new String[]{"John Doe", null};
        CSVRecord record = new CSVRecord(values, headerMapping, null, 1);

        assertEquals(2, record.size());
        assertEquals("John Doe", record.get("Name"));
        assertNull(record.get("Age"));
    }

    @Test
    public void testRecordWithNullMapping() {
        String[] values = new String[]{"Name=John Doe, Age=30, Comment=Great!"};
        CSVRecord record = new CSVRecord(values, null, "Great!", 1);

        assertEquals(3, record.size());
        assertEquals("John Doe", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("Great!", record.getComment());
    }

    @Test
    public void testRecordWithEmptyMapping() {
        String[] values = new String[]{"Name=John Doe, Age=30, Comment=Great!"};
        CSVRecord record = new CSVRecord(values, new HashMap<>(), "Great!", 1);

        assertEquals(3, record.size());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Age"));
        assertFalse(record.isMapped("Comment"));
    }

    @Test
    public void testRecordWithInvalidIndex() {
        Map<String, Integer> headerMapping = new HashMap<>();
        headerMapping.put("Name", 0);
        headerMapping.put("Age", 1);

        String[] values = new String[]{"Name=John Doe, Age=30"};
        CSVRecord record = new CSVRecord(values, headerMapping, null, 1);

        try {
            record.get(2);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            record.get("Invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}