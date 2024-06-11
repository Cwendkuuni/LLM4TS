package com.fasterxml.jackson.databind.test.typedeserializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setUp() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        typeIdResolver = new StdTypeIdResolver();
        typeDeserializer = new TypeDeserializerBase(baseType, typeIdResolver, "typeProperty", true, null);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer forProperty = typeDeserializer.forProperty(property);
        assertEquals(TypeDeserializerBase.class, forProperty.getClass());
        assertEquals("typeProperty", forProperty.getPropertyName());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals(TypeDeserializerBase.class.getName(), typeDeserializer.baseTypeName());
    }

    @Test
    public void testPropertyName() throws Exception {
        assertEquals("typeProperty", typeDeserializer.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        assertEquals(typeIdResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        String typeId = "com.example.StringDeserializer";
        JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(JsonType.STD_STRING);
        TypeIdResolver typeIdResolverSpy = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeIdStr) throws IOException {
                if (typeIdStr.equals(typeId)) {
                    return stringType;
                }
                return null;
            }
        };
        TypeDeserializerBase spy = new TypeDeserializerBase(null, typeIdResolverSpy, "typeProperty", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return deserializer;
            }
        };
        Object deserializedObject = spy.deserialize(mapper.readTree("{\"typeId\":\"" + typeId + "\"}"), null);
        assertNotNull(deserializedObject);
        assertEquals(String.class, deserializedObject.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object deserializedObject = typeDeserializer.deserialize(mapper.readTree("{}"), null);
        assertNull(deserializedObject);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        String typeId = "UnknownTypeId";
        TypeDeserializerBase spy = new TypeDeserializerBase(null, null, "typeProperty", true, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return mapper.getTypeFactory().constructType(String.class);
            }
        };
        Object deserializedObject = spy.deserialize(mapper.readTree("{\"typeId\":\"" + typeId + "\"}"), null);
        assertNotNull(deserializedObject);
        assertEquals(String.class, deserializedObject.getClass());
    }
}