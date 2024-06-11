import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModel attrModel1 = new AttributeModel("Attribute1");
        AttributeModel attrModel2 = new AttributeModel("Attribute2");
        AttributeModel attrModel3 = new AttributeModel("a");
        AttributeModel attrModel4 = new AttributeModel("Attribute4");

        int result = new AttributeModelComparator().compare(attrModel1, attrModel2);
        assertEquals(-1, result);

        result = new AttributeModelComparator().compare(attrModel3, attrModel1);
        assertEquals(1, result);

        result = new AttributeModelComparator().compare(attrModel1, attrModel3);
        assertEquals(-1, result);

        result = new AttributeModelComparator().compare(attrModel1, attrModel1);
        assertEquals(0, result);

        result = new AttributeModelComparator().compare(attrModel4, attrModel1);
        assertEquals(0, result);
    }
}