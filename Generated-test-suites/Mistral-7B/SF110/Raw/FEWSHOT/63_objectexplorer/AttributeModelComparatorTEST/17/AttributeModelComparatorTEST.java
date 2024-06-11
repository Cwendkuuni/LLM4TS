package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareSameAttributeModels() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name1", "Value1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModels() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0 || comparisonResult > 0);
    }

    @Test
    public void testCompareAttributeModelWithNameA() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModelA = new AttributeModel("a", "ValueA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModelA);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareAttributeModelWithNameA_againstAnotherAttributeModelWithNameA() {
        AttributeModel attributeModelA1 = new AttributeModel("a", "ValueA1");
        AttributeModel attributeModelA2 = new AttributeModel("a", "ValueA2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelA1, attributeModelA2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareAttributeModelWithNameB_againstAnAttributeModelWithNameA() {
        AttributeModel attributeModelA = new AttributeModel("a", "ValueA");
        AttributeModel attributeModelB = new AttributeModel("NameB", "ValueB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelA, attributeModelB);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareAttributeModelWithNameA_againstAnotherAttributeModelWithNameB() {
        AttributeModel attributeModelA = new AttributeModel("a", "ValueA");
        AttributeModel attributeModelB = new AttributeModel("NameB", "ValueB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelB, attributeModelA);
        assertTrue(comparisonResult > 0);
    }

    @Test
    public void testCompareAttributeModelWithNullName() {
        AttributeModel attributeModel1 = new AttributeModel(null, "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareAttributeModelWithEmptyName() {
        AttributeModel attributeModel1 = new AttributeModel("", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareAttributeModelWithNullValue() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", null);
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareAttributeModelWithEmptyValue() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareEmptyListWithNullAttributeModel() {
        List<AttributeModel> attributeModels = Collections.emptyList();
        AttributeModel nullAttributeModel = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(nullAttributeModel, attributeModels.get(0));
        assertNull(comparisonResult);
    }

    @Test
    public void testCompareEmptyListWithEmptyAttributeModel() {
        List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("", ""));
        AttributeModel emptyAttributeModel = new AttributeModel("", "");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(emptyAttributeModel, attributeModels.get(0));
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareEmptyListWithAttributeModelWithNameA() {
        List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("NameA", "ValueA"));
        AttributeModel emptyAttributeModel = new AttributeModel("", "");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(emptyAttributeModel, attributeModels.get(0));
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareListsWithAttributesModelsWithNameA() {
        AttributeModel attributeModelA1 = new AttributeModel("a", "ValueA1");
        AttributeModel attributeModelA2 = new AttributeModel("a", "ValueA2");
        List<AttributeModel> attributeModels = Arrays.asList(attributeModelA1, attributeModelA2);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelA1, attributeModelA2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareListsWithAttributesModelsWithDifferentNames() {
        AttributeModel attributeModelA = new AttributeModel("a", "ValueA");
        AttributeModel attributeModelB = new AttributeModel("NameB", "ValueB");
        List<AttributeModel> attributeModels = Arrays.asList(attributeModelA, attributeModelB);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModelA, attributeModelB);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareListsWithAttributesModelsWithNullAttributeModel() {
        AttributeModel attributeModel = new AttributeModel("Name1", "Value1");
        List<AttributeModel> attributeModels = Arrays.asList(attributeModel, null);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel, null);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testCompareListsWithAttributesModelsWithEmptyAttributeModel() {
        AttributeModel emptyAttributeModel = new AttributeModel("", "");
        List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("Name1", "Value1"), emptyAttributeModel);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(new AttributeModel("Name1", "Value1"), emptyAttributeModel);
        assertTrue(comparisonResult > 0);
    }
}