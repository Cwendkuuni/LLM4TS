package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];

        SettableBeanProperty property = new SimpleBeanProperty("test", String.class);
        properties[0] = new ExtTypedProperty(property, new SimpleTypeDeserializer(String.class));
        nameToPropertyIndex.put("test", 0);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser parser = new DefaultParser();
        DeserializationContext context = new DefaultDeserializationContext();
        Object bean = new Object();

        parser.setText("{\"test\": \"type1\"}");
        Assert.assertTrue(handler.handleTypePropertyValue(parser, context, "test", bean));
        Assert.assertEquals("type1", handler._typeIds[0]);

        parser.setText("{\"test\": null}");
        Assert.assertFalse(handler.handleTypePropertyValue(parser, context, "test", bean));
        Assert.assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];

        SettableBeanProperty property = new SimpleBeanProperty("test", String.class);
        properties[0] = new ExtTypedProperty(property, new SimpleTypeDeserializer(String.class));
        nameToPropertyIndex.put("test", 0);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser parser = new DefaultParser();
        DeserializationContext context = new DefaultDeserializationContext();
        Object bean = new Object();

        parser.setText("{\"test\": \"value\"}");
        Assert.assertTrue(handler.handlePropertyValue(parser, context, "test", bean));
        Assert.assertEquals("value", handler._tokens[0].getText());

        parser.setText("{\"type\": \"test\"}");
        handler.handleTypePropertyValue(parser, context, "type", bean);
        Assert.assertEquals("test", handler._typeIds[0]);
    }

    @Test
    public void testComplete() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];

        SettableBeanProperty property1 = new SimpleBeanProperty("test1", String.class);
        properties[0] = new ExtTypedProperty(property1, new SimpleTypeDeserializer(String.class));
        SettableBeanProperty property2 = new SimpleBeanProperty("test2", Integer.class);
        properties[1] = new ExtTypedProperty(property2, new SimpleTypeDeserializer(Integer.class));
        nameToPropertyIndex.put("test1", 0);
        nameToPropertyIndex.put("test2", 1);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser parser = new DefaultParser();
        DeserializationContext context = new DefaultDeserializationContext();

        parser.setText("{" +
                "\"test1\": \"value1\"," +
                "\"test2\": null" +
                "}");

        Object bean = handler.start().complete(parser, context, null);
        Assert.assertNotNull(bean);
        Assert.assertEquals("value1", ((Map) bean).get("test1"));
        Assert.assertNull(((Map) bean).get("test2"));

        parser.setText("{" +
                "\"test1\": \"value1\"," +
                "\"test2\": \"value2\"" +
                "}");

        bean = handler.start().complete(parser, context, null);
        Assert.assertNotNull(bean);
        Assert.assertEquals("value1", ((Map) bean).get("test1"));
        Assert.assertEquals("value2", ((Map) bean).get("test2"));

        parser.setText("{" +
                "\"test1\": \"value1\"," +
                "\"type\": \"test2\"" +
                "}");

        handler.handleTypePropertyValue(parser, context, "type", bean);
        Assert.assertNull(((Map) bean).get("test2"));
        Assert.assertEquals("value1", ((Map) bean).get("test1"));
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];

        SettableBeanProperty property1 = new SimpleBeanProperty("test1", String.class);
        properties[0] = new ExtTypedProperty(property1, new SimpleTypeDeserializer(String.class));
        SettableBeanProperty property2 = new SimpleBeanProperty("test2", Integer.class);
        properties[1] = new ExtTypedProperty(property2, new SimpleTypeDeserializer(Integer.class));
        nameToPropertyIndex.put("test1", 0);
        nameToPropertyIndex.put("test2", 1);

        Object bean = new Object();

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property1, new SimpleTypeDeserializer(String.class));
        builder.addExternal(property2, new SimpleTypeDeserializer(Integer.class));
        ExternalTypeHandler handler = builder.build();

        JsonParser parser = new DefaultParser();
        DeserializationContext context = new DefaultDeserializationContext();
        PropertyName idProperty = new PropertyName("type");
        PropertyName idValueProperty = new PropertyName("id");
        ValueInstantiator valueInstantiator = new SimpleValueInstantiator();

        parser.setText("{" +
                "\"test1\": \"value1\"," +
                "\"type\": {" +
                "\"id\": \"MyType\"," +
                "\"className\": \"com.test.MyType\"" +
                "}," +
                "\"test2\": null" +
                "}");

        handler.start();
        Object result = handler.complete(parser, context, null, null);
        Assert.assertNotNull(result);
        Assert.assertEquals("value1", ((Map) result).get("test1"));
        Assert.assertNull(((Map) result).get("test2"));

        MyType myType = (MyType) valueInstantiator.createValueInstance(parser.getText(), null, context);
        Assert.assertNotNull(myType);

        parser.setText("{" +
                "\"test1\": \"value1\"" +
                "}");

        handler.start();
        result = handler.complete(parser, context, null, myType);
        Assert.assertNotNull(result);
        Assert.assertEquals("value1", ((Map) result).get("test1"));
        Assert.assertNull(((Map) result).get("test2"));
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];

        SettableBeanProperty property = new SimpleBeanProperty("test", String.class);
        properties[0] = new ExtTypedProperty(property, new SimpleTypeDeserializer(String.class));
        nameToPropertyIndex.put("test", 0);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser parser = new DefaultParser();
        DeserializationContext context = new DefaultDeserializationContext();

        parser.setText("{" +
                "\"test\": \"value\"" +
                "}");

        handler.start();
        Object bean = handler.complete(parser, context, null);
        Assert.assertNotNull(bean);
        Assert.assertEquals("value", ((Map) bean).get("test"));
    }

    @Test(expected = JsonMappingException.class)
    public void testCompleteWithMissingProperty() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];

        SettableBeanProperty property = new SimpleBeanProperty("test", String.class);
        properties[0] = new ExtTypedProperty(property, new SimpleTypeDeserializer(String.class));
        nameToPropertyIndex.put("test", 0);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser parser = new DefaultParser();
        DeserializationContext context = new DefaultDeserializationContext();

        parser.setText("{" +
                "\"type\": \"MyType\"" +
                "}");

        handler.start();
        Object bean = handler.complete(parser, context, null);
        Assert.fail();
    }

    private static class SimpleBeanProperty extends BeanProperty {
        public SimpleBeanProperty(String name, Class<?> type) {
            super(new PropertyName(name), type);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public Object deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
            set(bean, deserialize(p, ctxt));
            return null;
        }
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        private Class<?> _type;

        public SimpleTypeDeserializer(Class<?> type) {
            _type = type;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return deserialize(p, ctxt);
        }

        @Override
        public void resolveType(DeserializationContext ctxt, BeanProperty property) throws IOException {
            ctxt.handleUnknownProperty(property);
        }

        @Override
        public Object getTypeId(SerializerProvider serializers, BeanProperty property) throws IOException {
            return property.getName();
        }

        @Override
        public String getTypeIdVersion() {
            return null;
        }

        @Override
        public Class<?> getType(DeserializationContext ctxt) {
            return _type;
        }

        @Override
        public String getPropertyName(DeserializationContext ctxt) throws IOException {
            return "test";
        }
    }

    private static class SimpleValueInstantiator implements ValueInstantiator {
        @Override
        public Object createValueInstance(String valueAsString, TypeDeserializer deserializer, DeserializationContext context) throws IOException {
            ObjectMapper mapper = ObjectMapper.getDefaultInstance();
            return mapper.readValue(valueAsString, deserializer.getType());
        }
    }
}

class MyType {
    // Add some properties here if needed
}