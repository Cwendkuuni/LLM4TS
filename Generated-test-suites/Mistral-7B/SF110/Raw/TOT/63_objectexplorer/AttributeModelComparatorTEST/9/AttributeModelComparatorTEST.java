import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

class AttributeModelComparatorTest {
    private static AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompareNull() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareSameName() {
        AttributeModel o1 = new AttributeModel("attribute1");
        AttributeModel o2 = new AttributeModel("attribute1");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareLessName() {
        AttributeModel o1 = new AttributeModel("attributeA");
        AttributeModel o2 = new AttributeModel("attributeB");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareGreaterName() {
        AttributeModel o1 = new AttributeModel("attributeZ");
        AttributeModel o2 = new AttributeModel("attributeA");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareAAndLess() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("attributeX");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareALarger() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("attributeY");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareNullVSNonnull() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("attributeX");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testNonnullVSNull() {
        AttributeModel o1 = new AttributeModel("attributeX");
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareEmptyNames() {
        AttributeModel o1 = new AttributeModel("");
        AttributeModel o2 = new AttributeModel("");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareSameNameDifferentCase() {
        AttributeModel o1 = new AttributeModel("AttributeA");
        AttributeModel o2 = new AttributeModel("attributeA");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithSpace() {
        AttributeModel o1 = new AttributeModel("attribute With Space");
        AttributeModel o2 = new AttributeModel("attributeX");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithNumbers() {
        AttributeModel o1 = new AttributeModel("attribute1");
        AttributeModel o2 = new AttributeModel("attribute2");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithSpecialChars() {
        AttributeModel o1 = new AttributeModel("attribute#");
        AttributeModel o2 = new AttributeModel("attribute%");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareLongStrings() {
        AttributeModel o1 = new AttributeModel("attributeWithAReallyLongNameThatWillBeLongerThanOtherStrings");
        AttributeModel o2 = new AttributeModel("attributeWithANameThatIsLongButShorterThanTheFirstOne");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithNullChars() {
        StringBuilder name1 = new StringBuilder("attribute\u0000");
        StringBuilder name2 = new StringBuilder("attributeWithNullChar");
        AttributeModel o1 = new AttributeModel(name1.toString());
        AttributeModel o2 = new AttributeModel("attributeWithNullChar");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
}