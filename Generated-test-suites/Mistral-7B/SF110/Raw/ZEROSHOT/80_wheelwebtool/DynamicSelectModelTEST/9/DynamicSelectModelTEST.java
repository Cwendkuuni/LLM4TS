package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DynamicSelectModelTest
{
    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression collectionExpression;
    private ElExpression labelExpression;
    private ElExpression valueExpression;
    private ElExpression translatorExpression;
    private Collection objects;

    @Before
    public void setUp()
    {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        collectionExpression = Mockito.spy(new ElExpression("collection"));
        labelExpression = Mockito.spy(new ElExpression("label"));
        valueExpression = Mockito.spy(new ElExpression("value"));
        translatorExpression = Mockito.spy(new ElExpression("translator"));

        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        dynamicSelectModel.collection(collectionExpression);
        dynamicSelectModel.label(labelExpression);
        dynamicSelectModel.value(valueExpression);
    }

    @Test
    public void testValidConstructor()
    {
        dynamicSelectModel = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testInvalidConstructor()
    {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.validate();
    }

    @Test
    public void testCollectionMethod()
    {
        dynamicSelectModel.collection("collectionExpression");
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(new ArrayList<>());
        dynamicSelectModel.getObjects();
        verify(collectionExpression).eval(topLevelComponent, component);
    }

    @Test
    public void testLabelMethod()
    {
        dynamicSelectModel.label("labelExpression");
        when(labelExpression.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("labelValue");
        String label = dynamicSelectModel.getLabel(0);
        verify(labelExpression).eval(Mockito.anyObject(), Mockito.anyObject());
        assertEquals("labelValue", label);
    }

    @Test
    public void testValueMethod()
    {
        dynamicSelectModel.value("valueExpression");
        when(valueExpression.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(123);
        String value = dynamicSelectModel.getValue(0);
        verify(valueExpression).eval(Mockito.anyObject(), Mockito.anyObject());
        assertEquals("123", value);
    }

    @Test
    public void testTranslatorMethod()
    {
        dynamicSelectModel.translator("translatorExpression");
        when(translatorExpression.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("translatorValue");
        Object translatedValue = dynamicSelectModel.translateValue("value");
        verify(translatorExpression).eval(Mockito.anyObject(), Mockito.anyObject());
        assertEquals("translatorValue", translatedValue);
    }

    @Test
    public void testEmptyMethod()
    {
        dynamicSelectModel.empty(new Object());
        List<Object> objects = dynamicSelectModel.getObjects();
        assertEquals(2, objects.size());
        verify(components, times(1)).add(Mockito.any());
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWhenNull()
    {
        dynamicSelectModel.setObjects(null);
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWhenNotCollectionOrObjectArray()
    {
        dynamicSelectModel.setObjects(new String());
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjects()
    {
        objects = new ArrayList<>();
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        List<Object> result = dynamicSelectModel.getObjects();
        assertEquals(1, result.size());
        assertEquals(result.get(0), objects.get(0));
    }

    @Test
    public void testGetComponent()
    {
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponent()
    {
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent()
    {
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent()
    {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testValidate()
    {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWhenNullCollection()
    {
        dynamicSelectModel.collection(null);
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWhenNullLabel()
    {
        dynamicSelectModel.label(null);
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWhenNullValue()
    {
        dynamicSelectModel.value(null);
        dynamicSelectModel.validate();
    }

    @Test
    public void testReset()
    {
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}