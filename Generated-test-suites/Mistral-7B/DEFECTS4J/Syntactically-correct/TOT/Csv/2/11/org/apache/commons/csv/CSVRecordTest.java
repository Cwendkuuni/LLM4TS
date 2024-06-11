package org.apache.commons.csv.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

class CSVRecordTest {

 /**
 * Test constructor with valid arguments.
 */
 @org.junit.Test
 public void testConstructorWithValidArguments() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertEquals(1, record.getRecordNumber());
 assertEquals(3, record.size());
 assertArrayEquals(values, record.values());
 }

 /**
 * Test constructor with null values.
 */
 @org.junit.Test
 public void testConstructorWithNullValues() {
 CSVRecord record = new CSVRecord(null, new HashMap<>(), null, 1);
 assertEquals(1, record.getRecordNumber());
 assertEquals(0, record.size());
 assertNull(record.values());
 }

 /**
 * Test constructor with empty mapping.
 */
 @org.junit.Test
 public void testConstructorWithEmptyMapping() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 CSVRecord record = new CSVRecord(values, null, null, 1);
 assertEquals(1, record.getRecordNumber());
 assertEquals(3, record.size());
 assertArrayEquals(values, record.values());
 }

 /**
 * Test get method with valid index.
 */
 @org.junit.Test
 public void testGetWithValidIndex() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertEquals("John Doe", record.get(0));
 assertEquals("35", record.get(1));
 assertEquals("john.doe@example.com", record.get(2));
 }

 /**
 * Test get method with invalid index.
 */
 @org.junit.Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithInvalidIndex() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 record.get(3);
 }

 /**
 * Test get method with null name.
 */
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testGetWithNullName() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 record.get(null);
 }

 /**
 * Test get method with name not found.
 */
 @org.junit.Test
 public void testGetWithNameNotFound() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertNull(record.get("unknown"));
 }

 /**
 * Test isConsistent method with valid mapping.
 */
 @org.junit.Test
 public void testIsConsistentWithValidMapping() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertTrue(record.isConsistent());
 }

 /**
 * Test isConsistent method with invalid mapping.
 */
 @org.junit.Test
 public void testIsConsistentWithInvalidMapping() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 CSVRecord record = new CSVRecord(values, null, null, 1);
 assertFalse(record.isConsistent());
 }

 /**
 * Test isMapped method with valid name.
 */
 @org.junit.Test
 public void testIsMappedWithValidName() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertTrue(record.isMapped("name"));
 }

 /**
 * Test isMapped method with invalid name.
 */
 @org.junit.Test
 public void testIsMappedWithInvalidName() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertFalse(record.isMapped("unknown"));
 }

 /**
 * Test isSet method with valid name.
 */
 @org.junit.Test
 public void testIsSetWithValidName() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertTrue(record.isSet("name"));
 }

 /**
 * Test isSet method with invalid name.
 */
 @org.junit.Test
 public void testIsSetWithInvalidName() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertFalse(record.isSet("unknown"));
 }

 /**
 * Test iterator method.
 */
 @org.junit.Test
 public void testIterator() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 Iterator<String> iterator = record.iterator();
 assertEquals("John Doe", iterator.next());
 assertEquals("35", iterator.next());
 assertEquals("john.doe@example.com", iterator.next());
 assertFalse(iterator.hasNext());
 }

 /**
 * Test values method.
 */
 @org.junit.Test
 public void testValues() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertArrayEquals(values, record.values());
 }

 /**
 * Test getComment method with valid comment.
 */
 @org.junit.Test
 public void testGetCommentWithValidComment() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
 assertEquals("comment", record.getComment());
 }

 /**
 * Test getComment method with null comment.
 */
 @org.junit.Test
 public void testGetCommentWithNullComment() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertNull(record.getComment());
 }

 /**
 * Test getRecordNumber method with valid record number.
 */
 @org.junit.Test
 public void testGetRecordNumberWithValidRecordNumber() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertEquals(1, record.getRecordNumber());
 }

 /**
 * Test getRecordNumber method with invalid record number.
 */
 @org.junit.Test
 public void testGetRecordNumberWithInvalidRecordNumber() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, -1);
 assertNotEquals(1, record.getRecordNumber());
 }

 /**
 * Test size method.
 */
 @org.junit.Test
 public void testSize() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertEquals(3, record.size());
 }

 /**
 * Test toString method.
 */
 @org.junit.Test
 public void testToString() {
 String[] values = new String[] {"John Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 mapping.put("email", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertEquals("[John Doe, 35, john.doe@example.com]", record.toString());
 }
}