package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty property;

    @Before
    public void setUp() throws Exception {
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        BeanPropertyDefinition metadata = Mockito.mock(BeanPropertyDefinition.class);
        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        property = new ObjectIdValueProperty(objectIdReader, metadata) {
            @Override
            public JsonDeserializer<?> getValueDeserializer() {
                return deserializer;
            }
        };
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.getCurrentName()).thenReturn(property.getName());
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        property.deserializeAndSet(parser, ctxt, instance);
        assertNull(instance);

        Mockito.verify(ctxt, Mockito.times(1)).findObjectId(Mockito.any(), Mockito.eq(objectIdReader.generator), Mockito.eq(objectIdReader.resolver));
    }

    @Test
    public void testDeserializeAndSetValue() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();

        Mockito.when(parser.getCurrentName()).thenReturn(property.getName());
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getValueAsString()).thenReturn("123");

        ObjectNode node = Mockito.mock(ObjectNode.class);
        Mockito.when(ctxt.findValueSerializer(Mockito.eq(Object.class))).thenReturn(new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeValue(value);
                gen.writeFieldName("id");
                gen.writeValue(node);
            }
        });
        Mockito.when(ctxt.findObjectId(Mockito.eq(id), Mockito.eq(objectIdReader.generator), Mockito.eq(objectIdReader.resolver))).thenReturn(node);

        property.deserializeAndSet(parser, ctxt, instance);
        assertEquals("123", instance.getClass().getField("id").get(instance));

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(ctxt, Mockito.times(1)).findValueSerializer(Mockito.eq(Object.class));
        Mockito.verify(ctxt, Mockito.times(1)).findObjectId(Mockito.eq(id), Mockito.eq(objectIdReader.generator), Mockito.eq(objectIdReader.resolver));
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = property.deserializeSetAndReturn(parser, ctxt, instance);
        assertNull(result);

        Mockito.verify(ctxt, Mockito.times(1)).findObjectId(Mockito.any(), Mockito.eq(objectIdReader.generator), Mockito.eq(objectIdReader.resolver));
    }

    @Test
    public void testDeserializeSetAndReturnValue() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();

        Mockito.when(parser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
        Mockito.when(parser.getValueAsString()).thenReturn("123");

        Object result = property.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals("123", result);

        Mockito.verify(parser, Mockito.times(1)).nextToken();
        Mockito.verify(parser, Mockito.times(1)).getCurrentName();

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(ctxt, Mockito.times(1)).findObjectId(Mockito.eq(id), Mockito.eq(objectIdReader.generator), Mockito.eq(objectIdReader.resolver));
    }

    @Test
    public void testSetAndReturnNull() throws IOException {
        Object instance = new Object();

        assertNull(property.setAndReturn(instance, null));

        Mockito.verify(instance, Mockito.times(0)).setField(Mockito.any(), Mockito.eq(null));
    }

    @Test
    public void testSetAndReturnValue() throws IOException {
        Object instance = new Object();
        Object id = new Object();

        Mockito.doAnswer(invocation -> invocation.getArguments()[1] = id).when(instance).setField(Mockito.eq("id"), Mockito.any());

        Object result = property.setAndReturn(instance, id);
        assertEquals(id, result);

        Mockito.verify(instance, Mockito.times(1)).setField(Mockito.eq("id"), Mockito.eq(id));
    }

    @Test
    public void testWithName() throws IOException {
        PropertyName newName = Mockito.mock(PropertyName.class);

        SettableBeanProperty result = property.withName(newName);

        assertEquals(newName, result.getName());
        assertEquals(property, result.getBackRef());
        assertEquals(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};

        SettableBeanProperty result = property.withValueDeserializer(deserializer);

        assertEquals(deserializer, result.getValueDeserializer());
        assertEquals(property, result.getBackRef());
        assertEquals(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testWithNullProvider() throws IOException {
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        SettableBeanProperty result = property.withNullProvider(nva);

        assertEquals(nva, result.getNullValueProvider());
        assertEquals(property, result.getBackRef());
        assertEquals(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testAnnotation() throws IOException {
        Class<JsonDeserialize> annotationType = JsonDeserialize.class;

        SettableBeanProperty result = property.withName(property.getName());

        assertNull(result.getAnnotation(annotationType));
    }

    @Test
    public void testGetMember() throws IOException {
        AnnotatedMember result = property.getMember();

        assertNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.getCurrentName()).thenReturn(property.getName());
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        property.deserialize(parser, ctxt, instance);
        assertNull(instance);

        Mockito.verify(ctxt, Mockito.times(1)).findObjectId(Mockito.any(), Mockito.eq(objectIdReader.generator), Mockito.eq(objectIdReader.resolver));
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectNode node = Mockito.mock(ObjectNode.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        Mockito.when(node.isContainerNode()).thenReturn(true);
        Mockito.when(node.get("id")).thenReturn(Mockito.mock(JsonNode.class));

        Mockito.when(gen.writeStartObject()).thenReturn(node);
        Mockito.when(gen.writeFieldName("id")).thenReturn(node);
        Mockito.when(gen.writeValue(Mockito.anyObject())).thenReturn(node);
        Mockito.when(gen.writeEndObject()).thenReturn(node);

        Object instance = new Object();
        property.set(instance, new Object());

        property.serialize(instance, gen, serializers);
        Mockito.verify(gen, Mockito.times(1)).writeStartObject();
        Mockito.verify(gen, Mockito.times(1)).writeFieldName("id");
        Mockito.verify(gen, Mockito.times(1)).writeValue(Mockito.anyObject());
        Mockito.verify(gen, Mockito.times(1)).writeEndObject();
    }

    @Test
    public void testSerializeWithEmptyObject() throws IOException {
        Object instance = new Object();

        property.serialize(instance, Mockito.mock(JsonGenerator.class), Mockito.mock(SerializerProvider.class));

        Mockito.verify(instance, Mockito.times(0)).getClass();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object instance = new Object();

        property.setAndReturn(instance, new Object());
    }
}