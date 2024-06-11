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

    // Test for compare method with null arguments
    @Test(expected = NullPointerException.class)
    public void testCompareWithNullArgs() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        comparator.compare(null, null);
    }

    // Test for compare method with same name
    @Test
    public void testCompareWithSameName() {
        AttributeModel model1 = new AttributeModel("attribute1", "value1");
        AttributeModel model2 = new AttributeModel("attribute1", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(0, result);
    }

    // Test for compare method with different names
    @Test
    public void testCompareWithDifferentNames() {
        AttributeModel model1 = new AttributeModel("attribute1", "value1");
        AttributeModel model2 = new AttributeModel("attribute2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    // Test for compare method with name "a" and different names
    @Test
    public void testCompareWithNameAAndDifferent() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("attribute2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    // Test for compare method with name "a" and same name but different order
    @Test
    public void testCompareWithNameAAndSameNameDifferentOrder() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(0, result);
    }

    // Test for compare method with name "a" and name not equal to "a" but with name coming before "a" in comparison
    @Test
    public void testCompareWithNameNotAAndNameBeforeA() {
        AttributeModel model1 = new AttributeModel("attribute1", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    // Test for compare method with name "a" and name not equal to "a" but with name coming after "a" in comparison
    @Test
    public void testCompareWithNameNotAAndNameAfterA() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("attribute1", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(1, result);
    }

    // Test for compareTo method with empty list
    @Test
    public void testCompareToEmptyList() {
        List<AttributeModel> list = new ArrayList<>();

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, list.toArray(new AttributeModel[0]));

        assertEquals(0, result);
    }

    // Test for compareTo method with list containing one element
    @Test
    public void testCompareToListWithOneElement() {
        AttributeModel model = new AttributeModel("attribute1", "value1");
        List<AttributeModel> list = new ArrayList<>();
        list.add(model);

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, list.toArray(new AttributeModel[0]));

        assertEquals(0, result);
    }

    // Test for compareTo method with list containing two elements with same name
    @Test
    public void testCompareToListWithTwoElementsSameName() {
        AttributeModel model1 = new AttributeModel("attribute1", "value1");
        AttributeModel model2 = new AttributeModel("attribute1", "value2");
        List<AttributeModel> list = new ArrayList<>();
        list.add(model1);
        list.add(model2);

        Collections.sort(list, new AttributeModelComparator());

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, list.toArray(new AttributeModel[0]));

        assertEquals(0, result);
    }

    // Test for compareTo method with list containing two elements with different names
    @Test
    public void testCompareToListWithTwoElementsDifferentNames() {
        AttributeModel model1 = new AttributeModel("attribute1", "value1");
        AttributeModel model2 = new AttributeModel("attribute2", "value2");
        List<AttributeModel> list = new ArrayList<>();
        list.add(model1);
        list.add(model2);

        Collections.sort(list, new AttributeModelComparator());

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, list.toArray(new AttributeModel[0]));

        assertEquals(-1, result);
    }
}