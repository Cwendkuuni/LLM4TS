package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@JacksonStdImpl
public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonDeserializer<Object> valueDeserializerMock;
    private TypeDeserializer valueTypeDeserializerMock;
    private ValueInstantiator valueInstantiatorMock;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        valueDeserializerMock = Mockito.mock(JsonDeserializer.class);
        valueTypeDeserializerMock = Mockito.mock(TypeDeserializer.class);
        valueInstantiatorMock = Mockito.mock(ValueInstantiator.class);
    }

    @Test
    public void testEmptyArray() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializerMock, valueTypeDeserializerMock, valueInstantiatorMock);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ArgumentCaptor<Collection<Object>> resultCaptor = ArgumentCaptor.forClass(Collection.class);

        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        deserializer.deserialize(jsonParser, context);

        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(Collection.class));
        Mockito.verify(context).findContextualValueDeserializer(Mockito.eq(collectionType.getContentType()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(valueDeserializerMock).deserializeWithType(jsonParser, context, Mockito.eq(valueTypeDeserializerMock));

        Mockito.verify(context).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType.getRawClass()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType));

        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();

        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyNoMoreInteractions(context);

        Mockito.verify(context).handleCollectionEmpty(resultCaptor.capture());
        assertEquals(Collections.emptyList(), resultCaptor.getValue());
    }

    @Test
    public void testSingleObject() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializerMock, valueTypeDeserializerMock, valueInstantiatorMock);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ArgumentCaptor<Collection<Object>> resultCaptor = ArgumentCaptor.forClass(Collection.class);

        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.START_ARRAY).thenReturn(JsonToken.END_ARRAY);
        String valueString = "Test Value";
        Mockito.when(jsonParser.getText()).thenReturn(valueString);

        deserializer.deserialize(jsonParser, context);

        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(context).findContextualValueDeserializer(Mockito.eq(collectionType.getContentType()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(valueDeserializerMock).deserializeWithType(jsonParser, context, Mockito.eq(valueTypeDeserializerMock));

        Mockito.verify(context).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType.getRawClass()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType));

        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).getText().thenReturn(valueString);
        Mockito.verify(jsonParser).nextToken();

        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyNoMoreInteractions(context);

        Mockito.verify(context).handleCollectionEmpty(resultCaptor.capture());
        List<Object> result = resultCaptor.getValue();
        assertEquals(1, result.size());
        assertEquals(valueString, result.get(0));
    }

    @Test
    public void testMultipleObjects() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializerMock, valueTypeDeserializerMock, valueInstantiatorMock);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ArgumentCaptor<Collection<Object>> resultCaptor = ArgumentCaptor.forClass(Collection.class);

        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.VALUE_NUMBER).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.START_ARRAY).thenReturn(JsonToken.END_ARRAY);

        deserializer.deserialize(jsonParser, context);

        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(context).findContextualValueDeserializer(Mockito.eq(collectionType.getContentType()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(valueDeserializerMock).deserializeWithType(jsonParser, context, Mockito.eq(valueTypeDeserializerMock));

        Mockito.verify(context).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType.getRawClass()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType));

        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();

        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyNoMoreInteractions(context);

        Mockito.verify(context).handleCollectionEmpty(resultCaptor.capture());
        List<Object> result = resultCaptor.getValue();
        assertEquals(5, result.size());
        assertNull(result.get(0));
        assertEquals(1.0d, result.get(1));
        assertEquals("Test Value 3", result.get(2));
        assertNull(result.get(3));
        assertEquals("Test Value 5", result.get(4));
    }

    @Test
    public void testEmptyStringAsSingleObject() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializerMock, valueTypeDeserializerMock, valueInstantiatorMock);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ArgumentCaptor<Collection<Object>> resultCaptor = ArgumentCaptor.forClass(Collection.class);

        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.START_ARRAY).thenReturn(JsonToken.END_ARRAY);
        String valueString = "";

        deserializer.deserialize(jsonParser, context);

        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(context).findContextualValueDeserializer(Mockito.eq(collectionType.getContentType()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(valueDeserializerMock).deserializeWithType(jsonParser, context, Mockito.eq(valueTypeDeserializerMock));

        Mockito.verify(context).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType.getRawClass()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType));

        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(jsonParser).getText().thenReturn(valueString);
        Mockito.verify(jsonParser).nextToken();

        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyNoMoreInteractions(context);

        Mockito.verify(context).handleCollectionEmpty(resultCaptor.capture());
        List<Object> result = resultCaptor.getValue();
        assertEquals(1, result.size());
        assertEquals("", result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArray() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializerMock, valueTypeDeserializerMock, valueInstantiatorMock);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ArgumentCaptor<Collection<Object>> resultCaptor = ArgumentCaptor.forClass(Collection.class);

        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenThrow(RuntimeException.class);

        deserializer.deserialize(jsonParser, context);

        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(context).findContextualValueDeserializer(Mockito.eq(collectionType.getContentType()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).handleSecondaryContextualization(valueDeserializerMock, Mockito.any(BeanProperty.class), Mockito.eq(collectionType.getContentType()));
        Mockito.verify(valueDeserializerMock).deserializeWithType(jsonParser, context, Mockito.eq(valueTypeDeserializerMock));

        Mockito.verify(context).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType.getRawClass()), Mockito.any(BeanProperty.class));
        Mockito.verify(context).findValueDeserializer(Mockito.eq(collectionType));

        Mockito.verify(jsonParser).nextToken();
        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyNoMoreInteractions(context);

        Throwable exception = Mockito.captor(Mockito.capture(Throwable.class)).getValue();
        assertEquals("Unexpected value: [ Wrapped exception: java.lang.RuntimeException: Unexpected value: [Test String] ] (through reference chain: com.fasterxml.jackson.databind.JsonMappingException[Wrapped exception: java.lang.RuntimeException: Unexpected value: [Test String]]", exception.getMessage());
        assertEquals("Test String", exception.getCause().getCause().getMessage());
    }
}