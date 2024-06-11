import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordInstance() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 assertTrue(record.isConsistent());
 assertEquals(3, record.size());
 assertNull(record.getComment());
 assertNull(record.getRecordNumber());
 assertEquals(3, record.values().length);
 }

 @Test
 public void testGetValueByIndex() {
 CSVRecord record = new CSVRecord(new String[]{"John", "25", "New York"}, null, null, 1);
 assertEquals("John", record.get(0));
 assertEquals("25", record.get(1));
 assertEquals("New York", record.get(2));
 }

 @Test
 public void testGetValueByName() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:25, City:New York"},
 new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }}, null, 1);
 assertEquals("John", record.get("Name"));
 assertEquals("25", record.get("Age"));
 assertEquals("New York", record.get("City"));
 assertNull(record.get("Gender"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueByNameWithoutMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:25, City:New York"}, null, null, 1);
 record.get("Gender");
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:25, City:New York"},
 new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }}, null, 1);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:25, City:New York"},
 new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }}, null, 1);
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:25, City:New York"},
 new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }}, null, 1);
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:25, City:New York"},
 new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }}, null, 1);
 Iterator<String> iterator = record.iterator();
 assertEquals("Name:John", iterator.next());
 assertEquals("Age:25", iterator.next());
 assertEquals("City:New York", iterator.next());
 }

 @Test
 public void testValues() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:25, City:New York"},
 new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }}, null, 1);
 String[] values = record.values();
 assertEquals(3, values.length);
 assertEquals("Name:John", values[0]);
 assertEquals("Age:25", values[1]);
 assertEquals("City:New York", values[2]);
 }

 @Test
 public void testComment() {
 CSVRecord record = new CSVRecordBuilder()
 .comment("Record 1 comment")
 .values(new String[]{"Name:John, Age:25, City:New York"})
 .mapping(new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }})
 .recordNumber(1)
 .build();
 assertEquals("Record 1 comment", record.getComment());
 }

 @Test
 public void testRecordNumber() {
 CSVRecord record = new CSVRecordBuilder()
 .comment("Record 1 comment")
 .values(new String[]{"Name:John, Age:25, City:New York"})
 .mapping(new HashMap<String, Integer>(){{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }})
 .recordNumber(1)
 .build();
 assertEquals(1, record.getRecordNumber());
 }

}