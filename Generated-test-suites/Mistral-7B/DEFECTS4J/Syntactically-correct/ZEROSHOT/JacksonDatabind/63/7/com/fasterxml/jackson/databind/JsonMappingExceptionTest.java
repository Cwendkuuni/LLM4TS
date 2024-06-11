package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProviderImpl;
import com.fasterxml.jackson.databind.ser.TestSerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.*;

/**
 * Test class for {@link JsonMappingException}.
 */
public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test default constructor
        JsonMappingException e1 = new JsonMappingException("Test message");
        assertEquals("Test message", e1.getMessage());
        assertEquals(null, e1.getPath());

        // Test constructor with JsonProcessingException
        JsonProcessingException e2 = new JsonProcessingException("Test message", null);
        JsonMappingException e3 = new JsonMappingException(e2);
        assertEquals("Test message", e3.getMessage());
        assertEquals(null, e3.getPath());

        // Test constructor with JsonProcessingException and path
        JsonProcessingException e4 = new JsonProcessingException("Test message", new JsonLocation(1, 1, "input.json"));
        JsonMappingException e5 = new JsonMappingException(e4, new Reference("input.json", 1));
        assertEquals("Test message (through reference chain: input.json)", e5.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", 1)), e5.getPath());

        // Test constructor with JsonParser and message
        JsonParser p = mapper.getFactory().createParser("{'a': []}");
        JsonMappingException e6 = new JsonMappingException(p, "Test message");
        assertEquals("Test message", e6.getMessage());
        assertEquals(null, e6.getPath());

        // Test constructor with JsonParser, message and path
        p.nextToken();
        JsonMappingException e7 = new JsonMappingException(p, "Test message", new Reference("input.json", "a"));
        assertEquals("Test message (through reference chain: input.json->a)", e7.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", "a")), e7.getPath());

        // Test constructor with JsonParser, message, path and root cause
        JsonMappingException e8 = new JsonMappingException(p, "Test message", new Reference("input.json", "a"), new IOException("Test IO exception"));
        assertEquals("Test message (through reference chain: input.json->a)", e8.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", "a")), e8.getPath());
        assertEquals("Test IO exception", e8.getCause().getMessage());

        // Test constructor with JsonParser, message, path, root cause and location
        JsonToken token = p.nextToken();
        JsonMappingException e9 = new JsonMappingException(p, "Test message", new Reference("input.json", "a"), new IOException("Test IO exception"), new JsonLocation(1, 1, "input.json"));
        assertEquals("Test message (through reference chain: input.json->a)", e9.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", "a")), e9.getPath());
        assertEquals("Test IO exception", e9.getCause().getMessage());
        assertEquals(1, e9.getLocation().getLineNumber());
        assertEquals(1, e9.getLocation().getColumnNumber());
        assertEquals("input.json", e9.getLocation().getFile());

        // Test constructor with JsonGenerator and message
        JsonMappingException e10 = new JsonMappingException(mapper.getFactory().createGenerator(), "Test message");
        assertEquals("Test message", e10.getMessage());
        assertEquals(null, e10.getPath());

        // Test constructor with JsonGenerator, message, path
        JsonMappingException e11 = new JsonMappingException(mapper.getFactory().createGenerator(), "Test message", new Reference("output.json", null));
        assertEquals("Test message (through reference chain: output.json)", e11.getMessage());
        assertEquals(Arrays.asList(new Reference("output.json", null)), e11.getPath());

        // Test constructor with JsonGenerator, message, path and root cause
        JsonMappingException e12 = new JsonMappingException(mapper.getFactory().createGenerator(), "Test message", new Reference("output.json", null), new IOException("Test IO exception"));
        assertEquals("Test message (through reference chain: output.json)", e12.getMessage());
        assertEquals(Arrays.asList(new Reference("output.json", null)), e12.getPath());
        assertEquals("Test IO exception", e12.getCause().getMessage());

        // Test constructor with DeserializationContext and message
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonMappingException e13 = new JsonMappingException(ctxt, "Test message");
        assertEquals("Test message", e13.getMessage());
        assertEquals(null, e13.getPath());

        // Test constructor with DeserializationContext, message, path
        JsonMappingException e14 = new JsonMappingException(ctxt, "Test message", new Reference("input.json", "a"));
        assertEquals("Test message (through reference chain: input.json->a)", e14.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", "a")), e14.getPath());

        // Test constructor with DeserializationContext, message, path and root cause
        AnnotatedClass ac = null;
        JsonDeserializer<String> deserializer = null;
        JsonMappingException e15 = new JsonMappingException(ctxt, "Test message", new Reference("input.json", "a"), new IOException("Test IO exception"), ac, deserializer);
        assertEquals("Test message (through reference chain: input.json->a)", e15.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", "a")), e15.getPath());
        assertEquals("Test IO exception", e15.getCause().getMessage());

        // Test constructor with SerializerProvider and message
        SerializerProvider sp = new TestSerializerProvider();
        JsonMappingException e16 = new JsonMappingException(sp, "Test message");
        assertEquals("Test message", e16.getMessage());
        assertEquals(null, e16.getPath());

        // Test constructor with SerializerProvider, message, and path
        TypeSerializer ts = mapper.getTypeSerializer();
        TypeDeserializer td = mapper.getTypeDeserializer();
        JsonMappingException e17 = new JsonMappingException(sp, "Test message", new Reference("output.json", null), ts, td);
        assertEquals("Test message (through reference chain: output.json)", e17.getMessage());
        assertEquals(Arrays.asList(new Reference("output.json", null)), e17.getPath());

        // Test constructor with SerializerProvider, message, path, root cause
        TypeSerializer ts2 = mapper.getTypeSerializer();
        TypeDeserializer td2 = mapper.getTypeDeserializer();
        JsonMappingException e18 = new JsonMappingException(sp, "Test message", new Reference("output.json", null), ts2, td2, new IOException("Test IO exception"));
        assertEquals("Test message (through reference chain: output.json)", e18.getMessage());
        assertEquals(Arrays.asList(new Reference("output.json", null)), e18.getPath());
        assertEquals("Test IO exception", e18.getCause().getMessage());

        // Test constructor with SerializerProvider, message, path, root cause and location
        SerializerProviderImpl spi = new SerializerProviderImpl();
        TsFactory tsFactory = mapper.getSerializerConfig().getFactory();
        JsonGenerator g = tsFactory.createGenerator(spi, new ByteArrayOutputStream(), null);
        g.setSerializer(td2);
        JsonMappingException e19 = new JsonMappingException(spi, "Test message", new Reference("output.json", null), ts2, td2, new IOException("Test IO exception"), null, g.getLocation());
        assertEquals("Test message (through reference chain: output.json)", e19.getMessage());
        assertEquals(Arrays.asList(new Reference("output.json", null)), e19.getPath());
        assertEquals("Test IO exception", e19.getCause().getMessage());

        // Test fromJsonMappingException
        JsonProcessingException ex = mapper.readValue("{'a': []}", String[].class);
        JsonMappingException ex1 = JsonMappingException.from(ex);
        assertEquals("Can not instantiate value of type [simple type, class java.lang.String] from JSON Text [[Chars: [83, 78, 68, 65, 84, 72, 65, 81, 74, 65, 72, 73, 73], Length: 13]", ex1.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", null)), ex1.getPath());

        // Test fromJsonMappingException with JsonParser
        JsonParser parser = mapper.getFactory().createParser("{'a': []}");
        JsonMappingException ex2 = JsonMappingException.from(parser, "Can not instantiate value of type [simple type, class java.lang.String] from JSON Text [[Chars: [83, 78, 68, 65, 84, 72, 65, 81, 74, 65, 72, 73, 73], Length: 13]");
        assertEquals("Can not instantiate value of type [simple type, class java.lang.String] from JSON Text [[Chars: [83, 78, 68, 65, 84, 72, 65, 81, 74, 65, 72, 73, 73], Length: 13]", ex2.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", null)), ex2.getPath());

        // Test fromJsonMappingException with JsonParser and root cause
        JsonParser parser2 = mapper.getFactory().createParser("{'a': []}");
        JsonProcessingException ex3 = parser2.parseValueAs(Map.class);
        JsonMappingException ex4 = JsonMappingException.from(parser2, "Can not instantiate value of type [simple type, class java.util.HashMap$Node] from JSON Text [[Chars: [83, 78, 68, 65, 84, 72, 65, 81, 74, 65, 72, 73, 73], Length: 13]", ex3);
        assertEquals("Can not instantiate value of type [simple type, class java.util.HashMap$Node] from JSON Text [[Chars: [83, 78, 68, 65, 84, 72, 65, 81, 74, 65, 72, 73, 73], Length: 13]", ex4.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", null)), ex4.getPath());
        assertEquals("Can not instantiate value of type [simple type, class java.util.HashMap$Node] from JSON Text [[Chars: [83, 78, 68, 65, 84, 72, 65, 81, 74, 65, 72, 73, 73], Length: 13]", ex4.getCause().getMessage());

        // Test fromJsonMappingException with JsonGenerator
        ObjectMapper mapper2 = new ObjectMapper();
        JsonGenerator gen = mapper2.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        JsonMappingException ex5 = JsonMappingException.from(gen, "Failed to serialize value of type java.util.HashMap$Node to String");
        assertEquals("Failed to serialize value of type java.util.HashMap$Node to String", ex5.getMessage());
        assertEquals(Arrays.asList(new Reference("output.json", null)), ex5.getPath());

        // Test fromJsonMappingException with JsonGenerator and root cause
        ObjectMapper mapper3 = new ObjectMapper();
        JsonGenerator gen2 = mapper3.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen2.setSerializerProvider(new TestSerializerProvider());
        JsonMappingException ex6 = JsonMappingException.from(gen2, "Failed to serialize value of type java.util.HashMap$Node to String", new IOException("Test IO exception"));
        assertEquals("Failed to serialize value of type java.util.HashMap$Node to String (through reference chain: output.json)", ex6.getMessage());
        assertEquals(Arrays.asList(new Reference("output.json", null)), ex6.getPath());
        assertEquals("Test IO exception", ex6.getCause().getMessage());

        // Test fromUnexpectedIOE
        IOException ioException = new IOException("Test IO exception");
        JsonMappingException ex7 = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type java.io.IOException): Test IO exception", ex7.getMessage());
        assertEquals(null, ex7.getPath());

        // Test wrapWithPath
        IOException ioException2 = new IOException("Test IO exception");
        JsonMappingException ex8 = JsonMappingException.wrapWithPath(ioException2, "input.json", "a");
        assertEquals("Test IO exception (through reference chain: input.json->a)", ex8.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", "a")), ex8.getPath());

        // Test wrapWithPath with index
        JsonMappingException ex9 = JsonMappingException.wrapWithPath(ioException2, "input.json", 0);
        assertEquals("Test IO exception (through reference chain: input.json[0])", ex9.getMessage());
        assertEquals(Arrays.asList(new Reference("input.json", 0)), ex9.getPath());
    }
}

class TestSerializerProvider extends SerializerProvider {
    @Override
    public void close() throws IOException {
        // Do nothing
    }
}