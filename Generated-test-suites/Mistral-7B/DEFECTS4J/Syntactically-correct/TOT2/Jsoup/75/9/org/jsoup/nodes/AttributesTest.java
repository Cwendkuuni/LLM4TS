package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 // Test constructor and basic methods
 @Test
 public void testAttributesConstruction() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 @Test
 public void testGetEmptyAttribute() {
 Attributes attributes = new Attributes();
 assertEquals(Attributes.EmptyString, attributes.get("nonexistent"));
 }

 @Test
 public void testPutAndGetAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testPutAndGetCaseInsensitiveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "value");
 assertEquals("value", attributes.getIgnoreCase("key"));
 }

 // Test iteration
 @Test
 public void testIterateAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 List<Map.Entry<String, String>> expectedList = Arrays.asList(
 new AbstractMap.SimpleImmutableEntry<>("key1", "value1"),
 new AbstractMap.SimpleImmutableEntry<>("key2", "value2")
 );

 List<Map.Entry<String, String>> actualList = Arrays.asList(attributes.asList().iterator().next());
 assertEquals(expectedList, actualList);
 }

 // Test size method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test addition of attributes
 @Test
 public void testAddAllAttributes() {
 Attributes baseAttributes = new Attributes();
 baseAttributes.put("baseKey1", "baseValue1");
 baseAttributes.put("baseKey2", "baseValue2");

 Attributes additionalAttributes = new Attributes();
 additionalAttributes.put("additionalKey1", "additionalValue1");
 additionalAttributes.put("additionalKey2", "additionalValue2");
 baseAttributes.addAll(additionalAttributes);

 List<Map.Entry<String, String>> expectedList = Arrays.asList(
 new AbstractMap.SimpleImmutableEntry<>("baseKey1", "baseValue1"),
 new AbstractMap.SimpleImmutableEntry<>("baseKey2", "baseValue2"),
 new AbstractMap.SimpleImmutableEntry<>("additionalKey1", "additionalValue1"),
 new AbstractMap.SimpleImmutableEntry<>("additionalKey2", "additionalValue2")
 );

 List<Map.Entry<String, String>> actualList = Arrays.asList(baseAttributes.asList().iterator().next());
 assertEquals(expectedList, actualList);
 }

 // Test removal of attributes
 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 assertEquals(1, attributes.size());
 assertEquals(Attributes.EmptyString, attributes.get("key1"));
 }

 // Test removal of case-insensitive attribute
 @Test
 public void testRemoveIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("KEY1", "value1");
 attributes.put("KEY2", "value2");
 attributes.removeIgnoreCase("Key1");
 assertEquals(1, attributes.size());
 assertEquals(Attributes.EmptyString, attributes.get("Key1"));
 }

 // Test checking for existence of attributes
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("KEY1", "value1");
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 }

 @Test
 public void testHasNoKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("nonexistent"));
 }

 @Test
 public void testHasNoKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
 }

 // Test exception handling
 @Test(expected = NullPointerException.class)
 public void testNullKeyException() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValueException() {
 Attributes attributes = new Attributes();attributes.put("key", null); }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributeByIndexException() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveNonExistentAttributeException() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.remove(Integer.MAX_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutKeyWithEmptyString() {
 Attributes attributes = new Attributes();
 attributes.put("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutEmptyStringValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutEmptyListValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", new ArrayList<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutSetValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", new AbstractSet<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMapValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", new HashMap<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutClassValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", Attributes.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutArrayValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", new String[1]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutPrimitiveValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", 123);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutLongValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", Long.MAX_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutDoubleValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", Double.MAX_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutFloatValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", Float.MAX_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutCharValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", 'A');
 }

}