package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidData() {
        String csvData = "Name,Age,City\nJohn Doe,25,New York\nJane Doe,30,Chicago";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));
        CSVRecord record = parser.nextRecord();

        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));
        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertEquals("25", record.get(1));
        Assert.assertEquals("New York", record.get(2));

        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));

        Map<String, String> map = record.toMap();
        List<String> values = record.toList();
        Assert.assertEquals(Arrays.asList("Name", "Age", "City"), map.keySet());
        Assert.assertEquals(Arrays.asList("John Doe", "25", "New York"), values);
    }

    @Test
    public void testCSVRecordWithEmptyData() {
        String csvData = "\n";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));
        CSVRecord record = parser.nextRecord();

        Assert.assertNull(record.getComment());
        Assert.assertArrayEquals(new String[0], record.values());
        Assert.assertNull(record.get("Name"));
        Assert.assertNull(record.get("Age"));
        Assert.assertNull(record.get("City"));
        Assert.assertFalse(record.isConsistent());
        Assert.assertFalse(record.isMapped("Name"));
        Assert.assertFalse(record.isMapped("Age"));
        Assert.assertFalse(record.isMapped("City"));
        Assert.assertFalse(record.isSet("Name"));
        Assert.assertFalse(record.isSet("Age"));
        Assert.assertFalse(record.isSet("City"));
    }

    @Test
    public void testCSVRecordWithInvalidData() {
        String csvData = "Name,Age,City\nJohn Doe,25,New York\nJane Doe,30,Chicago\n";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));
        CSVRecord record = parser.nextRecord();

        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));

        // Try to access non-existing column
        Assert.assertNull(record.get("InvalidColumnName"));

        // Try to access column with index out of bounds
        Assert.assertNull(record.get(3));

        // Try to access null column
        CSVRecord nullRecord = new CSVRecord(null, null, null, 0L);
        Assert.assertNull(nullRecord.get(0));

        // Try to access comment on empty record
        Assert.assertNull(record.getComment());
    }
}