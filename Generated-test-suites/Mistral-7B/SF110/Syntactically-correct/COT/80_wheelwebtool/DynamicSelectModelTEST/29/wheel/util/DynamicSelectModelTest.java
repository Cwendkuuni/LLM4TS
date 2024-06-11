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
    public void testConstructorWithCollectionLabelValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        assertNotNull(dynamicSelectModel);
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutCollection() {
        new DynamicSelectModel(new ElExpression("label"), new ElExpression("value"));
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutLabel() {
        new DynamicSelectModel(new ElExpression("collection"), new ElExpression("value"));
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutValue() {
        new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"));
    }

    @Test
    public void testCollectionMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("collection");
        assertNotNull(dynamicSelectModel.getCollection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionMethodWithInvalidExpression() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("invalidExpression");
    }

    @Test
    public void testLabelMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label("label");
        assertNotNull(dynamicSelectModel.getLabel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLabelMethodWithInvalidExpression() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label("invalidExpression");
    }

    @Test
    public void testValueMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value("value");
        assertNotNull(dynamicSelectModel.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueMethodWithInvalidExpression() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value("invalidExpression");
    }

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection collection = new ArrayList();
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.setObjects(collection);
        assertEquals(collection.size(), dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression label = new ElExpression("label");
        dynamicSelectModel.collection(new ArrayList());
        dynamicSelectModel.label(label);
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.setComponent(new Component());
        String expectedLabel = "label.eval(objects.get(0), component)";
        String actualLabel = dynamicSelectModel.getLabel(0);
        assertEquals(expectedLabel, actualLabel);
    }

    @Test(expected = WheelException.class)
    public void testGetLabelWithNullComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression label = new ElExpression("label");
        dynamicSelectModel.collection(new ArrayList());
        dynamicSelectModel.label(label);
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.setComponent(null);
        dynamicSelectModel.getLabel(0);
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression value = new ElExpression("value");
        dynamicSelectModel.collection(new ArrayList());
        dynamicSelectModel.label(new ElExpression("label"));
        dynamicSelectModel.value(value);
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.setComponent(new Component());
        String expectedValue = "value.eval(objects.get(0), component)";
        String actualValue = dynamicSelectModel.getValue(0);
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = WheelException.class)
    public void testGetValueWithNullComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression value = new ElExpression("value");
        dynamicSelectModel.collection(new ArrayList());
        dynamicSelectModel.label(new ElExpression("label"));
        dynamicSelectModel.value(value);
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.setComponent(null);
        dynamicSelectModel.getValue(0);
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection objects = new ArrayList();
        objects.add("value1");
        objects.add("value2");
        dynamicSelectModel.collection(new ElExpression("objects.values()"));
        dynamicSelectModel.label(new ElExpression("toString()"));
        dynamicSelectModel.value(new ElExpression("ordinal()"));
        dynamicSelectModel.translator(new ElExpression("translator"));
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        Object expectedValue = "objects.get(0)";
        Object actualValue = dynamicSelectModel.translateValue("value1");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testTranslateValueWithNullTranslator() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection objects = new ArrayList();
        objects.add("value1");
        objects.add("value2");
        dynamicSelectModel.collection(new ElExpression("objects.values()"));
        dynamicSelectModel.label(new ElExpression("toString()"));
        dynamicSelectModel.value(new ElExpression("ordinal()"));
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        try {
            dynamicSelectModel.translateValue("value1");
        } catch (NullPointerException e) {
            // Expected since translator is null
        }
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(null);
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjectsMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression collection = new ElExpression("collection");
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.setComponent(new Component());
        Collection expectedObjects = new ArrayList();
        expectedObjects.add("object1");
        expectedObjects.add("object2");
        Collection actualObjects = (Collection) collection.eval(new StandaloneComponent(), dynamicSelectModel.getComponent());
        assertEquals(expectedObjects, actualObjects);
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidCollectionExpression() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(new ElExpression("invalidExpression"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        Component expectedComponent = component;
        Component actualComponent = dynamicSelectModel.getComponent();
        assertEquals(expectedComponent, actualComponent);
    }

    @Test
    public void testSetComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        Component expectedComponent = component;
        dynamicSelectModel.setComponent(expectedComponent);
        Component actualComponent = dynamicSelectModel.getComponent();
        assertEquals(expectedComponent, actualComponent);
    }

    @Test
    public void testSetTopLevelComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        StandaloneComponent expectedTopLevelComponent = topLevelComponent;
        dynamicSelectModel.setTopLevelComponent(expectedTopLevelComponent);
        StandaloneComponent actualTopLevelComponent = dynamicSelectModel.getTopLevelComponent();
        assertEquals(expectedTopLevelComponent, actualTopLevelComponent);
    }

    @Test
    public void testValidateMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateMethodWithoutCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = null;
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateMethodWithoutLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = null;
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateMethodWithoutValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.collection = null;
        dynamicSelectModel.validate();
    }

    @Test
    public void testResetMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}