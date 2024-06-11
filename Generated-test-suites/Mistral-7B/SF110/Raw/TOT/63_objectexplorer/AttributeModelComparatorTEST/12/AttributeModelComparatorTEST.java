package de.paragon.explorer.model.test;

import org.junit.Test;
import static org.junit.Assert.*;

import de.paragon.explorer.model.*;
import de.paragon.explorer.model.comparator.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("A");
        AttributeModel model2 = new AttributeModel("B");
        AttributeModel model3 = new AttributeModel("A");
        AttributeModel model4 = new AttributeModel(null);

        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, result);

        result = new AttributeModelComparator().compare(model2, model1);
        assertEquals(1, result);

        result = new AttributeModelComparator().compare(model1, model1);
        assertEquals(0, result);

        result = new AttributeModelComparator().compare(model3, model2);
        assertEquals(0, result);

        result = new AttributeModelComparator().compare(model2, model4);
        assertNull(result);

        result = new AttributeModelComparator().compare(model4, model3);
        assertNull(result);

        result = new AttributeModelComparator().compare(model4, model1);
        assertNull(result);

        result = new AttributeModelComparator().compare(model1, model4);
        assertNull(result);
    }

    @Test
    public void testCompareIgnoreCase() {
        AttributeModel model1 = new AttributeModel("AttributeA");
        AttributeModel model2 = new AttributeModel("attributeA");
        AttributeModel model3 = new AttributeModel("AttributeB");

        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(0, result);

        result = new AttributeModelComparator().compare(model2, model1);
        assertEquals(0, result);

        result = new AttributeModelComparator().compare(model1, model3);
        assertEquals(-1, result);

        result = new AttributeModelComparator().compare(model3, model1);
        assertEquals(1, result);
    }

    @Test
    public void testCompareWithNullObjects() {
        AttributeModel model1 = new AttributeModel("AttributeA");
        AttributeModel model2 = null;

        int result = new AttributeModelComparator().compare(model1, model2);
        assertNull(result);

        result = new AttributeModelComparator().compare(model2, model1);
        assertNull(result);

        model2 = new AttributeModel("AttributeB");
        result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, result);

        result = new AttributeModelComparator().compare(model2, model1);
        assertEquals(1, result);
    }
}