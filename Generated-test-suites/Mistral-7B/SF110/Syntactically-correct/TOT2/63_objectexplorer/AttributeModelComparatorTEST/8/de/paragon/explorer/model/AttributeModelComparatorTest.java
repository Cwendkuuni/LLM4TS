import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareMethod() {
        AttributeModel attributeModel1 = new AttributeModel("attribute1");
        AttributeModel attributeModel2 = new AttributeModel("attribute2");

        int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);

        // Assuming the comparisonResult should be -1 since attribute1's name comes before attribute2's name
        assertNegative(comparisonResult);
    }
}