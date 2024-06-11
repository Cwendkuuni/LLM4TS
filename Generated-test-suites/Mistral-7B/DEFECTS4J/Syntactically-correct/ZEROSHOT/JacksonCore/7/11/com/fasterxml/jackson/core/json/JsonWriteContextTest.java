package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext childObjectContext;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 dupDetector = new DupDetector();
 rootContext = JsonWriteContext.createRootContext(dupDetector);
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 childObjectContext = objectContext.createChildObjectContext();
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
 assertNull(context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext context = arrayContext;
 assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
 assertEquals(rootContext, context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext context = objectContext;
 assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
 assertEquals(rootContext, context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 String name = "testName";
 childObjectContext.writeFieldName(name);
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childObjectContext.getStatus());
 assertEquals(name, childObjectContext.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwo() throws JsonProcessingException {
 String name = "testName";
 childObjectContext.writeFieldName(name);
 childObjectContext.writeFieldName(name);
 }

 @Test
 public void testWriteValue() {
 int status = arrayContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
 assertNull(arrayContext.getCurrentName());
 assertNull(arrayContext.getCurrentValue());
 }

 @Test
 public void testWriteValueObject() {
 Object value = new Object();
 int status = objectContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
 assertNull(objectContext.getCurrentName());
 assertEquals(value, objectContext.getCurrentValue());
 objectContext.setCurrentValue(null);
 }

 @Test
 public void testGetParent() {
 assertEquals(rootContext, arrayContext.getParent());
 assertEquals(objectContext, childObjectContext.getParent());
 }

 @Test
 public void testGetDupDetector() {
 assertEquals(dupDetector, objectContext.getDupDetector());
 }

 @Test
 public void testGetCurrentName() {
 String name = "testName";
 childObjectContext.writeFieldName(name);
 assertEquals(name, childObjectContext.getCurrentName());
 }

 @Test
 public void testGetCurrentValue() {
 Object value = new Object();
 childObjectContext.setCurrentValue(value);
 assertEquals(value, childObjectContext.getCurrentValue());
 }

 @Test
 public void testToString() {
 assertEquals("{}", rootContext.toString());
 assertEquals("[", arrayContext.toString());
 assertEquals("{\"testName\":}", childObjectContext.toString());
 }
}