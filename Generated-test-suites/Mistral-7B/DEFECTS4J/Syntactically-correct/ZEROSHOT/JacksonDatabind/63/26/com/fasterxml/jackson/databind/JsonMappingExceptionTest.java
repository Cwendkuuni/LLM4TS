package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Tests for {@link JsonMappingException}.
 */
public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws IOException {
        // Test default constructor
        JsonMappingException e = new JsonMappingException("Error message");
        Assert.assertEquals("Error message", e.getMessage());
        Assert.assertNull(e.getFrom());
        Assert.assertEquals(JsonLocation.ClassicLocation.ROOT, e.getLocation());

        // Test constructor with location
        JsonLocation location = new JsonLocation(1, 1, "source.json", 10);
        e = new JsonMappingException("Error message", location);
        Assert.assertEquals("Error message", e.getMessage());
        Assert.assertEquals(location, e.getLocation());
        Assert.assertNull(e.getFrom());

        // Test constructor with root cause
        IOException ioException = new IOException("IO error");
        e = new JsonMappingException("Error message", ioException);
        Assert.assertEquals("Error message", e.getMessage());
        Assert.assertEquals(ioException, e.getCause());

        // Test constructor with location and root cause
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Processing error", location, ioException);
        e = new JsonMappingException("Error message", location, jsonProcessingException);
        Assert.assertEquals("Error message", e.getMessage());
        Assert.assertEquals(location, e.getLocation());
        Assert.assertEquals(ioException, e.getCause());

        // Test constructor with processor
        JsonParser jsonParser = createMockJsonParser();
        e = new JsonMappingException(jsonParser, "Error message");
        Assert.assertEquals(JsonMappingException.class, e.getClass());
        Assert.assertEquals(jsonParser, e.getProcessor());
        Assert.assertNull(e.getLocation());
        Assert.assertNull(e.getCause());

        // Test getPath
        JsonMappingException e1 = new JsonMappingException(jsonParser, "Error message");
        JsonMappingException e2 = new JsonMappingException(jsonParser, "Error message", new Object());
        List<Object> references = Arrays.asList(new Object[]{new Object(), new Object()}, new int[]{1, 2}, new Object[]{});
        for (Object reference : references) {
            JsonMappingException e3 = new JsonMappingException(jsonParser, "Error message", reference);
            e1.prependPath(reference);
            e2.prependPath(reference);
            e3.prependPath(reference);
            Assert.assertEquals(Arrays.asList(e3.getPath()), Arrays.asList(e1.getPath(), e2.getPath()));
        }

        // Test getPathReference
        StringBuilder expected = new StringBuilder();
        Assert.assertEquals(expected.toString(), e1.getPathReference());
        expected.append("(through reference chain: [");
        expected.append(e1.getPath().get(0).toString());
        for (int i = 1; i < e1.getPath().size(); i++) {
            expected.append("->");
            expected.append(e1.getPath().get(i).toString());
        }
        expected.append("])");
        Assert.assertEquals(expected.toString(), e1.getPathReference());

        // Test toString
        Assert.assertEquals(JsonMappingException.class.getName() + ": Error message", e1.toString());

        // Test writeReplace
        ObjectWriter objectWriter = createMockObjectMapper().writer();
        ObjectWriter writer = new ObjectWriterDelegator(objectWriter);
        Assert.assertEquals(e1, writer.writeValueAsString(e1));

        // Test from unexpected IOE
        IOException ioException2 = new IOException("Unexpected IOE");
        JsonMappingException e4 = JsonMappingException.fromUnexpectedIOE(ioException2);
        Assert.assertEquals("Unexpected IOException (of type java.io.IOException): Unexpected IOE", e4.getMessage());

        // Test wrapWithPath
        Object referrer = new Object();
        Object referrer2 = new Object();
        Reference reference = new Reference(referrer, "fieldName");
        Reference reference2 = new Reference(referrer2, 1);
        JsonMappingException e5a = JsonMappingException.wrapWithPath(null, referrer, "fieldName");
        JsonMappingException e5b = JsonMappingException.wrapWithPath(null, referrer2, 1);
        JsonMappingException e5c = JsonMappingException.wrapWithPath(ioException2, referrer, "fieldName");
        JsonMappingException e5d = JsonMappingException.wrapWithPath(ioException2, referrer2, 1);
        JsonMappingException e5e = JsonMappingException.wrapWithPath(ioException2, reference);
        JsonMappingException e5f = JsonMappingException.wrapWithPath(ioException2, reference2);
        Assert.assertEquals(Arrays.asList(e5a.getPath()), Arrays.asList(reference));
        Assert.assertEquals(Arrays.asList(e5b.getPath()), Arrays.asList(reference2));
        Assert.assertEquals(Arrays.asList(e5c.getPath()), Arrays.asList(reference, referrer));
        Assert.assertEquals(Arrays.asList(e5d.getPath()), Arrays.asList(reference2, referrer2));
        Assert.assertEquals(Arrays.asList(e5e.getPath()), Arrays.asList(reference));
        Assert.assertEquals(Arrays.asList(e5f.getPath()), Arrays.asList(reference2));
    }

    private JsonParser createMockJsonParser() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("");
        when(parser.getCurrentToken()).thenReturn(null);
        when(parser.getLocation()).thenReturn(new JsonLocation(1, 1, "source.json", 10));
        return parser;
    }

    private ObjectMapper createMockObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter objectWriter = mapper.writer();
        return mapper;
    }

    private class ObjectWriterDelegator implements ObjectWriter {
        private final ObjectWriter delegate;

        public ObjectWriterDelegator(ObjectWriter delegate) {
            this.delegate = delegate;
        }

        @Override
        public void writeValue(Writer writer, Object value) throws IOException {
            delegate.writeValue(writer, value);
        }

        @Override
        public void writeValueAsString(Object value) throws IOException {
            return delegate.writeValueAsString(value);
        }

        @Override
        public void writeValueAsBytes(Object value) throws IOException {
            return delegate.writeValueAsBytes(value);
        }

        @Override
        public NarrowObjectWriter narrowWriterFor(Class<?> valueType) {
            return delegate.narrowWriterFor(valueType);
        }

        @Override
        public ObjectWriter withDefaultPreValues() {
            return delegate.withDefaultPreValues();
        }

        @Override
        public ObjectWriter withView(JsonView jsonView) {
            return delegate.withView(jsonView);
        }

        @Override
        public ObjectWriter writeValueAs(Class<?> valueType) throws JsonProcessingException {
            return delegate.writeValueAs(valueType);
        }

        @Override
        public ObjectWriter writeValueAs(Class<?> valueType, JsonGenerator gen) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(valueType, gen);
        }

        @Override
        public ObjectWriter writeValueAs(Class<?> valueType, JsonGenerator gen, JsonSerializer<Object> valueSerializer) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(valueType, gen, valueSerializer);
        }

        @Override
        public ObjectWriter writeValueAs(JsonFormat.Value valueFormat) throws JsonProcessingException {
            return delegate.writeValueAs(valueFormat);
        }

        @Override
        public ObjectWriter writeValueAs(JsonFormat.Value valueFormat, JsonGenerator gen) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(valueFormat, gen);
        }

        @Override
        public ObjectWriter writeValueAs(JsonFormat.Value valueFormat, JsonGenerator gen, JsonSerializer<Object> valueSerializer) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(valueFormat, gen, valueSerializer);
        }

        @Override
        public ObjectWriter writeValueAs(JsonFormat jsonFormat) throws JsonProcessingException {
            return delegate.writeValueAs(jsonFormat);
        }

        @Override
        public ObjectWriter writeValueAs(JsonFormat jsonFormat, JsonGenerator gen) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(jsonFormat, gen);
        }

        @Override
        public ObjectWriter writeValueAs(JsonFormat jsonFormat, JsonGenerator gen, JsonSerializer<Object> valueSerializer) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(jsonFormat, gen, valueSerializer);
        }

        @Override
        public ObjectWriter writeValueAs(Object value, JsonFormat jsonFormat) throws JsonProcessingException {
            return delegate.writeValueAs(value, jsonFormat);
        }

        @Override
        public ObjectWriter writeValueAs(Object value, JsonFormat jsonFormat, JsonSerializer<Object> valueSerializer) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(value, jsonFormat, valueSerializer);
        }

        @Override
        public ObjectWriter writeValueAs(Object value, JsonFormat jsonFormat, JsonSerializer<Object> valueSerializer, ObjectWriter serializers) throws JsonMappingException, JsonProcessingException {
            return delegate.writeValueAs(value, jsonFormat, valueSerializer, serializers);
        }
    }
}