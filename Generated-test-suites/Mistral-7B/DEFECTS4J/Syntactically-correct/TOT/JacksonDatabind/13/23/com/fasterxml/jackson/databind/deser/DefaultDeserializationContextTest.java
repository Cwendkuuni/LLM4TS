package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultDeserializationContextTest extends TestBase {

    public static void main(String[] args) throws Exception {
        // Initialize JSON parser and ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Test findObjectId method with simple serializable object
        testFindObjectId_SimpleSerializableObject(mapper);

        // Test findObjectId method with complex deserialized object
        testFindObjectId_ComplexDeserializedObject(mapper);

        // Test checkUnresolvedObjectId method with unresolved forward references
        testCheckUnresolvedObjectId_UnresolvedForwardReferences(mapper);

        // Test deserializerInstance method with custom JsonDeserializer
        testDeserializerInstance_CustomJsonDeserializer(mapper);

        // Test deserializerInstance method with custom KeyDeserializer
        testDeserializerInstance_CustomKeyDeserializer(mapper);

        // Test with method with custom DeserializerFactory
        testWith_CustomDeserializerFactory(mapper);

        // Test createInstance method with custom DeserializationConfig, JsonParser, and InjectableValues
        testCreateInstance_CustomDeserializationConfig(mapper);
    }

    private static void testFindObjectId_SimpleSerializableObject(ObjectMapper mapper) throws IOException {
        // Create a simple JavaBean class with a unique identifier
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        static class SimpleJavaBean {
            private int id;

            @JsonProperty("id")
            public int getId() { return id; }

            @JsonCreator
            public SimpleJavaBean(@JsonProperty("id") int id) { this.id = id; }
        }

        // Serialize the object to JSON
        String json = mapper.writeValueAsString(new SimpleJavaBean(1));

        // Create a DefaultDeserializationContext and test findObjectId method
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext().readTree(json);
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(SimpleJavaBean.class.getName(), id.getClass().getName(), id.hashCode());
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = ctxt.findObjectId(1, gen, resolver);
        assertEquals(1, roid.getKey().id);
    }

    private static void testFindObjectId_ComplexDeserializedObject(ObjectMapper mapper) throws IOException {
        // Create a custom JsonDeserializer for a complex object
        static class ComplexDeserializer extends StdSerializer<ComplexObject> {
            @Override
            public ComplexObject deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                ComplexObject obj = new ComplexObject();
                obj.setId(ctxt.getIdValue());
                return obj;
            }
        }

        // Register the custom JsonDeserializer
        mapper.registerDeserializer(ComplexObject.class, new ComplexDeserializer());

        // Serialize the complex object to JSON
        String json = mapper.writeValueAsString(new ComplexObject());

        // Create a DefaultDeserializationContext and test findObjectId method
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext().readTree(json);
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(ComplexObject.class.getName(), id.getClass().getName(), id.hashCode());
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = ctxt.findObjectId(new ComplexObject(), gen, resolver);
        assertEquals(ComplexObject.class, roid.getType());
    }

    private static void testCheckUnresolvedObjectId_UnresolvedForwardReferences(ObjectMapper mapper) throws IOException {
        // Create a JavaBean with a forward reference to another JavaBean
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        static class JavaBeanA {
            private int id;
            private JavaBeanB javaBeanB;

            @JsonProperty("id")
            public int getId() { return id; }

            @JsonProperty("javaBeanB")
            public JavaBeanB getJavaBeanB() { return javaBeanB; }

            @JsonCreator
            public JavaBeanA(@JsonProperty("id") int id, @JsonProperty("javaBeanB") JavaBeanB javaBeanB) {
                this.id = id;
                this.javaBeanB = javaBeanB;
            }
        }

        static class JavaBeanB {
            private int id;

            @JsonProperty("id")
            public int getId() { return id; }

            @JsonCreator
            public JavaBeanB(@JsonProperty("id") int id) { this.id = id; }
        }

        // Serialize the JavaBeans to JSON
        String json = mapper.writeValueAsString(new JavaBeanA(1, null));
        String jsonB = mapper.writeValueAsString(new JavaBeanB(2));

        // Create a DefaultDeserializationContext and deserialize the JSON
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext().readTree(json);
        Object id = ctxt.readValueAsObject(JsonNode.class);
        JavaBeanA javaBeanA = ctxt.readValueAs(JavaBeanA.class);
        JavaBeanB javaBeanB = ctxt.readValueAs(JavaBeanB.class, new TypeReference<JavaBeanB>() {});
        ctxt.checkUnresolvedObjectId();

        // Verify that the ID was correctly set and forward references were resolved
        assertEquals(1, javaBeanA.getId());
        assertEquals(2, javaBeanB.getId());
        assertEquals(javaBeanB, javaBeanA.getJavaBeanB());
    }

    private static void testDeserializerInstance_CustomJsonDeserializer(ObjectMapper mapper) throws IOException {
        // Create a custom JsonDeserializer for a simple JavaBean
        static class SimpleJsonDeserializer extends StdSerializer<SimpleJavaBean> {
            @Override
            public SimpleJavaBean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                SimpleJavaBean obj = new SimpleJavaBean();
                obj.setId(ctxt.getIdValue());
                return obj;
            }
        }

        // Register the custom JsonDeserializer
        mapper.registerDeserializer(SimpleJavaBean.class, new SimpleJsonDeserializer());

        // Serialize and deserialize a SimpleJavaBean using the DefaultDeserializationContext
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(SimpleJavaBean.class.getName(), id.getClass().getName(), id.hashCode());
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = mapper.getDeserializationContext().findObjectId(1, gen, resolver);
        SimpleJavaBean obj = mapper.getDeserializationContext().deserializerInstance(new AnnotatedClass(SimpleJavaBean.class), roid).deserialize(null, null);
        assertEquals(1, obj.getId());

        // Verify that the custom JsonDeserializer was used
        assertTrue(obj.getClass() == SimpleJavaBean.class);
    }

    private static void testDeserializerInstance_CustomKeyDeserializer(ObjectMapper mapper) throws IOException {
        // Create a custom KeyDeserializer for a simple JavaBean
        static class SimpleKeyDeserializer extends StdSerializer<PropertyName> {
            @Override
            public PropertyName deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new PropertyName(jp.getValueAsString());
            }
        }

        // Register the custom KeyDeserializer
        ObjectMapper.DefaultDeserializationContext defaultCtxt = mapper.getDeserializationContext();
        defaultCtxt.setKeyDeserializerInstance(new AnnotatedClass(SimpleJavaBean.class), new SimpleKeyDeserializer());

        // Serialize and deserialize a SimpleJavaBean using the DefaultDeserializationContext
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(SimpleJavaBean.class.getName(), id.getClass().getName(), id.hashCode());
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = defaultCtxt.findObjectId(1, gen, resolver);
        SimpleJavaBean obj = (SimpleJavaBean) defaultCtxt.deserializerInstance(new AnnotatedClass(SimpleJavaBean.class), roid).deserialize(null, null);
        assertEquals(1, obj.getId());

        // Verify that the custom KeyDeserializer was used
        assertTrue(obj.getClass() == SimpleJavaBean.class);
    }

    private static void testWith_CustomDeserializerFactory(ObjectMapper mapper) throws IOException {
        // Create a custom DeserializerFactory that returns a specific JsonDeserializer
        static class CustomDeserializerFactory implements DeserializerFactory {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, AnnotatedMember m, AnnotatedMethod am, @Nullable AnnotatedMethodParameter ap, JavaType valueType) {
                if (valueType.isAssignableFrom(SimpleJavaBean.class)) {
                    return new SimpleJsonDeserializer();
                }
                return null;
            }
        }

        // Use the custom DeserializerFactory with the DefaultDeserializationContext
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt = ctxt.with(new CustomDeserializerFactory());
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(SimpleJavaBean.class.getName(), id.getClass().getName(), id.hashCode());
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = ctxt.findObjectId(1, gen, resolver);
        SimpleJavaBean obj = (SimpleJavaBean) ctxt.deserializerInstance(new AnnotatedClass(SimpleJavaBean.class), roid).deserialize(null, null);
        assertEquals(1, obj.getId());

        // Verify that the custom DeserializerFactory was used
        assertTrue(obj.getClass() == SimpleJavaBean.class);
    }

    private static void testCreateInstance_CustomDeserializationConfig(ObjectMapper mapper) throws IOException {
        // Create a custom DeserializationConfig that sets specific features
        ObjectMapper.DefaultDeserializationContext.Impl defaultCtxt = mapper.getDeserializationContext();
        ObjectMapper.DefaultDeserializationContext config = new ObjectMapper.DefaultDeserializationContext(
                defaultCtxt, new ObjectMapper.DefaultDeserializationContext.Impl(),
                new ObjectMapper.DefaultDeserializationContext.Impl(), new ObjectMapper.SafeDataBindUtils());
        config.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        // Create a JsonParser with a custom ObjectReader
        JsonParser jp = mapper.getFactory().createParser("{\"id\": 1}");
        ObjectReader reader = mapper.getFactory().getBeanDeserializerProvider().findBeanDeserializer(Object.class, null);
        jp.setReader(reader);

        // Use the custom DeserializationConfig with the DefaultDeserializationContext
        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        DefaultDeserializationContext ctxt = new ObjectMapper.DefaultDeserializationContext(
                new ObjectMapper.DefaultDeserializationContext.Impl(config, null, jp, null),
                null, objectIds, null);
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(Object.class.getName(), id.getClass().getName(), id.hashCode());
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = ctxt.findObjectId(1, gen, resolver);
        Object obj = ctxt.deserializerInstance(new AnnotatedClass(Object.class), roid).deserialize(jp, null);

        // Verify that the custom DeserializationConfig was used
        assertTrue(ctxt.isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS));
        assertEquals(1, obj.hashCode());
    }
}