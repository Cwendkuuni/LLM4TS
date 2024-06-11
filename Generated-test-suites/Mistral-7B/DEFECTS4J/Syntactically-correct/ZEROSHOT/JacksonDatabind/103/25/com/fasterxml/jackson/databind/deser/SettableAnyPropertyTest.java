package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;
    private JsonDeserializer<Object> valueDeserializerMock;
    private TypeDeserializer typeDeserializerMock;
    private KeyDeserializer keyDeserializerMock;

    @Before
    public void setUp() throws Exception {
        valueDeserializerMock = Mockito.mock(JsonDeserializer.class);
        typeDeserializerMock = Mockito.mock(TypeDeserializer.class);
        keyDeserializerMock = Mockito.mock(KeyDeserializer.class);

        AnnotatedMember setterMock = Mockito.mock(AnnotatedMember.class);
        Mockito.when(setterMock.getDeclaringClass()).thenReturn(Object.class);
        Mockito.when(setterMock.getName()).thenReturn("setAnyProperty");

        BeanProperty propertyMock = Mockito.mock(BeanProperty.class);
        Mockito.when(propertyMock.getName()).thenReturn("anyProperty");
        Mockito.when(propertyMock.getMember()).thenReturn(setterMock);

        settableAnyProperty = new SettableAnyProperty(propertyMock, setterMock, JsonTypeFactory.instance.constructType(Object.class), keyDeserializerMock, valueDeserializerMock, typeDeserializerMock);
    }

    @Test
    public void testSettableAnyProperty() {
        assertNotNull(settableAnyProperty);
    }

    @Test
    public void testGetProperty() throws Exception {
        AnnotatedPropertyGetter propertyGetter = Mockito.mock(AnnotatedPropertyGetter.class);
        Mockito.when(propertyMock.getGetter()).thenReturn(propertyGetter);

        BeanProperty property = settableAnyProperty.getProperty();
        assertEquals(propertyMock, property);
    }

    @Test
    public void testHasValueDeserializer() throws Exception {
        assertFalse(settableAnyProperty.hasValueDeserializer());

        JsonDeserializer<Object> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        settableAnyProperty = settableAnyProperty.withValueDeserializer(jsonDeserializer);
        assertTrue(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetType() throws Exception {
        JavaType javaType = Mockito.mock(JavaType.class);
        when(typeDeserializerMock.deserializeTypedFromAny(Mockito.eq(valueDeserializerMock), eq(JsonParser.class), eq(DeserializationContext.class), anyObject()))
                .thenReturn(javaType);

        when(propertyMock.getType()).thenReturn(jsonTypeOf(Map.class));

        JavaType type = settableAnyProperty.getType();
        assertEquals(Map.class, type.getRawClass());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object result = new Object();

        String propName = "propName";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Mockito.when(setterMock.getName()).thenReturn("setAnyProperty");

        when(keyDeserializerMock.deserializeKey(eq(propName), eq(context))).thenReturn(propName);

        when(valueDeserializerMock.deserialize(jsonParser, context)).thenReturn(result);

        Object instance = new Object();

        settableAnyProperty.deserializeAndSet(jsonParser, context, instance, propName);

        assertEquals(result, instance);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithNullValueDeserializer() throws Exception {
        Object result = new Object();
        String propName = "propName";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(valueDeserializerMock.deserialize(jsonParser, context)).thenThrow(new IOException());

        settableAnyProperty.deserializeAndSet(jsonParser, context, null, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws Exception {
        Object result = new Object();
        String propName = "propName";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(valueDeserializerMock.getObjectIdReader()).thenReturn(Mockito.mock(ObjectIdReader.class));

        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);

        when(valueDeserializerMock.deserializeWithType(jsonParser, context, typeDeserializerMock)).thenReturn(result);

        when(valueDeserializerMock.getObjectIdReader()).thenReturn(Mockito.mock(ObjectIdReader.class));

        Object id = Mockito.mock(Object.class);
        when(objectIdReaderMock.deserializeKey(anyString(), eq(context))).thenReturn(propName);

        when(objectIdReaderMock.deserializeId(jsonParser, context)).thenReturn(id);

        when(valueDeserializerMock.getObjectIdReader().resolveForwardReference(id)).thenReturn(reference);

        settableAnyProperty.deserializeAndSet(jsonParser, context, null, propName);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        when(setterMock.getName()).thenReturn("setAnyProperty");

        Map<Object, Object> map = new HashMap<>();

        when(setterMock.getValue(instance)).thenReturn(map);

        settableAnyProperty.set(instance, propName, value);

        assertEquals(value, map.get(propName));
    }

    @Test
    public void testThrowAsIOE() throws Exception {
        Exception exception = new IOException("Exception message");
        String propName = "propName";
        Object value = new Object();

        settableAnyProperty._throwAsIOE(exception, propName, value);

        fail("Expected JsonMappingException");
    }

    @Test
    public void testGetClassName() throws Exception {
        String className = "ClassName";

        when(setterMock.getDeclaringClass()).thenReturn(Class.forName(className));

        assertEquals(className, settableAnyProperty.getClassName());
    }
}

class ObjectIdReaderMock extends ObjectIdReaderBase {
    @Override
    public ObjectIdReader deserializeId(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }
}