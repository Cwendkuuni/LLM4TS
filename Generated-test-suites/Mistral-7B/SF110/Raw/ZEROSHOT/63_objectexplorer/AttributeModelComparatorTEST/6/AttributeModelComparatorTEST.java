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
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompareSameAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Value2");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNamesIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute2", "Value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareAttributeModelWithNameAFirst() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute2", "Value1");
        AttributeModel attributeModel3 = new AttributeModel("a", "Value1");
        int result1 = comparator.compare(attributeModel1, attributeModel2);
        int result2 = comparator.compare(attributeModel3, attributeModel1);
        int result3 = comparator.compare(attributeModel2, attributeModel3);
        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(-1, result3);
    }

    @Test
    public void testCompareAttributeModelWithNameAAfter() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "Value1");
        AttributeModel attributeModel3 = new AttributeModel("Attribute2", "Value1");
        int result1 = comparator.compare(attributeModel1, attributeModel2);
        int result2 = comparator.compare(attributeModel3, attributeModel1);
        int result3 = comparator.compare(attributeModel2, attributeModel3);
        assertEquals(1, result1);
        assertEquals(-1, result2);
        assertEquals(0, result3);
    }

    @Test
    public void testCompareAttributeModelWithNullName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel(null, "Value1");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareWithNullComparator() {
        AttributeModelComparator comparator = null;
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute2", "Value1");
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareWithNullAttributeModel1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("Attribute2", "Value1");
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareWithNullAttributeModel2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = null;
        comparator.compare(attributeModel1, attributeModel2);
    }

    @Test
    public void testSerializability() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
        outStream.writeObject(comparator);
        byte[] byteArray = byteStream.toByteArray();
        ByteArrayInputStream byteStream2 = new ByteArrayInputStream(byteArray);
        ObjectInputStream inStream = new ObjectInputStream(byteStream2);
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) inStream.readObject();
        assertEquals(comparator, deserializedComparator);
    }

    @Test
    public void testComparatorReturnsExpectedOrderForMultipleAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(new AttributeModel("Attribute1", "Value1"));
        attributeModels.add(new AttributeModel("Attribute2", "Value1"));
        attributeModels.add(new AttributeModel("a", "Value1"));
        attributeModels.add(new AttributeModel("Attribute3", "Value1"));
        attributeModels.add(new AttributeModel("Attribute4", "Value1"));
        attributeModels.add(new AttributeModel("Attribute5", "Value1"));
        attributeModels.add(new AttributeModel("Attribute6", "Value1"));
        attributeModels.add(new AttributeModel("Attribute7", "Value1"));
        attributeModels.add(new AttributeModel("Attribute8", "Value1"));
        attributeModels.add(new AttributeModel("Attribute9", "Value1"));
        attributeModels.add(new AttributeModel("Attribute10", "Value1"));

        List<AttributeModel> sortedAttributeModels = new ArrayList<>(attributeModels);
        Collections.sort(sortedAttributeModels, comparator);

        List<String> expectedNames = Arrays.asList("a", "Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5", "Attribute6", "Attribute7", "Attribute8", "Attribute9", "Attribute10");
        List<String> actualNames = new ArrayList<>();
        for (AttributeModel attributeModel : sortedAttributeModels) {
            actualNames.add(attributeModel.getName());
        }
        assertEquals(expectedNames, actualNames);
    }

}