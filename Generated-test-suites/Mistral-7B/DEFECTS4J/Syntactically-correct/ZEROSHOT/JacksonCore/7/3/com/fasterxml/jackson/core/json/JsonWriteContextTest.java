package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childArrayContext;
 JsonWriteContext childObjectContext;
 DupDetector dupDetector = new DupDetector();

 @Test
 public void testCreateRootContext() {
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, jsonWriteContext.getType());
 assertNull(jsonWriteContext.getParent());
 assertNull(jsonWriteContext.getCurrentName());
 assertNull(jsonWriteContext.getCurrentValue());
 }

 @Test
 public void testCreateChildArrayContext() {
 childArrayContext = parentContext.createChildArrayContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
 assertSame(parentContext, childArrayContext.getParent());
 assertNull(childArrayContext.getCurrentName());
 assertNull(childArrayContext.getCurrentValue());
 }

 @Test
 public void testCreateChildObjectContext() {
 childObjectContext = parentContext.createChildObjectContext();
 assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
 assertSame(parentContext, childObjectContext.getParent());
 assertNull(childObjectContext.getCurrentName());
 assertNull(childObjectContext.getCurrentValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException {
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext(dupDetector);
 jsonWriteContext.writeFieldName("name");
 jsonWriteContext.writeFieldName("name");
 }

 @Test
 public void testWriteFieldNameAndValue() throws JsonProcessingException {
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
 jsonWriteContext.writeFieldName("name");
 jsonWriteContext.setCurrentValue("value");
 jsonWriteContext.writeValue();
 assertEquals("{\"name\":\"value\"}", jsonWriteContext.toString());
 }

 @Test
 public void testWriteValueWithParentContext() throws JsonProcessingException {
 JsonWriteContext childObjectContext = parentContext.createChildObjectContext();
 childObjectContext.writeFieldName("name");
 childObjectContext.setCurrentValue("value");
 parentContext.writeValue();
 assertEquals("{ \"name\": [", parentContext.toString());
 }

 @Test
 public void testWriteValueWithChildArrayContext() throws JsonProcessingException {
 childArrayContext = parentContext.createChildArrayContext();
 childArrayContext.writeValue();
 parentContext.writeValue();
 assertEquals("{ [", parentContext.toString());
 }

 @Test
 public void testWriteValueWithChildObjectContext() throws JsonProcessingException {
 childObjectContext = parentContext.createChildObjectContext();
 childObjectContext.writeFieldName("name");
 childObjectContext.setCurrentValue("value");
 parentContext.writeValue();
 assertEquals("{ \"name\": {", parentContext.toString());
 }

 @Test
 public void testToStringWithParentContext() {
 JsonWriteContext childObjectContext = parentContext.createChildObjectContext();
 childObjectContext.writeFieldName("name");
 assertEquals("/{ \"name\":{}", parentContext.toString());
 }

 @Test
 public void testToStringWithChildArrayContext() {
 childArrayContext = parentContext.createChildArrayContext();
 assertEquals("/[", parentContext.toString());
 }

 @Test
 public void testToStringWithChildObjectContext() {
 childObjectContext = parentContext.createChildObjectContext();
 childObjectContext.writeFieldName("name");
 assertEquals("/{ \"name\":{}", parentContext.toString());
 }

}