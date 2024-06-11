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
    public void testCSVRecord() {
        String csvData = "name,age,city\nJohn Doe,30,New York\nJane Doe,28,Los Angeles\n";

        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = format.newParser(new StringReader(csvData));

        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);

        // Test get methods
        String name = record.get("name");
        Assert.assertEquals("John Doe", name);

        int age = record.get("age").isEmpty() ? -1 : Integer.parseInt(record.get("age"));
        Assert.assertEquals(30, age);

        String city = record.get("city");
        Assert.assertEquals("New York", city);

        String emptyValue = record.get("empty");
        Assert.assertNull(emptyValue);

        long recordNumber = record.getRecordNumber();
        Assert.assertEquals(1, recordNumber);

        boolean isConsistent = record.isConsistent();
        Assert.assertTrue(isConsistent);

        boolean isMapped = record.isMapped("name");
        Assert.assertTrue(isMapped);

        boolean isSet = record.isSet("name");
        Assert.assertTrue(isSet);

        boolean isSetEmpty = record.isSet("empty");
        Assert.assertFalse(isSetEmpty);

        // Test get methods by index
        String nameByIndex = record.get(0);
        Assert.assertEquals("John Doe", nameByIndex);

        int ageByIndex = Integer.parseInt(record.get(1));
        Assert.assertEquals(30, ageByIndex);

        String cityByIndex = record.get(2);
        Assert.assertEquals("New York", cityByIndex);

        // Test get method by Enum
        Enum<String> nameEnum = Enum.valueOf(String.class, "name");
        String nameByEnum = record.get(nameEnum);
        Assert.assertEquals("John Doe", nameByEnum);

        // Test iterator method
        Iterator<String> iterator = record.iterator();
        List<String> values = Arrays.asList("John Doe", "30", "New York");
        for (int i = 0; i < values.size(); i++) {
            Assert.assertEquals(values.get(i), iterator.next());
        }

        // Test putIn method
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        List<Map.Entry<String, String>> entries = Arrays.asList(
                Map.entry("name", "John Doe"),
                Map.entry("age", "30"),
                Map.entry("city", "New York")
        );
        for (int i = 0; i < entries.size(); i++) {
            Assert.assertEquals(entries.get(i).getKey(), map.keySet().toArray()[i].toString());
            Assert.assertEquals(entries.get(i).getValue(), map.get(entries.get(i).getKey()));
        }

        // Test size method
        int size = record.size();
        Assert.assertEquals(3, size);

        // Test toString method
        String expectedToString = "[John Doe, 30, New York]";
        String toString = record.toString();
        Assert.assertEquals(expectedToString, toString);

        // Test values method
        String[] valuesArray = record.values();
        Assert.assertArrayEquals(values.toArray(new String[0]), valuesArray);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String csvData = "age,city\n30,New York\n28,Los Angeles\n";

        CSVFormat format = CSVFormat.DEFAULT.withNoHeader();
        CSVParser parser = format.newParser(new StringReader(csvData));

        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);

        String name = record.get("name"); // should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMappingNotFound() {
        String csvData = "name,age,city\nJohn Doe,30,New York\nJane Doe,28,Los Angeles\n";

        CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("age", "name", "city"))
                .build();
        CSVParser parser = format.newParser(new StringReader(csvData));

        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);

        String nonExistentColumn = "email";
        record.get(nonExistentColumn); // should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentRecord() {
        String csvData = "name,age,city\nJohn Doe,30,New York\nJane Doe,28,Los Angeles\n";

        CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("name", "age", "city", "email"))
                .build();
        CSVParser parser = format.newParser(new StringReader(csvData));

        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);

        record.get(3); // should throw an exception
    }
}