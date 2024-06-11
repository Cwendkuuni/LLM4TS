package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest {

    private ObjectInstanceFactory instanceFactory;
    private ObjectMapper objectMapper;
    private MethodProperty methodProperty;

    @Before
    public void setup() throws Exception {
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer());
        objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(introspector);
        objectMapper.registerModule(module);
        instanceFactory = objectMapper.getTypeFactory().constructValueType(Object.class, int[].class);
        methodProperty = new MethodProperty(new BeanPropertyDefinition(new AnnotatedMember("testMethod", new AnnotatedMethod(MethodPropertyTest.class.getMethod("testMethod"), MethodPropertyTest.class)), int[].class, null, null, null), Object.class, null, null, null);
    }

    @Test
    public void testMethodPropertyDeserializeAndSet() throws Exception {
        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[1, 2, 3]");

        parser.nextToken();
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), testInstance);

        int[] result = (int[]) testInstance;
        Assert.assertArrayEquals(new int[]{1, 2, 3}, result);
    }

    @Test
    public void testMethodPropertyDeserializeAndSetNull() throws Exception {
        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[null]");

        parser.nextToken();
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), testInstance);

        Assert.assertNull(testInstance);
    }

    @Test
    public void testMethodPropertyDeserializeSetAndReturn() throws Exception {
        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[1, 2, 3]");

        parser.nextToken();
        Object result = methodProperty.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), testInstance);

        Assert.assertSame(testInstance, result);
    }

    @Test
    public void testMethodPropertyDeserializeSetAndReturnNull() throws Exception {
        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[null]");

        parser.nextToken();
        Object result = methodProperty.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), testInstance);

        Assert.assertNull(result);
    }

    @Test
    public void testMethodPropertySet() throws Exception {
        Object testInstance = instanceFactory.valueFromString("[1, 2, 3]");

        methodProperty.set(testInstance, 5);

        int[] result = (int[]) testInstance;
        Assert.assertArrayEquals(new int[]{5}, result);
    }

    @Test
    public void testMethodPropertySetNull() throws Exception {
        Object testInstance = instanceFactory.valueFromString("[1, 2, 3]");

        methodProperty.set(testInstance, null);

        Assert.assertNull(testInstance);
    }

    @Test
    public void testMethodPropertyFixAccess() throws Exception {
        MethodProperty methodProperty = new MethodProperty(methodProperty, new PropertyName("testProperty"));
        methodProperty.fixAccess(objectMapper.getDeserializationConfig());

        String testValue = "test";
        Object testInstance = instanceFactory.valueFromString("[1, 2, 3]");

        AnnotatedMethod annotatedMethod = new AnnotatedMethod(MethodPropertyTest.class.getMethod("testMethod"), MethodPropertyTest.class);
        AnnotationInstance annotationInstance = new AnnotationInstance(AnnotationTypes.ACCESS.class);
        annotationInstance.setValue("PUBLIC");
        annotatedMethod.addAnnotation(annotationInstance);
        methodProperty._annotated = annotatedMethod;

        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);

        parser.nextToken();
        parser.getValueAsString();

        Assert.assertEquals("PUBLIC", methodProperty.getAnnotation(AnnotationTypes.ACCESS.class).getValue());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        methodProperty = new MethodProperty(methodProperty, null, null).withValueDeserializer(stringDeserializer);

        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[1, 2, 3]");

        parser.nextToken();
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), testInstance);

        String result = (String) testInstance;
        Assert.assertEquals("1, 2, 3", result);
    }

    @Test
    public void testMethodPropertyWithNullProvider() throws Exception {
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return "nullValue";
            }
        };
        methodProperty = new MethodProperty(methodProperty, null, null).withNullProvider(nullValueProvider);

        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[null]");

        parser.nextToken();
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), testInstance);

        Assert.assertEquals("nullValue", testInstance);
    }

    @Test
    public void testMethodPropertyWithValueDeserializerAndTypeDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeDeserializer intTypeDeserializer = new IntDeserializer();
        methodProperty = new MethodProperty(methodProperty, null, null, null, null).withValueDeserializer(stringDeserializer).withValueTypeDeserializer(intTypeDeserializer);

        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[\"1\", \"2\", \"3\"]");

        parser.nextToken();
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), testInstance);

        int[] result = (int[]) testInstance;
        Assert.assertArrayEquals(new int[]{1, 2, 3}, result);
    }

    @Test
    public void testMethodPropertyWithValueDeserializerAndNullValueProvider() throws Exception {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        methodProperty = new MethodProperty(methodProperty, null, null, null, null).withValueDeserializer(stringDeserializer).withNullProvider(nullValueProvider);

        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[null]");

        parser.nextToken();
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), testInstance);

        Assert.assertNull(testInstance);
    }

    @Test
    public void testMethodPropertyWithValueDeserializerAndNullValueProviderAndTypeDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeDeserializer intTypeDeserializer = new IntDeserializer();
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        methodProperty = new MethodProperty(methodProperty, null, null, null, null).withValueDeserializer(stringDeserializer)
                .withNullProvider(nullValueProvider).withValueTypeDeserializer(intTypeDeserializer);

        JsonParser parser = new BaseJsonParser();
        parser.setCodec(objectMapper);
        Object testInstance = instanceFactory.valueFromString("[null]");

        parser.nextToken();
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), testInstance);

        Assert.assertNull(testInstance);
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = p.getCurrentToken();
            if (currentToken.isScalarValue()) {
                String value = p.getValueAsString();
                p.nextToken();
                return value;
            } else {
                throw ctxt.mappingException("Expected String but found " + currentToken);
            }
        }
    }

    static class IntDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = p.getCurrentToken();
            if (currentToken.isNumberToken()) {
                int value = p.getIntValue();
                p.nextToken();
                return value;
            } else {
                throw ctxt.mappingException("Expected Int but found " + currentToken);
            }
        }
    }

    static class AnnotationTypes {
        public static final Class<Access> ACCESS = Access.class;
    }

    static class Access {
        public static final String PUBLIC = "PUBLIC";

        private Access() {
        }
    }
}