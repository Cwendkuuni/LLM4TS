package de.paragon.explorer.model;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        assertThat(result, is(-1));
    }

    @Test
    public void testCompare_BothObjectsNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        // Put your assertions here
    }

    @Test
    public void testCompare_BothObjectsSameName() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        // Put your assertions here
    }

    @Test
    public void testCompare_SpecialCharactersInNames() {
        AttributeModel model1 = new AttributeModel("name1#");
        AttributeModel model2 = new AttributeModel("name2%");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        // Put your assertions here
    }

    @Test
    public void testCompare_EmptyNameForObject1() {
        AttributeModel model1 = new AttributeModel("");
        AttributeModel model2 = new AttributeModel("name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        // Put your assertions here
    }

    @Test
    public void testCompare_EmptyNameForObject2() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        // Put your assertions here
    }

    @Test
    public void testCompare_NullNameForObject1() {
        AttributeModel model1 = new AttributeModel(null);
        AttributeModel model2 = new AttributeModel("name2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        // Put your assertions here
    }

    @Test
    public void testCompare_NullNameForObject2() {
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel(null);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(model1, model2);
        // Put your assertions here
    }
}