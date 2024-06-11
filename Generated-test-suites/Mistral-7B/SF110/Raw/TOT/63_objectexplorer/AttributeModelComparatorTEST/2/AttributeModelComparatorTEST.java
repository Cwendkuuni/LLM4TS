package de.paragon.explorer.model;
import org.junit.Test;
import static org.junit.Assert.*;;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_SameName_ReturnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompare_DifferentNames_ReturnsNegativeOrPositive() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int comparisonResult = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(comparisonResult < 0 || comparisonResult > 0);
    }

    @Test
    public void testCompare_NullObjects_ThrowsNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertThrows(NullPointerException.class, () -> comparator.compare(null, null));
    }

    @Test
    public void testCompare_NullObjectWithValidObject_ThrowsNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("name");
        assertThrows(NullPointerException.class, () -> comparator.compare(null, attributeModel));
    }

    @Test
    public void testCompare_ValidObjectWithNullObject_ThrowsNullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("name");
        assertThrows(NullPointerException.class, () -> comparator.compare(attributeModel, null));
    }

    
}