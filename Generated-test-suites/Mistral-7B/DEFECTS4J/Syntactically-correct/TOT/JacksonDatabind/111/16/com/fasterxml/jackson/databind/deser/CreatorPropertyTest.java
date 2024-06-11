package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testSimpleDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition def = new BeanPropertyDefinition("testProperty", String.class, null, 0);
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.constructRawClass(String.class),
                PropertyName.construct("testWrapperProperty"),
                null,
                null,
                null,
                0,
                null,
                def
        );

        JsonDeserializer<String> deserializer = new StdDeserializer<>("");
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<>("");
        Object nullValueProvider = new NeverNull();

        CreatorProperty testProperty = new CreatorProperty(property, deserializer, nullValueProvider);

        String json = "{\"testProperty\": \"testValue\"}";

        Object result = mapper.readValue(json, testProperty.getType());

        assertEquals("testValue", result);
    }

    @Test
    public void testInjectableValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Define a bean with an injectable property
        class TestBean {
            @JsonProperty(value = "injectedProperty")
            private String injectedValue;

            public String getInjectedValue() {
                return injectedValue;
            }

            public void setInjectedValue(String value) {
                this.injectedValue = value;
            }
        }

        BeanPropertyDefinition injectedPropertyDef = new BeanPropertyDefinition("injectedProperty", String.class, null, 0);
        CreatorProperty injectedProperty = new CreatorProperty(
                PropertyName.construct("injectedProperty"),
                JavaType.constructRawClass(String.class),
                PropertyName.construct("testWrapperProperty"),
                null,
                null,
                null,
                0,
                null,
                injectedPropertyDef
        );

        TestBean testBean = new TestBean();
        ObjectMapper mapperWithInjectableValues = ObjectMapperFactory.builder()
                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
                .addHandler(new InjectableValuesHandler(new InjectableValuesContextImpl()))
                .build();

        String json = "{\"testProperty\": { \"injectedProperty\": \"testValue\" }}";
        testBean = mapperWithInjectableValues.readValue(json, TestBean.class);

        assertEquals("testValue", testBean.getInjectedValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingInjectableValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Define a bean with an injectable property
        class TestBean {
            @JsonProperty(value = "injectedProperty")
            private String injectedValue;

            public String getInjectedValue() {
                return injectedValue;
            }

            public void setInjectedValue(String value) {
                this.injectedValue = value;
            }
        }

        BeanPropertyDefinition injectedPropertyDef = new BeanPropertyDefinition("injectedProperty", String.class, null, 0);
        CreatorProperty injectedProperty = new CreatorProperty(
                PropertyName.construct("injectedProperty"),
                JavaType.constructRawClass(String.class),
                PropertyName.construct("testWrapperProperty"),
                null,
                null,
                null,
                0,
                null,
                injectedPropertyDef
        );

        TestBean testBean = new TestBean();
        ObjectMapper mapperWithInjectableValues = ObjectMapperFactory.builder()
                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
                .addHandler(new InjectableValuesHandler(new InjectableValuesContextImpl()))
                .build();

        String json = "{\"testProperty\": {} }"; // Missing injectedValue
        testBean = mapperWithInjectableValues.readValue(json, TestBean.class);
    }

    @Test
    public void testIgnoreProperty() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Define a bean with an ignore property
        class TestBean {
            @JsonIgnore
            private String ignoreValue;

            public String getIgnoreValue() {
                return ignoreValue;
            }

            public void setIgnoreValue(String value) {
                this.ignoreValue = value;
            }
        }

        BeanPropertyDefinition ignorePropertyDef = new BeanPropertyDefinition("ignoreProperty", String.class, null, 0);
        CreatorProperty ignoreProperty = new CreatorProperty(
                PropertyName.construct("ignoreProperty"),
                JavaType.constructRawClass(String.class),
                PropertyName.construct("testWrapperProperty"),
                null,
                null,
                null,
                0,
                true, // Mark as ignorable
                ignorePropertyDef
        );

        TestBean testBean = new TestBean();
        String json = "{\"ignoreProperty\": \"testValue\"}";

        Object result = mapper.readValue(json, TestBean.class);
        assertEquals(null, result.getClass().getDeclaredField("ignoreValue").get(result));
    }
}