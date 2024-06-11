package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testAddAndGetAttributes() {
 // Test adding and getting attributes
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testAddingDuplicateKey() {
 // Test adding duplicate key
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 assertEquals("value2", attributes.get("key1"));
 }

 @Test
 public void testGetIgnoreCase() {
 // Test getting attribute ignore case
 attributes.put("key1", "value1");
 assertEquals("value1", attributes.getIgnoreCase("key1"));
 assertEquals("value1", attributes.getIgnoreCase("Key1"));
 }

 @Test
 public void testPutIgnoreCase() {
 // Test putting attribute ignore case
 attributes.put("Key1", "value1");
 assertEquals("value1", attributes.getIgnoreCase("Key1"));
 }

 @Test
 public void testBooleanAttribute() {
 // Test boolean attribute
 attributes.put("key1", "true");
 assertTrue(attributes.getBoolean("key1"));
 attributes.put("key1", null);
 assertFalse(attributes.getBoolean("key1"));
 }

 @Test
 public void testAddAll() {
 // Test adding all attributes
 Attributes newAttributes = new Attributes();
 newAttributes.put("key3", "value3");
 newAttributes.put("key4", "value4");
 attributes.addAll(newAttributes);
 assertEquals("value3", attributes.get("key3"));
 assertEquals("value4", attributes.get("key4"));
 }

 @Test
 public void testRemoveAttribute() {
 // Test removing attribute
 attributes.put("key1", "value1");
 assertEquals("value1", attributes.get("key1"));
 attributes.remove("key1");
 assertEquals(0, attributes.size());
 }

 @Test
 public void testRemoveIgnoreCase() {
 // Test removing attribute ignore case
 attributes.put("Key1", "value1");
 assertEquals("value1", attributes.get("Key1"));
 attributes.removeIgnoreCase("Key1");
 assertEquals(0, attributes.size());
 }

 @Test
 public void testHasKey() {
 // Test has key
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 // Test has key ignore case
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 // Test null key
 attributes.put(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 // Test null value
 attributes.put("key1", null);
 }

 @Test
 public void testSize() {
 // Test size
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }
}