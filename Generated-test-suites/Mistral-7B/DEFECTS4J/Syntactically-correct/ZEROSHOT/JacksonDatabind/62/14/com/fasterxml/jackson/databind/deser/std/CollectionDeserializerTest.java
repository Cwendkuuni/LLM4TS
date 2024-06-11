package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.test.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

@JacksonStdImpl
public class CollectionDeserializerTest extends BaseTest {

    private CollectionDeserializer _collectionDeserializer;
    private JsonDeserializer<Object> _valueDeserializerMock;
    private TypeDeserializer _valueTypeDeserializerMock;
    private ValueInstantiator _valueInstantiatorMock;

    @BeforeMethod
    public void setUp() {
        _valueDeserializerMock = mock(JsonDeserializer.class);
        _valueTypeDeserializerMock = mock(TypeDeserializer.class);
        _valueInstantiatorMock = mock(ValueInstantiator.class);

        _collectionDeserializer = new CollectionDeserializer(
                createTypeFactory().constructType(List.class),
                _valueDeserializerMock,
                _valueTypeDeserializerMock,
                _valueInstantiatorMock,
                null,
                null);
    }

    @Test
    public void testIsCachable() {
        assertFalse(_collectionDeserializer.isCachable());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Create contextual instance with mocked JsonDeserializer for _valueDeserializer
        JsonDeserializer<Object> mockedJsonDeserializer = mock(JsonDeserializer.class);
        CollectionDeserializer contextualInstance = _collectionDeserializer.withResolved(mockedJsonDeserializer, null, null, null);
        verify(mockedJsonDeserializer).forProperty(any());

        // Test if contextual instance has the correct mocked JsonDeserializer
        assertEquals(_collectionDeserializer.getContentDeserializer(), contextualInstance.getContentDeserializer());
        assertEquals(_valueDeserializerMock, contextualInstance.getContentDeserializer());
    }

    @Test
    public void testDeserializeFromEmptyArray() throws IOException {
        JsonParser mockJsonParser = createMockJsonParser("[]");

        List<Object> result = _collectionDeserializer.deserialize(mockJsonParser, objectMapper);
        verifyNoMoreInteractions(mockJsonParser);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeFromArray() throws IOException {
        JsonParser mockJsonParser = createMockJsonParser("[1, 2, 3]");

        List<Object> result = _collectionDeserializer.deserialize(mockJsonParser, objectMapper);
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();

        List<Object> expectedResult = Arrays.asList(1, 2, 3);
        assertEquals(expectedResult, result);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeFromNonArray() throws IOException {
        JsonParser mockJsonParser = createMockJsonParser("{\"name\": \"John\"}");

        _collectionDeserializer.deserialize(mockJsonParser, objectMapper);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser mockJsonParser = createMockJsonParser("[1, 2, 3]");

        TypeDeserializer typeDeserializerMock = mock(TypeDeserializer.class);
        when(typeDeserializerMock.deserializeTypedFromArray(mockJsonParser, objectMapper)).thenReturn(Arrays.asList(1, 2, 3));

        List<Object> result = _collectionDeserializer.deserializeWithType(mockJsonParser, objectMapper, typeDeserializerMock);
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();

        List<Object> expectedResult = Arrays.asList(1, 2, 3);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser mockJsonParser = createMockJsonParser("1");

        List<Object> result = _collectionDeserializer.handleNonArray(mockJsonParser, objectMapper, Collections.emptyList());
        verify(mockJsonParser).nextToken();

        Object expectedResult = 1;
        assertEquals(expectedResult, result.get(0));
    }

}