package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext() {
 return JsonWriteContext.createRootContext();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeFieldName("field1");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
 context.writeFieldName("field2");
 assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeValue());
 }

 @Test(expected = JsonGenerationException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeFieldName("field1");
 context.writeFieldName("field1");
 }

 @Test
 public void testWriteValueForRootContext() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test
 public void testWriteValueForObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext();
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 childContext.writeFieldName("field1");
 childContext.setCurrentValue("value1");
 childContext.writeValue();
 childContext.writeValue(); // second value
 parentContext.setCurrentValue(childContext);
 assertEquals("{\"field1\":[\"value1\",\"value1\"]}", parentContext.toString());
 }

 @Test
 public void testWriteValueForArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext();
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 childContext.writeValue(); // first value
 childContext.writeValue(); // second value
 parentContext.setCurrentValue(childContext);
 assertEquals("[,]", parentContext.toString());
 }
}