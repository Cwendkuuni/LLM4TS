import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefaultTypeDeserializer() throws Exception {
        SimpleModule module = new SimpleModule();
        TypeDeserializerBase testee = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                "",
                true,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public String toString() {
                return "TestTypeDeserializer";
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        JsonParser parser = mock(JsonParser.class);

        when(context.findContextualValueDeserializer(eq(String.class), any())).thenReturn(null);
        when(parser.getTypeId()).thenReturn(null);

        Object result = testee._deserializeWithNativeTypeId(parser, context);
        assertNull(result);
    }

    @Test
    public void testTypeDeserializerWithTypeId() throws Exception {
        SimpleModule module = new SimpleModule();
        TypeDeserializerBase testee = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                "",
                true,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.WRAP_WITH_ARRAY;
            }

            @Override
            public String toString() {
                return "TestTypeDeserializer";
            }

            @Override
            public Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        JsonParser parser = mock(JsonParser.class);

        when(parser.getTypeId()).thenReturn("TestTypeDeserializer");
        when(context.findContextualValueDeserializer(eq("TestTypeDeserializer"), any())).thenReturn(testee);

        Object result = testee._deserializeWithNativeTypeId(parser, context);
        assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testTypeIdNotFound() throws Exception {
        SimpleModule module = new SimpleModule();
        TypeDeserializerBase testee = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                "",
                true,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public String toString() {
                return "TestTypeDeserializer";
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        JsonParser parser = mock(JsonParser.class);

        when(context.findContextualValueDeserializer(eq(String.class), any())).thenReturn(null);
        when(parser.getTypeId()).thenReturn("TestTypeDeserializer");

        Object result = testee._deserializeWithNativeTypeId(parser, context);
        assertNull(result);
    }

    @Test
    public void testTypeIdNotResolvable() throws Exception {
        SimpleModule module = new SimpleModule();
        TypeDeserializerBase testee = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                "",
                true,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public String toString() {
                return "TestTypeDeserializer";
            }

            @Override
            public Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException {
                throw new IOException("Test error");
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        JsonParser parser = mock(JsonParser.class);

        when(parser.getTypeId()).thenReturn("TestTypeDeserializer");
        when(context.handleUnknownTypeId(eq(String.class), any(), any(), anyString())).thenThrow(new IOException("Test error"));

        Object result = testee._deserializeWithNativeTypeId(parser, context);
        assertNull(result);
    }
}