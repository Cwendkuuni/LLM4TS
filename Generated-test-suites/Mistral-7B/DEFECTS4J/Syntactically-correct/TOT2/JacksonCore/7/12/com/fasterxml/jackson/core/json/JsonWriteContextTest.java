package com.example.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;

public class JsonWriteContextTest {

    @Test
    public void testJsonWriteContextConstructor() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertNotNull(context);
    }

    @Test
    public void testWriteFieldNameWithExistingName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("name");
        JsonWriteContext dupDetector = new JsonWriteContext.DupDetector();
        context.withDupDetector(dupDetector);
        try {
            context.writeFieldName("name");
            fail();
        } catch (JsonProcessingException e) {
            // Expected exception
        }
    }
}