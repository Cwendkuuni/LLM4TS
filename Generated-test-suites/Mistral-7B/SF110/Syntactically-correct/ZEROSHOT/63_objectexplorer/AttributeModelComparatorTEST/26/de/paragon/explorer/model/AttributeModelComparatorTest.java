package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareBothNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareDifferentObjects() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int compareResult = comparator.compare(model1, model2);

        assertNotEquals(0, compareResult);
        if (compareResult < 0) {
            assertTrue(model1.getName().compareToIgnoreCase(model2.getName()) < 0);
        } else {
            assertTrue(model1.getName().compareToIgnoreCase(model2.getName()) > 0);
        }
    }

    @Test
    public void testCompareSameNameIgnoreCase() {
        AttributeModel model1 = new AttributeModel("nameA", "value1");
        AttributeModel model2 = new AttributeModel("nameA", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int compareResult = comparator.compare(model1, model2);

        assertEquals(0, compareResult);
        assertEquals(model1.getName(), model2.getName());
    }

    @Test
    public void testCompareSameNameDifferentStringOrder() {
        AttributeModel model1 = new AttributeModel("nameA", "value1");
        AttributeModel model2 = new AttributeModel("nameA", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int compareResult = comparator.compare(model2, model1);

        assertEquals(-1, compareResult);
        assertEquals(model1.getName(), model2.getName());
    }

    @Test
    public void testCompareIgnoreA() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("nameA", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int compareResult = comparator.compare(model1, model2);

        assertNotEquals(0, compareResult);
        assertTrue(compareResult < 0 || model1.getName().compareToIgnoreCase("a") < 0);
        assertTrue(compareResult > 0 || model2.getName().compareToIgnoreCase("a") >= 0);
    }

    @Test
    public void testCompareIgnoreANameFirst() {
        AttributeModel model1 = new AttributeModel("nameA", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int compareResult = comparator.compare(model1, model2);

        assertTrue(compareResult > 0 || model1.getName().compareToIgnoreCase("a") >= 0);
        assertTrue(compareResult < 0 || model2.getName().compareToIgnoreCase("a") < 0);
    }

    @Test
    public void testCompareTwoA() {
        AttributeModel model1 = new AttributeModel("nameA", "value1");
        AttributeModel model2 = new AttributeModel("nameA", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int compareResult = comparator.compare(model1, model2);

        assertEquals(0, compareResult);
        assertEquals(model1.getName(), model2.getName());
    }

    @Test
    public void testCompareList() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        AttributeModel model3 = new AttributeModel("nameA", "value3");

        List<AttributeModel> attributeModels = new ArrayList<>(Arrays.asList(model1, model2, model3));
        Collections.sort(attributeModels, new AttributeModelComparator());

        assertEquals("nameA, name1, name2", Arrays.toString(attributeModels.toArray()));
    }

    @Test
    public void testCompareListIgnoreANameFirst() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("nameA", "value2");
        AttributeModel model3 = new AttributeModel("name1", "value3");

        List<AttributeModel> attributeModels = new ArrayList<>(Arrays.asList(model1, model2, model3));
        Collections.sort(attributeModels, new AttributeModelComparator());

        assertEquals("name1, nameA, name1", Arrays.toString(attributeModels.toArray()));
    }
}