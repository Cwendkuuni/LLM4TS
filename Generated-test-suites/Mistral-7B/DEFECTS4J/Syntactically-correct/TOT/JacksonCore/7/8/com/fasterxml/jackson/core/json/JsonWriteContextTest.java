package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JsonWriteContextTest {

 private JsonWriteContext context;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 context = JsonWriteContext.createRootContext();
 dupDetector = new DupDetector();
 context = context.withDupDetector(dupDetector);
 }

 @Test
 public void testEmptyContext() {
 assertEquals("", context.toString());
 assertNull(context.getCurrentValue());
 assertNull(context.getParent());
 }

 @Test
 public void testRootContextWriteFieldName() throws JsonProcessingException {
 context.writeFieldName("name1");
 context.writeValue();
 context.writeFieldName("name2");
 context.writeValue();

 assertEquals("[{\"name1\":null,\"name2\":null}]", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 context.writeFieldName("name1");
 context.writeValue();
 context.writeFieldName("name1");
 context.writeValue();
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext childContext = context.createChildArrayContext();
 assertEquals("[", childContext.toString());
 assertSame(context, childContext.getParent());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext childContext = context.createChildObjectContext();
 assertEquals("{\"", childContext.toString());
 assertSame(context, childContext.getParent());
 }

 @Test
 public void testWriteValueInArrayContext() {
 JsonWriteContext arrayContext = context.createChildArrayContext();
 arrayContext.writeValue();
 arrayContext.writeValue();

 JsonWriteContext rootContext = context.reset(JsonWriteContext.TYPE_ROOT);
 rootContext.writeValue();
 rootContext.writeValue();

 assertEquals("[null,null]", rootContext.toString());
 }

 @Test
 public void testWriteValueInObjectContext() {
 JsonWriteContext objectContext = context.createChildObjectContext();
 objectContext.writeFieldName("name1");
 objectContext.writeValue("value1");
 objectContext.writeFieldName("name2");
 objectContext.writeValue("value2");

 JsonWriteContext rootContext = context.reset(JsonWriteContext.TYPE_ROOT);
 rootContext.writeFieldName("name1");
 rootContext.setCurrentValue("value1");
 rootContext.writeValue();
 rootContext.writeFieldName("name2");
 rootContext.setCurrentValue("value2");
 rootContext.writeValue();

 assertEquals("{\"name1\":\"value1\",\"name2\":\"value2\"}", rootContext.toString());
 }

 @Test
 public void testWriteValueAfterNameInObjectContext() throws JsonProcessingException {
 JsonWriteContext objectContext = context.createChildObjectContext();
 objectContext.writeFieldName("name1");

 // Check that error is thrown when trying to set value without writing name first
 try {
 objectContext.setCurrentValue("value1");
 objectContext.writeValue();
 } catch (JsonProcessingException e) {
 // Expected
 }

 objectContext.writeValue("value1");

 JsonWriteContext rootContext = context.reset(JsonWriteContext.TYPE_ROOT);
 rootContext.writeFieldName("name1");
 rootContext.setCurrentValue("value1");
 rootContext.writeValue();

 assertEquals("{\"name1\":\"value1\"}", rootContext.toString());
 }

 @Test
 public void testResetContext() {
 context.writeFieldName("name1");
 context.writeValue();
 context.writeFieldName("name2");
 context.writeValue();

 context = context.reset(JsonWriteContext.TYPE_ROOT);

 assertEquals("", context.toString());
 assertNull(context.getCurrentValue());
 assertNull(context.getParent());
 }
}