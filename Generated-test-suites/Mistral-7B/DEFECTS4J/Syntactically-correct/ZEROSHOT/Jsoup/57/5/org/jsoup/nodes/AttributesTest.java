package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String result = attributes.get("key");
 assertEquals("value", result);
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String result = attributes.getIgnoreCase("Key");
 assertEquals("value", result);
 }

 @Test
 public void testGetNull() {
 Attributes attributes = new Attributes();
 String result = attributes.get("key");
 assertEquals("", result);
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Attributes expected = new Attributes();
 expected.put("key", "value");
 assertEquals(expected, attributes);
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 Attributes expected = new Attributes();
 expected.put("key", new BooleanAttribute("key"));
 assertEquals(expected, attributes);
 }

 @Test
 public void testPutNull() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 attributes.put("key", null);
 assertNull(attributes.get("key"));
 assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testPutSameKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 Attributes expected = new Attributes();
 expected.put("key", "value2");
 assertEquals(expected, attributes);
 }

 @Test
 public void testPutEmptyString() {
 Attributes attributes = new Attributes();
 attributes.put("key", "");
 Attributes expected = new Attributes();
 expected.put("key", "");
 assertEquals(expected, attributes);
 }

 @Test
 public void testPutEmptyMap() {
 Attributes attributes = new Attributes();
 Attributes expected = new Attributes();
 attributes.putAll(expected);
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 Attributes expected = new Attributes();
 expected.put("key2", "value2");
 assertEquals(expected, attributes);
 }

 @Test
 public void testRemoveNull() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 attributes.remove("key");
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 attributes.removeIgnoreCase("key");
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testRemoveNonExistent() {
 Attributes attributes = new Attributes();
 attributes.remove("key");
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testRemoveAll() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Attributes expected = new Attributes();
 attributes.clear();
 assertEquals(expected, attributes);
 }

 @Test
 public void testAddAllNull() {
 Attributes attributes = new Attributes();
 Attributes incoming = null;
 attributes.addAll(incoming);
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testAddAllEmpty() {
 Attributes attributes = new Attributes();
 Attributes incoming = new Attributes();
 attributes.addAll(incoming);
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testAddAllSameInstance() {
 Attributes attributes = new Attributes();
 Attributes incoming = attributes;
 attributes.addAll(incoming);
 assertEquals(attributes, incoming);
 }

 @Test
 public void testAddAllDifferentInstances() {
 Attributes attributes = new Attributes();
 Attributes incoming = new Attributes();
 incoming.put("key1", "value1");
 attributes.addAll(incoming);
 Attributes expected = new Attributes();
 expected.put("key1", "value1");
 assertEquals(expected, attributes);
 }

 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> actualList = attributes.asList();
 Iterator<Attribute> actualIterator = attributes.iterator();
 List<Attribute> expectedList = new ArrayList<>();
 expectedList.add(new Attribute("key1", "value1"));
 expectedList.add(new Attribute("key2", "value2"));
 Iterator<Attribute> expectedIterator = expectedList.iterator();
 assertEquals(expectedIterator.next(), actualIterator.next());
 assertEquals(expectedList.size(), attributes.size());
 while (expectedIterator.hasNext()) {
 Attribute expectedAttribute = expectedIterator.next();
 Attribute actualAttribute = actualIterator.next();
 assertEquals(expectedAttribute, actualAttribute);
 }
 }

 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> expectedList = new ArrayList<>();
 expectedList.add(new Attribute("key1", "value1"));
 expectedList.add(new Attribute("key2", "value2"));
 List<Attribute> actualList = attributes.asList();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testHasCustomDataAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("data-key"));
 assertTrue(attributes.hasKey("data-key"));
 }

 @Test
 public void testHasNoCustomDataAttribute() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase("data-key"));
 assertFalse(attributes.hasKey("data-key"));
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> expectedMap = new LinkedHashMap<>(2);
 expectedMap.put("key1", "value1");
 expectedMap.put("key2", "value2");
 Map<String, String> actualMap = attributes.dataset();
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testDatasetEmpty() {
 Attributes attributes = new Attributes();
 Map<String, String> actualMap = attributes.dataset();
 assertTrue(actualMap.isEmpty());
 }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String expected = "{\"key\":\"value\"}";
 String actual = attributes.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsNull() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.equals(null));
 }

 @Test
 public void testEqualsSelf() {
 Attributes attributes = new Attributes();
 assertTrue(attributes.equals(attributes));
 }

 @Test
 public void testEqualsSameInstance() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = attributes1;
 assertTrue(attributes1.equals(attributes2));
 }

 @Test
 public void testEqualsSameContent() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 assertTrue(attributes1.equals(attributes2));
 }

 @Test
 public void testEqualsDifferentContent() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 assertFalse(attributes1.equals(attributes2));
 }

 @Test
 public void testHashCode() {
 Attributes attributes = new Attributes();
 int expected = 0;
 if (attributes.attributes != null) {
 expected += attributes.attributes.hashCode();
 }
 int actual = attributes.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes, clonedAttributes);
 }
}