package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext jsonWriteContext;
 private JsonWriteContext jsonWriteContextParent;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 jsonWriteContextParent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 dupDetector = new DupDetector();
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
 assertNull(context.getParent());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
 assertNull(context.getParent());
 assertEquals(dupDetector, context.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext context = jsonWriteContext.createChildArrayContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
 assertEquals(jsonWriteContext, context.getParent());
 assertNull(context.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext context = jsonWriteContext.createChildObjectContext();
 assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
 assertEquals(jsonWriteContext, context.getParent());
 assertNull(context.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldName_NullName() {
 jsonWriteContext.writeFieldName(null);
 }

 @Test
 public void testWriteFieldName_EmptyName() {
 jsonWriteContext.writeFieldName("");
 }

 @Test
 public void testWriteFieldName_ValidName() throws JsonProcessingException {
 jsonWriteContext.writeFieldName("fieldName");
 assertEquals("fieldName", jsonWriteContext.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldName_DupDetector_DuplicateName() throws JsonProcessingException {
 jsonWriteContextParent.writeFieldName("fieldName");
 dupDetector.addFieldName("fieldName");
 jsonWriteContext.withDupDetector(dupDetector);
 jsonWriteContext.writeFieldName("fieldName");
 }

 @Test
 public void testWriteValue() {
 int result = jsonWriteContext.writeValue();
 assertTrue(result >= 0 && result <= JsonWriteContext.STATUS_OK_AFTER_SPACE);
 }

 @Test
 public void testToString() {
 assertEquals("{}", jsonWriteContext.toString());
 }
}