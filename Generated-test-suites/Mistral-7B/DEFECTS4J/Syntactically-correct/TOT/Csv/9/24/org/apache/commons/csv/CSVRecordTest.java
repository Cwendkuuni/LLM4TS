package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles\n";

        CSVFormat format = new CSVFormat.Builder().build();
        CSVParser parser = format.newParser();
        parser.parse(new StringReader(csvData));

        // Test getting values by name
        CSVRecord record = parser.getRecord();
        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));

        // Test getting values by index
        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertEquals("30", record.get(1));
        Assert.assertEquals("New York", record.get(2));

        // Test getting values by Enum
        Assert.assertEquals("John Doe", record.get(CSVRecordTest.Name.NAME));
        Assert.assertEquals("30", record.get(CSVRecordTest.Age.AGE));
        Assert.assertEquals("New York", record.get(CSVRecordTest.City.CITY));

        // Test getting comment
        Assert.assertNull(record.getComment());

        // Test getting record number
        Assert.assertEquals(1L, record.getRecordNumber());

        // Test isConsistent
        Assert.assertTrue(record.isConsistent());

        // Test isMapped
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));

        // Test isSet
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));

        // Test iterator
        Map<String, String> map = record.toMap();
        for (String value : record) {
            Assert.assertEquals(map.get(CSVRecordTest.getName(value)), value);
        }

        // Test putIn
        Map<String, String> newMap = new HashMap<>();
        record.putIn(newMap);
        Assert.assertEquals(map, newMap);

        // Test size
        Assert.assertEquals(3, record.size());

        // Test toList
        List<String> list = record.toList();
        Assert.assertEquals(Arrays.asList("John Doe", "30", "New York"), list);

        // Test toMap
        Map<String, String> newMap2 = record.toMap();
        Assert.assertEquals(map, newMap2);

        // Test toString
        Assert.assertEquals("[John Doe, 30, New York]", record.toString());
    }

    private static final class Name {
        static final String NAME = "Name";
    }

    private static final class Age {
        static final String AGE = "Age";
    }

    private static final class City {
        static final String CITY = "City";
    }
}