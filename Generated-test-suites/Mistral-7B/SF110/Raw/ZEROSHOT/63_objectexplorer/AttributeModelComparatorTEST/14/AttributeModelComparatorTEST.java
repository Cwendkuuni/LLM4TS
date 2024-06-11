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
    public void testCompare_sameName_returnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_differentNames_returnsNegativeOrPositive() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertTrue(result < 0);

        attributeModel1 = new AttributeModel("nameA", "value1");
        attributeModel2 = new AttributeModel("name1", "value2");

        result = comparator.compare(attributeModel1, attributeModel2);

        assertTrue(result > 0);

        attributeModel1 = new AttributeModel("nameA", "value1");
        attributeModel2 = new AttributeModel("nameA", "value2");

        result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_nullObjects_throwsNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        assertThrows(NullPointerException.class, () -> comparator.compare(null, new AttributeModel("name", "value")));
        assertThrows(NullPointerException.class, () -> comparator.compare(new AttributeModel("name", "value"), null));
    }

    @Test
    public void testCompareToIgnoredCase() {
        AttributeModel attributeModel1 = new AttributeModel("nameA", "value1");
        AttributeModel attributeModel2 = new AttributeModel("namea", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }

    @Test
    public void testCompareToSpecificName() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("nameA", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertTrue(result < 0);

        attributeModel1 = new AttributeModel("nameA", "value1");
        attributeModel2 = new AttributeModel("name1", "value2");

        result = comparator.compare(attributeModel1, attributeModel2);

        assertTrue(result > 0);
    }

    @Test
    public void testComparator_sortsList() {
        List<AttributeModel> attributeModels = Arrays.asList(
                new AttributeModel("name5", "value5"),
                new AttributeModel("name4", "value4"),
                new AttributeModel("name3", "value3"),
                new AttributeModel("name1", "value1"),
                new AttributeModel("nameA", "valueA")
        );

        Collections.sort(attributeModels, new AttributeModelComparator());

        AttributeModel expectedFirst = attributeModels.get(0);
        AttributeModel expectedLast = attributeModels.get(attributeModels.size() - 1);

        assertTrue(expectedFirst.getName().compareToIgnoreCase("nameA") < 0);
        assertTrue(expectedLast.getName().compareToIgnoreCase("name5") > 0);
    }

    @Test
    public void testComparator_sortsListWithSameName() {
        List<AttributeModel> attributeModels = Arrays.asList(
                new AttributeModel("name1", "value1"),
                new AttributeModel("name1", "value2"),
                new AttributeModel("name1", "value3")
        );

        Collections.sort(attributeModels, new AttributeModelComparator());

        AttributeModel expectedFirst = attributeModels.get(0);
        AttributeModel expectedLast = attributeModels.get(attributeModels.size() - 1);

        assertEquals(expectedFirst, attributeModels.get(1));
        assertEquals(expectedLast, attributeModels.get(attributeModels.size() - 1));
    }
}