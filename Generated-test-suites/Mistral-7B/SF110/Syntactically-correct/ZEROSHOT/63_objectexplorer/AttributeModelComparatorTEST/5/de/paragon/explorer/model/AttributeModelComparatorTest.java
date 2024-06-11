package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNulls() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
        assertNull(comparator.compare(null, new AttributeModel("test")));
        assertNull(comparator.compare(new AttributeModel("test"), null));
    }

    @Test
    public void testCompareSameNames() {
        AttributeModel attr1 = new AttributeModel("test");
        AttributeModel attr2 = new AttributeModel("test");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attr1, attr2);
        assertTrue(result == 0);
    }

    @Test
    public void testCompareDifferentNamesIgnoringCase() {
        AttributeModel attr1 = new AttributeModel("Test");
        AttributeModel attr2 = new AttributeModel("test");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attr1, attr2);
        assertTrue(result < 0);

        AttributeModel attr3 = new AttributeModel("test");
        AttributeModel attr4 = new AttributeModel("A");
        AttributeModel attr5 = new AttributeModel("a");
        AttributeModel attr6 = new AttributeModel("AttributeModelComparator");
        Comparator<AttributeModel> comparatorInstance = Collections.reverseOrder(new AttributeModelComparator());
        List<AttributeModel> attributeModels = new ArrayList<>(List.of(attr3, attr4, attr5, attr1, attr2, attr6));
        Collections.sort(attributeModels, comparatorInstance);

        AttributeModel expected = attributeModels.get(0);
        AttributeModel actual = attr6;
        assertTrue(expected.getName().compareToIgnoreCase(actual.getName()) == 0);

        AttributeModel expected2 = attributeModels.get(1);
        AttributeModel actual2 = attr5;
        assertTrue(expected2.getName().compareToIgnoreCase(actual2.getName()) < 0);

        AttributeModel expected3 = attributeModels.get(2);
        AttributeModel actual3 = attr4;
        assertTrue(expected3.getName().compareToIgnoreCase(actual3.getName()) < 0);

        AttributeModel expected4 = attributeModels.get(3);
        AttributeModel actual4 = attr1;
        assertTrue(expected4.getName().compareToIgnoreCase(actual4.getName()) > 0);

        AttributeModel expected5 = attributeModels.get(4);
        AttributeModel actual5 = attr2;
        assertTrue(expected5.getName().compareToIgnoreCase(actual5.getName()) > 0);

        AttributeModel expected6 = attributeModels.get(5);
        AttributeModel actual6 = attr3;
        assertTrue(comparator.compare(expected6, expected5) < 0);
    }

    @Test
    public void testCompareWithSpecialName() {
        AttributeModel attr1 = new AttributeModel("test");
        AttributeModel attr2 = new AttributeModel("a");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attr1, attr2);
        assertTrue(result < 0);

        result = comparator.compare(attr2, attr1);
        assertTrue(result > 0);

        result = comparator.compare(attr1, attr1);
        assertTrue(result == 0);
    }
}