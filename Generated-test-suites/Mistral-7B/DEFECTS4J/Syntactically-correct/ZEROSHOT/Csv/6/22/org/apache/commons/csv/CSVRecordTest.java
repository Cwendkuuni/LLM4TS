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

 private CSVFormat csvFormat;

 @Before
 public void setUp() {
 csvFormat = new CSVFormat();
 }

 @Test
 public void testNewCSVRecord() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("lastName", 1);
 mapping.put("age", 2);
 mapping.put("city", 3);
 CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);

 assertEquals("Alice", csvRecord.get(0));
 assertEquals("Smith", csvRecord.get(1));
 assertEquals("age:25", csvRecord.get(2).startsWith("age:"));
 assertEquals("New York", csvRecord.get(3).startsWith("city:"));
 assertEquals(null, csvRecord.getComment());
 assertEquals(0, csvRecord.getRecordNumber());
 assertTrue(csvRecord.isConsistent());
 assertEquals(4, csvRecord.size());
 assertTrue(csvRecord.isMapped("name"));
 assertTrue(csvRecord.isMapped("lastName"));
 assertTrue(csvRecord.isMapped("age"));
 assertTrue(csvRecord.isMapped("city"));
 List<String> list = csvRecord.toList();
 assertEquals(Arrays.asList("Alice", "Smith", "age:25", "city:New York"), list);
 Map<String, String> map = csvRecord.toMap();
 assertEquals(Arrays.asList("name=Alice", "lastName=Smith", "age=age:25", "city=city:New York"), map.values());
 assertSame(map, csvRecord.putIn(map));
 assertSame(csvRecord, map.keySet().iterator().next());
 assertEquals(csvRecord.toString(), "[Alice, Smith, age:25, city:New York]");
 }

 @Test
 public void testNewCSVRecordWithEmptyValues() {
 String[] values = {};
 Map<String, Integer> mapping = new HashMap<>();
 CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);

 assertTrue(csvRecord.isConsistent());
 assertEquals(0, csvRecord.size());
 assertFalse(csvRecord.isMapped("name"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewCSVRecordWithInvalidMapping() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 5);
 CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewCSVRecordWithInvalidMappingIndex() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 4);
 CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueWithNoMapping() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 CSVRecord csvRecord = new CSVRecord(values, null, null, 0);
 csvRecord.get("name");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueWithNoMappingFromEmptyCSVRecord() {
 String[] values = {};
 CSVRecord csvRecord = new CSVRecord(values, null, null, 0);
 csvRecord.get("name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithInvalidName() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 2);
 CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
 csvRecord.get("invalidName");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 CSVRecord csvRecord = new CSVRecord(values, null, null, 0);
 csvRecord.get(4);
 }

 @Test
 public void testGetValueWithNullComment() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("lastName", 1);
 mapping.put("age", 2);
 mapping.put("city", 3);
 CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
 assertNull(csvRecord.getComment());
 }

 @Test
 public void testGetValueWithComment() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("lastName", 1);
 mapping.put("age", 2);
 mapping.put("city", 3);
 CSVRecord csvRecord = new CSVRecord(values, mapping, "comment", 0);
 assertEquals("comment", csvRecord.getComment());
 }

 @Test
 public void testGetValueWithRecordNumber() {
 String[] values = {"Alice", "Smith", "age:25", "city:New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("lastName", 1);
 mapping.put("age", 2);
 mapping.put("city", 3);
 CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
 assertEquals(1, csvRecord.getRecordNumber());
 }

 @Test
 public void testParseCSVRecord() throws Exception {
 String csvContent = "name,lastName,age:25,city:New York\nAlice,Smith,age:25,city:New York";
 CSVParser csvParser = new CSVParser(new StringReader(csvContent), csvFormat);
 List<CSVRecord> csvRecords = csvParser.getRecords();
 CSVRecord csvRecord = csvRecords.get(0);

 assertEquals("Alice", csvRecord.get(0));
 assertEquals("Smith", csvRecord.get(1));
 assertEquals("age:25", csvRecord.get(2).startsWith("age:"));
 assertEquals("New York", csvRecord.get(3).startsWith("city:"));
 assertEquals(null, csvRecord.getComment());
 assertEquals(0, csvRecord.getRecordNumber());
 assertTrue(csvRecord.isConsistent());
 assertEquals(4, csvRecord.size());
 assertTrue(csvRecord.isMapped("name"));
 assertTrue(csvRecord.isMapped("lastName"));
 assertTrue(csvRecord.isMapped("age"));
 assertTrue(csvRecord.isMapped("city"));
 List<String> list = csvRecord.toList();
 assertEquals(Arrays.asList("Alice", "Smith", "age:25", "city:New York"), list);
 Map<String, String> map = csvRecord.toMap();
 assertEquals(Arrays.asList("name=Alice", "lastName=Smith", "age=age:25", "city=city:New York"), map.values());
 assertSame(map, csvRecord.putIn(map));
 assertSame(csvRecord, map.keySet().iterator().next());
 assertEquals(csvRecord.toString(), "[Alice, Smith, age:25, city:New York]");
 }

}