package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_EXPECT_VALUE;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_EXPECT_NAME;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AFTER_COMMA;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AFTER_COLON;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AS_IS;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AFTER_SPACE;

public class JsonWriteContextTest {

    @Test
    public void testJsonWriteContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("[]", rootContext.toString());
        assertEquals(STATUS_OK_AS_IS, rootContext.writeValue());

        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        assertEquals("[", arrayContext.toString());
        assertEquals(STATUS_OK_AS_IS, arrayContext.writeValue());
        assertEquals(STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
        assertNull(arrayContext.getCurrentName());

        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        assertEquals("{\"root\":}", objectContext.toString());
        assertEquals(STATUS_OK_AS_IS, objectContext.writeValue());
        assertEquals(STATUS_OK_AFTER_COLON, objectContext.writeValue());
        assertEquals("root", objectContext.getCurrentName());
        assertNull(objectContext.getCurrentValue());

        JsonWriteContext objectContext2 = objectContext.createChildObjectContext();
        assertEquals("{\"root\":{\"child\":}", objectContext2.toString());
        assertEquals(STATUS_OK_AFTER_COLON, objectContext2.writeValue());
        assertEquals("child", objectContext2.getCurrentName());
        assertNull(objectContext2.getCurrentValue());

        JsonWriteContext arrayContext2 = arrayContext.createChildArrayContext();
        assertEquals("[0,1]", arrayContext2.toString());
        assertEquals(STATUS_OK_AFTER_COMMA, arrayContext2.writeValue());
        assertEquals(1, arrayContext2.writeValue());
        assertEquals(String.valueOf(1), arrayContext2.getCurrentValue().toString());

        try {
            JsonWriteContext objectContext3 = objectContext.createChildObjectContext();
            objectContext3.writeFieldName("duplicate");
            fail("Expected JsonGenerationException for duplicate field");
        } catch (JsonProcessingException e) {
            // Expected
        }

        JsonWriteContext objectContext3 = objectContext.createChildObjectContext();
        objectContext3.writeFieldName("name");
        assertEquals(STATUS_OK_AFTER_COLON, objectContext3.writeValue());
        assertEquals("name", objectContext3.getCurrentName());
        assertNull(objectContext3.getCurrentValue());

        JsonWriteContext arrayContext3 = arrayContext.createChildArrayContext();
        assertEquals("[0,1,2]", arrayContext3.toString());
        assertNull(arrayContext3.getCurrentName());
        assertNull(arrayContext3.getCurrentValue());

        objectContext3.setCurrentValue("value");
        assertEquals("{\"root\":{\"child\":\"value\"}}", objectContext3.toString());

        JsonWriteContext objectContext4 = objectContext3.createChildObjectContext();
        objectContext4.writeFieldName("innerName");
        assertEquals(STATUS_OK_AFTER_COLON, objectContext4.writeValue());
        assertEquals("innerName", objectContext4.getCurrentName());
        assertEquals("value", objectContext3.getCurrentValue());
    }
}