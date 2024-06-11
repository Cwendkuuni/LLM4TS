import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExtTypedProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new StringTypeDeserializer())
                .build());

        JsonParser jp = mapper.getFactory().createParser("{\"typeId\": \"StringType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new SimplePojo();

        handler.handleTypePropertyValue(jp, ctxt, "typeId", bean);
        assertEquals("StringType", bean.getTypeId());
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new NumberTypeDeserializer())
                .build());

        JsonParser jp = mapper.getFactory().createParser("{\"value\": 123}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new SimplePojo();

        handler.handlePropertyValue(jp, ctxt, "value", bean);
        assertEquals(123, bean.getValue());
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new StringTypeDeserializer())
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new NumberTypeDeserializer())
                .build());

        JsonParser jp = mapper.getFactory().createParser("{\"typeId\": \"StringType\", \"value\": 123}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new SimplePojo();

        handler.complete(jp, ctxt, bean);
        assertEquals("StringType", bean.getTypeId());
        assertEquals(123, bean.getValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new StringTypeDeserializer())
                .build());

        JsonParser jp = mapper.getFactory().createParser("{\"value\": 123}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new SimplePojo();

        handler.complete(jp, ctxt, bean);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new NumberTypeDeserializer())
                .build());

        JsonParser jp = mapper.getFactory().createParser("{\"typeId\": null, \"value\": 123}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new SimplePojo();

        handler.complete(jp, ctxt, bean);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojoWithCreator().getClass().getDeclaredField("typeId"), new StringTypeDeserializer())
                .addExternal(new SimplePojoWithCreator().getClass().getDeclaredField("value"), new NumberTypeDeserializer())
                .build());

        JsonParser jp = mapper.getFactory().createParser("{\"typeId\": \"StringType\", \"value\": 123}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        SimplePojoWithCreator creator = new SimplePojoWithCreator();

        handler.complete(jp, ctxt, buffer, creator);
        fail("Expected JsonMappingException");
    }

    private static class SimplePojo {
        private String typeId;
        private int value;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private static class SimplePojoWithCreator {
        private String typeId;
        private int value;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    static class StringTypeDeserializer extends StdDeserializer<String> {
        public final static String DEFAULT_TYPE_ID = "StringType";

        public StringTypeDeserializer() {
            this(null);
        }

        public StringTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText();
        }

        @Override
        public String getPropertyName() {
            return "typeId";
        }
    }

    static class NumberTypeDeserializer extends StdDeserializer<Number> {
        public final static String DEFAULT_TYPE_ID = "NumberType";

        public NumberTypeDeserializer() {
            this(null);
        }

        public NumberTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Number deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsNumber();
        }

        @Override
        public String getPropertyName() {
            return "value";
        }
    }
}