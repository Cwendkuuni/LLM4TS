package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link JsonWriteContext}.
 */
public class JsonWriteContextTest {

    @Test
    public void testWriteContextRoot() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        // Test writeValue() with no arguments
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertNull(context.getCurrentValue());

        // Test writeValue() with an argument
        context.setCurrentValue(42);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
        assertEquals(42, context.getCurrentValue());
    }

    @Test
    public void testWriteContextObject() throws JsonProcessingException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildObjectContext();

        context.writeFieldName("name");
        context.setCurrentValue("John Doe");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals("John Doe", context.getCurrentValue());
        assertEquals("{\"name\":\"John Doe\"}", context.toString());

        context.reset();
        context.writeFieldName("age");
        context.setCurrentValue(30);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(30, context.getCurrentValue());
        assertEquals("{\"name\":\"John Doe\",\"age\":30}", context.toString());

        context.reset();
        try {
            context.writeFieldName("name");
            context.writeFieldName("name"); // Duplicate name
            fail();
        } catch (JsonGenerationException e) {
            // Expected
        }
    }

    @Test
    public void testWriteContextArray() throws JsonProcessingException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();

        context.writeValue();
        assertNull(context.getCurrentValue());
        assertEquals("[", context.toString());

        context.writeValue("John Doe");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals("John Doe", context.getCurrentValue());
        assertEquals("[\"John Doe\"]", context.toString());

        context.writeValue("Jane Doe");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals("Jane Doe", context.getCurrentValue());
        assertEquals("[\"John Doe\",\"Jane Doe\"]", context.toString());

        context.writeValue(42);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeValue());
        assertNull(context.getCurrentValue());
        assertEquals("[\"John Doe\",\"Jane Doe\",]", context.toString());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteContextDuplicateField() throws JsonProcessingException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildObjectContext();

        context.writeFieldName("name");
        context.setCurrentValue("John Doe");
        context.writeFieldName("name"); // Duplicate field name
    }

    @Test
    public void testWriteContextReset() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createChildObjectContext();

        context.writeFieldName("name");
        context.setCurrentValue("John Doe");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals("John Doe", context.getCurrentValue());

        context.reset();
        assertNull(context.getCurrentValue());
        assertEquals("{}", context.toString());
    }
}