package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    // Test method: compare(AttributeModel o1, AttributeModel o2)
    
    @Test
    public void testCompareNullValues() {
        assertNull(new AttributeModelComparator().compare(null, null));
        assertNull(new AttributeModelComparator().compare(null, new AttributeModel()));
        assertNull(new AttributeModelComparator().compare(new AttributeModel(), null));
    }

    @Test
    public void testCompareEmptyNames() {
        AttributeModel model1 = new AttributeModel();
        AttributeModel model2 = new AttributeModel();
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result == 0);
    }

    @Test
    public void testCompareSameNamesIgnoreCase() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name1");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result == 0);
    }

    @Test
    public void testCompareDifferentNamesIgnoreCase() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result != 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullAttributeModel() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("Name2");
        new AttributeModelComparator().compare(model1, model2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullAttributeModel2() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = null;
        new AttributeModelComparator().compare(model1, model2);
    }

    @Test
    public void testCompareNamesWithA() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result < 0);

        AttributeModel model3 = new AttributeModel("a");
        AttributeModel model4 = new AttributeModel("Name1");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 > 0);

        AttributeModel model5 = new AttributeModel("a");
        AttributeModel model6 = new AttributeModel("a");
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 == 0);
    }

    @Test
    public void testCompareNamesWithAFirstInBoth() {
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result == 0);

        AttributeModel model3 = new AttributeModel("Name1");
        AttributeModel model4 = new AttributeModel("a");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 < 0);

        AttributeModel model5 = new AttributeModel("a");
        AttributeModel model6 = new AttributeModel("Name1");
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 > 0);
    }

    @Test
    public void testCompareNamesWithANotFirstInBoth() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("NameWithA");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result < 0);

        AttributeModel model3 = new AttributeModel("NameWithA");
        AttributeModel model4 = new AttributeModel("Name1");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 > 0);

        AttributeModel model5 = new AttributeModel("NameWithA");
        AttributeModel model6 = new AttributeModel("NameWithA");
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 == 0);
    }

    @Test
    public void testCompareNamesWithANotFirstInOne() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("NameWithA");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result < 0);

        AttributeModel model3 = new AttributeModel("NameWithA");
        AttributeModel model4 = new AttributeModel("Name1");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 > 0);

        AttributeModel model5 = new AttributeModel("NameWithA");
        AttributeModel model6 = new AttributeModel("NameWithA");
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 == 0);
    }

    @Test
    public void testCompareNamesDifferentLengths() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("NameWithLongerName");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result < 0);

        AttributeModel model3 = new AttributeModel("NameWithLongerName");
        AttributeModel model4 = new AttributeModel("Name1");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 > 0);

        AttributeModel model5 = new AttributeModel("NameWithLongerName");
        AttributeModel model6 = new AttributeModel("NameWithLongerName");
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 == 0);
    }

    @Test
    public void testCompareNamesDifferentCases() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result < 0);

        AttributeModel model3 = new AttributeModel("Name2");
        AttributeModel model4 = new AttributeModel("Name1");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 > 0);

        AttributeModel model5 = new AttributeModel("Name1");
        AttributeModel model6 = new AttributeModel("Name1");
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 == 0);

        AttributeModel model7 = new AttributeModel("Name1");
        AttributeModel model8 = new AttributeModel("NAME1");
        int result4 = new AttributeModelComparator().compare(model7, model8);
        assertTrue(result4 == 0);
    }

    @Test
    public void testCompareNamesWithSpecialCharacters() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name_with_special_characters");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result < 0);

        AttributeModel model3 = new AttributeModel("Name_with_special_characters");
        AttributeModel model4 = new AttributeModel("Name1");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 > 0);

        AttributeModel model5 = new AttributeModel("Name_with_special_characters");
        AttributeModel model6 = new AttributeModel("Name_with_special_characters");
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 == 0);
    }

    @Test
    public void testCompareNamesWithEmptyNames() {
        AttributeModel model1 = new AttributeModel();
        AttributeModel model2 = new AttributeModel();
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result == 0);

        AttributeModel model3 = new AttributeModel();
        AttributeModel model4 = new AttributeModel("Name");
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertTrue(result2 < 0);

        AttributeModel model5 = new AttributeModel("Name");
        AttributeModel model6 = new AttributeModel();
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 > 0);
    }

    @Test
    public void testCompareNamesWithNullNames() {
        AttributeModel model1 = new AttributeModel();
        AttributeModel model2 = null;
        int result = new AttributeModelComparator().compare(model1, model2);
        assertNotSame(0, result);

        AttributeModel model3 = null;
        AttributeModel model4 = new AttributeModel();
        int result2 = new AttributeModelComparator().compare(model3, model4);
        assertNotSame(0, result2);

        AttributeModel model5 = null;
        AttributeModel model6 = null;
        int result3 = new AttributeModelComparator().compare(model5, model6);
        assertTrue(result3 == 0);
    }

    @Test
    public void testCompareSameObjects() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = model1;
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result == 0);
    }

    @Test
    public void testCompareDifferentObjectsWithSameValues() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name1");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result == 0);
    }

    @Test
    public void testCompareObjectsWithDifferentValues() {
        AttributeModel model1 = new AttributeModel("Name1");
        AttributeModel model2 = new AttributeModel("Name2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertTrue(result != 0);
    }
}