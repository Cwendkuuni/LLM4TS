import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testNewCSVRecord() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertEquals("John", record.get(0));
 assertEquals("Doe", record.get(1));
 assertEquals("30", record.get(2));
 assertEquals("30", record.get(new Integer(2)));
 assertEquals(3, record.size());
 assertFalse(record.isConsistent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNullName() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 record.get(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIndexOutOfRange() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 record.get(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMappedNameNotFound() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), null, 1);
 record.get("Gender");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMappedIndexOutOfRange() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), null, 1);
 record.get(new Integer(3));
 }

 @Test
 public void testGetComment() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withComment("Test Comment"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), "Test Comment", 1);
 assertEquals("Test Comment", record.getComment());
 }

 @Test
 public void testIsConsistent() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), null, 1);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), null, 1);
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertFalse(record.isMapped("Gender"));
 }

 @Test
 public void testIsSet() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), null, 1);
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertFalse(record.isSet("Gender"));
 }

 @Test
 public void testIterable() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), null, 1);
 List<String> values = record.toList();
 for (int i = 0; i < values.size(); i++) {
 String value = values.get(i);
 assertEquals(record.get(i), value);
 }
 }

 @Test
 public void testPutIn() {
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age"));
 CSVRecord record = new CSVRecord(new String[]{"John", "30"}, format.getHeaderMap(), null, 1);
 Map<String, String> map = new HashMap<>();
 map = record.putIn(map);
 assertEquals("John", map.get("Name"));
 assertEquals("30", map.get("Age"));
 assertEquals(2, map.size());
 }

}