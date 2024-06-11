package de.paragon.explorer.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsNull_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenFirstObjectNull_returnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name1", "value1");
        int result = comparator.compare(null, model1);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenSecondObjectNull_returnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model2 = new AttributeModel("name2", "value2");
        int result = comparator.compare(model2, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveTheSameName_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenFirstObjectNameLessThanSecondObjectName_returnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenFirstObjectNameGreaterThanSecondObjectName_returnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name2", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        int result = comparator.compare(model1, model2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_whenFirstObjectNameEqualsSpecialCaseA_andSecondObjectNameGreaterThanA_returnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("b", "value2");
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenFirstObjectNameEqualsSpecialCaseA_andSecondObjectNameLessThanA_returnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("z", "value2");
        int result = comparator.compare(model1, model2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_whenFirstObjectNameEqualsSecondObjectName_butFirstObjectNameIsSpecialCaseA_andSecondObjectNameIsNot_returnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenFirstObjectNameEqualsSecondObjectName_andBothAreSpecialCaseA_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }
}