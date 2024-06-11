package de.paragon.explorer.model.test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompareWithNullObjects() {
        AttributeModel nullAttributeModel1 = null;
        AttributeModel nullAttributeModel2 = null;
        int result = new AttributeModelComparator().compare(nullAttributeModel1, nullAttributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithEqualObjects() {
        AttributeModel attributeModel1 = new AttributeModel("attribute1");
        AttributeModel attributeModel2 = new AttributeModel("attribute1");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithDifferentNamesIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = new AttributeModel("attribute2");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithDifferentNamesAndAName() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareWithDifferentNamesAndANameReverse() {
        AttributeModel attributeModel1 = new AttributeModel("Attribute1");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attributeModel2, attributeModel1);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithEmptyNames() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithNullNameInFirstObject() {
        AttributeModel attributeModel1 = new AttributeModel(null);
        AttributeModel attributeModel2 = new AttributeModel("name2");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithNullNameInSecondObject() {
        AttributeModel attributeModel1 = new AttributeModel("name1");
        AttributeModel attributeModel2 = new AttributeModel(null);
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareWithEmptyNameAndAName() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareWithEmptyNameAndANameReverse() {
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attributeModel2, attributeModel1);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithLongNames() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeNameWithALotOfCharacters1");
        AttributeModel attributeModel2 = new AttributeModel("AttributeNameWithALotOfCharacters2");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        // Assert that the comparison is in the correct order based on the length of the names
        if (attributeModel1.getName().length() > attributeModel2.getName().length()) {
            assertEquals(-1, result);
        } else if (attributeModel1.getName().length() < attributeModel2.getName().length()) {
            assertEquals(1, result);
        } else {
            assertEquals(0, result);
        }
    }

    @Test
    public void testCompareWithSameName() {
        AttributeModel attributeModel1 = new AttributeModel("sameName");
        AttributeModel attributeModel2 = new AttributeModel("sameName");
        int result = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithNullObject() {
        AttributeModel nullAttributeModel = null;
        AttributeModel attributeModel = new AttributeModel("name");
        int result = new AttributeModelComparator().compare(nullAttributeModel, attributeModel);
        assertEquals(-1, result); // Null objects should be treated as less than any other object
    }
}