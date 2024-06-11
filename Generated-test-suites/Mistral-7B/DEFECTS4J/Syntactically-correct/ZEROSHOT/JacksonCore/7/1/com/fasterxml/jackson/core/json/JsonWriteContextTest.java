package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = null;
JsonWriteContext childArrayContext = null;
JsonWriteContext childObjectContext = null;

@Test
public void testCreateRootContext() {
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, jsonWriteContext.getType());
 assertNull(jsonWriteContext.getParent());
}

@Test
public void testCreateRootContextWithDupDetector() {
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext(new NoopDupDetector());
 assertEquals(JsonWriteContext.TYPE_ROOT, jsonWriteContext.getType());
 assertNull(jsonWriteContext.getParent());
}

@Test
public void testCreateChildArrayContext() {
 childContext = parentContext;
 childArrayContext = parentContext.createChildArrayContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
 assertEquals(parentContext, childArrayContext.getParent());
 assertNull(childArrayContext._dups);
 assertNull(childArrayContext._child);
}

@Test
public void testCreateChildObjectContext() {
 childContext = parentContext;
 childObjectContext = parentContext.createChildObjectContext();
 assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
 assertEquals(parentContext, childObjectContext.getParent());
 assertNull(childObjectContext._dups);
 assertNull(childObjectContext._child);
}

@Test
public void testWriteFieldName() throws JsonProcessingException {
 childContext = parentContext;
 childContext = childContext.createChildObjectContext();
 childContext.writeFieldName("name");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.getStatus());
 assertEquals("name", childContext.getCurrentName());
}

@Test(expected = JsonProcessingException.class)
public void testWriteFieldNameTwice() throws JsonProcessingException {
 childContext = parentContext;
 childContext = childContext.createChildObjectContext();
 childContext.writeFieldName("name");
 childContext.writeFieldName("name");
}

@Test
public void testWriteValue() {
 int status = 0;
 childContext = parentContext;
 childContext = childContext.createChildArrayContext();
 status = childContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
 childContext = childContext.createChildObjectContext();
 status = childContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
}

@Test
public void testAppendDesc() {
 StringBuilder sb = new StringBuilder(64);
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
 jsonWriteContext.appendDesc(sb);
 assertEquals("{\r\n}", sb.toString());
 JsonWriteContext jsonWriteContext1 = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, jsonWriteContext, null);
 jsonWriteContext1.appendDesc(sb);
 assertEquals("[0]\r\n", sb.toString());
 JsonWriteContext jsonWriteContext2 = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, jsonWriteContext, null);
 jsonWriteContext2.appendDesc(sb);
 assertEquals("{\r\n  \"\":\r\n}", sb.toString());
}

@Test
public void testToString() {
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
 assertEquals("/", jsonWriteContext.toString());
 JsonWriteContext jsonWriteContext1 = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, jsonWriteContext, null);
 assertEquals("[0]", jsonWriteContext1.toString());
 JsonWriteContext jsonWriteContext2 = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, jsonWriteContext, null);
 assertEquals("{\r\n  \":\":\r\n}", jsonWriteContext2.toString());
}

class NoopDupDetector implements DupDetector {
 @Override public boolean isDup(String name) { return false; }

 @Override public DupDetector child() { return null; }
}

}