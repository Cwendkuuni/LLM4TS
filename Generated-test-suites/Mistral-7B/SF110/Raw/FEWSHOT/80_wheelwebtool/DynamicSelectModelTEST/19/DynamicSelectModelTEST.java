package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import wheel.*;

public class DynamicSelectModelTest {

    private ISelectModel dynamicModel;
    private Component component;
    private Collection collectionMock;

    @Before
    public void setup() {
        this.component = mock(Component.class);
        this.collectionMock = mock(Collection.class);
        when(this.component.getTopLevelComponent()).thenReturn(mock(StandaloneComponent.class));
        when(this.collectionMock.size()).thenReturn(3);

        this.dynamicModel = new DynamicSelectModel()
                .collection("collectionMock")
                .label("labelMock")
                .value("valueMock")
                .translator("translatorMock");
    }

    @Test
    public void testGetOptionCount() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);
        assertEquals(3, this.dynamicModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);
        final ArgumentCaptor<Object> labelArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Component> componentArgumentCaptor = ArgumentCaptor.forClass(Component.class);

        this.dynamicModel.getLabel(0);

        verify(this.labelMock).eval(eq(this.collectionMock.toArray()[0]), eq(componentArgumentCaptor.capture()));
        assertEquals(this.component, componentArgumentCaptor.getValue());
        assertEquals("labelMock", labelArgumentCaptor.getValue().getClass().getSimpleName());

        verifyNoMoreInteractions(this.labelMock);
        verifyNoMoreInteractions(this.component);

        assertEquals("Option 1", this.dynamicModel.getLabel(0));
    }

    @Test(expected = WheelException.class)
    public void testGetLabelWithError() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);
        this.dynamicModel.label(new ElExpression("errorMock"));

        this.dynamicModel.getLabel(0);
    }

    @Test
    public void testGetValue() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);
        final ArgumentCaptor<Object> valueArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Component> componentArgumentCaptor = ArgumentCaptor.forClass(Component.class);

        this.dynamicModel.getValue(0);

        verify(this.valueMock).eval(eq(this.collectionMock.toArray()[0]), eq(componentArgumentCaptor.capture()));
        assertEquals(this.component, componentArgumentCaptor.getValue());
        assertEquals("valueMock", valueArgumentCaptor.getValue().getClass().getSimpleName());

        verifyNoMoreInteractions(this.valueMock);
        verifyNoMoreInteractions(this.component);

        assertEquals("Option 1 Value", this.dynamicModel.getValue(0));
    }

    @Test(expected = WheelException.class)
    public void testGetValueWithError() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);
        this.dynamicModel.value(new ElExpression("errorMock"));

        this.dynamicModel.getValue(0);
    }

    @Test
    public void testTranslateValue() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);

        final Object result = this.dynamicModel.translateValue("Option 1 Value");

        verify(this.translatorMock).eval(eq(this.collectionMock.toArray()[0]), eq(this.component));
        assertEquals(this.component, componentArgumentCaptor.getValue());

        verifyNoMoreInteractions(this.translatorMock);

        assertEquals("Translated Value", result);
    }

    @Test
    public void testTranslateValueNull() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);

        assertNull(this.dynamicModel.translateValue(null));
    }

    @Test(expected = WheelException.class)
    public void testTranslateValueError() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);
        this.dynamicModel.translator(new ElExpression("errorMock"));

        this.dynamicModel.translateValue("Option 1 Value");
    }

    @Test
    public void testGetObjects() {
        when(this.dynamicModel.getObjects()).thenReturn(this.collectionMock);

        final List result = (List) this.dynamicModel.getObjects();
        assertEquals(4, result.size());

        assertTrue(result.contains("Option 1"));
        assertTrue(result.contains("Option 2"));
        assertTrue(result.contains("Option 3"));
        assertTrue(result.contains(this.dynamicModel.emptyObject));
    }

    @Test
    public void testGetEmptyObjects() {
        this.dynamicModel = new DynamicSelectModel()
                .collection("collectionMock")
                .label("labelMock")
                .value("valueMock")
                .translator("translatorMock")
                .empty("emptyObject");

        final List result = (List) this.dynamicModel.getObjects();
        assertEquals(4, result.size());

        assertTrue(result.contains("Option 1"));
        assertTrue(result.contains("Option 2"));
        assertTrue(result.contains("Option 3"));
        assertTrue(result.contains(this.dynamicModel.emptyObject));
    }

    @Test
    public void testValidate() {
        this.dynamicModel = new DynamicSelectModel();

        try {
            this.dynamicModel.validate();
            fail("Expected a WheelException to be thrown.");
        } catch (WheelException e) {
            // Expected
        }

        this.dynamicModel = new DynamicSelectModel()
                .collection("collectionMock")
                .label("labelMock")
                .value("valueMock");

        this.dynamicModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateMissingCollection() {
        this.dynamicModel = new DynamicSelectModel()
                .label("labelMock")
                .value("valueMock");

        this.dynamicModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateMissingLabel() {
        this.dynamicModel = new DynamicSelectModel()
                .collection("collectionMock")
                .value("valueMock");

        this.dynamicModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateMissingValue() {
        this.dynamicModel = new DynamicSelectModel()
                .collection("collectionMock")
                .label("labelMock");

        this.dynamicModel.validate();
    }
}