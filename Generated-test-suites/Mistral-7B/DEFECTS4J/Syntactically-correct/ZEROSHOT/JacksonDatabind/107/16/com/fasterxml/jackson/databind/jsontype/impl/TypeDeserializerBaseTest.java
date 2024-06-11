package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        typeIdResolver = mock(TypeIdResolver.class);
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "typePropertyName", true, null);
    }

    @Test
    public void testTypeDeserializerBaseConstruction() {
        assertNotNull(typeDeserializerBase);
        assertEquals(baseType.getRawClass().getName(), typeDeserializerBase.baseTypeName());
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testForPropertyMethod() throws IOException {
        BeanDescription beanDescription = mock(BeanDescription.class);
        BeanProperty property = mock(BeanProperty.class);
        when(beanDescription.getBeanClass()).thenReturn(Object.class);
        when(beanDescription.getBeanProperty(typeDeserializerBase.getPropertyName())).thenReturn(property);
        TypeDeserializerBase typeDeserializerBaseCopy = new TypeDeserializerBase(typeDeserializerBase, property);
        assertEquals(typeDeserializerBaseCopy.forProperty(property), typeDeserializerBase);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testForPropertyMethodWhenPropertyIsNull() {
        typeDeserializerBase.forProperty(null);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "testTypeId";
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(typeIdResolver.getTypeIdResolverFor(objectMapper.getFactory(), String.class)).thenReturn(typeIdResolver);
        when(typeIdResolver.typeFromId(mock(DeserializationContext.class), typeId)).thenReturn(new JavaType(Integer.class));
        when(typeIdResolver.findDeserializer(typeId, objectMapper.getFactory()))
                .thenReturn(jsonDeserializer);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getText()).thenReturn("testJsonString");
        when(jsonParser.getTypeId()).thenReturn(typeId);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class));
        assertNotNull(result);
        assertEquals(Integer.class, result.getClass());
        verify(jsonDeserializer).deserialize(jsonParser, mock(DeserializationContext.class));
    }

    @Test
    public void testFindDeserializerWhenTypeIdIsNull() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWhenTypeIdResolverReturnsNull() throws IOException {
        String typeId = "testTypeId";
        when(typeIdResolver.typeFromId(mock(DeserializationContext.class), typeId)).thenReturn(null);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getTypeId()).thenReturn(typeId);

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class));
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWhenJsonDeserializerIsNull() throws IOException {
        String typeId = "testTypeId";
        when(typeIdResolver.typeFromId(mock(DeserializationContext.class), typeId)).thenReturn(new JavaType(Integer.class));
        when(typeIdResolver.findDeserializer(typeId, objectMapper.getFactory())).thenReturn(null);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getText()).thenReturn("testJsonString");
        when(jsonParser.getTypeId()).thenReturn(typeId);

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class));
    }

    @Test
    public void testDefaultImplDeserializer() throws IOException {
        when(typeIdResolver.getTypeIdResolverFor(objectMapper.getFactory(), String.class)).thenReturn(null);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getText()).thenReturn("testJsonString");

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "testTypeId";
        when(typeIdResolver.typeFromId(mock(DeserializationContext.class), typeId)).thenReturn(null);

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.handleUnknownTypeId(baseType, typeId, typeIdResolver, "extraDesc")).thenThrow(IOException.class);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getTypeId()).thenReturn(typeId);

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType javaType = new TypeReference<Map<String, Object>>(){}.getType();

        when(context.handleMissingTypeId(baseType, typeIdResolver, "extraDesc")).thenReturn(javaType);

        assertNotNull(typeDeserializerBase._handleMissingTypeId(context, "extraDesc"));
        assertEquals(javaType, typeDeserializerBase._handleMissingTypeId(context, "extraDesc").getType());
    }
}