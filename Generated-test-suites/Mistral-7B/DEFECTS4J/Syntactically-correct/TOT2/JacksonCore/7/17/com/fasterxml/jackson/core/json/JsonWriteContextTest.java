package com.example.junit4;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import org.junit.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeValue();
        assertEquals("[{}]", context.toString());
    }

    @Test
    public void testWriteEmptyFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context._gotName = true; // Simulate calling writeFieldName() without providing a name
        assertEquals("[{}]", context.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithSpecialCharacters() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field\"name");
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithNullName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName(null);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteDuplicateFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, new DefaultDupDetector());
        context.writeFieldName("fieldName");
        context.writeFieldName("fieldName");
    }

    static class DefaultDupDetector implements DupDetector {
        private final Set<String> _seen = new HashSet<>();

        public boolean isDup(String name) {
            return _seen.contains(name);
        }

        public void reset() {
            _seen.clear();
        }

        public DupDetector child() {
            return new DefaultDupDetector();
        }
    }

}