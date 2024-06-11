import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat format;
 private CSVParser parser;
 private CSVRecord record;

 @Before
 public void setUp() {
 format = new CSVFormatBuilder()
 .setHeaderNames(Arrays.asList("name1", "name2", "name3"))
 .build();
 parser = new CSVParserBuilder(new StringReader("name1,name2,name3\n" +
 "Alice,25,female\n" +
 "Bob,30,male\n"))
 .setFormat(format)
 .build();
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGet() {
 // get by index
 assertEquals("Alice", record.get(0));
 assertEquals("25", record.get(1));
 assertEquals("female", record.get(2));

 // get by name
 assertEquals("Alice", record.get("name1"));
 assertEquals("25", record.get("age")); // should throw an exception
 assertEquals("female", record.get("gender"));
 }

 @Test
 public void testGetComment() {
 assertNull(record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetUnmappedColumn() {
 record = new CSVRecord(new String[0], null, null, 0);
 assertNull(record.get("test"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 record = new CSVRecord(new String[]{"a", "b"}, null, null, 0);
 record.get(2);
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("name1"));
 assertTrue(record.isMapped("name2"));
 assertTrue(record.isMapped("name3"));
 assertFalse(record.isMapped("test"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("name1"));
 assertTrue(record.isSet("name2"));
 assertTrue(record.isSet("name3"));
 assertFalse(record.isSet("test"));
 }

 @Test
 public void testIterator() {
 List<String> list = record.toList();
 Iterator<String> iterator = record.iterator();
 assertEquals(3, list.size());
 for (int i = 0; i < 3; i++) {
 assertEquals(list.get(i), iterator.next());
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testPutIn() {
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 Map<String, String> expected = new HashMap<>();
 expected.put("name1", "Alice");
 expected.put("name2", "25");
 expected.put("name3", "female");
 assertEquals(expected, map);
 }

 @Test
 public void testSize() {
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 String expected = "[name1=Alice, name2=25, name3=female]";
 assertEquals(expected, record.toString());
 }

 @Test
 public void testValues() {
 String[] values = record.values();
 String[] expected = new String[]{"Alice", "25", "female"};
 assertSame(expected, values);
 }
}