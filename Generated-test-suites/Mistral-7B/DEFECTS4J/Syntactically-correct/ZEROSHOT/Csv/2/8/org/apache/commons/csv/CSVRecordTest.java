import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGetByIndex() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);
 String value = record.get(0);
 assertEquals("John", value);
 }

 @Test
 public void testCSVRecordGetByNameNullMapping() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);
 assertNull(record.get("Name"));
 }

 @Test
 public void testCSVRecordGetByNameValidMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 String nameValue = record.get("Name");
 assertEquals("John", nameValue);
 String valueValue = record.get("Value");
 assertEquals("Doe", valueValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordGetByNameNullValues() {
 CSVRecord record = new CSVRecord(new String[0], null, null, 1);
 record.get("Name");
 }

 @Test
 public void testCSVRecordIsConsistentTrue() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsConsistentFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe"}, mapOf("Name", 0, "Value", 1), null, 1);
 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsMappedTrue() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testCSVRecordIsMappedFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertFalse(record.isMapped("Value"));
 }

 @Test
 public void testCSVRecordIsSetTrue() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertTrue(record.isSet("Name"));
 }

 @Test
 public void testCSVRecordIsSetFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertFalse(record.isSet("Value"));
 }

 @Test
 public void testCSVRecordSize() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertEquals(4, record.size());
 }

 @Test
 public void testCSVRecordToString() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertEquals("[Name=John, Value=Doe]", record.toString());
 }

 @Test
 public void testCSVRecordComment() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), "comment", 1);
 assertEquals("comment", record.getComment());
 }

 @Test
 public void testCSVRecordRecordNumber() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Value", "John", "Doe", "john.doe@example.com"},
 mapOf("Name", 0, "Value", 1), null, 1);
 assertEquals(1, record.getRecordNumber());
 }

}