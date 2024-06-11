package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.test.BaseTest;

public class TypeDeserializerBaseTest extends BaseTest {

    private static final String TYPE_PROPERTY_NAME = "typePropertyName";

    private static final String TYPE_ID = "typeId";

    private static final String NATIVE_TYPE_ID = "nativeTypeId";

    private static final String UNKNOWN_TYPE_ID = "unknownTypeId";

    private static final String MISSING_TYPE_ID = "missingTypeId";

    private static final String DEFAULT_IMPL_TYPE_NAME = "DefaultImpl";

    private static final JavaType BASE_TYPE = mapper.getTypeFactory().constructClassType(String.class);

    private static final JavaType DEFAULT_IMPL = mapper.getTypeFactory().constructClassType(DefaultImpl.class);

    private static final TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);

    private static final BeanProperty property = mock(BeanProperty.class);

    private static final DeserializationContext context = mock(DeserializationContext.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private TypeDeserializerBase<String> typeDeserializer;

    @Before
    public void setUp() throws Exception {
        when(typeIdResolver.idFromValue(eq(String.class), any(JsonParser.class))).thenReturn(TYPE_ID);
        when(typeIdResolver.idFromValue(eq(DefaultImpl.class), any(JsonParser.class))).thenReturn(DEFAULT_IMPL_TYPE_NAME);
        typeDeserializer = new TypeDeserializerBase<String>(BASE_TYPE, typeIdResolver, TYPE_PROPERTY_NAME, true, DEFAULT_IMPL) {
            @Override
            protected TypeDeserializer<String> forProperty(BeanProperty prop) {
                return this;
            }
        };
        when(context.findContextualValueDeserializer(eq(BASE_TYPE), any(BeanProperty.class))).thenReturn(mapper.getFactory().valueDeserializer(String.class));
        when(context.findContextualValueDeserializer(eq(DEFAULT_IMPL), any(BeanProperty.class))).thenReturn(mapper.getFactory().valueDeserializer(DefaultImpl.class));
        when(context.findValueDeserializer(eq(String.class), any(DeserializationContext.class))).thenReturn(mapper.getFactory().valueDeserializer(String.class));
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer<String> typeDeserializerForProperty = typeDeserializer.forProperty(property);
        assertSame(typeDeserializer, typeDeserializerForProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializer.getTypeInclusion();
        assertSame(JsonTypeInfo.As.WRAP_ONLY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        String baseTypeName = typeDeserializer.baseTypeName();
        assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializer.getPropertyName();
        assertEquals(TYPE_PROPERTY_NAME, propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializer.getTypeIdResolver();
        assertSame(mock(TypeIdResolver.class), typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        Class<?> defaultImpl = typeDeserializer.getDefaultImpl();
        assertEquals(DefaultImpl.class, defaultImpl);
    }

    @Test
    public void testFindDeserializerWithTypeId() throws IOException {
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, TYPE_ID);
        assertSame(mapper.getFactory().valueDeserializer(String.class), deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializer._findDefaultImplDeserializer(context);
        assertSame(mapper.getFactory().valueDeserializer(DefaultImpl.class), defaultImplDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser(mapper.writeValueAsString(new NativeTypeIdWrapper(NATIVE_TYPE_ID)));
        Object deserialized = typeDeserializer._deserializeWithNativeTypeId(jsonParser, context);
        assertSame(DEFAULT_IMPL, deserialized);
    }

    @Test
    public void testDeserializeWithUnknownTypeId() throws IOException, JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser(mapper.writeValueAsString(new UnknownTypeIdWrapper(UNKNOWN_TYPE_ID)));
        Object deserialized = typeDeserializer._deserializeWithNativeTypeId(jsonParser, context);
        assertNull(deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithMissingTypeId() throws IOException, JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser(mapper.writeValueAsString(new MissingTypeIdWrapper()));
        typeDeserializer._deserializeWithNativeTypeId(jsonParser, context);
    }
}

class DefaultImpl {

}

class NativeTypeIdWrapper {

    private final String nativeTypeId;

    public NativeTypeIdWrapper(String nativeTypeId) {
        this.nativeTypeId = nativeTypeId;
    }

    public String getNativeTypeId() {
        return nativeTypeId;
    }
}

class UnknownTypeIdWrapper {

}

class MissingTypeIdWrapper {

}