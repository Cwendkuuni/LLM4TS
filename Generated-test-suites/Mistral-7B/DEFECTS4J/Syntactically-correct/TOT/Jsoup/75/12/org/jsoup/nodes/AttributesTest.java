package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 private static final String KEY_1 = "key1";
 private static final String VALUE_1 = "value1";
 private static final String KEY_2 = "key2";
 private static final String VALUE_2 = "value2";
 private static final String KEY_3 = "class";
 private static final String VALUE_3 = "myClass";
 private static final String KEY_4 = "data-custom";
 private static final String VALUE_4 = "customValue";

 @Test
 public void testPutGet() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 assertEquals(VALUE_1, attributes.get(KEY_1));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase(KEY_1, VALUE_1);
 assertEquals(VALUE_1, attributes.get(KEY_1));
 }

 @Test
 public void testPutTwoAttributes() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 attributes.put(KEY_2, VALUE_2);
 assertEquals(VALUE_1, attributes.get(KEY_1));
 assertEquals(VALUE_2, attributes.get(KEY_2));
 }

 @Test
 public void testPutTwoAttributesSameKey() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 attributes.put(KEY_1, VALUE_2);
 assertEquals(VALUE_2, attributes.get(KEY_1));
 }

 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, false);
 assertFalse(attributes.get(KEY_1).isEmpty());
 assertNull(attributes.get(KEY_1));
 }

 @Test
 public void testPutBooleanAttributeRemove() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, false);
 attributes.remove(KEY_1);
 assertFalse(attributes.hasKey(KEY_1));
 }

 @Test
 public void testPutIgnoreCaseBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase(KEY_1, false);
 assertFalse(attributes.get(KEY_1).isEmpty());
 assertNull(attributes.get(KEY_1));
 attributes.removeIgnoreCase(KEY_1);
 assertFalse(attributes.hasKey(KEY_1));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 assertTrue(attributes.hasKey(KEY_1));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 assertTrue(attributes.hasKeyIgnoreCase(KEY_1));
 }

 @Test
 public void testHasKeyEmpty() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey(EMPTYSTRING));
 }

 @Test
 public void testHasKeyIgnoreCaseEmpty() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase(EMPTYSTRING));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 attributes.put(KEY_2, VALUE_2);
 assertEquals(2, attributes.size());
 }

 @Test
 public void testPutAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put(KEY_1, VALUE_1);
 Attributes attributes2 = new Attributes();
 attributes2.put(KEY_2, VALUE_2);
 Attributes attributes3 = new Attributes();
 attributes3.putAll(attributes1);
 attributes3.putAll(attributes2);
 assertEquals(2, attributes3.size());
 assertTrue(attributes3.hasKey(KEY_1));
 assertTrue(attributes3.hasKey(KEY_2));
 }

 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute(KEY_3, VALUE_3);
 attributes.put(attribute);
 assertEquals(VALUE_3, attributes.get(KEY_3));
 }

 @Test
 public void testPutDataAttribute() {
 Attributes attributes = new Attributes();
 attributes.put(new Attribute(KEY_4, VALUE_4));
 assertTrue(attributes.hasKey(KEY_4));
 assertEquals(VALUE_4, attributes.get(KEY_4));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put(null, VALUE_1);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 try {
 attributes.put(KEY_1, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 attributes.remove(KEY_1);
 assertFalse(attributes.hasKey(KEY_1));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase(KEY_1, VALUE_1);
 attributes.removeIgnoreCase(KEY_1);
 assertFalse(attributes.hasKey(KEY_1));
 }

 @Test
 public void testRemoveDoesntExist() {
 Attributes attributes = new Attributes();
 attributes.remove(KEY_1);
 assertFalse(attributes.hasKey(KEY_1));
 }

 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, "HEllO");
 attributes.normalize();
 assertEquals("hello", attributes.get(KEY_1));
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_4, VALUE_4);
 Map<String, String> dataset = attributes.dataset();
 assertTrue(dataset.containsKey(KEY_4));
 assertEquals(VALUE_4, dataset.get(KEY_4));
 }

 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put(KEY_1, VALUE_1);
 attributes.put(KEY_2, VALUE_2);
 assertEquals("<key1>value1</key1><key2>value2</key2>", attributes.html());
 }

 @Test
 public void testClone() {
 Attributes attributes1 = new Attributes();
 attributes1.put(KEY_1, VALUE_1);
 Attributes attributes2 = attributes1.clone();
 assertEquals(1, attributes1.size());
 assertEquals(1, attributes2.size());
 assertTrue(attributes1.hasKey(KEY_1));
 assertTrue(attributes2.hasKey(KEY_1));
 assertEquals(VALUE_1, attributes1.get(KEY_1));
 assertEquals(VALUE_1, attributes2.get(KEY_1));
 }
}