package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext arrayContext;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 rootContext = JsonWriteContext.createRootContext();
 dupDetector = new DupDetector();
 objectContext = JsonWriteContext.createRootContext(dupDetector);
 arrayContext = JsonWriteContext.createRootContext();
 }

 @Test
 public void testRootContextWriteValue() {
 JsonWriteContext context = rootContext;
 assertEquals("/", context.toString());
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testObjectContextInvalidWriteFieldName() {
 JsonWriteContext context = objectContext;
 context.writeFieldName(null);
 }

 @Test
 public void testObjectContextWriteFieldNameAndValue() {
 JsonWriteContext context = objectContext;
 context.writeFieldName("field1");
 context.setCurrentValue("value1");
 assertEquals("{field1}", context.toString());
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testObjectContextWriteFieldNameDuplicate() {
 JsonWriteContext context = objectContext;
 context.writeFieldName("field1");
 context.writeFieldName("field1");
 }

 @Test
 public void testObjectContextWriteValueAfterFieldName() {
 JsonWriteContext context = objectContext;
 context.writeFieldName("field1");
 context.setCurrentValue("value1");
 context.writeValue();
 assertEquals("{field1:value1}", context.toString());
 }

 @Test
 public void testArrayContextWriteValue() {
 JsonWriteContext context = arrayContext;
 context.writeValue();
 assertEquals("[0]", context.toString());
 }

 @Test
 public void testArrayContextWriteValueWithIndex() {
 JsonWriteContext context = arrayContext;
 context.writeValue();
 context.writeValue();
 context.writeValue();
 assertEquals("[0,1,2]", context.toString());
 }

 @Test
 public void testArrayContextWriteValueAfterIndex() {
 JsonWriteContext context = arrayContext;
 context.writeValue();
 context.writeValue("value1");
 assertEquals("[0,value1]", context.toString());
 }

 @Test
 public void testObjectAndArrayContextHierarchy() {
 JsonWriteContext parentContext = JsonWriteContext.createRootContext();
 JsonWriteContext objectContext = parentContext.createChildObjectContext();
 JsonWriteContext arrayContext = objectContext.createChildArrayContext();
 assertEquals("/", parentContext.toString());
 assertEquals("{\n", objectContext.toString());
 assertEquals("[", arrayContext.toString());
 }

 @Test
 public void testObjectContextReset() {
 JsonWriteContext context = objectContext;
 context.reset(JsonWriteContext.TYPE_OBJECT);
 assertEquals("{}", context.toString());
 }

 @Test
 public void testArrayContextReset() {
 JsonWriteContext context = arrayContext;
 context.reset(JsonWriteContext.TYPE_ARRAY);
 assertEquals("[]", context.toString());
 }

 @Test
 public void testContextWithDupDetector() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext contextWithDupDetector = context.withDupDetector(dupDetector);
 assertNull(contextWithDupDetector.getDupDetector());
 assertEquals(context, contextWithDupDetector.getParent());
 }

 @Test
 public void testContextWithNullParent() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, dupDetector);
 assertNull(context.getParent());
 }
}