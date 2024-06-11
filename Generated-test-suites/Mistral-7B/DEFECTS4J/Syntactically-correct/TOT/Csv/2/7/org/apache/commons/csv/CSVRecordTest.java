package org.apache.commons.csv;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testGetMethod() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 // Test getting value by index
 assertEquals("John", record.get(0));
 // Test getting value by name (assuming the header is "Name")
 assertEquals("John", record.get("Name"));
 // Test getting value by name with invalid name
 assertNull(record.get("Age"));
 // Test getting value by name with invalid index
 assertNull(record.get(3));
 // Test getting value by index with invalid index
 assertNull(record.get(3));
 }

 @Test
 public void testIsConsistentMethod() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 // Test record with consistent mapping
 assertTrue(record.isConsistent());
 CSVRecord recordWithInvalidMapping = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 recordWithInvalidMapping.mapping = Map.of("Name", 2, "Age", 1);
 // Test record with inconsistent mapping
 assertFalse(recordWithInvalidMapping.isConsistent());
 }

 @Test
 public void testIsMappedMethod() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John,Age:30"}, Map.of("Name", 0, "Age", 1), null, 1);
 // Test column with valid name and mapping
 assertTrue(record.isMapped("Name"));
 // Test column with invalid name
 assertFalse(record.isMapped("Age2"));
 }

 @Test
 public void testIsSetMethod() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John,Age:30"}, Map.of("Name", 0, "Age", 1), null, 1);
 // Test column with valid name and value
 assertTrue(record.isSet("Name"));
 // Test column with invalid name
 assertFalse(record.isSet("Age2"));
 // Test column with valid name but no value
 CSVRecord recordWithEmptyValue = new CSVRecord(new String[]{"Name:John,Age:", 1}, Map.of("Name", 0, "Age", 1), null, 1);
 assertFalse(recordWithEmptyValue.isSet("Name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithNoMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John,Age:30"}, null, null, 1);
 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John,Age:30"}, null, null, 1);
 record.get(3);
 }

 @Test
 public void testSizeMethod() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John,Age:30"}, null, null, 1);
 assertEquals(2, record.size());
 }

 @Test
 public void testToStringMethod() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John,Age:30"}, null, null, 1);
 assertEquals("[Name:John, Age:30]", record.toString());
 }
}