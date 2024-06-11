package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class AttributeModelComparatorTest {

    private AttributeModel attributeModel1;
    private AttributeModel attributeModel2;

    public AttributeModelComparatorTest(String name1, String name2) {
        attributeModel1 = new AttributeModel(A_AS_STRING, name1);
        attributeModel2 = new AttributeModel(A_AS_STRING, name2);
    }

    @Parameters(name = "{index}: compare({0}) and ({1})")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Name1", "Name1"},
                {"Name2", "Name1"},
                {"Name1", "Name2"},
                {"NameA", "NameA"},
                {"NameA", "nameA"},
                {"NameZ", "NameA"},
                {"null", "NameA"},
                {null, null}
        });
    }

    @Test
    public void testCompare() {
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);

        if (comparisonResult < 0) {
            assertEquals("Expected name1 to come before name2", comparisonResult, -1);
        } else if (comparisonResult > 0) {
            assertEquals("Expected name2 to come before name1", comparisonResult, 1);
        } else {
            assertNull("Expected null as a result of comparing the same objects", comparisonResult);
        }
    }

    @Test
    public void testCompareWithNull() {
        AttributeModel nullAttributeModel = null;
        int comparisonResult = comparator.compare(attributeModel1, nullAttributeModel);

        if (comparisonResult < 0) {
            assertNotEquals("Expected name1 to not be less than a null object", comparisonResult, -1);
        }

        comparisonResult = comparator.compare(nullAttributeModel, attributeModel1);

        if (comparisonResult > 0) {
            assertNotEquals("Expected a null object to not be greater than name1", comparisonResult, 1);
        }
    }

    @Test
    public void testCompareWithSameName() {
        int comparisonResult = comparator.compare(attributeModel1, attributeModel1);

        assertNull("Expected null as a result of comparing the same objects", comparisonResult);
    }

    @Test
    public void testCompareWithNameAFirst() {
        int comparisonResult = comparator.compare(new AttributeModel(A_AS_STRING, "A"), attributeModel1);

        assertEquals("Expected name A to come before name1", comparisonResult, 1);
    }

    @Test
    public void testCompareWithNameZFirst() {
        AttributeModel attributeModelZ = new AttributeModel(A_AS_STRING, "Z");
        int comparisonResult = comparator.compare(attributeModelZ, attributeModel1);

        assertEquals("Expected name Z to come before name1", comparisonResult, -1);
    }

    @Test
    public void testCompareWithNameANotIgnoreCase() {
        AttributeModel attributeModelANotIgnoreCase = new AttributeModel(A_AS_STRING, "a");
        int comparisonResult = comparator.compare(attributeModel1, attributeModelANotIgnoreCase);

        assertEquals("Expected name1 to come before name a (case-insensitive comparison)", comparisonResult, -1);
    }

    @Test
    public void testCompareWithNameANotIgnoreCaseReverse() {
        AttributeModel attributeModelANotIgnoreCase = new AttributeModel(A_AS_STRING, "a");
        int comparisonResult = comparator.compare(attributeModelANotIgnoreCase, attributeModel1);

        assertEquals("Expected name a to come before name1 (case-insensitive comparison)", comparisonResult, 1);
    }

    private Comparator<AttributeModel> comparator = new AttributeModelComparator();
}