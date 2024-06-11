package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

public class DefaultDeserializationContextTest {

    @ObjectIdGenerator(JsonSubTypes.Type.STATIC)
    static class MyObjectIdGenerator implements ObjectIdGenerator<String> {
        @Override
        public ObjectIdGenerator.Id key(Object id) {
            return new ObjectIdGenerator.Id() {
                private final String id;

                public MyObjectIdGenerator(String id) {
                    this.id = id;
                }

                @Override
                public String getId() {
                    return id;
                }
            };
        }
    }

    @JsonDeserialize(using = MyDeserializer.class)
    static class MyData {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class MyDeserializer extends StdDeserializer<MyData> {
        public MyDeserializer() {
            this(null);
        }

        public MyDeserializer(Class<?> vc) {
            super(MyData.class);
        }

        @Override
        public MyData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            MyData result = new MyData();
            result.setId(ctxt.readValueAs(String.class, jp));
            return result;
        }
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        ObjectIdGenerator<String> idGenerator = new MyObjectIdGenerator("testId");

        ObjectIdResolver idResolver = new SimpleObjectIdResolver() {
            @Override
            public ObjectId resolveId(Object id, DeserializationContext ctxt) {
                return new ReadableObjectId(new ObjectIdGenerator.Id() {
                    @Override
                    public String getId() {
                        return (String)id;
                    }
                });
            }
        };

        ctxt = ctxt.with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, JavaType type)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated type)
                    throws JsonMappingException {
                return new MyDeserializer();
            }

            @Override
            public ObjectIdGenerator<?> idGeneratorInstance(DeserializationConfig config) {
                return idGenerator;
            }

            @Override
            public ObjectIdResolver idResolverInstance(DeserializationConfig config) {
                return idResolver;
            }

            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated ann,
                    Object value) throws JsonMappingException {
                return null;
            }
        });

        ObjectIdGenerator.Id id = idGenerator.key("testId");
        ReadableObjectId roid = ctxt.findObjectId(id, idGenerator, null);
        assertNotNull(roid);
        assertNotNull(roid.getResolver());

        ctxt.checkUnresolvedObjectId();
        try {
            ctxt.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithNullIdGenerator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        ObjectIdResolver idResolver = new SimpleObjectIdResolver() {
            @Override
            public ObjectId resolveId(Object id, DeserializationContext ctxt) {
                return new ReadableObjectId(new ObjectIdGenerator.Id() {
                    @Override
                    public String getId() {
                        return (String)id;
                    }
                });
            }
        };

        ctxt = ctxt.with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, JavaType type)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated type)
                    throws JsonMappingException {
                return new MyDeserializer();
            }

            @Override
            public ObjectIdGenerator<?> idGeneratorInstance(DeserializationConfig config) {
                return null;
            }

            @Override
            public ObjectIdResolver idResolverInstance(DeserializationConfig config) {
                return idResolver;
            }

            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated ann,
                    Object value) throws JsonMappingException {
                return null;
            }
        });

        ObjectIdGenerator.Id id = null;
        ReadableObjectId roid = ctxt.findObjectId(id, null, null);
        assertNull(roid);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        ObjectIdGenerator<String> idGenerator = new MyObjectIdGenerator("testId");

        ctxt = ctxt.with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, JavaType type)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated type)
                    throws JsonMappingException {
                return new MyDeserializer();
            }

            @Override
            public ObjectIdGenerator<?> idGeneratorInstance(DeserializationConfig config) {
                return idGenerator;
            }

            @Override
            public ObjectIdResolver idResolverInstance(DeserializationConfig config) {
                return null;
            }

            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated ann,
                    Object value) throws JsonMappingException {
                return null;
            }
        });

        ObjectIdGenerator.Id id = idGenerator.key("testId");
        ctxt.findObjectId(id, idGenerator, null);

        try {
            ctxt.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testCheckUnresolvedObjectIdWithEnabledFeature() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);

        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        ObjectIdGenerator<String> idGenerator = new MyObjectIdGenerator("testId");

        ctxt = ctxt.with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, JavaType type)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated type)
                    throws JsonMappingException {
                return new MyDeserializer();
            }

            @Override
            public ObjectIdGenerator<?> idGeneratorInstance(DeserializationConfig config) {
                return idGenerator;
            }

            @Override
            public ObjectIdResolver idResolverInstance(DeserializationConfig config) {
                return null;
            }

            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated ann,
                    Object value) throws JsonMappingException {
                return null;
            }
        });

        ObjectIdGenerator.Id id = idGenerator.key("testId");
        try {
            ctxt.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        Annotated ann = mapper.findConcreteAnnotationInstance(MyData.class.getDeclaredField("id"), ObjectIdGenerator.class);
        Object idGenerator = ann.getValue();
        Object deserDef = new MyDeserializer();
        JsonDeserializer<Object> deser = ctxt.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertInstanceOf(MyDeserializer.class, deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        Annotated ann = mapper.findConcreteAnnotationInstance(MyData.class.getDeclaredField("id"), JsonDeserializer.Property.class);
        Object deserDef = new MyDeserializer();
        KeyDeserializer deser = ctxt.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertNull(deser.getValue());
    }
}