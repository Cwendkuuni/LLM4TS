package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testCreatorPropertyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Class<Foo> fooClass = Foo.class;

        JavaType fooType = mapper.getTypeFactory().constructType(fooClass);

        Annotations contextAnnotations = new Annotations();

        // Create a simple test case with no injection
        CreatorProperty property1 = new CreatorProperty(
                PropertyName.construct("name1"), fooType, null, null, contextAnnotations, null, -1, null, PropertyMetadata.STD_OBJECT);

        JsonDeserializer<String> stringDeser = new FromStringDeserializer();
        CreatorProperty property2 = new CreatorProperty(
                property1, stringDeser, null);

        Foo foo1 = mapper.readValue(
                "{ \"name1\": \"value1\" }", fooType);

        assertEquals("value1", foo1.getName1());

        // Create a test case with injection
        Object injectableValueId = "injectedValueId";
        CreatorProperty property3 = new CreatorProperty(
                property1, null, null, null, contextAnnotations, null, -1, injectableValueId, PropertyMetadata.STD_OBJECT);

        Object beanInstance = mapper.findAndRegisterSerializers(fooClass);

        Object injectedValue = mapper.findInjectableValue(injectableValueId, property3, beanInstance);
        assertEquals("injectedValue", injectedValue.toString());

        Foo foo2 = mapper.readValue(
                "{ \"name1\": \"value1\", \"injectedProperty\": \"injectedValue\" }", fooType);

        assertEquals("value1", foo2.getName1());
        assertEquals("injectedValue", foo2.getInjectedProperty());

        // Test error cases
        try {
            mapper.readValue(
                    "{ \"name1\": \"value1\", \"injectedProperty\": null }", fooType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            mapper.readValue(
                    "{ \"name1\": null, \"injectedProperty\": \"injectedValue\" }", fooType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            mapper.readValue(
                    "{ \"name1\": \"value1\", \"injectedProperty\": {} }", fooType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test setter methods
        Foo foo3 = new Foo();
        property3.setFallbackSetter(new CreatorProperty(
                property1, null, null, null, contextAnnotations, null, -1, null, PropertyMetadata.STD_OBJECT) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                super.set(instance, "setterValue");
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return "setterReturnValue";
            }
        });

        property3.inject(mapper.getDeserializationContext(), foo3);

        assertEquals("setterValue", foo3.getName1());
        assertEquals("setterReturnValue", foo3.getInjectedProperty());
    }

    public static class Foo {
        private String name1;
        private Object injectedProperty;

        public String getName1() {
            return name1;
        }

        @JsonDeserialize(using = CreatorProperty.class)
        public void setName1(String name1) {
            this.name1 = name1;
        }

        public Object getInjectedProperty() {
            return injectedProperty;
        }

        public void setInjectedProperty(Object injectedProperty) {
            this.injectedProperty = injectedProperty;
        }
    }
}