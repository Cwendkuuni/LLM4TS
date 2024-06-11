package de.paragon.explorer.model.test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareMethod() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name2");

        int result = new AttributeModelComparator().compare(model1, model2);

        // Check if compare method returns expected value based on names comparison
        assertEquals(-1, result);
    }

    @Test
    public void testCompareNullObjects() {
        int result = new AttributeModelComparator().compare(null, null);

        // Check if compare method returns 0 when both objects are null
        assertEquals(0, result);
    }

    @Test
    public void testCompareNullObjectAndNonNullObject() {
        AttributeModel model1 = new AttributeModel("Name1");
        int result = new AttributeModelComparator().compare(model1, null);

        // Check if compare method returns expected value (1 or -1) when one object is null
        assertEquals(1, result);
    }

    @Test
    public void testCompareSameObjects() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name1");

        int result = new AttributeModelComparator().compare(model1, model2);

        // Check if compare method returns 0 when both objects have the same name
        assertEquals(0, result);
    }

    @Test
    public void testCompareAAndNonA() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("a");

        int result = new AttributeModelComparator().compare(model1, model2);

        // Check if compare method returns expected value when one object's name is "a" and the other's name is not "a"
        assertEquals(1, result);
    }

    @Test
    public void testCompareAAndA() {
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("a");

        int result = new AttributeModelComparator().compare(model1, model2);

        // Check if compare method swaps the position of objects when names are equal but their positions should be swapped
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullNameObject() {
        AttributeModel model1 = new AttributeModel(null);
        AttributeModel model2 = new AttributeModel("Name2");

        int result = new AttributeModelComparator().compare(model1, model2);

        // Check if compare method throws NullPointerException when one object's name is null
        fail();
    }
}