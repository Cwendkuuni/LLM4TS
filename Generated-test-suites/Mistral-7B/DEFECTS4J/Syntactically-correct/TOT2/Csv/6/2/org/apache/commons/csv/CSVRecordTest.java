import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 @Test
 public void testCSVRecordBasicFunctionality() {
 String[] values = new String[]{"John Doe", "25", "New York"};
 Map<String, Integer> mapping = new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }};
 CSVRecord record = new CSVRecord(values, mapping, null, 1);

 // Test getting values by index
 Assert.assertEquals("John Doe", record.get(0));
 Assert.assertEquals("25", record.get(1));
 Assert.assertEquals("New York", record.get(2));

 // Test getting values by name
 Assert.assertEquals("John Doe", record.get("Name"));
 Assert.assertEquals("25", record.get("Age"));
 Assert.assertEquals("New York", record.get("City"));

 // Test getting comment
 Assert.assertNull(record.getComment());

 // Test getting record number
 Assert.assertEquals(1, record.getRecordNumber());

 // Test consistency
 Assert.assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoHeaderMapping() {
 CSVRecord record = new CSVRecord(new String[0], null, null, 1);
 record.get("Name"); // This should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMapping() {
 String[] values = new String[]{"John Doe", "25", "New York"};
 Map<String, Integer> mapping = new HashMap<String, Integer>() {{
 put("Name", 1);
 put("Age", 0); // Index should be smaller than the number of columns
 }};
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 record.get("Name"); // This should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInconsistentRecord() {
 String[] values = new String[]{"John Doe", "25", "New York"};
 Map<String, Integer> mapping = new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 2); // Index should be equal to the number of columns
 put("City", 1);
 }};
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 record.get("Name"); // This should throw an exception
 }
}