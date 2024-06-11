package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompareEqualAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "Value1");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("name1", "Value2");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameAndAAsFirstIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("a", "Value2");
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameAndAAfterIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "a");
        int result = comparator.compare(model1, model2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNamesIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithAFirstInOneIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("a", "Value2");
        int result = comparator.compare(model2, model1);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithAAfterInOneIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "a");
        int result = comparator.compare(model1, model2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNamesAndSameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "a");
        AttributeModel model2 = new AttributeModel("Name2", "a");
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNamesAndDifferentA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Name1", "a");
        AttributeModel model2 = new AttributeModel("Name2", "b");
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareMultipleAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> list = new ArrayList<>();
        list.add(new AttributeModel("Name1", "Value1"));
        list.add(new AttributeModel("Name2", "Value2"));
        list.add(new AttributeModel("Name3", "Value3"));
        list.add(new AttributeModel("a", "Value4"));
        Collections.sort(list, comparator);
        assertEquals("Name1", list.get(0).getName());
        assertEquals("Name2", list.get(1).getName());
        assertEquals("Name3", list.get(2).getName());
        assertEquals("a", list.get(3).getName());
    }

}