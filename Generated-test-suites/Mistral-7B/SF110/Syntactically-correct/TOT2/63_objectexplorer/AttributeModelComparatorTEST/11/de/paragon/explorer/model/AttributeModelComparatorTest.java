package de.paragon.explorer.model;
import org.junit.Assert;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_sameNameIgnoreCase_returnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("attributeA");
        AttributeModel attributeModel2 = new AttributeModel("attributeA");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_differentNameIgnoreCase_returnsValueOfCompareTo() {
        AttributeModel attributeModel1 = new AttributeModel("attributeA");
        AttributeModel attributeModel2 = new AttributeModel("attributeB");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_nullObject_throwsNullPointerException() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("attribute2");
        try {
            new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCompare_nullObject2_throwsNullPointerException() {
        AttributeModel attributeModel1 = new AttributeModel("attribute1");
        AttributeModel attributeModel2 = null;
        try {
            new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCompare_nameEqualsA_returnsValueBasedOnOrder() {
        AttributeModel attributeModel1 = new AttributeModel("attribute1");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_nameEqualsA_returnsValueBasedOnOrderReversed() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("attribute1");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_nameEqualsAAndBothAreA_returnsValueBasedOnOrder() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_nameIsEmpty_returnsNegativeValue() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("attributeB");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_nullObjectAndNullObject_returnsZero() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_sameNameIgnoreCaseAndNullObject_returnsValueOfCompareTo() {
        AttributeModel attributeModel1 = new AttributeModel("attributeA");
        AttributeModel attributeModel2 = null;
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_sameNameIgnoreCaseAndNullObjectReverse_returnsValueOfCompareTo() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("attributeA");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_differentNameIgnoreCaseAndNullObject_throwsNullPointerException() {
        AttributeModel attributeModel1 = new AttributeModel("attributeA");
        AttributeModel attributeModel2 = null;
        try {
            new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCompare_differentNameIgnoreCaseAndNullObjectReverse_throwsNullPointerException() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("attributeA");
        try {
            new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCompare_emptyStringsAreEqual() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_emptyStringAndNonEmptyStringIgnoreCase_returnsNegativeValue() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("attributeB");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_emptyStringAndNonEmptyStringIgnoreCaseReversed_returnsPositiveValue() {
        AttributeModel attributeModel1 = new AttributeModel("attributeB");
        AttributeModel attributeModel2 = new AttributeModel("");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_nonEmptyStringsAreEqualIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("attributeA");
        AttributeModel attributeModel2 = new AttributeModel("AttributeA");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_nonEmptyStringsAreEqualIgnoreCaseReversed() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeA");
        AttributeModel attributeModel2 = new AttributeModel("attributeA");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_nonEmptyStringsAreNotEqualIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("attributeA");
        AttributeModel attributeModel2 = new AttributeModel("attributeB");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_nonEmptyStringsAreNotEqualIgnoreCaseReversed() {
        AttributeModel attributeModel1 = new AttributeModel("attributeB");
        AttributeModel attributeModel2 = new AttributeModel("attributeA");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }
}