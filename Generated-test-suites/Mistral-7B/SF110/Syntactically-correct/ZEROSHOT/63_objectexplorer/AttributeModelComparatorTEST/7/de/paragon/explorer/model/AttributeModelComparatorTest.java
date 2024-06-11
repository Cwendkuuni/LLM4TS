package de.paragon.explorer.model.test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompareNullObjects() {
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareEqualObjects() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name1", "Value1");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentObjectsWithSameNameIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name1", "Value2");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentObjectsWithDifferentNamesIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0 || result > 0);
    }

    @Test
    public void testCompareDifferentObjectsWithNameA() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "Value2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareDifferentObjectsWithNameAA() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "Value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareDifferentObjectsWithNameADifferentValues() {
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "Value2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareMultipleObjects() {
        List<AttributeModel> list = new ArrayList<>();
        list.add(new AttributeModel("Name1", "Value1"));
        list.add(new AttributeModel("a", "Value1"));
        list.add(new AttributeModel("Name2", "Value2"));

        List<AttributeModel> sortedList = new ArrayList<>(Arrays.asList(
                new AttributeModel("a", "Value1"),
                new AttributeModel("Name1", "Value1"),
                new AttributeModel("Name2", "Value2")
        ));

        Collections.sort(list, comparator);
        assertEquals(sortedList, list);
    }
}