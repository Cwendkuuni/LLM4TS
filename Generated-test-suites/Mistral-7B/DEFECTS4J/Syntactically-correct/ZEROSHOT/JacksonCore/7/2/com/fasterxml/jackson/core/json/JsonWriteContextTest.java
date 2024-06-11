package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class JsonWriteContextTest {

 private JsonWriteContext createContext(DupDetector dupDetector) {
 return JsonWriteContext.createRootContext(dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = createContext(null);
 assertEquals("[]", context.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = createContext(dupDetector);
 assertEquals("[]", context.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals("[]", parentContext.toString());
 assertEquals("[]", childContext.toString());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals("{}", parentContext.toString());
 assertEquals("{\"}", childContext.toString());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeFieldName("testField");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithNameAlreadySet() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeFieldName("testField");
 context.writeFieldName("testField2");
 }

 @Test
 public void testWriteValue() {
 JsonWriteContext context = createContext(null);
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());
 }

 @Test
 public void testWriteValueForObjectContext() {
 JsonWriteContext context = createContext(null).createChildObjectContext();
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 }

 @Test
 public void testWriteValueForArrayContext() {
 JsonWriteContext context = createContext(null).createChildArrayContext();
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
 }

 @Test
 public void testWriteValueWithValueSet() {
 JsonWriteContext context = createContext(null);
 context.setCurrentValue("testValue");
 context.writeValue();
 assertEquals("testValue", context.getCurrentValue());
 }

 @Test
 public void testGetParent() {
 JsonWriteContext parentContext = createContext(null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, childContext.getParent());
 }

 @Test
 public void testGetCurrentName() {
 JsonWriteContext context = createContext(null).createChildObjectContext();
 context.writeFieldName("testField");
 assertEquals("testField", context.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext context = createContext(null);
 assertNull(context.getDupDetector());
 }

 @Test
 public void testWithDupDetector() {
 JsonWriteContext context = createContext(null);
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext contextWithDupDetector = context.withDupDetector(dupDetector);
 assertEquals(dupDetector, contextWithDupDetector.getDupDetector());
 }
}