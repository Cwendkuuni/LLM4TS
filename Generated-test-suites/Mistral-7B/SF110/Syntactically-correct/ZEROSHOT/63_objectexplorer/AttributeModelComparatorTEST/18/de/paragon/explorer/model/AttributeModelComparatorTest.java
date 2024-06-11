package de.paragon.explorer.model.test;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNamesIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        if (comparisonResult < 0) {
            assertTrue(attributeModel1.getName().compareToIgnoreCase("Name1") < attributeModel2.getName().compareToIgnoreCase("Name2"));
        } else if (comparisonResult > 0) {
            assertTrue(attributeModel1.getName().compareToIgnoreCase("Name1") > attributeModel2.getName().compareToIgnoreCase("Name2"));
        } else {
            fail("Unexpected comparison result: " + comparisonResult);
        }
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameA() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        if (comparisonResult < 0) {
            assertTrue(attributeModel1.getName().compareToIgnoreCase("Name1") < "a".compareToIgnoreCase("a"));
        } else {
            assertTrue("a".compareToIgnoreCase("Name1") < 0);
            assertTrue(comparisonResult > 0);
        }
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameAAndNameAInFirst() {
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        comparator.compare(null, new AttributeModel("Name", "Value"));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareWithNullAttributeModel2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        comparator.compare(new AttributeModel("Name", "Value"), null);
    }
}