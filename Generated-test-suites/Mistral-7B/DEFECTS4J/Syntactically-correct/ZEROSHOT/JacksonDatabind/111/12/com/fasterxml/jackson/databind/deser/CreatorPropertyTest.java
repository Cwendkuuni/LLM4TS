package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.serializer.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CreatorPropertyTest {

    private static final Object injectableValueId = new Object();
    private static final String propertyName = "testProperty";

    private ObjectBean objectBean;
    private CreatorProperty creatorProperty;

    @Before
    public void setUp() throws Exception {
        Class<?> objectBeanClass = Class.forName("com.fasterxml.jackson.databind.deser.ObjectBean");
        Constructor<?> constructor = objectBeanClass.getConstructor(String.class);
        objectBean = constructor.newInstance("test");

        creatorProperty = new CreatorProperty(
                PropertyName.construct(propertyName),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                injectableValueId,
                null);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();

        String injectedValue = "injected value";
        Object injectedObject = new Object() {
            @JsonDeserialize
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        };

        Field injectedField = injectedObject.getClass().getDeclaredField("value");
        injectedField.setAccessible(true);

        mapper.addValueIntrinsics(new StdDateFormat());
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        mapper.addValueIntrinsics(new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, SerializerProvider serializers, String format) throws IOException {
                serializers.getOutputFactory().writeValueAsString(value);
            }
        });

        mapper.registerModule(new SimpleModule().setSerializerFactory(new BeanSerializerFactory()));

        Map<Object, Object> valueInjectionMap = new HashMap<>();
        valueInjectionMap.put(injectableValueId, injectedValue);
        context.setValueInjection(valueInjectionMap);

        String json = "{\"testProperty\": \"test\"}";
        Object deserializedObject = mapper.readValue(new JsonFactory(), objectBeanClass);

        creatorProperty.inject(context, deserializedObject);

        String actualValue = (String) creatorProperty.get(deserializedObject);
        assertEquals("test", actualValue);

        Object injectedObjectValue = creatorProperty.findInjectableValue(context, deserializedObject);
        assertEquals(injectedValue, injectedObjectValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueFailed() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();

        String injectedValue = "injected value";
        Object injectedObject = new Object() {
            @JsonDeserialize
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        };

        Field injectedField = injectedObject.getClass().getDeclaredField("value");
        injectedField.setAccessible(true);

        mapper.addValueIntrinsics(new StdDateFormat());
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        mapper.addValueIntrinsics(new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, SerializerProvider serializers, String format) throws IOException {
                serializers.getOutputFactory().writeValueAsString(value);
            }
        });

        mapper.registerModule(new SimpleModule().setSerializerFactory(new BeanSerializerFactory()));

        Map<Object, Object> valueInjectionMap = new HashMap<>();
        context.setValueInjection(valueInjectionMap);

        String json = "{\"testProperty\": \"test\"}";
        Object deserializedObject = mapper.readValue(new JsonFactory(), objectBeanClass);

        String expectedMessage = String.format("No injectable value found for property '%s' (%s) of class %s", propertyName, getClass().getName(), objectBeanClass.getName());
        creatorProperty.findInjectableValue(context, deserializedObject);
    }

    @Test(expected = JsonMappingException.class)
    public void testIgnoreProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();

        ObjectMapper.DefaultTyping typing = ObjectMapper.DefaultTyping.STATIC;
        ContextualSerializationContext serializationContext = new ContextualSerializationContext(context, typing);

        creatorProperty.markAsIgnorable();

        String json = "{\"testProperty\": \"test\"}";
        Object deserializedObject = mapper.readValue(new JsonFactory(), objectBeanClass);

        mapper.writerWithContext(serializationContext);

        mapper.writeValueAsString(deserializedObject);
    }
}