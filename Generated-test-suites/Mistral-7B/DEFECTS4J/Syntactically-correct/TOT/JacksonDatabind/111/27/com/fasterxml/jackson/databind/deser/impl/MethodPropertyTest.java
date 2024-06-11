package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest extends BaseMapTest {

    private static final Class<MethodProperty> METHOD_PROPERTY_CLASS = MethodProperty.class;

    private Object testInstance;
    private MethodProperty methodProperty;

    @Before
    public void setup() throws Exception {
        testInstance = new Object();
        methodProperty = new MethodProperty(
                new BeanPropertyDefinition(new SimpleBeanPropertyDefinition("testField", Integer.class)),
                JsonTypeFactory.instance.constructType(Integer.class),
                null,
                Annotations.empty(),
                Mockito.mock(AnnotatedMethod.class));
        Method method = Mockito.mock(Method.class);
        methodProperty = new MethodProperty(methodProperty, method);
    }

    @Test
    public void testSetterMethodWithValidValue() throws JsonProcessingException {
        // Given
        JsonParser jsonParser = createParser("{\"testField\": 1}");
        Object validValue = 1;

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertEquals(validValue, testInstance);
    }

    @Test
    public void testSetterMethodWithNullValueAndSkipNulls() throws JsonProcessingException {
        // Given
        JsonParser jsonParser = createParser("{\"testField\": null}");
        methodProperty._skipNulls = true;

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertNull(testInstance);
    }

    @Test
    public void testSetterMethodWithNullValueAndDontSkipNulls() throws JsonProcessingException {
        // Given
        JsonParser jsonParser = createParser("{\"testField\": null}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertNull(testInstance);
        Object nullValue = _mapper.readValue(jsonParser, Object.class);
        Assert.assertNotNull(nullValue);
        methodProperty.set(testInstance, nullValue);
        Assert.assertNull(testInstance);
    }

    @Test
    public void testSetterMethodWithInvalidValue() throws JsonProcessingException {
        // Given
        JsonParser jsonParser = createParser("{\"testField\": \"invalidValue\"}");
        Object invalidValue = "invalidValue";

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertNotNull(testInstance);
        Assert.assertEquals("", testInstance.getClass().getSimpleName());
        Assert.fail("Expected an IOException to be thrown");
    }

    @Test
    public void testSetterMethodWithNullValueProvider() throws JsonProcessingException {
        // Given
        NullValueProvider nullValueProvider = null;
        methodProperty = new MethodProperty(methodProperty, nullValueProvider);
        JsonParser jsonParser = createParser("{\"testField\": null}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertNull(testInstance);
    }

    @Test
    public void testSetterMethodWithValueDeserializer() throws JsonProcessingException {
        // Given
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        methodProperty = new MethodProperty(methodProperty, valueDeserializer);
        JsonParser jsonParser = createParser("{\"testField\": \"testValue\"}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertEquals("testValue", testInstance);
    }

    @Test
    public void testSetterMethodWithTypeDeserializer() throws JsonProcessingException {
        // Given
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        methodProperty = new MethodProperty(methodProperty, typeDeserializer);
        JsonParser jsonParser = createParser("{\"testField\": 1}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertEquals(1, testInstance);
    }

    @Test
    public void testSetterMethodWithFixAccess() throws JsonProcessingException {
        // Given
        DeserializationConfig config = new DeserializationConfig();
        config.setBeanPropertyDefinitionVisibility(Mapper.DefaultVisibility.NONE);
        methodProperty.fixAccess(config);
        JsonParser jsonParser = createParser("{\"testField\": 1}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertEquals(1, testInstance);
    }

    @Test
    public void testSetterMethodWithAnnotations() throws JsonProcessingException {
        // Given
        Annotation annotation = Mockito.mock(Annotation.class);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(Mockito.mock(Method.class), new Annotations(annotation));
        methodProperty = new MethodProperty(methodProperty, annotatedMethod);
        JsonParser jsonParser = createParser("{\"testField\": 1}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertEquals(1, testInstance);
        Assert.assertEquals(annotation, methodProperty.getMember().getAnnotations().findValue(annotation.getClass()));
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializerAndTypeDeserializer() throws JsonProcessingException {
        // Given
        JsonDeserializer<Integer> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        methodProperty = new MethodProperty(methodProperty, valueDeserializer, typeDeserializer);
        JsonParser jsonParser = createParser("{\"testField\": \"42\"}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstance);

        // Then
        Assert.assertEquals(42, testInstance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException {
        // Given
        JsonParser jsonParser = createParser("{\"testField\": 1}");

        // When
        Object result = methodProperty.deserializeSetAndReturn(jsonParser, context, testInstance);

        // Then
        Assert.assertEquals(1, result);
        Assert.assertEquals(1, testInstance);
    }

    @Test
    public void testSet() throws JsonProcessingException {
        // Given
        int expectedValue = 1;
        methodProperty.set(testInstance, expectedValue);

        // Then
        Assert.assertEquals(expectedValue, testInstance);
    }

    @Test
    public void testSetAndReturn() throws JsonProcessingException {
        // Given
        int expectedValue = 1;
        Object result = methodProperty.setAndReturn(testInstance, expectedValue);

        // Then
        Assert.assertEquals(expectedValue, testInstance);
        Assert.assertEquals(expectedValue, result);
    }

    @Test(expected = IOException.class)
    public void testThrowExceptionOnInvalidSetter() throws JsonProcessingException {
        // Given
        Object testInstanceWithInvalidSetter = new Object() {
            @Override
            public void setTestField(Object value) {}
        };
        methodProperty = new MethodProperty(methodProperty, testInstanceWithInvalidSetter.getClass().getMethod("setTestField"));
        JsonParser jsonParser = createParser("{\"testField\": 1}");

        // When
        methodProperty.deserializeAndSet(jsonParser, context, testInstanceWithInvalidSetter);

        // Then
        // Expected IOException is thrown
    }
}