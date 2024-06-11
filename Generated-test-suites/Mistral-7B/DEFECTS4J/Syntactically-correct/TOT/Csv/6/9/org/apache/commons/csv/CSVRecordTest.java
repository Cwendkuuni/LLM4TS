import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_Basic() {
        String[] values = new String[]{"John Doe", "12345", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("ID", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertEquals("12345", record.get(1));
        Assert.assertEquals("New York", record.get(2));
        Assert.assertEquals(null, record.get("Unknown"));
        Assert.assertEquals(1, record.size());
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecord_NoMapping() {
        String[] values = new String[]{"John Doe", "12345", "New York"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        Assert.assertNull(record.getComment());
        Assert.assertArrayEquals(values, record.values());
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecord_InvalidMapping() {
        String[] values = new String[]{"John Doe", "12345", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("City", 3); // index out of bounds

        try {
            new CSVRecord(values, mapping, null, 1L);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCSVRecord_NullValue() {
        String[] values = new String[]{"John Doe", null, "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("City", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertNull(record.get(1));
        Assert.assertEquals(2, record.size());
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecord_EmptyRecord() {
        CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1L);

        Assert.assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values());
        Assert.assertEquals(0, record.size());
        Assert.assertTrue(record.isConsistent());
    }
}