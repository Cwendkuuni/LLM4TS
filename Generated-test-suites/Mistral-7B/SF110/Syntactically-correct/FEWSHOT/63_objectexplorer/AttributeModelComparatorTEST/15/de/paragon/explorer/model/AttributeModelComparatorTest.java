package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_nullAttributeModels() {
        assertNull(new AttributeModelComparator().compare(null, null));
    }

    @Test
    public void testCompare_sameAttributeModels() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        assertEquals(0, new AttributeModelComparator().compare(attributeModel1, attributeModel2));
    }

    @Test
    public void testCompare_differentAttributeModelsWithSameName() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        assertEquals(-1, new AttributeModelComparator().compare(attributeModel1, attributeModel2));

        AttributeModel attributeModel3 = new AttributeModel("name2", "value2");
        AttributeModel attributeModel4 = new AttributeModel("name1", "value1");
        assertEquals(1, new AttributeModelComparator().compare(attributeModel3, attributeModel4));
    }

    @Test
    public void testCompare_differentAttributeModelsWithDifferentNames() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
        assertEquals(-1, new AttributeModelComparator().compare(attributeModel1, attributeModel2));

        AttributeModel attributeModel3 = new AttributeModel("name3", "value3");
        AttributeModel attributeModel4 = new AttributeModel("name1", "value1");
        assertEquals(1, new AttributeModelComparator().compare(attributeModel3, attributeModel4));

        AttributeModel attributeModel5 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel6 = new AttributeModel("name1", "value2");
        assertNotEquals(0, new AttributeModelComparator().compare(attributeModel5, attributeModel6));
    }

    @Test
    public void testCompare_differentAttributeModelsWithNameA() {
        AttributeModel attributeModel1 = new AttributeModel("a", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
        assertEquals(-1, new AttributeModelComparator().compare(attributeModel1, attributeModel2));

        AttributeModel attributeModel3 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel4 = new AttributeModel("a", "value1");
        assertEquals(1, new AttributeModelComparator().compare(attributeModel3, attributeModel4));
    }

    @Test
    public void testCompare_differentAttributeModelsWithNameANotFirst() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("a", "value1");
        assertEquals(-1, new AttributeModelComparator().compare(attributeModel1, attributeModel2));

        AttributeModel attributeModel3 = new AttributeModel("a", "value1");
        AttributeModel attributeModel4 = new AttributeModel("name1", "value1");
        assertEquals(1, new AttributeModelComparator().compare(attributeModel3, attributeModel4));

        AttributeModel attributeModel5 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel6 = new AttributeModel("name1", "a");
        assertEquals(0, new AttributeModelComparator().compare(attributeModel5, attributeModel6));
    }

    @Test
    public void testCompare_equalNamesButDifferentValues() {
        AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
        AttributeModel attributeModel2 = new AttributeModel("name1", "value2");
        assertEquals(-1, new AttributeModelComparator().compare(attributeModel1, attributeModel2));

        AttributeModel attributeModel3 = new AttributeModel("name1", "value2");
        AttributeModel attributeModel4 = new AttributeModel("name1", "value1");
        assertEquals(1, new AttributeModelComparator().compare(attributeModel3, attributeModel4));
    }
}