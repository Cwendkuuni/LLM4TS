import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareMethod() {
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = new AttributeModel("Name2");

        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);

        // Assuming that the compare method returns a negative number when the first object's name is less than the second object's name
        // and a positive number when the first object's name is greater than the second object's name
        // Testing for the basic functionality: name1 < name2
        assertEquals(-1, result);

        // Testing for the edge case: name1 = name2
        AttributeModel attributeModel3 = new AttributeModel("Name1");
        int result2 = new AttributeModelComparator().compare(attributeModel3, attributeModel3);
        assertEquals(0, result2);

        // Testing for the exception handling: o1 or o2 can be null
        AttributeModel attributeModelNull1 = null;
        AttributeModel attributeModelNull2 = new AttributeModel("Name3");
        try {
            int result3 = new AttributeModelComparator().compare(attributeModelNull1, attributeModelNull2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        AttributeModel attributeModelNull2Null1 = null;
        try {
            int result4 = new AttributeModelComparator().compare(attributeModelNull2Null1, attributeModelNull2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}