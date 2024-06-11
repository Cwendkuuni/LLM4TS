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
    public void testCompare_sameName_returnsZero() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);

        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_differentNames_returnsNegativeOrPositive() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);

        if (model1.getName().compareToIgnoreCase(model2.getName()) < 0) {
            assertTrue(comparisonResult < 0);
        } else {
            assertTrue(comparisonResult > 0);
        }
    }

    @Test
    public void testCompare_nullValues_returnsComparableNames() {
        AttributeModel model1 = new AttributeModel("name1", null);
        AttributeModel model2 = new AttributeModel("name2", null);

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);

        String model1Name = model1.getName();
        String model2Name = model2.getName();

        if (model1Name.compareToIgnoreCase(model2Name) < 0) {
            assertTrue(comparisonResult < 0);
        } else if (model1Name.compareToIgnoreCase(model2Name) > 0) {
            assertTrue(comparisonResult > 0);
        } else {
            assertTrue(comparisonResult == 0);
        }
    }

    @Test
    public void testCompare_o1IsNull_returnsNegativeIfO2NameIsLessThanA() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("name2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);

        String model2Name = model2.getName();

        if (model2Name.compareToIgnoreCase("a") < 0) {
            assertTrue(comparisonResult < 0);
        } else {
            assertTrue(comparisonResult >= 0);
        }
    }

    @Test
    public void testCompare_o2IsNull_returnsPositiveIfO1NameIsGreaterThanA() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = null;

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);

        String model1Name = model1.getName();

        if (model1Name.compareToIgnoreCase("a") > 0) {
            assertTrue(comparisonResult > 0);
        } else {
            assertTrue(comparisonResult <= 0);
        }
    }

    @Test
    public void testCompare_bothObjectsHaveNameA_returnsZero() {
        AttributeModel model1 = new AttributeModel("a", null);
        AttributeModel model2 = new AttributeModel("a", null);

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(model1, model2);

        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_listWithDifferentObjects_returnsCorrectOrder() {
        List<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(new AttributeModel("name3", "value3"));
        attributeModels.add(new AttributeModel("name1", "value1"));
        attributeModels.add(new AttributeModel("name2", "value2"));
        attributeModels.add(new AttributeModel("a", null));

        Collections.sort(attributeModels, new AttributeModelComparator());

        assertEquals("name1", attributeModels.get(1).getName());
        assertEquals("name2", attributeModels.get(2).getName());
        assertEquals("name3", attributeModels.get(0).getName());
        assertEquals("a", attributeModels.get(3).getName());
    }
}