import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat csvFormat;
 private CSVParser csvParser;
 private String csv;

 @Before
 public void setUp() {
 csvFormat = new CSVFormat();
 csv = "name1,name2,name3\n" +
 "Alice,Smith,30\n" +
 "Bob,Doe,25\n" +
 "Eve,Eve,null";
 csvParser = new CSVParser(new StringReader(csv), csvFormat);
 }

 @After
 public void tearDown() throws IOException {
 csvParser.close();
 }

 @Test
 public void testGetByEnum() {
 CSVRecord record = csvParser.getRecord();
 assertEquals("Alice", record.get(CSVRecord.NAME1));
 assertEquals("Smith", record.get(CSVRecord.NAME2));
 assertEquals("30", record.get(CSVRecord.NAME3));
 }

 @Test
 public void testGetByInt() {
 CSVRecord record = csvParser.getRecord();
 assertEquals("Alice", record.get(0));
 assertEquals("Smith", record.get(1));
 assertEquals("30", record.get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameNullMapping() {
 CSVRecord record = csvParser.getRecord();
 record.get("NULL_NAME");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByInvalidIndex() {
 CSVRecord record = csvParser.getRecord();
 record.get(-1);
 }

 @Test
 public void testGetByInvalidName() {
 CSVRecord record = csvParser.getRecord();
 record.get("NULL_NAME");
 }

 @Test
 public void testGetByValidName() {
 CSVRecord record = csvParser.getRecord();
 assertEquals("Alice", record.get("name1"));
 assertEquals("Smith", record.get("name2"));
 assertEquals("30", record.get("name3"));
 }

 @Test
 public void testGetComment() {
 CSVRecord record = csvParser.getRecord();
 assertNull(record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = csvParser.getRecord();
 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = csvParser.getRecord();
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = csvParser.getRecord();
 assertTrue(record.isMapped("name1"));
 assertTrue(record.isMapped("name2"));
 assertTrue(record.isMapped("name3"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = csvParser.getRecord();
 assertTrue(record.isSet("name1"));
 assertTrue(record.isSet("name2"));
 assertTrue(record.isSet("name3"));
 }

 @Test
 public void testIterable() {
 CSVRecord record = csvParser.getRecord();
 List<String> values = Arrays.asList(record.values());
 assertEquals(3, values.size());
 for (int i = 0; i < 3; i++) {
 assertEquals(csv.split("\n")[1].split(",")[i], values.get(i));
 }
 }

 @Test
 public void testPutInMap() {
 CSVRecord record = csvParser.getRecord();
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals(3, map.size());
 assertEquals("Alice", map.get("name1"));
 assertEquals("Smith", map.get("name2"));
 assertEquals("30", map.get("name3"));
 }

 @Test
 public void testSize() {
 CSVRecord record = csvParser.getRecord();
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = csvParser.getRecord();
 assertEquals("[Alice, Smith, 30]", record.toString());
 }
}