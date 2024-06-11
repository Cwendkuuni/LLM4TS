import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstruction() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Boston";
        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(csvData));
        CSVRecord record = records.get(0);

        CSVRecord expectedRecord = new CSVRecord(
                new String[]{"Name", "Age", "City"},
                new HashMap<String, Integer>() {{
                    put("Name", 0);
                    put("Age", 1);
                    put("City", 2);
                }},
                null,
                1
        );

        Assert.assertEquals(expectedRecord.toString(), record.toString());
    }

    @Test
    public void testGetMethod() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Boston";
        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(csvData));
        CSVRecord record = records.get(0);

        // Test get by Enum
        Assert.assertEquals("John Doe", record.get(CSVRecord.class.getField("NAME")));

        // Test get by index
        Assert.assertEquals("John Doe", record.get(0));

        // Test get by name
        Assert.assertEquals("John Doe", record.get("Name"));

        // Test getComment method
        Assert.assertNull(record.getComment());

        // Test getRecordNumber method
        Assert.assertEquals(1, record.getRecordNumber());

        // Test isConsistent method
        Assert.assertTrue(record.isConsistent());

        // Test isMapped method
        Assert.assertTrue(record.isMapped("Name"));

        // Test isSet method
        Assert.assertTrue(record.isSet("Name"));

        // Test iterator method
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        Iterator<String> it = record.iterator();
        List<String> expectedList = Arrays.asList("Name", "John Doe");
        List<String> actualList = Arrays.asList(it.next(), it.next());
        Assert.assertEquals(expectedList, actualList);

        // Test size method
        Assert.assertEquals(2, record.size());

        // Test toList method
        List<String> expectedList2 = Arrays.asList("Name", "Age", "City");
        List<String> actualList2 = record.toList();
        Assert.assertEquals(expectedList2, actualList2);

        // Test toMap method
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "Name");
        expectedMap.put("Age", "Age");
        expectedMap.put("City", "City");
        Map<String, String> actualMap = record.toMap();
        Assert.assertEquals(expectedMap, actualMap);

        // Test toString method
        Assert.assertEquals("[Name, John Doe]", record.toString());

        // Test values method
        String[] expectedValues = new String[]{"Name", "John Doe"};
        String[] actualValues = record.values();
        Assert.assertArrayEquals(expectedValues, actualValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Boston";
        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(csvData));
        CSVRecord record = records.get(0);

        record.get("InvalidName");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutHeaderMapping() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Boston";
        List<CSVRecord> records = CSVFormat.DEFAULT.parse(new StringReader(csvData));
        CSVRecord record = records.get(0);

        record.get("Name");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByInvalidIndex() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Boston";
        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(csvData));
        CSVRecord record = records.get(0);

        record.get(10);
    }
}