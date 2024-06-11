package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.ObjectMapper;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.*;

public class DefaultDeserializationContextTest extends BaseTest {

    @SuppressWarnings("unchecked")
    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with null id and simple ObjectIdGenerator
        JsonDeserializer<SimpleClass> simpleClassDeserializer = mapper.deserializerForClass(SimpleClass.class);
        assertNull(findObjectId(null, mapper.getConfig(), simpleClassDeserializer.getObjectIdGenerator()));

        // Test with non-null id and simple ObjectIdGenerator
        SimpleClass simpleObject = new SimpleClass("simpleObject");
        ObjectIdGenerator<SimpleClass> idGenerator = new SimpleObjectIdGenerator();
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        ReadableObjectId id = mapper.getDeserializationContext().findObjectId(simpleObject, idGenerator, idResolver);
        assertEquals(IdKey.ROOT, id.getKey());
        assertEquals(simpleObject, id.getObjectId());
        assertEquals(idResolver, id.getResolver());

        // Test with non-null id and complex ObjectIdGenerator
        ComplexClass complexObject = new ComplexClass("complexObject");
        ObjectIdGenerator<ComplexClass> complexIdGenerator = new ComplexObjectIdGenerator();
        ObjectIdResolver complexIdResolver = new ComplexObjectIdResolver();
        id = mapper.getDeserializationContext().findObjectId(complexObject, complexIdGenerator, complexIdResolver);
        assertEquals(IdKey.ROOT, id.getKey());
        assertEquals(complexObject, id.getObjectId());
        assertEquals(complexIdResolver, id.getResolver());

        // Test with non-null id and ObjectIdResolver
        ComplexClass complexObject2 = new ComplexClass("complexObject2");
        ObjectIdGenerator<ComplexClass> complexIdGenerator2 = new ComplexObjectIdGenerator();
        ObjectIdResolver complexIdResolver2 = new ComplexObjectIdResolver();
        id = mapper.getDeserializationContext().findObjectId(complexObject2, complexIdGenerator2);
        assertNull(id);

        // Test with non-null id and ObjectIdResolver of the same type
        ComplexClass complexObject3 = new ComplexClass("complexObject3");
        ObjectIdGenerator<ComplexClass> complexIdGenerator3 = new ComplexObjectIdGenerator();
        ObjectIdResolver complexIdResolver3 = complexIdResolver2;
        id = mapper.getDeserializationContext().findObjectId(complexObject3, complexIdGenerator3);
        assertNull(id);

        // Test with non-null id and ObjectIdResolver of a different type
        ObjectIdGenerator<ComplexClass> complexIdGenerator4 = new ComplexObjectIdGenerator();
        ObjectIdResolver complexIdResolver4 = new SimpleObjectIdResolver();
        id = mapper.getDeserializationContext().findObjectId(complexObject3, complexIdGenerator4, complexIdResolver4);
        assertNull(id);

        // Test with non-null id and ObjectIdResolver that cannot use it
        ObjectIdGenerator<ComplexClass> complexIdGenerator5 = new ComplexObjectIdGenerator();
        ObjectIdResolver complexIdResolver5 = new SimpleObjectIdResolver();
        id = mapper.getDeserializationContext().findObjectId(complexObject3, complexIdGenerator5, complexIdResolver5);
        assertNull(id);
    }

    @SuppressWarnings("unchecked")
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with no object ids
        mapper.getDeserializationContext().checkUnresolvedObjectId();

        // Test with no unresolved object ids
        SimpleClass simpleObject = new SimpleClass("simpleObject");
        ObjectIdGenerator<SimpleClass> idGenerator = new SimpleObjectIdGenerator();
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        mapper.getDeserializationContext().findObjectId(simpleObject, idGenerator, idResolver);
        mapper.getDeserializationContext().checkUnresolvedObjectId();

        // Test with unresolved object ids
        SimpleClass simpleObject2 = new SimpleClass("simpleObject2");
        ObjectIdGenerator<SimpleClass> idGenerator2 = new SimpleObjectIdGenerator();
        ObjectIdResolver idResolver2 = new SimpleObjectIdResolver();
        mapper.getDeserializationContext().findObjectId(simpleObject2, idGenerator2, idResolver2);
        Map<IdKey, ReadableObjectId> objectIds = new HashMap<>();
        objectIds.put(IdKey.ROOT, new ReadableObjectId(IdKey.ROOT, simpleObject2));
        mapper.getDeserializationContext()._objectIds = objectIds;
        mapper.getDeserializationContext().checkUnresolvedObjectId();

        try {
            mapper.getDeserializationContext().checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertNotNull(e.getId());
            assertEquals("Unresolved forward references for: ", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with null deserializer definition
        JsonDeserializer<SimpleClass> simpleClassDeserializer = mapper.deserializerForClass(SimpleClass.class);
        JsonDeserializer<?> deserializer = mapper.getDeserializationContext().deserializerInstance(null, simpleClassDeserializer);
        assertNull(deserializer);

        // Test with valid deserializer definition - class
        JsonDeserializer<SimpleClass> simpleClassDeserializer2 = mapper.deserializerForClass(SimpleClass.class);
        Class<JsonDeserializer<SimpleClass>> deserializerClass = JsonDeserializer.class;
        JsonDeserializer<?> deserializer2 = mapper.getDeserializationContext().deserializerInstance(null, deserializerClass);
        assertNotNull(deserializer2);
        assertEquals(simpleClassDeserializer2, deserializer2);

        // Test with valid deserializer definition - instance
        JsonDeserializer<SimpleClass> simpleClassDeserializer3 = mapper.deserializerForClass(SimpleClass.class);
        JsonDeserializer<SimpleClass> simpleClassDeserializer4 = new SimpleClassDeserializer();
        deserializer = mapper.getDeserializationContext().deserializerInstance(new AnnotatedClass(SimpleClass.class), simpleClassDeserializer4);
        assertNotNull(deserializer);
        assertEquals(simpleClassDeserializer4, deserializer);

        // Test with invalid deserializer definition - not a JsonDeserializer
        JsonDeserializer<SimpleClass> simpleClassDeserializer5 = mapper.deserializerForClass(SimpleClass.class);
        Class<Converter<String, Integer>> converterClass = Converter.class;
        deserializer = mapper.getDeserializationContext().deserializerInstance(new AnnotatedClass(SimpleClass.class), converterClass);
        assertNull(deserializer);

        // Test with invalid deserializer definition - not a class or instance
        JsonDeserializer<SimpleClass> simpleClassDeserializer6 = mapper.deserializerForClass(SimpleClass.class);
        Object deserializerDefinition = new Object();
        deserializer = mapper.getDeserializationContext().deserializerInstance(new AnnotatedClass(SimpleClass.class), deserializerDefinition);
        assertNull(deserializer);
    }

    @SuppressWarnings("unchecked")
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with null deserializer definition
        KeyDeserializer keyDeserializer = mapper.keyDeserializerForPropertyName("name");
        KeyDeserializer deserializer = mapper.getDeserializationContext().keyDeserializerInstance(null, keyDeserializer);
        assertNull(deserializer);

        // Test with valid deserializer definition - class
        KeyDeserializer keyDeserializer2 = mapper.keyDeserializerForPropertyName("name");
        Class<KeyDeserializer> deserializerClass = KeyDeserializer.class;
        KeyDeserializer deserializer2 = mapper.getDeserializationContext().keyDeserializerInstance(null, deserializerClass);
        assertNotNull(deserializer2);
        assertEquals(keyDeserializer2, deserializer2);

        // Test with valid deserializer definition - instance
        KeyDeserializer keyDeserializer3 = mapper.keyDeserializerForPropertyName("name");
        KeyDeserializer keyDeserializer4 = new NameKeyDeserializer();
        deserializer = mapper.getDeserializationContext().keyDeserializerInstance(new AnnotatedClass(SimpleClass.class), keyDeserializer4);
        assertNotNull(deserializer);
        assertEquals(keyDeserializer4, deserializer);

        // Test with invalid deserializer definition - not a KeyDeserializer
        KeyDeserializer keyDeserializer5 = mapper.keyDeserializerForPropertyName("name");
        Class<Converter<String, Integer>> converterClass = Converter.class;
        deserializer = mapper.getDeserializationContext().keyDeserializerInstance(new AnnotatedClass(SimpleClass.class), converterClass);
        assertNull(deserializer);

        // Test with invalid deserializer definition - not a class or instance
        KeyDeserializer keyDeserializer6 = mapper.keyDeserializerForPropertyName("name");
        Object deserializerDefinition = new Object();
        deserializer = mapper.getDeserializationContext().keyDeserializerInstance(new AnnotatedClass(SimpleClass.class), deserializerDefinition);
        assertNull(deserializer);
    }

    static class SimpleClass {
        @JsonIdentityInfo(generator = ObjectIdGenerators.None.class)
        private String name;

        public SimpleClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        @JsonProperty
        public void setName(String name) {
            this.name = name;
        }
    }

    static class SimpleObjectIdGenerator implements ObjectIdGenerator<SimpleClass> {
        @Override
        public ObjectIdGenerator.IdKey key(SimpleClass id) {
            return IdKey.ROOT;
        }

        @Override
        public String idFromObject(SimpleClass obj) {
            return obj.toString();
        }
    }

    static class SimpleObjectIdResolver extends SimpleObjectIdResolver {
        @Override
        public Object resolveId(ObjectIdGenerator.IdKey key, Object id) {
            if (key == IdKey.ROOT) {
                return new SimpleClass("root");
            }
            throw new IllegalArgumentException("Unknown key: " + key);
        }
    }

    static class ComplexClass {
        @JsonIdentityInfo(generator = ObjectIdGenerators.None.class)
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        @JsonIgnore
        private String name;
        private int id;

        public ComplexClass(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return String.format("%s-%d", name, id);
        }

        @JsonProperty
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty
        public void setId(int id) {
            this.id = id;
        }
    }

    static class ComplexObjectIdGenerator implements ObjectIdGenerator<ComplexClass> {
        @Override
        public ObjectIdGenerator.IdKey key(ComplexClass id) {
            return IdKey.ROOT;
        }

        @Override
        public String idFromObject(ComplexClass obj) {
            return obj.toString();
        }
    }

    static class ComplexObjectIdResolver extends SimpleObjectIdResolver {
        @Override
        public Object resolveId(ObjectIdGenerator.IdKey key, Object id) {
            if (key == IdKey.ROOT) {
                return new ComplexClass("root", 0);
            }
            throw new IllegalArgumentException("Unknown key: " + key);
        }
    }

    static class SimpleClassDeserializer extends JsonDeserializer<SimpleClass> {
        @Override
        public SimpleClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new SimpleClass(p.getValueAsString());
        }
    }

    static class NameKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}