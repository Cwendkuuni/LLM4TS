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
 public void setUp() {
 parentContext = JsonWriteContext.createRootContext();
 dupDetector = new DupDetector();
 context = JsonWriteContext.createRootContext(dupDetector).withDupDetector(dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 JsonWriteContext rootContextWithDupDetector = JsonWriteContext.createRootContext(dupDetector);
 assertNull(rootContext.getParent());
 assertNull(rootContextWithDupDetector.getParent());
 assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
 assertEquals(JsonWriteContext.TYPE_ROOT, rootContextWithDupDetector.getType());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext arrayContext = context.createChildArrayContext();
 JsonWriteContext arrayContextWithDupDetector = context.createChildArrayContext().withDupDetector(dupDetector);
 assertSame(context, arrayContext.getParent());
 assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
 assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContextWithDupDetector.getType());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext objectContext = context.createChildObjectContext();
 JsonWriteContext objectContextWithDupDetector = context.createChildObjectContext().withDupDetector(dupDetector);
 assertSame(context, objectContext.getParent());
 assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
 assertEquals(JsonWriteContext.TYPE_OBJECT, objectContextWithDupDetector.getType());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 context = parentContext.createChildObjectContext();
 context.writeFieldName("name1");
 context.writeFieldName("name2");
 context.writeFieldName("name3");
 context.writeFieldName("name1");

 assertEquals("{\"name1\":null,\"name2\":null,\"name3\":null,\"name1\":null}", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException {
 context = parentContext.createChildObjectContext();
 context.writeFieldName("name1");
 context.writeFieldName("name1");
 }

 @Test
 public void testWriteValue() {
 context = parentContext.createChildArrayContext();
 context.writeValue();
 context.writeValue();
 context = parentContext.createChildObjectContext();
 context.writeValue();

 assertEquals("[null,null]{}", context.toString());
 }

 @Test
 public void testGetParent() {
 context = parentContext.createChildObjectContext();
 assertEquals(parentContext, context.getParent());
 }

 @Test
 public void testGetDupDetector() {
 context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 }

 @Test
 public void testReset() {
 context = JsonWriteContext.createObjectContext(parentContext).reset(JsonWriteContext.TYPE_ARRAY);
 assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
 }
}