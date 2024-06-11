import org.apache.commons.csv.*;
import org.junit.Assert.*; // for assertions
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        // Test empty CSVRecord
        CSVRecord record = new CSVRecord(null, null, null, 0L);
        Assert.assertEquals(0, record.size());
        Assert.assertNull(record.getComment());
        Assert.assertNull(record.getRecordNumber());
        Assert.assertFalse(record.isConsistent());

        // Test CSVRecord with valid values
        String[] validValues = {"Name1", "Age:18", "City:New York"};
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};
        CSVRecord validRecord = new CSVRecord(validValues, headerMapping, null, 1L);
        Assert.assertEquals(3, validRecord.size());
        Assert.assertNull(validRecord.getComment());
        Assert.assertEquals(1L, validRecord.getRecordNumber());
        Assert.assertTrue(validRecord.isConsistent());

        // Test getters
        Assert.assertEquals("Name1", validRecord.get(0));
        Assert.assertEquals("Age:18", validRecord.get(1));
        Assert.assertEquals("City:New York", validRecord.get(2));
        Assert.assertEquals("Name1", validRecord.get("Name"));
        Assert.assertEquals("Age:18", validRecord.get("Age"));
        Assert.assertEquals("City:New York", validRecord.get("City"));
        Assert.assertNull(validRecord.get("Unknown"));

        // Test invalid getters
        try {
            validRecord.get(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            validRecord.get(3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getComment() and getRecordNumber()
        Assert.assertNull(validRecord.getComment());
        Assert.assertEquals(1L, validRecord.getRecordNumber());

        // Test isConsistent()
        Assert.assertTrue(validRecord.isConsistent());

        // Test isMapped() and isSet()
        Assert.assertTrue(validRecord.isMapped("Name"));
        Assert.assertTrue(validRecord.isSet("Name"));
        Assert.assertTrue(validRecord.isMapped("Age"));
        Assert.assertTrue(validRecord.isSet("Age"));
        Assert.assertTrue(validRecord.isMapped("City"));
        Assert.assertTrue(validRecord.isSet("City"));
        Assert.assertFalse(validRecord.isMapped("Unknown"));
        Assert.assertFalse(validRecord.isSet("Unknown"));

        // Test iterator() and toList()
        Iterator<String> recordIterator = validRecord.iterator();
        List<String> recordList = validRecord.toList();
        Assert.assertEquals(validRecord.values(), recordList.toArray());
        String expectedIteratorValue = validRecord.get(0);
        String actualIteratorValue = recordIterator.next();
        Assert.assertEquals(expectedIteratorValue, actualIteratorValue);

        // Test putIn()
        Map<String, String> map = new HashMap<>();
        validRecord.putIn(map);
        String expectedMapValue = validRecord.get(0);
        String actualMapValue = map.get("Name");
        Assert.assertEquals(expectedMapValue, actualMapValue);

        // Test size() and toMap()
        Assert.assertEquals(3, validRecord.size());
        Map<String, String> map2 = validRecord.toMap();
        Assert.assertEquals(validRecord.get(0), map2.get("Name"));
        Assert.assertEquals(validRecord.get(1), map2.get("Age"));
        Assert.assertEquals(validRecord.get(2), map2.get("City"));
    }
}