package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareSameNamedAttributes() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentNamedAttributes() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertNotEquals(0, comparisonResult);
    }

    @Test
    public void testCompareNameAAttributeFirst() {
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareNameANameGreaterThanA() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareNameANameLessThanA() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("nameA", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareNameANameEqualA() {
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareNullAttributeAndValidAttribute() {
        AttributeModel attributeModel = new AttributeModel("name", "value");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, attributeModel));
    }
}