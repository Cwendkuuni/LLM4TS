package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 private Attributes attributes;
 private Attributes clone;
 private Attributes nullAttributes;
 private Attributes emptyAttributes;
 private Attributes sizeTestAttributes;

 @Before
 public void setup() {
 attributes = new Attributes();
 clone = attributes.clone();
 nullAttributes = null;
 emptyAttributes = new Attributes();
 sizeTestAttributes = new Attributes();
 sizeTestAttributes.put("key1", "value1");
 sizeTestAttributes.put("key2", "value2");
 sizeTestAttributes.put("key3", "value3");
 }

 @Test
 public void testGet() {
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 assertEquals("value3", attributes.get("key3"));
 assertNull(attributes.get("nonExistentKey"));
 }

 @Test
 public void testGetIgnoreCase() {
 assertEquals("value1", attributes.getIgnoreCase("key1"));
 assertEquals("value2", attributes.getIgnoreCase("key2"));
 assertEquals("value3", attributes.getIgnoreCase("key3"));
 assertNull(attributes.getIgnoreCase("nonExistentKey"));
 }

 @Test
 public void testPut() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key3", "value3");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 assertEquals("value3", attributes.get("key3"));
 }

 @Test
 public void testPutIgnoreCase() {
 attributes.putIgnoreCase("Key1", "value1");
 attributes.putIgnoreCase("key2", "value2");
 attributes.putIgnoreCase("key3", "value3");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 assertEquals("value3", attributes.get("key3"));
 }

 @Test
 public void testPutBoolean() {
 attributes.put("booleanKey", true);
 assertNull(attributes.get("booleanKey"));
 assertFalse(attributes.hasKey("booleanKey"));
 attributes.put("booleanKey", false);
 assertNull(attributes.get("booleanKey"));
 assertFalse(attributes.hasKey("booleanKey"));
 attributes.put("booleanKey", true);
 assertTrue(attributes.hasKey("booleanKey"));
 assertNull(attributes.get("booleanKey"));
 }

 @Test
 public void testPutAttribute() {
 Attribute attribute = new Attribute("key4", "value4");
 attributes.put(attribute);
 assertEquals("value4", attributes.get("key4"));
 assertSame(attribute, attributes.get("key4"));
 }

 @Test
 public void testRemove() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key3", "value3");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 assertEquals("value3", attributes.get("key3"));
 attributes.remove("key1");
 assertNull(attributes.get("key1"));
 assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 attributes.putIgnoreCase("key1", "value1");
 attributes.putIgnoreCase("key2", "value2");
 attributes.putIgnoreCase("key3", "value3");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 assertEquals("value3", attributes.get("key3"));
 attributes.removeIgnoreCase("key1");
 assertNull(attributes.get("key1"));
 assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testHasKey() {
 assertFalse(attributes.hasKey("nonExistentKey"));
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 }

 @Test
 public void testSize() {
 assertEquals(0, emptyAttributes.size());
 assertEquals(3, sizeTestAttributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes testAttributes = new Attributes();
 testAttributes.put("key4", "value4");
 Attributes mergedAttributes = new Attributes();
 mergedAttributes.addAll(attributes);
 mergedAttributes.addAll(testAttributes);
 assertEquals(6, mergedAttributes.size());
 assertTrue(mergedAttributes.hasKey("key1"));
 assertTrue(mergedAttributes.hasKey("key2"));
 assertTrue(mergedAttributes.hasKey("key3"));
 assertTrue(mergedAttributes.hasKey("key4"));
 }

 @Test
 public void testClone() {
 assertEquals(attributes, clone);
 attributes.put("key1", "value1");
 assertNotEquals(attributes, clone);
 }

 @Test(expected = NullPointerException.class)
 public void testNullArg() {
 attributes.put(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 attributes.put("key", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateKey() {
 attributes.put("key", "value");
 assertThrows(IllegalArgumentException.class, () -> attributes.put("key", "newValue"));
 }

 @Test
 public void testDataset() {
 Attributes dataset = attributes.dataset();
 assertTrue(dataset.hasKey("data-key"));
 assertTrue(dataset.hasKeyIgnoreCase("data-key"));
 assertFalse(dataset.hasKey("key"));
 assertFalse(dataset.hasKeyIgnoreCase("key"));
 }
}