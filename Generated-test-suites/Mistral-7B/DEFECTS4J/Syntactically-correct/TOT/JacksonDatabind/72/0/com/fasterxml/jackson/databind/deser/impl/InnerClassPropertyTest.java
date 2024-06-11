package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;

/**
 * Test class for InnerClassProperty.
 */
public class InnerClassPropertyTest {

    static class InnerClass {
        @JsonDeserialize(using = InnerClassProperty.InnerClassDeserializer.class)
        public Inner inner;
        
        public String outer;
        
        static class Inner {
            private final String name;

            public Inner(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        
            public static class InnerClassDeserializer extends InnerClassProperty.InnerClassPropertyDeserializer {
                InnerClassDeserializer() {
                    super(Inner.class);
                }
            }
        }
    }

    @org.junit.Test
    public void testDeserializeInnerClassProperty() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("outer", "testOuter");
        rootNode.put("inner.name", "testInnerName");

        InnerClass expected = new InnerClass();
        expected.outer = "testOuter";
        expected.inner = new Inner("testInnerName");

        JsonParser jsonParser = jsonFactory.createParser(rootNode);

        InnerClass result = objectMapper.readValue(jsonParser, InnerClass.class);

        assertNotNull(result);
        assertEquals(expected.outer, result.outer);
        assertEquals(expected.inner.getName(), result.inner.getName());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDeserializeMissingNonStaticConstructor() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("outer", "testOuter");
        rootNode.put("inner.name", "testInnerName");

        InnerClassProperty.InnerClassPropertyDeserializer innerClassPropertyDeserializer = new InnerClassProperty.InnerClassPropertyDeserializer(InnerClass.Inner.class) {
            @Override
            protected AnnotatedConstructor _annotated;

            @Override
            public AnnotatedConstructor getAnnotated() {
                return null;
            }
        };

        JsonDeserializer<?> innerClassDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                throw new IOException("Simulated missing constructor error");
            }

            @Override
            public AnnotatedConstructor getAnnotated() {
                return innerClassPropertyDeserializer;
            }
        };

        ObjectMapper objectMapperWithDeserializer = new ObjectMapper();
        objectMapperWithDeserializer.registerDeserializer(InnerClass.Inner.class, innerClassDeserializer);

        ObjectNode rootNodeWithDeserializer = objectMapperWithDeserializer.createObjectNode();
        rootNodeWithDeserializer.put("outer", "testOuter");
        rootNodeWithDeserializer.put("inner.name", "testInnerName");

        JsonParser jsonParserWithDeserializer = jsonFactory.createParser(rootNodeWithDeserializer);

        InnerClass result = objectMapperWithDeserializer.readValue(jsonParserWithDeserializer, InnerClass.class);

        fail("Expected IllegalArgumentException but got InnerClass instance instead");
    }

    @org.junit.Test
    public void testSerializeAndDeserialize() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapperWithToStringSerializer = new ObjectMapper();
        objectMapperWithToStringSerializer.registerSerializer(InnerClass.Inner.class, new ToStringSerializer(InnerClass.Inner.class));

        InnerClass input = new InnerClass();
        input.outer = "testOuter";
        input.inner = new Inner("testInnerName");

        ObjectNode rootNode = objectMapper.writeValueAsObjectNode(input);

        InnerClass expected = new InnerClass();
        expected.outer = "testOuter";
        expected.inner = new Inner("testInnerName");

        InnerClass result = objectMapper.readValue(rootNode, InnerClass.class);

        assertNotNull(result);
        assertEquals(expected.outer, result.outer);
        assertEquals(expected.inner.getName(), result.inner.getName());

        ObjectNode rootNodeWithToStringSerializer = objectMapperWithToStringSerializer.writeValueAsObjectNode(input);
        InnerClass resultWithToStringSerializer = objectMapperWithToStringSerializer.readValue(rootNodeWithToStringSerializer, InnerClass.class);

        assertNotNull(resultWithToStringSerializer);
        assertEquals(expected.outer, resultWithToStringSerializer.outer);
        assertEquals(expected.inner.getName(), resultWithToStringSerializer.inner.getName());
        assertEquals(input.toString(), resultWithToStringSerializer.toString());
    }

    @org.junit.Test
    public void testDeserializeWithValueDeserializer() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer.Instance();
        JsonDeserializer<InnerClass.Inner> innerDeserializer = new JsonDeserializer<InnerClass.Inner>() {
            @Override
            public InnerClass.Inner deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new InnerClass.Inner(stringDeserializer.deserialize(jp, ctxt));
            }
        };

        ObjectMapper objectMapperWithDeserializer = new ObjectMapper();
        objectMapperWithDeserializer.registerDeserializer(String.class, stringDeserializer);
        objectMapperWithDeserializer.registerDeserializer(InnerClass.Inner.class, innerDeserializer);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("outer", "testOuter");
        rootNode.put("inner.name", "testInnerName");

        JsonParser jsonParser = jsonFactory.createParser(rootNode);

        InnerClass result = objectMapperWithDeserializer.readValue(jsonParser, InnerClass.class);

        assertNotNull(result);
        assertEquals("testInnerName", result.inner.getName());
    }

    @org.junit.Test
    public void testReadResolve() {
        ObjectMapper objectMapper = new ObjectMapper();

        InnerClass input = new InnerClass();
        input.outer = "testOuter";
        input.inner = new Inner("testInnerName");

        ObjectNode rootNode = objectMapper.writeValueAsObjectNode(input);

        ObjectNode rootNodeWithObjectIdReader = objectMapper.readTree(rootNode);
        ObjectIdReader objectIdReader = objectMapper.readerForMapIds();
        ObjectNode rootNodeWithObjectIdReaderWrapped = objectIdReader.readValue(rootNodeWithObjectIdReader);

        InnerClass result = (InnerClass) rootNodeWithObjectIdReaderWrapped.readResolver().readAllFields(null, InnerClass.class);

        assertNotNull(result);
        assertEquals(input.outer, result.outer);
        assertEquals(input.inner.getName(), result.inner.getName());
    }
}