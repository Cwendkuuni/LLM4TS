package de.paragon.explorer.model;
import org.junit.Assert;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_whenBothObjectsAreNull_thenReturnZero() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int result = new AttributeModelComparator().compare(o1, o2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompare_whenBothObjectsHaveSameName_thenReturnZero() {
        AttributeModel o1 = new AttributeModel("name1");
        AttributeModel o2 = new AttributeModel("name1");
        int result = new AttributeModelComparator().compare(o1, o2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompare_whenFirstObjectHasNameLessThanSecondObject_thenReturnNegative() {
        AttributeModel o1 = new AttributeModel("nameA");
        AttributeModel o2 = new AttributeModel("nameB");
        int result = new AttributeModelComparator().compare(o1, o2);
        Assert.assertThat(result, lessThan(0));
    }

    @Test
    public void testCompare_whenFirstObjectHasNameGreaterThanSecondObject_thenReturnPositive() {
        AttributeModel o1 = new AttributeModel("nameC");
        AttributeModel o2 = new AttributeModel("nameA");
        int result = new AttributeModelComparator().compare(o1, o2);
        Assert.assertThat(result, greaterThan(0));
    }

    @Test
    public void testCompare_whenFirstObjectHasNameEqualToASpecialCase_thenReturnPositiveForObjectsWithNameLessThanA() {
        AttributeModel o1 = new AttributeModel("nameX");
        AttributeModel o2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(o1, o2);
        Assert.assertThat(result, lessThan(0));
    }

    @Test
    public void testCompare_whenFirstObjectHasNameEqualToASpecialCase_thenReturnNegativeForObjectsWithNameGreaterThanA() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("nameY");
        int result = new AttributeModelComparator().compare(o1, o2);
        Assert.assertThat(result, greaterThan(0));
    }
     
}