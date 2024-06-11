import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

 @Test
 public void testCSVRecordEmpty() {
 CSVRecord record = new CSVRecord(null, null, null, 0L);
 Assert.assertNull(record.comment);
 Assert.assertNotNull(record.mapping);
 Assert.assertNull(record.mapping.size());
 Assert.assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values);
 Assert.assertEquals(0, record.size());
 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordBasic() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 Assert.assertEquals("Alice", record.get(0));
 Assert.assertEquals("25", record.get(1));
 Assert.assertEquals("female", record.get(2));
 Assert.assertEquals(null, record.getComment());
 Assert.assertEquals(3, record.size());
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordMissingValue() {
 String[] values = {"Alice", "25"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 try {
 record.get(2);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCSVRecordGetByEnum() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 EnumGender gender = EnumGender.FEMALE;
 Assert.assertEquals("female", record.get(gender));
 }

 @Test
 public void testCSVRecordGetByNullName() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 try {
 record.get(null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCSVRecordGetByInvalidName() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 try {
 record.get("InvalidName");
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCSVRecordGetByMissingName() {
 String[] values = {"Alice", "25"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 try {
 record.get("Gender");
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCSVRecordGetComment() {
 String comment = "This is a test record";
 String[] values = {"Alice", "25"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 CSVRecord record = new CSVRecord(values, mapping, comment, 0L);

 Assert.assertEquals(comment, record.getComment());
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 long recordNumber = 123L;
 String[] values = {"Alice", "25"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

 Assert.assertEquals(recordNumber, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordIsConsistent() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsNotConsistent() {
 String[] values = {"Alice", "25", "Alice"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 Assert.assertTrue(record.isMapped("Name"));
 Assert.assertTrue(record.isMapped("Age"));
 Assert.assertTrue(record.isMapped("Gender"));
 Assert.assertFalse(record.isMapped("InvalidName"));
 }

 @Test
 public void testCSVRecordIsSet() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 Assert.assertTrue(record.isSet("Name"));
 Assert.assertTrue(record.isSet("Age"));
 Assert.assertFalse(record.isSet("InvalidName"));
 }

 @Test
 public void testCSVRecordIterator() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);
 List<String> expectedValues = Arrays.asList("Alice", "25", "female");

 Iterator<String> iterator = record.iterator();
 List<String> actualValues = new ArrayList<>();
 while (iterator.hasNext()) {
 actualValues.add(iterator.next());
 }

 Assert.assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testCSVRecordPutInMap() {
 String[] values = {"Alice", "25", "female"};
 Map<String, String> map = new HashMap<>();
 CSVRecord record = new CSVRecord(values, null, null, 0L);
 map = record.putIn(map);
 List<String> expectedValues = Arrays.asList("Alice", "25", "female");
 List<String> actualValues = new ArrayList<>();
 for (String value : map.values()) {
 actualValues.add(value);
 }

 Assert.assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testCSVRecordToMap() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("Name", "Alice");
 expectedMap.put("Age", "25");
 expectedMap.put("Gender", "female");

 Map<String, String> actualMap = record.toMap();
 List<String> expectedValues = new ArrayList<>(expectedMap.values());
 List<String> actualValues = new ArrayList<>();
 for (String value : actualMap.values()) {
 actualValues.add(value);
 }

 Assert.assertEquals(expectedMap, actualMap);
 Assert.assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testCSVRecordToString() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);

 String expectedString = "[Alice, 25, female]";
 String actualString = record.toString();

 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testCSVRecordValues() {
 String[] values = {"Alice", "25", "female"};
 CSVRecord record = new CSVRecord(values, null, null, 0L);
 String[] expectedValues = {"Alice", "25", "female"};
 String[] actualValues = record.values();

 Assert.assertArrayEquals(expectedValues, actualValues);
 }

 @Test
 public void testCSVParserCSVRecord() throws Exception {
 String csvContent = "Name,Age,Gender\nAlice,25,female\nBob,35,male";
 CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
 CSVParser parser = new CSVParser(new StringReader(csvContent), csvFormat);
 List<CSVRecord> records = new ArrayList<>();
 while (parser.hasNext()) {
 records.add(parser.nextRecord());
 }

 CSVRecord firstRecord = records.get(0);
 String[] expectedValues = {"Alice", "25", "female"};
 String[] actualValues = firstRecord.values();

 Assert.assertArrayEquals(expectedValues, actualValues);
 }

 private enum EnumGender {
 FEMALE,
 MALE
 }
}