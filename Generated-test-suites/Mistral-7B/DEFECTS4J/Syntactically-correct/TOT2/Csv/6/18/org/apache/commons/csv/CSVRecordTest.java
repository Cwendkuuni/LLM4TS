import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] values = new String[]{"John", "Doe", "30", "Male", "New York"};
 private Map<String, Integer> mapping;
 private CSVRecord record;

 @Test
 public void testCSVRecordWithValidValues() {
 mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 mapping.put("City", 4);
 record = new CSVRecord(values, mapping, null, 1);

 Assert.assertEquals("John", record.get(Enum.valueOf(CSVRecord.class, "Name").getName()));
 Assert.assertEquals("Doe", record.get(Enum.valueOf(CSVRecord.class, "Name").getName()));
 Assert.assertEquals("30", record.get(Enum.valueOf(CSVRecord.class, "Age").getName()));
 Assert.assertEquals("Male", record.get(Enum.valueOf(CSVRecord.class, "Gender").getName()));
 Assert.assertEquals("New York", record.get(Enum.valueOf(CSVRecord.class, "City").getName()));
 Assert.assertEquals("New York", record.get("City"));
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordWithEmptyValues() {
 mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 mapping.put("City", 4);
 record = new CSVRecord(new String[0], mapping, null, 1);

 Assert.assertEquals(0, record.size());
 Assert.assertNull(record.getComment());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidMapping() {
 mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Name", 1);
 mapping.put("Age", 2);
 mapping.put("City", 4);
 record = new CSVRecord(values, mapping, null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInconsistentMappingAndValues() {
 mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Name", 1);
 mapping.put("Age", 2);
 mapping.put("City", 4);
 record = new CSVRecord(new String[]{"John", "Doe", "30", "Male"}, mapping, null, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCSVRecordWithInvalidIndex() {
 mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("City", 4);
 record = new CSVRecord(values, mapping, null, 1);
 record.get(5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithNullMapping() {
 mapping = null;
 record = new CSVRecord(values, mapping, null, 1);
 }

 @Test
 public void testCSVRecordWithEmptyMapping() {
 mapping = new HashMap<>();
 record = new CSVRecord(values, mapping, null, 1);
 }

 @Test
 public void testCSVRecordWithComment() {
 mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 mapping.put("City", 4);
 record = new CSVRecord(values, mapping, "Comment for record", 1);
 Assert.assertEquals("Comment for record", record.getComment());
 }

}