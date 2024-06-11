package de.paragon.explorer.model;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AttributeModelComparatorTest {

    private AttributeModel attributeModel1;
    private AttributeModel attributeModel2;
    private AttributeModel attributeModel3;
    private AttributeModel attributeModel4;

    @Before
    public void setUp() {
        attributeModel1 = new AttributeModel("Name1");
        attributeModel2 = new AttributeModel("Name2");
        attributeModel3 = new AttributeModel("Name3");
        attributeModel4 = new AttributeModel("a");
    }

    @Test
    public void testCompareNullAttributeModels() {
        assertNull(AttributeModelComparator.getInstance().compare(null, null));
        assertNull(AttributeModelComparator.getInstance().compare(null, attributeModel1));
        assertNull(AttributeModelComparator.getInstance().compare(attributeModel1, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentNamesCaseInsensitive() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel2, attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareSameNameCaseInsensitiveFirstLessThanA() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel4);
        assertEquals(-1, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel4, attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareSameNameCaseInsensitiveFirstGreaterThanA() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel4, attributeModel1);
        assertEquals(-1, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel4);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareSameNameCaseInsensitiveBothEqualA() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel4, attributeModel4);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareNullAttributeModelAndNonNull() {
        assertNull(AttributeModelComparator.getInstance().compare(null, attributeModel1));
        assertNotNull(AttributeModelComparator.getInstance().compare(attributeModel1, null));
    }

    @Test
    public void testCompareNullNameAttributeModel1() {
        attributeModel1.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompareNullNameAttributeModel2() {
        attributeModel2.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompareNullNameBoth() {
        attributeModel1.setName(null);
        attributeModel2.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompareSameNameNullBoth() {
        attributeModel1.setName(null);
        attributeModel2.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentNamesNullAttributeModel1() {
        attributeModel1.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel2, attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareDifferentNamesNullAttributeModel2() {
        attributeModel2.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel2, attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareDifferentNamesNullBoth() {
        attributeModel1.setName(null);
        attributeModel2.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompareSameNameNullAndNonNull() {
        attributeModel1.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertNull(comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel2, attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareSameNameNonNullAndNull() {
        attributeModel2.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel2, attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareSameNameBothNull() {
        attributeModel1.setName(null);
        attributeModel2.setName(null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentNames() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel3);
        assertEquals(-1, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel3, attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareEqualNamesDifferentAttributeModels() {
        AttributeModel attributeModel5 = new AttributeModel("Name1");
        AttributeModel attributeModel6 = new AttributeModel("Name1");

        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel5);
        assertEquals(0, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel5, attributeModel1);
        assertEquals(0, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel6);
        assertEquals(0, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel6, attributeModel1);
        assertEquals(0, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel5, attributeModel6);
        assertNotSame(attributeModel5, attributeModel6);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareEqualNamesSameAttributeModels() {
        AttributeModel attributeModel5 = attributeModel1;

        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel5);
        assertEquals(0, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel5, attributeModel1);
        assertEquals(0, comparisonResult);
    }
}