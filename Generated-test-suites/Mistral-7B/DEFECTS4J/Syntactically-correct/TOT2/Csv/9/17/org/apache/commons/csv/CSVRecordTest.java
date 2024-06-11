import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecord_Basic() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30", "M"}, null, null, 1L);
 assertEquals("John", record.get(0));
 assertEquals("Doe", record.get(1));
 assertEquals("30", record.get(2));
 assertEquals("M", record.get(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_Get_InvalidIndex() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30", "M"}, null, null, 1L);
 try {
 record.get(4);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_Get_InvalidName() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30", "M"}, null, null, 1L);
 try {
 record.get("InvalidName");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCSVRecord_Get_Enum() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30", "M"}, null, null, 1L);
 assertEquals("Doe", record.get(CSVRecord.class.getDeclaredField("FIRST_NAME")));
 }

 @Test
 public void testCSVRecord_Get_Comment() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M", "comment"}, null, "comment", 1L);
 assertEquals("comment", record.getComment());
 }

 @Test
 public void testCSVRecord_Get_RecordNumber() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M"}, null, null, 1L);
 assertEquals(1L, record.getRecordNumber());
 }

 @Test
 public void testCSVRecord_IsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M"}, null, null, 1L);
 assertTrue(record.isConsistent());

 CSVRecord record2 = new CSVRecord(new String[]{"John,Doe,30,M", "comment"}, null, null, 1L);
 assertFalse(record2.isConsistent());
 }

 @Test
 public void testCSVRecord_IsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"John:Name,Doe:LastName,30:Age,M:Gender"}, null, null, 1L);
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("LastName"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("Gender"));
 }

 @Test
 public void testCSVRecord_IsSet() {
 CSVRecord record = new CSVRecord(new String[]{"John:Name,Doe:LastName,30:Age,M:Gender"}, null, null, 1L);
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("LastName"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("Gender"));
 }

 @Test
 public void testCSVRecord_Iterator() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M"}, null, null, 1L);
 List<String> values = record.toList();
 Iterator<String> it = record.iterator();
 assertEquals(values.get(0), it.next());
 assertEquals(values.get(1), it.next());
 assertEquals(values.get(2), it.next());
 assertEquals(values.get(3), it.next());
 assertFalse(it.hasNext());
 }

 @Test
 public void testCSVRecord_PutIn() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M"}, null, null, 1L);
 Map<String, String> map = new HashMap<>();
 map.putAll(record.putIn(map));
 assertEquals("John", map.get("Name"));
 assertEquals("Doe", map.get("LastName"));
 assertEquals("30", map.get("Age"));
 assertEquals("M", map.get("Gender"));
 }

 @Test
 public void testCSVRecord_Size() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M"}, null, null, 1L);
 assertEquals(4, record.size());
 }

 @Test
 public void testCSVRecord_ToString() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M"}, null, null, 1L);
 assertEquals("[John, Doe, 30, M]", record.toString());
 }

 @Test
 public void testCSVRecord_Values() {
 CSVRecord record = new CSVRecord(new String[]{"John,Doe,30,M"}, null, null, 1L);
 String[] values = record.values();
 assertEquals(4, values.length);
 assertEquals("John", values[0]);
 assertEquals("Doe", values[1]);
 assertEquals("30", values[2]);
 assertEquals("M", values[3]);
 }

 @Test
 public void testCSVRecord_Parse() throws Exception {
 String csvLine = "Name,LastName,Age,Gender,comment";
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(new StringReader(csvLine));
 CSVRecord record = parser.next();
 assertEquals("Name", record.get("Name"));
 assertEquals("LastName", record.get("LastName"));
 assertEquals(null, record.get("Age"));
 assertEquals(null, record.get("Comment"));
 }
}