package de.paragon.explorer.model;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNull_thenReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsAreNotNullAndHaveDifferentNames_thenCompareNamesIgnoreCase() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(-1 * "Name1".compareToIgnoreCase("Name2"), result);
    }

    @Test
    public void testCompare_whenBothObjectsAreNotNullAndHaveSameName_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenFirstObjectIsNotNullAndSecondIsNull_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_whenFirstObjectIsNullAndSecondIsNotNull_thenReturnNegativeNumber() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model2 = new AttributeModel("Name1", "Value1");

        int result = comparator.compare(null, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveNameA_thenCompareNamesIgnoringCase() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameAAndSecondHasNameB_thenReturnNegativeNumber() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameB", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameBAndSecondHasNameA_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("NameB", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameAAndSecondHasNameA_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("NameAA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveSameNameAndValue_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("NameA", "ValueA");
        AttributeModel model2 = new AttributeModel("NameA", "ValueA");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveSameNameButDifferentValues_thenCompareValues() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        String expected = "Value1".compareTo("Value2");
        assertEquals(expected, result);
    }

    @Test
    public void testCompare_whenFirstObjectHasNullName_thenThrowNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel(null, "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");

        try {
            comparator.compare(model1, model2);
        } catch (NullPointerException e) {
            assertEquals("Comparator received a null AttributeModel.Name", e.getMessage());
            return;
        }

        fail("Expected NullPointerException");
    }

    @Test
    public void testCompare_whenSecondObjectHasNullName_thenThrowNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel(null, "Value2");

        try {
            comparator.compare(model1, model2);
        } catch (NullPointerException e) {
            assertEquals("Comparator received a null AttributeModel.Name", e.getMessage());
            return;
        }

        fail("Expected NullPointerException");
    }

    @Test
    public void testCompareTo_whenBothObjectsAreNotNullAndHaveSameName_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_whenBothObjectsAreNotNullAndHaveDifferentNames_thenCompareNamesIgnoreCase() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(-1 * "Name1".compareToIgnoreCase("Name2"), result);
    }

    @Test
    public void testCompareTo_whenBothObjectsAreNotNullAndHaveSameNameAndValue_thenReturnZero() {
        AttributeModel model1 = new AttributeModel("NameA", "ValueA");
        AttributeModel model2 = new AttributeModel("NameA", "ValueA");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_whenFirstObjectIsNotNullAndSecondIsNull_thenReturnPositiveNumber() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compareTo(model1, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompareTo_whenFirstObjectIsNullAndSecondIsNotNull_thenReturnNegativeNumber() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model2 = new AttributeModel("NameA", "Value1");

        int result = comparator.compareTo(null, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareTo_whenBothObjectsHaveSameNameAndDifferentValues_thenCompareValues() {
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compareTo(model1, model2);
        String expected = "Value1".compareTo("Value2");
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo_whenFirstObjectHasNullName_thenThrowNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel(null, "Value1");

        try {
            comparator.compareTo(model1, new AttributeModel("NameA", "Value2"));
        } catch (NullPointerException e) {
            assertEquals("Comparator received a null AttributeModel.Name", e.getMessage());
            return;
        }

        fail("Expected NullPointerException");
    }

    @Test
    public void testCompareTo_whenSecondObjectHasNullName_thenThrowNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("NameA", "Value1");

        try {
            comparator.compareTo(new AttributeModel("NameA", "Value1"), null);
        } catch (NullPointerException e) {
            assertEquals("Comparator received a null AttributeModel.Name", e.getMessage());
            return;
        }

        fail("Expected NullPointerException");
    }

    @Test
    public void testSort_whenListContainsNullObjects_thenThrowNullPointerException() {
        List<AttributeModel> unsortedList = new ArrayList<>();
        unsortedList.add(null);
        unsortedList.add(new AttributeModel("Name1", "Value1"));

        try {
            Collections.sort(unsortedList, new AttributeModelComparator());
        } catch (NullPointerException e) {
            assertEquals("Comparator received a null AttributeModel.Name", e.getMessage());
            return;
        }

        fail("Expected NullPointerException");
    }

    @Test
    public void testSort_whenListIsEmpty_thenReturnEmptyList() {
        List<AttributeModel> list = Collections.emptyList();
        Collections.sort(list, new AttributeModelComparator());

        assertEquals(list, Collections.emptyList());
    }

    @Test
    public void testSort_whenListContainsOneElement_thenReturnUnmodifiedList() {
        List<AttributeModel> list = new ArrayList<>();
        AttributeModel model = new AttributeModel("Name1", "Value1");
        list.add(model);

        Collections.sort(list, new AttributeModelComparator());

        assertEquals(list.get(0), model);
    }

    @Test
    public void testSort_whenListContainsObjectsWithSameName_thenSortByValues() {
        List<AttributeModel> list = new ArrayList<>();
        AttributeModel model1 = new AttributeModel("NameA", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "Value2");
        AttributeModel model3 = new AttributeModel("NameA", "Value3");
        list.add(model1);
        list.add(model2);
        list.add(model3);

        Collections.sort(list, new AttributeModelComparator());

        AttributeModel expected = list.get(0);
        assertEquals("Value1", expected.getValue());
        assertEquals("Value2", list.get(1).getValue());
        assertEquals("Value3", list.get(2).getValue());
    }

    @Test
    public void testSort_whenListContainsObjectsWithDifferentNames_thenSortByNameIgnoringCase() {
        List<AttributeModel> list = new ArrayList<>();
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("NameB", "Value2");
        AttributeModel model3 = new AttributeModel("NameA", "Value3");
        list.add(model1);
        list.add(model2);
        list.add(model3);

        Collections.sort(list, new AttributeModelComparator());

        AttributeModel expected = list.get(0);
        assertEquals("NameA", expected.getName());
        assertEquals("NameA", list.get(2).getName());
        assertEquals("NameB", list.get(1).getName());
    }
}