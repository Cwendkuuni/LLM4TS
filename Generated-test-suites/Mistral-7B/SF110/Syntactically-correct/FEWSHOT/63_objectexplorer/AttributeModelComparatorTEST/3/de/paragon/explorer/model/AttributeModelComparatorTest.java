package de.paragon.explorer.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsNull_thenReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(null, null);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_whenBothObjectsNotNullAndDifferentNames_thenCompareNamesIgnoreCase() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        String expectedComparisonResult = "Name1".compareToIgnoreCase("Name2");
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testCompare_whenBothObjectsNotNullAndSameNameButOneIsBeforeA_thenReturnNegativeNumber() {
        AttributeModel model1 = new AttributeModel("A", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        int expectedComparisonResult = "-1";
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testCompare_whenBothObjectsNotNullAndSameNameButOneIsAfterA_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("A", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        int expectedComparisonResult = "1";
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testCompare_whenFirstObjectIsNull_thenReturnNegativeNumberIfSecondObjectNotNull() {
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(null, model2);
        int expectedComparisonResult = "-1";
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testCompare_whenSecondObjectIsNull_thenReturnPositiveNumberIfFirstObjectNotNull() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, null);
        int expectedComparisonResult = "1";
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testCompare_whenBothObjectsNullAndArrayContainsElementWithNameA_thenReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel[] attributeModels = {new AttributeModel("Name1", "Value1"), new AttributeModel("A", "ValueA")};
        int comparisonResult = comparator.compare(null, null);
        int expectedComparisonResult = "0";
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameA_andSecondObjectHasNameDifferentFromA_thenReturnNegativeNumber() {
        AttributeModel model1 = new AttributeModel("A", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        int expectedComparisonResult = "-1";
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameDifferentFromA_andSecondObjectHasNameA_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("A", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        int expectedComparisonResult = "1";
        assertEquals(expectedComparisonResult, comparisonResult);
    }
}