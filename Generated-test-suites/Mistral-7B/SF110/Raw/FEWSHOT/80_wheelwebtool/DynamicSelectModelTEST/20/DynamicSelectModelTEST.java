package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import wheel.*;

public class DynamicSelectModelTest {

    private ElExpression collectionExpression;
    private ElExpression labelExpression;
    private ElExpression valueExpression;
    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        collectionExpression = mock(ElExpression.class);
        labelExpression = mock(ElExpression.class);
        valueExpression = mock(ElExpression.class);
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collectionExpression);
        dynamicSelectModel.label(labelExpression);
        dynamicSelectModel.value(valueExpression);
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testEmptyConstructor() {
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testConstructorWithCollection() {
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(Arrays.asList("Option1", "Option2", "Option3"));
        assertEquals(3, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        when(labelExpression.eval(anyObject(), component)).thenReturn("Label1");
        assertEquals("Label1", dynamicSelectModel.getLabel(0));
    }

    @Test
    public void testGetValue() {
        when(valueExpression.eval(anyObject(), component)).thenReturn("Value1");
        assertEquals("Value1", dynamicSelectModel.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        final Object translatedValue = new Object() {
            @Override
            public String toString() {
                return "TranslatedValue";
            }
        };
        doReturn(translatedValue).when(translatorExpression).eval(anyObject(), component);
        when(labelExpression.eval(anyObject(), component)).thenReturn("Label1");
        when(valueExpression.eval(anyObject(), component)).thenReturn("Value1");
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(Arrays.asList("Option1", "Option2", "Option3"));
        assertEquals("TranslatedValue", dynamicSelectModel.translateValue("Value1"));
    }

    @Test(expected = WheelException.class)
    public void testInvalidCollection() {
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(null);
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testInvalidCollectionType() {
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(new ArrayList<>());
        dynamicSelectModel.getObjects();
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(new Object[] { null });
        dynamicSelectModel.getObjects();
        when(collectionExpression.eval(topLevelComponent, component)).thenReturn(123);
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testEmptyOptionModel() {
        dynamicSelectModel.reset();
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }
}