package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DupDetector;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(DupDetector dupDetector) {
 return new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);

 // Test simple case
 context.writeFieldName("field1");
 assertEquals("field1", context.getCurrentName());
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());

 // Test duplicate field name
 DupDetector dupDetector = new DupDetector();
 context = createContext(dupDetector);
 context.writeFieldName("field1");
 context.writeFieldName("field1");
 assertTrue(dupDetector.isDup("field1"));

 // Test null field name
 context.writeFieldName(null);
 assertNull(context.getCurrentName());
 assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameAfterValue() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.setCurrentValue("value1");
 context.writeFieldName("field1");
 // This should fail as name was already set
 context.writeValue();
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeFieldName("field1");
 context.setCurrentValue("value1");
 assertEquals("value1", context.getCurrentValue());
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
 }

 @Test
 public void testWriteValueForArray() throws JsonProcessingException {
 JsonWriteContext context = createContext(null).createChildArrayContext();
 context.writeValue();
 context.writeValue();
 context.writeValue();
 assertEquals(3, context.getCurrentIndex());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueForNonArrayContext() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeValue();
 fail("Expected JsonProcessingException");
 }

 @Test
 public void testToString() {
 JsonWriteContext context = createContext(null);
 context.writeFieldName("field1");
 context.setCurrentValue("value1");
 context.writeValue();
 String jsonPath = context.toString();
 assertEquals("{field1}", jsonPath);
 }
}