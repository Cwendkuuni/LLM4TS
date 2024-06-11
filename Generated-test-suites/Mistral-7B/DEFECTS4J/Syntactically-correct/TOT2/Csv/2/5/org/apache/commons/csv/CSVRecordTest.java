import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {
    

    @Test
    public void testGet() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String name = "Name";
        String expectedName = "John";
        String expectedAge = "30";

        // Test get(int)
        String actualName = record.get(0);
        Assert.assertEquals(expectedName, actualName);

        // Test get(int) with out-of-bound index
        String actualAge = record.get(values.length);
        Assert.assertNull(actualAge);

        // Test get(String)
        String actualExpectedName = record.get(name);
        Assert.assertEquals(expectedName, actualExpectedName);

        // Test get(String) with null mapping
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);
        Assert.assertNull(recordWithoutMapping.get("Name"));

        // Test get(String) with null value
        CSVRecord recordWithNullValue = new CSVRecord(new String[]{"John", null}, mapping, null, 1);
        Assert.assertNull(recordWithNullValue.get("Age"));

        // Test isConsistent()
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        Assert.assertFalse(inconsistentRecord.isConsistent());

        // Test isMapped(String) with invalid column name
        String invalidColumnName = "InvalidColumnName";
        Assert.assertFalse(record.isMapped(invalidColumnName));

        // Test isSet(String) with invalid column name
        Assert.assertFalse(record.isSet(invalidColumnName));

        // Test size()
        Assert.assertEquals(values.length, record.size());

        // Test toString()
        Assert.assertEquals("[John, Doe, 30]", record.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        record.get(-1);
    }

    @Test
    public void testIsConsistentWithInconsistentRecord() {
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        Assert.assertFalse(inconsistentRecord.isConsistent());
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullMapping() {
        String[] values = new String[]{"John", "Doe", "30"};
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);
        recordWithoutMapping.get("Name");
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullValue() {
        String[] values = new String[]{"John", null, "30"};
        CSVRecord recordWithNullValue = new CSVRecord(values, null, null, 1);
        recordWithNullValue.get("Age");
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Iterator<String> iterator = record.iterator();
        String[] expectedValues = {"John", "Doe", "30"};
        int i = 0;
        while (iterator.hasNext()) {
            String actualValue = iterator.next();
            Assert.assertEquals(expectedValues[i++], actualValue);
        }
    }

    @Test
    public void testGetCommentWithNullComment() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumberWithNullRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Assert.assertEquals(0, record.getRecordNumber());
    }

    
}