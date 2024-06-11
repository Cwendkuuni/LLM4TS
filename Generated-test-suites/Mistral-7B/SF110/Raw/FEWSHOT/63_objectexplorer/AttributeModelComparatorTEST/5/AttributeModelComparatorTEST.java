package de.paragon.explorer.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNull_thenReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveTheSameName_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameLessThanSecondObject_thenReturnNegativeNumber() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertNotNull(result);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameGreaterThanSecondObject_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("name2", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertNotNull(result);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameEqualToASpecialValueAndSecondObjectDoesNot_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertNotNull(result);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_whenSecondObjectHasNameEqualToASpecialValueAndFirstObjectDoesNot_thenReturnNegativeNumber() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertNotNull(result);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameGreaterThanASpecialValueAndSecondObjectLessThanASpecialValue_thenReturnNegativeNumber() {
        AttributeModel model1 = new AttributeModel("name2", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertNotNull(result);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameLessThanASpecialValueAndSecondObjectGreaterThanASpecialValue_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertNotNull(result);
        assertTrue(result > 0);
    }
}