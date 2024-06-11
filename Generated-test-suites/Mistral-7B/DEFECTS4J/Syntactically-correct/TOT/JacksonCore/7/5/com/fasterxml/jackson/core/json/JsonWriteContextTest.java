package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DupDetector;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 dupDetector = new DupDetector();
 rootContext = JsonWriteContext.createRootContext(dupDetector);
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 }

 @Test
 public void testWriteFieldName_ValidName() throws JsonProcessingException {
 String name = "testName";
 int status = objectContext.writeFieldName(name);
 assertEquals("Expected STATUS_OK_AFTER_COMMA", JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldName_DuplicateName() throws JsonProcessingException {
 String name = "testName";
 objectContext.writeFieldName(name);
 objectContext.writeFieldName(name);
 }

 @Test
 public void testWriteValue_RootContext() throws JsonProcessingException {
 int status = rootContext.writeValue();
 assertEquals("Expected STATUS_OK_AS_IS for root context", JsonWriteContext.STATUS_OK_AS_IS, status);
 }

 @Test
 public void testWriteValue_ArrayContext() throws JsonProcessingException {
 int status = arrayContext.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COMMA for array context", JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
 }

 @Test
 public void testWriteValue_ObjectContext() throws JsonProcessingException {
 String name1 = "testName1";
 String name2 = "testName2";
 int index = 0;

 objectContext.writeFieldName(name1);
 int status1 = objectContext.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COLON for object context", JsonWriteContext.STATUS_OK_AFTER_COLON, status1);

 objectContext.writeFieldName(name2);
 int status2 = objectContext.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COMMA for object context", JsonWriteContext.STATUS_OK_AFTER_COMMA, status2);

 // Check if correct value was set
 assertEquals("Expected 'testName1' for current name", name1, objectContext.getCurrentName());
 assertEquals("Expected 'testName2' for current value", (Object) index++, objectContext.getCurrentValue());
 }
}