package de.paragon.explorer.model.test;

import org.junit.Test;
import static org.junit.Assert.*;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareMethod() {
        AttributeModel model1 = new AttributeModel("attribute1");
        AttributeModel model2 = new AttributeModel("attribute2");
        AttributeModel model3 = new AttributeModel("attributeA");

        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, result);

        result = new AttributeModelComparator().compare(model1, model3);
        assertEquals(1, result);

        result = new AttributeModelComparator().compare(model3, model1);
        assertEquals(-1, result);

        result = new AttributeModelComparator().compare(model2, model2);
        assertEquals(0, result);

        result = new AttributeModelComparator().compare(null, model1);
        assertNull(result);

        result = new AttributeModelComparator().compare(model1, null);
        assertNull(result);
    }

    @Test
    public void testCompareSameName() {
        AttributeModel model1 = new AttributeModel("attribute1");
        AttributeModel model2 = new AttributeModel("attribute1");

        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullAttributeModel() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("attribute2");

        new AttributeModelComparator().compare(model1, model2);
    }
}