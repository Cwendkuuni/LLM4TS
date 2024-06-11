package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private ElExpression collectionExpression;
 private ElExpression labelExpression;
 private ElExpression valueExpression;
 private Component component;
 private ElExpression translatorExpression;
 private Object emptyObject;

 @Before
 public void setup() {
 collectionExpression = Mockito.mock(ElExpression.class);
 labelExpression = Mockito.mock(ElExpression.class);
 valueExpression = Mockito.mock(ElExpression.class);
 component = mock(Component.class);
 translatorExpression = Mockito.mock(ElExpression.class);
 emptyObject = new Object();

 dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 dynamicSelectModel.collection(collectionExpression);
 dynamicSelectModel.label(labelExpression);
 dynamicSelectModel.value(valueExpression);

 dynamicSelectModel.validate();

 assertEquals(collectionExpression, dynamicSelectModel.getCollection());
 assertEquals(labelExpression, dynamicSelectModel.getLabelExpression());
 assertEquals(valueExpression, dynamicSelectModel.getValueExpression());
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutCollection() {
 dynamicSelectModel.label(labelExpression);
 dynamicSelectModel.value(valueExpression);

 dynamicSelectModel.validate();
 }

 @Test
 public void testEnumCollection() {
 dynamicSelectModel.enumeration("MyEnumeration");

 assertEquals(Collections.singletonList("MyEnumeration.values()"), dynamicSelectModel.getCollectionExpressions());
 assertEquals("toString()", dynamicSelectModel.getLabelExpression().getExpression());
 assertEquals("ordinal()", dynamicSelectModel.getValueExpression().getExpression());
 }

 @Test
 public void testTranslator() {
 dynamicSelectModel.translator("MyTranslator");

 assertEquals("MyTranslator", dynamicSelectModel.getTranslatorExpression().getExpression());
 }

 @Test
 public void testEmpty() {
 dynamicSelectModel.empty(emptyObject);

 List<Object> objects = dynamicSelectModel.getObjects();
 assertEquals(1, objects.size());
 assertEquals(emptyObject, objects.get(0));
 }

 @Test
 public void testGetOptionCount() {
 List<Object> objects = Arrays.asList(new Object[]{"One", 1, "Two", 2, "Three", 3});
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(One, 1, Two, 2, Three, 3))"));
 dynamicSelectModel.label(new ElExpression("toString()"));
 dynamicSelectModel.value(new ElExpression("toString()"));

 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));

 dynamicSelectModel.validate();
 dynamicSelectModel.setObjects(objects);

 assertEquals(objects.size(), dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 List<Object> objects = Arrays.asList(new Object[]{"One", 1, "Two", 2, "Three", 3});
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(One, 1, Two, 2, Three, 3))"));
 dynamicSelectModel.label(new ElExpression("toString()"));
 dynamicSelectModel.value(new ElExpression("toString()"));
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));

 dynamicSelectModel.validate();
 dynamicSelectModel.setObjects(objects);

 assertEquals("One", dynamicSelectModel.getLabel(0));
 assertEquals("Two", dynamicSelectModel.getLabel(1));
 assertEquals("Three", dynamicSelectModel.getLabel(2));
 }

 @Test
 public void testGetValue() {
 List<Object> objects = Arrays.asList(new Object[]{1, "One", 2, "Two", 3, "Three"});
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(1, One, 2, Two, 3, Three))"));
 dynamicSelectModel.label(new ElExpression("toString()"));
 dynamicSelectModel.value(new ElExpression("toString()"));
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));

 dynamicSelectModel.validate();
 dynamicSelectModel.setObjects(objects);

 assertEquals("1", dynamicSelectModel.getValue(0));
 assertEquals("One", dynamicSelectModel.getValue(1));
 assertEquals("2", dynamicSelectModel.getValue(2));
 assertEquals("Two", dynamicSelectModel.getValue(3));
 assertEquals("3", dynamicSelectModel.getValue(4));
 assertEquals("Three", dynamicSelectModel.getValue(5));
 }

 @Test
 public void testTranslateValue() {
 List<Object> objects = Arrays.asList(new Object[]{1, "One", 2, "Two", 3, "Three"});
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(1, One, 2, Two, 3, Three))"));
 dynamicSelectModel.label(new ElExpression("toString()"));
 dynamicSelectModel.value(new ElExpression("toString()"));
 dynamicSelectModel.translator(new ElExpression("toString()"));
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));
 dynamicSelectModel.setObjects(objects);

 assertEquals(1, (int)dynamicSelectModel.translateValue("1").hashCode());
 assertEquals("One", dynamicSelectModel.translateValue("One").toString());
 assertEquals("Two", dynamicSelectModel.translateValue("Two").toString());
 assertEquals("Three", dynamicSelectModel.translateValue("Three").toString());
 assertNull(dynamicSelectModel.translateValue("Four"));
 }

 @Test
 public void testGetObjects() {
 List<Object> objects = Arrays.asList(new Object[]{"One", 1, "Two", 2, "Three", 3});
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(One, 1, Two, 2, Three, 3))"));
 dynamicSelectModel.label(new ElExpression("toString()"));
 dynamicSelectModel.value(new ElExpression("toString()"));
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));

 dynamicSelectModel.validate();
 dynamicSelectModel.setObjects(new ArrayList<Object>());

 List<Object> result = dynamicSelectModel.getObjects();
 assertEquals(0, result.size());

 dynamicSelectModel.setObjects(objects);
 result = dynamicSelectModel.getObjects();
 assertEquals(objects, result);
 }

 @Test
 public void testGetComponent() {
 assertSame(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testSetComponent() {
 Component newComponent = mock(Component.class);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setComponent(newComponent);

 assertSame(newComponent, dynamicSelectModel.getComponent());
 }

 @Test
 public void testGetTopLevelComponent() {
 assertSame(mock(StandaloneComponent.class), dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testSetTopLevelComponent() {
 StandaloneComponent newTopLevelComponent = mock(StandaloneComponent.class);
 dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));
 dynamicSelectModel.setTopLevelComponent(newTopLevelComponent);

 assertSame(newTopLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testValidate() {
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithoutCollection() {
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithoutLabel() {
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(One, 1, Two, 2, Three, Three))"));
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithoutValue() {
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(One, 1, Two, 2, Three, Three))"));
 dynamicSelectModel.label(new ElExpression("toString()"));
 dynamicSelectModel.validate();
 }

 @Test
 public void testReset() {
 List<Object> objects = Arrays.asList(new Object[]{"One", 1, "Two", 2, "Three", 3});
 dynamicSelectModel.collection(new ElExpression("new java.util.ArrayList(Arrays.asList(One, 1, Two, 2, Three, Three))"));
 dynamicSelectModel.label(new ElExpression("toString()"));
 dynamicSelectModel.value(new ElExpression("toString()"));
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));
 dynamicSelectModel.validate();
 dynamicSelectModel.setObjects(objects);

 dynamicSelectModel.reset();

 assertNull(dynamicSelectModel.getObjects());
 }
}