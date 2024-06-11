package de.paragon.explorer.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_sameName_returnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("attributeName1");
        AttributeModel attributeModel2 = new AttributeModel("attributeName1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_differentNames_returnsNegativeOrPositive() {
        AttributeModel attributeModel1 = new AttributeModel("attributeName1");
        AttributeModel attributeModel2 = new AttributeModel("attributeName2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertNotNull(result);
        if (result < 0) {
            // The names are compared in a case-insensitive manner,
            // so the result should be negative if the first name comes before the second name
            assertTrue(attributeModel1.getName().compareToIgnoreCase("attributeName1") < attributeModel2.getName().compareToIgnoreCase("attributeName2"));
        } else {
            // The result should be positive if the first name comes after the second name
            assertTrue(attributeModel1.getName().compareToIgnoreCase("attributeName1") > attributeModel2.getName().compareToIgnoreCase("attributeName2"));
        }
    }

    @Test
    public void testCompare_nullObjects_throwsNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        try {
            comparator.compare(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testCompare_nullObject_and_nonNullObject_returnsNegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("attributeName1");
        AttributeModel attributeModel2 = null;
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_nullAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("attributeName1");
        AttributeModel attributeModel2 = null;
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test
    public void testCompare_emptyString_and_nonEmptyString() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("attributeName2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_nonEmptyString_and_emptyString() {
        AttributeModel attributeModel1 = new AttributeModel("attributeName1");
        AttributeModel attributeModel2 = new AttributeModel("");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_nullComparator() {
        AttributeModel attributeModel1 = new AttributeModel("attributeName1");
        AttributeModel attributeModel2 = new AttributeModel("attributeName2");
        try {
            Comparator<AttributeModel> comparator = null;
            comparator.compare(attributeModel1, attributeModel2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testCompare_twoNullComparators() {
        try {
            Comparator<AttributeModel> comparator1 = null;
            Comparator<AttributeModel> comparator2 = null;
            comparator1.compare(new AttributeModel("attributeName1"), new AttributeModel("attributeName2"));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}