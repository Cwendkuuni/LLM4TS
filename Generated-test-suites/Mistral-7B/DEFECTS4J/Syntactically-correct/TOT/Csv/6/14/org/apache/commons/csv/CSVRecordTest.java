package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test for CSVRecord class
 */
public class CSVRecordTest {

    /**
     * Test for get method with valid input
     */
    @Test
    public void testGetWithValidInput() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago\n";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record = parser.nextRecord();

        assertEquals("Alice", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("New York", record.get("City"));
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("New York", record.get(2));
    }

    /**
     * Test for get method with invalid input: no header mapping
     */
    @Test
    public void testGetWithNoHeaderMapping() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago\n";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record = parser.nextRecord();

        assertThrows(IllegalStateException.class, () -> record.get("Name"));
    }

    /**
     * Test for get method with invalid input: invalid column name
     */
    @Test
    public void testGetWithInvalidColumnName() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago\n";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record = parser.nextRecord();

        assertThrows(IllegalArgumentException.class, () -> record.get("InvalidName"));
    }

    /**
     * Test for get method with invalid input: inconsistent record
     */
    @Test
    public void testGetWithInconsistentRecord() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25\n"; // missing City value
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record = parser.nextRecord();

        assertThrows(IllegalArgumentException.class, () -> record.get("City"));
    }

    /**
     * Test for get method with enum input
     */
    @Test
    public void testGetWithEnumInput() {
        String csvContent = "Name, Age, City\nAlice, 30, New York\nBob, 25, Chicago\n";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record = parser.nextRecord();

        assertEquals("Alice", record.get(CSVRecordTest.Column.NAME.toString()));
        assertEquals("30", record.get(CSVRecordTest.Column.AGE.toString()));
        assertEquals("New York", record.get(CSVRecordTest.Column.CITY.toString()));
    }

    /**
     * Test for getComment method
     */
    @Test
    public void testGetComment() {
        String csvContent = "Name,Age,City\nAlice,30,New York#This is a comment\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record = parser.nextRecord();

        assertEquals("This is a comment", record.getComment());
        assertNull(parser.nextRecord().getComment()); // no comment in second record
    }

    /**
     * Test for getRecordNumber method
     */
    @Test
    public void testGetRecordNumber() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();

        assertEquals(1L, record1.getRecordNumber());
        assertEquals(2L, record2.getRecordNumber());
    }

    /**
     * Test for isConsistent method with valid input
     */
    @Test
    public void testIsConsistentWithValidInput() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertTrue(record1.isConsistent());
    }

    /**
     * Test for isConsistent method with invalid input: no header mapping
     */
    @Test
    public void testIsConsistentWithNoHeaderMapping() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertFalse(record1.isConsistent());
    }

    /**
     * Test for isMapped method with valid input
     */
    @Test
    public void testIsMappedWithValidInput() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertTrue(record1.isMapped("Name"));
        assertTrue(record1.isMapped("Age"));
        assertTrue(record1.isMapped("City"));
    }

    /**
     * Test for isMapped method with invalid input: no header mapping
     */
    @Test
    public void testIsMappedWithNoHeaderMapping() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertFalse(record1.isMapped("InvalidName"));
    }

    /**
     * Test for isSet method with valid input
     */
    @Test
    public void testIsSetWithValidInput() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertTrue(record1.isSet("Name"));
        assertTrue(record1.isSet("Age"));
        assertTrue(record1.isSet("City"));
    }

    /**
     * Test for isSet method with invalid input: no header mapping
     */
    @Test
    public void testIsSetWithNoHeaderMapping() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertFalse(record1.isSet("InvalidName"));
    }

    /**
     * Test for iterator method
     */
    @Test
    public void testIterator() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        List<String> values = Arrays.asList(record1.values());

        assertEquals(3, values.size());
        assertEquals("Alice", values.get(0));
        assertEquals("30", values.get(1));
        assertEquals("New York", values.get(2));
    }

    /**
     * Test for putIn method
     */
    @Test
    public void testPutIn() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        Map<String, String> map = new HashMap<>();
        record1.putIn(map);

        assertEquals(3, map.size());
        assertEquals("Alice", map.get("Name"));
        assertEquals("30", map.get("Age"));
        assertEquals("New York", map.get("City"));
    }

    /**
     * Test for size method
     */
    @Test
    public void testSize() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertEquals(3, record1.size());
    }

    /**
     * Test for toList method
     */
    @Test
    public void testToList() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        List<String> values = record1.toList();

        assertEquals(3, values.size());
        assertEquals("Alice", values.get(0));
        assertEquals("30", values.get(1));
        assertEquals("New York", values.get(2));
    }

    /**
     * Test for toMap method
     */
    @Test
    public void testToMap() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        Map<String, String> map = record1.toMap();

        assertEquals(3, map.size());
        assertEquals("Name", map.keySet().iterator().next());
        assertEquals("Alice", map.get("Name"));
        assertEquals("Age", map.keySet().iterator().next());
        assertEquals("30", map.get("Age"));
        assertEquals("City", map.keySet().iterator().next());
        assertEquals("New York", map.get("City"));
    }

    /**
     * Test for toString method
     */
    @Test
    public void testToString() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        assertEquals("[Alice, 30, New York]", record1.toString());
    }

    /**
     * Test for values method
     */
    @Test
    public void testValues() {
        String csvContent = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago";
        CSVFormat format = new CSVFormat().withHeader();
        CSVParser parser = format.newParser(new StringReader(csvContent));
        CSVRecord record1 = parser.nextRecord();

        String[] values = record1.values();

        assertEquals(3, values.length);
        assertEquals("Alice", values[0]);
        assertEquals("30", values[1]);
        assertEquals("New York", values[2]);
    }

    /**
     * Enum for column names
     */
    enum Column {
        NAME, AGE, CITY
    }
}