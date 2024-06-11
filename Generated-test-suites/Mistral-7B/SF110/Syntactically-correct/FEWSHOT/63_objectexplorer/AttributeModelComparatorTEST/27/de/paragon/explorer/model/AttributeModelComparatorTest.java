package de.paragon.explorer.model;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompare_NullAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel nullAttributeModel = null;
        AttributeModel nonNullAttributeModel = new AttributeModel("name", "value");
        int comparisonResult = comparator.compare(nullAttributeModel, nonNullAttributeModel);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_SameAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name1", "Value1");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_DifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-Name1.compareToIgnoreCase(Name2), comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_FirstModelBefore() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_SecondModelBefore() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "Value2");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_BothBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name3", "Value3");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-Name1.compareToIgnoreCase(Name3), comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_BothA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "Value2");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_FirstModelHasHigherValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name", "Value2");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-Value1.compareTo(Value2), comparisonResult);
    }

    @Test
    public void testCompare_DifferentAttributeModels_SameName_SecondModelHasHigherValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name", "Value3");
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(Value1.compareTo(Value3), comparisonResult);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_NullAttributeModelName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel(null, "Value");
        AttributeModel anotherAttributeModel = new AttributeModel("Name", "Value");
        comparator.compare(attributeModel, anotherAttributeModel);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_NullAttributeModelValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("Name", null);
        AttributeModel anotherAttributeModel = new AttributeModel("Name", "Value");
        comparator.compare(attributeModel, anotherAttributeModel);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(comparator);
        ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) objInputStream.readObject();
        assertNotSame(comparator, deserializedComparator);
    }
}