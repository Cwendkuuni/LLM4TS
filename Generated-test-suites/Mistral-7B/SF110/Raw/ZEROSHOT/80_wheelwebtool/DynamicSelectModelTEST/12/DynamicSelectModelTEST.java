package wheel.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DynamicSelectModelTest
{
    private Component component;
    private StandaloneComponent topLevelComponent;
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp()
    {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @After
    public void tearDown()
    {
        dynamicSelectModel = null;
        component = null;
        topLevelComponent = null;
    }

    @Test
    public void testDynamicSelectModelConstructorWithCollectionLabelValue()
    {
        dynamicSelectModel.collection("someCollection")
                .label("someLabel")
                .value("someValue");

        assertEquals("someCollection", dynamicSelectModel.getCollection().getExpression());
        assertEquals("someLabel", dynamicSelectModel.getLabel().getExpression());
        assertEquals("someValue", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testDynamicSelectModelEmptyConstructor()
    {
        dynamicSelectModel = new DynamicSelectModel();

        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());
    }

    @Test
    public void testDynamicSelectModelCollectionMethod()
    {
        dynamicSelectModel.collection("someCollection");

        assertEquals("someCollection", dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testDynamicSelectModelLabelMethod()
    {
        dynamicSelectModel.label("someLabel");

        assertEquals("someLabel", dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testDynamicSelectModelValueMethod()
    {
        dynamicSelectModel.value("someValue");

        assertEquals("someValue", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testDynamicSelectModelEnumerationMethod()
    {
        dynamicSelectModel.enumeration("someEnumeration");

        assertEquals("someEnumeration.values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testDynamicSelectModelTranslatorMethod()
    {
        dynamicSelectModel.translator("someTranslator");

        assertEquals("someTranslator", dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testDynamicSelectModelEmptyMethod()
    {
        dynamicSelectModel.empty("someEmptyObject");

        assertSame(dynamicSelectModel.getEmptyObject(), "someEmptyObject");
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelInvalidCollection()
    {
        dynamicSelectModel.collection(null);

        // Should throw a WheelException
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelInvalidLabel()
    {
        dynamicSelectModel.label(null);

        // Should throw a WheelException
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelInvalidValue()
    {
        dynamicSelectModel.value(null);

        // Should throw a WheelException
    }

    @Test
    public void testDynamicSelectModelGetOptionCount()
    {
        dynamicSelectModel.collection(Mockito.mock(Collection.class));

        Collection collection = Mockito.mock(Collection.class);
        when(collection.size()).thenReturn(5);
        when(dynamicSelectModel.getObjects()).thenReturn(collection);

        assertEquals(5, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testDynamicSelectModelGetLabel()
    {
        String labelExpression = "someLabelExpression";
        dynamicSelectModel.label(new ElExpression(labelExpression));

        String label = "someLabelValue";
        when(dynamicSelectModel.getObjects().toArray()[0]).thenReturn(label);

        assertEquals(label, dynamicSelectModel.getLabel(0));
    }

    @Test
    public void testDynamicSelectModelGetValue()
    {
        String valueExpression = "someValueExpression";
        dynamicSelectModel.value(new ElExpression(valueExpression));

        String value = "someValueValue";
        when(dynamicSelectModel.getObjects().toArray()[0]).thenReturn(value);

        assertEquals(value, dynamicSelectModel.getValue(0));
    }

    @Test
    public void testDynamicSelectModelTranslateValue()
    {
        String value = "someValue";
        dynamicSelectModel.value(new ElExpression(value));
        String translatedValue = "translatedValue";
        dynamicSelectModel.translator(new ElExpression(translatedValue));

        when(dynamicSelectModel.getObjects().toArray()[0].toString()).thenReturn(value);
        when(dynamicSelectModel.translator.eval(Mockito.any(), Mockito.any())).thenReturn("translatedValue");

        assertEquals("translatedValue", dynamicSelectModel.translateValue(value));
    }

    @Test
    public void testDynamicSelectModelGetObjects()
    {
        List collection = new ArrayList();
        collection.add("Option 1");
        collection.add("Option 2");

        dynamicSelectModel.collection(new ElExpression(collection.toString()));

        Collection objects = dynamicSelectModel.getObjects();

        assertEquals(2, objects.size());

        List expectedList = new ArrayList();
        expectedList.add("Option 1");
        expectedList.add("Option 2");

        List actualList = new ArrayList();
        actualList.addAll((Collection)objects);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDynamicSelectModelGetObjectsWithEmptyOption()
    {
        Object emptyObject = "Empty Option";
        dynamicSelectModel.empty(emptyObject);
        Collection objects = dynamicSelectModel.getObjects();

        assertEquals(1, objects.size());
        assertEquals(emptyObject, objects.toArray()[0]);
    }

    @Test
    public void testDynamicSelectModelGetObjectsWithCollectionAndEmptyOption()
    {
        List collection = new ArrayList();
        collection.add("Option 1");
        collection.add("Option 2");

        dynamicSelectModel.collection(new ElExpression(collection.toString()));
        dynamicSelectModel.empty("Empty Option");
        Collection objects = dynamicSelectModel.getObjects();

        assertEquals(2, objects.size());

        List expectedList = new ArrayList();
        expectedList.add("Empty Option");
        expectedList.addAll(collection);

        List actualList = new ArrayList();
        actualList.addAll((Collection)objects);

        assertEquals(expectedList, actualList);
    }
}