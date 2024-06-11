import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGet() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};

 assertEquals("John Doe", record.get(0));
 assertEquals("Age", record.get(1).getClass().getName());
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCSVRecordGetNegativeIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};

 record.get(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetNullName() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};

 record.get(0);
 record.get(null);
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};

 assertTrue(record.isConsistent());

 record.mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record.values = new String[]{"John Doe", "30", "New York", "Another City"};
 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record.values = new String[]{"John Doe", "30", "New York"};

 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 assertFalse(record.isMapped("Other"));
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record.values = new String[]{"John Doe", "30", "New York"};

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 assertFalse(record.isSet("Other"));
 }

 @Test
 public void testCSVRecordIterator() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};

 Iterator<String> iterator = record.iterator();
 assertEquals("John Doe", iterator.next());
 assertEquals("30", iterator.next());
 assertEquals("New York", iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testCSVRecordSize() {
 CSVRecord record = new CSVRecord(null, null, null, 0L);
 record.values = new String[0];

 assertEquals(0, record.size());

 record.values = new String[]{"John Doe"};
 assertEquals(1, record.size());

 record.values = new String[]{"John Doe", "30"};
 assertEquals(2, record.size());
 }

 @Test
 public void testCSVRecordToString() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};

 assertEquals("[\"John Doe\", \"30\", \"New York\"]", record.toString());
 }

 @Test
 public void testCSVRecordGetComment() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, "Comment", 0L);
 record.values = new String[]{"John Doe", "30", "New York"};

 assertEquals("Comment", record.getComment());
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1L);
 record.values = new String[]{"John Doe", "30", "New York"};

 assertEquals(1, record.getRecordNumber());
 }
}