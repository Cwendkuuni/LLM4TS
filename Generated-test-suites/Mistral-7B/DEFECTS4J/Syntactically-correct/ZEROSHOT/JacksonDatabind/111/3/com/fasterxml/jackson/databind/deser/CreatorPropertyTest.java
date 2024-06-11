package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    ObjectNode node;
    JsonParser parser;
    ObjectMapper mapper;
    DeserializationContext ctxt;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        node = mapper.createObjectNode();
        parser = mapper.getFactory().createParser(node);
        ctxt = mapper.getDeserializationContext();
    }

    @Test
    public void testConstructor() throws Exception {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        assertEquals("testProperty", property.getName().asString());
        assertEquals(JavaType.construct(String.class), property.getType());
        assertNull(property._annotated);
        assertEquals(0, property._creatorIndex);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
    }

    @Test
    public void testCopyConstructor() throws Exception {
        CreatorProperty srcProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        CreatorProperty copyProperty = new CreatorProperty(srcProperty, PropertyName.construct("newName"));

        assertEquals("newName", copyProperty.getName().asString());
        assertEquals("testProperty", copyProperty.getName().asOriginalName());
        assertEquals(JavaType.construct(String.class), copyProperty.getType());
        assertSame(srcProperty._annotated, copyProperty._annotated);
        assertEquals(0, copyProperty._creatorIndex);
        assertNull(copyProperty._injectableValueId);
        assertNull(copyProperty._fallbackSetter);
    }

    @Test
    public void testCopyConstructorWithDeserializer() throws Exception {
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class, ctxt);

        CreatorProperty srcProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        CreatorProperty copyProperty = new CreatorProperty(srcProperty, deserializer, null);

        assertEquals("testProperty", copyProperty.getName().asString());
        assertEquals("testProperty", copyProperty.getName().asOriginalName());
        assertEquals(JavaType.construct(String.class), copyProperty.getType());
        assertSame(srcProperty._annotated, copyProperty._annotated);
        assertEquals(0, copyProperty._creatorIndex);
        assertNull(copyProperty._injectableValueId);
        assertSame(deserializer, copyProperty._valueDeserializer);
        assertNull(copyProperty._fallbackSetter);
    }

    @Test
    public void testCopyConstructorWithNullProvider() throws Exception {
        NullValueProvider nva = new DefaultNullValueProvider();

        CreatorProperty srcProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        CreatorProperty copyProperty = new CreatorProperty(srcProperty, null, nva);

        assertEquals("testProperty", copyProperty.getName().asString());
        assertEquals("testProperty", copyProperty.getName().asOriginalName());
        assertEquals(JavaType.construct(String.class), copyProperty.getType());
        assertSame(srcProperty._annotated, copyProperty._annotated);
        assertEquals(0, copyProperty._creatorIndex);
        assertNull(copyProperty._injectableValueId);
        assertNull(copyProperty._valueDeserializer);
        assertSame(nva, copyProperty._nullProvider);
        assertNull(copyProperty._fallbackSetter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyConstructorWithNullAnnotated() throws Exception {
        CreatorProperty srcProperty = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        CreatorProperty copyProperty = new CreatorProperty(srcProperty, PropertyName.construct("newName"), null);
    }

    @Test
    public void testSetFallbackSetter() throws Exception {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        SettableBeanProperty fallbackSetter = new SimpleBeanProperty("testFallback");
        property.setFallbackSetter(fallbackSetter);

        assertSame(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testIsIgnorable() throws Exception {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        assertFalse(property.isIgnorable());

        property.markAsIgnorable();

        assertTrue(property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        Object id = new Object();

        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                id,
                PropertyMetadata.STANDARD_EMPTY);

        assertEquals(id, property.findInjectableValue(ctxt, null));
    }

    @Test
    public void testInject() throws Exception {
        Object beanInstance = mapper.readValue(node, Object.class);
        Object id = new Object();

        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                id,
                PropertyMetadata.STANDARD_EMPTY);

        property.inject(ctxt, beanInstance);

        assertEquals(id, property.getPropertyValue(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueException() throws Exception {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        property.findInjectableValue(ctxt, null);
    }
}