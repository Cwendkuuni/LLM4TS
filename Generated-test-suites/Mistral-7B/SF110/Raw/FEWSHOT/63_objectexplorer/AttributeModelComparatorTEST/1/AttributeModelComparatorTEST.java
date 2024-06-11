package de.paragon.explorer.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullAttributeModels() {
        AttributeModel comparator1 = null;
        AttributeModel comparator2 = null;
        AttributeModelComparator attributeModelComparator = new AttributeModelComparator();
        int result = attributeModelComparator.compare(comparator1, comparator2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_NullAttributeModelAndValidAttributeModel() {
        AttributeModel validAttributeModel = new AttributeModel("ValidName", "ValidValue");
        AttributeModelComparator attributeModelComparator = new AttributeModelComparator();
        int result = attributeModelComparator.compare(null, validAttributeModel);
        assertNull(result);
    }

    @Test
    public void testCompare_ValidAttributeModels_DifferentNames() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator attributeModelComparator = new AttributeModelComparator();
        int result = attributeModelComparator.compare(model1, model2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompare_ValidAttributeModels_SameName_DifferentOrder() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator attributeModelComparator = new AttributeModelComparator();
        int result = attributeModelComparator.compare(model1, model2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompare_ValidAttributeModels_SameName_SameOrder() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value1");
        AttributeModelComparator attributeModelComparator = new AttributeModelComparator();
        int result = attributeModelComparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_ValidAttributeModels_SameName_FirstModelIsA() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator attributeModelComparator = new AttributeModelComparator();
        int result = attributeModelComparator.compare(model2, model1);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_ValidAttributeModels_SameName_FirstModelIsNotA() {
        AttributeModel model1 = new AttributeModel("NameB", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator attributeModelComparator = new AttributeModelComparator();
        int result = attributeModelComparator.compare(model1, model2);
        assertEquals(1, result);
    }
}