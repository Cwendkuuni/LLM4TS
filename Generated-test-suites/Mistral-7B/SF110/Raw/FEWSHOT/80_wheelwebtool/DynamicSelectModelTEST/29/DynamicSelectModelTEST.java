package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import wheel.*;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModelConstructionWithCollectionLabelAndValue() {
        final ElExpression collection = mock(ElExpression.class);
        final ElExpression label = mock(ElExpression.class);
        final ElExpression value = mock(ElExpression.class);

        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        assertEquals(collection, dynamicSelectModel.getCollection());
        assertEquals(label, dynamicSelectModel.getLabelExpression());
        assertEquals(value, dynamicSelectModel.getValueExpression());

        verify(collection, times(1)).eval(any(Component.class), any(Component.class));
        verify(label, times(1)).eval(any(Object.class), any(Component.class));
        verify(value, times(1)).eval(any(Object.class), any(Component.class));
        verifyNoMoreInteractions(collection);
        verifyNoMoreInteractions(label);
        verifyNoMoreInteractions(value);
    }

    @Test
    public void testDynamicSelectModelConstructionWithEmptyArguments() {
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabelExpression());
        assertNull(dynamicSelectModel.getValueExpression());
    }

    @Test
    public void testDynamicSelectModelCollectionMethod() {
        final ElExpression collectionExpression = mock(ElExpression.class);
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.collection(collectionExpression);

        assertEquals(collectionExpression, dynamicSelectModel.getCollection());
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelCollectionMethodWithNullExpression() {
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.collection(null);

        fail("Exception expected: WheelException");
    }

    @Test
    public void testDynamicSelectModelLabelMethod() {
        final ElExpression labelExpression = mock(ElExpression.class);
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.label(labelExpression);

        assertEquals(labelExpression, dynamicSelectModel.getLabelExpression());
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelLabelMethodWithNullExpression() {
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.label(null);

        fail("Exception expected: WheelException");
    }

    @Test
    public void testDynamicSelectModelValueMethod() {
        final ElExpression valueExpression = mock(ElExpression.class);
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.value(valueExpression);

        assertEquals(valueExpression, dynamicSelectModel.getValueExpression());
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelValueMethodWithNullExpression() {
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.value(null);

        fail("Exception expected: WheelException");
    }

    @Test
    public void testDynamicSelectModelEnumerateMethod() {
        final ElExpression collectionExpression = mock(ElExpression.class);
        final ElExpression labelExpression = mock(ElExpression.class);
        final ElExpression valueExpression = mock(ElExpression.class);

        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.enumeration("enumName");

        assertEquals("enumName.values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabelExpression().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValueExpression().getExpression());

        verify(collectionExpression, times(1)).eval(any(Component.class), any(Component.class));
        verify(labelExpression, times(1)).eval(any(Object.class), any(Component.class));
        verify(valueExpression, times(1)).eval(any(Object.class), any(Component.class));
        verifyNoMoreInteractions(collectionExpression);
        verifyNoMoreInteractions(labelExpression);
        verifyNoMoreInteractions(valueExpression);
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelEnumerateMethodWithNullExpression() {
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.enumeration(null);

        fail("Exception expected: WheelException");
    }

    @Test
    public void testDynamicSelectModelTranslatorMethod() {
        final ElExpression translatorExpression = mock(ElExpression.class);
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.translator("translatorName");

        assertEquals("translatorName", dynamicSelectModel.getTranslatorExpression().getExpression());

        verify(translatorExpression, times(1)).eval(any(Object.class), any(Component.class));
        verifyNoMoreInteractions(translatorExpression);
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelTranslatorMethodWithNullExpression() {
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.translator(null);

        fail("Exception expected: WheelException");
    }

    @Test
    public void testDynamicSelectModelEmptyMethod() {
        final Object emptyObject = new Object();
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.empty(emptyObject);

        final Collection objects = dynamicSelectModel.getObjects();
        assertEquals(1, objects.size());
        assertEquals(emptyObject, objects.iterator().next());
    }

    @Test
    public void testDynamicSelectModelGetObjectsMethodReturnsEmptyCollection() {
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        assertEquals(Collections.emptyList(), dynamicSelectModel.getObjects());
    }

    @Test
    public void testDynamicSelectModelGetObjectsMethodReturnsCollectionWithElements() {
        final ElExpression collectionExpression = mock(ElExpression.class);
        final Object[] collectionElements = new Object[]{new Object(), new Object()};
        final Collection expectedCollection = Arrays.asList(collectionElements);

        when(collectionExpression.eval(any(Component.class), any(Component.class))).thenReturn(collectionElements);
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection("collectionExpression");

        assertEquals(expectedCollection, dynamicSelectModel.getObjects());

        verify(collectionExpression, times(1)).eval(any(Component.class), any(Component.class));
        verifyNoMoreInteractions(collectionExpression);
    }

    @Test
    public void testDynamicSelectModelGetValueMethodReturnsValueOfCorrespondingIndex() {
        final Object[] collectionElements = new Object[]{"Foo", "Bar"};
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection("[" + "['Foo','Bar']" + "]").label("toString").value("toString");

        assertEquals("Foo", dynamicSelectModel.getValue(0));
        assertEquals("Bar", dynamicSelectModel.getValue(1));
    }

    @Test
    public void testDynamicSelectModelGetValueMethodReturnsNullIfNoMatchingValue() {
        final Object[] collectionElements = new Object[]{"Foo", "Bar"};
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection("[" + "['Foo','Bar']" + "]").label("toString").value("Baz");

        assertNull(dynamicSelectModel.getValue(2));
    }

    @Test
    public void testDynamicSelectModelTranslateValueMethodReturnsTranslatedValue() {
        final Object[] collectionElements = new Object[]{"Foo", "Bar"};
        final ElExpression translatorExpression = mock(ElExpression.class);
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection("[" + "['Foo','Bar']" + "]").label("toString").value("toString").translator("translatorExpression");

        when(translatorExpression.eval(any(Object.class), any(Component.class))).thenReturn("TranslatedValue");

        assertEquals("TranslatedValue", dynamicSelectModel.translateValue("Foo"));
    }

    @Test
    public void testDynamicSelectModelTranslateValueMethodReturnsNullIfNoMatchingValue() {
        final Object[] collectionElements = new Object[]{"Foo", "Bar"};
        final DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection("[" + "['Foo','Bar']" + "]").label("toString").value("toString").translator("translatorExpression");

        assertNull(dynamicSelectModel.translateValue("Baz"));
    }

}