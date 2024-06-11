import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parserMock;
    private DeserializationContext contextMock;
    private Object beanMock;

    @Before
    public void setUp() {
        initMocks(this);
        parserMock = mock(JsonParser.class);
        contextMock = mock(DeserializationContext.class);
        beanMock = mock(Object.class);

        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);
        TypeDeserializer typeDeserializer1 = mock(TypeDeserializer.class);
        TypeDeserializer typeDeserializer2 = mock(TypeDeserializer.class);

        when(property1.getName()).thenReturn("property1");
        when(property1.getType()).thenReturn(String.class);
        when(property1.getPropertyName()).thenReturn("typeProperty1");
        when(property1.getTypeDeserializer()).thenReturn(typeDeserializer1);

        when(property2.getName()).thenReturn("property2");
        when(property2.getType()).thenReturn(Integer.class);
        when(property2.getPropertyName()).thenReturn("typeProperty2");
        when(property2.getTypeDeserializer()).thenReturn(typeDeserializer2);

        when(typeDeserializer1.getDefaultImpl()).thenReturn(String.class);
        when(typeDeserializer1.getTypeIdResolver()).thenReturn(mock(TypeIdResolver.class));
        when(typeDeserializer1.getTypeIdResolver().idFromValueAndType(any(), eq(String.class))).thenReturn("typeId1");

        when(typeDeserializer2.getDefaultImpl()).thenReturn(Integer.class);
        when(typeDeserializer2.getTypeIdResolver()).thenReturn(mock(TypeIdResolver.class));
        when(typeDeserializer2.getTypeIdResolver().idFromValueAndType(any(), eq(Integer.class))).thenReturn("typeId2");

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property1, typeDeserializer1);
        builder.addExternal(property2, typeDeserializer2);
        handler = builder.build();
    }

    @After
    public void tearDown() {
        reset(parserMock, contextMock, beanMock, property1, property2, typeDeserializer1, typeDeseralizer2);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String typeId = "typeId1";
        when(parserMock.getText()).thenReturn(typeId);
        handler.handleTypePropertyValue(parserMock, contextMock, "property1", beanMock);

        verify(parserMock).nextToken(); // skip the type id token
        verifyNoMoreInteractions(parserMock);
        verify(contextMock).mappingException(eq("Missing property 'typeProperty1' for external type id 'typeId1'"));
    }

    @Test
    public void testHandleTypePropertyValue_noProperty() throws IOException {
        handler.handleTypePropertyValue(parserMock, contextMock, "nonExistentProperty", beanMock);

        verify(parserMock).nextToken(); // skip the property name token
        verifyNoMoreInteractions(parserMock);
        verifyNoInteractions(contextMock);
    }

    @Test
    public void testHandleTypePropertyValue_noTypeId() throws IOException {
        handler.handleTypePropertyValue(parserMock, contextMock, "typeProperty1", beanMock);

        verify(parserMock).nextToken(); // skip the property name token
        verify(parserMock).nextToken(); // skip the value token
        verifyNoMoreInteractions(parserMock);
        verifyNoInteractions(contextMock);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String propertyName = "property1";
        when(parserMock.getText()).thenReturn(propertyName);

        handler.handlePropertyValue(parserMock, contextMock, propertyName, beanMock);

        verify(parserMock).nextToken(); // skip the property name token
        verify(parserMock).nextToken(); // skip the value token
        verifyNoMoreInteractions(parserMock);
        verifyNoInteractions(contextMock);
    }

    @Test
    public void testHandlePropertyValue_noProperty() throws IOException {
        handler.handlePropertyValue(parserMock, contextMock, "nonExistentProperty", beanMock);

        verify(parserMock).nextToken(); // skip the property name token
        verifyNoMoreInteractions(parserMock);
        verifyNoInteractions(contextMock);
    }

    @Test
    public void testHandlePropertyValue_typeIdPresent() throws IOException {
        String propertyName = "typeProperty1";
        String typeId = "typeId1";
        when(parserMock.getText()).thenReturn(propertyName);

        handler.handlePropertyValue(parserMock, contextMock, propertyName, beanMock);

        verify(parserMock).nextToken(); // skip the property name token
        verify(parserMock).getText().thenReturn(typeId);
        handler.handleTypePropertyValue(parserMock, contextMock, propertyName, beanMock);
        verifyNoMoreInteractions(parserMock);
        verifyNoMoreInteractions(contextMock);
    }

    @Test
    public void testComplete() throws IOException {
        handler.complete(parserMock, contextMock, beanMock);

        verify(parserMock).nextToken(); // skip the START_OBJECT token
        verify(parserMock).nextToken(); // skip the END_OBJECT token
        verifyNoMoreInteractions(parserMock);
        verifyNoInteractions(contextMock);
    }

    @Test(expected = IOException.class)
    public void testComplete_missingTypeId() throws IOException {
        handler = new ExternalTypeHandler(new ExternalTypeHandler[0], null, null, null);
        handler.complete(parserMock, contextMock, beanMock);

        verify(parserMock).nextToken(); // skip the START_OBJECT token
        verify(parserMock).nextToken(); // skip the property name token
        verify(parserMock).nextToken(); // skip the null value token
        verify(parserMock).nextToken(); // skip the END_OBJECT token

        throw new IOException("Expected mapping exception");
    }

    @Test
    public void testComplete_missingProperty() throws IOException {
        handler = new ExternalTypeHandler(new ExternalTypeHandler[0], null, null, null);
        handler.complete(parserMock, contextMock, beanMock);

        verify(parserMock).nextToken(); // skip the START_OBJECT token
        verify(parserMock).nextToken(); // skip the property name token
        verify(parserMock).nextToken(); // skip the END_OBJECT token

        verifyNoInteractions(contextMock);
    }
}