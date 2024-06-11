package de.paragon.explorer.model;

import static org.junit.Assert.*;

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
    public void testCompareIdenticalAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelWithNameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value > 0);
    }

    @Test
    public void testCompareAttributeModelWithNameA_vs_AttributeModelWithNameAA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("aa", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelWithNameAA_vs_AttributeModelWithNameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("aa", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value > 0);
    }

    @Test
    public void testCompareAttributeModelWithNameA_vs_AttributeModelWithNameA_Identical() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value1");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModelWithNullName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel(null, "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertNull(value);
    }

    @Test
    public void testCompareAttributeModelWithEmptyName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelWithNullValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", null);
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertNull(value);
    }

    @Test
    public void testCompareAttributeModelWithEmptyValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelsWithSameNameAndValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModelsWithDifferentNamesAndValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelsWithNamesInReverseOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name2", "value2");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value > 0);
    }

    @Test
    public void testCompareAttributeModelsWithNamesEqualAndValuesInReverseOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value2");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelsWithNamesEqualAndValuesEqual() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModelsWithNamesEqualAndValuesDifferent() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelsWithNamesDifferentAndValuesEqual() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModelsWithNamesDifferentAndValuesDifferent() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_Null_vs_AttributeModelWithName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("name1", "value1");
        int value = comparator.compare(null, attributeModel);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithNullName_vs_AttributeModelWithName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel(null, "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithNullName_vs_AttributeModelWithNullName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel(null, null);
        AttributeModel attributeModel2 = new AttributeModel(null, null);
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithEmptyName_vs_AttributeModelWithName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithEmptyName_vs_AttributeModelWithEmptyName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("", "");
        AttributeModel attributeModel2 = new AttributeModel("", "");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithNullValue_vs_AttributeModelWithValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", null);
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithNullValue_vs_AttributeModelWithNullValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", null);
        AttributeModel attributeModel2 = new AttributeModel("name1", null);
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithEmptyValue_vs_AttributeModelWithValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithEmptyValue_vs_AttributeModelWithEmptyValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "");
        AttributeModel attributeModel2 = new AttributeModel("name1", "");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithDifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value1");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithSameNameButDifferentValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value2");
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithSameNameButSameValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithSameNameButSameValuesInReverseOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        Collections.reverse(Arrays.asList(attributeModel1.getValue().split("")));
        Collections.reverse(Arrays.asList(attributeModel2.getValue().split("")));
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithSameNameButSameValuesInReverseOrder_WithEmptyString() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "");
        AttributeModel attributeModel2 = new AttributeModel("name1", "");
        Collections.reverse(Arrays.asList(attributeModel1.getValue().split("")));
        Collections.reverse(Arrays.asList(attributeModel2.getValue().split("")));
        int value = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(value < 0);
    }

    @Test
    public void testCompareAttributeModels_AttributeModelWithSameNameButSameValuesAndEmptyString() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel3 = new AttributeModel("name1", "");
        int value = comparator.compare(attributeModel1, attributeModel3);
        assertTrue(value < 0);
    }
}