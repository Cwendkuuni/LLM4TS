package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

import wheel.*;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression label = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);

        DynamicSelectModel model = new DynamicSelectModel(collection, label, value);

        verify(collection).isValid();
        verify(label).isValid();
        verify(value).isValid();
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelConstructionWithNullCollection() {
        ElExpression label = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);
        DynamicSelectModel model = new DynamicSelectModel(null, label, value);
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelConstructionWithNullLabel() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);
        DynamicSelectModel model = new DynamicSelectModel(collection, null, value);
    }

    @Test(expected = WheelException.class)
    public void testDynamicSelectModelConstructionWithNullValue() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression label = mock(ElExpression.class);
        DynamicSelectModel model = new DynamicSelectModel(collection, label, null);
    }

    @Test
    public void testGetOptionCount() {
        ElExpression collection = mock(ElExpression.class);
        Collection objects = Arrays.asList("Option1", "Option2", "Option3");
        when(collection.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(objects);

        DynamicSelectModel model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

        assertEquals(3, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        ElExpression label = new ElExpression("Option1");
        DynamicSelectModel model = new DynamicSelectModel();
        model.label(label);

        assertEquals("Option1", model.getLabel(0));
    }

    @Test
    public void testGetValue() {
        ElExpression value = new ElExpression("1");
        DynamicSelectModel model = new DynamicSelectModel();
        model.value(value);

        assertEquals("1", model.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        ElExpression label = new ElExpression("Option1");
        ElExpression translator = new ElExpression("Option1Name");
        DynamicSelectModel model = new DynamicSelectModel().collection("options").label(label).translator(translator);
        List<Object> options = Arrays.asList(new Object[]{"Option1", "Option2", "Option3"});
        when(model.getObjects().toArray()).thenReturn(options.toArray());

        assertEquals("Option1Name", model.translateValue("1"));
    }

    @Test
    public void testTranslateValue_Null() {
        DynamicSelectModel model = new DynamicSelectModel();

        assertNull(model.translateValue(null));
    }

    @Test
    public void testGetObjects() {
        ElExpression collection = mock(ElExpression.class);
        Collection objects = Arrays.asList("Option1", "Option2", "Option3");
        when(collection.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(objects);

        DynamicSelectModel model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

        assertSame(objects, model.getObjects());
    }

    @Test
    public void testGetObjects_NullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();

        assertNull(model.getObjects());
    }

    @Test
    public void testValidate() {
        DynamicSelectModel model = new DynamicSelectModel();

        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = null;

        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label = null;

        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value = null;

        model.validate();
    }

    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.objects = Arrays.asList("Option1", "Option2");

        model.reset();

        assertNull(model.objects);
    }
}