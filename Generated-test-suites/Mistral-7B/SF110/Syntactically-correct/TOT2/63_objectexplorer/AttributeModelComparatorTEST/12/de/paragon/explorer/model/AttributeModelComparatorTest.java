 
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareMethod() {
        AttributeModel model1 = new AttributeModel("attribute1");
        AttributeModel model2 = new AttributeModel("attribute2");

        int result = new AttributeModelComparator().compare(model1, model2);

        assertEquals(-1, result);
    }

    // ... test cases for edge cases and exception handling

}