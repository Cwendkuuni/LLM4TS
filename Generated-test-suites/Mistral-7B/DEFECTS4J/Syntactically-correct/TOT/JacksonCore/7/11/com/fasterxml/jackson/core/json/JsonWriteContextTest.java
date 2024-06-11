package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPreprendableDupDetector;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext nestedObjectContext;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 rootContext = JsonWriteContext.createRootContext(new DefaultPreprendableDupDetector());
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 nestedObjectContext = objectContext.createChildObjectContext();
 dupDetector = new DefaultPreprendableDupDetector();
 }

 @Test
 public void testWriteFieldName_ValidScenario() throws JsonProcessingException {
 // Given
 String name = "testField";
 int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COMMA;

 // When
 int status = objectContext.writeFieldName(name);

 // Then
 assertEquals(expectedStatus, status);
 }

 @Test
 public void testWriteFieldName_NameAlreadyUsed() throws JsonProcessingException {
 // Given
 String name = "testField";
 dupDetector.addDup(name);
 JsonWriteContext context = rootContext.createChildObjectContext();
 context.withDupDetector(dupDetector);

 // When & Then
 assertThrows(JsonGenerationException.class, () -> context.writeFieldName(name));
 }

 @Test
 public void testWriteValue_ValidScenario() throws JsonProcessingException {
 // Given
 JsonWriteContext context = arrayContext;
 Object value = new Object();
 int expectedStatus = (context.getIndex() < 0) ? JsonWriteContext.STATUS_OK_AS_IS : JsonWriteContext.STATUS_OK_AFTER_COMMA;

 // When
 int status = context.writeValue();

 // Then
 assertEquals(expectedStatus, status);
 context.setCurrentValue(value);
 }

 @Test
 public void testWriteValue_RecursiveNestedObjects() throws JsonProcessingException {
 // Given
 String name = "testField";
 int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COLON;

 // When
 int status = nestedObjectContext.writeFieldName(name);
 assertEquals(expectedStatus, status);
 nestedObjectContext.setCurrentValue("testValue");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValue_WriteFieldNameNotCalled() throws JsonProcessingException {
 // Given
 JsonWriteContext context = arrayContext;
 int expectedStatus = JsonWriteContext.STATUS_EXPECT_VALUE;

 // When
 int status = context.writeValue();

 // Then
 assertEquals(expectedStatus, status);
 }

 @Test
 public void testGetParent() {
 // Given
 JsonWriteContext parent = rootContext;
 JsonWriteContext child = objectContext;
 child.setParent(parent);

 // When
 JsonWriteContext parentRetrieved = child.getParent();

 // Then
 assertEquals(parent, parentRetrieved);
 }

 @Test
 public void testGetCurrentName() {
 // Given
 String name = "testField";
 objectContext.writeFieldName(name);

 // When
 String currentName = objectContext.getCurrentName();

 // Then
 assertNotNull(currentName);
 assertEquals(name, currentName);
 }

 @Test
 public void testGetCurrentName_NoNameSet() {
 // Given
 JsonWriteContext context = rootContext;

 // When
 String currentName = context.getCurrentName();

 // Then
 assertNull(currentName);
 }

 @Test
 public void testGetDupDetector() {
 // Given
 JsonWriteContext context = rootContext;
 DupDetector dupDetector = new DefaultPreprendableDupDetector();
 context.withDupDetector(dupDetector);

 // When
 DupDetector dupDetectorRetrieved = context.getDupDetector();

 // Then
 assertEquals(dupDetector, dupDetectorRetrieved);
 }

 @Test
 public void testGetDupDetector_NoDupDetectorSet() {
 // Given
 JsonWriteContext context = rootContext;

 // When
 DupDetector dupDetectorRetrieved = context.getDupDetector();

 // Then
 assertNull(dupDetectorRetrieved);
 }

 @Test
 public void testReset() throws JsonProcessingException {
 // Given
 JsonWriteContext context = arrayContext;
 context.writeValue();
 context.setCurrentValue("testValue");
 int indexBeforeReset = context.getIndex();

 // When
 JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_ARRAY);

 // Then
 assertEquals(JsonWriteContext.TYPE_ARRAY, resetContext.getType());
 assertEquals(-1, resetContext.getIndex());
 assertNull(resetContext.getCurrentName());
 assertNull(resetContext.getCurrentValue());
 assertNull(resetContext.getDupDetector());
 assertNotEquals(indexBeforeReset, resetContext.getIndex());
 }
}