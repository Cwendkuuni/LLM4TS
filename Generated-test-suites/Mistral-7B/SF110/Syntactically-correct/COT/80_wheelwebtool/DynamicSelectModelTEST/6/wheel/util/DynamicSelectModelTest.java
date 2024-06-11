package wheel.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.*;

import wheel.*;
import wheel.components.*;

@RunWith(MockitoJUnitRunner.class)
public class DynamicSelectModelTest {

    @Test
    public void testConstructorWithCollectionLabelValue() {
        ElExpression collection = Mockito.mock(ElExpression.class);
        ElExpression label = Mockito.mock(ElExpression.class);
        ElExpression value = Mockito.mock(ElExpression.class);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        // Check that the methods were called in the constructor
        Mockito.verify(collection).isValid();
        Mockito.verify(label).isValid();
        Mockito.verify(value).isValid();
    }

    @Test
    public void testConstructorWithoutArguments() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testCollectionMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String collection = "testCollection";

        dynamicSelectModel.collection(collection);

        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testLabelMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String label = "testLabel";

        dynamicSelectModel.label(label);

        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testValueMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String value = "testValue";

        dynamicSelectModel.value(value);

        assertEquals(value, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testEnumerationMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String enumeration = "testEnumeration";

        dynamicSelectModel.enumeration(enumeration);

        assertEquals(enumeration + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testTranslatorMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String translator = "testTranslator";

        dynamicSelectModel.translator(translator);

        assertEquals(translator, dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testEmptyMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Object emptyObject = Mockito.mock(Object.class);

        dynamicSelectModel.empty(emptyObject);

        assertNotNull(dynamicSelectModel.getObjects());
        assertTrue(dynamicSelectModel.getObjects().contains(emptyObject));
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsNullCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = null;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsInvalidCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression invalidCollection = Mockito.mock(ElExpression.class);
        dynamicSelectModel.collection = invalidCollection;

        dynamicSelectModel.getObjects();

        Mockito.verify(invalidCollection).errorMessage("Could not evaluate expression");
    }

    @Test
    public void testGetObjectsValidCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression validCollection = Mockito.mock(ElExpression.class);
        Object[] collectionArray = new Object[5];

        Mockito.when(validCollection.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(collectionArray);
        dynamicSelectModel.collection = validCollection;

        Collection objects = dynamicSelectModel.getObjects();

        assertEquals(5, objects.size());
        for (int i = 0; i < objects.size(); i++) {
            assertEquals(collectionArray[i], objects.get(i));
        }
    }

    @Test
    public void testGetLabelIndexOutOfBounds() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression label = Mockito.mock(ElExpression.class);
        Object[] objects = new Object[5];

        Mockito.when(label.eval(Mockito.anyObject(), Mockito.any(Component.class))).thenAnswer(invocation -> invocation.getArguments()[0].toString());
        dynamicSelectModel.label = label;
        dynamicSelectModel.objects = Arrays.asList(objects).subList(0, 4).asList();

        try {
            String labelAtIndex = dynamicSelectModel.getLabel(5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetLabelValidIndex() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression label = Mockito.mock(ElExpression.class);
        Object[] objects = new Object[5];

        Mockito.when(label.eval(Mockito.anyObject(), Mockito.any(Component.class))).thenAnswer(invocation -> invocation.getArguments()[0].toString());
        dynamicSelectModel.label = label;
        dynamicSelectModel.objects = Arrays.asList(objects).subList(0, 5).asList();

        String labelAtIndex = dynamicSelectModel.getLabel(0);
        assertNotNull(labelAtIndex);
    }

    @Test
    public void testGetValueIndexOutOfBounds() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression value = Mockito.mock(ElExpression.class);
        Object[] objects = new Object[5];

        Mockito.when(value.eval(Mockito.anyObject(), Mockito.any(Component.class))).thenAnswer(invocation -> invocation.getArguments()[0].toString());
        dynamicSelectModel.value = value;
        dynamicSelectModel.objects = Arrays.asList(objects).subList(0, 4).asList();

        try {
            String valueAtIndex = dynamicSelectModel.getValue(5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueValidIndex() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        ElExpression value = Mockito.mock(ElExpression.class);
        Object[] objects = new Object[5];

        Mockito.when(value.eval(Mockito.anyObject(), Mockito.any(Component.class))).thenAnswer(invocation -> invocation.getArguments()[0].toString());
        dynamicSelectModel.value = value;
        dynamicSelectModel.objects = Arrays.asList(objects).subList(0, 5).asList();

        String valueAtIndex = dynamicSelectModel.getValue(0);
        assertNotNull(valueAtIndex);
    }

    @Test
    public void testTranslateValueNullValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String value = null;

        assertNull(dynamicSelectModel.translateValue(value));
    }

    @Test
    public void testTranslateValueValueNotFound() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String value = "notFoundValue";

        assertNull(dynamicSelectModel.translateValue(value));
    }

    @Test
    public void testTranslateValueValueFound() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        String value = "foundValue";
        Object valueObject = new Object();

        dynamicSelectModel.objects = Arrays.asList(new Object[] { valueObject }).asList();

        Object translatedValue = dynamicSelectModel.translateValue(value);

        assertEquals(valueObject, translatedValue);
    }

    @Test
    public void testValidateThrowsExceptionWhenCollectionIsNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = null;

        try {
            dynamicSelectModel.validate();
            fail();
        } catch (WheelException e) {
            // Expected
        }
    }

    @Test
    public void testValidateThrowsExceptionWhenLabelIsNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = null;

        try {
            dynamicSelectModel.validate();
            fail();
        } catch (WheelException e) {
            // Expected
        }
    }

    @Test
    public void testValidateThrowsExceptionWhenValueIsNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value = null;

        try {
            dynamicSelectModel.validate();
            fail();
        } catch (WheelException e) {
            // Expected
        }
    }
}