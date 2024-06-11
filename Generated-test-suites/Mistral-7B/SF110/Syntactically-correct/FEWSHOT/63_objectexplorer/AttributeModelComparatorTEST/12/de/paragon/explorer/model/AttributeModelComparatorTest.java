package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.hamcrest.CoreMatchers;

import java.util.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompareWithNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareWithNullAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model = new AttributeModel("testName");
        assertNotEquals(0, comparator.compare(model, null));
    }

    @Test
    public void testCompareIdenticalAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("identicalName");
        AttributeModel model2 = new AttributeModel("identicalName");
        assertEquals(0, comparator.compare(model1, model2));
    }

    @Test
    public void testCompareDifferentAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("differentName1");
        AttributeModel model2 = new AttributeModel("differentName2");
        assertNotEquals(0, comparator.compare(model1, model2));
    }

    @Test
    public void testCompareWithNameA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("differentName");
        int value = comparator.compare(model1, model2);
        assertEquals(-1, value);
    }

    @Test
    public void testCompareWithNameADifferentOrder() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("differentName");
        AttributeModel model2 = new AttributeModel("a");
        int value = comparator.compare(model1, model2);
        assertEquals(1, value);
    }

    @Test
    public void testCompareWithNameAA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("a");
        assertEquals(0, comparator.compare(model1, model2));
    }

    @Test
    public void testSerializability() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
        outputStream.writeObject(comparator);
        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        ObjectInputStream inputStream = new ObjectInputStream(byteInput);
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) inputStream.readObject();
        AttributeModel model1 = new AttributeModel("testName1");
        AttributeModel model2 = new AttributeModel("testName2");
        int value = deserializedComparator.compare(model1, model2);
        assertEquals(-1, value);
    }
}