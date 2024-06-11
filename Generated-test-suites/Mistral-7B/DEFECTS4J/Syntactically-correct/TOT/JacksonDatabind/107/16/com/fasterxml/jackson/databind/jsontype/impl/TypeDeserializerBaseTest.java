package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase.TypeIdResolver;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.Types;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testTypeDeserializerBase() throws Exception {
        // Test base case with no type id
        String json = "{\"name\": \"John\"}";
        TypeDeserializerBase deserializer = mapper.readValue(json, TypeDeserializerBase.class);
        assertNull(deserializer._typeIdVisible);
        assertNull(deserializer._deserializers);
        assertNull(deserializer._defaultImplDeserializer);
        assertNull(deserializer._property);

        // Test with type id
        json = "{\"typeId\": \"MyClass\", \"name\": \"John\"}";
        TypeDeserializerBase deserializerWithTypeId = mapper.readValue(json, TypeDeserializerBase.class);
        assertNotNull(deserializerWithTypeId._typeIdVisible);
        assertEquals("MyClass", deserializerWithTypeId._typeIdVisible);
        assertNotNull(deserializerWithTypeId._deserializers);
        assertNull(deserializerWithTypeId._defaultImplDeserializer);
        assertNull(deserializerWithTypeId._property);

        // Test with default implementation
        json = "{\"typeId\": null, \"name\": \"John\"}";
        TypeDeserializerBase deserializerWithDefaultImpl = mapper.readValue(json, TypeDeserializerBase.class);
        assertNotNull(deserializerWithDefaultImpl._typeIdVisible);
        assertNull(deserializerWithDefaultImpl._typeIdVisible);
        assertNotNull(deserializerWithDefaultImpl._deserializers);
        assertNotNull(deserializerWithDefaultImpl._defaultImplDeserializer);
        assertNull(deserializerWithDefaultImpl._property);

        // Test with property
        TypeIdResolver idResolver = mapper.getTypeFactory().constructType(Map.class.getTypeName());
        BeanProperty property = new BeanProperty("", SimpleType.construct("java.lang.String"), idResolver);
        TypeDeserializerBase deserializerWithProperty =
                new TypeDeserializerBase(mapper.getTypeFactory().constructType(TypeDeserializerBase.class.getTypeName()),
                        idResolver, "", true, mapper.getTypeFactory().constructType(String.class.getTypeName()));
        deserializerWithProperty._property = property;
        JsonParser jp = mapper.getFactory().createParser("{\"name\": \"John\"}");
        Object result = deserializerWithProperty._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext(), null);
        assertEquals("John", result);

        // Test with unknown type id
        json = "{\"typeId\": \"UnknownType\", \"name\": \"John\"}";
        try {
            TypeDeserializerBase deserializerWithUnknownTypeId = mapper.readValue(json, TypeDeserializerBase.class);
            fail("Expected JsonMappingException");
        } catch (IOException e) {
            // Expected
        }

        // Test with missing type id
        json = "{\"name\": \"John\"}";
        try {
            TypeDeserializerBase deserializerWithMissingTypeId = mapper.readValue(json, TypeDeserializerBase.class);
            fail("Expected JsonMappingException");
        } catch (IOException e) {
            // Expected
        }
    }
}