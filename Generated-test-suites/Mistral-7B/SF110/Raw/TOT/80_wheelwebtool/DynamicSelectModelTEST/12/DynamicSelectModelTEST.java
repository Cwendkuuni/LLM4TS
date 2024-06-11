package wheel.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import wheel.components.*;
import wheel.*;
import java.util.*;

public class DynamicSelectModelTest {
    private DynamicSelectModel model;

    @Before
    public void setUp() {
        this.model = new DynamicSelectModel()
                .collection("new ArrayList<String>()")
                .label("toString()")
                .value("toString()");
    }

    @Test
    public void testGetOptionCount_ValidCollection() {
        this.model.validate();
        final Collection objects = this.model.getObjects();
        assertEquals("Should return the correct number of options in the collection", 3, objects.size());
    }

    @Test(expected = WheelException.class)
    public void testGetOptionCount_EmptyCollection() {
        this.model = new DynamicSelectModel()
                .collection(Collections.emptyList());
        this.model.validate();
        this.model.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetLabel_InvalidIndex() {
        this.model = new DynamicSelectModel()
                .collection(Arrays.asList("Option1", "Option2", "Option3"))
                .label("toString()")
                .value("toString()");

        this.model.validate();
        this.model.setComponent(new Component());

        this.model.label.errorMessage("Could not evaluate expression toString() to create a label for dynamic ISelectModel. Index was -1");
    }

    @Test
    public void testGetLabel_ValidIndex() {
        this.model = new DynamicSelectModel()
                .collection(Arrays.asList("Option1", "Option2", "Option3"))
                .label("toString()")
                .value("toString()");

        this.model.validate();
        this.model.setComponent(new Component());

        assertEquals("Should return the correct label for the given index", "Option1", this.model.getLabel(0));
    }

    @Test(expected = WheelException.class)
    public void testGetValue_InvalidIndex() {
        this.model = new DynamicSelectModel()
                .collection(Arrays.asList("Option1", "Option2", "Option3"))
                .label("toString()")
                .value("toString()");

        this.model.validate();
        this.model.setComponent(new Component());

        this.model.value.errorMessage("Could not evaluate expression toString() to create a value for dynamic ISelectModel. Index was -1");
    }

    @Test
    public void testGetValue_ValidIndex() {
        this.model = new DynamicSelectModel()
                .collection(Arrays.asList("Option1", "Option2", "Option3"))
                .label("toString()")
                .value("toString()");

        this.model.validate();
        this.model.setComponent(new Component());

        assertEquals("Should return the correct value for the given index", "Option1", this.model.getValue(0));
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateValue_NullValue() {
        this.model = new DynamicSelectModel()
                .collection(Arrays.asList("Option1", "Option2", "Option3"))
                .label("toString()")
                .value("null")
                .translator("toString()");

        this.model.validate();
        this.model.setComponent(new Component());

        this.model.translateValue(null);
    }

    @Test
    public void testTranslateValue_ValidValue() {
        this.model = new DynamicSelectModel()
                .collection(Arrays.asList("Option1", "Option2", "Option3"))
                .label("toString()")
                .value("Option1")
                .translator("toString()");

        this.model.validate();
        this.model.setComponent(new Component());

        assertEquals("Should return the correct translated value for the given value", "Option1", this.model.translateValue("Option1"));
    }
}