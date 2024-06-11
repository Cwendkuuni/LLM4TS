package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class DynamicSelectModelTest {

    @Test
    public void testConstructorWithCollectionLabelValue() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression label = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);

        DynamicSelectModel model = new DynamicSelectModel(collection, label, value);

        assertEquals(collection, model.getCollection());
        assertEquals(label, model.getLabel());
        assertEquals(value, model.getValue());
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutCollection() {
        ElExpression label = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);

        new DynamicSelectModel(label, value);
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutLabel() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);

        new DynamicSelectModel(collection, value);
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutValue() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression label = mock(ElExpression.class);

        new DynamicSelectModel(collection, label);
    }

    @Test
    public void testCollectionMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        String collection = "collectionExpression";

        ElExpression elCollection = new ElExpression(collection);
        model = model.collection(elCollection);

        assertEquals(elCollection, model.getCollection());
    }

    @Test
    public void testLabelMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        String label = "labelExpression";

        ElExpression elLabel = new ElExpression(label);
        model = model.label(elLabel);

        assertEquals(elLabel, model.getLabel());
    }

    @Test
    public void testValueMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        String value = "valueExpression";

        ElExpression elValue = new ElExpression(value);
        model = model.value(elValue);

        assertEquals(elValue, model.getValue());
    }

    @Test
    public void testEnumsMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        String collection = "enumCollectionExpression";
        String label = "toStringExpression";
        String value = "ordinalExpression";

        ElExpression elCollection = new ElExpression(collection + ".values()");
        ElExpression elLabel = new ElExpression(label);
        ElExpression elValue = new ElExpression(value);

        model = model.enumeration(collection);

        assertEquals(elCollection, model.getCollection());
        assertEquals(elLabel, model.getLabel());
        assertEquals(elValue, model.getValue());
    }

    @Test
    public void testTranslatorMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        String translator = "translatorExpression";

        ElExpression elTranslator = new ElExpression(translator);
        model = model.translator(elTranslator);

        assertEquals(elTranslator, model.getTranslator());
    }

    @Test
    public void testEmptyMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        Object emptyObject = new Object();

        model = model.empty(emptyObject);

        assertNotNull(model.getObjects());
        assertEquals(2, model.getObjects().size());
        assertEquals(emptyObject, model.getObjects().toArray()[0]);
    }

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection objects = new ArrayList();
        objects.add("Option 1");
        objects.add("Option 2");

        model.setObjects(objects);

        assertEquals(2, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection objects = new ArrayList();
        objects.add(new ElExpression("Option 1 Label Expression"));
        objects.add(new ElExpression("Option 2 Label Expression"));

        model.setObjects(objects);

        assertEquals("Option 1 Label Expression", model.getLabel(0));
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection objects = new ArrayList();
        objects.add(new ElExpression("Option 1 Value Expression"));
        objects.add(new ElExpression("Option 2 Value Expression"));

        model.setObjects(objects);

        assertEquals("Option 1 Value Expression", model.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection objects = new ArrayList();
        objects.add(new ElExpression("Option 1"));
        objects.add(new ElExpression("Option 2"));

        model.setObjects(objects);

        String expectedValue = "Expected Translated Value";

        model.setTranslator(new ElExpression("translateExpression"));

        Object translatedValue = model.translateValue(expectedValue);

        assertNotNull(translatedValue);
        assertEquals(expectedValue, translatedValue.toString());
    }

    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        Component component = mock(Component.class);

        Collection expectedObjects = new ArrayList();
        expectedObjects.add("Object 1");
        expectedObjects.add("Object 2");

        when(component.getElContext()).thenReturn(mock(ElContext.class));
        when(topLevelComponent.getComponent()).thenReturn(component);

        ElExpression elCollection = new ElExpression("collectionExpression");
        ElExpression elLabel = new ElExpression("labelExpression");
        ElExpression elValue = new ElExpression("valueExpression");

        model.setCollection(elCollection);
        model.setLabel(elLabel);
        model.setValue(elValue);
        model.setTopLevelComponent(topLevelComponent);

        model.validate();

        Collection objects = model.getObjects();

        assertEquals(expectedObjects, objects);
    }

    @Test
    public void testValidate() {
        DynamicSelectModel model = new DynamicSelectModel();

        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithoutCollection() {
        DynamicSelectModel model = new DynamicSelectModel();

        model.setValue(new ElExpression("valueExpression"));
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithoutLabel() {
        DynamicSelectModel model = new DynamicSelectModel();

        model.setCollection(new ElExpression("collectionExpression"));
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithoutValue() {
        DynamicSelectModel model = new DynamicSelectModel();

        model.setCollection(new ElExpression("collectionExpression"));
        model.setLabel(new ElExpression("labelExpression"));
        model.validate();
    }

    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection objects = new ArrayList();
        objects.add("Option 1");
        objects.add("Option 2");

        model.setObjects(objects);

        model.reset();

        assertNull(model.getObjects());
    }
}