package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing ExternalTypeHandler class")
public class ExternalTypeHandlerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @DisplayName("Test constructor and initialization")
    @Test
    public void testConstructor() throws JsonProcessingException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        assertEquals(2, handler._properties.length);
        assertEquals(0, handler._nameToPropertyIndex.size());
        assertEquals(null, handler._typeIds);
        assertEquals(null, handler._tokens);

        ExternalTypeHandler clone = handler.start();
        assertEquals(2, clone._properties.length);
        assertEquals(0, clone._nameToPropertyIndex.size());
        assertNotSame(handler._properties, clone._properties);
        assertNull(clone._typeIds);
        assertNull(clone._tokens);
    }

    @DisplayName("Test handleTypePropertyValue method")
    @Test
    public void testHandleTypePropertyValue() throws JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        String propertyName = "property1";
        String typeId1 = "type1";
        String jsonString = "{\"property1\":\"" + typeId1 + "\", \"otherProperty\":\"value\"}";

        ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty(node, "property1"), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty(node, "otherProperty"), null);

        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put(property1.getProperty().getName(), 0);
        nameToPropertyIndex.put(property2.getProperty().getName(), 1);

        ArrayList<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null).start();

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        assertTrue(handler.handleTypePropertyValue(parser, context, propertyName, bean));
        assertEquals(typeId1, handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler.complete(parser, context, bean);
        assertNull(handler._typeIds);
        assertNull(handler._tokens);
    }

    @DisplayName("Test handlePropertyValue method")
    @Test
    public void testHandlePropertyValue() throws JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        String propertyName = "property1";
        String jsonString = "{\"property1\":\"value\", \"otherProperty\":\"value\"}";

        ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty(node, "property1"), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty(node, "otherProperty"), null);

        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put(property1.getProperty().getName(), 0);
        nameToPropertyIndex.put(property2.getProperty().getName(), 1);

        ArrayList<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null).start();

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        assertTrue(handler.handlePropertyValue(parser, context, propertyName, bean));
        assertNull(handler._typeIds);
        assertNull(handler._tokens);

        handler.complete(parser, context, bean);
        assertNull(handler._typeIds);
        assertNull(handler._tokens);
    }

    @DisplayName("Test complete method")
    @Test
    public void testComplete() throws JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        String propertyName1 = "property1";
        String propertyName2 = "property2";
        String typeId1 = "type1";
        String jsonString = "{\"property1\":\"" + typeId1 + "\", \"property2\":\"value\"}";

        ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty(node, propertyName1), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty(node, propertyName2), null);

        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put(property1.getProperty().getName(), 0);
        nameToPropertyIndex.put(property2.getProperty().getName(), 1);

        ArrayList<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null).start();

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, propertyName1, bean);
        handler.handlePropertyValue(parser, context, propertyName2, bean);
        handler.complete(parser, context, bean);

        Object result = handler.complete(parser, context, null, null);
        assertNotNull(result);

        handler.complete(parser, context, result);
        assertEquals(result, handler.complete(parser, context, null, null));
    }

    @DisplayName("Test complete method with PropertyValueBuffer and PropertyBasedCreator")
    @Test
    public void testCompleteWithCreator() throws JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        String propertyName1 = "property1";
        String propertyName2 = "property2";
        String typeId1 = "type1";
        String jsonString = "{\"property1\":\"" + typeId1 + "\", \"property2\":\"value\"}";

        ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty(node, propertyName1), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty(node, propertyName2), null);

        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put(property1.getProperty().getName(), 0);
        nameToPropertyIndex.put(property2.getProperty().getName(), 1);

        ArrayList<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property1, new SimpleTypeDeserializer(String.class));
        builder.addExternal(property2, new SimpleTypeDeserializer(Integer.class));
        ExternalTypeHandler handler = builder.build();

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext context = mapper.getDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new MapPropertyBasedCreator(Map.class);

        handler.handleTypePropertyValue(parser, context, propertyName1, null);
        handler.handlePropertyValue(parser, context, propertyName2, null);
        handler.complete(parser, context, buffer, creator);

        Object result = creator.build(context, buffer);
        assertNotNull(result);
        assertEquals(result.getClass(), Map.class);

        handler.handleTypePropertyValue(parser, context, propertyName1, result);
        handler.handlePropertyValue(parser, context, propertyName2, result);
        Object finalResult = handler.complete(parser, context, result);
        assertNotNull(finalResult);
        assertEquals(finalResult.getClass(), Map.class);
    }
}