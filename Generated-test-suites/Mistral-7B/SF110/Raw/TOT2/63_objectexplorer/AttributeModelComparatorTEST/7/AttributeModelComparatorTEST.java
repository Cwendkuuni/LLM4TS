import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNotNullAndHaveDifferentNames_returnsCorrectValue() {
        AttributeModel attributeModel1 = new AttributeModel("attribute1");
        AttributeModel attributeModel2 = new AttributeModel("attribute2");

        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);

        assertThat(result, is(-1));
    }

    // Edge cases and exception handling scenarios will be added later
}