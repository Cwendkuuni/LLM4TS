import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals("Doe", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));
        Assert.assertNull(record.getComment());
        Assert.assertTrue(record.isConsistent());
    }

    // Test cases for each method (omitted for brevity)
}