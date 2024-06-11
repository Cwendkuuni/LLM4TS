import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String csvData = "Name,Age,City\nJohn Doe,25,New York\nJane Doe,32,Los Angeles";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));

        CSVRecord record = parser.nextRecord();
        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));

        record = parser.nextRecord();
        Assert.assertEquals("Jane Doe", record.get("Name"));
        Assert.assertEquals("32", record.get("Age"));
        Assert.assertEquals("Los Angeles", record.get("City"));

        parser.close();
    }

    @Test
    public void testCSVRecordWithNullValues() {
        String csvData = "Name,Age,City\nJohn Doe,25,New York\nJane Doe,32,Los Angeles\n,null,San Francisco";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));

        CSVRecord record = parser.nextRecord();
        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));

        record = parser.nextRecord();
        Assert.assertEquals("Jane Doe", record.get("Name"));
        Assert.assertEquals("32", record.get("Age"));
        Assert.assertEquals("Los Angeles", record.get("City"));

        record = parser.nextRecord();
        Assert.assertNull(record.get("Name"));
        Assert.assertNull(record.get("Age"));
        Assert.assertEquals("San Francisco", record.get("City"));

        parser.close();
    }

    @Test
    public void testCSVRecordWithMissingColumns() {
        String csvData = "Name,Age\nJohn Doe,25";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));

        CSVRecord record = parser.nextRecord();
        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));

        record = parser.nextRecord(); // There's no record after the first one.
        Assert.fail("Expected no more records but got one.");

        parser.close();
    }

    @Test
    public void testCSVRecordWithEmptyMapping() {
        String csvData = "Name,Age,City\nJohn Doe,25,New York\nJane Doe,32,Los Angeles";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));

        CSVRecord record = parser.nextRecord();
        Assert.assertNull(record.mapping);

        record = parser.nextRecord();
        Assert.assertNull(record.mapping);

        parser.close();
    }

    @Test
    public void testCSVRecordWithInvalidMappingSize() {
        String csvData = "Name,Age,City\nJohn Doe,25,New York\nJane Doe,32,Los Angeles\n,null,San Francisco,test";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = csvFormat.parse(new StringReader(csvData));

        CSVRecord record = parser.nextRecord();
        Assert.assertEquals("Name", record.mapping.keySet().iterator().next());
        Assert.assertEquals(3, record.mapping.size());

        record = parser.nextRecord();
        Assert.assertEquals(3, record.mapping.size());

        record = parser.nextRecord();
        Assert.assertEquals(4, record.mapping.size()); // This one should fail

        record = parser.nextRecord(); // There's no record after the first three.
        Assert.fail("Expected no more records but got one.");

        parser.close();
    }
}