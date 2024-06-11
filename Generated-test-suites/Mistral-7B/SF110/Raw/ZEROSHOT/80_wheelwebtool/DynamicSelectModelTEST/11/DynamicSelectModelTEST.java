package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 component = Mockito.mock(Component.class);
 topLevelComponent = Mockito.mock(StandaloneComponent.class);
 dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testGetOptionCount_WhenObjectsArePresent_ShouldReturnSizeOfObjects() {
 List<Object> objects = new ArrayList<>();
 objects.add(new Object());
 objects.add(new Object());
 dynamicSelectModel.setObjects(objects);
 assertEquals(objects.size(), dynamicSelectModel.getOptionCount());
 }

 @Test(expected = RuntimeException.class)
 public void testGetOptionCount_WhenObjectsAreNull_ShouldThrowRuntimeException() {
 dynamicSelectModel.setObjects(null);
}

 @Test
 public void testGetLabel_WhenIndexIsValid_ShouldReturnLabelValue() {
 String labelExpression = "labelExpression";
 dynamicSelectModel.setLabel(new ElExpression(labelExpression));
 Object[] labels = new Object[]{"Label 1", "Label 2"};
 dynamicSelectModel.setObjects(new ArrayList<>(Arrays.asList(labels)));
 assertEquals("Label 1", dynamicSelectModel.getLabel(0));
}

 @Test(expected = RuntimeException.class)
 public void testGetLabel_WhenIndexIsInvalid_ShouldThrowRuntimeException() {
 String labelExpression = "labelExpression";
 dynamicSelectModel.setLabel(new ElExpression(labelExpression));
 Object[] labels = new Object[]{"Label 1", "Label 2"};
 dynamicSelectModel.setObjects(new ArrayList<>(Arrays.asList(labels)));
 dynamicSelectModel.getLabel(-1);
}

 @Test
 public void testGetValue_WhenIndexIsValid_ShouldReturnValueValue() {
 String valueExpression = "valueExpression";
 dynamicSelectModel.setValue(new ElExpression(valueExpression));
 Object[] values = new Object[]{"Value 1", "Value 2"};
 dynamicSelectModel.setObjects(new ArrayList<>(Arrays.asList(values)));
 assertEquals("Value 1", dynamicSelectModel.getValue(0));
}

 @Test(expected = RuntimeException.class)
 public void testGetValue_WhenIndexIsInvalid_ShouldThrowRuntimeException() {
 String valueExpression = "valueExpression";
 dynamicSelectModel.setValue(new ElExpression(valueExpression));
 Object[] values = new Object[]{"Value 1", "Value 2"};
 dynamicSelectModel.setObjects(new ArrayList<>(Arrays.asList(values)));
 dynamicSelectModel.getValue(-1);
}

 @Test
 public void testTranslateValue_WhenValidValueIsProvided_ShouldReturnCorrespondingObject() {
 Object translatedObject = new Object();
 String value = "Value to translate";
 dynamicSelectModel.setValue(new ElExpression(value));
 dynamicSelectModel.setObjects(new ArrayList<>());
 dynamicSelectModel.setObjects(new ArrayList<>());
 dynamicSelectModel.setObjects(new ArrayList<>());
 dynamicSelectModel.addObject(translatedObject);
 assertEquals(translatedObject, dynamicSelectModel.translateValue(value));
}

 @Test
 public void testTranslateValue_WhenInvalidValueIsProvided_ShouldReturnNull() {
 dynamicSelectModel.setValue(new ElExpression("ValueExpression"));
 dynamicSelectModel.setObjects(new ArrayList<>());
 assertNull(dynamicSelectModel.translateValue("InvalidValue"));
}

 @Test
 public void testGetObjects_WhenValidCollectionIsProvided_ShouldReturnCollection() {
 ElExpression collectionExpression = new ElExpression("collectionExpression");
 dynamicSelectModel.setCollection(collectionExpression);
 Object[] collectionValues = new Object[]{"Value 1", "Value 2"};
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setComponent(component);
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(collectionValues);
 assertEquals(Arrays.asList(collectionValues), dynamicSelectModel.getObjects());
}

 @Test(expected = RuntimeException.class)
 public void testGetObjects_WhenCollectionExpressionFailsToEvaluate_ShouldThrowRuntimeException() {
 ElExpression collectionExpression = new ElExpression("collectionExpression");
 dynamicSelectModel.setCollection(collectionExpression);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setComponent(component);
 Mockito.when(collectionExpression.eval(Mockito.any(), Mockito.any())).thenThrow(new RuntimeException("Collection expression failed to evaluate"));
 assertNull(dynamicSelectModel.getObjects());
}

 @Test
 public void testGetObjects_WhenCollectionIsEmpty_ShouldReturnEmptyCollection() {
 ElExpression collectionExpression = new ElExpression("emptyCollectionExpression");
 dynamicSelectModel.setCollection(collectionExpression);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setComponent(component);
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(new Object[0]);
 assertEquals(Collections.emptyList(), dynamicSelectModel.getObjects());
}

 @Test
 public void testEmpty_WhenEmptyObjectIsProvided_ShouldAddItToObjects() {
 Object emptyObject = new Object();
 dynamicSelectModel.empty(emptyObject);
 dynamicSelectModel.setObjects(new LinkedList<>());
 dynamicSelectModel.addObject(new Object());
 assertEquals(2, dynamicSelectModel.getOptionCount());
}
}