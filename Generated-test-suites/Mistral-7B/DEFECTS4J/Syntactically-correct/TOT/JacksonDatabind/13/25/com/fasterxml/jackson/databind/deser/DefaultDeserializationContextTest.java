package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGeneratorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;
import java.util.Map.Entry;

/**
 * Test class for {@link DefaultDeserializationContext}
 */
public class DefaultDeserializationContextTest {

    private ObjectMapper mapper;

    @BeforeMethod
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test for findObjectId method with null input
     */
    @Test
    public void testFindObjectIdNullInput() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Object id = null;
        ObjectIdGenerator<?> gen = new SimpleObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        try {
            context.findObjectId(id, gen, resolver);
            Assert.fail("Expected UnresolvedObjectId exception");
        } catch (UnresolvedObjectIdException e) {
            // Expected
        }
    }

    /**
     * Test for findObjectId method with valid input
     */
    @Test
    public void testFindObjectIdValidInput() throws JsonProcessingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Object id = new Object();
        ObjectIdGenerator<?> gen = new SimpleObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId readableObjectId = context.findObjectId(id, gen, resolver);
        Assert.assertNotNull(readableObjectId);
    }

    /**
     * Test for checkUnresolvedObjectId method with no unresolved ids
     */
    @Test
    public void testCheckUnresolvedObjectIdNoUnresolvedIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        context.checkUnresolvedObjectId();
    }

    /**
     * Test for checkUnresolvedObjectId method with unresolved ids
     */
    @Test
    public void testCheckUnresolvedObjectIdUnresolvedIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<?> gen = new SimpleObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey(id1.getClass()), new ReadableObjectId(id1));
        context.checkUnresolvedObjectId();
        context._objectIds.put(new ObjectIdGenerator.IdKey(id2.getClass()), new ReadableObjectId(id2));
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            List<String> ids = new ArrayList<>();
            ids.add(id1.getClass().getName());
            ids.add(id2.getClass().getName());
            Assert.assertEquals(e.getUnresolvedIds(), ids.toArray(new String[0]));
        }
    }

    /**
     * Test for deserializerInstance method with null deserDef
     */
    @Test
    public void testDeserializerInstanceNullDeserDef() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Annotated ann = new AnnotatedClass(TestClass.class);
        Object deserDef = null;
        try {
            context.deserializerInstance(ann, deserDef);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for deserializerInstance method with JsonDeserializer instance
     */
    @Test
    public void testDeserializerInstanceJsonDeserializerInstance() throws JsonProcessingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Annotated ann = new AnnotatedClass(TestClass.class);
        JsonDeserializer<TestClass> deser = new TestClassDeserializer();
        Object deserDef = deser;
        TestClass result = context.deserializerInstance(ann, deserDef);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), deser.getId(ann));
    }

    /**
     * Test for deserializerInstance method with Class instance
     */
    @Test
    public void testDeserializerInstanceClassInstance() throws JsonProcessingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Annotated ann = new AnnotatedClass(TestClass.class);
        Class<JsonDeserializer<TestClass>> deserClass = JsonDeserializer.class;
        TestClass result = context.deserializerInstance(ann, deserClass);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), mapper.findValueToValueConverter(null, TestClass.class, TestClass.class, null, null).getValue(new TestClass()));
    }

    /**
     * Test for keyDeserializerInstance method with null deserDef
     */
    @Test
    public void testKeyDeserializerInstanceNullDeserDef() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Annotated ann = new AnnotatedClass(TestClassKey.class);
        Object deserDef = null;
        try {
            context.keyDeserializerInstance(ann, deserDef);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for keyDeserializerInstance method with KeyDeserializer instance
     */
    @Test
    public void testKeyDeserializerInstanceKeyDeserializerInstance() throws JsonProcessingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Annotated ann = new AnnotatedClass(TestClassKey.class);
        KeyDeserializer deser = new TestClassKeyDeserializer();
        Object deserDef = deser;
        TestClassKey result = context.keyDeserializerInstance(ann, deserDef);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), deser.getId(ann));
    }

    /**
     * Test for keyDeserializerInstance method with Class instance
     */
    @Test
    public void testKeyDeserializerInstanceClassInstance() throws JsonProcessingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getFactory());
        Annotated ann = new AnnotatedClass(TestClassKey.class);
        Class<KeyDeserializer<TestClassKey>> deserClass = KeyDeserializer.class;
        TestClassKey result = context.keyDeserializerInstance(ann, deserClass);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), mapper.findValueToValueConverter(null, TestClassKey.class, TestClassKey.class, null, null).getValue(new TestClassKey()));
    }

    /**
     * Test class for objects with @JsonIdentityInfo
     */
    @JsonAutoDetect
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    static class TestClass {
        private int id;

        @JsonIdentityReference(alwaysAsId = true)
        private TestClass ref;

        public TestClass() {
        }

        public TestClass(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public TestClass getRef() {
            return ref;
        }

        public void setRef(TestClass ref) {
            this.ref = ref;
        }
    }

    /**
     * Test class for keys with @JsonIdentityInfo
     */
    static class TestClassKey {
        private int id;

        @JsonIdentityReference(alwaysAsId = true)
        private TestClass ref;

        public TestClassKey() {
        }

        public TestClassKey(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public TestClass getRef() {
            return ref;
        }

        public void setRef(TestClass ref) {
            this.ref = ref;
        }
    }

    /**
     * Test deserializer for TestClass
     */
    static class TestClassDeserializer extends JsonDeserializer<TestClass> {

        @Override
        public TestClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TestClass result = new TestClass();
            result.setId(ctxt.readValueAs(Integer.class, p));
            JsonToken idToken = p.nextToken();
            if (idToken == JsonToken.VALUE_NULL) {
                result.setRef(null);
            } else {
                result.setRef(ctxt.readValueAs(TestClass.class, p));
            }
            return result;
        }

        @Override
        public Object getId(Annotated ann) {
            return TestClass.class;
        }
    }

    /**
     * Test deserializer for TestClassKey
     */
    static class TestClassKeyDeserializer extends KeyDeserializer.None<TestClassKey> {

        @Override
        public TestClassKey deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TestClassKey result = new TestClassKey();
            result.setId(ctxt.readValueAs(Integer.class, p));
            JsonToken idToken = p.nextToken();
            if (idToken == JsonToken.VALUE_NULL) {
                result.setRef(null);
            } else {
                result.setRef(ctxt.readValueAs(TestClass.class, p));
            }
            return result;
        }
    }

    /**
     * Test generator for ObjectId
     */
    static class SimpleObjectIdGenerator extends ObjectIdGenerator {

        @Override
        public ObjectGenerator.IdKey key(Object id) {
            return new ObjectIdGenerator.IdKey(id.getClass());
        }

        @Override
        public Object generateId(SerializerProvider serializers, BeanProperty property, JsonGenerator jgen, Object idValue) throws IOException {
            return idValue;
        }

        @Override
        public Object generateId(SerializerProvider serializers, Object bean) throws IOException {
            return bean.getClass().getName();
        }
    }

    /**
     * Test resolver for ObjectId
     */
    static class SimpleObjectIdResolver extends SimpleObjectIdResolver {

        @Override
        public Object resolveId(DefaultDeserializationContext ctxt, DeserializationContext rootContext, ObjectId id) throws IOException {
            return id.getValue();
        }
    }
}