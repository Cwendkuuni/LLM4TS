package de.paragon.explorer.model;
import org.junit.Test;
import static org.junit.Assert.*;;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_whenBothObjectsNull_thenReturnZero() {
        AttributeModel nullAttributeModel1 = null;
        AttributeModel nullAttributeModel2 = null;
        int comparisonResult = new AttributeModelComparator().compare(nullAttributeModel1, nullAttributeModel2);
        assertEquals(0, comparisonResult);
    }
    

    @Test
    public void testCompare_whenBothObjectsHaveSameName_thenReturnZero() {
        String name = "testName";
        AttributeModel attributeModel1 = new AttributeModel(name);
        AttributeModel attributeModel2 = new AttributeModel(name);
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }
    

    @Test
    public void testCompare_whenFirstObjectNameLessThanSecondObjectName_thenReturnNegativeNumber() {
        String name1 = "name1";
        String name2 = "name2";
        AttributeModel attributeModel1 = new AttributeModel(name1);
        AttributeModel attributeModel2 = new AttributeModel(name2);
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertFalse(comparisonResult > 0);
        assertTrue(comparisonResult < 0);
    }
    

    @Test
    public void testCompare_whenFirstObjectNameGreaterThanSecondObjectName_thenReturnPositiveNumber() {
        String name1 = "nameA";
        String name2 = "nameB";
        AttributeModel attributeModel1 = new AttributeModel(name1);
        AttributeModel attributeModel2 = new AttributeModel(name2);
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult > 0);
    }
    

    @Test
    public void testCompare_whenFirstObjectNameEqualsSecondObjectNameAndFirstObjectNameIsAA_thenSecondObjectNameComesFirst() {
        String name1 = "a";
        String name2 = "test";
        AttributeModel attributeModel1 = new AttributeModel(name1);
        AttributeModel attributeModel2 = new AttributeModel(name2);
        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0);
    }
    
}