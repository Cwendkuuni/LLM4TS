package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;
import wheel.*;

public class DynamicSelectModelTest {

 private Component component;
 private StandaloneComponent topLevelComponent;
 private Collection objects;
 private DynamicSelectModel model;

 @Before
 public void setUp() {
 component = mock(Component.class);
 topLevelComponent = mock(StandaloneComponent.class);
 objects = new ArrayList<>();
 objects.add("Option 1");
 objects.add("Option 2");
 }

 @Test
 public void testDynamicSelectModelInitialization() {
 // Test initialization with collection, label, and value
 model = new DynamicSelectModel(new ElExpression("collectionExpression"), new ElExpression("labelExpression"), new ElExpression("valueExpression"));
 assertEquals(3, model.getOptionCount());

 // Test initialization without parameters
 model = new DynamicSelectModel();
 model.collection("collectionExpression");
 model.label("labelExpression");
 model.value("valueExpression");
 assertEquals(3, model.getOptionCount());
 }

 @Test
 public void testGetLabelAndValue() {
 model = new DynamicSelectModel(new ElExpression("collectionExpression"), new ElExpression("labelExpression"), new ElExpression("valueExpression"));
 model.setComponent(component);

 when(labelExpression.eval(anyObject(), anyObject())).thenReturn("Label 1");
 when(valueExpression.eval(anyObject(), anyObject())).thenReturn("Value 1");

 assertEquals("Label 1", model.getLabel(0));
 assertEquals("Value 1", model.getValue(0));
 }

 @Test
 public void testTranslateValue() {
 model = new DynamicSelectModel(new ElExpression("collectionExpression"), new ElExpression("labelExpression"), new ElExpression("valueExpression"));
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.translator(new ElExpression("translatorExpression"));

 when(translatorExpression.eval(anyObject(), anyObject())).thenReturn("Translated Value");

 assertEquals("Translated Value", model.translateValue("Value 1"));
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsNullCollection() {
 model = new DynamicSelectModel();
 model.collection(null);
 model.label(new ElExpression("labelExpression"));
 model.value(new ElExpression("valueExpression"));
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsNullLabel() {
 model = new DynamicSelectModel();
 model.collection(new ElExpression("collectionExpression"));
 model.label(null);
 model.value(new ElExpression("valueExpression"));
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsNullValue() {
 model = new DynamicSelectModel();
 model.collection(new ElExpression("collectionExpression"));
 model.label(new ElExpression("labelExpression"));
 model.value(null);
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.validate();
 }

 @Test
 public void testGetObjects() {
 model = new DynamicSelectModel(new ElExpression("collectionExpression"), new ElExpression("labelExpression"), new ElExpression("valueExpression"));
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.collection(new ElExpression("collectionExpression"));
 model.label(new ElExpression("labelExpression"));
 model.value(new ElExpression("valueExpression"));

 when(collectionExpression.eval(anyObject(), anyObject())).thenReturn(objects.toArray());

 assertEquals(objects, model.getObjects());
 }

 @Test
 public void testReset() {
 model = new DynamicSelectModel(new ElExpression("collectionExpression"), new ElExpression("labelExpression"), new ElExpression("valueExpression"));
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.collection(new ElExpression("collectionExpression"));
 model.label(new ElExpression("labelExpression"));
 model.value(new ElExpression("valueExpression"));
 model.validate();
 model.getObjects();
 model.reset();
 assertNull(model.getObjects());
 }
}