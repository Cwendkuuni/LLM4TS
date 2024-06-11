package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;

public class JsonWriteContextTest {

 private JsonFactory factory = new JsonFactory();
 private JsonWriter writer;
 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;

 @Test
 public void testRootContext() throws JsonProcessingException {
 rootContext = JsonWriteContext.createRootContext(null);
 writer = factory.createJsonWriter(new StringWriter());
 writer.setRootValue(rootContext);
 writer.writeValueAsString("Test Root Context");
 JsonWriteContext actualContext = writer.getActiveContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, actualContext.getParent().getType());
 assertEquals("Test Root Context", actualContext.getCurrentValue());
 }

 @Test
 public void testArrayContext() throws JsonProcessingException {
 rootContext = JsonWriteContext.createRootContext(null);
 arrayContext = rootContext.createChildArrayContext();
 writer = factory.createJsonWriter(new StringWriter());
 writer.setRootValue(rootContext);
 arrayContext.writeValue();
 writer.writeValueAsString("[");
 rootContext.setCurrentValue("Test Array");
 arrayContext.writeValue();
 writer.writeValueAsString("]");
 JsonWriteContext actualContext = writer.getActiveContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, actualContext.getParent().getType());
 assertEquals("Test Array", actualContext.getCurrentValue());
 }

 @Test
 public void testObjectContext() throws JsonProcessingException {
 rootContext = JsonWriteContext.createRootContext(null);
 objectContext = rootContext.createChildObjectContext();
 writer = factory.createJsonWriter(new StringWriter());
 writer.setRootValue(rootContext);
 objectContext.writeFieldName("Name");
 objectContext.writeValue("Test Object");
 writer.writeValueAsString("{\"Name\":\"Test Object\"}");
 JsonWriteContext actualContext = writer.getActiveContext();
 assertEquals(JsonWriteContext.TYPE_OBJECT, actualContext.getParent().getType());
 assertEquals("Test Object", actualContext.getCurrentValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 rootContext = JsonWriteContext.createRootContext(null);
 objectContext = rootContext.createChildObjectContext();
 writer = factory.createJsonWriter(new StringWriter());
 writer.setRootValue(rootContext);
 objectContext.writeFieldName("Name");
 objectContext.writeValue("Test Object 1");
 objectContext.writeFieldName("Name"); // duplicated field name
 objectContext.writeValue("Test Object 2");
 }

 @Test
 public void testGetCurrentIndex() throws JsonProcessingException {
 rootContext = JsonWriteContext.createRootContext(null);
 arrayContext = rootContext.createChildArrayContext();
 arrayContext.writeValue();
 arrayContext.writeValue();
 arrayContext.writeValue();
 assertEquals(2, arrayContext.getCurrentIndex());
 }

 @Test
 public void testWithDupDetector() throws JsonProcessingException {
 rootContext = JsonWriteContext.createRootContext(null);
 DupDetector dupDetector = new DupDetector();
 objectContext = rootContext.withDupDetector(dupDetector).createChildObjectContext();
 objectContext.writeFieldName("Name");
 objectContext.writeValue("Test Object 1");
 objectContext.writeFieldName("Name"); // duplicated field name
 try {
 objectContext.writeValue("Test Object 2");
 fail("Expected JsonGenerationException: Duplicate field 'Name'");
 } catch (JsonGenerationException e) {
 // expected
 }
 }

 @Test
 public void testReset() throws JsonProcessingException {
 rootContext = JsonWriteContext.createRootContext(null);
 objectContext = rootContext.createChildObjectContext();
 objectContext.writeFieldName("Name1");
 objectContext.writeValue("Test Object 1");
 objectContext.reset(JsonWriteContext.TYPE_ARRAY);
 writer = factory.createJsonWriter(new StringWriter());
 writer.setRootValue(rootContext);
 objectContext.writeValue();
 writer.writeValueAsString("[");
 objectContext.writeValue();
 writer.writeValueAsString("]");
 JsonWriteContext actualContext = writer.getActiveContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, actualContext.getParent().getType());
 assertEquals(null, actualContext.getCurrentValue());
 }
}