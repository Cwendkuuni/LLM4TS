package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;
    private ElExpression collectionExpression;
    private ElExpression labelExpression;
    private ElExpression valueExpression;
    private Component component;
    private StandaloneComponent topLevelComponent;

    @Before
    public void initialize() {
        collectionExpression = Mockito.mock(ElExpression.class);
        labelExpression = Mockito.mock(ElExpression.class);
        valueExpression = Mockito.mock(ElExpression.class);
        component = Mockito.mock(Component.class);
        topLevelComponent = Mockito.mock(StandaloneComponent.class);

        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collectionExpression);
        dynamicSelectModel.label(labelExpression);
        dynamicSelectModel.value(valueExpression);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testGetOptionCount_ValidCollection_ReturnsSizeOfCollection() {
        // Given
        List<Object> collection = new ArrayList<>();
        collection.add("Option 1");
        collection.add("Option 2");
        collectionExpression.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(collection);

        // When
        int optionCount = dynamicSelectModel.getOptionCount();

        // Then
        assertEquals(2, optionCount);
    }

    @Test(expected = RuntimeException.class)
    public void testGetLabel_InvalidIndex_ThrowsException() {
        // Given
        dynamicSelectModel.reset();

        // When & Then
        dynamicSelectModel.getLabel(1);
    }

    @Test
    public void testGetLabel_ValidIndex_ReturnsLabelString() {
        // Given
        String label = "Option Label";
        labelExpression.when(labelExpression.eval(Mockito.anyObject(), component)).thenReturn(label);
        List<Object> collection = new ArrayList<>();
        collection.add(Mockito.mock(Object.class));
        collection.add(Mockito.mock(Object.class));
        collectionExpression.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(collection);

        // When
        String labelFromModel = dynamicSelectModel.getLabel(0);

        // Then
        assertEquals(label, labelFromModel);
    }

    @Test(expected = RuntimeException.class)
    public void testGetValue_InvalidIndex_ThrowsException() {
        // Given
        dynamicSelectModel.reset();

        // When & Then
        dynamicSelectModel.getValue(1);
    }

    @Test
    public void testGetValue_ValidIndex_ReturnsValueString() {
        // Given
        String value = "Option Value";
        valueExpression.when(valueExpression.eval(Mockito.anyObject(), component)).thenReturn(value);
        List<Object> collection = new ArrayList<>();
        collection.add(Mockito.mock(Object.class));
        collection.add(Mockito.mock(Object.class));
        collection.add(Mockito.mock(Object.class, "OptionWithValue"));
        collectionExpression.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(collection);

        // When
        String valueFromModel = dynamicSelectModel.getValue(2);

        // Then
        assertEquals(value, valueFromModel);
    }

    @Test
    public void testTranslateValue_ValidValue_ReturnsTranslatedObject() {
        // Given
        Object translatedObject = Mockito.mock(Object.class, "TranslatedObject");
        String value = "Option Value";
        String translatedValue = "Translated Value";
        valueExpression.when(valueExpression.eval(Mockito.anyObject(), component)).thenReturn(value);
        ElExpression translatorExpression = Mockito.mock(ElExpression.class);
        translatorExpression.when(translatorExpression.eval(Mockito.anyObject(), component)).thenReturn(translatedObject);
        dynamicSelectModel.translator("translatorExpression");

        // When
        Object translatedValueFromModel = dynamicSelectModel.translateValue(translatedValue);

        // Then
        assertEquals(translatedObject, translatedValueFromModel);
    }

    @Test
    public void testTranslateValue_InvalidValue_ReturnsNull() {
        // Given
        dynamicSelectModel.reset();

        // When & Then
        assertNull(dynamicSelectModel.translateValue("Invalid Value"));
    }

    @Test
    public void testGetObjects_ValidCollection_ReturnsCollection() {
        // Given
        List<Object> collection = new ArrayList<>();
        collection.add("Option 1");
        collection.add("Option 2");
        collectionExpression.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(collection);

        // When
        Collection objects = dynamicSelectModel.getObjects();

        // Then
        assertEquals(collection, objects);
    }

    @Test
    public void testGetObjects_EmptyCollection_ReturnsEmptyCollection() {
        // Given
        dynamicSelectModel.reset();
        Collection emptyCollection = new ArrayList<>();

        // When
        Collection objects = dynamicSelectModel.getObjects();

        // Then
        assertEquals(emptyCollection, objects);
    }

    @Test
    public void testSetTopLevelComponent_ValidComponent_SetsTopLevelComponent() {
        // Given
        dynamicSelectModel.reset();

        // When
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        // Then
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
}