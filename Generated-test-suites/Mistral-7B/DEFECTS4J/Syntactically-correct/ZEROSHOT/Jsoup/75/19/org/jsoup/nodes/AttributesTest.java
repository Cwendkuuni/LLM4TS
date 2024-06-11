package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class AttributesTest {

 @Test
 public void testAttributes() {
 Attributes attributes = new Attributes();

 // test get and put methods
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));

 // test getIgnoreCase method
 attributes.put("KEY1", "VALUE1");
 assertEquals("VALUE1", attributes.getIgnoreCase("key1"));
 assertEquals("VALUE1", attributes.getIgnoreCase("KEY1"));

 // test size method
 assertEquals(2, attributes.size());

 // test hasKey method
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
 assertTrue(attributes.hasKey("key2"));
 assertTrue(attributes.hasKeyIgnoreCase("key2"));

 // test remove method
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 assertFalse(attributes.hasKeyIgnoreCase("key1"));

 // test put method with existing key
 attributes.put("key1", "value3");
 assertEquals("value3", attributes.get("key1"));

 // test put method with boolean value
 attributes.put("boolKey", false);
 assertFalse(attributes.hasKey("boolKey"));
 attributes.put("boolKey", true);
 assertTrue(attributes.hasKey("boolKey"));

 // test put method with Attribute object
 Attribute attribute = new Attribute("key3", "value3");
 attributes.put(attribute);
 assertEquals("value3", attributes.get("key3"));

 // test remove method with Attribute object
 attribute = new Attribute("key3", "value3");
 attributes.put(attribute);
 attributes.remove(attribute);
 assertFalse(attributes.hasKey("key3"));

 // test addAll method
 Attributes otherAttributes = new Attributes();
 otherAttributes.put("otherKey1", "otherValue1");
 otherAttributes.put("otherKey2", "otherValue2");
 attributes.addAll(otherAttributes);
 assertEquals(4, attributes.size());
 assertTrue(attributes.hasKey("otherKey1"));
 assertTrue(attributes.hasKey("otherKey2"));

 // test iterator method
 List<Attribute> attributesList = attributes.asList();
 for (Attribute attribute : attributesList) {
 System.out.println(attribute.getKey() + "=" + attribute.getValue());
 }
 }
}