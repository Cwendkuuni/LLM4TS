package de.paragon.explorer.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareSameObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("name1", "value1");
        AttributeModel obj2 = new AttributeModel("name1", "value1");
        assertEquals(0, comparator.compare(obj1, obj2));
    }

    @Test
    public void testCompareDifferentObjectsWithSameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("name1", "value1");
        AttributeModel obj2 = new AttributeModel("name2", "value2");
        assertEquals(0, comparator.compare(obj1, obj2));
    }

    @Test
    public void testCompareObjectsWithDifferentNamesInDifferentOrders() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("name2", "value1");
        AttributeModel obj2 = new AttributeModel("name1", "value1");
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }

    @Test
    public void testCompareObjectsWithSameNameButDifferentValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("name1", "value1");
        AttributeModel obj2 = new AttributeModel("name1", "value2");
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }

    @Test
    public void testCompareObjectsWithNameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("nameA", "value1");
        AttributeModel obj2 = new AttributeModel("nameB", "value1");
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }

    @Test
    public void testCompareObjectsWithNameAA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("nameAA", "value1");
        AttributeModel obj2 = new AttributeModel("nameA", "value1");
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }

    @Test
    public void testCompareObjectsWithNameA_AndNameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("nameA", "value1");
        AttributeModel obj2 = new AttributeModel("nameA", "value2");
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }

    @Test
    public void testCompareObjectsWithNameAAndNameB() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("nameA", "value1");
        AttributeModel obj2 = new AttributeModel("nameB", "value1");
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }

    @Test
    public void testCompareObjectsWithNameA_AndNameC() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("nameA", "value1");
        AttributeModel obj2 = new AttributeModel("nameC", "value1");
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }

    @Test
    public void testCompareObjectsWithNameA_AndNameNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("nameA", "value1");
        AttributeModel obj2 = null;
        assertEquals(-1, comparator.compare(obj1, obj2));
        assertEquals(1, comparator.compare(obj2, obj1));
    }
}