package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.StdDeserializationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    @InjectMocks
    private MethodProperty methodProperty;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() throws JsonMappingException, JsonProcessingException {
        // Test default constructor
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        MethodProperty actual = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(MethodProperty.class, actual.getClass());
        assertEquals(propDef, actual.getBeanPropertyDefinition());
        assertEquals(type, actual.getType());
        assertEquals(typeDeser, actual.getTypeDeserializer());
        assertEquals(contextAnnotations, actual.getContextAnnotations());
        assertEquals(method, actual.getAnnotated());
        assertEquals(Method.class, actual.getSetter().getClass());
        assertFalse(actual._skipNulls);
    }

    @Test
    public void testCopyConstructor() throws JsonMappingException, JsonProcessingException {
        // Test copy constructor
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        MethodProperty original = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);

        MethodProperty copy = new MethodProperty(original, deserializer, nullValueProvider);

        assertEquals(MethodProperty.class, copy.getClass());
        assertEquals(original.getBeanPropertyDefinition(), copy.getBeanPropertyDefinition());
        assertEquals(original.getType(), copy.getType());
        assertEquals(original.getTypeDeserializer(), copy.getTypeDeserializer());
        assertEquals(original.getContextAnnotations(), copy.getContextAnnotations());
        assertEquals(original.getAnnotated(), copy.getAnnotated());
        assertEquals(original.getSetter(), copy.getSetter());
        assertEquals(deserializer, copy._valueDeserializer);
        assertEquals(nullValueProvider, copy._nullProvider);
        assertFalse(copy._skipNulls);
    }

    @Test
    public void testWithNameConstructor() throws JsonMappingException, JsonProcessingException {
        // Test withName constructor
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        PropertyName newName = PropertyName.construct("newTestProperty");

        MethodProperty actual = new MethodProperty(methodProperty, newName);

        assertEquals(MethodProperty.class, actual.getClass());
        assertEquals(methodProperty.getBeanPropertyDefinition(), actual.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), actual.getType());
        assertEquals(methodProperty.getTypeDeserializer(), actual.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), actual.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), actual.getAnnotated());
        assertEquals(methodProperty.getSetter(), actual.getSetter());
        assertEquals(methodProperty._skipNulls, actual._skipNulls);
        assertEquals("newTestProperty", actual.getName().asString());
    }

    @Test
    public void testWithValueDeserializerConstructor() throws JsonMappingException, JsonProcessingException {
        // Test withValueDeserializer constructor
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);

        MethodProperty actual = new MethodProperty(methodProperty, deserializer, nullValueProvider);

        assertEquals(MethodProperty.class, actual.getClass());
        assertEquals(methodProperty.getBeanPropertyDefinition(), actual.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), actual.getType());
        assertEquals(methodProperty.getTypeDeserializer(), actual.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), actual.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), actual.getAnnotated());
        assertEquals(methodProperty.getSetter(), actual.getSetter());
        assertEquals(methodProperty._skipNulls, actual._skipNulls);
        assertEquals(deserializer, actual._valueDeserializer);
        assertEquals(nullValueProvider, actual._nullProvider);
    }

    @Test
    public void testWithNullProviderConstructor() throws JsonMappingException, JsonProcessingException {
        // Test withNullProvider constructor
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);

        MethodProperty actual = new MethodProperty(methodProperty, nullValueProvider);

        assertEquals(MethodProperty.class, actual.getClass());
        assertEquals(methodProperty.getBeanPropertyDefinition(), actual.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), actual.getType());
        assertEquals(methodProperty.getTypeDeserializer(), actual.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), actual.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), actual.getAnnotated());
        assertEquals(methodProperty.getSetter(), actual.getSetter());
        assertEquals(methodProperty._skipNulls, actual._skipNulls);
        assertEquals(nullValueProvider, actual._nullProvider);
    }

    @Test
    public void testDeserializeAndSet() throws JsonMappingException, JsonProcessingException {
        // Test deserializeAndSet method
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        Object instance = new Object();

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        when(deserializer.deserialize(any(JsonParser.class), any(StdDeserializationContext.class))).thenReturn("Test Value");

        methodProperty.withValueDeserializer(deserializer);
        methodProperty.withNullProvider(nullValueProvider);

        methodProperty.deserializeAndSet(mapper.createParser("{\"testProperty\": null}"), new StdDeserializationContext(), instance);
        assertNull(instance);

        methodProperty.deserializeAndSet(mapper.createParser("{\"testProperty\": null}"), new StdDeserializationContext(), instance);
        assertNull(instance);

        methodProperty.deserializeAndSet(mapper.createParser("{\"testProperty\": \"Test Value\"}"), new StdDeserializationContext(), instance);
        assertEquals("Test Value", instance.getClass().getDeclaredMethod("setTestProperty", String.class).invoke(instance, "Test Value"));
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonMappingException, JsonProcessingException {
        // Test deserializeSetAndReturn method
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        Object instance = new Object();

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        when(deserializer.deserialize(any(JsonParser.class), any(StdDeserializationContext.class))).thenReturn("Test Value");

        methodProperty.withValueDeserializer(deserializer);
        methodProperty.withNullProvider(nullValueProvider);

        assertEquals(instance, methodProperty.deserializeSetAndReturn(mapper.createParser("{\"testProperty\": null}"), new StdDeserializationContext(), instance));
        assertNull(instance);

        assertEquals(instance, methodProperty.deserializeSetAndReturn(mapper.createParser("{\"testProperty\": null}"), new StdDeserializationContext(), instance));
        assertNull(instance);

        Object result = methodProperty.deserializeSetAndReturn(mapper.createParser("{\"testProperty\": \"Test Value\"}"), new StdDeserializationContext(), instance);
        assertEquals("Test Value", result);
        assertEquals("Test Value", instance.getClass().getDeclaredMethod("setTestProperty", String.class).invoke(instance, "Test Value"));
    }

    @Test
    public void testSet() throws JsonMappingException, JsonProcessingException {
        // Test set method
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        Object instance = new Object();
        String value = "Test Value";

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        methodProperty.set(instance, value);
        assertEquals(value, instance.getClass().getDeclaredMethod("getTestProperty", null).invoke(instance));
    }

    @Test
    public void testSetAndReturn() throws JsonMappingException, JsonProcessingException {
        // Test setAndReturn method
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        Object instance = new Object();
        String value = "Test Value";

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(instance, methodProperty.setAndReturn(instance, value));
        assertEquals(value, instance.getClass().getDeclaredMethod("getTestProperty", null).invoke(instance));
    }

    @Test
    public void testReadResolve() throws JsonMappingException, JsonProcessingException {
        // Test readResolve method
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty("testProperty");
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(BeanProperty.class, "testProperty");
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        Object instance = new Object();

        MethodProperty original = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        MethodProperty copy = original.readResolve();

        assertEquals(MethodProperty.class, copy.getClass());
        assertEquals(original.getBeanPropertyDefinition(), copy.getBeanPropertyDefinition());
        assertEquals(original.getType(), copy.getType());
        assertEquals(original.getTypeDeserializer(), copy.getTypeDeserializer());
        assertEquals(original.getContextAnnotations(), copy.getContextAnnotations());
        assertEquals(original.getAnnotated(), copy.getAnnotated());
        assertEquals(original.getSetter(), copy.getSetter());
        assertEquals(original._skipNulls, copy._skipNulls);
    }
}