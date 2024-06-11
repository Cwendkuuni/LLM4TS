package de.paragon.explorer.model;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullargs() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareNullFirst() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, new AttributeModel("name1", "value1")));
    }

    @Test
    public void testCompareNullSecond() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(new AttributeModel("name1", "value1"), null));
    }

    @Test
    public void testCompareSameObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = model1;
        assertEquals(0, comparator.compare(model1, model2));
    }

    @Test
    public void testCompareDifferentObjectsDifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        assertEquals(-name1.compareToIgnoreCase(name2), comparator.compare(model1, model2));
    }

    @Test
    public void testCompareDifferentObjectsSameNamesInDifferentOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");
        assertEquals(model2.compareTo(model1), comparator.compare(model1, model2));
    }

    @Test
    public void testCompareDifferentObjectsSameNamesFirstIsA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        assertEquals(1, comparator.compare(model1, model2));
    }

    @Test
    public void testCompareDifferentObjectsSameNamesSecondIsA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name1", "a");
        AttributeModel model2 = new AttributeModel("name2", "a");
        assertEquals(-1, comparator.compare(model1, model2));
    }

    @Test
    public void testCompareDifferentObjectsDifferentNamesDifferentValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        String name1 = model1.getName();
        String name2 = model2.getName();
        String value1 = model1.getValue();
        String value2 = model2.getValue();
        int compareResult = comparator.compare(model1, model2);
        int expectedCompareResult = name1.compareToIgnoreCase(name2);
        if (expectedCompareResult < 0 && compareResult > 0) {
            assertSame(name1, model1.getName());
            assertSame(name2, model2.getName());
            assertNotSame(value1, value2);
        } else if (expectedCompareResult > 0 && compareResult < 0) {
            assertSame(name2, model1.getName());
            assertSame(name1, model2.getName());
            assertNotSame(value1, value2);
        } else {
            assertSame(value1, model1.getValue());
            assertSame(value2, model2.getValue());
            assertSame(name1, model1.getName());
            assertSame(name2, model2.getName());
        }
    }

    @Test
    public void testCompareDifferentObjectsDifferentValuesSameNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");
        assertEquals(-value1.compareTo(value2), comparator.compare(model1, model2));
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(comparator);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) in.readObject();
        assertSame(comparator, deserializedComparator);
    }

    @Test
    public void testComparatorWithEmptyList() {
        List<AttributeModel> emptyList = Collections.emptyList();
        AttributeModelComparator comparator = new AttributeModelComparator();
        int compareResult = comparator.compare(emptyList.get(0), emptyList.get(0));
        assertEquals(0, compareResult);
    }

    @Test
    public void testComparatorWithListContainingOneElement() {
        List<AttributeModel> listWithOneElement = Arrays.asList(new AttributeModel("name1", "value1"));
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel firstElement = listWithOneElement.get(0);
        int compareResult = comparator.compare(firstElement, firstElement);
        assertEquals(0, compareResult);
    }

    @Test
    public void testComparatorWithListContainingTwoDifferentElements() {
        List<AttributeModel> listWithTwoElements = Arrays.asList(new AttributeModel("name1", "value1"), new AttributeModel("name2", "value2"));
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel firstElement = listWithTwoElements.get(0);
        AttributeModel secondElement = listWithTwoElements.get(1);
        int compareResult = comparator.compare(firstElement, secondElement);
        String firstName = firstElement.getName();
        String secondName = secondElement.getName();
        int expectedCompareResult = firstName.compareToIgnoreCase(secondName);
        if (expectedCompareResult < 0) {
            assertSame(firstName, firstElement.getName());
            assertSame(secondName, secondElement.getName());
            assertNotSame(firstElement.getValue(), secondElement.getValue());
        } else if (expectedCompareResult > 0) {
            assertSame(secondName, firstElement.getName());
            assertSame(firstName, secondElement.getName());
            assertNotSame(firstElement.getValue(), secondElement.getValue());
        } else {
            assertSame(firstElement.getValue(), secondElement.getValue());
            assertSame(firstName, firstElement.getName());
            assertSame(secondName, secondElement.getName());
        }
    }
}