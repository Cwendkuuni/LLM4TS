package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class CreatorPropertyTest {

    private ObjectNode node;
    private JsonDeserializer<CreatorProperty> deserializer;
    private ObjectMapper mapper;

    @BeforeClass
    public void setup() throws Exception {
        node = (ObjectNode) new ObjectMapper().createObjectNode();
        deserializer = mock(JsonDeserializer.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreatorPropertySerializaton() throws JsonProcessingException {
        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                "testValueId",
                PropertyMetadata.empty());

        ObjectWriter writer = mapper.writer().withRootValue(node);
        writer.writeValue(new File("test.json"), property);

        ObjectNode readValue = (ObjectNode) mapper.readValue(new File("test.json"), ObjectNode.class);
        CreatorProperty readProperty = mapper.treeToValue(readValue, CreatorProperty.class);

        assertEquals(property, readProperty);
    }

    @Test
    public void testFindInjectableValue() {
        Object beanInstance = new Object();
        DeserializationContext context = mock(DeserializationContext.class);

        when(context.findInjectableValue("testValueId", any(CreatorProperty.class), eq(beanInstance)))
                .thenReturn("testValue");

        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                "testValueId",
                PropertyMetadata.empty());

        Object value = property.findInjectableValue(context, beanInstance);

        assertEquals("testValue", value);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testFindInjectableValueWithoutValueId() {
        Object beanInstance = new Object();
        DeserializationContext context = mock(DeserializationContext.class);

        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.empty());

        property.findInjectableValue(context, beanInstance);
    }

    @Test
    public void testInject() {
        Object beanInstance = new Object();
        DeserializationContext context = mock(DeserializationContext.class);

        when(context.findInjectableValue("testValueId", any(CreatorProperty.class), eq(beanInstance)))
                .thenReturn("testValue");

        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                "testValueId",
                PropertyMetadata.empty());

        property.inject(context, beanInstance);

        assertEquals("testValue", property.getName().get(beanInstance));
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        Object beanInstance = new Object();
        JsonParser p = mapper.treeToValues(node, Object.class)[0];

        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                "testValueId",
                PropertyMetadata.empty());

        property.deserializeAndSet(p, null, beanInstance);

        assertEquals("testValue", property.getName().get(beanInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException {
        Object beanInstance = new Object();
        JsonParser p = mapper.treeToValues(node, Object.class)[0];

        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                "testValueId",
                PropertyMetadata.empty());

        Object value = property.deserializeSetAndReturn(p, null, beanInstance);

        assertEquals("testValue", value);
        assertEquals("testValue", property.getName().get(beanInstance));
    }

    @Test
    public void testSet() throws JsonProcessingException {
        Object beanInstance = new Object();
        JsonParser p = mapper.treeToValues(node, Object.class)[0];

        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                "testValueId",
                PropertyMetadata.empty());

        property.set(beanInstance, "testValue");

        assertEquals("testValue", property.getName().get(beanInstance));
    }

    @Test
    public void testSetFallbackSetter() {
        Object beanInstance = new Object();
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);

        CreatorProperty property = new CreatorProperty(
                PropertyName.forPrefix("test"),
                JavaType.of(String.class),
                PropertyName.forPrefix("testWrapper"),
                null,
                Annotations.empty(),
                null,
                0,
                "testValueId",
                PropertyMetadata.empty());

        property.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, property._fallbackSetter);
    }
}