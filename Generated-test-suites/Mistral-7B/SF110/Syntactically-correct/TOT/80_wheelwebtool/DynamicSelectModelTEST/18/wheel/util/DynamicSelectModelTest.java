package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    @Test
    public void testValidDynamicSelectModel() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("['red', 'green', 'blue']"), new ElExpression("color"), new ElExpression("colorValue"))
                .label("Color")
                .translator("capitalize");

        assertEquals(3, model.getOptionCount());
        assertEquals("Color", model.getLabel(0));
        assertEquals("red", model.getValue(0));
        assertEquals("RED", model.translateValue("red"));
        assertEquals("green", model.getValue(1));
        assertEquals("Green", model.translateValue("green"));
        assertEquals("blue", model.getValue(2));
        assertEquals("BLUE", model.translateValue("blue"));
    }

    @Test
    public void testEmptyDynamicSelectModel() {
        DynamicSelectModel model = new DynamicSelectModel()
                .collection("")
                .label("")
                .value("")
                .empty("null");

        assertEquals(1, model.getOptionCount());
        assertNull(model.getLabel(0));
        assertNull(model.getValue(0));
        assertNull(model.translateValue(""));
    }

    @Test(expected = WheelException.class)
    public void testInvalidCollectionExpression() {
        DynamicSelectModel model = new DynamicSelectModel(null, new ElExpression("label"), new ElExpression("value"));
    }

    @Test(expected = WheelException.class)
    public void testInvalidLabelExpression() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("['red', 'green', 'blue']"), null, new ElExpression("value"));
    }

    @Test(expected = WheelException.class)
    public void testInvalidValueExpression() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("['red', 'green', 'blue']"), new ElExpression("label"), null);
    }

    @Test(expected = WheelException.class)
    public void testInvalidCollectionObject() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("1 + 1"), new ElExpression("label"), new ElExpression("value"));
    }

    @Test(expected = WheelException.class)
    public void testInvalidCollectionArray() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("['red', 'green', 'blue']"), new ElExpression("label"), new ElExpression("value"))
                .empty("null");
        model.setComponent(new Component());

        try {
            model.getObjects();
            fail("Expected WheelException but none was thrown.");
        } catch (WheelException e) {
            // Expected
        }
    }

    @Test
    public void testTranslatorFunction() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("['apple', 'banana', 'cherry']"), new ElExpression("fruit"), new ElExpression("fruit"))
                .label("Fruit")
                .translator("capitalize");

        assertEquals(3, model.getOptionCount());
        assertEquals("Fruit", model.getLabel(0));
        assertEquals("apple", model.getValue(0));
        assertEquals("APPLE", model.translateValue("apple"));
        assertEquals("banana", model.getValue(1));
        assertEquals("BANANA", model.translateValue("banana"));
        assertEquals("cherry", model.getValue(2));
        assertEquals("CHERRY", model.translateValue("cherry"));
    }
}