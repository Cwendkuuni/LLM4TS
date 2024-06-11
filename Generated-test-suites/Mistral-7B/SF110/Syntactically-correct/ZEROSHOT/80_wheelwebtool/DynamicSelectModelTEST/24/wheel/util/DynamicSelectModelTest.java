package wheel.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DynamicSelectModelTest
{
    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;

    @Before
    public void setUp()
    {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        collection = mock(ElExpression.class);
        translator = mock(ElExpression.class);
        emptyObject = new Object();

        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testInitializationWithCollectionLabelAndValue()
    {
        dynamicSelectModel.collection("testCollection").label("testLabel").value("testValue");
        verify(component).errorMessage(Mockito.never());
        verify(topLevelComponent).errorMessage(Mockito.never());

        assertEquals("testLabel", dynamicSelectModel.getLabel(0));
        assertEquals("testValue", dynamicSelectModel.getValue(0));
    }

    @Test(expected = WheelException.class)
    public void testInitializationWithoutCollection()
    {
        dynamicSelectModel.label("testLabel").value("testValue");
        dynamicSelectModel.validate();

        verify(component).errorMessage("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.");
    }

    @Test(expected = WheelException.class)
    public void testInitializationWithoutLabel()
    {
        dynamicSelectModel.collection("testCollection").value("testValue");
        dynamicSelectModel.validate();

        verify(component).errorMessage("Could not create DynamicSelection model. Label attribute was null or not an el-expression.");
    }

    @Test(expected = WheelException.class)
    public void testInitializationWithoutValue()
    {
        dynamicSelectModel.collection("testCollection").label("testLabel");
        dynamicSelectModel.validate();

        verify(component).errorMessage("Could not create DynamicSelection model. Value attribute was null or not an el-expression.");
    }

    @Test
    public void testGetOptionCount()
    {
        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class));

        assertEquals(0, dynamicSelectModel.getOptionCount());

        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class))
            .addObject(Mockito.mock(Object.class));

        assertEquals(1, dynamicSelectModel.getOptionCount());

        List<Object> objects = new ArrayList<>();
        objects.add(Mockito.mock(Object.class));
        objects.add(Mockito.mock(Object.class));

        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>(objects))
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class));

        assertEquals(2, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel()
    {
        when(label.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("testLabel");

        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(label)
            .value(Mockito.mock(ElExpression.class));

        assertEquals("testLabel", dynamicSelectModel.getLabel(0));

        when(label.eval(Mockito.anyObject(), Mockito.anyObject())).thenThrow(new WheelException("Could not evaluate expression label to create a label for dynamic ISelectModel. Index was 0"));

        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(label)
            .value(Mockito.mock(ElExpression.class));

        dynamicSelectModel.getLabel(0);

        verify(label).errorMessage("Could not evaluate expression label to create a label for dynamic ISelectModel. Index was 0");
    }

    @Test
    public void testGetValue()
    {
        when(value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("testValue");

        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(Mockito.mock(ElExpression.class))
            .value(value);

        assertEquals("testValue", dynamicSelectModel.getValue(0));

        when(value.eval(Mockito.anyObject(), Mockito.anyObject())).thenThrow(new WheelException("Could not evaluate expression value to create a value for dynamic ISelectModel. Index was 0"));

        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(Mockito.mock(ElExpression.class))
            .value(value);

        dynamicSelectModel.getValue(0);

        verify(value).errorMessage("Could not evaluate expression value to create a value for dynamic ISelectModel. Index was 0");
    }

    @Test
    public void testTranslateValue()
    {
        Object translatedValue = Mockito.mock(Object.class);

        when(translator.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(translatedValue);

        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class))
            .translator(translator);

        Object testObject = Mockito.mock(Object.class);

        when(value.eval(testObject, component)).thenReturn("testValue");

        assertSame(testObject, dynamicSelectModel.translateValue("testValue"));

        verify(translator).eval(testObject, component);
    }

    @Test
    public void testGetObjects()
    {
        Collection testObjects = new ArrayList<>();
        testObjects.add(Mockito.mock(Object.class));
        testObjects.add(Mockito.mock(Object.class));

        when(collection.eval(topLevelComponent, component)).thenReturn(testObjects);

        dynamicSelectModel = new DynamicSelectModel()
            .collection("testCollection")
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class));

        Collection objects = dynamicSelectModel.getObjects();

        assertEquals(testObjects, objects);
    }

    @Test
    public void testGetObjectsWithEmptyCollection()
    {
        dynamicSelectModel = new DynamicSelectModel()
            .collection(new ArrayList<>())
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class));

        Collection objects = dynamicSelectModel.getObjects();

        assertNull(objects);
    }

    @Test
    public void testGetObjectsWithObjectArray()
    {
        Object[] testObjects = new Object[2];
        testObjects[0] = Mockito.mock(Object.class);
        testObjects[1] = Mockito.mock(Object.class);

        when(collection.eval(topLevelComponent, component)).thenReturn(testObjects);

        dynamicSelectModel = new DynamicSelectModel()
            .collection("testCollection")
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class));

        Collection objects = dynamicSelectModel.getObjects();

        assertEquals(new ArrayList<>(Arrays.asList(testObjects)), objects);
    }

    @Test
    public void testGetObjectsWithLinkedList()
    {
        List<Object> testObjects = new ArrayList<>();
        testObjects.add(Mockito.mock(Object.class));
        testObjects.add(Mockito.mock(Object.class));

        Collection objects = new LinkedList<>(testObjects);

        when(collection.eval(topLevelComponent, component)).thenReturn(objects);

        dynamicSelectModel = new DynamicSelectModel()
            .collection("testCollection")
            .label(Mockito.mock(ElExpression.class))
            .value(Mockito.mock(ElExpression.class));

        Collection objectsFromModel = dynamicSelectModel.getObjects();

        assertEquals(objects, objectsFromModel);
    }

    @After
    public void tearDown()
    {
        dynamicSelectModel = null;
        component = null;
        topLevelComponent = null;
        label = null;
        value = null;
        collection = null;
        translator = null;
        objects = null;
        emptyObject = null;
    }
}