package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;
    private ElExpression collectionExpression;
    private ElExpression labelExpression;
    private ElExpression valueExpression;
    private Component component;
    private StandaloneComponent topLevelComponent;

    @Before
    public void setUp() {
        collectionExpression = Mockito.mock(ElExpression.class);
        labelExpression = Mockito.mock(ElExpression.class);
        valueExpression = Mockito.mock(ElExpression.class);
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);

        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collectionExpression);
        dynamicSelectModel.label(labelExpression);
        dynamicSelectModel.value(valueExpression);
    }

    @Test
    public void testConstructorWithElExpressions() {
        dynamicSelectModel = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
        assertSame(collectionExpression, dynamicSelectModel.getCollection());
        assertSame(labelExpression, dynamicSelectModel.getLabel());
        assertSame(valueExpression, dynamicSelectModel.getValue());
    }

    @Test
    public void testEmptyConstructor() {
        assertEquals(dynamicSelectModel, new DynamicSelectModel());
        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());
    }

    @Test
    public void testCollection() {
        dynamicSelectModel.collection("testCollection");
        assertEquals("testCollection", dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testLabel() {
        dynamicSelectModel.label("testLabel");
        assertEquals("testLabel", dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testValue() {
        dynamicSelectModel.value("testValue");
        assertEquals("testValue", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testEnumeration() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("testCollection.values()");
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("ordinal()");

        Collection objects = Arrays.asList(new Object[]{"ONE", "TWO", "THREE"}).asList();
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(objects);

        assertEquals("ONE", dynamicSelectModel.getLabel(0));
        assertEquals("ONE", dynamicSelectModel.getValue(0));
        assertEquals("ONE", dynamicSelectModel.translateValue("ONE"));

        assertEquals("TWO", dynamicSelectModel.getLabel(1));
        assertEquals("1", dynamicSelectModel.getValue(1));
        assertNull(dynamicSelectModel.translateValue("FOUR"));
    }

    @Test
    public void testTranslator() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("testCollection.values()");
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("ordinal()");
        dynamicSelectModel.translator("testTranslator");

        Collection objects = Arrays.asList(new Object[]{"ONE", "TWO", "THREE"}).asList();
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(objects);

        List<Object> translatedObjects = new ArrayList<>();
        translatedObjects.add(Mockito.mock(Object.class));
        translatedObjects.add(Mockito.mock(Object.class));
        when(translator.eval(anyObject(), any(Component.class))).thenReturn(translatedObjects.get(0));

        assertEquals(translatedObjects.get(0), dynamicSelectModel.translateValue("ONE"));
        assertEquals(translatedObjects.get(1), dynamicSelectModel.translateValue("TWO"));
    }

    @Test
    public void testEmpty() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.empty(Mockito.mock(Object.class));

        Collection objects = new LinkedList<>(Arrays.asList(new Object[]{"ONE", "TWO", "THREE"}));
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(objects);

        Collection newCollection = new LinkedList<>();
        newCollection.add(Mockito.mock(Object.class));
        newCollection.addAll(objects);
        assertEquals(newCollection, dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetObjects() {
        Collection objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());

        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("ordinal()");
        dynamicSelectModel.objects = objects;

        assertEquals(objects, dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetComponent() {
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponent() {
        dynamicSelectModel.setComponent(component);
        assertSame(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertSame(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testValidate() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(null);
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label(null);
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value(null);
        dynamicSelectModel.validate();
    }
}