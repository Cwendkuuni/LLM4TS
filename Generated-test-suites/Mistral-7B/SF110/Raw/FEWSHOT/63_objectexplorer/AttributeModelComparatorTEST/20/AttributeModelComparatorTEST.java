package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testNullComparison() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull(comparator.compare(null, null));
        assertNull(comparator.compare(null, new AttributeModel()));
        assertNull(comparator.compare(new AttributeModel(), null));
    }

    @Test
    public void testCompareSameNames() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "Value2");
        int comparisonResult = new AttributeModelComparator().compare(model1, model2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareDifferentNamesIgnoringCase() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        int comparisonResult = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, comparisonResult);

        AttributeModel model3 = new AttributeModel("Name1", "Value1");
        AttributeModel model4 = new AttributeModel("NameA", "ValueA");
        int comparisonResult2 = new AttributeModelComparator().compare(model3, model4);
        assertEquals(-1, comparisonResult2);
    }

    @Test
    public void testCompareSameNameWithAFirst() {
        AttributeModel model1 = new AttributeModel("a", "Value1");
        AttributeModel model2 = new AttributeModel("Name1", "Value2");
        int comparisonResult = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, comparisonResult);

        AttributeModel model3 = new AttributeModel("Name1", "Value1");
        AttributeModel model4 = new AttributeModel("a", "ValueA");
        int comparisonResult2 = new AttributeModelComparator().compare(model3, model4);
        assertEquals(1, comparisonResult2);
    }

    @Test
    public void testCompareMultipleModels() {
        List<AttributeModel> models = new ArrayList<>();
        models.add(new AttributeModel("Name1", "Value1"));
        models.add(new AttributeModel("Name2", "Value2"));
        models.add(new AttributeModel("Name3", "Value3"));
        models.add(new AttributeModel("a", "ValueA"));

        Collections.sort(models, new AttributeModelComparator());

        AttributeModel first = models.get(0);
        assertEquals("a", first.getName());

        AttributeModel last = models.get(models.size() - 1);
        assertEquals("Name3", last.getName());

        AttributeModel name1 = models.get(1);
        assertEquals("Name1", name1.getName());

        AttributeModel name2 = models.get(2);
        assertEquals("Name2", name2.getName());
    }
}