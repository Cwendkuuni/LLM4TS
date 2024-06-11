package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class DynamicSelectModelTest
{
    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression enumeration;
    private ElExpression translator;

    @Before
    public void setup() {
        component = Mockito.mock(Component.class);
        topLevelComponent = Mockito.mock(StandaloneComponent.class);
        label = Mockito.mock(ElExpression.class);
        value = Mockito.mock(ElExpression.class);
        collection = Mockito.mock(ElExpression.class);
        enumeration = Mockito.mock(ElExpression.class);
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testDynamicSelectModelEmptyConstructor() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());
        assertNull(dynamicSelectModel.getObjects());
        assertNull(dynamicSelectModel.getTranslator());
    }

    @Test
    public void testDynamicSelectModelConstructorWithExpressions() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.value("testValue");

        assertEquals("testCollection", dynamicSelectModel.getCollection().getExpression());
        assertEquals("testLabel", dynamicSelectModel.getLabel().getExpression());
        assertEquals("testValue", dynamicSelectModel.getValue().getExpression());
        assertNull(dynamicSelectModel.getObjects());
        assertNull(dynamicSelectModel.getTranslator());
    }

    @Test
    public void testDynamicSelectModelEmptyCollection() {
        dynamicSelectModel.collection("");
        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.value("testValue");

        assertEquals(0, dynamicSelectModel.getOptionCount());
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testDynamicSelectModelCollectionWithElements() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new Object[] { "Option1", "Option2", "Option3" });

        dynamicSelectModel.collection(collection);
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("ordinal()");

        List<String> expectedOptions = new ArrayList<>();
        expectedOptions.add("Option1");
        expectedOptions.add("Option2");
        expectedOptions.add("Option3");

        Collection actualOptions = dynamicSelectModel.getObjects();

        assertEquals(expectedOptions.size(), dynamicSelectModel.getOptionCount());
        assertSame(expectedOptions, actualOptions);
    }

    @Test
    public void testDynamicSelectModelEnumeration() {
        when(enumeration.eval(topLevelComponent, component)).thenReturn(new Enum[0]);

        dynamicSelectModel.enumeration("testEnumeration");
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("ordinal()");

        assertEquals(0, dynamicSelectModel.getOptionCount());
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testDynamicSelectModelTranslator() {
        when(collection.eval(topLevelComponent, component)).thenReturn(new Object[] { "Option1", "Option2", "Option3" });
        when(translator.eval(Mockito.any(), Mockito.any())).thenReturn("TranslatedValue");

        dynamicSelectModel.collection(collection);
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("ordinal()");
        dynamicSelectModel.translator(translator);

        Collection options = dynamicSelectModel.getObjects();
        Object option1 = options.toArray()[0];
        Object option2 = options.toArray()[1];

        assertEquals("Option1", dynamicSelectModel.getValue(0));
        assertEquals("TranslatedValue", dynamicSelectModel.translateValue("Option1"));
        assertEquals("Option2", dynamicSelectModel.getValue(1));
        assertNull(dynamicSelectModel.translateValue("OptionX"));
    }

    @Test
    public void testDynamicSelectModelEmpty() {
        dynamicSelectModel.collection("");

        assertNull(dynamicSelectModel.getObjects());
        assertThrows(WheelException.class, () -> dynamicSelectModel.getLabel(0));
        assertThrows(WheelException.class, () -> dynamicSelectModel.getValue(0));
    }

    @Test
    public void testDynamicSelectModelValidate() {
        dynamicSelectModel.collection("");
        dynamicSelectModel.label(null);
        dynamicSelectModel.value(null);

        assertThrows(WheelException.class, dynamicSelectModel::validate);
    }

    @Test
    public void testDynamicSelectModelReset() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("toString()");
        dynamicSelectModel.value("ordinal()");
        dynamicSelectModel.setObjects(Mockito.mock(Collection.class));

        List<Object> options = new ArrayList<>();
        options.add("Option1");
        options.add("Option2");
        dynamicSelectModel.setObjects(options);

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.getObjects());
    }
}