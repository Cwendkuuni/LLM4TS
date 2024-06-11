import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithValidValues() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = null;
 String comment = null;
 long recordNumber = 1;
 CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

 // Test get method with valid index
 String expectedValue = "Name";
 String actualValue = csvRecord.get(0);
 assertEquals(expectedValue, actualValue);

 // Test get method with invalid index
 int index = values.length + 1;
 String actualValue1 = csvRecord.get(index);
 assertNull(actualValue1);

 // Test get method with valid name
 String name = "Name";
 String expectedNameValue = "Name";
 String actualNameValue = csvRecord.get(name);
 assertEquals(expectedNameValue, actualNameValue);

 // Test get method with invalid name
 String invalidName = "InvalidName";
 String actualValue2 = csvRecord.get(invalidName);
 assertNull(actualValue2);

 // Test isConsistent method
 assertTrue(csvRecord.isConsistent());

 // Test isMapped method with valid name
 assertTrue(csvRecord.isMapped("Name"));

 // Test isSet method with valid name
 assertTrue(csvRecord.isSet("Name"));

 // Test iterator method
 Iterator<String> iterator = csvRecord.iterator();
 String[] expectedValues = values;
 int i = 0;
 while (iterator.hasNext()) {
 String actualValue3 = iterator.next();
 assertEquals(expectedValues[i], actualValue3);
 i++;
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithNoMapping() {
 String[] values = new String[]{"Name", "Age", "City"};
 String comment = null;
 long recordNumber = 1;
 CSVRecord csvRecord = new CSVRecord(values, null, comment, recordNumber);
 String name = "Name";
 String expectedExceptionMessage = "No header mapping was specified, the record values can't be accessed by name";
 String actualExceptionMessage = null;
 try {
 csvRecord.get(name);
 } catch (IllegalStateException e) {
 actualExceptionMessage = e.getMessage();
 }
 assertEquals(expectedExceptionMessage, actualExceptionMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInvalidMapping() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new java.util.HashMap<>();
 mapping.put("Name", 3);
 String comment = null;
 long recordNumber = 1;
 CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
 String name = "Name";
 String expectedExceptionMessage = "The mapping for the given name is out of range";
 String actualExceptionMessage = null;
 try {
 csvRecord.get(name);
 } catch (IllegalArgumentException e) {
 actualExceptionMessage = e.getMessage();
 }
 assertEquals(expectedExceptionMessage, actualExceptionMessage);
 }
}