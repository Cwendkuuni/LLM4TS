import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompareMethod() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult < 0);
        model1 = new AttributeModel("name1");
        model2 = new AttributeModel("name1");
        compareResult = comparator.compare(model1, model2);
        assertEquals(0, compareResult);
        model1 = new AttributeModel("a");
        model2 = new AttributeModel("name2");
        compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult > 0);
    }

    @Test
    public void testCompareMethodWithSameName() {
        AttributeModel model1 = new AttributeModel("name");
        AttributeModel model2 = new AttributeModel("name");
        int compareResult = comparator.compare(model1, model2);
        assertEquals(0, compareResult);
    }

    @Test
    public void testCompareMethodWithSameNameAndOneIsA() {
        AttributeModel model1 = new AttributeModel("name");
        AttributeModel model2 = new AttributeModel("a");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult < 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithNullObjects() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("name2");
        comparator.compare(model1, model2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithNullObjectsAndNullComparator() {
        AttributeModel model1 = null;
        AttributeModel model2 = null;
        AttributeModelComparator comparator = null;
        comparator = new AttributeModelComparator();
        comparator.compare(model1, model2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithSameNameAndOneIsAAndNullComparator() {
        AttributeModel model1 = new AttributeModel("name");
        AttributeModel model2 = new AttributeModel("a");
        AttributeModelComparator comparator = null;
        comparator = new AttributeModelComparator();
        comparator.compare(model1, model2);
    }

    @Test
    public void testCompareMethodWithDifferentNames() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult < 0);
    }

    @Test
    public void testCompareMethodWithDifferentNamesAndOneIsA() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("a");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult > 0);
    }

    @Test
    public void testCompareMethodWithDifferentNamesAndBothAreA() {
        AttributeModel model1 = new AttributeModel("a");
        AttributeModel model2 = new AttributeModel("a");
        int compareResult = comparator.compare(model1, model2);
        assertEquals(0, compareResult);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithNullObject1() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("name2");
        comparator.compare(model1, model2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithNullObject2() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = null;
        comparator.compare(model1, model2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithNullObjectsAndBothAreNull() {
        AttributeModel model1 = null;
        AttributeModel model2 = null;
        comparator.compare(model1, model2);
    }

    @Test
    public void testCompareMethodWithEmptystring() {
        AttributeModel model1 = new AttributeModel("");
        AttributeModel model2 = new AttributeModel("name");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult < 0);
    }

    @Test
    public void testCompareMethodWithEmptystringAndA() {
        AttributeModel model1 = new AttributeModel("");
        AttributeModel model2 = new AttributeModel("a");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult > 0);
    }

    @Test
    public void testCompareMethodWithBothAreEmptyString() {
        AttributeModel model1 = new AttributeModel("");
        AttributeModel model2 = new AttributeModel("");
        int compareResult = comparator.compare(model1, model2);
        assertEquals(0, compareResult);
    }

    @Test
    public void testCompareMethodWithEmptyStringAndName() {
        AttributeModel model1 = new AttributeModel("");
        AttributeModel model2 = new AttributeModel("name");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult < 0);
    }

    @Test
    public void testCompareMethodWithNameAndEmptyString() {
        AttributeModel model1 = new AttributeModel("name");
        AttributeModel model2 = new AttributeModel("");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult > 0);
    }

    @Test
    public void testCompareMethodWithNullComparator() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        AttributeModelComparator comparator = null;
        int compareResult = comparator.compare(model1, model2);
        fail("Expected NullPointerException, but got: " + compareResult);
    }

    @Test
    public void testCompareMethodWithDistinctObjects() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        int compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult < 0);

        model1 = new AttributeModel("name3");
        model2 = new AttributeModel("name2");
        compareResult = comparator.compare(model1, model2);
        assertTrue(compareResult > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareMethodWithNullComparatorAndNullObjects() {
        AttributeModel model1 = null;
        AttributeModel model2 = null;
        AttributeModelComparator comparator = null;
        comparator = new AttributeModelComparator();
        comparator.compare(model1, model2);
    }

    @Test
    public void testCompareMethodWithNullComparatorAndDifferentObjects() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        AttributeModelComparator comparator = null;
        int compareResult = comparator.compare(model1, model2);
        fail("Expected IllegalArgumentException, but got: " + compareResult);
    }

    @Test
    public void testCompareMethodWithNullComparatorAndSameObjects() {
        AttributeModel model1 = new AttributeModel("name");
        AttributeModel model2 = new AttributeModel("name");
        AttributeModelComparator comparator = null;
        int compareResult = comparator.compare(model1, model2);
        assertEquals(0, compareResult);
    }

    @Test
    public void testCompareMethodWithListOfObjects() {
        List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("name1"), new AttributeModel("name2"),
                new AttributeModel("name3"), new AttributeModel("name4"), new AttributeModel("name5"));
        Collections.sort(attributeModels, comparator);
        assertEquals("name1, name2, name3, name4, name5", Arrays.toString(attributeModels.toArray()));
    }

    @Test
    public void testCompareMethodWithListOfObjectsReversed() {
        List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("name5"), new AttributeModel("name4"),
                new AttributeModel("name3"), new AttributeModel("name2"), new AttributeModel("name1"));
        Collections.sort(attributeModels, Collections.reverseOrder(comparator));
        assertEquals("name5, name4, name3, name2, name1", Arrays.toString(attributeModels.toArray()));
    }

    @Test
    public void testCompareMethodWithListOfObjectsNullComparator() {
        List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("name1"), new AttributeModel("name2"),
                new AttributeModel("name3"), new AttributeModel("name4"), new AttributeModel("name5"));
        AttributeModelComparator comparator = null;
        try {
            Collections.sort(attributeModels, comparator);
            fail("Expected NullPointerException, but got: sorted list");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCompareMethodWithListOfObjectsNullObjects() {
        List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("name1"), null, null,
                new AttributeModel("name4"), new AttributeModel("name5"));
        AttributeModelComparator comparator = new AttributeModelComparator();
        try {
            Collections.sort(attributeModels, comparator);
            fail("Expected NullPointerException, but got: sorted list");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}