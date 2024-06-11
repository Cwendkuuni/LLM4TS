
package com.example.junit; // Replace with your test package name

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getParent());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(dupDetector, context.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector_DuplicateField() throws JsonProcessingException {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        context.writeFieldName("testField");
        context.writeFieldName("testField");
        fail("Expected JsonProcessingException to be thrown");
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("testField");
        assertEquals("testField", context.getCurrentName());
        assertTrue(context.isGotName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameTwice() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("testField");
        context.writeFieldName("testField");
    }

    @Test
    public void testWriteFieldNameEmptyString() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("");
        assertEquals("", context.getCurrentName());
        assertTrue(context.isGotName());
    }

    @Test
    public void testWriteFieldNameSpecialChars() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("testField@#$%^&*()_+={}[]|\\:\"<>,.?/~`");
        assertEquals("testField@#$%^&*()_+={}[]|\\:\"<>,.?/~`", context.getCurrentName());
        assertTrue(context.isGotName());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("testValue");
        context.writeValue();
        assertEquals("testValue", context.getCurrentValue());
    }

    @Test
    public void testWriteValueWithParent() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildArrayContext();
        child.writeValue();
        assertEquals(-1, parent.getCurrentIndex());
        assertEquals(0, child.getCurrentIndex());
    }

    @Test
    public void testWriteValueWithGotName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("testField");
        context.writeValue();
        assertFalse(context.isGotName());
    }

    @Test
    public void testWriteValueWithRootContextWithoutValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeValue();
        assertEquals(-1, context.getCurrentIndex());
    }

    @Test
    public void testWriteValueWithRootContextWithValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("testValue");
        context.writeValue();
        assertEquals("testValue", context.getCurrentValue());
        assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteValueWithArrayContextWithoutValue() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildArrayContext();
        child.writeValue();
        assertEquals(-1, parent.getCurrentIndex());
        assertEquals(-1, child.getCurrentIndex());
    }

    @Test
    public void testWriteValueWithArrayContextWithValue() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildArrayContext();
        child.setCurrentValue("testValue");
        child.writeValue();
        assertEquals(0, parent.getCurrentIndex());
        assertEquals(0, child.getCurrentIndex());
    }

    @Test
    public void testWriteValueWithObjectContextWithoutValue() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildObjectContext();
        child.writeValue();
        assertEquals(-1, parent.getCurrentIndex());
        assertNull(child.getCurrentValue());
    }

    @Test
    public void testWriteValueWithObjectContextWithValue() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildObjectContext();
        child.setCurrentValue(42);
        child.writeValue();
        assertEquals(-1, parent.getCurrentIndex());
        assertEquals(42, child.getCurrentValue());
    }
}
