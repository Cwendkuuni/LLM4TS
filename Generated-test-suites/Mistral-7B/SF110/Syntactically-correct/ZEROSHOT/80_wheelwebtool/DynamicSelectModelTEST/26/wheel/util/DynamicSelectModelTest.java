package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModelCreation() {
        // Test empty constructor
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        assertNull(dynamicSelectModel.getLabel(0));
        assertNull(dynamicSelectModel.getValue(0));
        assertNull(dynamicSelectModel.getObjects());

        // Test constructor with collection, label, and value
        DynamicSelectModel dynamicSelectModel1 = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        assertNotNull(dynamicSelectModel1.getObjects());
        assertEquals("label", dynamicSelectModel1.getLabel(0));
        assertEquals("value", dynamicSelectModel1.getValue(0));
    }

    @Test
    public void testSetters() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.value("value");
        dynamicSelectModel.translator("translator");
        dynamicSelectModel.empty(new Object());

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals("collection", objects.get(0).toString());
        assertEquals("label", dynamicSelectModel.getLabel(0));
        assertEquals("value", dynamicSelectModel.getValue(0));
        assertEquals("translator", dynamicSelectModel.getLabel(1)); // emptyObject is at index 1
    }

    @Test(expected = WheelException.class)
    public void testInvalidCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(null);
    }

    @Test(expected = WheelException.class)
    public void testInvalidLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label(null);
    }

    @Test(expected = WheelException.class)
    public void testInvalidValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value(null);
    }

    @Test
    public void testGettingObjects() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(new ElExpression("collection"));

        Collection objects = dynamicSelectModel.getObjects();
        assertEquals(0, objects.size());

        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        objects = dynamicSelectModel.getObjects();
        assertNotNull(objects);
        assertEquals(0, objects.size());

        dynamicSelectModel.setComponent(new Component());
        objects = dynamicSelectModel.getObjects();
        assertNotNull(objects);
        assertEquals(0, objects.size());

        dynamicSelectModel.collection("list").setValue("[1, 2, 3]");
        objects = dynamicSelectModel.getObjects();
        assertNotNull(objects);
        assertEquals(3, objects.size());

        assertEquals("1", dynamicSelectModel.getLabel(0));
        assertEquals("2", dynamicSelectModel.getLabel(1));
        assertEquals("3", dynamicSelectModel.getLabel(2));

        assertEquals("1", dynamicSelectModel.getValue(0));
        assertEquals("2", dynamicSelectModel.getValue(1));
        assertEquals("3", dynamicSelectModel.getValue(2));
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("enum").setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.label("toString").value("ordinal");
        dynamicSelectModel.translator("translator");

        dynamicSelectModel.translateValue("FIRST");
        assertNotNull(dynamicSelectModel.translator.eval(dynamicSelectModel.getObjects().toArray()[0], dynamicSelectModel.getComponent()));

        dynamicSelectModel.translateValue("SECOND");
        assertNotNull(dynamicSelectModel.translator.eval(dynamicSelectModel.getObjects().toArray()[1], dynamicSelectModel.getComponent()));
    }

    @Test
    public void testEmptyObject() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("enum").setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.label("toString").value("ordinal");
        dynamicSelectModel.translator("translator");
        dynamicSelectModel.empty(new Object());

        dynamicSelectModel.translateValue("EMPTY");
        assertEquals(dynamicSelectModel.emptyObject, dynamicSelectModel.translateValue("EMPTY"));
    }
}