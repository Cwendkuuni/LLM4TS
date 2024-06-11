package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import wheel.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private Collection objects;
    private Object emptyObject;
    private Component component;
    private StandaloneComponent topLevelComponent;

    @Before
    public void setUp() {
        collection = Mockito.mock(ElExpression.class);
        label = Mockito.mock(ElExpression.class);
        value = Mockito.mock(ElExpression.class);
        objects = Mockito.mock(Collection.class);
        emptyObject = Mockito.mock(Object.class);
        component = Mockito.mock(Component.class);
        topLevelComponent = Mockito.mock(StandaloneComponent.class);

        when(collection.eval(topLevelComponent, component)).thenAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] array = new Object[3];
                array[0] = "Option1";
                array[1] = "Option2";
                array[2] = "Option3";
                return array;
            }
        });

        when(label.eval(anyObject(), anyObject())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0).toString();
            }
        });

        when(value.eval(anyObject(), anyObject())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0).toString();
            }
        });

        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
    }

    @Test
    public void testGetOptionCount() {
        assertEquals(3, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        String label1 = dynamicSelectModel.getLabel(0);
        assertEquals("Option1", label1);

        String label2 = dynamicSelectModel.getLabel(1);
        assertEquals("Option2", label2);

        String label3 = dynamicSelectModel.getLabel(2);
        assertEquals("Option3", label3);
    }

    @Test
    public void testGetValue() {
        String value1 = dynamicSelectModel.getValue(0);
        assertEquals("Option1", value1);

        String value2 = dynamicSelectModel.getValue(1);
        assertEquals("Option2", value2);

        String value3 = dynamicSelectModel.getValue(2);
        assertEquals("Option3", value3);
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollection() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(null);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidCollectionType() {
        ElExpression invalidCollection = Mockito.mock(ElExpression.class);
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(invalidCollection);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullTopLevelComponent() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
        dynamicSelectModel.setTopLevelComponent(null);
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testTranslateValue() {
        String valueToTranslate = "Option2";
        Object translatedValue = dynamicSelectModel.translateValue(valueToTranslate);
        assertEquals("Option2", translatedValue);

        when(translator.eval(anyObject(), anyObject())).thenReturn("TranslatedValue");
        dynamicSelectModel.translator("translator");
        translatedValue = dynamicSelectModel.translateValue(valueToTranslate);
        assertEquals("TranslatedValue", translatedValue);
    }

    @Test
    public void testTranslateValueWithNullValue() {
        assertNull(dynamicSelectModel.translateValue(null));
    }

    @Test
    public void testTranslateValueWithEmptyList() {
        when(collection.eval(topLevelComponent, component)).thenReturn(Collections.emptyList());
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
        dynamicSelectModel.translateValue("value");
        verify(component, times(1)).errorMessage("Could not evaluate expression " + collection + " to translate value ' value'");
    }

    @Test
    public void testGetObjectsWithEmptyList() {
        when(collection.eval(topLevelComponent, component)).thenReturn(Collections.emptyList());
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
        dynamicSelectModel.getObjects();
        verify(component, times(1)).errorMessage("Could not evaluate expression " + collection + " to create a dynamic ISelectModel.");
    }

    @Test
    public void testGetObjectsWithNullObjects() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
        dynamicSelectModel.objects = null;
        dynamicSelectModel.getObjects();
        verify(component, times(1)).errorMessage("Could not evaluate expression " + collection + " to create a dynamic ISelectModel.");
    }
}