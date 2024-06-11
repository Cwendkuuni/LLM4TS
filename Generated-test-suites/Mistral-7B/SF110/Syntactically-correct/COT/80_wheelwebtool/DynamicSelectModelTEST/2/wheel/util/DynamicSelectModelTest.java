package wheel.util;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModelConstructorWithCollectionLabelValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        assertNotNull(dynamicSelectModel);
    }

    @Test
    public void testDynamicSelectModelEmptyConstructor() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        assertNotNull(dynamicSelectModel);
    }

    @Test
    public void testDynamicSelectModelCollectionMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("collectionExpression");
        assertEquals("collectionExpression", dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testDynamicSelectModelLabelMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label("labelExpression");
        assertEquals("labelExpression", dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testDynamicSelectModelValueMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value("valueExpression");
        assertEquals("valueExpression", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testDynamicSelectModelEnumerationMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.enumeration("enumerationExpression");
        assertEquals("enumerationExpression.values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testDynamicSelectModelTranslatorMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.translator("translatorExpression");
        assertEquals("translatorExpression", dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testDynamicSelectModelEmptyMethod() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.empty(new Object());
        assertNotNull(dynamicSelectModel.getObjects());
        assertTrue(dynamicSelectModel.getObjects().contains(dynamicSelectModel.getEmptyObject()));
    }

    @Test
    public void testDynamicSelectModelGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection collection = new ArrayList();
        collection.add(new Object());
        collection.add(new Object());
        dynamicSelectModel.setObjects(collection);
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelGetLabelWithNullIndex() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.getLabel(-1);
    }

    @Test
    public void testDynamicSelectModelGetLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection collection = new ArrayList();
        collection.add(new Object() {
            @Override
            public String toString() {
                return "Label1";
            }
        });
        collection.add(new Object() {
            @Override
            public String toString() {
                return "Label2";
            }
        });
        dynamicSelectModel.setObjects(collection);
        assertEquals("Label1", dynamicSelectModel.getLabel(0));
        assertEquals("Label2", dynamicSelectModel.getLabel(1));
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelGetValueWithNullIndex() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.getValue(-1);
    }

    @Test
    public void testDynamicSelectModelGetValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection collection = new ArrayList();
        collection.add(new Object() {
            @Override
            public String toString() {
                return "Value1";
            }
        });
        collection.add(new Object() {
            @Override
            public String toString() {
                return "Value2";
            }
        });
        dynamicSelectModel.setObjects(collection);
        assertEquals("Value1", dynamicSelectModel.getValue(0));
        assertEquals("Value2", dynamicSelectModel.getValue(1));
    }

    @Test
    public void testDynamicSelectModelTranslateValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection collection = new ArrayList();
        collection.add(new Object() {
            @Override
            public String toString() {
                return "Value1";
            }
        });
        collection.add(new Object() {
            @Override
            public String toString() {
                return "Value2";
            }
        });
        dynamicSelectModel.setObjects(collection);
        assertEquals("Value1", dynamicSelectModel.translateValue("Value1"));
        assertEquals("Value2", dynamicSelectModel.translateValue("Value2"));
        assertNull(dynamicSelectModel.translateValue("nonExistentValue"));
    }

    @Test
    public void testDynamicSelectModelGetObjects() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection collection = new ArrayList();
        collection.add(new Object());
        dynamicSelectModel.setObjects(collection);
        assertEquals(collection, dynamicSelectModel.getObjects());
    }

    @Test
    public void testDynamicSelectModelGetComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testDynamicSelectModelSetComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testDynamicSelectModelGetTopLevelComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testDynamicSelectModelSetTopLevelComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testDynamicSelectModelValidate() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            fail("validate method should not throw an exception when no attributes are set.");
        }

        DynamicSelectModel dynamicSelectModelWithNullCollection = new DynamicSelectModel();
        try {
            dynamicSelectModelWithNullCollection.validate();
            fail("validate method should have thrown an exception when the collection attribute was null.");
        } catch (WheelException e) {
            // expected
        }

        DynamicSelectModel dynamicSelectModelWithNullLabel = new DynamicSelectModel();
        dynamicSelectModelWithNullLabel.collection("collection");
        try {
            dynamicSelectModelWithNullLabel.validate();
            fail("validate method should have thrown an exception when the label attribute was null.");
        } catch (WheelException e) {
            // expected
        }

        DynamicSelectModel dynamicSelectModelWithNullValue = new DynamicSelectModel();
        dynamicSelectModelWithNullValue.collection("collection").label("label");
        try {
            dynamicSelectModelWithNullValue.validate();
            fail("validate method should have thrown an exception when the value attribute was null.");
        } catch (WheelException e) {
            // expected
        }
    }

    @Test
    public void testDynamicSelectModelReset() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        Collection collection = new ArrayList();
        collection.add(new Object());
        dynamicSelectModel.setObjects(collection);
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}