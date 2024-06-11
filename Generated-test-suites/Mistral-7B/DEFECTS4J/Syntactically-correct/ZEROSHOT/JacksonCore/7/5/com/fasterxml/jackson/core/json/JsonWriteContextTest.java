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
 parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 dupDetector = new DupDetector();
 context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals("root", rootContext.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
 assertEquals("/", rootContext.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext arrayContext = context.createChildArrayContext();
 assertEquals("[", arrayContext.toString());
 }

 @Test
 public void testCreateChildArrayContextWithDupDetector() {
 JsonWriteContext arrayContext = context.createChildArrayContext();
 JsonWriteContext newArrayContext = arrayContext.createChildArrayContext();
 JsonWriteContext newArrayContextWithDupDetector = newArrayContext.withDupDetector(dupDetector);
 assertEquals("[", arrayContext.toString());
 assertEquals("[", newArrayContext.toString());
 assertEquals("[", newArrayContextWithDupDetector.toString());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext objectContext = context.createChildObjectContext();
 assertEquals("{", objectContext.toString());
 }

 @Test
 public void testCreateChildObjectContextWithDupDetector() {
 JsonWriteContext objectContext = context.createChildObjectContext();
 JsonWriteContext newObjectContext = objectContext.createChildObjectContext();
 JsonWriteContext newObjectContextWithDupDetector = newObjectContext.withDupDetector(dupDetector);
 assertEquals("{", objectContext.toString());
 assertEquals("{", newObjectContext.toString());
 assertEquals("{", newObjectContextWithDupDetector.toString());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 context.writeFieldName("fieldName");
 JsonWriteContext childContext = context.createChildArrayContext();
 context.writeFieldName("fieldName2");
 assertEquals("{fieldName,fieldName2}", context.toString());
 assertEquals("[", childContext.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwiceWithSameName() throws JsonProcessingException {
 context.writeFieldName("fieldName");
 context.writeFieldName("fieldName");
 }

 @Test
 public void testWriteValue() {
 context.writeValue();
 assertEquals("{fieldName}", context.toString());
 }

 @Test
 public void testWriteValueInRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 rootContext.writeValue();
 assertEquals("/", rootContext.toString());
 }

 @Test
 public void testGetParent() {
 assertEquals(parentContext, context.getParent());
 }

 @Test
 public void testGetCurrentName() {
 context.writeFieldName("fieldName");
 assertEquals("fieldName", context.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 assertEquals(dupDetector, context.getDupDetector());
 }
}