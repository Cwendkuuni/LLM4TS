package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Testing the functionality of CSVRecord class.
 */
public class CSVRecordTest {

 /**
 * Testing get method with valid index.
 */
 @Test
 public void testGetValidIndex() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 String expectedName = "John";
 String actualName = record.get(0);
 assertEquals(expectedName, actualName);
 }

 /**
 * Testing get method with invalid index.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInvalidIndex() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 String actualValue = record.get(3);
 }

 /**
 * Testing get method with valid name.
 */
 @Test
 public void testGetValidName() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 String expectedName = "Name";
 String actualName = record.get(expectedName);
 assertEquals(expectedName, actualName);
 }

 /**
 * Testing get method with invalid name.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidName() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 String actualValue = record.get("InvalidName");
 }

 /**
 * Testing isConsistent method.
 */
 @Test
 public void testIsConsistent() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 assertTrue(record.isConsistent());
 }

 /**
 * Testing isMapped method.
 */
 @Test
 public void testIsMapped() {
 String csvData = "Name,Age,City\nName,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 assertTrue(record.isMapped("Name"));
 }

 /**
 * Testing isSet method.
 */
 @Test
 public void testIsSet() {
 String csvData = "Name,Age,City\nName,30,New York\n,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 assertFalse(record.isSet("Age"));
 }

 /**
 * Testing iterator method.
 */
 @Test
 public void testIterator() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 List<String> expectedValues = Arrays.asList("John", "30", "New York");
 List<String> actualValues = new ArrayList<>();
 for (String value : record) {
 actualValues.add(value);
 }
 assertEquals(expectedValues, actualValues);
 }

 /**
 * Testing size method.
 */
 @Test
 public void testSize() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 int expectedSize = 3;
 int actualSize = record.size();
 assertEquals(expectedSize, actualSize);
 }

 /**
 * Testing getRecordNumber method.
 */
 @Test
 public void testGetRecordNumber() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 long expectedRecordNumber = 1;
 long actualRecordNumber = record.getRecordNumber();
 assertEquals(expectedRecordNumber, actualRecordNumber);
 }

 /**
 * Testing toString method.
 */
 @Test
 public void testToString() {
 String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Boston";
 List<CSVRecord> records = parseCSV(csvData);
 CSVRecord record = records.get(0);
 String expectedValues = "[\"John\", \"30\", \"New York\"]";
 String actualValues = record.toString();
 assertEquals(expectedValues, actualValues);
 }

 /**
 * Helper method to parse CSV data.
 */
 private List<CSVRecord> parseCSV(String csvData) {
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader());
 return parser.getRecords();
 }

}