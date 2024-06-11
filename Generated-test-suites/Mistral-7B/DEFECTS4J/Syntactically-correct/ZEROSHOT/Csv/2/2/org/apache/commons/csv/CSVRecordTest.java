import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGet() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 assertEquals("John Doe", testRecord.get(0));
 assertEquals("Smith", testRecord.get(1));
 assertEquals("john.doe@example.com", testRecord.get(2));
 assertEquals("456-1234-5678", testRecord.get(3));
 assertEquals("New York", testRecord.get(4));
 }

 @Test
 public void testCSVRecordGetByName() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 assertEquals("John Doe", testRecord.get("Name"));
 assertEquals("Smith", testRecord.get("Surname"));
 assertEquals("john.doe@example.com", testRecord.get("Email"));
 assertEquals("456-1234-5678", testRecord.get("Phone"));
 assertEquals("New York", testRecord.get("City"));
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 // Test valid record
 assertTrue(testRecord.isConsistent());

 // Test record with different number of columns
 CSVRecord invalidRecord = record.getRecord(1);
 assertFalse(invalidRecord.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 // Test valid record
 assertTrue(testRecord.isMapped("Name"));
 assertTrue(testRecord.isMapped("Surname"));
 assertTrue(testRecord.isMapped("Email"));
 assertTrue(testRecord.isMapped("Phone"));
 assertTrue(testRecord.isMapped("City"));

 // Test record with no header mapping
 CSVRecord noMappingRecord = new CSVRecord(null, null, null, 0);
 assertFalse(noMappingRecord.isMapped("Name"));
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 // Test valid record
 assertTrue(testRecord.isSet("Name"));
 assertTrue(testRecord.isSet("Surname"));
 assertTrue(testRecord.isSet("Email"));
 assertTrue(testRecord.isSet("Phone"));
 assertTrue(testRecord.isSet("City"));

 // Test record with missing value
 CSVRecord missingValueRecord = record.getRecord(1);
 assertFalse(missingValueRecord.isSet("Phone"));
 }

 @Test
 public void testCSVRecordIterator() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 Iterator<String> iterator = testRecord.iterator();

 assertEquals("John Doe", iterator.next());
 assertEquals("Smith", iterator.next());
 assertEquals("john.doe@example.com", iterator.next());
 assertEquals("456-1234-5678", iterator.next());
 assertEquals("New York", iterator.next());
 }

 @Test
 public void testCSVRecordSize() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 assertEquals(5, testRecord.size());
 }

 @Test
 public void testCSVRecordToString() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 assertEquals("[John Doe, Smith, john.doe@example.com, 456-1234-5678, New York]", testRecord.toString());
 }

 @Test
 public void testCSVRecordCommentAndRecordNumber() {
 CSVRecord record = new CSVParser(getClass().getResourceAsStream("/test.csv"))
 .parse();
 CSVRecord testRecord = record.getRecord(0);

 assertEquals("", testRecord.getComment());
 assertEquals(0, testRecord.getRecordNumber());
 }
}