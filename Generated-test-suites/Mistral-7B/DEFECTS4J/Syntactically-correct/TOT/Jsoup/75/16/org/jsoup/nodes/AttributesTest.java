package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testBasicUsage() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 assertNull(attributes.get("key3"));

 assertEquals(2, attributes.size());
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 assertFalse(attributes.hasKey("key3"));
 assertFalse(attributes.hasKeyIgnoreCase("KEY3"));

 Attributes clone = attributes.clone();
 assertEquals(attributes.size(), clone.size());
 assertNotSame(attributes, clone);
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");

 assertEquals("Value1", attributes.getIgnoreCase("Key1"));
 assertEquals("Value1", attributes.getIgnoreCase("key1"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attributes attributes = new Attributes();
 attributes.put(null, "Value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("Key", null);
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.putIgnoreCase("key1", "Value2");

 assertEquals("Value2", attributes.get("Key1"));
 assertEquals("Value2", attributes.getIgnoreCase("Key1"));
 assertEquals("Value2", attributes.getIgnoreCase("key1"));
 }

 @Test
 public void testBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("attribute", true);
 assertTrue(attributes.hasKey("attribute"));
 assertFalse(attributes.get("attribute").isEmpty());
 assertNull(attributes.get("attribute").getValue());
 }

 @Test
 public void testPutReplace() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value1");
 attributes.put("Key", "Value2");

 assertEquals("Value2", attributes.get("Key"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");

 assertEquals(2, attributes.size());
 attributes.remove("Key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("Key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");

 assertEquals(2, attributes.size());
 attributes.removeIgnoreCase("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("Key1"));
 }

 @Test
 public void testPutAndRemove() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");
 attributes.remove("Key1");

 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("Key1"));
 assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
 assertTrue(attributes.hasKey("Key2"));
 }

 @Test
 public void testPutAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("Key1", "Value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("Key2", "Value2");

 Attributes combined = new Attributes();
 combined.putAll(attributes1);
 combined.putAll(attributes2);

 assertEquals(2, combined.size());
 assertTrue(combined.hasKey("Key1"));
 assertTrue(combined.hasKey("Key2"));
 }

 @Test
 public void testHasKeyIgnoreCaseCaseSensitive() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");

 assertFalse(attributes.hasKeyIgnoreCase("Key2"));
 assertFalse(attributes.hasKeyIgnoreCase("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 }

 @Test
 public void testHasKeyIgnoreCaseCaseInsensitive() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");

 assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 }

 @Test
 public void testEmptyAttributes() {
 Attributes attributes = new Attributes();

 assertEquals(0, attributes.size());
 assertFalse(attributes.hasKey("Key"));
 assertFalse(attributes.hasKeyIgnoreCase("Key"));
 }

 @Test
 public void testLowerCase() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "VALUE");

 assertEquals("key", attributes.getIgnoreCase("KEY").getKey());
 assertEquals("VALUE", attributes.getIgnoreCase("KEY").getValue());
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");

 Attributes clone = attributes.clone();

 assertEquals(2, clone.size());
 assertTrue(clone.hasKey("Key1"));
 assertTrue(clone.hasKey("Key2"));
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();

 attributes1.put("Key1", "Value1");
 attributes2.put("Key1", "Value1");
 attributes3.put("Key2", "Value2");

 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 }

 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();

 attributes1.put("Key1", "Value1");
 attributes2.put("Key1", "Value1");
 attributes3.put("Key2", "Value2");

 assertEquals(attributes1.hashCode(), attributes1.hashCode());
 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
 }
}