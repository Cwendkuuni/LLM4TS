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
    public void testCompare_nullAttributeModels_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_sameAttributeModels_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_differentAttributeModels_withNullNames_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel(null, "value1");
        AttributeModel attributeModel2 = new AttributeModel(null, "value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_differentAttributeModels_withSameNameIgnoreCase_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_differentAttributeModels_withDifferentNamesIgnoreCase_returnsValueFromCompareToIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1 * "Name1".compareToIgnoreCase("name2"), result);
    }

    @Test
    public void testCompare_differentAttributeModels_withNameA_returns1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_differentAttributeModels_withNameA_andNameANewerThanA_returnsMinusOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name1", "value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_multipleAttributeModels_sortsCorrectly() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");
        AttributeModel attributeModel3 = new AttributeModel("Name3", "value3");
        AttributeModel attributeModel4 = new AttributeModel("a", "valueA");
        AttributeModel attributeModel5 = new AttributeModel("Name5", "value5");

        List<AttributeModel> attributeModels = new ArrayList<>(Arrays.asList(attributeModel1, attributeModel2, attributeModel3, attributeModel4, attributeModel5));
        Collections.sort(attributeModels, comparator);

        AttributeModel expectedFirst = attributeModel4;
        AttributeModel actualFirst = attributeModels.get(0);
        assertEquals(expectedFirst.getName(), actualFirst.getName());

        AttributeModel expectedSecond = attributeModel1;
        AttributeModel actualSecond = attributeModels.get(1);
        assertEquals(expectedSecond.getName(), actualSecond.getName());

        AttributeModel expectedThird = attributeModel2;
        AttributeModel actualThird = attributeModels.get(2);
        assertEquals(expectedThird.getName(), actualThird.getName());

        AttributeModel expectedFourth = attributeModel3;
        AttributeModel actualFourth = attributeModels.get(3);
        assertEquals(expectedFourth.getName(), actualFourth.getName());

        AttributeModel expectedFifth = attributeModel5;
        AttributeModel actualFifth = attributeModels.get(4);
        assertEquals(expectedFifth.getName(), actualFifth.getName());
    }
}