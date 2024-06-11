package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"typeProperty\": \"StringType\", \"externalProperty\": \"typeId1\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1"}, new TokenBuffer[1]);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        StdDeserializer typeDeserializer = new StdDeserializer<>();
        typeDeserializer.setEmptyValue(null);
        typeDeserializer.setPropertyName("typeProperty");

        handler.handleTypePropertyValue(jp, ctxt, "typeProperty", null);

        Assert.assertEquals("StringType", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValueWithSimpleType() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"property1\": 123, \"externalProperty\": \"typeId1\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1"}, new TokenBuffer[1]);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        handler.handlePropertyValue(jp, ctxt, "property1", null);

        Assert.assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValueWithCustomDeserializer() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"property1\": { \"typeId\": \"CustomType\", \"value\": 123 }, \"externalProperty\": \"typeId1\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1"}, new TokenBuffer[1]);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        StdDeserializer typeDeserializer = new StdDeserializer<>();
        typeDeserializer.setEmptyValue(null);
        typeDeserializer.setPropertyName("typeId");
        typeDeserializer.findValueDeserializer(Object.class).setContentClass(CustomType.class);

        handler.handlePropertyValue(jp, ctxt, "property1", null);

        Assert.assertEquals(CustomType.class, handler._typeIds[0].getClass());
    }

    @Test
    public void testHandlePropertyValueWithMissingProperty() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"externalProperty\": \"typeId1\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1"}, new TokenBuffer[1]);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        try {
            handler.handlePropertyValue(jp, ctxt, "missingProperty", null);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testHandlePropertyValueWithMissingExternalProperty() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"property1\": 123}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, new TokenBuffer[1]);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        try {
            handler.handlePropertyValue(jp, ctxt, "property1", null);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"externalProperty1\": \"typeId1\", \"externalProperty2\": \"typeId2\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1", "typeId2"}, new TokenBuffer[2]);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        Object result = handler.complete(jp, ctxt, null);

        Assert.assertNotNull(result);
    }

    @Test
    public void testCompleteWithNullBean() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"externalProperty1\": \"typeId1\", \"externalProperty2\": \"typeId2\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1", "typeId2"}, new TokenBuffer[2]);

        Object result = handler.complete(null, mapper.getDeserializationContext(), json);

        Assert.assertNotNull(result);
    }

    @Test
    public void testCompleteWithEmptyJson() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, new TokenBuffer[1]);

        Object result = handler.complete(null, mapper.getDeserializationContext(), json);

        Assert.assertNotNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteWithMissingExternalProperties() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"property1\": 123}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, new TokenBuffer[1]);

        Object result = handler.complete(null, mapper.getDeserializationContext(), json);

        Assert.fail();
    }

    @Test
    public void testCompleteWithNullExternalId() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"externalProperty1\": null, \"externalProperty2\": \"typeId2\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId2"}, new TokenBuffer[1]);

        Object result = handler.complete(null, mapper.getDeserializationContext(), json);

        Assert.assertNotNull(result);
    }

    @Test
    public void testCompleteWithNullPropertyName() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"externalProperty1\": \"typeId1\", \"\": \"typeId2\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1", "typeId2"}, new TokenBuffer[2]);

        try {
            Object result = handler.complete(null, mapper.getDeserializationContext(), json);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCompleteWithUnassignedProperties() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"externalProperty1\": \"typeId1\", \"externalProperty2\": null}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1", null}, new TokenBuffer[2]);

        Object result = handler.complete(null, mapper.getDeserializationContext(), json);

        Assert.assertNotNull(result);
    }

    @Test
    public void testCompleteWithEmptyProperties() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{\"externalProperty1\": \"typeId1\", \"externalProperty2\": {}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, new String[]{"typeId1", null}, new TokenBuffer[2]);

        Object result = handler.complete(null, mapper.getDeserializationContext(), json);

        Assert.assertNotNull(result);
    }

    @Test
    public void testCompleteWithEmptyJsonAndEmptyProperties() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String json = "{}";
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, new TokenBuffer[1]);

        Object result = handler.complete(null, mapper.getDeserializationContext(), json);

        Assert.assertNotNull(result);
    }

    static class CustomTypeDeserializer extends StdDeserializer<CustomType> {
        public CustomTypeDeserializer() {
            this(null);
        }

        public CustomTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public CustomType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new CustomType();
        }
    }

    static class CustomType {
    }

    static class CustomBean {
        @JsonDeserialize(using = CustomTypeDeserializer.class)
        private CustomType property1;

        public CustomType getProperty1() {
            return property1;
        }

        public void setProperty1(CustomType property1) {
            this.property1 = property1;
        }
    }

    static class CustomBuilder {
        private final List<ExtTypedProperty> _properties = new ArrayList<ExtTypedProperty>();

        public void addProperty(PropertyName propertyName, TypeDeserializer typeDeserializer) {
            _properties.add(new ExtTypedProperty(propertyName, typeDeserializer));
        }

        public ExternalTypeHandler build() {
            return new ExternalTypeHandler(_properties.toArray(new ExtTypedProperty[_properties.size()]), null, null, null);
        }
    }
}