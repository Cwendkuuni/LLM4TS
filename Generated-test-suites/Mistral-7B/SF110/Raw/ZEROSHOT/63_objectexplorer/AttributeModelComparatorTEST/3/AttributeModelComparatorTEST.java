package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(null, null);
        assertEquals("Comparing null AttributeModels should return 0", 0, comparisonResult);
    }

    @Test
    public void testCompareEqualAttributeModels() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertEquals("Comparing equal AttributeModels should return 0", 0, comparisonResult);
    }

    @Test
    public void testCompareAttributeModelWithNullName() {
        AttributeModel model1 = new AttributeModel(null, "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertNotNull("Comparing AttributeModel with null name should not return 0", comparisonResult);
    }

    @Test
    public void testCompareAttributeModelWithNullValue() {
        AttributeModel model1 = new AttributeModel("Name1", null);
        AttributeModel model2 = new AttributeModel("Name2", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertNotNull("Comparing AttributeModel with null value should not return 0", comparisonResult);
    }

    @Test
    public void testCompareAttributeModelsWithDifferentNames() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertTrue("Comparing AttributeModels with different names should return a non-zero value", comparisonResult != 0);
    }

    @Test
    public void testCompareAttributeModelsWithSameNamesButDifferentCases() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("nameA", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertTrue("Comparing AttributeModels with same names but different cases should return a non-zero value", comparisonResult != 0);
    }

    @Test
    public void testCompareAttributeModelsWithSameNamesIgnoreCase() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertEquals("Comparing AttributeModels with the same names (ignore case) should return 0", 0, comparisonResult);
    }

    @Test
    public void testCompareAttributeModelsWithSameNameButOneIsA() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("a", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertTrue("Comparing AttributeModels with the same name but one is 'a' should return 1 for model1", comparisonResult > 0);
    }

    @Test
    public void testCompareAttributeModelsWithSameNameButOneIsAAndTheOtherIsNot() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameB", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);
        assertTrue("Comparing AttributeModels with different names should return a non-zero value", comparisonResult != 0);
    }

    @Test
    public void testCompareMultipleAttributeModels() {
        List<AttributeModel> models = new ArrayList<>();
        models.add(new AttributeModel("Name1", "Value1"));
        models.add(new AttributeModel("Name2", "Value2"));
        models.add(new AttributeModel("Name3", "Value3"));
        models.add(new AttributeModel("NameA", "ValueA"));

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(models.get(2), models.get(3));
        assertTrue("Comparing AttributeModels with the same name but different indices should return a non-zero value", comparisonResult != 0);
    }
}