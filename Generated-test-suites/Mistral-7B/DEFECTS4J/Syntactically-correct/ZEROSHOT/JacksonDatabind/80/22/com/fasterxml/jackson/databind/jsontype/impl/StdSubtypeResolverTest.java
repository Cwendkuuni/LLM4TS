package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.util.AnnotationIntrospectorUtil;

/**
 * Unit tests for {@link StdSubtypeResolver} class.
 */
public class StdSubtypeResolverTest {

    @Test
    public void testStdSubtypeResolver() throws Exception {
        // Test empty registration
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Default(),
                null, new ClassType(Object.class));
        assertEquals(0, subtypes.size());

        // Test registration of single subtype
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Foo.class));
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Default(),
                null, new ClassType(Object.class));
        assertEquals(1, subtypes.size());
        assertEquals(Foo.class, subtypes.get(0).getType().getRawClass());

        // Test registration of multiple subtypes
        resolver.registerSubtypes(new NamedType(Bar.class), new NamedType(Baz.class));
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Default(),
                null, new ClassType(Object.class));
        assertEquals(3, subtypes.size());
        assertEquals(Foo.class, subtypes.get(0).getType().getRawClass());
        assertEquals(Bar.class, subtypes.get(1).getType().getRawClass());
        assertEquals(Baz.class, subtypes.get(2).getType().getRawClass());

        // Test registration by class
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Foo.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Default(),
                null, new ClassType(Object.class));
        assertEquals(1, subtypes.size());
        assertEquals(Foo.class, subtypes.get(0).getType().getRawClass());

        // Test registration with name
        resolver.registerSubtypes(new NamedType(Baz.class, "baz"));
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Default(),
                null, new ClassType(Object.class));
        assertEquals(3, subtypes.size());
        assertEquals(Foo.class, subtypes.get(0).getType().getRawClass());
        assertEquals(Bar.class, subtypes.get(1).getType().getRawClass());
        assertEquals(Baz.class, subtypes.get(2).getType().getRawClass());
        assertEquals("baz", subtypes.get(2).getName());

        // Test resolution by class (serialization)
        resolver = new StdSubtypeResolver();
        registerSubtypes(resolver, Foo.class, Bar.class, Baz.class);
        ObjectWriter writer = new ObjectMapper().writerWithTypeResolver(resolver);
        String input = "{\"@type\":\"Foo\"}";
        JsonNode node = writer.writeValueAsTree(input);
        Foo foo = writer.readValue(node, Foo.class);
        assertEquals("Foo", foo.getName());

        input = "{\"@type\":\"Bar\"}";
        node = writer.writeValueAsTree(input);
        Bar bar = writer.readValue(node, Bar.class);
        assertEquals("Bar", bar.getName());

        input = "{\"@type\":\"Baz\"}";
        node = writer.writeValueAsTree(input);
        Baz baz = writer.readValue(node, Baz.class);
        assertEquals("Baz", baz.getName());

        // Test resolution by class (deserialization)
        resolver = new StdSubtypeResolver();
        registerSubtypes(resolver, Foo.class, Bar.class, Baz.class);
        ObjectReader reader = new ObjectMapper().readerWithTypeResolver(resolver);
        JsonNode node1 = reader.readTree("{\"@type\":\"Foo\"}");
        Foo foo1 = reader.readValue(node1);
        assertEquals("Foo", foo1.getName());

        JsonNode node2 = reader.readTree("{\"@type\":\"Bar\"}");
        Bar bar1 = reader.readValue(node2);
        assertEquals("Bar", bar1.getName());

        JsonNode node3 = reader.readTree("{\"@type\":\"Baz\"}");
        Baz baz1 = reader.readValue(node3);
        assertEquals("Baz", baz1.getName());

        // Test resolution by type id (serialization)
        resolver = new StdSubtypeResolver();
        registerSubtypes(resolver, Foo.class, Bar.class, Baz.class);
        ObjectWriter writer1 = new ObjectMapper().writerWithTypeResolver(resolver);
        String input1 = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.Foo\"}";
        JsonNode node4 = writer1.writeValueAsTree(input1);
        Foo foo2 = writer1.readValue(node4, Foo.class);
        assertEquals("Foo", foo2.getName());

        String input2 = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.Bar\"}";
        JsonNode node5 = writer1.writeValueAsTree(input2);
        Bar bar2 = writer1.readValue(node5, Bar.class);
        assertEquals("Bar", bar2.getName());

        String input3 = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.Baz\"}";
        JsonNode node6 = writer1.writeValueAsTree(input3);
        Baz baz2 = writer1.readValue(node6, Baz.class);
        assertEquals("Baz", baz2.getName());

        // Test resolution by type id (deserialization)
        resolver = new StdSubtypeResolver();
        registerSubtypes(resolver, Foo.class, Bar.class, Baz.class);
        ObjectReader reader1 = new ObjectMapper().readerWithTypeResolver(resolver);
        JsonNode node7 = reader1.readTree("{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.Foo\"}");
        Foo foo3 = reader1.readValue(node7, Foo.class);
        assertEquals("Foo", foo3.getName());

        JsonNode node8 = reader1.readTree("{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.Bar\"}");
        Bar bar3 = reader1.readValue(node8, Bar.class);
        assertEquals("Bar", bar3.getName());

        JsonNode node9 = reader1.readTree("{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.Baz\"}");
        Baz baz3 = reader1.readValue(node9, Baz.class);
        assertEquals("Baz", baz3.getName());

        // Test resolution with ambiguous type
        resolver = new StdSubtypeResolver();
        registerSubtypes(resolver, Foo.class, Bar.class, Baz.class);
        ObjectWriter writer2 = new ObjectMapper().writerWithTypeResolver(resolver);
        String input4 = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.UnknownType\"}";
        JsonNode node10 = writer2.writeValueAsTree(input4);
        try {
            UnknownType unknown = writer2.readValue(node10, UnknownType.class);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Unknown type [UnknownType] (not a subtype)", e.getMessage());
        }

        // Test resolution with non-existent type
        String input5 = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.NonExistentType\"}";
        JsonNode node11 = writer2.writeValueAsTree(input5);
        try {
            NonExistentType nonExistent = writer2.readValue(node11, NonExistentType.class);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Unknown type [com.fasterxml.jackson.databind.jsontype.impl.NonExistentType] (not known to TypesMapper)",
                    e.getMessage());
        }
    }

    // Helper method for registration of subtypes with different packages
    private void registerSubtypes(StdSubtypeResolver resolver, Class<?>... types) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        resolver.registerSubtypes(introspector.findSerializationConfig(types[0]));
        resolver.registerSubtypes(introspector.findDeserializationConfig(types[0]));
    }

    static class Foo {
        String name;

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type")
        static class CustomSerializer extends JsonSerializer<Foo> {}

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type")
        static class CustomDeserializer extends JsonDeserializer<Foo> {}
    }

    static class Bar extends Foo {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY)
        static class CustomSerializer extends JsonSerializer<Bar> {}

        static class CustomDeserializer extends JsonDeserializer<Bar> {
            @Override
            public Bar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Bar bar = new Bar();
                bar.name = p.getValueAsString();
                return bar;
            }
        }
    }

    static class Baz extends Foo {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type")
        static class CustomSerializer extends JsonSerializer<Baz> {}

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY)
        static class CustomDeserializer extends JsonDeserializer<Baz> {}
    }

    static class UnknownType extends Foo {}

    static class NonExistentType {}
}