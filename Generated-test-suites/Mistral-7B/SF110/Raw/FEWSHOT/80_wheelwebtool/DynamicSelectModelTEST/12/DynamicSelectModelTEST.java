package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
        Collection collection = Mockito.mock(Collection.class);
        ElExpression label = Mockito.mock(ElExpression.class);
        ElExpression value = Mockito.mock(ElExpression.class);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(label, value, collection);

        assertEquals(collection, dynamicSelectModel.getObjects());
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }

    @Test
    public void testDynamicSelectModelConstructionWithoutArguments() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        assertNull(dynamicSelectModel.collection);
        assertNull(dynamicSelectModel.label);
        assertNull(dynamicSelectModel.value);
    }

    @Test
    public void testCollectionMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression collectionExpression = Mockito.mock(ElExpression.class);

        dynamicSelectModel.collection("collectionExpression");

        assertEquals(collectionExpression, dynamicSelectModel.collection);
    }

    @Test
    public void testLabelMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression labelExpression = Mockito.mock(ElExpression.class);

        dynamicSelectModel.label("labelExpression");

        assertEquals(labelExpression, dynamicSelectModel.label);
    }

    @Test
    public void testValueMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression valueExpression = Mockito.mock(ElExpression.class);

        dynamicSelectModel.value("valueExpression");

        assertEquals(valueExpression, dynamicSelectModel.value);
    }

    @Test
    public void testEnumerationMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression enumerationExpression = Mockito.mock(ElExpression.class);

        dynamicSelectModel.enumeration("enumerationExpression");

        assertEquals(enumerationExpression, dynamicSelectModel.collection);
        assertEquals(new ElExpression("toString()"), dynamicSelectModel.label);
        assertEquals(new ElExpression("ordinal()"), dynamicSelectModel.value);
    }

    @Test
    public void testTranslatorMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression translatorExpression = Mockito.mock(ElExpression.class);

        dynamicSelectModel.translator("translatorExpression");

        assertEquals(translatorExpression, dynamicSelectModel.translator);
    }

    @Test
    public void testEmptyMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Object emptyObject = new Object();

        dynamicSelectModel.empty(emptyObject);

        Collection objects = dynamicSelectModel.getObjects();
        assertTrue(objects.contains(emptyObject));
    }

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection objects = Arrays.asList("Option 1", "Option 2", "Option 3");
        when(dynamicSelectModel.getObjects()).thenReturn(objects);

        assertEquals(3, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection objects = Arrays.asList(new Object() {
            @Override
            public String toString() {
                return "Label for Option 1";
            }
        }, new Object() {
            @Override
            public String toString() {
                return "Label for Option 2";
            }
        });
        when(dynamicSelectModel.getObjects()).thenReturn(objects);

        assertEquals("Label for Option 1", dynamicSelectModel.getLabel(0));
        assertEquals("Label for Option 2", dynamicSelectModel.getLabel(1));
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWhenCollectionIsNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = null;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWhenCollectionEvaluatesToNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression collectionExpression = Mockito.mock(ElExpression.class);
        when(collectionExpression.eval(any(Component.class), any(Component.class))).thenReturn(null);
        dynamicSelectModel.collection = collectionExpression;

        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            // Expected
        }
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWhenCollectionEvaluatesToInvalidType() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression collectionExpression = Mockito.mock(ElExpression.class);
        when(collectionExpression.eval(any(Component.class), any(Component.class))).thenReturn(new Object());

        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            // Expected
        }
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection objects = Arrays.asList("Option 1", "Option 2", "Option 3");
        when(dynamicSelectModel.getObjects()).thenReturn(objects);

        String valueToTranslate = "Option 2";

        Object translatedValue = dynamicSelectModel.translateValue(valueToTranslate);

        assertEquals("Option 2", translatedValue);

        String anotherValueToTranslate = "Invalid Value";

        assertNull(dynamicSelectModel.translateValue(anotherValueToTranslate));
    }

    @Test
    public void testGetObjectsWithEmptyObject() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Object emptyObject = new Object();
        dynamicSelectModel.empty(emptyObject);
        Collection objects = Arrays.asList("Option 1", "Option 2", "Option 3");
        when(dynamicSelectModel.getObjects()).thenReturn(objects);

        Collection newObjects = new ArrayList<>();
        newObjects.add(emptyObject);
        newObjects.addAll(objects);

        assertEquals(newObjects, dynamicSelectModel.getObjects());
    }
}