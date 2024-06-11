package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 rootContext = JsonWriteContext.createRootContext(new NoOpDupDetector());
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 dupDetector = new ArrayDupDetector();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 Object value = new Object();

 // Test root context
 rootContext.writeValue();
 rootContext.writeFieldName("field1");
 rootContext.setCurrentValue(value);
 int result = rootContext.writeValue();
 assertEquals("Expected root context to write value after field name", JsonWriteContext.STATUS_OK_AFTER_COLON, result);

 // Test array context
 arrayContext.writeValue();
 arrayContext.writeFieldName("field1");
 result = arrayContext.writeValue();
 assertEquals("Expected array context to write value after comma", JsonWriteContext.STATUS_OK_AFTER_COMMA, result);

 // Test object context
 objectContext.writeFieldName("field1");
 objectContext.setCurrentValue(value);
 result = objectContext.writeValue();
 assertEquals("Expected object context to write value after colon", JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 }

 @Test(expected = JsonGenerationException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 Object value = new Object();

 rootContext.writeFieldName("field1");
 rootContext.setCurrentValue(value);
 rootContext.writeFieldName("field1");
 }

 @Test
 public void testGetParent() {
 assertNull("Expected root context to have no parent", rootContext.getParent());
 assertEquals("Expected array context to have root context as parent", rootContext, arrayContext.getParent());
 assertEquals("Expected object context to have root context as parent", rootContext, objectContext.getParent());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext contextWithDupDetector = JsonWriteContext.createRootContext(dupDetector);
 assertEquals("Expected context to have the same dup detector", dupDetector, contextWithDupDetector.getDupDetector());
 }

 @Test
 public void testToString() {
 assertEquals("/", rootContext.toString());
 assertEquals("[0]", arrayContext.toString());
 assertEquals("{\"field1\"}", objectContext.toString());
 }
}

class NoOpDupDetector implements DupDetector {
 @Override public boolean isDup(String name) {
 return false;
 }

 @Override public DupDetector child() {
 return null;
 }

 @Override public void reset() {}
}

class ArrayDupDetector implements DupDetector {
 private final String[] names;
 private int currentIndex = 0;

 public ArrayDupDetector() {
 names = new String[]{"field1", "field1", "field2"};
 }

 @Override public boolean isDup(String name) {
 for (String existingName : names) {
 if (name.equals(existingName)) {
 return true;
 }
 }
 return false;
 }

 @Override public DupDetector child() {
 return new ArrayDupDetector();
 }

 @Override public void reset() {
 currentIndex = 0;
 }
}