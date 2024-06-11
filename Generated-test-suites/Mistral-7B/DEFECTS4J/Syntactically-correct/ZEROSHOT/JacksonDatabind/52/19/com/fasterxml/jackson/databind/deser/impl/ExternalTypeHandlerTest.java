package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler createTestObject(List<SettableBeanProperty> properties) {
        Builder builder = new Builder();
        for (SettableBeanProperty property : properties) {
            TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
            when(typeDeserializer.getPropertyName()).thenReturn(property.getName());
            builder.addExternal(property, typeDeserializer);
        }
        return builder.build();
    }

    @org.junit.Test
    public void testHandleTypePropertyValue() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        String propName = "typeProperty";
        SettableBeanProperty typeProperty = new SettableBeanProperty(null, new PropertyName(propName));
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);
        properties.add(typeProperty);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};

        when(parser.getText()).thenReturn("typeId");
        handler.handleTypePropertyValue(parser, context, propName, bean);
        assertEquals("typeId", handler._typeIds[1]);
        assertNull(handler._tokens[1]);

        handler = new ExternalTypeHandler(handler);
        handler.complete(parser, context, bean);
        assertEquals("typeId", handler._properties[1].getProperty().getType().getSimpleName());
    }

    @org.junit.Test
    public void testHandlePropertyValue() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        String propName = "typeProperty";
        SettableBeanProperty typeProperty = new SettableBeanProperty(null, new PropertyName(propName));
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);
        properties.add(typeProperty);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};

        when(parser.getText()).thenReturn("typeId");
        handler.handlePropertyValue(parser, context, propName, bean);
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);

        handler = new ExternalTypeHandler(handler);
        handler.complete(parser, context, bean);
        assertEquals("typeId", handler._properties[1].getProperty().getType().getSimpleName());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testHandlePropertyValueMissingProperty() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};

        handler.handlePropertyValue(parser, context, "missingProperty", bean);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testHandlePropertyValueMissingTypeProperty() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        String propName = "typeProperty";
        SettableBeanProperty typeProperty = new SettableBeanProperty(null, new PropertyName(propName));
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);
        properties.add(typeProperty);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};

        when(parser.getText()).thenReturn("typeId");
        handler.handlePropertyValue(parser, context, "property", bean);
    }

    @org.junit.Test
    public void testComplete() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        String propName = "typeProperty";
        SettableBeanProperty typeProperty = new SettableBeanProperty(null, new PropertyName(propName));
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);
        properties.add(typeProperty);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};

        when(parser.getText()).thenReturn("typeId");
        handler.handleTypePropertyValue(parser, context, propName, bean);
        handler.handlePropertyValue(parser, context, "property", bean);
        handler.handlePropertyValue(parser, context, propName, bean);

        handler = new ExternalTypeHandler(handler);
        handler.complete(parser, context, bean);
        assertEquals("typeId", handler._properties[1].getProperty().getType().getSimpleName());
    }

    @org.junit.Test
    public void testCompleteWithNullValues() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        String propName = "typeProperty";
        SettableBeanProperty typeProperty = new SettableBeanProperty(null, new PropertyName(propName));
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);
        properties.add(typeProperty);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};

        handler.handleTypePropertyValue(parser, context, propName, bean);
        handler.handlePropertyValue(parser, context, "property", bean);
        handler.handlePropertyValue(parser, context, propName, bean);

        handler = new ExternalTypeHandler(handler);
        handler.complete(parser, context, bean);
        assertEquals(null, handler._properties[0].getProperty().get(bean));
        assertEquals("typeId", handler._properties[1].getProperty().getType().getSimpleName());
    }

    @org.junit.Test
    public void testCompleteWithNaturalType() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};

        when(parser.getText()).thenReturn("42");
        handler.handlePropertyValue(parser, context, "property", bean);

        handler = new ExternalTypeHandler(handler);
        handler.complete(parser, context, bean);
        assertEquals(Integer.valueOf(42), handler._properties[0].getProperty().get(bean));
    }

    @org.junit.Test
    public void testCompleteWithCreator() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        String typePropertyName = "typeProperty";
        String creatorPropertyName = "creatorProperty";

        SettableBeanProperty typeProperty = new SettableBeanProperty(null, new PropertyName(typePropertyName)) {
            @Override
            public int getCreatorIndex() {
                return 0;
            }
        };
        SettableBeanProperty creatorProperty = new SettableBeanProperty(null, new PropertyName(creatorPropertyName)) {
            @Override
            public int getCreatorIndex() {
                return 1;
            }
        };
        SettableBeanProperty property = new SettableBeanProperty(null, new PropertyName("property"));
        properties.add(property);
        properties.add(typeProperty);
        properties.add(creatorProperty);

        ExternalTypeHandler handler = createTestObject(properties);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object() {};
        Object creator = new Object() {};

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.getPropertyName()).thenReturn(creatorPropertyName);
        when(typeDeserializer.createInstance(context)).thenReturn(creator);

        TypeFactory typeFactory = mock(TypeFactory.class);
        when(typeDeserializer.getTypeFactory()).thenReturn(typeFactory);
        when(typeFactory.constructType(parser.getCurrentName())).thenReturn(String.class);
        when(parser.getText()).thenReturn("String");

        handler.handleTypePropertyValue(parser, context, typePropertyName, bean);
        handler.handlePropertyValue(parser, context, creatorPropertyName, bean);
        handler.handlePropertyValue(parser, context, "property", bean);

        handler = new ExternalTypeHandler(handler);
        handler.complete(parser, context, bean);
        assertEquals(creator, handler._properties[1].getProperty().get(bean));
        assertEquals("String", handler._properties[2].getProperty().getType().getSimpleName());
    }
}