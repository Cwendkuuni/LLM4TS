package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBaseTypeDeserialization() throws IOException {
        String json = "{\"typeId\":\"BaseType\",\"value\":42}";
        BaseTypeDeserializer baseTypeDeserializer = new BaseTypeDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializer(BaseType.class, baseTypeDeserializer, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object readValue = jsonParser.readValue(typeDeserializer);

        assertEquals(42, readValue);
    }

    @Test
    public void testMissingTypeIdDeserialization() throws IOException {
        String json = "{\"value\":42}";
        BaseTypeDeserializer baseTypeDeserializer = new BaseTypeDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializer(BaseType.class, baseTypeDeserializer, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object readValue = jsonParser.readValue(typeDeserializer);

        assertNull(readValue);
    }

    @Test
    public void testUnknownTypeIdDeserialization() throws IOException {
        String json = "{\"typeId\":\"UnknownTypeId\",\"value\":42}";
        BaseTypeDeserializer baseTypeDeserializer = new BaseTypeDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializer(BaseType.class, baseTypeDeserializer, null, null);

        try {
            JsonParser jsonParser = mapper.getFactory().createParser(json);
            Object readValue = jsonParser.readValue(typeDeserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id: UnknownTypeId", e.getMessage());
        }
    }

    @Test
    public void testNullTypeIdDeserialization() throws IOException {
        String json = "{}";
        BaseTypeDeserializer baseTypeDeserializer = new BaseTypeDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializer(BaseType.class, baseTypeDeserializer, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object readValue = jsonParser.readValue(typeDeserializer);

        assertNull(readValue);
    }

    @Test
    public void testTypeIdVisibleDeserialization() throws IOException {
        String json = "{\"typeId\":\"SubType\",\"value\":42}";
        SubTypeDeserializer subTypeDeserializer = new SubTypeDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializer(BaseType.class, subTypeDeserializer, null, true);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object readValue = jsonParser.readValue(typeDeserializer);

        assertInstanceOf(SubType.class, readValue);
    }

    @Test
    public void testTypeIdNotVisibleDeserialization() throws IOException {
        String json = "{\"typeId\":\"SubType\",\"value\":42}";
        SubTypeDeserializer subTypeDeserializer = new SubTypeDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializer(BaseType.class, subTypeDeserializer, null, false);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object readValue = jsonParser.readValue(typeDeserializer);

        assertEquals(42, readValue);
    }

    static class BaseType {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    static class SubType extends BaseType {
        // empty
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase<BaseType> {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new BaseTypeDeserializer();
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXTERNAL_PROPERTY;
        }
    }

    static class SubTypeDeserializer extends BaseTypeDeserializer {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new SubTypeDeserializer();
        }
    }
}