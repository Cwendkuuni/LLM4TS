package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationError;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext objectContextWithName;
 private DupDetector noDupDetector;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 noDupDetector = new DupDetector();
 dupDetector = new DupDetector(noDupDetector);
 rootContext = JsonWriteContext.createRootContext(dupDetector);
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 objectContextWithName = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
 }

 @Test
 public void testWriteFieldName() {
 String name = "testField";
 try {
 objectContextWithName.writeFieldName(name);
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContextWithName.getStatus());
 } catch (JsonProcessingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWhenGotNameIsTrue() {
 String name = "testField";
 try {
 objectContextWithName.writeFieldName(name);
 objectContextWithName.writeFieldName(name);
 fail("Should have thrown an exception");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testWriteValue_rootContext() {
 try {
 rootContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.getStatus());
 } catch (JsonProcessingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testWriteValue_arrayContext() {
 try {
 arrayContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.getStatus());
 } catch (JsonProcessingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testWriteValue_objectContext_noName() {
 try {
 objectContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.getStatus());
 } catch (JsonProcessingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testWriteValue_objectContext_withName() {
 String name = "testField";
 try {
 objectContextWithName.writeFieldName(name);
 objectContextWithName.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContextWithName.getStatus());
 } catch (JsonProcessingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValue_objectContext_withName_twice() {
 String name = "testField";
 try {
 objectContextWithName.writeFieldName(name);
 objectContextWithName.writeValue();
 objectContextWithName.writeValue();
 fail("Should have thrown an exception");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testCheckDup() {
 String name1 = "testField1";
 String name2 = "testField2";
 try {
 dupDetector.record(name1);
 dupDetector.record(name2);
 dupDetector.checkDup(name1);
 fail("Should have thrown an exception");
 } catch (JsonGenerationError e) {
 // Expected
 }
 }

 @Test
 public void testCheckDup_unique() {
 String name = "testField";
 try {
 dupDetector.record(name);
 dupDetector.checkDup(name);
 // No exception
 } catch (JsonProcessingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetParent() {
 JsonWriteContext parent = rootContext;
 JsonWriteContext child = rootContext.createChildArrayContext();
 assertEquals(parent, child.getParent());
 }

 @Test
 public void testGetCurrentName() {
 String name = "testField";
 objectContextWithName.writeFieldName(name);
 assertEquals(name, objectContextWithName.getCurrentName());
 }

 @Test
 public void testGetCurrentIndex() {
 int index = 0;
 expectThrown(JsonProcessingException.class, () -> arrayContext.writeValue());
 // Index should not be set
 assertNull(arrayContext.getCurrentIndex());
 }

 @Test
 public void testGetDupDetector() {
 assertSame(dupDetector, rootContext.getDupDetector());
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext(noDupDetector);
 assertNotNull(context);
 assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
 assertNull(context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 assertNull(context.getDupDetector().getParent());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext context = rootContext.createChildArrayContext();
 assertNotNull(context);
 assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
 assertEquals(rootContext, context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 assertNull(context.getDupDetector().getParent());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext context = rootContext.createChildObjectContext();
 assertNotNull(context);
 assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
 assertEquals(rootContext, context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 assertNull(context.getDupDetector().getParent());
 }

 @Test
 public void testReset() {
 JsonWriteContext context = rootContext.createChildObjectContext();
 context.writeFieldName("testField1");
 context.writeValue();
 context = context.reset(JsonWriteContext.TYPE_ARRAY);
 assertNotNull(context);
 assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
 assertSame(rootContext, context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 assertNull(context.getDupDetector().getParent());
 }

 @Test
 public void testAppendDesc() {
 StringBuilder sb = new StringBuilder(64);
 rootContext.appendDesc(sb);
 assertEquals("{\r\n", sb.toString());

 arrayContext.appendDesc(sb);
 assertEquals("[0]\r\n", sb.toString());

 objectContextWithName.appendDesc(sb);
 assertEquals("{\"testField\":}\r\n", sb.toString());
 }

 private void expectThrown(Class<? extends Throwable> exceptionType, Runnable runnable) {
 try {
 runnable.run();
 fail("Expected exception: " + exceptionType.getSimpleName());
 } catch (Throwable e) {
 if (exceptionType.isInstance(e)) {
 // Expected
 } else {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}

}