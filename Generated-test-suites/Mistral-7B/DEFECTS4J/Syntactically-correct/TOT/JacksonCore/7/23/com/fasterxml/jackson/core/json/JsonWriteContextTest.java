package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.*;

public class JsonWriteContextTest {

 @BeforeAll
 static void setup() {
 System.setProperty("com.fasterxml.jackson.core.JsonProcessingException.findAndReport", "NONE");
 }

 @AfterAll
 static void teardown() {
 System.clearProperty("com.fasterxml.jackson.core.JsonProcessingException.findAndReport");
 }

 @ParameterizedTest
 @ValueSource(strings = { "validObject", "validArray", "empty" })
 void testCreateContext(String contextType) {
 JsonWriteContext parentContext = null;
 DupDetector noDups = null;
 JsonWriteContext context = null;

 switch (contextType) {
 case "validObject":
 context = JsonWriteContext.createRootContext(noDups);
 break;
 case "validArray":
 context = JsonWriteContext.createRootContext(noDups).createChildArrayContext();
 break;
 case "empty":
 context = JsonWriteContext.createRootContext(noDups).reset(JsonWriteContext.TYPE_OBJECT);
 break;
 default:
 throw new RuntimeException("Invalid context type: " + contextType);
 }

 assertNotNull(context);
 assertEquals(contextType.equals("validObject") ? JsonWriteContext.TYPE_OBJECT : JsonWriteContext.TYPE_ARRAY, context._type);
 assertNull(context._parent);
 assertNull(context._dups);
 assertNull(context._child);
 assertNull(context._currentName);
 assertNull(context._currentValue);
 assertFalse(context._gotName);
 }

 @Test
 void testWriteFieldNameNullName() {
 JsonWriteContext parentContext = null;
 DupDetector noDups = null;
 JsonWriteContext context = JsonWriteContext.createRootContext(noDups);
 assertThrows(JsonProcessingException.class, () -> context.writeFieldName(null));
 }

 @Test
 void testWriteFieldNameEmptyName() {
 JsonWriteContext parentContext = null;
 DupDetector noDups = null;
 JsonWriteContext context = JsonWriteContext.createRootContext(noDups);
 assertThrows(JsonProcessingException.class, () -> context.writeFieldName(""));
 }

 @Test
 void testWriteFieldNameValidName() {
 JsonWriteContext parentContext = null;
 DupDetector noDups = null;
 JsonWriteContext context = JsonWriteContext.createRootContext(noDups);
 context.writeFieldName("testName");
 assertTrue(context._gotName);
 assertEquals("testName", context._currentName);
 }

 @Test
 void testWriteValueNullContext() {
 assertThrows(IllegalStateException.class, () -> JsonWriteContext.writeValue(null));
 }

 @Test
 void testWriteValueEmptyContext() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeValue());
 }

 @Test
 void testWriteValueObjectContext() {
 JsonWriteContext parentContext = null;
 DupDetector noDups = null;
 JsonWriteContext context = JsonWriteContext.createRootContext(noDups);
 context.writeFieldName("testName");
 context.setCurrentValue("testValue");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
 }

 @Test
 void testWriteValueArrayContext() {
 JsonWriteContext parentContext = null;
 DupDetector noDups = null;
 JsonWriteContext context = JsonWriteContext.createRootContext(noDups).createChildArrayContext();
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
 }

 @Test
 void testWriteValueRootContext() {
 JsonWriteContext parentContext = null;
 DupDetector noDups = null;
 JsonWriteContext context = JsonWriteContext.createRootContext(noDups);
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
 }
}