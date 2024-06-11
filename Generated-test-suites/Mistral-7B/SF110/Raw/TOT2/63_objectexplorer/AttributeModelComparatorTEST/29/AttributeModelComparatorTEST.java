
package de.paragon.explorer.model.test;

import org.junit.Test;
import static org.junit.Assert.*;
import de.paragon.explorer.model.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareWithNullAttributes() {
        AttributeModel nullAttribute1 = null;
        AttributeModel nullAttribute2 = null;
        int result = new AttributeModelComparator().compare(nullAttribute1, nullAttribute2);
        assertEquals(0, result);
    }

    // ... (The other test methods from the combined tests go here)

    @Test
    public void testCompareWithAttributesContainingNullName() {
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel("attributeName");
        int result = new AttributeModelComparator().compare(attribute1, attribute2);
        assertEquals(-1, result);
    }

}
