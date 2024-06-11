package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
 return new JsonWriteContext(type, parent, dups);
 }

 @Test
 public void testCreateRootContext() throws JsonProcessingException, IOException {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals("", context.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() throws JsonProcessingException, IOException {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals("/", context.toString());
 }

 @Test
 public void testCreateChildArrayContext() throws JsonProcessingException, IOException {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals("[]", childContext.toString());
 }

 @Test
 public void testCreateChildObjectContext() throws JsonProcessingException, IOException {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals("{\"\":}", childContext.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithoutGotName() throws JsonProcessingException, IOException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("fieldName");
 }

 @Test
 public void testWriteFieldNameWithGotName() throws JsonProcessingException, IOException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("fieldName");
 context.setGotName(true);
 context.writeFieldName("fieldName2");
 assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.getStatus());
 }

 @Test
 public void testWriteValue() throws JsonProcessingException, IOException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ARRAY, null, null);
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.getStatus());

 context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 }

 @Test
 public void testCheckDup() throws JsonProcessingException, IOException {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 context.writeFieldName("fieldName");
 context.writeFieldName("fieldName");
 }

 @Test(expected = JsonGenerationException.class)
 public void testCheckDupWithDupName() throws JsonProcessingException, IOException {
 DupDetector dupDetector = new DupDetector();
 dupDetector.addDuplicate("fieldName");
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 context.writeFieldName("fieldName");
 }
}