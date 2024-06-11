package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, DupDetector dupDetector) {
 return new JsonWriteContext(type, null, dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals("root", context.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals("/", context.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals("[", childContext.toString());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals("{\\", childContext.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwice() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null);
 context.writeFieldName("test");
 context.writeFieldName("test");
 }

 @Test
 public void testWriteValueInRootContext() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null);
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test
 public void testWriteValueInArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ARRAY, null);
 JsonWriteContext context = parentContext.createChildArrayContext();
 context.writeValue();
 assertEquals("[0]", context.toString());
 }

 @Test
 public void testWriteValueInObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null);
 JsonWriteContext context = parentContext.createChildObjectContext();
 context.writeFieldName("test");
 context.writeValue();
 assertEquals("{\"test\"}", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueInObjectContextWithoutFieldName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null);
 context.writeValue();
 }

 @Test
 public void testGetParent() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null);
 JsonWriteContext context = parentContext.createChildArrayContext();
 assertEquals(parentContext, context.getParent());
 }

 @Test
 public void testGetCurrentName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null);
 context.writeFieldName("test");
 assertEquals("test", context.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null);
 DupDetector dupDetector = new DupDetector();
 context.withDupDetector(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 }
}