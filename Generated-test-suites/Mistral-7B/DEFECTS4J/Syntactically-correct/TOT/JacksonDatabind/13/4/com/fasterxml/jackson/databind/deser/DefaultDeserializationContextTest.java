package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntIdGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.*;

import java.io.Serializable;
import java.util.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getConfiguration().getDefaultDeserializationContext();

        // Test null id
        Object id = null;
        ObjectIdGenerator<?> gen = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid = ctxt.findObjectId(id, gen);
        assertNull(roid);

        // Test existing id
        User user1 = new User(1, "Alice");
        ObjectIdGenerator<?> gen1 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid1 = ctxt.findObjectId(user1, gen1);
        assertNotNull(roid1);
        assertEquals(roid1.getKey().key, 1);

        // Test non-existing id
        User user2 = new User(2, "Bob");
        ObjectIdGenerator<?> gen2 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid2 = ctxt.findObjectId(user2, gen2);
        assertNotNull(roid2);
        assertEquals(roid2.getKey().key, 2);

        // Test existing id with resolver
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ctxt._objectIdResolvers = Arrays.asList(resolver);
        User user3 = new User(3, "Charlie");
        ObjectIdGenerator<?> gen3 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid3 = ctxt.findObjectId(user3, gen3);
        assertNotNull(roid3);
        assertEquals(roid3.getKey().key, 3);

        // Test multiple resolvers
        SimpleObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        SimpleObjectIdResolver resolver2 = new SimpleObjectIdResolver();
        ctxt._objectIdResolvers = Arrays.asList(resolver1, resolver2);
        User user4 = new User(4, "David");
        ObjectIdGenerator<?> gen4 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid4 = ctxt.findObjectId(user4, gen4);
        assertNotNull(roid4);
        assertEquals(roid4.getKey().key, 4);

        // Test missing id and fail-on-unresolved-object-ids
        ctxt._config.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        User user5 = new User(5, "Eve");
        ObjectIdGenerator<?> gen5 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid5 = ctxt.findObjectId(user5, gen5);
        assertNotNull(roid5);
        assertEquals(roid5.getKey().key, 5);

        ctxt._config.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        User user6 = new User(6, "Frank");
        ObjectIdGenerator<?> gen6 = ObjectIdGenerators.Int.instance();
        try {
            ReadableObjectId roid6 = ctxt.findObjectId(user6, gen6);
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        // Test invalid id generator
        ObjectIdGenerator<?> genInvalid = new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                throw new RuntimeException("Invalid IdGenerator");
            }
        };
        User user7 = new User(7, "George");
        try {
            ReadableObjectId roid7 = ctxt.findObjectId(user7, genInvalid);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getConfiguration().getDefaultDeserializationContext();

        // Test empty objectIds
        ctxt._objectIds = null;
        try {
            ctxt.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        // Test no unresolved references
        ctxt._objectIds = new LinkedHashMap<>();
        User user1 = new User(1, "Alice");
        ObjectIdGenerator<?> gen1 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid1 = ctxt.findObjectId(user1, gen1);
        User user2 = new User(2, "Bob");
        ObjectIdGenerator<?> gen2 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid2 = ctxt.findObjectId(user2, gen2);
        assertEquals(ctxt._objectIds.size(), 2);
        ctxt.checkUnresolvedObjectId();

        // Test unresolved references
        User user3 = new User(3, "Charlie");
        ObjectIdGenerator<?> gen3 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid3 = ctxt.findObjectId(user3, gen3);
        ctxt._objectIds.put(roid3.getKey(), roid3);
        User user4 = new User(4, "David");
        ObjectIdGenerator<?> gen4 = ObjectIdGenerators.Int.instance();
        ReadableObjectId roid4 = ctxt.findObjectId(user4, gen4);
        ctxt._objectIds.put(roid4.getKey(), roid4);
        ctxt._objectIds.remove(roid3.getKey());
        try {
            ctxt.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
            assertEquals(e.getUnresolvedIds().size(), 1);
            assertEquals(e.getUnresolvedIds().get(0).getId(), roid3.getKey().key);
            assertEquals(e.getUnresolvedIds().get(0).getBeanType(), User.class);
            assertEquals(e.getUnresolvedIds().get(0).getLocation(), "DefaultDeserializationContextTest.testCheckUnresolvedObjectId()");
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<User> userDeserializer = new UserDeserializer();
        mapper.registerDeserializer(User.class, userDeserializer);
        DefaultDeserializationContext ctxt = mapper.getConfiguration().getDefaultDeserializationContext();

        // Test null deserializer
        Object deserDef1 = null;
        Annotated ann1 = null;
        JsonDeserializer<User> deser1 = ctxt.deserializerInstance(ann1, deserDef1);
        assertNull(deser1);

        // Test existing deserializer
        Annotated ann2 = new AnnotatedClass(User.class);
        JsonDeserializer<User> deserDef2 = userDeserializer;
        JsonDeserializer<User> deser2 = ctxt.deserializerInstance(ann2, deserDef2);
        assertEquals(deser2, deserDef2);

        // Test missing deserializer
        Annotated ann3 = new AnnotatedClass(String.class);
        Object deserDef3 = StringDeserializer.class;
        JsonDeserializer<String> deser3 = ctxt.deserializerInstance(ann3, deserDef3);
        assertNotNull(deser3);
        assertTrue(deser3 instanceof StringDeserializer);

        // Test custom deserializer
        Annotated ann4 = new AnnotatedClass(User.class);
        Object deserDef4 = new CustomUserDeserializer();
        JsonDeserializer<User> deser4 = ctxt.deserializerInstance(ann4, deserDef4);
        assertNotNull(deser4);
        assertTrue(deser4 instanceof CustomUserDeserializer);

        // Test missing factory
        Annotated ann5 = new AnnotatedClass(User.class);
        Object deserDef5 = CustomUserDeserializer.class;
        try {
            JsonDeserializer<User> deser5 = ctxt.deserializerInstance(ann5, deserDef5);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        KeyDeserializer keyDeserializer = new UserKeyDeserializer();
        mapper.registerKeyDeserializer(User.class, keyDeserializer);
        DefaultDeserializationContext ctxt = mapper.getConfiguration().getDefaultDeserializationContext();

        // Test null key deserializer
        Object deserDef1 = null;
        Annotated ann1 = null;
        KeyDeserializer deser1 = ctxt.keyDeserializerInstance(ann1, deserDef1);
        assertNull(deser1);

        // Test existing key deserializer
        Annotated ann2 = new AnnotatedClass(User.class);
        Object deserDef2 = keyDeserializer;
        KeyDeserializer deser2 = ctxt.keyDeserializerInstance(ann2, deserDef2);
        assertEquals(deser2, deserDef2);

        // Test missing key deserializer
        Annotated ann3 = new AnnotatedClass(String.class);
        Object deserDef3 = StringKeyDeserializer.class;
        KeyDeserializer deser3 = ctxt.keyDeserializerInstance(ann3, deserDef3);
        assertNotNull(deser3);
        assertTrue(deser3 instanceof StringKeyDeserializer);

        // Test custom key deserializer
        Annotated ann4 = new AnnotatedClass(User.class);
        Object deserDef4 = new CustomUserKeyDeserializer();
        KeyDeserializer deser4 = ctxt.keyDeserializerInstance(ann4, deserDef4);
        assertNotNull(deser4);
        assertTrue(deser4 instanceof CustomUserKeyDeserializer);

        // Test missing factory
        Annotated ann5 = new AnnotatedClass(User.class);
        Object deserDef5 = CustomUserKeyDeserializer.class;
        try {
            KeyDeserializer deser5 = ctxt.keyDeserializerInstance(ann5, deserDef5);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    static class User {
        @JsonAutoDetect
        @JsonIdentityInfo(generator = ObjectIdGenerators.Int.class)
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    static class UserDeserializer extends StdDeserializer<User> {
        public UserDeserializer() { this(null); }

        public UserDeserializer(Class<?> c) { super(c); }

        @Override
        public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            int id = jp.readValueAsInt();
            String name = jp.readValueAsString();
            return new User(id, name);
        }
    }

    static class CustomUserDeserializer extends JsonDeserializer<User> {
        @Override
        public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            int id = jp.readValueAsInt();
            String name = jp.readValueAsString();
            return new User(id * 2, name);
        }
    }

    static class UserKeyDeserializer extends KeyDeserializer.None {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.readValueAsInt();
        }
    }

    static class CustomUserKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.readValueAsInt() * 2;
        }
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() { this(null); }

        public StringDeserializer(Class<?> c) { super(c); }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    static class StringKeyDeserializer extends KeyDeserializer.None {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}