import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CSVRecordTest {

 private CSVRecord record;
 private CSVFormat format;
 private String csv;

 @Parameterized.Parameters(name = "{index}: CSVRecord test with CSV = {0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // valid CSV record
 new Object[]{"\"name1\",\"name2\",\"name3\"", "name1,name2,name3"},
 // record with comment
 new Object[]{"\"name1\",\"name2\",\"name3\"#comment", "name1,name2,name3", "comment"},
 // record with empty values
 new Object[]{"\"\",\"\",\"\"", null},
 // record with null values
 new Object[]{"\"name1\",\"name2\",null", "name1,name2", null},
 // record with empty map
 new Object[]{"\"name1\",\"name2\",\"name3\"", null, null, new String[0]},
 // record with map
 new Object[]{"\"name1:value1\",\"name2:value2\",\"name3:value3\"", null, null, new String[]{"name1:value1", "name2:value2", "name3:value3"}}
 });
 }

 @Before
 public void setup() throws Exception {
 format = CSVFormat.DEFAULT;
 csv = (String) params[0];
 record = CSVParser.parse(new StringReader(csv), format).getRecord(0);
 }

 @Test
 public void testGet() {
 // get by index
 assertEquals("name1", record.get(0));
 assertEquals("name2", record.get(1));
 assertEquals("name3", record.get(2));
 // get by name
 assertEquals("name1", record.get("name1"));
 assertEquals("name2", record.get("name2"));
 assertEquals("name3", record.get("name3"));
 // get by enum
 assertEquals("name1", record.get(CSVRecordTest.Name1.class));
 // get by null index
 assertNull(record.get(-1));
 // get unassigned column
 assertNull(record.get(3));
 }

 @Test
 public void testGetComment() {
 // record with comment
 assertEquals("comment", record.getComment());
 // record without comment
 assertNull(record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 // record with record number
 assertEquals(1L, record.getRecordNumber());
 // record without record number
 assertNull(record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 // record with mapping
 assertTrue(record.isConsistent());
 // record without mapping
 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 // record with mapping
 assertTrue(record.isMapped("name1"));
 assertTrue(record.isMapped("name2"));
 assertTrue(record.isMapped("name3"));
 // record without mapping
 assertFalse(record.isMapped("unknown"));
 }

 @Test
 public void testIsSet() {
 // record with mapping and values
 assertTrue(record.isSet("name1"));
 assertTrue(record.isSet("name2"));
 assertTrue(record.isSet("name3"));
 // record with mapping but no values
 assertFalse(record.isSet("unknown"));
 }

 @Test
 public void testGetIterable() {
 List<String> list = record.iterator().toList();
 assertEquals(Arrays.asList("name1", "name2", "name3"), list);
 }

 @Test
 public void testPutIn() {
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals(Arrays.asList("name1", "name2", "name3"), map.values());
 }

 @Test
 public void testSize() {
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 assertEquals("[name1, name2, name3]", record.toString());
 }

 @Test
 public void testValues() {
 String[] values = record.values();
 assertEquals(new String[]{"name1", "name2", "name3"}, values);
 }

 static class Name1 {
 }
}