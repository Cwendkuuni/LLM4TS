package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new CustomStringDeserializer());
        MAPPER.registerModule(module);
    }

    @Test
    public void testBaseTypeDeserialization() throws Exception {
        String json = "{\"name\": \"John\", \"type\": \"StringType\"}";
        TypeDeserializerBase deserializer = MAPPER.readValue(json, TypeDeserializerBase.class);
        assertEquals("StringType", deserializer.baseTypeName());
        assertEquals("type", deserializer.getPropertyName());
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", deserializer.getClass().getName());
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        String json = "{\"name\": \"John\", \"type\": \"CustomType\"}";
        TypeDeserializerBase deserializer = MAPPER.readValue(json, TypeDeserializerBase.class);
        TypeIdResolver idResolver = deserializer.getTypeIdResolver();
        assertNotNull(idResolver);
        JavaType type = idResolver.typeFromId(new DeserializationContext(MAPPER.getFactory(), null), "CustomType");
        assertEquals(CustomType.class, type.getRawClass());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        String json = "{\"name\": \"John\"}";
        TypeDeserializerBase deserializer = MAPPER.readValue(json, TypeDeserializerBase.class);
        assertEquals(Void.class, deserializer.getDefaultImpl().getRawClass());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String json = "{\"name\": \"John\", \"type\": \"CustomStringType\"}";
        TypeDeserializerBase deserializer = MAPPER.readValue(json, TypeDeserializerBase.class);
        JsonDeserializer<Object> deserializerForCustomStringType = deserializer._findDeserializer(MAPPER.getDeserializationContext(), "CustomStringType");
        assertEquals(CustomStringDeserializer.class, deserializerForCustomStringType.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String json = "{\"name\": \"John\", \"type\": \"UnknownType\"}";
        TypeDeserializerBase deserializer = MAPPER.readValue(json, TypeDeserializerBase.class);
        assertNull(deserializer.findDeserializerForProperty(MAPPER.getDeserializationContext(), ""));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMissingTypeId() throws Exception {
        String json = "{\"name\": \"John\"}";
        TypeDeserializerBase deserializer = MAPPER.readValue(json, TypeDeserializerBase.class);
        deserializer.handleMissingTypeId(MAPPER.getDeserializationContext(), "MissingTypeId");
    }

    static class CustomType {}
    static class CustomStringType extends TypeDeserializerBase {
        CustomStringType() {
            super(null, null, null, null, null, null, new ConcurrentHashMap<>(), null, null, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new CustomStringTypeForProperty(this, prop);
        }

        static class CustomStringTypeForProperty extends CustomStringType {
            CustomStringTypeForProperty(TypeDeserializerBase base, BeanProperty prop) {
                super(base, prop);
            }
        }
    }

    static class CustomStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText();
        }
    }
}