import org.junit.Test;
import static org.junit.Assert.*;
import de.paragon.explorer.model.*;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        String name1 = "attribute1";
        String name2 = "attribute2";
        AttributeModel attributeModel1 = new AttributeModel(name1);
        AttributeModel attributeModel2 = new AttributeModel(name2);

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);

        String name3 = "a";
        AttributeModel attributeModel3 = new AttributeModel(name3);
        AttributeModel attributeModel4 = new AttributeModel("attribute3");

        result = comparator.compare(attributeModel3, attributeModel4);
        assertEquals(-1, result);

        result = comparator.compare(attributeModel1, attributeModel1);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithNullObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompareNullWithValidAttributeModel() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("validName");
        int result = comparator.compare(null, attributeModel);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareSameNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("sameName");
        AttributeModel attributeModel2 = new AttributeModel("sameName");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullWithNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareValidWithNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("validName");
        int result = comparator.compare(attributeModel, null);
    }
}