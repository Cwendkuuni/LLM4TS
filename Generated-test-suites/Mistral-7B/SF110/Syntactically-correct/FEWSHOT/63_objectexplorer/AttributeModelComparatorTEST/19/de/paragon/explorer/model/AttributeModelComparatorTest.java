package de.paragon.explorer.model;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullName() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = null;
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertNull(attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareNullAttributeModel() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertNull(attributeModel1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareSameAttributeModels() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentNamesIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentNames() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareNameAFirst() {
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareNameAAfter() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value2");
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareMultipleModels() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");
        AttributeModel attributeModel3 = new AttributeModel("a", "value3");
        List<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(attributeModel1);
        attributeModels.add(attributeModel2);
        attributeModels.add(attributeModel3);
        Collections.sort(attributeModels, new AttributeModelComparator());
        AttributeModel expectedFirst = attributeModel3;
        AttributeModel actualFirst = attributeModels.get(0);
        assertSame(expectedFirst, actualFirst);

        AttributeModel expectedSecond = attributeModel1;
        AttributeModel actualSecond = attributeModels.get(1);
        assertSame(expectedSecond, actualSecond);

        AttributeModel expectedThird = attributeModel2;
        AttributeModel actualThird = attributeModels.get(2);
        assertSame(expectedThird, actualThird);
    }
}