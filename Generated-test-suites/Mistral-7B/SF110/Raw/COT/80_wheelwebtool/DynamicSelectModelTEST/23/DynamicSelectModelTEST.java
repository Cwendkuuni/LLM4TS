package wheel.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModelConstructorWithCollectionLabelValue() {
        ElExpression collection = Mockito.mock(ElExpression.class);
        ElExpression label = Mockito.mock(ElExpression.class);
        ElExpression value = Mockito.mock(ElExpression.class);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        // Check that the constructor has set the fields correctly
        assertEquals(collection, dynamicSelectModel.getCollection());
        assertEquals(label, dynamicSelectModel.getLabel());
        assertEquals(value, dynamicSelectModel.getValue());

        // Check that the validate method is called
        dynamicSelectModel.validate();
    }

    @Test
    public void testDynamicSelectModelEmptyConstructor() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        // Check that the fields are initialized to null
        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());
    }

    @Test
    public void testDynamicSelectModelCollectionMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        String collection = "someCollection";
        dynamicSelectModel.collection(collection);

        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testDynamicSelectModelLabelMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        String label = "someLabel";
        dynamicSelectModel.label(label);

        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testDynamicSelectModelValueMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        String value = "someValue";
        dynamicSelectModel.value(value);

        assertEquals(value, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testDynamicSelectModelEnumsMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        String collection = "enumCollection";
        dynamicSelectModel.enumeration(collection);

        assertEquals(collection + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testDynamicSelectModelTranslatorMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        String translator = "someTranslator";
        dynamicSelectModel.translator(translator);

        assertEquals(translator, dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testDynamicSelectModelEmptyMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        Object emptyObject = new Object();
        dynamicSelectModel.empty(emptyObject);

        assertEquals(emptyObject, dynamicSelectModel.getEmptyObject());
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelValidateMethodThrowsExceptionWhenCollectionIsNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = null;
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelValidateMethodThrowsExceptionWhenLabelIsNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = null;
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelValidateMethodThrowsExceptionWhenValueIsNull() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value = null;
        dynamicSelectModel.validate();
    }

    @Test
    public void testGetOptionCountMethodReturnsCorrectSizeOfObjects() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object());
        dynamicSelectModel.objects.add(new Object());

        int optionCount = dynamicSelectModel.getOptionCount();

        assertEquals(2, optionCount);
    }

    @Test
    public void testGetLabelMethodReturnsCorrectLabelForGivenIndex() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Label1";
            }
        });
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Label2";
            }
        });

        String label1 = dynamicSelectModel.getLabel(0);
        String label2 = dynamicSelectModel.getLabel(1);

        assertEquals("Label1", label1);
        assertEquals("Label2", label2);
    }

    @Test(expected = WheelException.class)
    public void testGetLabelMethodThrowsExceptionWhenIndexIsOutOfBounds() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object());

        dynamicSelectModel.getLabel(1);
    }

    @Test
    public void testGetValueMethodReturnsCorrectValueForGivenIndex() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Value1";
            }
        });
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Value2";
            }
        });

        String value1 = dynamicSelectModel.getValue(0);
        String value2 = dynamicSelectModel.getValue(1);

        assertEquals("Value1", value1);
        assertEquals("Value2", value2);
    }

    @Test(expected = WheelException.class)
    public void testGetValueMethodThrowsExceptionWhenIndexIsOutOfBounds() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object());

        dynamicSelectModel.getValue(1);
    }

    @Test
    public void testTranslateValueMethodReturnsCorrectTranslatedValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Value1";
            }

            public int getValue() {
                return 1;
            }
        });
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Value2";
            }

            public int getValue() {
                return 2;
            }
        });

        Object translatedValue1 = dynamicSelectModel.translateValue("1");
        Object translatedValue2 = dynamicSelectModel.translateValue("2");

        assertEquals(dynamicSelectModel.objects.get(0), translatedValue1);
        assertEquals(dynamicSelectModel.objects.get(1), translatedValue2);
    }

    @Test
    public void testTranslateValueMethodReturnsNullWhenNoMatchingValueIsFound() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Value1";
            }

            public int getValue() {
                return 1;
            }
        });
        dynamicSelectModel.objects.add(new Object() {
            @Override
            public String toString() {
                return "Value2";
            }

            public int getValue() {
                return 2;
            }
        });

        Object nullValue = dynamicSelectModel.translateValue("3");

        assertNull(nullValue);
    }

    @Test
    public void testGetObjectsMethodReturnsCorrectCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        Collection collection = new ArrayList();
        collection.add(new Object());
        collection.add(new Object());

        dynamicSelectModel.objects = collection;

        Collection returnedObjects = dynamicSelectModel.getObjects();

        assertEquals(collection, returnedObjects);
    }

    @Test
    public void testGetObjectsMethodReturnsEmptyCollectionWhenEmptyObjectIsSet() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object());
        dynamicSelectModel.emptyObject = new Object();

        Collection returnedObjects = dynamicSelectModel.getObjects();

        assertTrue(returnedObjects.isEmpty());
    }

    @Test
    public void testGetComponentMethodReturnsComponent() {
        Component component = Mockito.mock(Component.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = component;

        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponentMethodSetsComponent() {
        Component component = Mockito.mock(Component.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.setComponent(component);

        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponentMethodReturnsTopLevelComponent() {
        StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.topLevelComponent = topLevelComponent;

        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponentMethodSetsTopLevelComponent() {
        StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
}