package wheel.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class DynamicSelectModelTest {

    @Test
    public void testConstructorWithCollectionLabelValue() {
        ElExpression collection = PowerMockito.mock(ElExpression.class);
        ElExpression label = PowerMockito.mock(ElExpression.class);
        ElExpression value = PowerMockito.mock(ElExpression.class);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithNullCollection() {
        ElExpression label = Mockito.mock(ElExpression.class);
        ElExpression value = Mockito.mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(null, label, value);
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithNullLabel() {
        ElExpression collection = Mockito.mock(ElExpression.class);
        ElExpression value = Mockito.mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, null, value);
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithNullValue() {
        ElExpression collection = Mockito.mock(ElExpression.class);
        ElExpression label = Mockito.mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, null);
    }

    @Test
    public void testCollectionMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel = dynamicSelectModel.collection("collection");

        Assert.assertEquals("collection", dynamicSelectModel.collection.getExpression());
    }

    @Test
    public void testLabelMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel = dynamicSelectModel.label("label");

        Assert.assertEquals("label", dynamicSelectModel.label.getExpression());
    }

    @Test
    public void testValueMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel = dynamicSelectModel.value("value");

        Assert.assertEquals("value", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testEnumsMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel = dynamicSelectModel.enumeration("MyEnum");

        Assert.assertEquals("MyEnum.values()", dynamicSelectModel.collection.getExpression());
        Assert.assertEquals("toString()", dynamicSelectModel.label.getExpression());
        Assert.assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testTranslatorMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel = dynamicSelectModel.translator("translator");

        Assert.assertEquals("translator", dynamicSelectModel.translator.getExpression());
    }

    @Test
    public void testEmptyMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel = dynamicSelectModel.empty(new Object());

        Assert.assertNotNull(dynamicSelectModel.emptyObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionCountWithEmptyObjects() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.objects = Collections.emptyList();

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetLabelWithValidIndex() {
        DynamicSelectModel dynamicSelectModel = createDynamicSelectModelWithObjects();
        String label = dynamicSelectModel.getLabel(0);

        Assert.assertNotNull(label);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetLabelWithInvalidIndex() {
        DynamicSelectModel dynamicSelectModel = createDynamicSelectModelWithObjects();

        dynamicSelectModel.getLabel(3);
    }

    @Test(expected = WheelException.class)
    public void testGetLabelWithErrorExpression() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = new ElExpression("errorExpression");

        dynamicSelectModel.getLabel(0);
    }

    @Test(expected = WheelException.class)
    public void testGetValueWithInvalidIndex() {
        DynamicSelectModel dynamicSelectModel = createDynamicSelectModelWithObjects();

        dynamicSelectModel.getValue(3);
    }

    @Test(expected = WheelException.class)
    public void testGetValueWithErrorExpression() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value = new ElExpression("errorExpression");

        dynamicSelectModel.getValue(0);
    }

    @Test
    public void testTranslateValueWithValidValue() {
        DynamicSelectModel dynamicSelectModel = createDynamicSelectModelWithObjects();
        Object translatedValue = dynamicSelectModel.translateValue("value1");

        Assert.assertNotNull(translatedValue);
    }

    @Test
    public void testTranslateValueWithInvalidValue() {
        DynamicSelectModel dynamicSelectModel = createDynamicSelectModelWithObjects();

        dynamicSelectModel.translateValue("value3");
    }

    @Test
    public void testGetObjectsWithValidCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("validCollection");

        Collection objects = dynamicSelectModel.getObjects();

        Assert.assertNotNull(objects);
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithEmptyCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("emptyCollection");

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithErrorCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("errorCollection");

        dynamicSelectModel.getObjects();
    }

    private DynamicSelectModel createDynamicSelectModelWithObjects() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("validCollection");
        List objects = new ArrayList();
        objects.add("object1");
        objects.add("object2");
        dynamicSelectModel.objects = objects;

        return dynamicSelectModel;
    }
}