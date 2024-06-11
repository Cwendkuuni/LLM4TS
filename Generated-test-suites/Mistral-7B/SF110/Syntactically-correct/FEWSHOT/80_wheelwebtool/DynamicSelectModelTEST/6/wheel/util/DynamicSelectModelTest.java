package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {

    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private ElExpression translator;
    private DynamicSelectModel dynamicSelectModel;
    private Collection objects;
    private Object emptyObject;

    @Before
    public void setUp() {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        collection = mock(ElExpression.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        translator = mock(ElExpression.class);
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testNewInstanceWithCollectionLabelValue() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<>());

        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.value("value");

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals(0, objects.size());
    }

    @Test
    public void testNewInstanceWithoutArguments() {
        dynamicSelectModel = new DynamicSelectModel();

        Collection objects = dynamicSelectModel.getObjects();
        assertNull(objects);
    }

    @Test
    public void testCollectionMethod() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<>());

        dynamicSelectModel.collection("collection");

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals(0, objects.size());
    }

    @Test
    public void testLabelMethod() {
        when(label.eval(null, component)).thenReturn("label");

        dynamicSelectModel.label("label");

        String label = dynamicSelectModel.getLabel(0);
        assertEquals("label", label);
    }

    @Test(expected = WheelException.class)
    public void testLabelMethodError() {
        when(label.errorMessage(anyString())).thenThrow(new RuntimeException());

        dynamicSelectModel.label("label");

        dynamicSelectModel.getLabel(0);
    }

    @Test
    public void testValueMethod() {
        when(value.eval(null, component)).thenReturn("value");

        dynamicSelectModel.value("value");

        String value = dynamicSelectModel.getValue(0);
        assertEquals("value", value);
    }

    @Test(expected = WheelException.class)
    public void testValueMethodError() {
        when(value.errorMessage(anyString())).thenThrow(new RuntimeException());

        dynamicSelectModel.value("value");

        dynamicSelectModel.getValue(0);
    }

    @Test
    public void testEnumerationMethod() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<>());

        dynamicSelectModel.enumeration("enumeration");

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals(2, objects.size());
        assertSame(Integer.class, objects.toArray()[0].getClass());
        assertSame(String.class, objects.toArray()[1].getClass());
    }

    @Test
    public void testTranslatorMethod() {
        when(translator.eval(null, component)).thenReturn("translatedValue");

        dynamicSelectModel.translator("translator");

        Object translatedValue = dynamicSelectModel.translateValue("value");
        assertEquals("translatedValue", translatedValue);
    }

    @Test(expected = WheelException.class)
    public void testTranslatorMethodError() {
        when(translator.errorMessage(anyString())).thenThrow(new RuntimeException());

        dynamicSelectModel.translator("translator");

        dynamicSelectModel.translateValue("value");
    }

    @Test
    public void testEmptyMethod() {
        dynamicSelectModel.empty(new Object());

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals(2, objects.size());
    }

    @Test
    public void testGetObjectCount() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<>());

        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.value("value");

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals(0, dynamicSelectModel.getOptionCount());

        when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<>(Arrays.asList(new Object[]{"Option 1", 1, "Option 2", 2})));

        objects = dynamicSelectModel.getObjects();
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }
}