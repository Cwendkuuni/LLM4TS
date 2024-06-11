package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareNullAttributeModelWithValidAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel validAttributeModel = new AttributeModel("ValidName", "ValidValue");
        int result = comparator.compare(null, validAttributeModel);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareValidAttributeModelsWithSameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ValidName", "ValidValue1");
        AttributeModel attributeModel2 = new AttributeModel("ValidName", "ValidValue2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareValidAttributeModelsWithDifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ValidName1", "ValidValue1");
        AttributeModel attributeModel2 = new AttributeModel("ValidName2", "ValidValue2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompareValidAttributeModelsWithNameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ValidName1", "ValidValue1");
        AttributeModel attributeModel2 = new AttributeModel("a", "ValidValue2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertLessThan(0, result);

        AttributeModel attributeModel3 = new AttributeModel("a", "ValidValue1");
        AttributeModel attributeModel4 = new AttributeModel("ValidName1", "ValidValue1");
        int result2 = comparator.compare(attributeModel3, attributeModel4);
        assertGreaterThan(0, result2);
    }

    @Test
    public void testCompareValidAttributeModelsWithNameAAndA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ValidName1", "ValidValue1");
        AttributeModel attributeModel2 = new AttributeModel("ValidNameA", "ValidValueA");
        AttributeModel attributeModel3 = new AttributeModel("a", "ValidValueA");
        List<AttributeModel> attributeModels = Arrays.asList(attributeModel1, attributeModel2, attributeModel3);
        Collections.sort(attributeModels, comparator);
        AttributeModel expectedFirst = attributeModel3;
        AttributeModel expectedSecond = attributeModel2;
        AttributeModel actualFirst = attributeModels.get(0);
        AttributeModel actualSecond = attributeModels.get(1);
        assertEquals(expectedFirst.getName(), actualFirst.getName());
        assertEquals(expectedSecond.getName(), actualSecond.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullAttributeModelWithValidAttributeModelNullName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel validAttributeModel = new AttributeModel(null, "ValidValue");
        int result = comparator.compare(null, validAttributeModel);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareValidAttributeModelsWithNullAttributeModelName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ValidName", null);
        AttributeModel attributeModel2 = new AttributeModel("ValidName2", "ValidValue2");
        int result = comparator.compare(attributeModel1, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareValidAttributeModelsWithNullAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ValidName", "ValidValue");
        AttributeModel attributeModel2 = null;
        int result = comparator.compare(attributeModel1, attributeModel2);
    }

}