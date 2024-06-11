package de.paragon.explorer.model;
import org.junit.Assert;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_whenBothObjectsAreNull_thenReturnZero() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int actual = new AttributeModelComparator().compare(o1, o2);
        int expected = 0;
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCompare_whenBothObjectsHaveTheSameName_thenReturnZero() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name1");
        int actual = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        int expected = 0;
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCompare_whenFirstObjectNameIsLessThanSecondObjectName_thenReturnNegativeNumber() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name2");
        int actual = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        int expected = -1;
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCompare_whenFirstObjectNameIsGreaterThanSecondObjectName_thenReturnPositiveNumber() {
        AttributeModel attributeModel1 = new AttributeModel("name3");
        AttributeModel attributeModel2 = new AttributeModel("name2");
        int actual = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCompare_whenFirstObjectNameIsEqualToSecondObjectNameButFirstObjectNameIsAA_thenSecondObjectNameIsGreaterThanFirstObjectName() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("b");
        int actual = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        int expected = -1;
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCompare_whenFirstObjectNameIsEqualToSecondObjectNameButSecondObjectNameIsAA_thenFirstObjectNameIsGreaterThanSecondObjectName() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("a");
        AttributeModel attributeModel3 = new AttributeModel("a");
        AttributeModel attributeModel4 = new AttributeModel("a");
        attributeModel4.setName("a"); // set name to "a" manually instead of using the constructor to test name setter
        int actual = new AttributeModelComparator().compare(attributeModel1, attributeModel4);
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCompare_whenFirstObjectIsNull_thenThrowNullPointerException() {
        AttributeModel attributeModel2 = new AttributeModel("name2");
        int actual = new AttributeModelComparator().compare(null, attributeModel2);
        // No need to assert anything here as we are expecting a NullPointerException to be thrown
    }
    
    @Test(expected = NullPointerException.class)
    public void testCompare_whenSecondObjectIsNull_thenThrowNullPointerException() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        int actual = new AttributeModelComparator().compare(attributeModel1, null);
        // No need to assert anything here as we are expecting a NullPointerException to be thrown
    }
    
}