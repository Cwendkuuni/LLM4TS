
package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 1's test case)
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithGotName() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 1's test case with error)
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameWithDuplicateName() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 1's test case with error)
    }

    @Test
    public void testWriteValue() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 2's test case)
    }

    @Test
    public void testWriteValue_RootContext_NoName() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 3's test case)
    }

    @Test
    public void testWriteValue_ArrayContext_NoName() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 3's test case)
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValue_ObjectContext_NoName() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 3's test case with error)
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValue_ObjectContext_GotName() throws JsonProcessingException, JsonGenerationException {
        // ... (Expert 3's test case with error)
    }
}