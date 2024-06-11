package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test null object id
        assertNull(context.findObjectId(null, null, null));

        // Test simple object id
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId id = context.findObjectId(new Object(), idGen, resolver);
        assertNotNull(id);
        assertEquals("testKey", id.getIdKey().toString());
        assertNull(id.getResolver());

        // Test object id with referring properties
        IdKey idKey = new ObjectIdGenerator.IdKey("testKey");
        Object referencedObject = new Object() {
            @JsonProperty
            private String name;
        };
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
                ctx.findObjectId(this, p, new SimpleObjectIdResolver());
                return new Object();
            }

            @JsonProperty
            private Object referencedProperty;

            @JsonIgnore
            private void setReferencedProperty(Object referencedProperty) {
                this.referencedProperty = referencedProperty;
            }

            @JsonProperty
            public Object getReferencedProperty() {
                return referencedProperty;
            }
        };
        Object idObject = new Object() {
            @JsonIdentityInfo(generator = idGen, resolver = resolver)
            @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
            @JsonProperty(value = "id")
            private ReadableObjectId id;

            @JsonProperty
            @JsonPropertyOrder(1)
            private String name;

            @JsonProperty(value = "referencedProperty")
            private Referring referencedProperty;
        };
        ObjectReader reader = mapper.readerFor(Object.class);
        JsonParser parser = reader.getParser();
        parser.readValueAs(Object.class); // Read object to initialize context
        parser.setValue(JsonFactory.valueToTree(Arrays.asList(idObject)));
        Object deserializedObject = reader.readValue(parser);
        ReadableObjectId deserializedId = context.findObjectId(deserializer, parser, null);
        assertNotNull(deserializedId);
        assertEquals(idKey, deserializedId.getIdKey());
        assertNotNull(deserializedId.getResolver());
        assertEquals(idObject.getClass(), deserializedId.getResolver().getClass());
        assertEquals(idObject, deserializedId.getResolver().getObjectIdContext());
        assertEquals(idObject.referencedProperty, deserializedId.getResolver().getReferencedProperties().iterator().next().getObjectIdContext());
    }

    @Test
    public void testCheckUnresolvedObjectId() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test empty context
        context.checkUnresolvedObjectId();

        // Test context with unresolved object id
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object idObject = new Object() {
            @JsonIdentityInfo(generator = idGen, resolver = resolver)
            @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
            @JsonProperty(value = "id")
            private ReadableObjectId id;
        };
        ObjectReader reader = mapper.readerFor(Object.class);
        JsonParser parser = reader.getParser();
        parser.readValueAs(Object.class); // Read object to initialize context
        parser.setValue(JsonFactory.valueToTree(Arrays.asList(idObject)));
        Object deserializedObject = reader.readValue(parser);
        ContextWrapper contextWrapper = new ContextWrapper(context);
        contextWrapper.setObjectIds(new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>());
        DefaultDeserializationContext testContext = new DefaultDeserializationContext(contextWrapper) {
            @Override
            public ReadableObjectId findObjectId(ObjectIdGenerator<?> gen, ObjectIdResolver resolverType) {
                ReadableObjectId id = super.findObjectId(gen, resolverType);
                if (id != null) {
                    saveObjectId(id);
                }
                return id;
            }

            private void saveObjectId(ReadableObjectId id) {
                ObjectIdGenerator.IdKey key = id.getIdKey();
                ReadableObjectId existingId = _objectIds.get(key);
                if (existingId != null) {
                    throw new UnresolvedForwardReference("Unresolved forward references for: " + key + " of type " + id.getClass().getName() + ", from " + id.getLocation());
                }
                _objectIds.put(key, id);
            }
        };
        Object deserializedObjectWithError = reader.readValue(parser, testContext.getTypeFactory().constructType(DeserializationContext.class));
        try {
            testContext.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test null deserializer definition
        JsonDeserializer<Object> deserializer = null;
        Annotated ann = null;
        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserializer);
        assertNull(deser);

        // Test valid deserializer definition (class)
        JsonDeserializer<Object> deserializerClass = new JsonDeserializer<Object>() {};
        Annotated annClass = null;
        JsonDeserializer<Object> deserClass = context.deserializerInstance(annClass, deserializerClass.getClass());
        assertNotNull(deserClass);
        assertEquals(deserializerClass, deserClass);

        // Test valid deserializer definition (instance)
        JsonDeserializer<Object> deserializerInstance = new JsonDeserializer<Object>() {};
        Annotated annInstance = null;
        JsonDeserializer<Object> deserInstance = context.deserializerInstance(annInstance, deserializerInstance);
        assertNotNull(deserInstance);
        assertEquals(deserializerInstance, deserInstance);

        // Test invalid deserializer definition (not JsonDeserializer or Class<JsonDeserializer>)
        Object deserializerDef = new Object() {};
        Annotated annDef = null;
        JsonDeserializer<Object> deserDef = context.deserializerInstance(annDef, deserializerDef);
        assertNull(deserDef);

        // Test invalid deserializer definition (not a class)
        Object deserializerDefClass = JsonDeserializer.None.class;
        Annotated annDefClass = null;
        JsonDeserializer<Object> deserDefClass = context.deserializerInstance(annDefClass, deserializerDefClass);
        assertNull(deserDefClass);

        // Test invalid deserializer definition (not JsonDeserializer or Class<JsonDeserializer> and not a subclass of that)
        Object deserializerDefOther = new Object() {};
        Annotated annDefOther = null;
        JsonDeserializer<Object> deserDefOther = context.deserializerInstance(annDefOther, deserializerDefOther);
        assertNull(deserDefOther);

        // Test deserializer resolution
        ObjectMapper mapperWithCustomDeserializer = new ObjectMapper();
        mapperWithCustomDeserializer.registerModule(new SimpleModule() {
            @Override
            public JsonDeserializer<Object> deserializerForProperty(PropertyDescriptor descriptor) {
                return new JsonDeserializer<Object>() {};
            }
        });
        DefaultDeserializationContext contextWithCustomDeserializer = mapperWithCustomDeserializer.getDeserializationContext();
        JsonDeserializer<Object> deserializerWithCustomDeserializer = contextWithCustomDeserializer.deserializerInstance(null, JsonDeserializer.class);
        assertNotNull(deserializerWithCustomDeserializer);
        assertEquals(JsonDeserializer.class, deserializerWithCustomDeserializer.getClass());
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test null key deserializer definition
        KeyDeserializer deserializer = null;
        Annotated ann = null;
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserializer);
        assertNull(deser);

        // Test valid key deserializer definition (class)
        KeyDeserializer deserializerClass = new KeyDeserializer() {};
        Annotated annClass = null;
        KeyDeserializer deserClass = context.keyDeserializerInstance(annClass, deserializerClass.getClass());
        assertNotNull(deserClass);
        assertEquals(deserializerClass, deserClass);

        // Test valid key deserializer definition (instance)
        KeyDeserializer deserializerInstance = new KeyDeserializer() {};
        Annotated annInstance = null;
        KeyDeserializer deserInstance = context.keyDeserializerInstance(annInstance, deserializerInstance);
        assertNotNull(deserInstance);
        assertEquals(deserializerInstance, deserInstance);

        // Test invalid key deserializer definition (not KeyDeserializer or Class<KeyDeserializer>)
        Object deserializerDef = new Object() {};
        Annotated annDef = null;
        KeyDeserializer deserDef = context.keyDeserializerInstance(annDef, deserializerDef);
        assertNull(deserDef);

        // Test invalid key deserializer definition (not KeyDeserializer or Class<KeyDeserializer> and not a subclass of that)
        Object deserializerDefOther = new Object() {};
        Annotated annDefOther = null;
        KeyDeserializer deserDefOther = context.keyDeserializerInstance(annDefOther, deserializerDefOther);
        assertNull(deserDefOther);

        // Test key deserializer resolution
        ObjectMapper mapperWithCustomKeyDeserializer = new ObjectMapper();
        mapperWithCustomKeyDeserializer.registerModule(new SimpleModule() {
            @Override
            public KeyDeserializer keyDeserializerForProperty(PropertyDescriptor descriptor) {
                return new KeyDeserializer() {};
            }
        });
        DefaultDeserializationContext contextWithCustomKeyDeserializer = mapperWithCustomKeyDeserializer.getDeserializationContext();
        KeyDeserializer deserializerWithCustomKeyDeserializer = contextWithCustomKeyDeserializer.keyDeserializerInstance(null, KeyDeserializer.class);
        assertNotNull(deserializerWithCustomKeyDeserializer);
        assertEquals(KeyDeserializer.class, deserializerWithCustomKeyDeserializer.getClass());
    }

    private static class ContextWrapper extends DefaultDeserializationContext {
        private LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> _objectIds;

        public ContextWrapper(DefaultDeserializationContext context) {
            super(context.getFactory(), context.getCache());
            _objectIds = new LinkedHashMap<>();
        }

        public void setObjectIds(LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds) {
            _objectIds = objectIds;
        }

        @Override
        public ReadableObjectId findObjectId(ObjectIdGenerator<?> gen, ObjectIdResolver resolverType) {
            return super.findObjectId(gen, resolverType);
        }

        private void saveObjectId(ReadableObjectId id) {
            ObjectIdGenerator.IdKey key = id.getIdKey();
            ReadableObjectId existingId = _objectIds.get(key);
            if (existingId != null) {
                throw new UnresolvedForwardReference("Unresolved forward references for: " + key + " of type " + id.getClass().getName() + ", from " + id.getLocation());
            }
            _objectIds.put(key, id);
        }
    }
}