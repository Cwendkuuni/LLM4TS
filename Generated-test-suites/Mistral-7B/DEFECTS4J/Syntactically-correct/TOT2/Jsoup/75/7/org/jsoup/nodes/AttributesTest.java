package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 // Test case for get() method with non-existing key
 @Test
 public void testGetNonExistingKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.get("nonExistingKey");
 assertEquals(Attributes.EmptyString, actual);
 }

 // Test case for getIgnoreCase() method
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase() method with non-existing key
 @Test
 public void testGetIgnoreCaseNonExistingKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.getIgnoreCase("nonExistingKey");
 assertEquals(Attributes.EmptyString, actual);
 }

 // Test case for put() method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 String actual = attributes.get(key);
 String expected = value;
 assertEquals(expected, actual);
 }

 // Test case for put() method with existing key
 @Test
 public void testPutExistingKey() {
 Attributes attributes = new Attributes();
 String key = "key";
 String existingValue = "existingValue";
 String newValue = "newValue";
 attributes.put(key, existingValue);
 attributes.put(key, newValue);
 String actual = attributes.get(key);
 String expected = newValue;
 assertEquals(expected, actual);
 }

 // Test case for put() method with null value
 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 String key = "key";
 attributes.put(key, null);
 }

 // Test case for putIgnoreCase() method
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 String key = "Key";
 String value = "Value";
 attributes.putIgnoreCase(key, value);
 String actual = attributes.getIgnoreCase("key");
 String expected = value;
 assertEquals(expected, actual);
 }

 // Test case for putIgnoreCase() method with case change and existing key
 @Test
 public void testPutIgnoreCaseWithCaseChange() {
 Attributes attributes = new Attributes();
 String key = "Key";
 String value = "Value";
 attributes.putIgnoreCase(key, value);
 attributes.put(key, "existingValue");
 String actual = attributes.getIgnoreCase("key");
 String expected = value;
 assertEquals(expected, actual);
 }

 // Test case for put() method with empty key
 @Test(expected = IllegalArgumentException.class)
 public void testPutEmptyKey() {
 Attributes attributes = new Attributes();
 String key = "";
 String value = "value";
 attributes.put(key, value);
 }

 // Test case for size() method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actual = attributes.size();
 int expected = 2;
 assertEquals(expected, actual);
 }

 // Test case for addAll() method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);
 String actualKey1 = attributes1.get("key1");
 String expectedValue1 = "value1";
 assertEquals(expectedValue1, actualKey1);
 String actualKey2 = attributes1.get("key2");
 String expectedValue2 = "value2";
 assertEquals(expectedValue2, actualKey2);
 }

 // Test case for iterator() method
 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Iterator<Attribute> iterator = attributes.iterator();
 Attribute attribute = iterator.next();
 String actualKey1 = attribute.getKey();
 String expectedValue1 = "value1";
 assertEquals(expectedValue1, actualKey1);
 }

 // Test case for asList() method
 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> list = attributes.asList();
 Attribute attribute1 = list.get(0);
 Attribute attribute2 = list.get(1);
 String actualKey1 = attribute1.getKey();
 String expectedValue1 = "value1";
 assertEquals(expectedValue1, actualKey1);
 String actualKey2 = attribute2.getKey();
 String expectedValue2 = "value2";
 assertEquals(expectedValue2, actualKey2);
 }

 // Test case for hasKey() method
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actual = attributes.hasKey("key");
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test case for hasKey() method with non-existing key
 @Test
 public void testHasKeyNonExistingKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKey("nonExistingKey");
 boolean expected = false;
 assertEquals(expected, actual);
 }

 // Test case for hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 boolean actual = attributes.hasKeyIgnoreCase("key");
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test case for hasKeyIgnoreCase() method with non-existing key
 @Test
 public void testHasKeyIgnoreCaseNonExistingKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKeyIgnoreCase("nonExistingKey");
 boolean expected = false;
 assertEquals(expected, actual);
 }

 // Test case for remove() method
 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 String actual = attributes.get("key");
 assertEquals(Attributes.EmptyString, actual);
 }

 // Test case for removeIgnoreCase() method
 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 attributes.removeIgnoreCase("key");
 String actual = attributes.getIgnoreCase("key");
 assertEquals(Attributes.EmptyString, actual);
 }

 // Test case for remove() method with non-existing key
 @Test
 public void testRemoveNonExistingKey() {
 Attributes attributes = new Attributes();
 attributes.remove("nonExistingKey");
 int actualSize = attributes.size();
 int expectedSize = 0;
 assertEquals(expectedSize, actualSize);
 }

 // Test case for removeIgnoreCase() method with non-existing key
 @Test
 public void testRemoveIgnoreCaseNonExistingKey() {
 Attributes attributes = new Attributes();
 attributes.removeIgnoreCase("nonExistingKey");
 int actualSize = attributes.size();
 int expectedSize = 0;
 assertEquals(expectedSize, actualSize);
 }

 // Test case for checkCapacity() method
 @Test
 public void testCheckCapacity() {
 Attributes attributes = new Attributes();
 attributes.checkCapacity(10);
 int actualCapacity = attributes.keys.length;
 int expectedCapacity = 10;
 assertEquals(expectedCapacity, actualCapacity);
 }
}