package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_SameName_AscendingOrder() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_SameName_DifferentValues_AscendingOrder() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name1", "value2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertNotEquals(0, result);
    }

    @Test
    public void testCompare_DifferentNames_AscendingOrder() {
        AttributeModel model1 = new AttributeModel("name1", "value1");
        AttributeModel model2 = new AttributeModel("name2", "value2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_DifferentNames_DescendingOrder() {
        AttributeModel model1 = new AttributeModel("name2", "value2");
        AttributeModel model2 = new AttributeModel("name1", "value1");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_NullObjects() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("name1", "value1");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, result);

        model1 = new AttributeModel("name1", "value1");
        model2 = null;
        result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(1, result);

        model1 = null;
        model2 = null;
        result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_NameA_BeforeA() {
        AttributeModel model1 = new AttributeModel("z", "value1");
        AttributeModel model2 = new AttributeModel("a", "value2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NameA_AfterA() {
        AttributeModel model1 = new AttributeModel("a", "value1");
        AttributeModel model2 = new AttributeModel("z", "value2");
        int result = new AttributeModelComparator().compare(model1, model2);
        assertEquals(1, result);
    }
}