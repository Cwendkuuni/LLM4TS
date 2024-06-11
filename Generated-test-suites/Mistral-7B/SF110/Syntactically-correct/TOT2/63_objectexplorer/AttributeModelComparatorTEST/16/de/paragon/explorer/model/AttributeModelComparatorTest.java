package de.paragon.explorer.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import de.paragon.explorer.model.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare_whenBothObjectsAreNotNullAndHaveDifferentNames_returnsCorrectValue() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenBothObjectsAreNull_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_whenFirstObjectIsNull_throwsException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel2 = new AttributeModel("name2");
        int result = comparator.compare(null, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_whenSecondObjectIsNull_throwsException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1");
        int result = comparator.compare(attributeModel1, null);
    }

    @Test
    public void testCompare_whenBothObjectsHaveSameName_returnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }
}