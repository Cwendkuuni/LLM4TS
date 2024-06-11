package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Test;

/**
 * Test class for {@link JsonWriteContext}.
 */
public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext parent = context.getParent();
        assertNull(parent);
        assertNotNull(context);
        assertEquals("", context.toString());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertNull(context.getParent());
        assertEquals("[", context.toString());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertNull(context.getParent());
        assertEquals("{}", context.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        DupDetector duplicatesDetector = new DupDetector();
        context.withDupDetector(duplicatesDetector);
        context.writeFieldName("test");
        context.writeFieldName("test");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field1");
        context.writeValue();
        context.writeValue();
        assertEquals("{\"field1\":}", context.toString());
    }

    @Test
    public void testWriteValueWithArray() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.writeValue();
        context.writeValue();
        assertEquals("[,]", context.toString());
    }

    @Test
    public void testWriteValueWithObject() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field1");
        context.writeValue();
        context.writeValue();
        assertEquals("{\"field1\":,\"field1\":}", context.toString());
    }

    @Test
    public void testWriteValueWithParent() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.writeValue();
        context.writeValue();
        JsonWriteContext parentContextCheck = context.getParent();
        assertEquals(JsonWriteContext.TYPE_ARRAY, parentContextCheck.getType());
        assertEquals("[,]", parentContextCheck.toString());
    }
}