package com.example.junit4.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private static final String TEST_JSON = "{\"typeId\":\"MyClass\",\"value\":42}";

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializerBase deserializer = new MyTypeDeserializerBase(null, null, "", false, null);
        TypeFactory typeFactory = mapper.getTypeFactory();

        MyClass expected = new MyClass();
        expected.value = 42;

        MyClass actual = deserializer.deserialize(factory, ctxt, null, null, new ObjectParser(TEST_JSON));

        assertSame(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializerBase deserializer = new MyTypeDeserializerBase(null, null, "", false, null);

        JsonParser parser = factory.createParser(new ByteArrayInputStream(TEST_JSON.getBytes()));
        parser.nextToken(); // skip [
        parser.nextValue(); // skip {"

        deserializer.deserialize(factory, ctxt, null, null, parser);

        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializerBase deserializer = new MyTypeDeserializerBase(null, null, "", false, null);
        TypeFactory typeFactory = mapper.getTypeFactory();

        JsonParser parser = factory.createParser(new ByteArrayInputStream(("{\"typeId\":\"UnknownType\"}" + TEST_JSON.substring(TEST_JSON.indexOf(","))).getBytes()));
        parser.nextToken(); // skip [
        parser.nextValue(); // skip {"

        deserializer.deserialize(factory, ctxt, null, null, parser);

        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializerBase deserializer = new MyTypeDeserializerBase(null, null, "", false, null);

        Object actual = deserializer.deserialize(factory, ctxt, null, null, new ObjectParser(null));

        assertTrue(actual instanceof Void);
    }

    static class MyClass {
        int value;
    }

    static class MyTypeDeserializerBase extends TypeDeserializerBase {
        public MyTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXTERNAL_PROPERTY;
        }

        @Override
        public JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            if ("MyClass".equals(typeId)) {
                return new MyClassDeserializer();
            }
            return super._findDeserializer(ctxt, typeId);
        }

        protected class MyClassDeserializer extends JsonDeserializer<MyClass> {
            @Override
            public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                MyClass result = new MyClass();
                p.readValueAs(MyClass.class);
                return result;
            }
        }
    }

    static class ObjectParser extends BaseParserMinimalBase {
        ObjectParser(String content) {
            super(new ByteArrayInputStream(content.getBytes()));
        }

        ObjectParser(ByteArrayInputStream content) {
            super(content);
        }

        ObjectParser(byte[] content) {
            super(new ByteArrayInputStream(content));
        }

        @Override
        public Object parseValue() throws IOException {
            return _parseValue();
        }

        Object _parseValue() throws IOException {
            switch (nextToken()) {
                case START_ARRAY:
                    return parseArray();
                case START_OBJECT:
                    return parseObject();
                default:
                    return null;
            }
        }

        Object parseArray() throws IOException {
            if (nextToken() != END_ARRAY) {
                return parseValue();
            }
            return null;
        }

        Object parseObject() throws IOException {
            Map<String, Object> result = new HashMap<>();
            String currentName = null;

            while (nextToken() != END_OBJECT) {
                currentName = textValueOf();
                if (currentName == null) {
                    throw new RuntimeException("Unexpected null property name");
                }

                if (nextToken() != COLON) {
                    throw new RuntimeException("Unexpected token: " + nameOf());
                }

                result.put(currentName, parseValue());
            }

            return result;
        }
    }
}