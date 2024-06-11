package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {

    private Component component;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;
    private DynamicSelectModel dynamicSelectModel;
    private List<Object> objects;

    @Before
    public void setUp() {
        component = mock(Component.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        collection = mock(ElExpression.class);
        translator = mock(ElExpression.class);
        dynamicSelectModel = new DynamicSelectModel();
        objects = new ArrayList<>();
    }

    @Test
    public void testNewInstanceWithCollectionLabelValue() {
        dynamicSelectModel.validate();
        dynamicSelectModel.collection(new ElExpression("collection"));
        dynamicSelectModel.label("label");
        dynamicSelectModel.value("value");

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }

    @Test(expected = WheelException.class)
    public void testNewInstanceWithoutCollection() {
        dynamicSelectModel.validate();
        dynamicSelectModel.collection(null);
        dynamicSelectModel.label("label");
        dynamicSelectModel.value("value");

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        // Throws WheelException when collection is null
    }

    @Test(expected = WheelException.class)
    public void testNewInstanceWithoutLabel() {
        dynamicSelectModel.validate();
        dynamicSelectModel.collection(new ElExpression("collection"));
        dynamicSelectModel.label(null);
        dynamicSelectModel.value("value");

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        // Throws WheelException when label is null
    }

    @Test(expected = WheelException.class)
    public void testNewInstanceWithoutValue() {
        dynamicSelectModel.validate();
        dynamicSelectModel.collection(new ElExpression("collection"));
        dynamicSelectModel.label("label");
        dynamicSelectModel.value(null);

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        // Throws WheelException when value is null
    }

    @Test
    public void testCollectionMethod() {
        dynamicSelectModel.collection("collection");
        assertEquals("collection", dynamicSelectModel.collection.getExpression());
    }

    @Test
    public void testLabelMethod() {
        dynamicSelectModel.label("label");
        assertEquals("label", dynamicSelectModel.label.getExpression());
    }

    @Test
    public void testValueMethod() {
        dynamicSelectModel.value("value");
        assertEquals("value", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testEnumerationMethod() {
        dynamicSelectModel.enumeration("enumeration");
        assertEquals("enumeration.values()", dynamicSelectModel.collection.getExpression());
        assertEquals("toString()", dynamicSelectModel.label.getExpression());
        assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testTranslatorMethod() {
        dynamicSelectModel.translator("translator");
        assertEquals("translator", dynamicSelectModel.translator.getExpression());
    }

    @Test
    public void testEmptyMethod() {
        dynamicSelectModel.empty(new Object());
        assertNull(dynamicSelectModel.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        objects.add(new Object());
        objects.add(new Object());

        when(collection.eval(component, component)).thenReturn(objects.toArray());

        assertEquals(2, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        objects.add(new Object() {
            @Override
            public String toString() {
                return "label1";
            }
        });

        when(label.eval(objects.toArray()[0], component)).thenReturn("label1");

        assertEquals("label1", dynamicSelectModel.getLabel(0));
    }

    @Test
    public void testGetValue() {
        objects.add(new Object() {
            @Override
            public String toString() {
                return "value1";
            }
        });

        when(value.eval(objects.toArray()[0], component)).thenReturn("value1");

        assertEquals("value1", dynamicSelectModel.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        objects.add(new Object() {
            @Override
            public String toString() {
                return "value1";
            }
        });

        when(value.eval(objects.toArray()[0], component)).thenReturn("value1");
        when(translator.eval(objects.toArray()[0], component)).thenReturn("translatedValue1");

        assertSame("translatedValue1", dynamicSelectModel.translateValue("value1"));
    }

    @Test
    public void testGetObjects() {
        Object[] array = new Object[] { new Object(), new Object() };

        when(collection.eval(component, component)).thenReturn(array);

        List<Object> expectedObjects = new ArrayList<>();
        expectedObjects.addAll(Arrays.asList(array));

        List<Object> actualObjects = dynamicSelectModel.getObjects();
        assertEquals(expectedObjects, actualObjects);
    }

    @Test
    public void testGetObjectsEmptyList() {
        dynamicSelectModel.reset();

        List<Object> expectedObjects = Collections.emptyList();

        List<Object> actualObjects = dynamicSelectModel.getObjects();
        assertEquals(expectedObjects, actualObjects);
    }
}