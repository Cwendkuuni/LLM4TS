package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 assertEquals(Attributes.EMPTY_STRING, attributes.get("emptyKey"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 assertEquals("Value", attributes.getIgnoreCase("Key"));
 assertEquals(Attributes.EMPTY_STRING, attributes.getIgnoreCase("emptyKey"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value1");
 attributes.putIgnoreCase("key", "Value2");
 assertEquals("Value2", attributes.get("Key"));
 assertEquals("Value2", attributes.get("key"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("BooleanKey", true);
 assertTrue(attributes.hasKey("BooleanKey"));
 assertEquals(Attributes.EMPTY_STRING, attributes.get("BooleanKey"));
 attributes.put("BooleanKey", false);
 assertFalse(attributes.hasKey("BooleanKey"));
 }

 @Test
 public void testPutWithNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 assertEquals(Attributes.EMPTY_STRING, attributes.get("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put(null, "value");
 } catch (IllegalArgumentException e) {
 assertEquals("key cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithEmptyKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put("", "value");
 } catch (IllegalArgumentException e) {
 assertEquals("key cannot be empty", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithKeyContainingDataPrefix() {
 Attributes attributes = new Attributes();
 try {
 attributes.put("data-key", "value");
 } catch (IllegalArgumentException e) {
 assertEquals("key cannot contain data- prefix", e.getMessage());
 }
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNullKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.remove(null);
 } catch (IllegalArgumentException e) {
 assertEquals("key cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithEmptyKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.remove("");
 } catch (IllegalArgumentException e) {
 assertEquals("key cannot be empty", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithKeyContainingDataPrefix() {
 Attributes attributes = new Attributes();
 try {
 attributes.remove("data-key");
 } catch (IllegalArgumentException e) {
 assertEquals("key cannot contain data- prefix", e.getMessage());
 }
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
 assertFalse(attributes.hasKeyIgnoreCase("key"));
 attributes.put("Key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testHasKeyWithEmptyKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey(""));
 }

 @Test
 public void testHasKeyWithNullKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey(null));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 assertTrue(attributes3.hasKey("key1"));
 assertTrue(attributes3.hasKey("key2"));
 }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithNullOutputSettings() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 try {
 attributes.html(null, new Document(""));
 } catch (SerializationException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHtmlWithInvalidSyntax() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 try {
 attributes.html(new Document.OutputSettings(Document.OutputSettings.Syntax.xml), new Document(""));
 } catch (IllegalArgumentException e) {
 assertEquals("Attributes can only be serialized to HTML", e.getMessage());
 }
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertEquals("value1", dataset.get("data-key1"));
 assertEquals("value2", dataset.get("data-key2"));
 }

 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "Value");
 attributes.normalize();
 assertEquals("key", attributes.get("KEY"));
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testNormalizeWithEmptyKey() {
 Attributes attributes = new Attributes();
 attributes.put("", "Value");
 attributes.normalize();
 assertEquals("", attributes.get(""));
 }

 @Test
 public void testNormalizeWithNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 attributes.normalize();
 assertEquals(Attributes.EMPTY_STRING, attributes.get("key"));
 }
}