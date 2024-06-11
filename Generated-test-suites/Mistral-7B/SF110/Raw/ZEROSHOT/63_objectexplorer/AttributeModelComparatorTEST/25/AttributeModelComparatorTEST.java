package de.paragon.explorer.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("A", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("A", "Value1");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameIgnored() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameAFirst() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("A", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameASecond() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("A", "Value2");
        assertEquals(1, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameANotFirst() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("A", "Value3");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name1", "Value1");
        AttributeModel attributeModel2 = new AttributeModel("Name2", "Value2");
        assertEquals(-Name1.compareToIgnoreCase(Name2), comparator.compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCheckSerializability() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(comparator);
        out.close();
        byte[] byteArray = byteStream.toByteArray();
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream in = new ObjectInputStream(byteInStream);
        AttributeModelComparator comparatorDeserialized = (AttributeModelComparator) in.readObject();
        in.close();
        assertNotNull(comparatorDeserialized);
    }

    @Test
    public void testComparatorFunctionality() {
        List<AttributeModel> attributeModels = Arrays.asList(
                new AttributeModel("Name1", "Value1"),
                new AttributeModel("Name2", "Value2"),
                new AttributeModel("Name3", "Value3"),
                new AttributeModel("Name4", "Value4"),
                new AttributeModel("A", "ValueA"),
                new AttributeModel("Name5", "Value5")
        );
        Collections.sort(attributeModels, new AttributeModelComparator());
        List<String> expectedNames = Arrays.asList(
                "A",
                "Name1",
                "Name2",
                "Name3",
                "Name4",
                "Name5"
        );
        List<String> actualNames = new ArrayList<>();
        for (AttributeModel attributeModel : attributeModels) {
            actualNames.add(attributeModel.getName());
        }
        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testCompareNullAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = null;
        assertNull(comparator.compare(attributeModel, new AttributeModel("Name1", "Value1")));
        assertNull(comparator.compare(new AttributeModel("Name1", "Value1"), attributeModel));
    }
}