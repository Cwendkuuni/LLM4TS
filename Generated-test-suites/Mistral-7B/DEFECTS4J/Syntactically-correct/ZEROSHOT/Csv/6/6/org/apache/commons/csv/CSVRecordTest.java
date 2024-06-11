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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat format;
 private CSVParser parser;
 private CSVRecord record;

 @Before
 public void setUp() throws Exception {
 format = CSVFormat.DEFAULT.withHeader("col1", "col2");
 parser = new CSVParser(new StringReader("col1,col2,col1,col2\n" +
 "header1,value1,header2,value2\n" +
 "record1,value3,record2,value4"));
 record = parser.nextRecord();
 }

 @Test
 public void testGet() {
 assertEquals("header1", record.get("col1"));
 assertEquals("value1", record.get(0));
 assertEquals("value3", record.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidColumnName() {
 record.get("invalidColumnName");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNoHeaderMapping() {
 CSVRecord noHeaderRecord = new CSVRecord(null, null, null, 0);
 noHeaderRecord.get("col1");
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

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("col1"));
 assertFalse(record.isMapped("nonExistentColumn"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("col1"));
 assertFalse(record.isSet("nonExistentColumn"));
 }

 @Test
 public void testIterator() {
 List<String> values = record.toList();
 Iterator<String> iter = record.iterator();
 int i = 0;
 while (iter.hasNext()) {
 assertEquals(values.get(i), iter.next());
 i++;
 }
 }

 @Test
 public void testPutIn() {
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals("value1", map.get("col1"));
 assertEquals("value3", map.get("col2"));
 }

 @Test
 public void testSize() {
 assertEquals(4, record.size());
 }

 @Test
 public void testToList() {
 List<String> values = record.toList();
 assertEquals(Arrays.asList("header1", "value1", "header2", "value2"), values);
 }

 @Test
 public void testToMap() {
 Map<String, String> map = record.toMap();
 assertEquals("value1", map.get("col1"));
 assertEquals("value3", map.get("col2"));
 }

 @Test
 public void testToString() {
 assertEquals("[header1, value1, header2, value2]", record.toString());
 }

 @Test
 public void testValues() {
 String[] values = record.values();
 assertEquals(new String[]{"header1", "value1", "header2", "value2"}, values);
 }

}