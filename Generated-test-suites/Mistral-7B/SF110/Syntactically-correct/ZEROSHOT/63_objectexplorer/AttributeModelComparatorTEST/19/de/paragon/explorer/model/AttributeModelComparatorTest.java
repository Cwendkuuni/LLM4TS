package de.paragon.explorer.model.test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModel attributeModel1;
    private AttributeModel attributeModel2;
    private AttributeModel attributeModelA;
    private AttributeModel comparatorUnderTest;

    @Before
    public void setUp() {
        attributeModel1 = new AttributeModel("attribute1");
        attributeModel2 = new AttributeModel("attribute2");
        attributeModelA = new AttributeModel("a");

        comparatorUnderTest = new AttributeModelComparator();
    }

    @Test
    public void testCompareNullAttributeModels() {
        assertNull(comparatorUnderTest.compare(null, null));
        assertNull(comparatorUnderTest.compare(null, attributeModel1));
        assertNull(comparatorUnderTest.compare(attributeModel1, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        int comparisonResult = comparatorUnderTest.compare(attributeModel1, attributeModel1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentNamesIgnoreCase() {
        int comparisonResult = comparatorUnderTest.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0);

        comparisonResult = comparatorUnderTest.compare(attributeModel2, attributeModel1);
        assertTrue(comparisonResult > 0);
    }

    @Test
    public void testCompareAttributeModelWithNameAAfterA() {
        int comparisonResult = comparatorUnderTest.compare(attributeModelA, attributeModel1);
        assertTrue(comparisonResult > 0);

        comparisonResult = comparatorUnderTest.compare(attributeModel1, attributeModelA);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testSortListOfAttributeModels() {
        List<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(attributeModel1);
        attributeModels.add(attributeModelA);
        attributeModels.add(attributeModel2);

        Collections.sort(attributeModels, comparatorUnderTest);

        AttributeModel expectedFirstModel = attributeModelA;
        AttributeModel firstModel = attributeModels.get(0);
        assertEquals(expectedFirstModel.getName(), firstModel.getName());

        AttributeModel expectedLastModel = attributeModel1;
        AttributeModel lastModel = attributeModels.get(2);
        assertEquals(expectedLastModel.getName(), lastModel.getName());

        AttributeModel middleModel = attributeModels.get(1);
        assertEquals(attributeModel2.getName(), middleModel.getName());
    }
}