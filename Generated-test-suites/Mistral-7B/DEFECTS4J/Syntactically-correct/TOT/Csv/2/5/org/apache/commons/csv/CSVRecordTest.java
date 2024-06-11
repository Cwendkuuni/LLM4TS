import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] values = new String[]{"John Doe", "123 Main St.", "New York", "NY", "12345"};
 private Map<String, Integer> mapping;

 @Test
 public void testGetByIndex() {
 CSVRecord record = new CSVRecord(values, null, null, 1);

 for (int i = 0; i < values.length; i++) {
 String expected = values[i];
 String actual = record.get(i);
 Assert.assertEquals("Unexpected value at index " + i, expected, actual);
 }
 }

 @Test
 public void testGetByName() {
 mapping = Map.of("Name", 0, "Address", 1, "City", 2, "State", 3, "Zip", 4);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);

 String name = "Name";
 String expected = "John Doe";
 String actual = record.get(name);
 Assert.assertEquals("Unexpected name value", expected, actual);

 String wrongName = "State";
 String actualWrong = record.get(wrongName);
 Assert.assertNull("Unexpected value for wrong name", actualWrong);
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 Assert.assertTrue("Unexpected inconsistent record", record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoMapping() {
 CSVRecord record = new CSVRecord(values, null, null, 1);
 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInconsistentRecord() {
 mapping = Map.of("Name", 0, "Address", 1, "City", 3, "State", 4, "Zip", 5);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 record.get("City");
 }

 @Test
 public void testIterator() {
 Iterator<String> iterator = record.iterator();
 for (int i = 0; i < values.length; i++) {
 String expected = values[i];
 String actual = iterator.next();
 Assert.assertEquals("Unexpected value at index " + i, expected, actual);
 }
 Assert.assertFalse("Iterator didn't throw NoSuchElementException", iterator.hasNext());
 }
}