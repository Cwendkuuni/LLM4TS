package de.paragon.explorer.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_SameNames_DifferentObjects() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SameNames_SameObjects() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = model1;

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_SameName_FirstModelHasLowerPriority() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SameName_FirstModelHasHigherPriority() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model2, model1);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_NullObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_NullObjectFirst() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(null, model1);

        assertTrue(result < 0);
    }

    @Test
    public void testCompare_NullObjectSecond() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, null);

        assertTrue(result > 0);
    }

    @Test
    public void testCompare_EqualStringValues() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_DifferentStringValues() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_DifferentNames_SameValues() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_DifferentNames_FirstModelNameLowercase() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("Name2", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_DifferentNames_FirstModelNameUppercase() {
        AttributeModel model1 = new AttributeModel("NAME1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_DifferentNames_FirstModelNameEqualsA() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_DifferentNames_SecondModelNameEqualsA() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("a", "value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_EmptyList() {
        List<AttributeModel> list = Collections.emptyList();
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(list.get(0), list.get(1));

        assertNull(result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCompare_ListIndexOutOfBounds() {
        List<AttributeModel> list = Arrays.asList(new AttributeModel("name1", "value1"), new AttributeModel("name2", "value1"));
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(list.get(1), list.get(2));
    }

    @Test
    public void testCompare_ListsWithSingleElement() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        List<AttributeModel> list1 = Collections.singletonList(model1);

        AttributeModel model2 = new AttributeModel("name2", "value1");
        List<AttributeModel> list2 = Collections.singletonList(model2);

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(list1.get(0), list2.get(0));

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_ListsWithDifferentNumberOfElements() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value1");

        List<AttributeModel> list1 = Arrays.asList(model1);
        List<AttributeModel> list2 = Arrays.asList(model1, model2);

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(list1.get(0), list2.get(0));

        assertEquals(-1, result);
    }
}