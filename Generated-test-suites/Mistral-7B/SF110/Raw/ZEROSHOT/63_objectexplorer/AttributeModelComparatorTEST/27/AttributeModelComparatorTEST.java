package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNull_shouldReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsAreValidWithDifferentNames_shouldReturnNegativeOrPositiveValueBasedOnComparison() {
        AttributeModel model1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel model2 = new AttributeModel("Attribute2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        String expectedName1 = model1.getName();
        String expectedName2 = model2.getName();

        if (result < 0) {
            assertTrue(expectedName1.compareToIgnoreCase(expectedName2) < 0);
        } else if (result > 0) {
            assertTrue(expectedName1.compareToIgnoreCase(expectedName2) > 0);
        } else {
            fail("Expected comparison result to be neither zero nor null.");
        }
    }

    @Test
    public void testCompare_whenBothObjectsHaveTheSameName_shouldReturnZero() {
        AttributeModel model1 = new AttributeModel("AttributeName", "Value1");
        AttributeModel model2 = new AttributeModel("AttributeName", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenFirstObjectIsNullAndSecondIsValid_shouldReturnPositiveValue() {
        AttributeModel model2 = new AttributeModel("AttributeName", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(null, model2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_whenFirstObjectIsValidAndSecondIsNull_shouldReturnNegativeValue() {
        AttributeModel model1 = new AttributeModel("AttributeName", "Value1");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, null);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsSmallerThanSecondObjectName_shouldReturnNegativeValue() {
        AttributeModel model1 = new AttributeModel("AttributeName1", "Value1");
        AttributeModel model2 = new AttributeModel("AttributeName2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        String expectedName1 = model1.getName();
        String expectedName2 = model2.getName();

        assertTrue(expectedName1.compareToIgnoreCase(expectedName2) < 0);
        assertEquals(result, -1);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsGreaterThanSecondObjectName_shouldReturnPositiveValue() {
        AttributeModel model1 = new AttributeModel("AttributeName2", "Value1");
        AttributeModel model2 = new AttributeModel("AttributeName1", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        String expectedName1 = model1.getName();
        String expectedName2 = model2.getName();

        assertTrue(expectedName1.compareToIgnoreCase(expectedName2) > 0);
        assertEquals(result, 1);
    }

    @Test
    public void testCompare_whenBothObjectsHaveNameAsStringA_shouldReturnObjectWithLargerIndexInListAsGreaterValue() {
        AttributeModel model1 = new AttributeModel("A", "Value1");
        AttributeModel model2 = new AttributeModel("A", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        List<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(model1);
        attributeModels.add(model2);
        Collections.sort(attributeModels, comparator);

        AttributeModel expectedModel1 = attributeModels.get(0);
        AttributeModel expectedModel2 = attributeModels.get(1);
        String expectedName1 = expectedModel1.getName();
        String expectedName2 = expectedModel2.getName();

        assertTrue(expectedName1.compareToIgnoreCase(expectedName2) < 0);
        assertEquals(expectedModel1.hashCode(), comparator.compare(expectedModel1, expectedModel2));
    }

    @Test
    public void testCompareToNull_whenComparatorIsCalledWithNullObject_shouldReturnNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(new AttributeModel("AttributeName", "Value"), null);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareToSelf_whenComparatorIsCalledWithSameObjectTwice_shouldReturnZero() {
        AttributeModel model = new AttributeModel("AttributeName", "Value");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model, model);
        assertEquals(0, result);
    }
}