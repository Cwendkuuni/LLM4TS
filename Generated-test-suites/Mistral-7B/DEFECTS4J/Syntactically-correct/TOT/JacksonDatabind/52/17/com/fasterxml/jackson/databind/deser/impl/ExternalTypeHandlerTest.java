package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.*;

/**
 * Test class for {@link ExternalTypeHandler}.
 */
public class ExternalTypeHandlerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public static void setup() throws Exception {
        MAPPER.registerModule(new SimpleModule() {{
            addDeserializer(String[].class, new StringArrayDeserializer());
            addDeserializer(ExternalTypeHandler.class, new ExternalTypeHandlerDeserializer());
        }});
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", "type1");
        node.put("prop2", "type2");

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();
        ExtTypedProperty[] properties = handler._properties;

        // Test handling of a valid type property value.
        assertFalse(handler.handleTypePropertyValue(MAPPER.createParser(node), MAPPER.context(), "prop1", new Object()));
        assertEquals("type1", properties[0].getProperty().getType());
        assertEquals("type1", handler._typeIds[0]);

        // Test handling of an invalid type property value.
        assertFalse(handler.handleTypePropertyValue(MAPPER.createParser(node), MAPPER.context(), "prop3", new Object()));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", "value2");

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();
        ExtTypedProperty[] properties = handler._properties;

        // Test handling of a value that doesn't correspond to any type property.
        assertFalse(handler.handlePropertyValue(MAPPER.createParser(node), MAPPER.context(), "prop3", new Object()));

        // Test handling of a value that corresponds to a type property.
        assertFalse(handler.handlePropertyValue(MAPPER.createParser(node), MAPPER.context(), "prop1", new Object()));
        assertEquals("value1", properties[0].getProperty().getValue(new Object()));
        assertNull(handler._typeIds[0]);

        // Test handling of a value that corresponds to a type property with a type deserializer.
        JsonNode node2 = MAPPER.readTree("{\"typeId\": \"String[]\"}");
        JsonParser parser = MAPPER.parserForObject(node2);
        handler.handlePropertyValue(parser, MAPPER.context(), "prop2", new Object());
        assertArrayEquals(new String[]{"value2"}, (Object[])properties[1].getProperty().getValue(new Object()));
        assertEquals("String[]", handler._typeIds[1]);
    }

    @Test
    public void testComplete() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", "type1");
        node.put("prop3", "value3");

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();

        Object result = handler.complete(MAPPER.createParser(node), MAPPER.context(), new Object());
        assertNotNull(result);

        // Test that all properties have been set correctly.
        assertEquals("value1", properties[0].getProperty().getValue(result));
        assertEquals("type1", properties[1].getProperty().getValue(result));
        assertNull(properties[2].getProperty().getValue(result));
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", "type1");
        node.put("prop3", "type2");

        // Create a simple creator class.
        class MyClass {
            private final String _type1;
            private final String _type2;

            public MyClass(String type1, String type2) {
                _type1 = type1;
                _type2 = type2;
            }

            public String getType1() {
                return _type1;
            }

            public String getType2() {
                return _type2;
            }
        }

        // Register the creator.
        MAPPER.registerAlternativeMapper(new MyClassMapper());

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();

        Object result = handler.complete(MAPPER.createParser(node), MAPPER.context(), new PropertyValueBuffer(), new MyClassCreator());
        assertNotNull(result);

        // Test that all properties have been set correctly.
        assertEquals("value1", properties[0].getProperty().getValue(result));
        assertEquals("type1", properties[1].getProperty().getValue(result));
        assertEquals("type2", properties[2].getProperty().getValue(result));
        assertEquals("type1", ((MyClass)result).getType1());
        assertEquals("type2", ((MyClass)result).getType2());
    }

    @Test
    public void testCompleteWithMissingProperties() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop3", "value3");

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();

        try {
            handler.complete(MAPPER.createParser(node), MAPPER.context(), new Object());
            fail("Expected a mapping exception.");
        } catch (MappingException e) {
            // Expected.
        }

        // Test that the missing property error message is correct.
        String[] errors = e.getErrors();
        assertEquals(1, errors.length);
        assertEquals("Missing property 'prop2' for external type id 'null'", errors[0].getOriginalMessage());
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", null);

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();

        try {
            handler.complete(MAPPER.createParser(node), MAPPER.context(), new Object());
            fail("Expected a mapping exception.");
        } catch (MappingException e) {
            // Expected.
        }

        // Test that the missing type id error message is correct.
        String[] errors = e.getErrors();
        assertEquals(1, errors.length);
        assertEquals("Missing external type id property 'typePropertyName1' for property 'prop1'", errors[0].getOriginalMessage());
    }

    @Test
    public void testCompleteWithNullValues() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", null);
        node.put("prop2", "value2");

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();

        Object result = handler.complete(MAPPER.createParser(node), MAPPER.context(), new Object());
        assertNotNull(result);

        // Test that the null value has been set correctly.
        assertNull(properties[0].getProperty().getValue(result));
    }

    @Test
    public void testCompleteWithInvalidTypeId() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", "invalidTypeId");

        ExternalTypeHandler handler = MAPPER.readValue(node.toString(), ExternalTypeHandler.class).start();

        try {
            handler.complete(MAPPER.createParser(node), MAPPER.context(), new Object());
            fail("Expected a mapping exception.");
        } catch (MappingException e) {
            // Expected.
        }

        // Test that the mapping exception message is correct.
        String[] errors = e.getErrors();
        assertEquals(1, errors.length);
        assertEquals("Unknown type id: invalidTypeId", errors[0].getOriginalMessage());
    }

    /**
     * Custom deserializer for {@link ExternalTypeHandler}.
     */
    static class ExternalTypeHandlerDeserializer extends StdDeserializer<ExternalTypeHandler> {
        public ExternalTypeHandlerDeserializer() {
            this(null);
        }

        public ExternalTypeHandlerDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ExternalTypeHandler deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectNode node = p.readValueAsTree();
            ExtTypedProperty[] properties = new ExtTypedProperty[node.size()];
            HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();

            for (int i = 0; i < node.size(); ++i) {
                ObjectNode propNode = (ObjectNode)node.values().next();
                SettableBeanProperty property = (SettableBeanProperty)ctxt.findBeanProperty(propNode.get("propertyName").asText(), null);
                TypeDeserializer typeDeserializer = ctxt.findValueDeserializer(propNode.get("typeDeserializer"), null);
                properties[i] = new ExtTypedProperty(property, typeDeserializer);
                nameToPropertyIndex.put(property.getName(), i);
                nameToPropertyIndex.put(typeDeserializer.getPropertyName(), i);
            }

            return new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        }
    }

    /**
     * Custom creator for {@link MyClass}.
     */
    static class MyClassCreator extends BaseCreator<MyClass> {
        @Override
        public MyClass createInstance(DeserializationContext ctxt) throws IOException {
            ObjectCodec oc = ctxt.getObjectCodec();
            JsonNode node = ctxt.getCurrentName();
            JsonParser p = ctxt.getParser();

            ObjectNode propsNode = node.get("properties");
            ExtTypedProperty[] properties = new ExtTypedProperty[propsNode.size()];
            HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();

            for (int i = 0; i < propsNode.size(); ++i) {
                ObjectNode propNode = (ObjectNode)propsNode.values().next();
                SettableBeanProperty property = (SettableBeanProperty)ctxt.findBeanProperty(propNode.get("propertyName").asText(), null);
                TypeDeserializer typeDeserializer = ctxt.findValueDeserializer(propNode.get("typeDeserializer"), null);
                properties[i] = new ExtTypedProperty(property, typeDeserializer);
                nameToPropertyIndex.put(property.getName(), i);
                nameToPropertyIndex.put(typeDeserializer.getPropertyName(), i);
            }

            MyClass result = new MyClass(ctxt.constructValue("typeId1", String.class), ctxt.constructValue("typeId2", String[].class));
            PropertyValueBuffer buffer = new PropertyValueBuffer();

            for (ExtTypedProperty property : properties) {
                property.getProperty().deserializeAndSet(p, ctxt, result);
                buffer.assignParameter(property.getProperty(), property.getProperty().getValue(result));
                if (property.getTypeProperty() != null) {
                    buffer.assignParameter(property.getTypeProperty(), property.getTypeProperty().getValue(result));
                }
            }

            return result;
        }
    }

    /**
     * Custom mapper for {@link MyClass}.
     */
    static class MyClassMapper extends SimpleSerializerMapper {
        @Override
        public List<Mapper.SerializerDescrip> findSerializerDescriptionsFor(Class<?> valueClass, AnnotationIntrospector annotater) {
            if (valueClass == MyClass.class) {
                return Arrays.asList(new Mapper.SerializerDescrip(valueClass, new MyClassSerializer()));
            }
            return super.findSerializerDescriptionsFor(valueClass, annotater);
        }
    }

    /**
     * Custom serializer for {@link MyClass}.
     */
    static class MyClassSerializer extends StdSerializer<MyClass> {
        public MyClassSerializer() {
            this(null);
        }

        public MyClassSerializer(Class<MyClass> t) {
            super(t);
        }

        @Override
        public void serializeValues(Object p, JsonGenerator g, SerializerProvider sps) throws IOException {
            MyClass obj = (MyClass)p;
            g.writeStartObject();
            g.writeFieldName("typeId1");
            g.writeString(obj.getType1());
            g.writeFieldName("typeId2");
            g.writeValueAsArray(obj.getType2());
            g.writeEndObject();
        }
    }
}