package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTest;

public class CreatorPropertyTest extends JsonTest {

    public static class TestBean {
        private int value;

        @JsonCreator
        public TestBean(@JsonProperty("value") int value) {
            this.value = value;
        }

        @JsonProperty
        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @JsonProperty(value = "ignoredValue")
        public int getIgnoredValue() {
            return 0;
        }
    }

    @org.junit.Test
    public void testCreatorPropertyConstruction() throws Exception {
        JavaType javaType = mapper().getTypeFactory().constructType(TestBean.class);
        PropertyName propertyName = PropertyName.construct("value", javaType);
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getParameterName()).thenReturn("value");
        Annotations contextAnnotations = mock(Annotations.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 0, injectableValueId, null);
        assertEquals("value", property.getName().getSimpleName());
        assertEquals(TestBean.class, property.getType());
        assertNull(property.getFallbackSetter());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.isIgnorable());
        assertEquals(injectableValueId, property.getInjectableValueId());
    }

    @org.junit.Test
    public void testCreatorPropertyWithFallbackSetter() throws Exception {
        JavaType javaType = mapper().getTypeFactory().constructType(TestBean.class);
        PropertyName propertyName = PropertyName.construct("value", javaType);
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getParameterName()).thenReturn("value");
        Annotations contextAnnotations = mock(Annotations.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object injectableValueId = "injectValueId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        CreatorProperty property = new CreatorProperty(propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 0, injectableValueId, null) {
            @Override
            public SettableBeanProperty getFallbackSetter() {
                return fallbackSetter;
            }
        };
        assertEquals("value", property.getName().getSimpleName());
        assertEquals(TestBean.class, property.getType());
        assertEquals(fallbackSetter, property.getFallbackSetter());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.isIgnorable());
        assertEquals(injectableValueId, property.getInjectableValueId());
    }

    @org.junit.Test(expected = InvalidDefinitionException.class)
    public void testCreatorPropertyMissingFallbackSetter() throws Exception {
        JavaType javaType = mapper().getTypeFactory().constructType(TestBean.class);
        PropertyName propertyName = PropertyName.construct("value", javaType);
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getParameterName()).thenReturn("value");
        Annotations contextAnnotations = mock(Annotations.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 0, injectableValueId, null);
        property.deserializeAndSet(null, null, null);
    }

    @org.junit.Test(expected = InvalidDefinitionException.class)
    public void testCreatorPropertyMissingFallbackSetterWithContext() throws Exception {
        JavaType javaType = mapper().getTypeFactory().constructType(TestBean.class);
        PropertyName propertyName = PropertyName.construct("value", javaType);
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getParameterName()).thenReturn("value");
        Annotations contextAnnotations = mock(Annotations.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 0, injectableValueId, null);
        JsonParser p = createContextualParser();
        property.deserializeAndSet(p, null, null);
    }

    @org.junit.Test
    public void testCreatorPropertyFindAndInjectValue() throws Exception {
        JavaType javaType = mapper().getTypeFactory().constructType(TestBean.class);
        PropertyName propertyName = PropertyName.construct("value", javaType);
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getParameterName()).thenReturn("value");
        Annotations contextAnnotations = mock(Annotations.class);
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(propertyName, javaType, null, null, contextAnnotations, annotatedParameter, 0, injectableValueId, null);
        DeserializationContext ctxt = mapper()._context;
        Object beanInstance = new TestBean();
        Object injectedValue = createContextualValue("injectedValue");
        when(ctxt.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(injectedValue);
        property.inject(ctxt, beanInstance);
        assertEquals(injectedValue, beanInstance.getValue());
    }

    @org.junit.Test
    public void testCreatorPropertyIgnore() throws Exception {
        JavaType javaType = mapper().getTypeFactory().constructType(TestBean.class);
        PropertyName propertyName = PropertyName.construct("ignoredValue", javaType);
        AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getParameterName()).thenReturn("ignoredValue");
        Annotations contextAnnotations = mock(Annotations.class);
        Object injectableValueId = "injectValueId";
        CreatorProperty property = new CreatorProperty(propertyName, javaType, null, null, contextAnnotations, annotatedParameter, 0, injectableValueId, null) {
            @Override
            public void markAsIgnorable() {
                super.markAsIgnorable();
                _ignorable = true;
            }
        };
        DeserializationContext ctxt = mapper()._context;
        Object beanInstance = new TestBean();
        property.inject(ctxt, beanInstance);
        assertNull(beanInstance.getIgnoredValue());
        assertTrue(property.isIgnorable());
    }
}