package de.paragon.explorer.model.test;

import org.junit.Assert;
import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompareWithBothNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testCompareWithBothNotNullAndSameName() {
        AttributeModel o1 = new AttributeModel("testName");
        AttributeModel o2 = new AttributeModel("testName");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testCompareWithBothNotNullAndDifferentNames() {
        AttributeModel o1 = new AttributeModel("testName1");
        AttributeModel o2 = new AttributeModel("testName2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertThat(result, lessThan(0)); // or Assert.assertTrue(result < 0)
    }
    
    @Test
    public void testCompareWithNullAndNotNull() {
        AttributeModel o1 = new AttributeModel("testName");
        AttributeModel o2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertTrue(result < 0);
    }
    
    @Test
    public void testCompareWithSameNameAndAAfterAnother() {
        AttributeModel o1 = new AttributeModel("testName");
        AttributeModel o2 = new AttributeModel("a");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertTrue(result < 0);
    }

    @Test
    public void testCompareWithAAfterAnotherAndSameName() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("testName");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertTrue(result > 0);
    }
}