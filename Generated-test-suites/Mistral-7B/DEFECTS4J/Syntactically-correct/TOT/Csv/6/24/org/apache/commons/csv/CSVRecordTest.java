package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordBuilder;

/**
 * Unit tests for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertEquals(3, record.size());

        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertSame(0L, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("unknown"));

        final CSVRecordBuilder builder = new CSVRecordBuilder();
        builder.add("header1", "value1");
        builder.add("header2", "value2");
        builder.add("header3", "value3");
        builder.setComment("comment");
        builder.setRecordNumber(1L);

        final CSVRecord recordFromBuilder = builder.build();
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, recordFromBuilder.values());
        assertEquals("comment", recordFromBuilder.getComment());
        assertEquals(1L, recordFromBuilder.getRecordNumber());
        assertTrue(recordFromBuilder.isConsistent());
        assertTrue(recordFromBuilder.isMapped("header1"));
        assertTrue(recordFromBuilder.isMapped("header2"));
        assertTrue(recordFromBuilder.isMapped("header3"));
        assertFalse(recordFromBuilder.isMapped("unknown"));
    }

    /**
     * Test get method.
     */
    @org.junit.Test
    public void testGet() {
        final String[] values = new String[] { "a", "b", "c", null };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertEquals("a", record.get("header1"));
        assertEquals("b", record.get("header2"));
        assertEquals("c", record.get("header3"));
        assertNull(record.get("unknown"));
        assertNull(record.get(3));

        final String comment = "comment";
        final long recordNumber = 1L;
        final CSVRecord recordWithCommentAndNumber = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, recordWithCommentAndNumber.getComment());
        assertEquals(recordNumber, recordWithCommentAndNumber.getRecordNumber());
    }

    /**
     * Test isConsistent method.
     */
    @org.junit.Test
    public void testIsConsistent() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertTrue(record.isConsistent());

        // Test inconsistent record
        final String[] valuesInconsistent = new String[] { "a", "b", "c", "d" };
        final Map<String, Integer> mappingInconsistent = new HashMap<>();
        mappingInconsistent.put("header1", 0);
        mappingInconsistent.put("header2", 1);
        mappingInconsistent.put("header3", 2);
        mappingInconsistent.put("header4", 3);

        final CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, mappingInconsistent, null, 0L);
        assertFalse(recordInconsistent.isConsistent());

        // Test inconsistent record with comment
        final String comment = "comment";
        final long recordNumber = 1L;
        final CSVRecord recordInconsistentWithComment = new CSVRecord(valuesInconsistent, mappingInconsistent, comment,
                recordNumber);
        assertFalse(recordInconsistentWithComment.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @org.junit.Test
    public void testIsMapped() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("unknown"));

        final String comment = "comment";
        final long recordNumber = 1L;
        final CSVRecord recordWithCommentAndNumber = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(recordWithCommentAndNumber.isMapped("header1"));
        assertTrue(recordWithCommentAndNumber.isMapped("header2"));
        assertTrue(recordWithCommentAndNumber.isMapped("header3"));
        assertFalse(recordWithCommentAndNumber.isMapped("unknown"));
    }

    /**
     * Test iterator method.
     */
    @org.junit.Test
    public void testIterator() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        final Iterator<String> iterator = record.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test putIn method.
     */
    @org.junit.Test
    public void testPutIn() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, String> map = new HashMap<>();

        final CSVRecord record = new CSVRecord(values, null, null, 0L);
        map.putAll(record.putIn(map));

        assertEquals(values[0], map.get("header0"));
        assertEquals(values[1], map.get("header1"));
        assertEquals(values[2], map.get("header2"));

        final Map<String, String> mapConsistent = new HashMap<>();
        final CSVRecord recordConsistent = new CSVRecord(new String[] { "a", "b", "c", "d" }, new HashMap<>(), null, 0L);
        mapConsistent.putAll(recordConsistent.putIn(mapConsistent));
        assertEquals(4, mapConsistent.size());
    }

    /**
     * Test size method.
     */
    @org.junit.Test
    public void testSize() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertEquals(3, record.size());

        final String comment = "comment";
        final long recordNumber = 1L;
        final CSVRecord recordWithCommentAndNumber = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(3, recordWithCommentAndNumber.size());
    }

    /**
     * Test toList method.
     */
    @org.junit.Test
    public void testToList() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        final List<String> list = record.toList();
        assertEquals(Arrays.asList("a", "b", "c"), list);
    }

    /**
     * Test toMap method.
     */
    @org.junit.Test
    public void testToMap() {
        final String[] values = new String[] { "a", "b", "c" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        final Map<String, String> map = record.toMap();
        assertEquals(map, record.putIn(new HashMap<>()));
        assertEquals(Arrays.asList("a", "b", "c"), map.values());
    }
}