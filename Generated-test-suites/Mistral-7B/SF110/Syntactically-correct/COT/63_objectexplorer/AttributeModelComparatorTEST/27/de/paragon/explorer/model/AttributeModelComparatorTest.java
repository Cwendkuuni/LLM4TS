package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");
        AttributeModel modelA = new AttributeModel("a", "ValueA");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();

        int comparisonResult = comparator.compare(model1, model2);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(modelA, model1);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(model1, modelA);
        assertEquals(1, comparisonResult);

        comparisonResult = comparator.compare(modelA, modelA);
        assertEquals(0, comparisonResult);

        comparisonResult = comparator.compare(null, model1);
        assertNull(comparisonResult);

        comparisonResult = comparator.compare(model1, null);
        assertNull(comparisonResult);

        comparisonResult = comparator.compare(null, null);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompare_NullAndNotNull() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = null;

        Comparator<AttributeModel> comparator = new AttributeModelComparator();

        int comparisonResult = comparator.compare(model1, model2);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(model2, model1);
        assertNotEquals(-1, comparisonResult);
        assertNotEquals(0, comparisonResult);
        assertNull(comparisonResult);
    }

    @Test
    public void testCompare_SameNameIgnoreCase() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("name1", "Value2");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();

        int comparisonResult = comparator.compare(model1, model2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_SameNameWithASmaller() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel modelA = new AttributeModel("a", "ValueA");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();

        int comparisonResult = comparator.compare(model1, modelA);
        assertEquals(-1, comparisonResult);

        comparisonResult = comparator.compare(modelA, model1);
        assertEquals(1, comparisonResult);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_NullModel() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = null;

        Comparator<AttributeModel> comparator = new AttributeModelComparator();

        comparator.compare(model1, model2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_NullFirstModel() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("Name2", "Value2");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();

        comparator.compare(model1, model2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_NullSecondModel() {
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = null;

        Comparator<AttributeModel> comparator = new AttributeModelComparator();

        comparator.compare(model1, model2);
    }

    @Test
    public void testCompare_EdgeCases() {
        List<AttributeModel> models = new ArrayList<>();
        models.add(new AttributeModel("zzz", "Value1"));
        models.add(new AttributeModel("AAA", "Value2"));
        models.add(new AttributeModel("a", "ValueA"));
        models.add(new AttributeModel("Name1", "Value1"));
        models.add(new AttributeModel("Name2", "Value2"));
        models.add(new AttributeModel("Name3", "Value3"));

        Collections.sort(models, new AttributeModelComparator());

        AttributeModel expectedResult = models.get(0);
        assertEquals("a", expectedResult.getName());

        expectedResult = models.get(1);
        assertEquals("AAA", expectedResult.getName());

        expectedResult = models.get(2);
        assertEquals("Name1", expectedResult.getName());

        expectedResult = models.get(3);
        assertEquals("Name2", expectedResult.getName());

        expectedResult = models.get(4);
        assertEquals("Name3", expectedResult.getName());

        expectedResult = models.get(5);
        assertEquals("zzz", expectedResult.getName());
    }
}