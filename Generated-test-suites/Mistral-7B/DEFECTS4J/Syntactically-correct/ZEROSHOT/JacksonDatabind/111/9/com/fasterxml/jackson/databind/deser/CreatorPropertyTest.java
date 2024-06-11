package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;

public class CreatorPropertyTest {

    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreatorPropertyConstruction() {
        JavaType javaType = new TypeReference<Foo>() {}.getType();
        PropertyName propertyName = PropertyName.construct("testProperty", javaType);
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(BeanProperty property, JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(
                Foo.class.getDeclaredConstructor(String.class), 0, null, null, null);
        CreatorProperty creatorProperty = new CreatorProperty(
                propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 1, null, null);

        assertEquals(creatorProperty.getName(), "testProperty");
        assertEquals(creatorProperty.getType(), javaType);
        assertEquals(creatorProperty.getWrapperName(), null);
        assertNull(creatorProperty.getValueDeserializer());
        assertNull(creatorProperty.getNullProvider());
        assertEquals(creatorProperty.getCreatorIndex(), 1);
        assertNull(creatorProperty.getInjectableValueId());
        assertNull(creatorProperty.getFallbackSetter());
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyWithInjectableValue() throws IOException {
        JavaType javaType = new TypeReference<FooWithInjectableValue>() {}.getType();
        PropertyName propertyName = PropertyName.construct("testProperty", javaType);
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(BeanProperty property, JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(
                FooWithInjectableValue.class.getDeclaredConstructor(String.class), 0, null, null, new FromStringDeserializer(String.class));
        Object injectableValueId = "injectableValueId";
        CreatorProperty creatorProperty = new CreatorProperty(
                propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 1, injectableValueId, null);

        assertEquals(creatorProperty.getName(), "testProperty");
        assertEquals(creatorProperty.getType(), javaType);
        assertEquals(creatorProperty.getWrapperName(), null);
        assertEquals(creatorProperty.getValueDeserializer(), new FromStringDeserializer(String.class));
        assertEquals(creatorProperty.getNullProvider(), null);
        assertEquals(creatorProperty.getCreatorIndex(), 1);
        assertEquals(creatorProperty.getInjectableValueId(), injectableValueId);
        assertNull(creatorProperty.getFallbackSetter());
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyWithFallbackSetter() throws IOException {
        JavaType javaType = new TypeReference<FooWithFallbackSetter>() {}.getType();
        PropertyName propertyName = PropertyName.construct("testProperty", javaType);
        Object beanInstance = new FooWithFallbackSetter("initialValue");
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(BeanProperty property, JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(
                FooWithFallbackSetter.class.getDeclaredConstructor(), 1, null, null, null);
        CreatorProperty creatorProperty = new CreatorProperty(
                propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 1, null, beanInstance);

        assertEquals(creatorProperty.getName(), "testProperty");
        assertEquals(creatorProperty.getType(), javaType);
        assertEquals(creatorProperty.getWrapperName(), null);
        assertNull(creatorProperty.getValueDeserializer());
        assertNull(creatorProperty.getNullProvider());
        assertEquals(creatorProperty.getCreatorIndex(), 1);
        assertNull(creatorProperty.getInjectableValueId());
        assertNotNull(creatorProperty.getFallbackSetter());
        assertEquals(creatorProperty.getFallbackSetter().getName(), "testProperty2");
        assertEquals(creatorProperty.getFallbackSetter().getType(), javaType.getComponentType());
        assertEquals(creatorProperty.getFallbackSetter().getValueDeserializer(), null);
        assertEquals(creatorProperty.getFallbackSetter().getNullProvider(), null);
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyWithIgnorableFlag() throws IOException {
        JavaType javaType = new TypeReference<FooWithIgnorableFlag>() {}.getType();
        PropertyName propertyName = PropertyName.construct("testProperty", javaType);
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(BeanProperty property, JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(
                FooWithIgnorableFlag.class.getDeclaredConstructor(), 1, null, null, null);
        CreatorProperty creatorProperty = new CreatorProperty(
                propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 1, null, null);
        creatorProperty.markAsIgnorable();

        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyWithMissingFallbackSetter() throws IOException {
        JavaType javaType = new TypeReference<FooWithoutFallbackSetter>() {}.getType();
        PropertyName propertyName = PropertyName.construct("testProperty", javaType);
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(BeanProperty property, JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(
                FooWithoutFallbackSetter.class.getDeclaredConstructor(), 1, null, null, null);
        CreatorProperty creatorProperty = new CreatorProperty(
                propertyName, javaType, null, typeDeserializer, contextAnnotations, annotatedParameter, 1, null, null);

        assertThrows(InvalidDefinitionException.class, () -> {
            creatorProperty.setFallbackSetter(null);
        });
    }

    @JsonCreator
    static class Foo {
        private final String testProperty;

        @JsonProperty
        public String getTestProperty() {
            return testProperty;
        }

        public Foo(String testProperty) {
            this.testProperty = testProperty;
        }
    }

    @JsonCreator
    static class FooWithInjectableValue {
        @JsonProperty
        private final String testProperty;
        private final String injectedValue;

        public FooWithInjectableValue(String testProperty, @JsonProperty("injectValue") String injectedValue) {
            this.testProperty = testProperty;
            this.injectedValue = injectedValue;
        }

        public String getTestProperty() {
            return testProperty;
        }

        public String getInjectedValue() {
            return injectedValue;
        }
    }

    @JsonCreator
    static class FooWithFallbackSetter {
        @JsonProperty
        private final String testProperty;
        @JsonProperty("testProperty2")
        private SettableBeanProperty fallbackSetter;

        public FooWithFallbackSetter(String testProperty) {
            this.testProperty = testProperty;
        }

        public void setFallbackSetter(SettableBeanProperty fallbackSetter) {
            this.fallbackSetter = fallbackSetter;
        }

        public String getTestProperty() {
            return testProperty;
        }
    }

    @JsonCreator
    static class FooWithIgnorableFlag {
        @JsonProperty
        private final int testProperty;

        public FooWithIgnorableFlag(int testProperty) {
            this.testProperty = testProperty;
        }

        public int getTestProperty() {
            return testProperty;
        }

        @JsonIgnore
        public boolean isIgnorable() {
            return true;
        }
    }

    @JsonCreator
    static class FooWithoutFallbackSetter {
        @JsonProperty
        private final int testProperty;

        public FooWithoutFallbackSetter(int testProperty) {
            this.testProperty = testProperty;
        }

        public int getTestProperty() {
            return testProperty;
        }
    }
}