package de.paragon.explorer.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullVersusNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompare_NullVersusNotNull() {
        AttributeModel nullAttributeModel = null;
        AttributeModel notNullAttributeModel = new AttributeModel("name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(nullAttributeModel, notNullAttributeModel);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompare_NotNullVersusNull() {
        AttributeModel notNullAttributeModel = new AttributeModel("name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(notNullAttributeModel, null);
        int expected = 1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_EqualNames() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        int expected = 0;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_DifferentNamesIgnoringCase() {
        AttributeModel attributeModel1 = new AttributeModel("NAME1");
        AttributeModel attributeModel2 = new AttributeModel("name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        int expected = 0; // or any non-zero value if the order is important
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameABeforeNameB() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelB = new AttributeModel("nameB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelA, attributeModelB);
        int expected = -1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameBBeforeNameA() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelB = new AttributeModel("nameB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelB, attributeModelA);
        int expected = 1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameA() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelAA = new AttributeModel("nameAA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelA, attributeModelAA);
        int expected = -1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameB() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelB = new AttributeModel("nameAA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelB, attributeModelA);
        int expected = 1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameACaseInsensitive() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelAA = new AttributeModel("nameAA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelAA, attributeModelA);
        int expected = -1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameBCaseInsensitive() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelB = new AttributeModel("nameAA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelA, attributeModelB);
        int expected = 1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameAWithNameAAssignValue() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelAA = new AttributeModel("nameAA");
        attributeModelAA.setValue("valueA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelAA, attributeModelA);
        int expected = -1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameBWithNameBAssignValue() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelB = new AttributeModel("nameAA");
        attributeModelB.setValue("valueB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelB, attributeModelA);
        int expected = 1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameACaseInsensitiveWithNameAAssignValue() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelAA = new AttributeModel("nameAA");
        attributeModelAA.setValue("valueA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelAA, attributeModelA);
        int expected = -1;
        assertEquals(expected, comparisonResult);
    }

    @Test
    public void testCompare_NameAAfterNameBWithNameBAssignValueCaseInsensitive() {
        AttributeModel attributeModelA = new AttributeModel("nameA");
        AttributeModel attributeModelB = new AttributeModel("nameAA");
        attributeModelB.setValue("valueB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelB, attributeModelA);
        int expected = 1;
        assertEquals(expected, comparisonResult);
    }
}