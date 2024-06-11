package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext context;
 private JsonWriteContext parentContext;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 dupDetector = new DupDetector();
 context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, dupDetector);
 }

 @Test
 public void testCreateRootContext() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals("[]", rootContext.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
 assertEquals("[]", rootContext.toString());
 }

 @Test
 public void testCreateChildArrayContext() throws JsonProcessingException {
 JsonWriteContext childContext = context.createChildArrayContext();
 assertEquals("[{}]", childContext.toString());
 }

 @Test
 public void testCreateChildArrayContextWithDupDetector() throws JsonProcessingException {
 JsonWriteContext childContext = context.createChildArrayContext();
 JsonWriteContext dupChildContext = context.createChildArrayContext();
 dupChildContext.withDupDetector(dupDetector);
 assertNotEquals(childContext, dupChildContext);
 assertEquals("[{}]", childContext.toString());
 assertEquals("[{}]", dupChildContext.toString());
 }

 @Test
 public void testCreateChildObjectContext() throws JsonProcessingException {
 JsonWriteContext childContext = context.createChildObjectContext();
 assertEquals("{\"field\":{}}", childContext.toString());
 }

 @Test
 public void testCreateChildObjectContextWithDupDetector() throws JsonProcessingException {
 JsonWriteContext childContext = context.createChildObjectContext();
 JsonWriteContext dupChildContext = context.createChildObjectContext();
 dupChildContext.withDupDetector(dupDetector);
 assertNotEquals(childContext, dupChildContext);
 assertEquals("{\"field\":{}}", childContext.toString());
 assertEquals("{\"field\":{}}", dupChildContext.toString());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 context.writeFieldName("field");
 assertEquals("{\"field\":}", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwice() throws JsonProcessingException {
 context.writeFieldName("field");
 context.writeFieldName("field");
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 context.writeValue();
 assertEquals("{\"field\":}", context.toString());
 }

 @Test
 public void testWriteValueTwice() throws JsonProcessingException {
 context.writeValue();
 context.writeValue();
 assertEquals("{\"field\":}", context.toString());
 }

 @Test
 public void testReset() throws JsonProcessingException {
 context.reset(JsonWriteContext.TYPE_OBJECT);
 assertEquals("{}", context.toString());
 }

 @Test
 public void testGetParent() throws JsonProcessingException {
 assertEquals(parentContext, context.getParent());
 }

 @Test
 public void testGetDupDetector() throws JsonProcessingException {
 assertEquals(dupDetector, context.getDupDetector());
 }
}