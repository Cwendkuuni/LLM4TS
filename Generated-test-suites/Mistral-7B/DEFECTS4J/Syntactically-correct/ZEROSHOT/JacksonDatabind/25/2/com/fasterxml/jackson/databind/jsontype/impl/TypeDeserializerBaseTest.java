package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private static final String TEST_TYPE_ID = "testTypeId";
    private static final String TEST_TYPE_NAME = "testTypeName";
    private static final String TEST_PROPERTY_NAME = "testPropertyName";
    private static final String TEST_DEFAULT_IMPL_CLASS_NAME = "testDefaultImpl";
    private static final String TEST_TYPE_PROPERTY_NAME = "typePropertyName";

    private TypeDeserializerBase typeDeserializerBase;
    private JsonDeserializer<Object> testDeserializer;
    private TypeIdResolver typeIdResolver;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() throws Exception {
        JavaType baseType = JsonTypeFactory.constructType(Object.class);
        JavaType testType = JsonTypeFactory.constructType(String.class);
        TypeIdResolver typeIdResolver = new TestTypeIdResolver();
        this.typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, TEST_PROPERTY_NAME, true, TEST_DEFAULT_IMPL_CLASS_NAME.getClass()) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return testDeserializer;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return testDeserializer;
            }
        };
        this.testDeserializer = new TestJsonDeserializer();
        this.deserializationContext = new TestDeserializationContext();
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = new TestBeanProperty();
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        Assert.assertNotNull(typeDeserializer);
        Assert.assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
        Assert.assertEquals(TEST_PROPERTY_NAME, typeDeserializer.getPropertyName());
        Assert.assertEquals(typeDeserializerBase, typeDeserializer.getTypeIdResolver());
        Assert.assertEquals(TEST_DEFAULT_IMPL_CLASS_NAME, typeDeserializerBase.getDefaultImpl().getRawClass().getName());
        Assert.assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        Assert.assertEquals(Object.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() throws Exception {
        String expectedToString = "[TypeDeserializerBase; base-type:class java.lang.Object; id-resolver=class com.fasterxml.jackson.databind.jsontype.impl.TestTypeIdResolver]";
        Assert.assertEquals(expectedToString, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String testTypeId = TEST_TYPE_ID;
        JsonParser jsonParser = new TestJsonParser();
        jsonParser.setCurrentName(TEST_TYPE_ID);
        JsonToken currentToken = jsonParser.nextToken();
        Object readValue = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        Assert.assertNotNull(readValue);
        Assert.assertTrue(readValue instanceof String);
        Assert.assertEquals("testValue", readValue);

        Map<String, JsonDeserializer<Object>> deserializers = typeDeserializerBase._deserializers;
        JsonDeserializer<Object> deserializer = deserializers.get(testTypeId);
        Assert.assertEquals(testDeserializer, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerWithInvalidTypeId() throws Exception {
        String invalidTypeId = "invalidTypeId";
        JsonParser jsonParser = new TestJsonParser();
        jsonParser.setCurrentName(INVALID_TYPE_ID);
        JsonToken currentToken = jsonParser.nextToken();
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        Object readValue = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext).deserialize(new TestJsonParser(), deserializationContext);
        Assert.assertNull(readValue);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String unknownTypeId = "unknownTypeId";
        TypeIdResolver typeIdResolver = new TestTypeIdResolver();
        TypeDeserializerBase testTypeDeserializerBase = new TypeDeserializerBase(null, typeIdResolver, TEST_PROPERTY_NAME, true, null) {
            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return new TestJsonDeserializer();
            }
        };
        JsonParser jsonParser = new TestJsonParser();
        jsonParser.setCurrentName(unknownTypeId);
        JsonToken currentToken = jsonParser.nextToken();
        Object readValue = testTypeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        Assert.assertNotNull(readValue);
        Assert.assertTrue(readValue instanceof String);
        Assert.assertEquals("testValue", readValue);
    }

    static class TestBeanProperty extends BeanProperty {
        public TestBeanProperty() {
            super("testPropertyName", Object.class, null);
        }
    }

    static class TestJsonDeserializer extends StdDeserializer<String> {
        public TestJsonDeserializer() {
            this(null);
        }

        public TestJsonDeserializer(Class<?> vc) {
            super(String.class, vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "testValue";
        }
    }

    static class TestTypeIdResolver extends TypeIdResolverBase {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return JsonTypeFactory.constructType(String.class);
        }

        @Override
        public String getDescForKnownTypeIds() {
            return TEST_TYPE_ID;
        }
    }

    static class TestDeserializationContext extends JacksonAnnotationIntrospector.BaseDeserializerAnnotationContext {
        @Override
        public Object findValueDeserializer(BeanProperty property, JavaType type) throws IOException {
            return null;
        }

        @Override
        public Object findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return null;
        }

        @Override
        public Object findValueDeserializer(JavaType type) throws IOException {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> type) throws IOException {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(Class<?> type, BeanProperty property) throws IOException {
            return null;
        }

        @Override
        public Object deserializeValue(JsonParser p, DeserializationContext ctxt, JavaType type) throws IOException {
            return null;
        }
    }

    static class TestJsonParser extends JsonParserBase {
        private String currentName;

        public TestJsonParser() {
            this(new TestArrayContext(), new TestTreeContext());
        }

        public TestJsonParser(JsonFactory jsonFactory, JsonParser p) {
            super(jsonFactory, p);
        }

        public TestJsonParser(JsonFactory jsonFactory, JsonParser p, TreeRoot root) {
            super(jsonFactory, p, root);
        }

        public TestJsonParser(JsonFactory jsonFactory, JsonParser p, TreeRoot root, ArrayContext ac) {
            super(jsonFactory, p, root, ac);
        }

        public void setCurrentName(String name) {
            this.currentName = name;
        }

        public String getCurrentName() {
            return currentName;
        }
    }

}