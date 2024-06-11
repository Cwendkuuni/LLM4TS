package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    private ObjectMapper mapper;
    private CreatorProperty property;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        Class<?> testClass = TestClass.class;
        JavaType javaType = mapper.getTypeFactory().constructType(testClass);
        PropertyName name = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter(TestClass.class.getConstructor(String.class), 0);
        int index = 0;
        Object injectableValueId = "injectValueId";
        property = new CreatorProperty(name, javaType, null, typeDeser, contextAnnotations, param, index, injectableValueId, null);
    }

    @Test
    public void testGetName() {
        assertEquals("testProperty", property.getName().getShortName());
    }

    @Test
    public void testGetType() {
        assertEquals(String.class, property.getType());
    }

    @Test
    public void testGetWrapperName() {
        assertNull(property.getWrapperName());
    }

    @Test
    public void testGetTypeDeserializer() {
        assertNull(property.getTypeDeserializer());
    }

    @Test
    public void testGetContextAnnotations() {
        assertEquals(Annotations.class, property.getContextAnnotations().getClass());
    }

    @Test
    public void testGetAnnotated() {
        Annotation jsonCreatorAnnotation = property.getAnnotated().getAnnotation(JsonCreator.class);
        assertNotNull(jsonCreatorAnnotation);
        Annotation jsonPropertyAnnotation = property.getAnnotated().getAnnotation(JsonProperty.class);
        assertNull(jsonPropertyAnnotation);
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(property.isIgnorable());
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = new StdSerializer<Object>(Object.class) {}.withClass(Object.class);
        property.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, property._fallbackSetter);
    }

    @Test(expected = IOException.class)
    public void testFindInjectableValueThrowsIOExceptionWhenNoInjectableValueIdConfigured() throws Exception {
        DeserializationContext context = mapper.getDeserializationConfig().contextInstance();
        property.findInjectableValue(context, null);
    }

    @Test
    public void testFindInjectableValueReturnsValue() throws Exception {
        DeserializationContext context = mapper.getDeserializationConfig().contextInstance();
        context.addValueIntrinsics(new TestValueIntrinsics("injectValue"));
        Object injectedValue = property.findInjectableValue(context, null);
        assertEquals("injectValue", injectedValue);
    }

    @Test
    public void testInjectSetsValue() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("[\"injectValue\"]");
        Object beanInstance = new TestClass("initialValue");
        property.inject(context, beanInstance);
        assertEquals("injectValue", property.get(beanInstance));
    }

    @JsonDeserialize(using = CreatorPropertyDeserializer.class)
    static class TestClass {
        private String testProperty;

        @JsonCreator
        public TestClass(String testProperty) {
            this.testProperty = testProperty;
        }

        public String getTestProperty() {
            return testProperty;
        }

        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }
    }

    static class CreatorPropertyDeserializer extends JsonDeserializer<TestClass> {
        @Override
        public TestClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object beanInstance = ctxt.deserializationContext.getInstanceProvider().getBeans(TestClass.class).next();
            property.inject(ctxt, beanInstance);
            return beanInstance;
        }
    }

    static class TestValueIntrinsics {
        private String value;

        TestValueIntrinsics(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}