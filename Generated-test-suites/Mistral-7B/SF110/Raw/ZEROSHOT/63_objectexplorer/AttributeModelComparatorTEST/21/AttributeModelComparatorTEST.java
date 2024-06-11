package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

import de.paragon.explorer.model.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_WhenBothObjectsNull_ThenReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_WhenBothObjectsNotNullAndHaveDifferentNames_ThenReturnNegativeOrPositiveValue() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompare_WhenBothObjectsNotNullAndHaveSameName_ThenReturnNegativeOrZeroValue() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        if (model1.getName().compareToIgnoreCase("a") < 0) {
            assertTrue(result < 0);
        } else if (model2.getName().compareToIgnoreCase("a") < 0) {
            assertTrue(result > 0);
        } else {
            assertEquals(0, result);
        }
    }

    @Test
    public void testCompare_WhenFirstObjectIsNull_ThenReturnNegativeValue() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_WhenFirstObjectIsNotNullAndSecondObjectIsNotNullButHaveTheSameName_ThenReturnZero() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_WhenFirstObjectIsNotNullAndSecondObjectIsNotNullAndNameOfFirstObjectIsASmallerStringIgnoreCase_ThenReturnNegativeValue() {
        AttributeModel model1 = new AttributeModel("nameA", "value1");
        AttributeModel model2 = new AttributeModel("nameB", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_WhenFirstObjectIsNotNullAndSecondObjectIsNotNullAndNameOfFirstObjectIsALargerStringIgnoreCase_ThenReturnPositiveValue() {
        AttributeModel model1 = new AttributeModel("nameZ", "value1");
        AttributeModel model2 = new AttributeModel("nameA", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_WhenFirstObjectIsNullAndSecondObjectIsNotNull_ThenThrowNullPointerException() {
        AttributeModel model2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        comparator.compare(null, model2);
    }
}