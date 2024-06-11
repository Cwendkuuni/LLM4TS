import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompareMethodWithSameNames() {
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = new AttributeModel("Name1");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }
}