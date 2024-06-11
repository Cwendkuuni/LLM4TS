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
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareSameAttributeModels() {
        AttributeModel attr1 = new AttributeModel("Name1", "Type1", "Value1");
        AttributeModel attr2 = new AttributeModel("Name1", "Type1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(attr1, attr2));
    }

    @Test
    public void testCompareDifferentAttributeModels() {
        AttributeModel attr1 = new AttributeModel("Name1", "Type1", "Value1");
        AttributeModel attr2 = new AttributeModel("Name2", "Type2", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attr1, attr2);

        if (comparisonResult < 0) {
            assertTrue(attr1.getName().compareToIgnoreCase("Name1") < attr2.getName().compareToIgnoreCase("Name2"));
        } else if (comparisonResult > 0) {
            assertTrue(attr1.getName().compareToIgnoreCase("Name1") > attr2.getName().compareToIgnoreCase("Name2"));
        } else {
            fail("Unexpected comparison result: " + comparisonResult);
        }
    }

    @Test
    public void testCompareWithAAttributeModel() {
        AttributeModel attr1 = new AttributeModel("Name1", "Type1", "Value1");
        AttributeModel attrA = new AttributeModel("a", "TypeA", "ValueA");
        AttributeModelComparator comparator = new AttributeModelComparator();

        List<AttributeModel> attributeModels = Arrays.asList(attr1, attrA);
        Collections.sort(attributeModels, comparator);

        AttributeModel expectedFirst = null;
        if (attr1.getName().compareToIgnoreCase("a") >= 0) {
            expectedFirst = attrA;
        } else {
            expectedFirst = attr1;
        }
        AttributeModel first = attributeModels.get(0);
        assertEquals(expectedFirst, first);
    }

    @Test
    public void testCompareWithAnotherAAttributeModel() {
        AttributeModel attrA1 = new AttributeModel("a", "TypeA1", "ValueA1");
        AttributeModel attrA2 = new AttributeModel("a", "TypeA2", "ValueA2");
        AttributeModelComparator comparator = new AttributeModelComparator();

        List<AttributeModel> attributeModels = Arrays.asList(attrA1, attrA2);
        Collections.sort(attributeModels, comparator);

        AttributeModel expectedFirst = null;
        if (attrA1.getName().compareToIgnoreCase("a") < attrA2.getName().compareToIgnoreCase("a")) {
            expectedFirst = attrA1;
        } else {
            expectedFirst = attrA2;
        }
        AttributeModel first = attributeModels.get(0);
        assertEquals(expectedFirst, first);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
        outStream.writeObject(comparator);
        byte[] serializedObject = byteStream.toByteArray();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedObject);
        ObjectInputStream inStream = new ObjectInputStream(byteInput);
        AttributeModelComparator deserializedObject = (AttributeModelComparator) inStream.readObject();

        AttributeModel attr1 = new AttributeModel("Name1", "Type1", "Value1");
        AttributeModel attr2 = new AttributeModel("Name2", "Type2", "Value2");

        assertEquals(0, deserializedObject.compare(attr1, attr2));
    }
}