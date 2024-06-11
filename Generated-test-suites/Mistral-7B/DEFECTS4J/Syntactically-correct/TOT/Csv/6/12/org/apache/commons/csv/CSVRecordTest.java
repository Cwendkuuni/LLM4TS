import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String CSV_CONTENT = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Paris\n";

 @Test
 public void testCSVRecord() {
 CSVFormat csvFormat = new CSVFormat(',').withNullString("NA");
 CSVParser csvParser = csvFormat.parse(new StringReader(CSV_CONTENT));
 CSVRecord record = csvParser.nextRecord();

 // Test basic methods
 assertEquals("John Doe", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 assertEquals("Name", record.mapping.get("Name"));
 assertEquals(3, record.size());

 // Test get by index, name, and null
 assertEquals("New York", record.get(2));
 assertEquals("Age", record.mapping.get("Age"));
 assertNull(record.get(3));

 // Test get with invalid index
 try {
 record.get(-1);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }

 // Test get with invalid name
 try {
 record.get("InvalidName");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 // Test get comment and record number
 assertEquals("", record.getComment());
 assertEquals(1, record.getRecordNumber());

 // Test isConsistent and isMapped
 assertTrue(record.isConsistent());
 assertTrue(record.isMapped("Name"));

 // Test putIn and toMap
 Map<String, String> map = record.toMap();
 assertEquals("Name", map.keySet().iterator().next());
 assertEquals("John Doe", map.get("Name"));
 map = record.putIn(new HashMap<String, String>());
 assertEquals("Name", map.keySet().iterator().next());
 assertEquals("John Doe", map.get("Name"));

 // Test iterator
 List<String> values = record.toList();
 Iterator<String> iterator = record.iterator();
 for (int i = 0; i < values.size(); i++) {
 assertEquals(values.get(i), iterator.next());
 }
 }

 @Test
 public void testCSVRecordWithCommentAndRecordNumber() {
 CSVFormat csvFormat = new CSVFormat(',').withNullString("NA");
 CSVParser csvParser = csvFormat.parse(new StringReader(CSV_CONTENT + "\n---\nRecord 1\n"));
 CSVRecord record = csvParser.nextRecord();

 // Test basic methods with comment and record number
 assertEquals("John Doe", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 assertEquals("Record 1", record.getComment());
 assertEquals(1, record.getRecordNumber());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordWithoutMapping() {
 CSVFormat csvFormat = new CSVFormat(',').withNullString("NA");
 CSVParser csvParser = csvFormat.parse(new StringReader(CSV_CONTENT));
 CSVRecord record = csvParser.nextRecord();
 record.mapping = null;
 // should throw exception
 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidMapping() {
 CSVFormat csvFormat = new CSVFormat(',').withNullString("NA");
 CSVParser csvParser = csvFormat.parse(new StringReader(CSV_CONTENT + "\nName,InvalidColumn\nJohn Doe,30,New York\n"));
 CSVRecord record = csvParser.nextRecord();
 // should throw exception
 record.get("InvalidColumn");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithMissingValue() {
 CSVFormat csvFormat = new CSVFormat(',').withNullString("NA");
 CSVParser csvParser = csvFormat.parse(new StringReader(CSV_CONTENT + "\nName,Age\nJohn Doe,30,New York\n"));
 CSVRecord record = csvParser.nextRecord();
 // should throw exception
 record.get("City");
 }
}