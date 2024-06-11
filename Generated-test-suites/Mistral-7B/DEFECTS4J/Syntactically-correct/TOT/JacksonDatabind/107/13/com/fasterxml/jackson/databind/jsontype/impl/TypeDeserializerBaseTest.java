package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.serializer.TestBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TypeDeserializerBaseTest extends TestBase {

    private static final String TEST_CLASS_NAME = TypeDeserializerBase.class.getName();
    private static final String BASE_TYPE_NAME = "java.lang.String";
    private static final String PROPERTY_NAME = "typeProperty";
    private static final boolean TYPE_ID_VISIBLE = true;

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase<Object, String> TEST_INSTANCE = new BaseTypeDeserializer() {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new PropertyTypeDeserializer(prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }

        @Override
        protected Map<String, JsonDeserializer<Object>> _deserializers = new HashMap<>();

        private static class BaseTypeDeserializer extends TypeDeserializerBase<Object, String> {
            public BaseTypeDeserializer() {
                super(TypeFactory.defaultInstance.constructType(Object.class),
                        new StdTypeIdResolver(), PROPERTY_NAME, TYPE_ID_VISIBLE,
                        TypeFactory.defaultInstance.constructType(BASE_TYPE_NAME));
            }
        }

        private static class PropertyTypeDeserializer extends TypeDeserializerBase<Object, String> {
            public PropertyTypeDeserializer(BeanProperty prop) {
                super(TypeDeserializerBase.this, prop);
            }
        }
    };

    @SuppressWarnings("unchecked")
    private static final JsonDeserializer<Object> STRING_DESERIALIZER = new JsonDeserializer<Object>() {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText();
        }
    };

    @SuppressWarnings("unchecked")
    private static final JsonDeserializer<Object> INT_DESERIALIZER = new JsonDeserializer<Object>() {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getIntValue();
        }
    };

    @BeforeClass
    public static void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerVal(StdSerializer.class);
        mapper.registerModule(new SimpleModule() {{
            addDeserializer(String.class, STRING_DESERIALIZER);
            addDeserializer(Integer.class, INT_DESERIALIZER);
        }});
    }

    @Test
    public void testDeserializeWithValidTypeId() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(JsonMapping.TypeIdProperty, "java.lang.String");
        node.put(PROPERTY_NAME, "test");

        Object actual = mapper.readValue(node, TEST_INSTANCE.baseType());

        assertEquals("test", actual);
    }

    @Test
    public void testDeserializeWithMissingTypeId() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(PROPERTY_NAME, "test");

        try {
            Object actual = mapper.readValue(node, TEST_INSTANCE.baseType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals(JsonMappingException.ClassIdMismatch.class, e.getCause().getClass());
        }
    }

    @Test
    public void testDeserializeWithInvalidTypeId() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(JsonMapping.TypeIdProperty, "java.lang.Integer");
        node.put(PROPERTY_NAME, "test");

        try {
            Object actual = mapper.readValue(node, TEST_INSTANCE.baseType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals(JsonMappingException.UnknownProperty.class, e.getCause().getCause().getClass());
            assertEquals("Unknown property: 'java.lang.Integer' (for POJO property 'typeProperty')",
                         e.getCause().getMessage());
        }
    }

    @Test
    public void testDeserializeWithDefaultImpl() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(JsonMapping.TypeIdProperty, null);

        Object actual = mapper.readValue(node, TEST_INSTANCE.baseType());

        assertNull(actual);
    }

    @Test
    public void testDeserializeWithDefaultImplDeserializer() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put(JsonMapping.TypeIdProperty, null);

        JsonDeserializer<Object> defaultImplDeserializer = new StdDeserializer();
        TEST_INSTANCE.setDefaultImplDeserializer(defaultImplDeserializer);

        Object actual = mapper.readValue(node, TEST_INSTANCE.baseType());

        assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_STATIC_TYPES);
        mapper.registerModule(new SimpleModule() {{
            addDeserializer(BaseTypeDeserializer.class, new BaseTypeDeserializer());
        }});

        ObjectNode node = mapper.createObjectNode();
        node.put(JsonMapping.TypeIdProperty, "java.lang.String");
        node.put(PROPERTY_NAME, "test");

        Object actual = mapper.readValue(node, BaseTypeDeserializer.class);

        assertEquals("test", actual);
    }
}