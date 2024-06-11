package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    private Component component;
    private StandaloneComponent topLevelComponent;
    private Collection objects;
    private DynamicSelectModel model;

    @Before
    public void setUp() {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        objects = new ArrayList<>();
        model = new DynamicSelectModel();
        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testGetOptionCount() {
        when(collection.eval(topLevelComponent, component)).thenReturn(objects.toArray());
        assertEquals(objects.size(), model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        when(label.eval(objects.toArray()[0], component)).thenReturn("Label 1");
        assertEquals("Label 1", model.getLabel(0));
    }

    @Test
    public void testGetValue() {
        when(value.eval(objects.toArray()[0], component)).thenReturn("Value 1");
        assertEquals("Value 1", model.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        Object translatedValue = new Object() {};
        when(translator.eval(objects.toArray()[0], component)).thenReturn(translatedValue);
        assertSame(translatedValue, model.translateValue("Value 1"));
    }

    @Test
    public void testGetObjects_Collection() {
        when(collection.eval(topLevelComponent, component)).thenReturn(objects.toArray());
        model.collection("collection");
        model.label("label");
        model.value("value");
        Collection expectedObjects = new ArrayList<>();
        expectedObjects.add("Collection element 1");
        expectedObjects.add("Collection element 2");
        when(collection.eval(topLevelComponent, component)).thenReturn(expectedObjects.toArray());
        assertEquals(expectedObjects, model.getObjects());
    }

    @Test
    public void testGetObjects_ObjectArray() {
        Object[] objectArray = new Object[] {"Object 1", "Object 2"};
        when(collection.eval(topLevelComponent, component)).thenReturn(objectArray);
        model.collection("collection");
        model.label("label");
        model.value("value");
        Collection expectedObjects = new ArrayList<>();
        expectedObjects.add("Object 1");
        expectedObjects.add("Object 2");
        assertEquals(expectedObjects, model.getObjects());
    }

    @Test
    public void testGetObjects_LinkedList() {
        List<Object> linkedList = new LinkedList<>();
        linkedList.add("LinkedList element 1");
        linkedList.add("LinkedList element 2");
        when(collection.eval(topLevelComponent, component)).thenReturn(linkedList.toArray());
        model.collection("collection");
        model.label("label");
        model.value("value");
        Collection expectedObjects = new ArrayList<>();
        expectedObjects.add("LinkedList element 1");
        expectedObjects.add("LinkedList element 2");
        assertEquals(expectedObjects, model.getObjects());
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_NullCollection() {
        when(collection.eval(topLevelComponent, component)).thenReturn(null);
        model.collection("collection");
        model.label("label");
        model.value("value");
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_NonCollection() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new Object());
        model.collection("collection");
        model.label("label");
        model.value("value");
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_EmptyCollection() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new Object[0]);
        model.collection("collection");
        model.label("label");
        model.value("value");
    }
}