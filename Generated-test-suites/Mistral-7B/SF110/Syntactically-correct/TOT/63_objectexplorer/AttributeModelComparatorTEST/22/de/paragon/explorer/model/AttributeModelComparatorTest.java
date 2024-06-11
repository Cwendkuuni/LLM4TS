package de.paragon.explorer.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel o1 = new AttributeModel("attribute1");
        AttributeModel o2 = new AttributeModel("Attribute1");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);

        AttributeModel o3 = new AttributeModel("attributeB");
        AttributeModel o4 = new AttributeModel("attributeA");
        result = comparator.compare(o3, o4);
        assertEquals(-1, result);

        result = comparator.compare(o4, o3);
        assertEquals(1, result);
    }

    // Test case for compare method when both objects are null
    @Test
    public void testCompareNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = null;

        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    // Test case for compare method when both objects have the same name
    @Test
    public void testCompareSameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("attribute1");
        AttributeModel o2 = new AttributeModel("attribute1");

        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    // Test case for compare method when o1 name is less than o2 name
    @Test
    public void testCompareLessName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("attributeA");
        AttributeModel o2 = new AttributeModel("attributeB");

        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    // Test case for compare method when o1 name is greater than o2 name
    @Test
    public void testCompareGreaterName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("attributeC");
        AttributeModel o2 = new AttributeModel("attributeA");

        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    // Test case for compare method when one object name is "a" and the other is not
    @Test
    public void testCompareNameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("attributeX");
        AttributeModel o2 = new AttributeModel("a");

        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    // Test case for compare method when one object name is "a" and the other is also "a"
    @Test
    public void testCompareNameAA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("a");

        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    // Test case for compare method when o1 name is less than o2 name, but o1 name is "a"
    @Test
    public void testCompareLessNameWithA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("attributeX");
        AttributeModel o2 = new AttributeModel("a");

        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    // Test case for compare method when o1 name is greater than o2 name, but o2 name is "a"
    @Test
    public void testCompareGreaterNameWithA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("attributeX");

        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
}