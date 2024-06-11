import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String CSV_RECORD = "name1,name2,name3\n" +
         "value1_1,value1_2,value1_3\n" +
         "value2_1,value2_2,value2_3";

 @Test
 public void testCSVRecord_ValidConstruction() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 assertNotNull(csvRecord);
 assertEquals(3, csvRecord.size());
 assertNull(csvRecord.getComment());

 String[] expectedValues = new String[]{"value1_1", "value1_2", "value1_3"};
 assertArrayEquals(expectedValues, csvRecord.values());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecord_NullMapping() {
 CSVFormat format = new CSVFormat();
 CSVRecord csvRecord = new CSVRecord(null, null, null, 0);
 }

 @Test
 public void testCSVRecord_GetValueByEnum() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 Enum testEnum = Enum.valueOf("testEnum", "Test Value");
 String value = csvRecord.get(testEnum);
 assertEquals("Test Value", value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_GetValueByInvalidIndex() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 int invalidIndex = 4;
 String value = csvRecord.get(invalidIndex);
 fail("Expected IllegalArgumentException but got " + value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_GetValueByNameMissingColumn() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 String missingColumn = "missingColumn";
 String value = csvRecord.get(missingColumn);
 fail("Expected IllegalArgumentException but got " + value);
 }

 @Test
 public void testCSVRecord_GetValueByName() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 String name1 = "name1";
 String value1 = csvRecord.get(name1);
 assertEquals("value1_1", value1);
 }

 @Test
 public void testCSVRecord_GetComment() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD + "\nname1,name2,name3\n" + // Header
         "value1_1,value1_2,value1_3# This is a comment for record 1");
 CSVRecord csvRecord = records.get(0);

 String comment = csvRecord.getComment();
 assertEquals("This is a comment for record 1", comment);
 }

 @Test
 public void testCSVRecord_GetRecordNumber() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 long recordNumber = csvRecord.getRecordNumber();
 assertEquals(0, recordNumber);
 }

 @Test
 public void testCSVRecord_IsConsistent() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 assertTrue(csvRecord.isConsistent());
 }

 @Test
 public void testCSVRecord_IsMapped() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD + "\nname1,name2,name3\n" + // Header
         "value1_1,value1_2,value1_3");
 CSVRecord csvRecord = records.get(0);

 assertTrue(csvRecord.isMapped("name1"));
 assertTrue(csvRecord.isMapped("name2"));
 assertTrue(csvRecord.isMapped("name3"));
 }

 @Test
 public void testCSVRecord_IsSet() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD + "\nname1,name2,name3\n" + // Header
         "value1_1,value1_2,value1_3");
 CSVRecord csvRecord = records.get(0);

 assertTrue(csvRecord.isSet("name1"));
 assertTrue(csvRecord.isSet("name2"));
 assertTrue(csvRecord.isSet("name3"));
 }

 @Test
 public void testCSVRecord_Iterator() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 Iterator<String> iterator = csvRecord.iterator();
 List<String> expectedValues = Arrays.asList("value1_1", "value1_2", "value1_3");

 while (iterator.hasNext()) {
 String value = iterator.next();
 assertTrue(expectedValues.contains(value));
 expectedValues.remove(value);
 }

 assertTrue(expectedValues.isEmpty());
 }

 @Test
 public void testCSVRecord_PutIn() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 Map<String, String> map = new HashMap<>();
 map = csvRecord.putIn(map);

 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("name1", "value1_1");
 expectedMap.put("name2", "value1_2");
 expectedMap.put("name3", "value1_3");

 assertEquals(expectedMap, map);
 }

 @Test
 public void testCSVRecord_Size() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 assertEquals(3, csvRecord.size());
 }

 @Test
 public void testCSVRecord_ToList() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 List<String> expectedList = Arrays.asList("value1_1", "value1_2", "value1_3");
 List<String> list = csvRecord.toList();

 assertEquals(expectedList, list);
 }

 @Test
 public void testCSVRecord_ToMap() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("name1", "value1_1");
 expectedMap.put("name2", "value1_2");
 expectedMap.put("name3", "value1_3");

 Map<String, String> map = csvRecord.toMap();

 assertEquals(expectedMap, map);
 }

 @Test
 public void testCSVRecord_ToString() {
 CSVFormat format = new CSVFormat();
 List<CSVRecord> records = format.parse(CSV_RECORD);
 CSVRecord csvRecord = records.get(0);

 String expectedToString = "[value1_1, value1_2, value1_3]";
 String toString = csvRecord.toString();

 assertEquals(expectedToString, toString);
 }
}