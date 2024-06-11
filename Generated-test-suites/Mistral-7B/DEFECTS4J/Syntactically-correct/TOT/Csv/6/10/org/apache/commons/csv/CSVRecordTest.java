import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String[] values = new String[]{"John Doe", "30", "M", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        mapping.put("City", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("M", record.get("Gender"));
        Assert.assertEquals("New York", record.get("City"));
        Assert.assertEquals("New York", record.get(3));
        Assert.assertEquals("30", record.get(1));
        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertEquals(1, record.size());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("Gender"));
        Assert.assertTrue(record.isSet("City"));
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isEmpty());
        Assert.assertFalse(record.isConsistent());
        Assert.assertFalse(record.isMapped("Name"));
        Assert.assertFalse(record.isSet("Name"));
        Assert.assertFalse(record.isSet("Age"));
        Assert.assertFalse(record.isSet("Gender"));
        Assert.assertFalse(record.isSet("City"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"John Doe", "30", "M", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        mapping.put("City", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(values.length + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidName() {
        String[] values = new String[]{"John Doe", "30", "M", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        mapping.put("City", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("InvalidName");
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"John Doe", "30", "M", "New York"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
    }
}