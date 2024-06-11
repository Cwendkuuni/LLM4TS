package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext context;
 private JsonWriteContext parentContext;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 dupDetector = new DupDetector();
 context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
 assertNull(rootContext.getParent());
 assertNull(rootContext.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext childContext = context.createChildArrayContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
 assertEquals(context, childContext.getParent());
 assertNull(childContext.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext childContext = context.createChildObjectContext();
 assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
 assertEquals(context, childContext.getParent());
 assertNull(childContext.getDupDetector());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 context.writeFieldName("name1");
 context.writeFieldName("name2");
 context.writeFieldName("name3");

 JsonWriteContext parent = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 JsonWriteContext child = parent.createChildObjectContext();
 child.writeFieldName("name4");
 child.writeFieldName("name1");

 assertEquals(3, context.getCurrentIndex());
 assertEquals(3, context.getIndexForCurrentName("name1"));
 assertEquals(1, context.getIndexForCurrentName("name2"));
 assertEquals(1, parent.getIndexForCurrentName("name4"));
 assertEquals(-1, parent.getIndexForCurrentName("name1"));

 context.reset();
 assertNull(context.getCurrentValue());
 assertNull(context.getCurrentName());
 assertEquals(-1, context.getCurrentIndex());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
 context.withDupDetector(dupDetector);
 context.writeFieldName("name1");
 context.writeFieldName("name1");
 }

 @Test
 public void testWriteValue() {
 context.writeValue();
 assertEquals(0, context.getCurrentIndex());
 context.writeValue();
 assertEquals(1, context.getCurrentIndex());
 context.writeValue();
 assertEquals(2, context.getCurrentIndex());
 context.writeValue();
 assertEquals(3, context.getCurrentIndex());
 context.writeValue();
 assertEquals(4, context.getCurrentIndex());
 }

 @Test
 public void testToString() {
 assertEquals("{}", context.toString());
 assertEquals("[", context.createChildArrayContext().toString());
 assertEquals("{name1}", context.writeFieldName("name1").toString());
 assertEquals("{name1}", context.writeFieldName("name1").toString());
 assertEquals("{name1,name2}", context.writeFieldName("name2").toString());
 assertEquals("{name1,name2,name3}", context.writeFieldName("name3").toString());
 }
}