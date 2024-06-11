package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Type1");
        AttributeModel attributeModel2 = attributeModel1;
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute2", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertNotEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullFirstAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Type1");
        int result = comparator.compare(null, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullSecondAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Type1");
        int result = comparator.compare(attributeModel1, null);
    }

    @Test
    public void testCompareAttributeModelsWithSameNameButDifferentCases() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ATTRIBUTE1", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompareAttributeModelsWithSameNameAndSameCases() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Type1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareAttributeModelsWithNameStartingWithASmallerThanAnother() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("A_Attribute", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertLessThan(0, result);
    }

    @Test
    public void testCompareAttributeModelsWithNameStartingWithALargerThanAnother() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("A_Attribute", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertGreaterThan(0, result);
    }

    @Test
    public void testCompareAttributeModelsWithNamesContainingASmallerThanAnother() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("AttributeB", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("AttributeA", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertLessThan(0, result);
    }

    @Test
    public void testCompareAttributeModelsWithNamesContainingALargerThanAnother() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("AttributeA", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("AttributeB", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertGreaterThan(0, result);
    }

    @Test
    public void testCompareAttributeModelsWithNullNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel(null, "Type1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertLessThan(0, result); // Null strings should be considered smaller than non-null strings
    }

    @Test
    public void testCompareAttributeModelsWithEmptyNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertLessThan(0, result); // Empty strings should be considered smaller than non-empty strings
    }

    @Test
    public void testCompareAttributeModelsWithnamesWithDifferentLengths() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("ShortName", "Type1");
        AttributeModel attributeModel2 = new AttributeModel("LongerName", "Type2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertLessThan(0, result);
    }

    @Test
    public void testSerializeAndDeserializeAttributeModelComparator() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(comparator);
        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(byteInput);
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) in.readObject();
        assertNotNull(deserializedComparator);
        assertEquals(comparator, deserializedComparator);
    }

}