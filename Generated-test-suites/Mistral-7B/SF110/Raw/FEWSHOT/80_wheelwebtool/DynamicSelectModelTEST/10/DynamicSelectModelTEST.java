package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import wheel.components.*;
import wheel.*;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModelWithValidCollectionLabelValueInitialization() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("['foo', 'bar', 'baz']");
        ElExpression label = new ElExpression("toString()");
        ElExpression value = new ElExpression("ordinal()");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection)
                .label(label)
                .value(value);

        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }

    @Test
    public void testDynamicSelectModelEmptyCollectionInitialization() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("[]");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection);

        assertEquals(Collections.emptyList(), dynamicSelectModel.getObjects());
    }

    @Test
    public void testDynamicSelectModelWithValidCollectionLabelValueAndTranslatorInitialization() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("['foo', 'bar', 'baz']");
        ElExpression label = new ElExpression("toString()");
        ElExpression value = new ElExpression("ordinal()");
        ElExpression translator = new ElExpression("upperCase()");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection)
                .label(label)
                .value(value)
                .translator(translator);

        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
        assertNotNull(dynamicSelectModel.translator);
    }

    @Test
    public void testDynamicSelectModelGetObjectsReturnsExpectedObjects() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("['foo', 'bar', 'baz']");
        ElExpression label = new ElExpression("toString()");
        ElExpression value = new ElExpression("ordinal()");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection)
                .label(label)
                .value(value);

        List<Object> expectedObjects = Arrays.asList("foo", "bar", "baz");
        when(collection.eval(component, component)).then(new Answer<List<Object>>() {
            @Override
            public List<Object> answer(InvocationOnMock invocation) throws Throwable {
                return (List<Object>) invocation.getArgument(0);
            }
        });

        List<Object> actualObjects = dynamicSelectModel.getObjects();

        assertEquals(expectedObjects, actualObjects);
    }

    @Test
    public void testDynamicSelectModelGetObjectsThrowsExceptionWhenCollectionEvaluationFails() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("invalidCollectionExpression");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection);

        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Expression invalidCollectionExpression evaluated to null. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }

    @Test
    public void testDynamicSelectModelGetObjectsReturnsExpectedObjectsWhenCollectionIsObjectArray() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("[1, 2, 3]");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection);

        Object[] expectedObjectsArray = new Object[]{1, 2, 3};
        when(collection.eval(component, component)).then(new Answer<Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                return (Object[]) invocation.getArgument(0);
            }
        });

        List<Object> actualObjects = dynamicSelectModel.getObjects();
        List<Object> expectedObjects = new ArrayList<>();
        Collections.addAll(expectedObjects, expectedObjectsArray);

        assertEquals(expectedObjects, actualObjects);
    }

    @Test
    public void testDynamicSelectModelGetObjectsReturnsExpectedObjectsWhenCollectionIsLinkedList() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("[1, 2, 3]");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection);

        List<Object> expectedObjects = Arrays.asList(1, 2, 3);
        when(collection.eval(component, component)).then(new Answer<LinkedList<Object>>() {
            @Override
            public LinkedList<Object> answer(InvocationOnMock invocation) throws Throwable {
                return (LinkedList<Object>) invocation.getArgument(0);
            }
        });

        List<Object> actualObjects = dynamicSelectModel.getObjects();

        assertEquals(expectedObjects, actualObjects);
    }

    @Test
    public void testDynamicSelectModelGetLabelReturnsExpectedLabelForEachObject() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("[{label: 'foo'}, {label: 'bar'}, {label: 'baz'}]");
        ElExpression label = new ElExpression("label");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection)
                .label(label);

        Object[] expectedObjects = new Object[]{new HashMap(){{put("label", "foo");}}, new HashMap(){{put("label", "bar");}}, new HashMap(){{put("label", "baz");}}};
        when(collection.eval(component, component)).then(new Answer<Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                return (Object[]) invocation.getArgument(0);
            }
        });

        List<String> actualLabels = new ArrayList<>();
        for (int i = 0; i < dynamicSelectModel.getObjects().size(); i++) {
            actualLabels.add(dynamicSelectModel.getLabel(i));
        }

        List<String> expectedLabels = new ArrayList<>();
        for (Map<String, String> map : (List<Map<String, String>>) expectedObjects) {
            expectedLabels.add(map.get("label"));
        }

        assertEquals(expectedLabels, actualLabels);
    }

    @Test
    public void testDynamicSelectModelGetValueReturnsExpectedValueForEachObject() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("[{value: 'foo'}, {value: 'bar'}, {value: 'baz'}]");
        ElExpression value = new ElExpression("value");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection)
                .value(value);

        Object[] expectedObjects = new Object[]{new HashMap(){{put("value", "foo");}}, new HashMap(){{put("value", "bar");}}, new HashMap(){{put("value", "baz");}}};
        when(collection.eval(component, component)).then(new Answer<Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                return (Object[]) invocation.getArgument(0);
            }
        });

        List<String> actualValues = new ArrayList<>();
        for (int i = 0; i < dynamicSelectModel.getObjects().size(); i++) {
            actualValues.add(dynamicSelectModel.getValue(i));
        }

        List<String> expectedValues = new ArrayList<>();
        for (Map<String, String> map : (List<Map<String, String>>) expectedObjects) {
            expectedValues.add(map.get("value"));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testDynamicSelectModelTranslateValueReturnsExpectedTranslatedValueForEachObject() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("[{value: 'foo'}, {value: 'bar'}, {value: 'baz'}]");
        ElExpression label = new ElExpression("toString()");
        ElExpression value = new ElExpression("ordinal()");
        ElExpression translator = new ElExpression("upperCase()");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection)
                .label(label)
                .value(value)
                .translator(translator);

        Object[] expectedObjects = new Object[]{new HashMap(){{put("value", "foo");}}, new HashMap(){{put("value", "bar");}}, new HashMap(){{put("value", "baz");}}};
        when(collection.eval(component, component)).then(new Answer<Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                return (Object[]) invocation.getArgument(0);
            }
        });

        List<String> actualTranslatedValues = new ArrayList<>();
        for (int i = 0; i < dynamicSelectModel.getObjects().size(); i++) {
            Object translatedValue = dynamicSelectModel.translateValue(dynamicSelectModel.getValue(i));
            actualTranslatedValues.add(translatedValue != null ? translatedValue.toString() : "");
        }

        List<String> expectedTranslatedValues = new ArrayList<>();
        for (Map<String, String> map : (List<Map<String, String>>) expectedObjects) {
            String expectedTranslatedValue = map.get("value").toUpperCase();
            expectedTranslatedValues.add(expectedTranslatedValue);
        }

        assertEquals(expectedTranslatedValues, actualTranslatedValues);
    }

    @Test
    public void testDynamicSelectModelTranslateValueReturnsNullWhenNoTranslatorProvided() {
        Component component = Mockito.mock(Component.class);
        ElExpression collection = new ElExpression("[{value: 'foo'}, {value: 'bar'}, {value: 'baz'}]");
        ElExpression value = new ElExpression("ordinal()");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(collection)
                .label(new ElExpression("toString()"))
                .value(value);

        Object[] expectedObjects = new Object[]{new HashMap(){{put("value", "foo");}}, new HashMap(){{put("value", "bar");}}, new HashMap(){{put("value", "baz");}}};
        when(collection.eval(component, component)).then(new Answer<Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                return (Object[]) invocation.getArgument(0);
            }
        });

        List<String> actualTranslatedValues = new ArrayList<>();
        for (int i = 0; i < dynamicSelectModel.getObjects().size(); i++) {
            String valueString = dynamicSelectModel.getValue(i);
            Object translatedValue = dynamicSelectModel.translateValue(valueString);
            actualTranslatedValues.add(translatedValue == null ? "" : translatedValue.toString());
        }

        List<String> expectedTranslatedValues = new ArrayList<>();
        for (Map<String, String> map : (List<Map<String, String>>) expectedObjects) {
            expectedTranslatedValues.add("");
        }

        assertEquals(expectedTranslatedValues, actualTranslatedValues);
    }

    @Test
    public void testDynamicSelectModelWithEmptyCollectionInitialization() {
        Component component = Mockito.mock(Component.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        List<Object> actualObjects = dynamicSelectModel.getObjects();

        assertEquals(Collections.emptyList(), actualObjects);
    }

    @Test
    public void testDynamicSelectModelWithValidEmptyCollectionInitialization() {
        Component component = Mockito.mock(Component.class);
        ElExpression emptyCollection = new ElExpression("[]");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(emptyCollection);

        List<Object> actualObjects = dynamicSelectModel.getObjects();

        assertEquals(Collections.emptyList(), actualObjects);
    }

    @Test
    public void testDynamicSelectModelWithValidEmptyCollectionInitializationAndEmptyObject() {
        Component component = Mockito.mock(Component.class);
        ElExpression emptyCollection = new ElExpression("[]");
        Object emptyObject = new Object();

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel()
                .collection(emptyCollection)
                .empty(emptyObject);

        List<Object> actualObjects = dynamicSelectModel.getObjects();

        List<Object> expectedObjects = new ArrayList<>();
        expectedObjects.add(emptyObject);

        assertEquals(expectedObjects, actualObjects);
    }
}