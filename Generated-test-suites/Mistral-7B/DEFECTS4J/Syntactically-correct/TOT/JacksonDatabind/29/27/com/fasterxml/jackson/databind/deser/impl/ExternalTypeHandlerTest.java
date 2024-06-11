import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private ObjectNode testNode;
    private ExternalTypeHandler handler;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addSerializer(Object.class, new StdSerializer<Object>(Object.class) {})
                .addSerializer(String.class, new ToStringSerializer(String.class))
                .addDeserializer(String.class, new StdDeserializer<String>(String.class)));

        testNode = mapper.createObjectNode();
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "prop2"), mock(TypeDeserializer.class))
                .build());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String[] typeIds = { "Type1", "Type2" };
        TokenBuffer[] tokens = new TokenBuffer[2];
        handler = new ExternalTypeHandler(new ExternalTypeHandler(handler)
                .setProperties(new ExternalTypeHandler.ExtTypedProperty[2])
                .setNameToPropertyIndex(new HashMap<String, Integer>())
                .setTypeIds(typeIds)
                .setTokens(tokens)
                .build());

        String propName = "prop1";
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);

        when(jp.getText()).thenReturn("Type1");
        when(ctxt.findBeanProperty(_properties(0), "prop1")).thenReturn(_property(0));
        doReturn(true).when(handler).handleTypePropertyValue(jp, ctxt, propName, bean);

        assertEquals(typeIds[0], handler.getTypeIds()[0]);
        assertEquals(tokens[0], handler._tokens[0]);

        handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        verify(jp).nextToken();
        verify(bean).setPropertyValue(eq(_property(0)), eq("Type1"));

        verifyNoMoreInteractions(jp);
        verifyNoMoreInteractions(ctxt);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String[] typeIds = { "Type1", "Type2" };
        TokenBuffer[] tokens = new TokenBuffer[2];
        handler = new ExternalTypeHandler(new ExternalTypeHandler(handler)
                .setProperties(new ExternalTypeHandler.ExtTypedProperty[2])
                .setNameToPropertyIndex(new HashMap<String, Integer>())
                .setTypeIds(typeIds)
                .setTokens(tokens)
                .build());

        String propName = "prop1";
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);

        when(jp.getText()).thenReturn("value1");
        when(ctxt.findBeanProperty(_properties(0), "prop1")).thenReturn(_property(0));
        when(handler.handlePropertyValue(jp, ctxt, propName, bean)).thenReturn(true);

        Object value = handler.handlePropertyValue(jp, ctxt, propName, bean);

        assertEquals("value1", value);
        assertEquals(typeIds[0], handler.getTypeIds()[0]);
        assertEquals(tokens[0], handler._tokens[0]);

        verify(jp).nextToken();
        verify(bean).setPropertyValue(eq(_property(0)), eq("value1"));

        verifyNoMoreInteractions(jp);
        verifyNoMoreInteractions(ctxt);
    }

    @Test
    public void testComplete() throws IOException {
        String[] typeIds = { "Type1", "Type2" };
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "prop2"), mock(TypeDeserializer.class))
                .build());

        Object bean = mapper.readValue(testNode.toString(), Object.class);

        handler.complete(mock(JsonParser.class), mock(DeserializationContext.class), bean);

        verifyNoMoreInteractions(mock(JsonParser.class));
        verifyNoMoreInteractions(mock(DeserializationContext.class));
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws IOException {
        String[] typeIds = { "Type1" };
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "prop2"), mock(TypeDeserializer.class))
                .build());

        Object bean = mapper.readValue(testNode.toString(), Object.class);

        handler.complete(mock(JsonParser.class), mock(DeserializationContext.class), bean);

        verifyNoMoreInteractions(mock(JsonParser.class));
        verifyNoMoreInteractions(mock(DeserializationContext.class));

        handler = new ExternalTypeHandler(new ExternalTypeHandler(handler)
                .setProperties(new ExternalTypeHandler.ExtTypedProperty[2])
                .setNameToPropertyIndex(new HashMap<String, Integer>())
                .setTypeIds(typeIds)
                .build());

        String propName = "prop2";
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean2 = mock(Object.class);

        when(jp.getText()).thenReturn("Type2");
        when(ctxt.findBeanProperty(_properties(1), "prop2")).thenReturn(_property(1));
        doThrow(new IOException("Missing external type id property 'prop2'")).when(handler).handleTypePropertyValue(jp, ctxt, propName, bean2);

        handler.complete(jp, ctxt, bean2);
    }

    @Test(expected = IOException.class)
    public void testMissingProperty() throws IOException {
        String[] typeIds = { "Type1", "Type2" };
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .build());

        Object bean = mapper.readValue(testNode.toString(), Object.class);

        handler.complete(mock(JsonParser.class), mock(DeserializationContext.class), bean);

        verifyNoMoreInteractions(mock(JsonParser.class));
        verifyNoMoreInteractions(mock(DeserializationContext.class));

        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "missingProp"), mock(TypeDeserializer.class))
                .build());

        String propName = "missingProp";
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean2 = mock(Object.class);

        when(jp.getText()).thenReturn("Type2");
        when(ctxt.findBeanProperty(_properties(1), propName)).thenReturn(null);

        handler.complete(jp, ctxt, bean2);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        String[] typeIds = { "Type1", "Type2" };
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "prop2"), mock(TypeDeserializer.class))
                .build());

        JsonNode node = mapper.readTree(testNode.toString());
        ObjectNode nodeWithCreator = mapper.treeToObject(node.set("creatorProperty", mapper.createObjectNode()));
        Object bean = mapper.readValue(nodeWithCreator.toString(), Object.class);

        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "prop2"), mock(TypeDeserializer.class))
                .build());

        Object bean2 = handler.complete(mock(JsonParser.class), mock(DeserializationContext.class),
                mapper.createPropertyValueBuffer(), mock(PropertyBasedCreator.class)).getValue();

        assertEquals(bean, bean2);
    }

    @Test
    public void testDeserialize() throws IOException {
        String[] typeIds = { "Type1", "Type2" };
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "prop2"), mock(TypeDeserializer.class))
                .build());

        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        int index = 0;
        String typeId = "Type1";

        when(jp.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.getValueAsString()).thenReturn(typeId);
        when(ctxt.findBeanProperty(_properties(0), "prop1")).thenReturn(_property(0));

        Object value = handler._deserialize(jp, ctxt, index, typeId);

        assertEquals(typeId, handler.getTypeIds()[index]);
        assertEquals(value, handler.getProperty().deserialize(jp, ctxt));

        verify(jp).nextToken();
        verify(jp).nextToken();
        verifyNoMoreInteractions(ctxt);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String[] typeIds = { "Type1", "Type2" };
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(mock(SettableBeanProperty.class, "prop1"), mock(TypeDeserializer.class))
                .addExternal(mock(SettableBeanProperty.class, "prop2"), mock(TypeDeserializer.class))
                .build());

        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        int index = 0;
        String typeId = "Type1";

        when(jp.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.getValueAsString()).thenReturn(typeId);
        when(ctxt.findBeanProperty(_properties(0), "prop1")).thenReturn(_property(0));

        handler._deserializeAndSet(jp, ctxt, bean, index, typeId);

        verify(bean).setPropertyValue(eq(_property(0)), eq(typeId));
        verify(jp).nextToken();
        verify(jp).nextToken();
        verifyNoMoreInteractions(ctxt);
    }

    private SettableBeanProperty _property(int index) {
        return mock(SettableBeanProperty.class);
    }

    private ExternalTypeHandler.ExtTypedProperty[] _properties(int length) {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[length];
        for (int i = 0; i < length; i++) {
            properties[i] = mock(ExternalTypeHandler.ExtTypedProperty.class);
        }
        return properties;
    }
}