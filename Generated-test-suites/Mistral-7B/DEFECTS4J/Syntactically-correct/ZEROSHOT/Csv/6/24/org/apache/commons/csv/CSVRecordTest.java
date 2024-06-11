import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

 private String csv;
 private CSVFormat format;
 private CSVParser parser;
 private CSVRecord record;

 @Before
 public void setUp() {
 csv = "name1,age1,comment1\n" +
 "John Doe,30,Comments for John Doe\n" +
 "Jane Doe,25,\n" +
 "Mike Doe,45,Comments for Mike Doe";

 format = new CSVFormat();
 try {
 parser = format.parse(new StringReader(csv));
 } catch (IOException e) {
 e.printStackTrace();
 }

 record = null;
 try {
 record = parser.nextRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetIntValue() {
 assertNotNull(record);
 assertEquals("John Doe", record.get(0));
 assertEquals("30", record.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNullMapping() {
 record = new CSVRecord(null, null, null, 0);
 try {
 record.get("name");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetWrongMapping() {
 record = new CSVRecord(new String[]{"name1", "age1", ""}, null, null, 0);
 try {
 record.get("comment");
 } catch (IllegalArgumentException e) {
 assertEquals("Mapping for comment not found, expected one of [name1, age1]", e.getMessage());
 }
 }

 @Test
 public void testGetConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInconsistent() {
 String[] values = new String[]{"name1", "age1", "comment1", "value4"};
 record = new CSVRecord(values, null, null, 0);
 assertFalse(record.isConsistent());
 try {
 record.get("comment");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetComment() {
 assertEquals("Comments for John Doe", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 assertEquals(1, record.getRecordNumber());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetRecordNumberNegative() {
 record = new CSVRecord(new String[0], null, null, -1);
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("name1"));
 assertFalse(record.isMapped("name2"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("name1"));
 assertFalse(record.isSet("name3"));
 }

 @Test
 public void testSize() {
 assertEquals(3, record.size());
 }

 @Test
 public void testIterator() {
 List<String> list = record.toList();
 Iterator<String> iterator = list.iterator();
 assertEquals("name1", iterator.next());
 assertEquals("age1", iterator.next());
 assertEquals("Comments for John Doe", iterator.next());
 }

 @Test
 public void testPutIn() {
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals("John Doe", map.get("name1"));
 assertEquals("30", map.get("age1"));
 assertEquals("Comments for John Doe", map.get("comment1"));
 }

 @Test
 public void testToString() {
 assertNotNull(record.toString());
 String expected = "[Ljava.lang.String;@68971522";
 assertNotEquals(expected, record.toString());
 }

 @Test
 public void testValues() {
 assertEquals(new String[]{"name1", "age1", "Comments for John Doe"}, record.values());
 }

}