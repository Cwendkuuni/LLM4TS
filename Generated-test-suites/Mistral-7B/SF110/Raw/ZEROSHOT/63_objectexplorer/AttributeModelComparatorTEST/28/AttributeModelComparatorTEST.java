package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsNull_thenReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsNotNull_thenCompareByNameIgnoreCase() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);

        if (result < 0) {
            assertTrue(model1.getName().compareToIgnoreCase(model2.getName()) < 0);
        } else if (result > 0) {
            assertTrue(model1.getName().compareToIgnoreCase(model2.getName()) > 0);
        } else {
            fail("Expected different names, but they were equal.");
        }
    }

    @Test
    public void testCompare_whenFirstObjectHasNameA_andSecondObjectDoesNot_thenReturnNegativeOne() {
        AttributeModel model1 = new AttributeModel("a", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenFirstObjectDoesNotHaveNameA_andSecondObjectHasNameA_thenReturnOne() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("a", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveNameA_thenCompareByNameIgnoreCase() {
        AttributeModel model1 = new AttributeModel("a", "Value1");
        AttributeModel model2 = new AttributeModel("a", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);

        if (result < 0) {
            assertTrue(model1.getName().compareToIgnoreCase(model2.getName()) < 0);
        } else if (result > 0) {
            assertTrue(model1.getName().compareToIgnoreCase(model2.getName()) > 0);
        } else {
            fail("Expected different values, but they were equal.");
        }
    }

    @Test
    public void testComparator_whenSortingList_thenSortedByNameIgnoreCase() {
        List<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(new AttributeModel("Name3", "Value3"));
        attributeModels.add(new AttributeModel("Name1", "Value1"));
        attributeModels.add(new AttributeModel("Name2", "Value2"));
        attributeModels.add(new AttributeModel("a", "Value4"));

        Collections.sort(attributeModels, new AttributeModelComparator());

        AttributeModel previousModel = null;
        for (AttributeModel model : attributeModels) {
            if (previousModel != null) {
                if (previousModel.getName().compareToIgnoreCase(model.getName()) > 0) {
                    fail("Expected sorted list, but found: " + previousModel.getName() + " > " + model.getName());
                }
            }
            previousModel = model;
        }
    }

    @Test
    public void testComparator_whenSortingEmptyList_thenReturnsEmptyList() {
        List<AttributeModel> attributeModels = Collections.emptyList();
        List<AttributeModel> sortedAttributeModels = new ArrayList<>(attributeModels);
        Collections.sort(sortedAttributeModels, new AttributeModelComparator());

        assertEquals(0, sortedAttributeModels.size());
    }

}