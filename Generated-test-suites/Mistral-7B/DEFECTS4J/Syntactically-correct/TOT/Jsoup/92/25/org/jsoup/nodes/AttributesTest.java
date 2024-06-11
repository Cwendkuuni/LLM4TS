package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class AttributesTest {

 @Test
 public void testBasicUsage() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 assertEquals(2, attributes.size());

 assertFalse(attributes.hasKey("nonexistentkey"));
 assertNull(attributes.get("nonexistentkey"));
 }

 @Test
 public void testCaseInsensitive() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");

 assertEquals("value1", attributes.getIgnoreCase("key1"));
 assertEquals("value1", attributes.getIgnoreCase("Key1"));
 }

 @Test
 public void testReplaceExistingValue() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");

 assertEquals("value2", attributes.get("key1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 }

 @Test
 public void testBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("booleanAttribute", false);
 assertFalse(attributes.get("booleanAttribute").equals("true"));

 attributes.put("booleanAttribute", true);
 assertTrue(attributes.get("booleanAttribute").equals(""));

 attributes.put("booleanAttribute", false);
 assertFalse(attributes.get("booleanAttribute").equals(""));
 }

 @Test
 public void testPutIgnoringCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("key1", "value1");
 attributes.putIgnoreCase("Key1", "value2");

 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("Key1"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");

 attributes.remove("key1");
 assertNull(attributes.get("key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("key1", "value2");

 attributes.removeIgnoreCase("key1");
 assertNull(attributes.get("key1"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");

 assertTrue(attributes.hasKey("key1"));
 assertFalse(attributes.hasKey("nonexistentkey"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");

 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
 }

 @Test
 public void testAddAll() {
 Attributes first = new Attributes();
 first.put("key1", "value1");
 first.put("key2", "value2");

 Attributes second = new Attributes();
 second.put("key2", "value3");
 second.put("key3", "value3");

 Attributes result = new Attributes();
 result.addAll(first);
 result.addAll(second);

 assertEquals(4, result.size());
 assertEquals("value1", result.get("key1"));
 assertEquals("value3", result.get("key2"));
 assertEquals("value3", result.get("key3"));
 assertNull(result.get("nonexistentkey"));
 }

 @Test
 public void testIterable() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 List<Attribute> list = attributes.asList();
 assertEquals(2, list.size());
 assertTrue(list.get(0).getKey().equals("key1"));
 assertTrue(list.get(0).getValue().equals("value1"));
 assertTrue(list.get(1).getKey().equals("key2"));
 assertTrue(list.get(1).getValue().equals("value2"));
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();attributes.put("key1", "value1");attributes.put("data-key2", "value2");attributes.put("data-key3", "value3");

 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertTrue(dataset.containsKey("data-key2"));
 assertTrue(dataset.containsKey("data-key3"));
 assertEquals("value2", dataset.get("data-key2"));
 assertEquals("value3", dataset.get("data-key3"));
 }
}