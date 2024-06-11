package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class FieldPropertyTest extends TestBase {

    @Test
    public void testBasic() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field\": 42}";
        Foofoo = new Foo();
        mapper.readValue(json, Foo.class, foo);
        assertEquals(42, foo.getFieldValue());
    }

    @Test
    public void testNull() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object foo = new Foo();
        String json = "{\"field\": null}";
        mapper.readValue(json, Foo.class, foo);
        assertNull(foo.getFieldValue());
    }

    @Test
    public void testSkipNulls() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperWithNullProvider = new ObjectMapper();
        mapperWithNullProvider.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithNullProvider.setNullValueProvider(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return "nullValue";
            }
        });
        Object foo = new Foo();
        String json = "{\"field\": null}";
        mapper.readValue(json, Foo.class, foo); // should fail
        Foo fooWithNullProvider = mapperWithNullProvider.readValue(json, Foo.class);
        assertEquals("nullValue", fooWithNullProvider.getFieldValue());
    }

    @Test
    public void testTypeHandling() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperWithTypeHandler = new ObjectMapper();
        mapperWithTypeHandler.registerTypeHandler(String.class, new StringDeserializer());
        Foofoo = new Foo();
        String json = "{\"field\": \"test\"}";
        mapper.readValue(json, Foo.class, foo);
        assertEquals("", foo.getFieldValue());
        mapperWithTypeHandler.readValue(json, Foo.class, foo);
        assertEquals("test", foo.getFieldValue());
    }

    @Test
    public void testAccess() throws IOException, JsonProcessingException {
        Foofoo = new Foo();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAccessModifier(AccessLevel.PRIVATE);
        String json = "{\"field\": 42}";
        mapper.readValue(json, Foo.class, foo);
        assertEquals(42, foo.getFieldValue());
    }

    @Test(expected = IOException.class)
    public void testInvalidGet() throws IOException, JsonProcessingException {
        Foofoo = new Foo();
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"invalidField\": 42}";
        mapper.readValue(json, Foo.class, foo);
    }

    @Test
    public void testWithName() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, "_name", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, String.class, null, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Foo.class), "_name"), "_name", String.class, null));
        assertEquals("_name", fieldProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> valueDeserializer = new StandardDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return "test";
            }
        };
        FieldProperty fieldProperty = new FieldProperty(null, String.class, valueDeserializer, null, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Foo.class), "_valueDeserializer", String.class, null), "_valueDeserializer", String.class, null));
        assertEquals(valueDeserializer, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = new StandardNullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return "nullValue";
            }
        };
        FieldProperty fieldProperty = new FieldProperty(null, String.class, null, nullValueProvider, new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Foo.class), "_nullProvider", NullValueProvider.class, null), "_nullProvider", NullValueProvider.class, null));
        assertEquals(nullValueProvider, fieldProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() throws IOException, JsonProcessingException {
        Foofoo = new Foo();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAccessModifier(AccessLevel.PROTECTED);
        String json = "{\"protectedField\": 42}";
        mapper.readValue(json, Foo.class, foo);
        assertEquals(42, foo.getProtectedFieldValue());
    }

    @Test
    public void testGetAnnotation() {
        MyAnnotation myAnnotation = new MyAnnotation();
        Foofoo = new Foo();
        Field field = Foo.class.getField("_annotation");
        AnnotatedField annotatedField = new AnnotatedField(new AnnotatedMember(new AnnotatedClass(Foo.class), "_annotation"), "_annotation", MyAnnotation.class, myAnnotation);
        FieldProperty fieldProperty = new FieldProperty(null, MyAnnotation.class, null, null, annotatedField);
        assertEquals(myAnnotation, fieldProperty.getAnnotation(MyAnnotation.class));
    }

    static class Foo {
        private int field;
        private String fieldValue;
        private String protectedFieldValue;

        protected int getProtectedFieldValue() {
            return field;
        }

        public int getFieldValue() {
            return field;
        }

        @MyAnnotation
        public String getAnnotation() {
            return "test";
        }

        public String getProtectedFieldValue() {
            return protectedFieldValue;
        }

        public void setFieldValue(int value) {
            field = value;
        }

        public void setProtectedFieldValue(int value) {
            this.protectedFieldValue = Integer.toString(value);
        }

        public static class StringDeserializer extends StdDeserializer<String> {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return p.getValueAsString();
            }
        }

        private static final long serialVersionUID = 1L;
    }

    @MyAnnotation
    static class MyAnnotation {
    }
}

class StandardNullValueProvider implements NullValueProvider {
    @Override
    public Object getNullValue(DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}