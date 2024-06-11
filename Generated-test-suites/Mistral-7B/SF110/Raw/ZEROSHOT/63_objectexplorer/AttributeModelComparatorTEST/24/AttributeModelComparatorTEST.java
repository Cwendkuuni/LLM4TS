package de.paragon.explorer.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import de.paragon.explorer.model.*;

import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNull_thenReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveNullNames_thenReturnZero() {
        AttributeModel model1 = new AttributeModel();
        AttributeModel model2 = new AttributeModel();
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveSameName_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveDifferentNames_thenReturnCorrectValue() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsLessThanSecondObjectName_thenReturnNegativeValue() {
        AttributeModel model1 = new AttributeModel("NameA");
        AttributeModel model2 = new AttributeModel("NameB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsGreaterThanSecondObjectName_thenReturnPositiveValue() {
        AttributeModel model1 = new AttributeModel("NameB");
        AttributeModel model2 = new AttributeModel("NameA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsEqualToSpecialValue_thenReturnPositiveValueIfSecondObjectNameIsLessThanSpecialValue() {
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("NameB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsEqualToSpecialValue_thenReturnNegativeValueIfSecondObjectNameIsGreaterThanSpecialValue() {
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("NameA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsEqualToSpecialValueAndSecondObjectNameIsEqualToSpecialValue_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("a");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }
}