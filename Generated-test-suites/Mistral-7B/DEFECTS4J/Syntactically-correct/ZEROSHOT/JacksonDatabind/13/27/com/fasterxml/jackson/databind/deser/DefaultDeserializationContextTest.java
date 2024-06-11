package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test for {@link DefaultDeserializationContext} class.
 */
public class DefaultDeserializationContextTest {

    @ObjectIdGenerator(JsonSubTypes.With.Value(value = MyClassWithId.class, id = "ID1"))
    public static enum MyCustomIdGenerator {
        INSTANCE;

        @ObjectIdGenerator(JsonSubTypes.With.Value(value = MyClassWithoutId.class, id = "ID2"))
        public ObjectIdGenerator idGeneratorForOtherClass() {
            return null;
        }
    }

    @JsonIdentityInfo(generator = MyCustomIdGenerator.class)
    static class MyClassWithId {
        private int id;

        @JsonIdentityReference(alwaysAsId = true)
        private MyClassWithId reference;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public MyClassWithId getReference() {
            return reference;
        }

        public void setReference(MyClassWithId reference) {
            this.reference = reference;
        }
    }

    static class MyClassWithoutId {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    @JsonDeserialize(using = MyClassDeserializer.class)
    static class MyClassToDeserialize {
        private MyClassWithId idd;
        private MyClassWithoutId idd2;

        public MyClassWithId getIdd() {
            return idd;
        }

        public void setIdd(MyClassWithId idd) {
            this.idd = idd;
        }

        public MyClassWithoutId getIdd2() {
            return idd2;
        }

        public void setIdd2(MyClassWithoutId idd2) {
            this.idd2 = idd2;
        }
    }

    static class MyClassDeserializer extends StdDeserializer<MyClassToDeserialize> {
        public MyClassDeserializer() {
            this(null);
        }

        public MyClassDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public MyClassToDeserialize deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            MyClassToDeserialize ret = new MyClassToDeserialize();

            ret.setIdd(ctxt.findValueAsObjectId(p, MyCustomIdGenerator.class, "ID1").getObject());
            ret.setIdd2(ctxt.readValueAs(MyClassWithoutId.class, p));
            return ret;
        }
    }

    @org.junit.Test
    public void testFindObjectId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        ObjectIdGenerator idGenerator = new MyCustomIdGenerator();

        Object id = new Object();
        ObjectIdGenerator.IdKey key = idGenerator.key(id);

        ReadableObjectId readableObjectId = context.findObjectId(id, idGenerator);
        assertNotNull(readableObjectId);
        assertEquals("ID1", readableObjectId.getIdKey().getId());

        ObjectIdGenerator idGenerator2 = idGenerator.idGeneratorForOtherClass();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ObjectIdResolver resolver2 = context.findObjectIdResolver(resolver.getClass());
        assertNotNull(resolver2);
        assertTrue(resolver2 instanceof SimpleObjectIdResolver);
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        ObjectIdGenerator idGenerator = new MyCustomIdGenerator();

        Object id = new Object();
        ObjectIdGenerator.IdKey key = idGenerator.key(id);

        context.findObjectId(id, idGenerator); // Add an entry to _objectIds

        context.checkUnresolvedObjectId();

        ObjectIdGenerator.IdKey key2 = new ObjectIdGenerator.IdKey();
        ObjectIdGenerator idGenerator2 = new MyCustomIdGenerator();
        Object id2 = new Object();
        Object readableObjectId = context.findObjectId(id2, idGenerator2);
        context.removeObjectId(readableObjectId); // Remove an entry from _objectIds

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
            assertEquals("Unresolved forward references for: ID2", e.getMessage());
            fail("Unexpected Exception: " + e.getCauses());
        }
    }

    @org.junit.Test
    public void testDeserializerInstance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        MyClassToDeserialize objectToDeserialize = context.readValue(new JsonFactory(), MyClassToDeserialize.class);

        // Test ObjectIdGenerator
        ObjectIdGenerator idGenerator = new MyCustomIdGenerator();
        JsonDeserializer<MyClassWithId> deserializer = context.deserializerInstance(null, idGenerator);
        assertEquals(MyClassWithId.class, deserializer.getClass());
        assertTrue(deserializer instanceof MyClassWithIdDeserializer);

        // Test ObjectIdResolver
        ObjectIdResolver resolver = context.keyDeserializerInstance(null, MyCustomKeyDeserializer.class);
        assertEquals(MyCustomKeyDeserializer.class, resolver.getClass());
        assertTrue(resolver instanceof MyCustomKeyDeserializer);
    }

    static class MyCustomKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    static class MyClassWithIdDeserializer extends JsonDeserializer<MyClassWithId> {
        @Override
        public MyClassWithId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyClassWithId();
        }
    }

    @org.junit.Test
    public void testCreateInstance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        MyClassToDeserialize objectToDeserialize = context.createInstance(null, null, null);

        assertEquals(MyClassToDeserialize.class, objectToDeserialize.getClass());
        assertNull(objectToDeserialize.getIdd());
        assertNull(objectToDeserialize.getIdd2());
    }
}