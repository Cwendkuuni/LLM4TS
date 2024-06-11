package de.paragon.explorer.model.test;

import org.junit.Test;
import static org.junit.Assert.*;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareSameName() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentNamesIgnoredA() {
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = new AttributeModel("Name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareDifferentNamesAfirst() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("Name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareEmptyName() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("Name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareEmptyName2() {
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = new AttributeModel("");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareEqualEmptyNames() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareLongNames() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeModel1Name");
        AttributeModel attributeModel2 = new AttributeModel("AttributeModel2Name");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareNullNames() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareNullName1() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("Name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareNullName2() {
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareEqualNames() {
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = new AttributeModel("Name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("Name2");
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNull2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = null;
        comparator.compare(attributeModel1, attributeModel2);
    }
}