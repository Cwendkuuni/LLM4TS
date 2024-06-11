import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private String[] sampleValues = new String[]{"John", "Doe", "jdoe@example.com", "45"};
 private Map<String, Integer> sampleMapping = Map.of("Name", 0, "Email", 1, "Age", 2);
 private CSVRecord sampleRecord = new CSVRecord(sampleValues, sampleMapping, null, 1);

 @Test
 public void testGet() {
 String expected = "John";
 String actual = sampleRecord.get(0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetByName() {
 String expected = "John";
 String actual = sampleRecord.get("Name");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByName_NoMapping() {
 String actual = sampleRecord.get("UnknownColumn");
 fail("No Exception Thrown");
 }

 @Test
 public void testIsConsistent() {
 assertTrue(sampleRecord.isConsistent());
 }

 @Test
 public void testIsMapped() {
 assertTrue(sampleRecord.isMapped("Name"));
 }

 @Test
 public void testIsSet() {
 assertTrue(sampleRecord.isSet("Name"));
 }

 @Test
 public void testIterator() {
 String[] expected = sampleValues;
 Iterator<String> actual = sampleRecord.iterator();
 assertEquals(expected.length, actual.nextCount());
 for (int i = 0; i < expected.length; i++) {
 String e = expected[i];
 String a = actual.next();
 assertEquals(e, a);
 }
 }

 @Test
 public void testSize() {
 int expected = sampleValues.length;
 int actual = sampleRecord.size();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 String expected = "[Name=John, Email=jdoe@example.com, Age=45]";
 String actual = sampleRecord.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRecordNumber() {
 long expected = 1;
 long actual = sampleRecord.getRecordNumber();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetComment() {
 String expected = null;
 String actual = sampleRecord.getComment();
 assertEquals(expected, actual);
 }

}