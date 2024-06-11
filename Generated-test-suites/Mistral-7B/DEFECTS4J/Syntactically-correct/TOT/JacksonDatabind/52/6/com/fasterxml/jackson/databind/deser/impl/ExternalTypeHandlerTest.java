package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private ExternalTypeHandler handler;

    @Before
    public void setup() throws JsonProcessingException {
        mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("name1", "type1");
        node.put("name2", "type2");
        node.put("type1", "[1,2,3]");
        handler = mapper.readValue(node.toString(), ExternalTypeHandler.class);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name1\":\"type3\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        boolean result = handler.handleTypePropertyValue(p, ctxt, "name1", bean);
        Assert.assertTrue(result);
        Assert.assertEquals("type3", handler._typeIds[0]);
    }

    @Test
    public void testHandleTypePropertyValue_MissingProperty() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"unknown\":\"type3\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        boolean result = handler.handleTypePropertyValue(p, ctxt, "unknown", bean);
        Assert.assertFalse(result);
    }

    @Test
    public void testHandleTypePropertyValue_MissingTypeId() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name1\":null}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        boolean result = handler.handleTypePropertyValue(p, ctxt, "name1", bean);
        Assert.assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue_EmptyValue() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name1\":null}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        boolean result = handler.handlePropertyValue(p, ctxt, "name1", bean);
        Assert.assertTrue(result);
        Assert.assertNull(handler._typeIds[0]);
        Assert.assertNull(_tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_StringValue() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name1\":\"value\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        boolean result = handler.handlePropertyValue(p, ctxt, "name1", bean);
        Assert.assertTrue(result);
        Assert.assertEquals("value", handler._tokens[0].currentValue());
    }

    @Test
    public void testHandlePropertyValue_ComplexValue() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name1\":[1,2,3]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        boolean result = handler.handlePropertyValue(p, ctxt, "name1", bean);
        Assert.assertTrue(result);
        Assert.assertNotNull(handler._typeIds[0]);
        Assert.assertNotNull(_tokens[0]);
        Assert.assertEquals("[1,2,3]", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_MissingTypeProperty() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name1\":[1,2,3], wrongName:[4,5,6]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        boolean result = handler.handlePropertyValue(p, ctxt, "wrongName", bean);
        Assert.assertFalse(result);
    }

    @Test
    public void testComplete_Empty() throws IOException {
        Object bean = handler.complete(mapper.getFactory().getParser("{}"), mapper.getDeserializationContext(), null);
        Assert.assertNotNull(bean);
        Assert.assertArrayEquals(new Object[handler._properties.size()], bean.getClass().getDeclaredFields());
    }

    @Test
    public void testComplete_AllValuesPresent() throws IOException {
        Object bean = handler.complete(mapper.getFactory().getParser("{\"name1\":\"type1\", \"name2\":[1,2,3]}"),
                mapper.getDeserializationContext(), null);
        Assert.assertNotNull(bean);
        SettableBeanProperty prop1 = handler._properties.get(0).getProperty();
        SettableBeanProperty prop2 = handler._properties.get(1).getProperty();
        Assert.assertEquals("type1", prop1.get(bean));
        Assert.assertEquals(3, prop2.get(bean).length);
        Object[] array = (Object[]) prop2.get(bean);
        Assert.assertEquals(1, array[0]);
        Assert.assertEquals(2, array[1]);
        Assert.assertEquals(3, array[2]);
    }

    @Test
    public void testComplete_MissingValue() throws IOException {
        Object bean = handler.complete(mapper.getFactory().getParser("{\"name1\":\"type1\"}"), mapper.getDeserializationContext(), null);
        Assert.assertNotNull(bean);
        SettableBeanProperty prop1 = handler._properties.get(0).getProperty();
        Assert.assertEquals("type1", prop1.get(bean));
        SettableBeanProperty prop2 = handler._properties.get(1).getProperty();
        Assert.assertNull(prop2.get(bean));
    }

    @Test
    public void testComplete_MissingTypeId() throws IOException {
        Object bean = handler.complete(mapper.getFactory().getParser("{\"name1\":null, name2:[1,2,3]}"), mapper.getDeserializationContext(), null);
        Assert.assertNotNull(bean);
        SettableBeanProperty prop1 = handler._properties.get(0).getProperty();
        Assert.assertNull(prop1.get(bean));
        SettableBeanProperty prop2 = handler._properties.get(1).getProperty();
        Assert.assertArrayEquals(new Object[]{1, 2, 3}, prop2.get(bean));
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_MissingProperty() throws IOException {
        Object bean = handler.complete(mapper.getFactory().getParser("{\"name1\":\"type1\"}"), mapper.getDeserializationContext(), null);
        Assert.assertNotNull(bean);
        handler._properties.get(1).getProperty().set(bean, new Object());
        handler._properties.get(1).getProperty().get(bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_InvalidTypeId() throws IOException {
        handler._properties.get(0).setTypeDeserializer(null);
        Object bean = handler.complete(mapper.getFactory().getParser("{\"name1\":null, name2:\"invalidTypeId\"}"), mapper.getDeserializationContext(), null);
        Assert.assertNotNull(bean);
        SettableBeanProperty prop2 = handler._properties.get(1).getProperty();
        prop2.set(bean, new Object());
        prop2.get(bean);
    }
}