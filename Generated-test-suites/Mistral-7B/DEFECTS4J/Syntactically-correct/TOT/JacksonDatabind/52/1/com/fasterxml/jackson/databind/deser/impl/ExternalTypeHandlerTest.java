import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ObjectMock bean;
    private JsonParser parserMock;
    private DeserializationContext contextMock;

    @Before
    public void setUp() throws Exception {
        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(newExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class)));
        properties.add(newExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class)));

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null);

        bean = mock(Object.class);
        parserMock = mock(JsonParser.class);
        contextMock = mock(DeserializationContext.class);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "testProp";
        String typeId = "String";
        when(parserMock.getText()).thenReturn(typeId);
        handler.handleTypePropertyValue(parserMock, contextMock, propName, bean);
        verify(bean).getClass(); // checks if bean is not null
        verifyNoMoreInteractions(bean); // no property was set
        verify(contextMock).handleUnexpectedValue(parserMock, bean.getClass(), propName, typeId);
    }

    @Test
    public void testHandlePropertyValueWithValidTypeId() throws IOException {
        String propName = "testProp";
        String typeId = "String";
        when(parserMock.getText()).thenReturn(typeId);
        handler.handlePropertyValue(parserMock, contextMock, propName, bean);
        verify(bean).getClass(); // checks if bean is not null

        ExtTypedProperty prop = findProperty(propName);
        assertNotNull(prop);
        assertEquals(typeId, prop.getTypeDeserializer().getTypeIdResolver().idFromValueAndType(null, String.class));
        assertEquals(typeId, prop.getTypePropertyName());
        assertEquals(String.class, prop.getProperty().getType());
        verify(parserMock).nextToken(); // checks if all tokens were parsed
        verifyNoMoreInteractions(parserMock); // no more parser interactions
        verify(bean).setPropertyValue(eq(prop.getProperty()), eq(typeId));
    }

    @Test
    public void testHandlePropertyValueWithNullTypeId() throws IOException {
        String propName = "testProp";
        when(parserMock.getText()).thenReturn(null);
        handler.handlePropertyValue(parserMock, contextMock, propName, bean);
        verify(bean).getClass(); // checks if bean is not null
        verifyNoMoreInteractions(bean); // no property was set
        verify(contextMock).handleMissingTypeId(parserMock, bean.getClass(), propName);
    }

    @Test
    public void testHandlePropertyValueWithNullBean() throws IOException {
        String propName = "testProp";
        handler.handlePropertyValue(parserMock, contextMock, propName, null);
        verifyZeroInteractions(bean);
        verifyZeroInteractions(parserMock);
        verify(contextMock).reportMappingException("Missing property 'testProp' for external type id 'testId'");
    }

    @Test
    public void testHandlePropertyValueWithNullProperty() throws IOException {
        String propName = "testProp";
        ExtTypedProperty prop = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        prop.getProperty().set(bean, "initialValue");
        handler._nameToPropertyIndex.put(propName, 0);
        handler._properties[0] = prop;
        when(parserMock.getText()).thenReturn("String");

        handler.handlePropertyValue(parserMock, contextMock, propName, bean);
        assertEquals("initialValue", prop.getProperty().get(bean));
        verify(parserMock).nextToken(); // checks if all tokens were parsed
        verifyNoMoreInteractions(parserMock); // no more parser interactions
        verify(bean).setPropertyValue(eq(prop.getProperty()), eq("String"));
    }

    @Test
    public void testHandlePropertyValueWithNullPropertyAndNullTypeId() throws IOException {
        String propName = "testProp";
        ExtTypedProperty prop = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        handler._nameToPropertyIndex.put(propName, 0);
        handler._properties[0] = prop;

        handler.handlePropertyValue(parserMock, contextMock, propName, bean);
        assertEquals(null, prop.getProperty().get(bean));
        verify(parserMock).nextToken(); // checks if all tokens were parsed
        verifyNoMoreInteractions(parserMock); // no more parser interactions
        verify(bean, never()).setPropertyValue(any(), any());
    }

    @Test
    public void testCompleteWithValidTypeIds() throws IOException {
        handler.start();
        String propName1 = "testProp1";
        String typeId1 = "String";
        handler.handleTypePropertyValue(parserMock, contextMock, propName1, bean);

        String propName2 = "testProp2";
        String typeId2 = "Integer";
        handler.handlePropertyValue(parserMock, contextMock, propName2, bean);

        handler.complete(parserMock, contextMock, bean);
        verify(bean).getClass(); // checks if bean is not null

        ExtTypedProperty prop1 = findProperty(propName1);
        ExtTypedProperty prop2 = findProperty(propName2);
        assertNotNull(prop1);
        assertNotNull(prop2);
        assertEquals(typeId1, prop1.getTypeDeserializer().getTypeIdResolver().idFromValueAndType(null, String.class));
        assertEquals(typeId1, prop1.getTypePropertyName());
        assertEquals(String.class, prop1.getProperty().getType());
        assertEquals(typeId2, prop2.getTypeDeserializer().getTypeIdResolver().idFromValueAndType(null, Integer.class));
        assertEquals(typeId2, prop2.getTypePropertyName());
        assertEquals(Integer.class, prop2.getProperty().getType());

        // test that all properties were set
        String expectedPropertyName = prop1.getProperty().getName() + "_set";
        String actualPropertyName = prop1.getProperty().getName();
        assertEquals(expectedPropertyName, actualPropertyName);
        assertEquals(typeId1, prop1.getProperty().get(bean));

        expectedPropertyName = prop2.getProperty().getName() + "_set";
        actualPropertyName = prop2.getProperty().getName();
        assertEquals(expectedPropertyName, actualPropertyName);
        assertEquals(typeId2, prop2.getProperty().get(bean));
    }

    @Test
    public void testCompleteWithMissingTypeIds() throws IOException {
        handler.start();
        String propName1 = "testProp1";
        String typeId1 = "String";
        handler.handleTypePropertyValue(parserMock, contextMock, propName1, bean);

        String propName2 = "testProp2";
        handler.handlePropertyValue(parserMock, contextMock, propName2, bean);

        handler.complete(parserMock, contextMock, bean);
        verify(bean).getClass(); // checks if bean is not null

        ExtTypedProperty prop1 = findProperty(propName1);
        ExtTypedProperty prop2 = findProperty(propName2);
        assertNotNull(prop1);
        assertNull(prop2);

        verify(parserMock, times(2)).nextToken(); // checks if all tokens were parsed
        verify(parserMock).getCurrentName(); // checks if parser name was read
        verify(bean, never()).setPropertyValue(any(), any());

        verify(contextMock).handleMissingTypeId(parserMock, bean.getClass(), propName2);
    }

    @Test
    public void testCompleteWithMissingBean() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parserMock, contextMock, "testProp1", null);
        handler.handlePropertyValue(parserMock, contextMock, "testProp2", bean);

        handler.complete(parserMock, contextMock, null);
        verifyZeroInteractions(bean);
        verifyZeroInteractions(parserMock);
        verify(contextMock).reportMappingException("Missing property 'testProp1' for external type id 'testId1'");
    }

    @Test
    public void testCompleteWithNullBean() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parserMock, contextMock, "testProp1", null);
        handler.handlePropertyValue(parserMock, contextMock, "testProp2", bean);

        handler.complete(parserMock, contextMock, null);
        verifyZeroInteractions(bean);
        verifyZeroInteractions(parserMock);
        verify(contextMock).reportMappingException("Missing property 'testProp1' for external type id 'testId1'");
    }

    private ExtTypedProperty findProperty(String propName) {
        for (ExtTypedProperty prop : handler._properties) {
            if (prop.getProperty().getName().equals(propName)) {
                return prop;
            }
        }
        return null;
    }
}