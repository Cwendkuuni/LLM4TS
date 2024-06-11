package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\"}}";
        JsonMappingException ex = null;
        try {
            mapper.readValue(json, PojoWithArray.class);
        } catch (JsonMappingException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertNotNull(ex.getPath());
        assertEquals(3, ex.getPath().size());
        assertEquals("New York", ex.getPath().get(2).getFieldName());
        assertEquals(4, ex.getPath().get(2)._index);
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException$Reference[New York]", ex.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithNullPath() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\"}";
        JsonMappingException ex = null;
        try {
            mapper.readValue(json, PojoWithNullPath.class);
        } catch (JsonMappingException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertNull(ex.getPath());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionThrown() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":null}}";
        mapper.readValue(json, PojoWithNullPath.class);
    }

    @Test
    public void testJsonMappingExceptionWithCustomMessage() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonMappingException ex = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals("Invalid state: IL", ex.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonParseException parseEx = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            JsonMappingException ex = (JsonMappingException) e;
            parseEx = ex.getOriginalException();
        }
        assertNotNull(parseEx);
        assertEquals("Unexpected character ('i' (code 105)): expected a valid value (number, string, array, object or token 'null') at [Source: (StringReader); line: 2, column: 11]", parseEx.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithCustomMessageAndRootCause() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonParseException parseEx = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            JsonMappingException ex = (JsonMappingException) e;
            parseEx = ex.getOriginalException();
            assertEquals("Invalid state: IL. Cause: Unexpected character ('i' (code 105)): expected a valid value (number, string, array, object or token 'null') at [Source: (StringReader); line: 2, column: 11]", ex.getMessage());
        }
        assertNotNull(parseEx);
        assertEquals("Unexpected character ('i' (code 105)): expected a valid value (number, string, array, object or token 'null') at [Source: (StringReader); line: 2, column: 11]", parseEx.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithCustomMessageAndRootCauseAndPath() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonParseException parseEx = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            JsonMappingException ex = (JsonMappingException) e;
            parseEx = ex.getOriginalException();
            List<Reference> path = ex.getPath();
            assertEquals("state", path.get(0).getFieldName());
            assertEquals("IL", path.get(0)._fieldName);
            assertNull(path.get(0).getIndex());
            assertEquals("PojoWithState$State[state=IL]", path.get(0).getDescription());
        }
        assertNotNull(parseEx);
        assertEquals("Unexpected character ('i' (code 105)): expected a valid value (number, string, array, object or token 'null') at [Source: (StringReader); line: 2, column: 11]", parseEx.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonMappingException ex = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertNotNull(ex.getProcessor());
        assertNotNull(ex.getLocation());
        assertEquals(0, ex.getLocation().getColumnNumber());
        assertEquals(1, ex.getLocation().getLineNumber());
        assertNull(ex.getLocation().getLineNumber());
        assertNull(ex.getLocation().getColumnNumber());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorAndPath() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonMappingException ex = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            ex = e;
        }
        assertNotNull(ex);
        Reference reference = new JsonMappingException.Reference(mapper, "PojoWithState", 0);
        ex.prependPath(reference);
        List<Reference> path = ex.getPath();
        assertEquals("PojoWithState", path.get(0).getFrom().getClass().getName());
        assertEquals(0, path.get(0).getIndex());
        assertEquals("PojoWithState$PojoWithState[]", path.get(0).getDescription());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorAndPathAndCustomMessage() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonMappingException ex = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            ex = e;
        }
        assertNotNull(ex);
        Reference reference = new JsonMappingException.Reference(mapper, "PojoWithState", 0);
        ex.prependPath(reference);
        assertEquals("Unexpected state: IL. Path:\nPojoWithState$PojoWithState[]\n", ex.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorAndPathAndCustomMessageAndRootCause() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":[1, 2, 3, 4], \"address\":{\"city\":\"New York\", \"state\":\"IL\"}}";
        JsonParseException parseEx = null;
        try {
            mapper.readValue(json, PojoWithState.class);
        } catch (JsonMappingException e) {
            JsonMappingException ex = (JsonMappingException) e;
            parseEx = ex.getOriginalException();
            Reference reference = new JsonMappingException.Reference(mapper, "PojoWithState", 0);
            ex.prependPath(reference);
            List<Reference> path = ex.getPath();
            assertEquals("state", path.get(0).getFieldName());
            assertEquals("IL", path.get(0)._fieldName);
            assertNull(path.get(0).getIndex());
            assertEquals("PojoWithState$State[state=IL]", path.get(0).getDescription());
        }
        assertNotNull(parseEx);
        assertEquals("Unexpected character ('i' (code 105)): expected a valid value (number, string, array, object or token 'null') at [Source: (StringReader); line: 2, column: 11]", parseEx.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOException() throws IOException {
        String json = "{\"name\":\"John\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException ex = JsonMappingException.fromUnexpectedIOE(new IOException("Unexpected IOException"));
        assertNotNull(ex);
        assertEquals("Unexpected IOException (of type java.io.IOException): Unexpected IOException", ex.getMessage());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOExceptionWithCustomMessage() throws IOException {
        String json = "{\"name\":\"John\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException ex = JsonMappingException.fromUnexpectedIOE(new IOException("Unexpected IOException: expected a valid JSON"));
        assertNotNull(ex);
        assertEquals("Unexpected IOException (of type java.io.IOException): expected a valid JSON", ex.getMessage());
    }

    static class PojoWithArray {
        private Address address;

        public static class Address {
            private String city;
            private String[] state;

            public String getCity() {
                return city;
            }

            public String[] getState() {
                return state;
            }
        }

        public Address getAddress() {
            return address;
        }

        public static class Address2 {
            private String city;
            private String state;

            public String getCity() {
                return city;
            }

            public String getState() {
                return state;
            }
        }
    }

    static class PojoWithNullPath {
        private String name;
        private String nullProperty;
    }

    static class PojoWithState {
        private String name;
        private State state;

        public static class State {
            private String name;
        }

        public String getName() {
            return name;
        }

        public State getState() {
            return state;
        }
    }
}