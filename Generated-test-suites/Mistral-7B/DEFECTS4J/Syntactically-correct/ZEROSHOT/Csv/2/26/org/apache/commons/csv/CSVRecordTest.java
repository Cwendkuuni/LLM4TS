import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.Assert.*;

public class CSVRecordTest {

 private String[] testValues = new String[]{"Alice", "25", "alice@example.com"};
 private Map<String, Integer> testMapping = Map.of("Name", 0, "Age", 1, "Email", 2);
 private String testComment = "This is a test record.";
 private long testRecordNumber = 1;

 @Test
 public void testCSVRecordCreation() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 assertEquals(testValues, record.values());
 assertEquals(testMapping, record.mapping);
 assertEquals(testComment, record.getComment());
 assertEquals(testRecordNumber, record.getRecordNumber());
 }

 @Test
 public void testGetValueByIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 for (int i = 0; i < testValues.length; i++) {
 String value = record.get(i);
 assertEquals(testValues[i], value);
 }
 }

 @Test
 public void testGetValueByName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 String name = entry.getKey();
 int index = entry.getValue();
 String value = record.get(name);
 assertEquals(testValues[index], value);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueByNameWithoutMapping() {
 CSVRecord record = new CSVRecord(testValues, null, testComment, testRecordNumber);
 try {
 record.get("Name");
 fail();
 } catch (IllegalStateException e) {
 assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
 }
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 assertTrue(record.isSet("Name"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 Iterator<String> recordIterator = record.iterator();
 for (int i = 0; i < testValues.length; i++) {
 String value = recordIterator.next();
 assertEquals(testValues[i], value);
 }
 assertFalse(recordIterator.hasNext());
 }

 @Test
 public void testSize() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 assertEquals(testValues.length, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(testValues, testMapping, testComment, testRecordNumber);
 String recordString = record.toString();
 String expectedRecordString = "[Ljava.lang.String;@" + record.hashCode() + "]";
 assertTrue(recordString.startsWith(expectedRecordString));
 }
}