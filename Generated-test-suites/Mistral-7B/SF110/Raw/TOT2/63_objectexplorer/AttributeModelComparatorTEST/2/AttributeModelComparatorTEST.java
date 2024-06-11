package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.*;

public class AttributeModelComparatorTest {

    private static AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompare() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute2");
        AttributeModel attributeModel3 = new AttributeModel("a");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(attributeModel2, attributeModel1);
        assertEquals(1, comparisonResult);

        comparisonResult = comparator.compare(attributeModel1, attributeModel1);
        assertEquals(0, comparisonResult);

        comparisonResult = comparator.compare(attributeModel3, attributeModel1);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(attributeModel1, attributeModel3);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareWithNullObjects() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("Attribute2");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompareWithNullAttributeModel1() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("Attribute2");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult); // As per the code behavior
    }

    @Test
    public void testCompareWithNullAttributeModel2() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = null;

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareWithSameNames() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("a");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareWithNullComparators() {
        AttributeModelComparator comparator = null;
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute2");

        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test
    public void testCompareWithNullAttributeModel1AndNullAttributeModel2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompareWithDifferentNamesCaseInsensitive() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = new AttributeModel("ATTRIBUTE2");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareWithDifferentNamesAndNegativeValue() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute3");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareWithDifferentNamesAndPositiveValue() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute2");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareWithAttributesHavingSameNameButDifferentValues() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeName");
        AttributeModel attributeModel2 = new AttributeModel("AttributeName_Value2");

        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }
}