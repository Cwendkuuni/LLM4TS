import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstruction() {
        // Test empty CSVRecord
        CSVRecord emptyRecord = new CSVRecord(null, null, null, 0L);
        Assert.assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, emptyRecord.values());
        Assert.assertNull(emptyRecord.comment);
        Assert.assertNull(emptyRecord.mapping);
        Assert.assertEquals(0L, emptyRecord.getRecordNumber());
        Assert.assertTrue(emptyRecord.isConsistent());

        // Test CSVRecord with values and mapping
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();
        Assert.assertArrayEquals(new String[]{"John", "30", "New York"}, record.values());
        Assert.assertEquals("", record.comment);
        Map<String, Integer> expectedMapping = new HashMap<>();
        expectedMapping.put("Name", 0);
        expectedMapping.put("Age", 1);
        expectedMapping.put("City", 2);
        Assert.assertEquals(expectedMapping, record.mapping);
        Assert.assertEquals(1L, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        // Test CSVRecord with empty mapping
        String csvWithoutHeader = "Age,30\n28";
        CSVParser parserWithoutHeader = format.parse(new StringReader(csvWithoutHeader));
        CSVRecord recordWithoutHeader = parserWithoutHeader.nextRecord();
        Assert.assertNull(recordWithoutHeader.mapping);
        Assert.assertArrayEquals(new String[]{"30"}, recordWithoutHeader.values());
        Assert.assertEquals(1L, recordWithoutHeader.getRecordNumber());
        Assert.assertTrue(recordWithoutHeader.isConsistent());
    }

    @Test
    public void testCSVRecordGet() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        // Test get by index
        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals("30", record.get(1));
        Assert.assertEquals("New York", record.get(2));

        // Test get by name
        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));

        // Test get by enum
        EnumTest enumTest = new EnumTest();
        Assert.assertEquals("John", record.get(enumTest.Name));
        Assert.assertEquals("30", record.get(enumTest.Age));
        Assert.assertEquals("New York", record.get(enumTest.City));
    }

    @Test
    public void testCSVRecordGetComment() {
        String csv = "Name,Age,City,Comment\nJohn,30,New York,Hello\nMary,28,Paris,,World";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Assert.assertEquals("Hello", record.getComment());
        Assert.assertNull(parser.nextRecord().getComment());
        Assert.assertNull(parser.nextRecord().getComment());
    }

    @Test
    public void testCSVRecordGetRecordNumber() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris\nSusan,25,London";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));

        CSVRecord record1 = parser.nextRecord();
        Assert.assertEquals(1L, record1.getRecordNumber());

        CSVRecord record2 = parser.nextRecord();
        Assert.assertEquals(2L, record2.getRecordNumber());

        CSVRecord record3 = parser.nextRecord();
        Assert.assertEquals(3L, record3.getRecordNumber());
    }

    @Test
    public void testCSVRecordIsConsistent() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris\nSusan,25,London";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));

        CSVRecord record1 = parser.nextRecord();
        Assert.assertTrue(record1.isConsistent());

        String csvWithoutHeader = "Age,30\n28";
        CSVParser parserWithoutHeader = format.parse(new StringReader(csvWithoutHeader));
        CSVRecord recordWithoutHeader = parserWithoutHeader.nextRecord();
        Assert.assertTrue(recordWithoutHeader.isConsistent());

        String csvWithMissingValue = "Name,Age,City\nJohn,30,New York\nMary,28";
        CSVParser parserWithMissingValue = format.parse(new StringReader(csvWithMissingValue));
        CSVRecord recordWithMissingValue = parserWithMissingValue.nextRecord();
        Assert.assertFalse(recordWithMissingValue.isConsistent());
    }

    @Test
    public void testCSVRecordIsMapped() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));

        Assert.assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));

        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testCSVRecordIterator() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Iterator<String> recordIterator = record.iterator();

        Assert.assertEquals("John", recordIterator.next());
        Assert.assertEquals("30", recordIterator.next());
        Assert.assertEquals("New York", recordIterator.next());
        Assert.assertFalse(recordIterator.hasNext());
    }

    @Test
    public void testCSVRecordPutIn() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Map<String, String> map = record.putIn(new HashMap<>());

        Assert.assertEquals("John", map.get("Name"));
        Assert.assertEquals("30", map.get("Age"));
        Assert.assertEquals("New York", map.get("City"));
    }

    @Test
    public void testCSVRecordSize() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordToList() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        List<String> recordList = record.toList();

        Assert.assertEquals("John", recordList.get(0));
        Assert.assertEquals("30", recordList.get(1));
        Assert.assertEquals("New York", recordList.get(2));
        Assert.assertEquals(3, recordList.size());
    }

    @Test
    public void testCSVRecordToMap() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Map<String, String> recordMap = record.toMap();

        Assert.assertEquals("John", recordMap.get("Name"));
        Assert.assertEquals("30", recordMap.get("Age"));
        Assert.assertEquals("New York", recordMap.get("City"));
    }

    @Test
    public void testCSVRecordToString() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        Assert.assertEquals("[John, 30, New York]", record.toString());
    }

    @Test
    public void testCSVRecordValues() {
        String csv = "Name,Age,City\nJohn,30,New York\nMary,28,Paris";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();

        String[] recordValues = record.values();

        Assert.assertEquals("John", recordValues[0]);
        Assert.assertEquals("30", recordValues[1]);
        Assert.assertEquals("New York", recordValues[2]);
        Assert.assertEquals(3, recordValues.length);
    }

    enum EnumTest {
        Name,
        Age,
        City
    }
}