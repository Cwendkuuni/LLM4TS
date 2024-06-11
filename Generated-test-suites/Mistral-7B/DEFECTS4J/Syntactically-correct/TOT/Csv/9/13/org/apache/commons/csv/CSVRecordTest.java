import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("Alice", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));
        Assert.assertEquals("alice@example.com", record.get("Email"));
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertNull(record.get("Name"));
        Assert.assertNull(record.get("Age"));
        Assert.assertNull(record.get("Email"));
        Assert.assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithMissingColumn() {
        String[] values = new String[]{"Alice", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Email");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInconsistentValues() {
        String[] values = new String[]{"Alice", "25", null};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Email");
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);

        String comment = "This record represents Alice.";

        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        Assert.assertEquals(comment, record.getComment());
    }
}