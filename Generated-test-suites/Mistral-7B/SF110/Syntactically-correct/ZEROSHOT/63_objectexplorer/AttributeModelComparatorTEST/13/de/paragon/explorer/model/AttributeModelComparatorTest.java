package de.paragon.explorer.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;
import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        Assert.assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareBothNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        Assert.assertEquals(0, comparator.compare(null, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute1", "Value1");
        Assert.assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Attribute2", "Value2");
        Assert.assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNamesIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("AttributeA", "ValueA");
        AttributeModel attributeModel2 = new AttributeModel("AttributeB", "ValueB");
        Assert.assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));

        AttributeModel attributeModel3 = new AttributeModel("AttributeB", "ValueB");
        AttributeModel attributeModel4 = new AttributeModel("AttributeA", "ValueA");
        Assert.assertEquals(1, comparator.compare(attributeModel3, attributeModel4));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameButOneIsA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("AttributeA", "ValueA");
        Assert.assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));

        AttributeModel attributeModel3 = new AttributeModel("AttributeA", "ValueA");
        AttributeModel attributeModel4 = new AttributeModel("Attribute1", "Value1");
        Assert.assertEquals(1, comparator.compare(attributeModel3, attributeModel4));
    }

    @Test
    public void testSerializability() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStreamOS = new ByteArrayOutputStream();
        ObjectOutputStream objStreamOS = new ObjectOutputStream(byteStreamOS);
        try {
            objStreamOS.writeObject(comparator);
        } catch (IOException e) {
            fail("Failed to serialize AttributeModelComparator: " + e.getMessage());
        }
        byte[] byteArray = byteStreamOS.toByteArray();
        ByteArrayInputStream byteStreamIS = new ByteArrayInputStream(byteArray);
        ObjectInputStream objStreamIS = null;
        try {
            objStreamIS = new ObjectInputStream(byteStreamIS);
            AttributeModelComparator deserializedComparator = (AttributeModelComparator) objStreamIS.readObject();
            objStreamIS.close();
            byteStreamIS.close();
            Assert.assertNotNull(deserializedComparator);
        } catch (IOException | ClassNotFoundException e) {
            fail("Failed to deserialize AttributeModelComparator: " + e.getMessage());
        }
        byteStreamIS.close();
    }

    @Test
    public void testComparatorWithDifferentObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertNull(comparator.compare(obj1, obj2));
    }
}