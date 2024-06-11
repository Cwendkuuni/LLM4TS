package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import org.mockito.Mockito;

public class JsonWriteContextTest {

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);

        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testWriteValue_arrayContext() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);

        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValue_objectContext() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValue_outOfBoundsIndex() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);

        for (int i = 0; i < JsonWriteContext.STATUS_OK_AFTER_COMMA; ++i) {
            context.writeValue();
        }

        context.writeValue();
    }

    @Test
    public void testWriteValue_checkDupException() throws JsonProcessingException {
        DupDetector dupDetector = mock(DupDetector.class);
        when(dupDetector.isDup("test")).thenReturn(true);

        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);

        try {
            context.writeValue();
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected exception
        }
    }
}