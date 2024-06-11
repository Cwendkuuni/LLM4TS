package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext nestedObjectContext;

 @Before
 public void setUp() {
 rootContext = JsonWriteContext.createRootContext();
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 nestedObjectContext = objectContext.createChildObjectContext();
 }

 @After
 public void tearDown() {
 rootContext = null;
 arrayContext = null;
 objectContext = null;
 nestedObjectContext = null;
 }

 @Test
 public void testGetParent() {
 assertNull(rootContext.getParent());
 assertEquals(rootContext, arrayContext.getParent());
 assertEquals(arrayContext, objectContext.getParent());
 assertEquals(objectContext, nestedObjectContext.getParent());
 }

 @Test
 public void testGetDupDetector() {
 assertNull(rootContext.getDupDetector());
 assertNotNull(arrayContext.getDupDetector());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 String fieldName = "name";
 int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COMMA;

 arrayContext.writeFieldName(fieldName);
 assertEquals(expectedStatus, arrayContext.getStatus());

 objectContext.writeFieldName(fieldName);
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.getStatus());

 // Test for duplicate field name
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext dupObjectContext = objectContext.withDupDetector(dupDetector);
 dupObjectContext.writeFieldName(fieldName);
 dupObjectContext.writeFieldName(fieldName);
 assertNotNull(dupDetector.getDuplicate());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithInvalidName() throws JsonProcessingException {
 arrayContext.writeFieldName("\"name\"");
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COMMA;

 arrayContext.writeValue();
 assertEquals(expectedStatus, arrayContext.getStatus());

 expectedStatus = JsonWriteContext.STATUS_OK_AFTER_SPACE;
 rootContext.writeValue();
 assertEquals(expectedStatus, rootContext.getStatus());
 }

 @Test
 public void testGetCurrentIndex() {
 assertEquals(-1, arrayContext.getCurrentIndex());
 arrayContext.writeValue();
 arrayContext.writeValue();
 assertEquals(0, arrayContext.getCurrentIndex());
 }

 @Test
 public void testToString() {
 assertEquals("{}", rootContext.toString());
 assertEquals("[]", arrayContext.toString());
 assertEquals("{\"name\":}", objectContext.toString());
 assertEquals("{\"name\":\"value\"}", nestedObjectContext.toString());
 }
}