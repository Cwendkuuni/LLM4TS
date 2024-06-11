package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {
    private static final AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
    private static final AttributeModel attributeModel2 = new AttributeModel("name2", "value2");
    private static final AttributeModel attributeModelA = new AttributeModel("a", null);

    @Test
    public void testCompareNullAttributeModels() {
        assertNull(AttributeModelComparator.getInstance().compare(null, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentAttributeModels() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
        assertNotEquals(0, comparisonResult);

        String name1 = attributeModel1.getName();
        String name2 = attributeModel2.getName();

        if (name1.compareToIgnoreCase(name2) < 0) {
            assertTrue(comparisonResult < 0);
        } else if (name1.compareToIgnoreCase(name2) > 0) {
            assertTrue(comparisonResult > 0);
        } else {
            assertNotEquals("a", name1);
            assertNotEquals("a", name2);

            if (attributeModel1.getName().compareToIgnoreCase("a") < 0 && attributeModel2.getName().compareToIgnoreCase("a") >= 0) {
                assertTrue(comparisonResult > 0);
            } else if (attributeModel1.getName().compareToIgnoreCase("a") > 0 && attributeModel2.getName().compareToIgnoreCase("a") < 0) {
                assertTrue(comparisonResult < 0);
            } else {
                // Both names are equal and not "a", so test for null values
                assertNull(attributeModel1.getValue());
                assertNull(attributeModel2.getValue());

                comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, attributeModel2);
                assertEquals(0, comparisonResult);
            }
        }
    }

    @Test
    public void testCompareAttributeModelWithNullName() {
        AttributeModel nullNameAttributeModel = new AttributeModel(null, "value3");
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, nullNameAttributeModel);
        assertNotEquals(0, comparisonResult);

        String name1 = attributeModel1.getName();

        if (name1.compareToIgnoreCase(null) < 0) {
            assertTrue(comparisonResult > 0);
        } else {
            assertNotEquals("a", name1);

            if (name1.compareToIgnoreCase("a") < 0 && "a".compareToIgnoreCase(null) >= 0) {
                assertTrue(comparisonResult < 0);
            } else if (name1.compareToIgnoreCase("a") > 0 && "a".compareToIgnoreCase(null) < 0) {
                assertTrue(comparisonResult > 0);
            } else {
                // Both names are equal and not "a", so test for null values
                assertNull(attributeModel1.getValue());
                assertNotNull(nullNameAttributeModel.getValue());

                comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, nullNameAttributeModel);
                assertEquals(-1, comparisonResult);
            }
        }
    }

    @Test
    public void testCompareAttributeModelWithNullValue() {
        AttributeModel nullValueAttributeModel = new AttributeModel("name3", null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, nullValueAttributeModel);
        assertNotEquals(0, comparisonResult);

        String name1 = attributeModel1.getName();

        if (name1.compareToIgnoreCase(null) < 0) {
            assertTrue(comparisonResult > 0);
        } else if (name1.compareToIgnoreCase("a") < 0) {
            assertTrue(comparisonResult < 0);
        } else if (name1.compareToIgnoreCase("a") > 0) {
            assertTrue(comparisonResult > 0);
        } else {
            // Both names are equal and not "a", so test for null values
            assertNull(attributeModel1.getValue());
            assertNull(nullValueAttributeModel.getValue());

            comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, nullValueAttributeModel);
            assertEquals(0, comparisonResult);
        }
    }

    @Test
    public void testCompareAttributeModelWithSameNameAndNullValues() {
        AttributeModel nullValueAttributeModel = new AttributeModel("name4", null);
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModelA, nullValueAttributeModel);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareAttributeModelWithSameNameAndDifferentValues() {
        AttributeModel differentValueAttributeModel = new AttributeModel("name5", "value4");
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModelA, differentValueAttributeModel);
        assertNotEquals(0, comparisonResult);

        comparisonResult = AttributeModelComparator.getInstance().compare(differentValueAttributeModel, attributeModelA);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareAttributeModelWithNullAttributeModel() {
        int comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, null);
        assertNotEquals(0, comparisonResult);

        String name1 = attributeModel1.getName();

        if (name1.compareToIgnoreCase(null) < 0) {
            assertTrue(comparisonResult > 0);
        } else if (name1.compareToIgnoreCase("a") < 0) {
            assertTrue(comparisonResult > 0);
        } else {
            // Both names are equal and not "a", so test for null values
            assertNotNull(attributeModel1.getValue());

            comparisonResult = AttributeModelComparator.getInstance().compare(attributeModel1, null);
            assertEquals(-1, comparisonResult);
        }
    }

    @Test
    public void testCompareAttributeModelsWithNullFirst() {
        AttributeModel nullAttributeModel = new AttributeModel(null, "value6");
        int comparisonResult = AttributeModelComparator.getInstance().compare(nullAttributeModel, attributeModel1);
        assertNotEquals(0, comparisonResult);

        if (attributeModel1.getName() != null) {
            String name1 = attributeModel1.getName();

            if (name1.compareToIgnoreCase(null) < 0) {
                assertTrue(comparisonResult < 0);
            } else if (name1.compareToIgnoreCase("a") < 0) {
                assertTrue(comparisonResult < 0);
            } else {
                // Both names are equal and not "a", so test for null values
                assertNotNull(attributeModel1.getValue());

                comparisonResult = AttributeModelComparator.getInstance().compare(nullAttributeModel, attributeModel1);
                assertEquals(-1, comparisonResult);
            }
        }
    }

    @Test
    public void testCompareMultipleAttributeModels() {
        AttributeModel attributeModel3 = new AttributeModel("name6", "value3");
        AttributeModel attributeModel4 = new AttributeModel("name7", "value7");
        AttributeModel attributeModel5 = new AttributeModel("name8", "value8");

        List<AttributeModel> attributeModels = Arrays.asList(attributeModel1, attributeModel2, attributeModel3, attributeModel4, attributeModel5);
        Collections.sort(attributeModels, AttributeModelComparator.getInstance());

        AttributeModel expectedOrder = attributeModelA;
        for (AttributeModel attributeModel : attributeModels) {
            if (expectedOrder != null) {
                String nameExpectedOrder = expectedOrder.getName();
                String nameCurrent = attributeModel.getName();

                if (nameExpectedOrder.compareToIgnoreCase(nameCurrent) < 0) {
                    continue;
                } else if (nameExpectedOrder.compareToIgnoreCase(nameCurrent) > 0) {
                    fail("Failed to sort: Expected " + nameExpectedOrder + " to come before " + nameCurrent);
                }

                expectedOrder = null;
            }

            if (nameCurrent.compareToIgnoreCase("a") < 0) {
                continue;
            }

            if (nameCurrent.compareToIgnoreCase("a") > 0 && expectedOrder == null) {
                expectedOrder = attributeModel;
            } else if (nameCurrent.compareToIgnoreCase("a") > 0 && expectedOrder != null) {
                fail("Failed to sort: Expected " + expectedOrder.getName() + " to come before " + nameCurrent);
            }
        }

        if (expectedOrder != null) {
            fail("Failed to sort: Expected " + expectedOrder.getName() + " to be in the list");
        }
    }
}