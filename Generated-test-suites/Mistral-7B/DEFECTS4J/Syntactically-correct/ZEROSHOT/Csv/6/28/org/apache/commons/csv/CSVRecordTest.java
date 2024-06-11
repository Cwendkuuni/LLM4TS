import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat format;
 private CSVParser parser;
 private String csvContent = "name1,age1,comment1\nname2,age2,comment2\nname3,age3";

 @Before
 public void setUp() throws Exception {
 format = new CSVFormat();
 parser = new CSVParser(new StringReader(csvContent), format);
 }

 @Test
 public void testNewInstance() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertEquals(1, record.size());
 assertNull(record.getComment());
 assertNull(record.getRecordNumber());
 assertNull(record.get("name1"));
 assertNull(record.get("name2"));
 assertEquals("", record.get(0));
 assertEquals("name1", record.getMapping().get("name1"));
 assertEquals(0, record.getMapping().get("name1").intValue());
 }

 @Test
 public void testGetValueByEnum() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertEquals("name1", record.get(CSVHeader.NAME.getText()));
 assertEquals("age1", record.get(CSVHeader.AGE.getText()));
 }

 @Test
 public void testGetValueByName() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertEquals("name1", record.get("name1"));
 assertEquals("age1", record.get("age1"));
 assertNull(record.get("nonExistingColumn"));
 }

 @Test
 public void testGetValueByIndex() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertEquals("name1", record.get(0));
 assertEquals("age1", record.get(1));
 }

 @Test
 public void testGetComment() throws Exception {
 CSVRecord record = parser.nextRecord();
 record.setComment("comment1");
 assertEquals("comment1", record.getComment());
 }

 @Test
 public void testGetRecordNumber() throws Exception {
 CSVRecord record = parser.nextRecord();
 record.setRecordNumber(1);
 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsMappedWithNoMapping() throws Exception {
 CSVRecord record = parser.nextRecord();
 record.isMapped("name1");
 }

 @Test
 public void testIsSet() throws Exception {
 CSVRecord record = parser.nextRecord();
 record.set("name1", "name3");
 record.set("age1", "25");
 assertTrue(record.isSet("name1"));
 assertTrue(record.isSet("age1"));
 }

 @Test
 public void testIterator() throws Exception {
 CSVRecord record = parser.nextRecord();
 List<String> list = record.iterator();
 assertEquals(2, list.size());
 for (int i = 0; i < list.size(); i++) {
 assertEquals(record.get(i), list.get(i));
 }
 }

 @Test
 public void testPutIn() throws Exception {
 CSVRecord record = parser.nextRecord();
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals(2, map.size());
 assertEquals("name1", map.get("name1"));
 assertEquals("age1", map.get("age1"));
 }

 @Test
 public void testSize() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertEquals(2, record.size());
 }

 @Test
 public void testToString() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertEquals("[name1, age1]", record.toString());
 }

 @Test
 public void testValues() throws Exception {
 CSVRecord record = parser.nextRecord();
 String[] values = record.values();
 assertEquals(2, values.length);
 assertEquals("name1", values[0]);
 assertEquals("age1", values[1]);
 }
}