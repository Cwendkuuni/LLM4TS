import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    private DeserializationContext context;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        context = mock(DeserializationContext.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
        assertNull(typeDeserializerBase.getPropertyName());
        assertNull(typeDeserializerBase.getTypeIdResolver());
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testConstructorWithArguments() throws Exception {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        assertEquals(JsonTypeInfo.As.PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "testTypeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(context.findContextualValueDeserializer(eq(typeId), any())).thenReturn(deserializer);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return super._findDeserializer(ctxt, typeId);
            }
        };

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);

        assertEquals(deserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerMissing() throws Exception {
        when(context.findContextualValueDeserializer(eq("testTypeId"), any())).thenReturn(null);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return super._findDeserializer(ctxt, typeId);
            }
        };

        typeDeserializerBase._findDeserializer(context, "testTypeId");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        String typeId = "testTypeId";

        when(jsonParser.getTypeId()).thenReturn(typeId);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return super._deserializeWithNativeTypeId(jp, ctxt, typeId);
            }
        };

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);

        // Verify that _findDeserializer is called
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(typeId), any())).thenReturn(deserializer);

        typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return deserializer;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return super._deserializeWithNativeTypeId(jp, ctxt, typeId);
            }
        };

        result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId);

        assertEquals(deserializer.deserialize(jsonParser, context), result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdMissing() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        String typeId = "testTypeId";

        when(jsonParser.getTypeId()).thenReturn(typeId);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return super._deserializeWithNativeTypeId(jp, ctxt, typeId);
            }
        };

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId);
    }
}