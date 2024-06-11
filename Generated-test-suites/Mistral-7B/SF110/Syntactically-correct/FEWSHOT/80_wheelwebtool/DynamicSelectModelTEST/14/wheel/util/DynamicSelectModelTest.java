package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;

    @Before
    public void setup() {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        collection = mock(ElExpression.class);
        translator = mock(ElExpression.class);

        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testEmptyConstructor() {
        dynamicSelectModel = new DynamicSelectModel();
        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());
    }

    @Test
    public void testCollectionConstructor() {
        dynamicSelectModel = new DynamicSelectModel(collection);
        assertEquals(collection, dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());

        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.value("testValue");
        assertEquals("testLabel", dynamicSelectModel.getLabel().getExpression());
        assertEquals("testValue", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testCollectionProperties() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.value("testValue");

        assertEquals("testCollection", dynamicSelectModel.getCollection().getExpression());
        assertEquals("testLabel", dynamicSelectModel.getLabel().getExpression());
        assertEquals("testValue", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testGetObjects() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<>());
        Collection objects = dynamicSelectModel.getObjects();
        assertNotNull(objects);
        assertTrue(objects.isEmpty());

        when(collection.eval(topLevelComponent, component)).thenReturn(new LinkedList<>(List.of("test1", "test2")));
        objects = dynamicSelectModel.getObjects();
        assertNotNull(objects);
        assertEquals(2, objects.size());
        assertTrue(objects.contains("test1"));
        assertTrue(objects.contains("test2"));

        when(collection.eval(topLevelComponent, component)).thenReturn(new Object[] { "test3" });
        dynamicSelectModel.setEmptyObject("testEmpty");
        objects = dynamicSelectModel.getObjects();
        assertNotNull(objects);
        assertEquals(3, objects.size());
        assertTrue(objects.contains("testEmpty"));
        assertTrue(objects.contains("test1"));
        assertTrue(objects.contains("test2"));
    }

    @Test(expected = WheelException.class)
    public void testInvalidGetObjects() {
        when(collection.eval(topLevelComponent, component)).thenReturn(null);
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testTranslateValue() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<>(List.of("test1", "test2")));
        when(value.eval(any(Object.class), eq(component))).thenReturn("testValue");
        when(translator.eval(any(Object.class), eq(component))).thenReturn("translatedValue");

        Object translatedObject = dynamicSelectModel.translateValue("testValue");
        assertEquals("test2", translatedObject);

        translatedObject = dynamicSelectModel.translateValue("test3");
        assertNull(translatedObject);
    }

    @Test(expected = WheelException.class)
    public void testTranslateValueNullCollection() {
        when(collection.eval(topLevelComponent, component)).thenReturn(null);
        dynamicSelectModel.translateValue("testValue");
    }
}