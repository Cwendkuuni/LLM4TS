import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeModelComparatorTest {
    @Test
    public void testCompareMethod() {
        AttributeModel model1 = new AttributeModel("attribute1");
        AttributeModel model2 = new AttributeModel("attribute2");
        AttributeModel model3 = new AttributeModel("a");

        Comparator<AttributeModel> comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertEquals(-1, result);

        result = comparator.compare(model2, model1);
        assertEquals(1, result);

        result = comparator.compare(model1, model1);
        assertEquals(0, result);

        result = comparator.compare(model3, model1);
        assertEquals(1, result);

        result = comparator.compare(model1, model3);
        assertEquals(-1, result);
    }
}