package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext objectContextWithParent;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 rootContext = JsonWriteContext.createRootContext();
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 objectContextWithParent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, arrayContext, null);
 dupDetector = new DupDetector();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 // Test root context
 rootContext.writeFieldName("field1");
 rootContext.writeValue();
 rootContext.writeFieldName("field2");
 rootContext.writeValue();

 // Test array context
 arrayContext.writeFieldName("element1");
 arrayContext.writeValue();
 arrayContext.writeFieldName("element2");
 arrayContext.writeValue();

 // Test object context
 objectContext.writeFieldName("field1");
 objectContext.writeValue("value1");
 objectContext.writeFieldName("field2");
 objectContext.writeValue("value2");

 // Test object context with parent
 objectContextWithParent.writeFieldName("field1");
 objectContextWithParent.writeValue("value1");
 objectContextWithParent.writeFieldName("field2");
 objectContextWithParent.writeValue("value2");

 // Test getCurrentValue()
 assertEquals("value1", objectContext.getCurrentValue());
 assertEquals("value2", objectContext.writeValue("value3").getCurrentValue());

 // Test getDupDetector()
 objectContext.withDupDetector(dupDetector);
 objectContext.writeFieldName("field1");
 objectContext.writeValue("value1");
 objectContext.writeFieldName("field1");
 assertThrows(JsonProcessingException.class, () -> objectContext.writeValue("value2"));
 }

 @Test
 public void testToString() {
 assertEquals("/", rootContext.toString());
 assertEquals("[0]", arrayContext.toString());
 assertEquals("{\"field1\":null}", objectContext.toString());
 assertEquals("{\"field1\":null,\"field2\":null}", objectContextWithParent.toString());
 }
}