package de.paragon.explorer.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare_whenBothObjectsAreNotNullAndHaveDifferentNamesIgnoreCase_thenReturnValueLessThanZero() {
        AttributeModel attributeModel1 = new AttributeModel("firstName");
        AttributeModel attributeModel2 = new AttributeModel("lastName");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testCompare_whenBothObjectsAreNotNullAndHaveTheSameNameIgnoreCase_thenReturnZero() {
        AttributeModel attributeModel1 = new AttributeModel("test");
        AttributeModel attributeModel2 = new AttributeModel("test");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void testCompare_whenOneObjectIsNull_thenReturnNegativeOne() {
        AttributeModel attributeModel1 = new AttributeModel("test");
        AttributeModel attributeModel2 = null;

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testCompare_whenOneObjectIsNotNullAndTheOtherIsNull_thenReturnPositiveOne() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("test");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testCompare_whenBothObjectsAreNull_thenReturnZero() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_whenFirstObjectIsNullAndSecondObjectIsNull_thenThrowNullPointerException() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_whenFirstObjectIsNull_thenThrowNullPointerException() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("test");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_whenSecondObjectIsNull_thenThrowNullPointerException() {
        AttributeModel attributeModel1 = new AttributeModel("test");
        AttributeModel attributeModel2 = null;

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test
    public void testCompare_whenBothObjectsHaveSameNameAndCaseSensitive_thenReturnZero() {
        AttributeModel attributeModel1 = new AttributeModel("test");
        AttributeModel attributeModel2 = new AttributeModel("TEST");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsLessThanSecondObjectName_thenReturnNegativeValue() {
        AttributeModel attributeModel1 = new AttributeModel("A");
        AttributeModel attributeModel2 = new AttributeModel("B");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testCompare_whenFirstObjectNameIsGreaterThanSecondObjectName_thenReturnPositiveValue() {
        AttributeModel attributeModel1 = new AttributeModel("Z");
        AttributeModel attributeModel2 = new AttributeModel("A");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int actual = comparator.compare(attributeModel1, attributeModel2);

        int expected = 1;
        assertEquals(expected, actual);
    }
}