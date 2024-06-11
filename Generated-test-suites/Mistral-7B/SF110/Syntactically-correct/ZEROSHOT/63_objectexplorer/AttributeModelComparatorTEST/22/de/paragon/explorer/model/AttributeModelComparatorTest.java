package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
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
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNames() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute2", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameButDifferentValues() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute1", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameAndSameValue() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameAFirst() {
        AttributeModel a1 = new AttributeModel("A", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute1", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameAAfter() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("A", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameAAfterAndValueAFirst() {
        AttributeModel a1 = new AttributeModel("Attribute1", "A");
        AttributeModel a2 = new AttributeModel("A", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameAAfterAndValueASame() {
        AttributeModel a1 = new AttributeModel("Attribute1", "A");
        AttributeModel a2 = new AttributeModel("A", "A");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameAAfterAndValueABefore() {
        AttributeModel a1 = new AttributeModel("Attribute1", "A");
        AttributeModel a2 = new AttributeModel("A", "B");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithNameAAfterAndValueASameAndDifferentValues() {
        AttributeModel a1 = new AttributeModel("Attribute1", "A");
        AttributeModel a2 = new AttributeModel("A", "B");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareNullWithValidAttributeModel() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, a1);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareValidAttributeModelWithNull() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, null);
        assertTrue(result > 0);
    }

    @Test
    public void testComparingEqualAttributeModels() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = a1;

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertEquals(0, result);
    }

    @Test
    public void testComparingEqualsAttributeModelsWithDifferentNames() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute2", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }

    @Test
    public void testComparingUnequalAttributeModelsWithDifferentValues() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute1", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }

    @Test
    public void testComparingUnequalAttributeModelsWithSameValues() {
        AttributeModel a1 = new AttributeModel("Attribute1", "Value1");
        AttributeModel a2 = new AttributeModel("Attribute1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertEquals(0, result);
    }

    @Test
    public void testComparatorSortingList() {
        List<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(new AttributeModel("Attribute5", "Value5"));
        attributeModels.add(new AttributeModel("Attribute1", "Value1"));
        attributeModels.add(new AttributeModel("Attribute3", "Value3"));
        attributeModels.add(new AttributeModel("Attribute2", "Value2"));
        attributeModels.add(new AttributeModel("A", "ValueA"));

        Collections.sort(attributeModels, new AttributeModelComparator());

        AttributeModel firstAttributeModel = attributeModels.get(0);
        assertEquals("A", firstAttributeModel.getName());

        AttributeModel lastAttributeModel = attributeModels.get(attributeModels.size() - 1);
        assertEquals("Attribute5", lastAttributeModel.getName());

        for (int i = 1; i < attributeModels.size(); i++) {
            AttributeModel currentAttributeModel = attributeModels.get(i);
            AttributeModel previousAttributeModel = attributeModels.get(i - 1);

            int comparisonResult = new AttributeModelComparator().compare(currentAttributeModel, previousAttributeModel);
            if (i == 1) {
                assertTrue(comparisonResult < 0);
            } else {
                assertTrue(comparisonResult > 0);
            }
        }
    }
}