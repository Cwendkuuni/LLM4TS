package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Test class for CreatorProperty.java
 */
public class CreatorPropertyTest {

    @Test
    public void testCreatorPropertyConstruction() throws Exception {
        // Test construction with valid parameters
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = new TypeReference<Integer>() {}.getType();
        PropertyName wrapperName = PropertyName.construct("wrapperTestProperty");
        TypeDeserializer typeDeser = new FromStringDeserializer();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.constructParameterAnnotation(JsonCreator.class, "testParam");
        int index = 0;
        Object injectableValueId = "testId";
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        // Test constructor with null fallback setter
        assertEquals(null, creatorProperty._fallbackSetter);

        // Test constructor with valid fallback setter
        SettableBeanProperty fallbackSetter = new TestSettableBeanProperty();
        creatorProperty = new CreatorProperty(creatorProperty, fallbackSetter.getName());
        assertEquals(fallbackSetter, creatorProperty._fallbackSetter);

        // Test constructor with valid value deserializer and null value provider
        JsonDeserializer<?> valueDeserializer = new TestJsonDeserializer<>();
        NullValueProvider nullValueProvider = null;
        creatorProperty = new CreatorProperty(creatorProperty, valueDeserializer, nullValueProvider);
        assertEquals(valueDeserializer, creatorProperty._valueDeserializer);
        assertEquals(nullValueProvider, creatorProperty._nullProvider);

        // Test constructor with valid value deserializer and valid null value provider
        NullValueProvider nullValueProvider2 = new TestNullValueProvider();
        creatorProperty = new CreatorProperty(creatorProperty, valueDeserializer, nullValueProvider2);
        assertEquals(valueDeserializer, creatorProperty._valueDeserializer);
        assertEquals(nullValueProvider2, creatorProperty._nullProvider);
    }

    @Test
    public void testCreatorPropertyMethods() throws Exception {
        // Test getters and setters
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = new TypeReference<Integer>() {}.getType();
        PropertyName wrapperName = PropertyName.construct("wrapperTestProperty");
        TypeDeserializer typeDeser = new FromStringDeserializer();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.constructParameterAnnotation(JsonCreator.class, "testParam");
        int index = 0;
        Object injectableValueId = "testId";
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty._annotated);
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(null, creatorProperty._fallbackSetter);

        // Test setters
        SettableBeanProperty fallbackSetter = new TestSettableBeanProperty();
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, creatorProperty._fallbackSetter);

        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws Exception {
        // Test missing setter
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = new TypeReference<Integer>() {}.getType();
        PropertyName wrapperName = PropertyName.construct("wrapperTestProperty");
        TypeDeserializer typeDeser = new FromStringDeserializer();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.constructParameterAnnotation(JsonCreator.class, "testParam");
        int index = 0;
        Object injectableValueId = "testId";
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        creatorProperty.deserializeAndSet(null, null, null);
    }
}

class TestJsonDeserializer<T> extends JsonDeserializer<T> {
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }
}

class TestNullValueProvider implements NullValueProvider {
    @Override
    public Object getNullValue(DeserializationContext ctxt) throws IOException {
        return null;
    }
}

class TestSettableBeanProperty {
    private String name;

    public TestSettableBeanProperty() {
        this.name = "testSetter";
    }

    public String getName() {
        return name;
    }

    public void set(Object instance, Object value) throws IOException {
    }

    public Object setAndReturn(Object instance, Object value) throws IOException {
        return null;
    }
}