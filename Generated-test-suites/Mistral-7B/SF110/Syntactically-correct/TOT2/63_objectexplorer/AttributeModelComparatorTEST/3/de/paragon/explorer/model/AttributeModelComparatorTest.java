import static org.junit.Assert.*;

import de.paragon.explorer.model.AttributeModel;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNotNullAndHaveDifferentNames_returnsCorrectResult() {
        // Given
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel("name2");

        // When
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);

        // Then
        assertEquals(-1, result);
    }
}