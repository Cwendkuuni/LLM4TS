import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordEmpty() {
 CSVRecord record = new CSVRecord(null, null, null, 0L);
 assertTrue(record.isConsistent());
 assertEquals(0, record.size());
 assertNull(record.getComment());
 assertNull(record.getRecordNumber());
 }

 @Test
 public void testCSVRecordBasic() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("City", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);

 assertTrue(record.isConsistent());
 assertEquals(values.length, record.size());
 assertNull(record.getComment());
 assertEquals(1L, record.getRecordNumber());

 assertEquals("Name", record.get(0));
 assertEquals("Age", record.get(1));
 assertEquals("City", record.get(2));

 assertNull(record.get("UnknownColumn"));

 assertEquals("Name", record.get("Name"));
 assertEquals("Age", record.get("Age"));
 assertEquals("City", record.get("City"));
 }

 @Test
 public void testCSVRecordWithComment() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("City", 2);
 CSVRecord record = new CSVRecord(values, mapping, "Comment", 1L);

 assertTrue(record.isConsistent());
 assertEquals(values.length, record.size());
 assertEquals("Comment", record.getComment());
 assertEquals(1L, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordWithMissingMapping() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);

 assertTrue(record.isConsistent());
 assertEquals(values.length, record.size());
 assertNull(record.getComment());
 assertEquals(1L, record.getRecordNumber());

 try {
 record.get("City");
 fail("An exception should have been thrown!");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCSVRecordWithMismatchedMapping() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 2);
 mapping.put("City", 1);
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);

 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithNullString() {
 CSVFormat format = CSVFormat.DEFAULT.withNullString("");
 CSVParser parser = format.newParser();
 String csv = "Name,Age,City\n" +
 "John Doe,25,\"\n" +
 "Jane Doe,30,\"New York\"\n";
 List<CSVRecord> records = parser.parseAll(new StringReader(csv));
 CSVRecord record = records.get(0);

 assertTrue(record.isConsistent());
 assertEquals(3, record.size());
 assertNull(record.getComment());
 assertEquals(0L, record.getRecordNumber());

 assertEquals("", record.get("Age"));
 assertEquals("John Doe", record.get("Name"));
 assertNull(record.get("City"));
 }
}