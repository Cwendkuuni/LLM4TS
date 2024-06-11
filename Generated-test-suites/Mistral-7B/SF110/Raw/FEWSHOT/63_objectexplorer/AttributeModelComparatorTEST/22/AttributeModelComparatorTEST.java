package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        AttributeModel attrModel1 = new AttributeModel("name1", "value1");
        AttributeModel attrModel2 = new AttributeModel("name1", "value1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(attrModel1, attrModel2));
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameName() {
        AttributeModel attrModel1 = new AttributeModel("name1", "value1");
        AttributeModel attrModel2 = new AttributeModel("name2", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attrModel1, attrModel2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameButFirstOneFirst() {
        AttributeModel attrModel1 = new AttributeModel("a", "value1");
        AttributeModel attrModel2 = new AttributeModel("name1", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameButSecondOneFirst() {
        AttributeModel attrModel1 = new AttributeModel("name1", "value1");
        AttributeModel attrModel2 = new AttributeModel("a", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNames() {
        AttributeModel attrModel1 = new AttributeModel("name1", "value1");
        AttributeModel attrModel2 = new AttributeModel("name2", "value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attrModel1, attrModel2);
        assertNotEquals(0, result);
    }
}