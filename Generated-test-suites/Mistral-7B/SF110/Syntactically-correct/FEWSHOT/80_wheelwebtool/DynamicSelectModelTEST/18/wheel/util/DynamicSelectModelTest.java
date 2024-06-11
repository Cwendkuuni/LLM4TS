package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {

    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private DynamicSelectModel model;

    @Before
    public void setUp() {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        collection = mock(ElExpression.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);

        model = new DynamicSelectModel();
        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testEmptyInitialization() {
        final Collection emptyCollection = new ArrayList<>();
        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(emptyCollection);

        model.collection(collection);
        model.label(label);
        model.value(value);

        assertEquals(0, model.getOptionCount());
    }

    @Test
    public void testInitializeWithCollection() {
        final List<String> collectionList = new ArrayList<>();
        collectionList.add("Option 1");
        collectionList.add("Option 2");

        final Collection collection = new ArrayList<>(collectionList);

        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(collection);

        model.collection(collection);
        model.label(label);
        model.value(value);

        assertEquals(2, model.getOptionCount());

        final Object option1 = model.getObjects().toArray()[0];
        final Object option2 = model.getObjects().toArray()[1];

        assertEquals("Option 1", model.getLabel(0));
        assertEquals("Option 1", model.getValue(0));
        assertSame(option1, model.getObjects().toArray()[0]);

        assertEquals("Option 2", model.getLabel(1));
        assertEquals("Option 2", model.getValue(1));
        assertSame(option2, model.getObjects().toArray()[1]);
    }

    @Test(expected = WheelException.class)
    public void testInitializeWithNullCollection() {
        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(null);

        model.collection(collection);
        model.label(label);
        model.value(value);
    }

    @Test(expected = WheelException.class)
    public void testInitializeWithNonCollectionObject() {
        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        final Object nonCollectionObject = new Object();

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(nonCollectionObject);

        model.collection(collection);
        model.label(label);
        model.value(value);
    }

    @Test
    public void testInitializeWithCollectionFromElExpression() {
        final List<String> collectionList = new ArrayList<>();
        collectionList.add("Option 1");
        collectionList.add("Option 2");

        final Collection collection = new ArrayList<>(collectionList);

        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(collection);

        model.collection(new ElExpression("someCollectionExpression"));
        model.label(label);
        model.value(value);

        assertEquals(2, model.getOptionCount());

        final Object option1 = model.getObjects().toArray()[0];
        final Object option2 = model.getObjects().toArray()[1];

        assertEquals("Option 1", model.getLabel(0));
        assertEquals("Option 1", model.getValue(0));
        assertSame(option1, model.getObjects().toArray()[0]);

        assertEquals("Option 2", model.getLabel(1));
        assertEquals("Option 2", model.getValue(1));
        assertSame(option2, model.getObjects().toArray()[1]);
    }

    @Test
    public void testInitializeWithEmptyCollection() {
        final Collection emptyCollection = new ArrayList<>();

        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(emptyCollection);

        model.collection(collection);
        model.label(label);
        model.value(value);

        assertEquals(1, model.getOptionCount());

        final Object emptyOption = model.getObjects().toArray()[0];

        assertNull(model.getLabel(0));
        assertNull(model.getValue(0));
        assertSame(emptyOption, model.getObjects().toArray()[0]);
    }

    @Test
    public void testTranslateValue() {
        final Object option1 = new Object() {
            @Override
            public String toString() {
                return "Option 1";
            }
        };
        final Object option2 = new Object() {
            @Override
            public String toString() {
                return "Option 2";
            }
        };
        final Object translatedOption1 = new Object() {
            @Override
            public String toString() {
                return "Translated Option 1";
            }
        };

        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(new ArrayList<>(Arrays.asList(option1, option2)));

        model.collection(collection);
        model.label(label);
        model.value(value);
        model.translator(new ElExpression("translatorExpression"));

        final Object translatedValue = model.translateValue("Option 1");

        assertEquals("Translated Option 1", translatedValue.toString());
    }

    @Test
    public void testTranslateValueNotFound() {
        final Object option1 = new Object() {
            @Override
            public String toString() {
                return "Option 1";
            }
        };
        final Object option2 = new Object() {
            @Override
            public String toString() {
                return "Option 2";
            }
        };

        model.setComponent(component);
        model.setTopLevelComponent(topLevelComponent);

        when(collection.eval(any(StandaloneComponent.class), any(Component.class))).thenReturn(new ArrayList<>(Arrays.asList(option1, option2)));

        model.collection(collection);
        model.label(label);
        model.value(value);
        model.translator(new ElExpression("translatorExpression"));

        assertNull(model.translateValue("Option 3"));
    }
}