import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithValidValues() {
 CSVRecord record = new CSVRecordBuilder()
 .values(new String[]{"Alice", "25", "female"})
 .build();

 assertEquals("Alice", record.get(0));
 assertEquals("25", record.get(1));
 assertEquals("female", record.get(2));
 assertEquals(3, record.size());
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithEmptyValues() {
 CSVRecord record = new CSVRecordBuilder()
 .values(new String[0])
 .build();

 assertNull(record.get(0));
 assertEquals(0, record.size());
 assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordWithoutMapping() {
 CSVRecord record = new CSVRecord(null, null, null, 0);
 record.get("Name");
 }

 @Test
 public void testCSVRecordWithInvalidIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "female"}, null, null, 0);
 try {
 record.get(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
}

 @Test
 public void testCSVRecordWithInvalidName() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "female"}, null, null, 0);
 try {
 record.get("InvalidName");
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCSVRecordWithValidMapping() {
 CSVRecord record = new CSVRecordBuilder()
 .mapping(Map.of("Name", 0, "Age", 1, "Gender", 2))
 .values(new String[]{"Alice", "25", "female"})
 .build();

 assertEquals("Alice", record.get("Name"));
 assertEquals("25", record.get("Age"));
 assertEquals("female", record.get("Gender"));
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithInvalidMappingSize() {
 CSVRecord record = new CSVRecordBuilder()
 .mapping(Map.of("Name", 0, "Age", 1))
 .values(new String[]{"Alice", "25", "female", "Bob"})
 .build();

 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithComment() {
 CSVRecord record = new CSVRecordBuilder()
 .values(new String[]{"Alice", "25", "female"})
 .comment("This is a comment")
 .build();

 assertEquals("This is a comment", record.getComment());
 }

 @Test
 public void testCSVRecordWithRecordNumber() {
 CSVRecord record = new CSVRecordBuilder()
 .values(new String[]{"Alice", "25", "female"})
 .comment("This is a comment")
 .recordNumber(1)
 .build();

 assertEquals(1, record.getRecordNumber());
 }
}