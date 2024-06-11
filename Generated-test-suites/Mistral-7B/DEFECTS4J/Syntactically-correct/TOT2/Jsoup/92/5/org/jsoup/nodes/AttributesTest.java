package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testGet() {
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 Assert.assertEquals(value, attributes.get(key));
 }

 @Test
 public void testGetIgnoreCase() {
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 Assert.assertEquals(value, attributes.getIgnoreCase(key));
 }

 @Test
 public void testGetEmpty() {
 Assert.assertEquals(Attributes.EmptyString, attributes.get("empty"));
 }

 @Test
 public void testPut() {
 String key = "key";
 String value1 = "value1";
 String value2 = "value2";
 attributes.put(key, value1);
 attributes.put(key, value2);
 Assert.assertEquals(value2, attributes.get(key));
 }

 @Test
 public void testPutIgnoreCase() {
 String key1 = "Key1";
 String key2 = "key2";
 String value = "value";
 attributes.put(key1, value);
 attributes.putIgnoreCase(key2, value);
 Assert.assertEquals(value, attributes.get(key1));
 Assert.assertEquals(value, attributes.getIgnoreCase(key2));
 }

 @Test
 public void testPutBoolean() {
 String key = "key";
 boolean value = true;
 attributes.put(key, value);
 Assert.assertTrue(attributes.getBoolean(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 attributes.put(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 attributes.put("key", null);
 }

 @Test
 public void testRemove() {
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 attributes.remove(key);
 Assert.assertFalse(attributes.hasKey(key));
 }

 @Test
 public void testRemoveIgnoreCase() {
 String key1 = "Key1";
 String key2 = "key2";
 String value = "value";
 attributes.put(key1, value);
 attributes.removeIgnoreCase(key2);
 Assert.assertFalse(attributes.hasKey(key1));
 Assert.assertFalse(attributes.hasKeyIgnoreCase(key2));
 }

 @Test
 public void testHasKey() {
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 Assert.assertTrue(attributes.hasKey(key));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 String key1 = "Key1";
 String key2 = "key2";
 String value = "value";
 attributes.put(key1, value);
 Assert.assertTrue(attributes.hasKeyIgnoreCase(key2));
 }

 @Test
 public void testHasKeyEmpty() {
 Assert.assertFalse(attributes.hasKey(""));
 }

 @Test
 public void testSize() {
 String key1 = "key1";
 String key2 = "key2";
 String value = "value";
 attributes.put(key1, value);
 attributes.put(key2, value);
 Assert.assertEquals(2, attributes.size());
 }

 @Test
 public void testAddAll() {
 String key1 = "key1";
 String value1 = "value1";
 String key2 = "key2";
 String value2 = "value2";
 Attributes attributes1 = new Attributes();
 attributes1.put(key1, value1);
 Attributes attributes2 = new Attributes();
 attributes2.put(key2, value2);
 attributes.addAll(attributes1);
 Assert.assertEquals(2, attributes.size());
 Assert.assertEquals(value1, attributes.get(key1));
 Assert.assertEquals(value2, attributes.get(key2));
 }

 @Test
 public void testAsList() {
 String key1 = "key1";
 String value1 = "value1";
 String key2 = "key2";
 String value2 = "value2";
 attributes.put(key1, value1);
 attributes.put(key2, value2);
 List<Map.Entry<String, String>> list = attributes.asList();
 Map.Entry<String, String> entry1 = list.get(0);
 Map.Entry<String, String> entry2 = list.get(1);
 Assert.assertEquals(key1, entry1.getKey());
 Assert.assertEquals(value1, entry1.getValue());
 Assert.assertEquals(key2, entry2.getKey());
 Assert.assertEquals(value2, entry2.getValue());
 }

 @Test
 public void testDataset() {
 String key1 = "data-key1";
 String value1 = "value1";
 String key2 = "data-key2";
 String value2 = "value2";
 attributes.put(key1, value1);
 attributes.put(key2, value2);
 Map<String, String> dataset = attributes.dataset();
 Assert.assertEquals(value1, dataset.get(key1));
 Assert.assertEquals(value2, dataset.get(key2));
 }

 @Test
 public void testHtml() {
 String html = "<div data-key1=\"value1\" data-key2=\"value2\" key=\"value\"></div>";
 String expected = "<div key=\"value\" data-key1=\"value1\" data-key2=\"value2\"></div>";
 attributes = new Attributes(html.substring(html.indexOf(" "), html.length()));
 Assert.assertEquals(expected, attributes.html());
 }

 @Test
 public void testToString() {
 String html = "<div data-key1=\"value1\" data-key2=\"value2\" key=\"value\"></div>";
 attributes = new Attributes(html.substring(html.indexOf(" "), html.length()));
 Assert.assertEquals(html, attributes.toString());
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 Attributes attributes4 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes3.put("key2", "value2");
 attributes4.put("key1", "value1");
 attributes4.put("key2", "value2");
 Assert.assertEquals(attributes1, attributes2);
 Assert.assertNotEquals(attributes1, attributes3);
 Assert.assertEquals(attributes1, attributes4);
 }

 @Test
 public void testClone() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = attributes1.clone();
 attributes1.put("key1", "value1");
 Assert.assertNotEquals(attributes1, attributes2);
 }

 @Test
 public void testNormalize() {
 String key1 = "KEY1";
 String key2 = "key2";
 Attributes attributes = new Attributes();
 attributes.put(key1, "value1");
 attributes.put(key2, "value2");
 attributes.normalize();
 Assert.assertEquals("key1", attributes.get(0).getKey());
 Assert.assertEquals("value1", attributes.get(0).getValue());
 Assert.assertEquals("key2", attributes.get(1).getKey());
 Assert.assertEquals("value2", attributes.get(1).getValue());
 }

 @Test
 public void testCapacity() {
 Attributes attributes = new Attributes();
 for (int i = 0; i < InitialCapacity + GrowthFactor; i++) {
 attributes.put("key" + i, "value" + i);
 }
 }
}