import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNotNullWithDifferentNames_thenCorrectlyReturnsLessThanZero() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        int comparisonResult = new AttributeModelComparator().compare(model1, model2);
        assertTrue(comparisonResult < 0);
    }

    // Edge case and exception handling test cases can be added here
}