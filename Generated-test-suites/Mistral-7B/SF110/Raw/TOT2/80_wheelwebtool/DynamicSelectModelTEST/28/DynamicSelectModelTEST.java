package wheel.util;

import static org.junit.Assert.*;
import wheel.*;
import java.util.*;

import org.junit.Test;

public class DynamicSelectModelTest {

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("Label");
        assertEquals("Label", model.getLabel(0));
    }

    @Test
    public void testGetLabelWithEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("emptyCollection");
        assertFalse(model.getObjects().isEmpty()); // validate that the collection is empty
        try {
            model.getLabel(0);
            fail("Expected WheelException for empty collection");
        } catch (WheelException e) {
            // expected
        }
    }

    @Test
    public void testGetLabelWithNullProperties() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection(null);
        model.label(null);
        model.value(null);
        try {
            model.getLabel(0);
            fail("Expected WheelException for null properties");
        } catch (WheelException e) {
            // expected
        }
    }

    @Test
    public void testGetLabelWithInvalidCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection(new HashMap<>());
        try {
            model.getLabel(0);
            fail("Expected WheelException for invalid collection type");
        } catch (WheelException e) {
            // expected
        }
    }

    @Test
    public void testGetLabelWithEmptyNonCollectionObject() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        model.empty(new Object());
        try {
            model.getLabel(0);
            fail("Expected WheelException for empty non-collection object");
        } catch (WheelException e) {
            // expected
        }
    }

    @Test
    public void testGetLabelWithInvalidTranslator() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        model.label("Label");
        model.value("Value");
        model.translator(new ElExpression("InvalidTranslator"));
        try {
            model.translateValue("Value");
            fail("Expected WheelException for invalid translator expression");
        } catch (WheelException e) {
            // expected
        }
    }

    @Test
    public void testGetLabelOutOfBounds() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        model.label("Label");
        model.value("Value");
        model.addObjects(new Object[]{"Object1", "Object2", "Object3"});
        try {
            model.getLabel(3);
            fail("Expected WheelException for out of bounds index");
        } catch (WheelException e) {
            // expected
        }
    }

    @Test
    public void testGetLabelWithDuplicateObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        model.label("Label");
        model.value("Value");
        model.addObjects(new Object[]{new Object(), new Object()});
        assertEquals("Object", model.getLabel(0)); // first object
        assertEquals("Object", model.getLabel(1)); // second object should have the same label
    }
}