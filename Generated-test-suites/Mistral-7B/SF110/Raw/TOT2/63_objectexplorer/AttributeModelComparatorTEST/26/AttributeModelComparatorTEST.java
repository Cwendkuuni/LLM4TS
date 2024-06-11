import static org.junit.Assert.assertEquals;
import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareMethod() {
        AttributeModel model1 = new AttributeModel("Attribute1");
        AttributeModel model2 = new AttributeModel("Attribute2");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        // This assumes that 'Attribute1' coming before 'Attribute2' in alphabetical order
        assertEquals(-1, result);
    }
}