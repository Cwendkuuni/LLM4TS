package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompare_NullAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        assertNull(comparator.compare(o1, null));
    }

    @Test
    public void testCompare_SameAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name1", "value1");
        assertEquals(0, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_DifferentAttributeModels_DifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name2", "value2");
        int comparisonResult = comparator.compare(o1, o2);
        assertNotEquals(0, comparisonResult);
        assertTrue(comparisonResult < 0 || comparisonResult > 0);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_DifferentOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a", "value1");
        AttributeModel o2 = new AttributeModel("b", "value2");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(o2, o1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_SameOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a", "value1");
        AttributeModel o2 = new AttributeModel("a", "value2");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(0, comparisonResult);

        comparisonResult = comparator.compare(o2, o1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_FirstIsEarlier() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a", "value1");
        AttributeModel o2 = new AttributeModel("a", "value2");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_SecondIsEarlier() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a", "value1");
        AttributeModel o2 = new AttributeModel("a", "value2");
        int comparisonResult = comparator.compare(o2, o1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_BothAreEarlierThanA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("a", "value3");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(1, comparisonResult);

        comparisonResult = comparator.compare(o2, o1);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_BothAreLaterThanA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("a", "value3");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(o2, o1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_FirstIsEarlierThanA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("a", "value3");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_SecondIsEarlierThanA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("a", "value3");
        int comparisonResult = comparator.compare(o2, o1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_BothAreA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a", "value1");
        AttributeModel o2 = new AttributeModel("a", "value2");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(0, comparisonResult);

        comparisonResult = comparator.compare(o2, o1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_DifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name2", "value2");
        int comparisonResult = comparator.compare(o1, o2);
        assertNotEquals(0, comparisonResult);
        assertTrue(comparisonResult < 0 || comparisonResult > 0);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameNames_DifferentValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name1", "value2");
        int comparisonResult = comparator.compare(o1, o2);
        assertNotEquals(0, comparisonResult);
        assertTrue(comparisonResult < 0 || comparisonResult > 0);
    }

    @Test
    public void testCompare_DifferentAttributeModels_NullValues() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", null);
        AttributeModel o2 = new AttributeModel("name2", null);
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_NullValue1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name2", null);
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_NullValue2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name2", "value2");
        AttributeModel o3 = new AttributeModel("name2", null);
        int comparisonResult = comparator.compare(o1, o3);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_EmptyStrings() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "");
        AttributeModel o2 = new AttributeModel("name2", "");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_EmptyString1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name2", "");
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_EmptyString2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "value1");
        AttributeModel o2 = new AttributeModel("name2", "value2");
        AttributeModel o3 = new AttributeModel("name2", "");
        int comparisonResult = comparator.compare(o1, o3);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_EmptyStringsAndNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "");
        AttributeModel o2 = new AttributeModel("name2", null);
        int comparisonResult = comparator.compare(o1, o2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_EmptyStringsAndNull_ReverseOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name1", "");
        AttributeModel o2 = new AttributeModel("name2", null);
        int comparisonResult = comparator.compare(o2, o1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(comparator);
        byte[] serializedByteArray = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedByteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) objectInputStream.readObject();

        assertEquals(comparator, deserializedComparator);
    }
}