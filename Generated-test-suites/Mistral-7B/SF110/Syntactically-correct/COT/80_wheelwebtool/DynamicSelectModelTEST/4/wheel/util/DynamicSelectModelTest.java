package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicModel;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Test
 public void testDynamicSelectModelConstructorWithCollectionLabelValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  assertEquals(3, dynamicModel.getOptionCount());
  assertNotNull(dynamicModel.getObjects());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructorWithoutCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  new DynamicSelectModel(null, new ElExpression("label"), new ElExpression("value"));
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructorWithoutLabel() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  new DynamicSelectModel(new ElExpression("collection"), null, new ElExpression("value"));
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructorWithoutValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), null);
 }

 @Test
 public void testDynamicSelectModelGetLabel() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  String label = dynamicModel.getLabel(0);
  assertNotNull(label);
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithNegativeIndex() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  dynamicModel.getLabel(-1);
 }

 @Test
 public void testDynamicSelectModelGetValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  String value = dynamicModel.getValue(0);
  assertNotNull(value);
 }

 @Test(expected = WheelException.class)
 public void testGetValueWithNegativeIndex() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  dynamicModel.getValue(-1);
 }

 @Test
 public void testDynamicSelectModelTranslateValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  Object translatedValue = dynamicModel.translateValue("testValue");
  assertNull(translatedValue);
 }

 @Test
 public void testDynamicSelectModelTranslateValueWithValidIndex() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  Object translatedValue = dynamicModel.translateValue(dynamicModel.getValue(0));
  assertNotNull(translatedValue);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelTranslateValueWithNullValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  dynamicModel.translateValue(null);
 }

 @Test
 public void testDynamicSelectModelGetObjects() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
  List expectedObjects = Collections.singletonList("expectedObject");
  dynamicModel.setObjects(expectedObjects);
  List actualObjects = dynamicModel.getObjects();
  assertEquals(expectedObjects, actualObjects);
 }

 @Test
 public void testDynamicSelectModelEmpty() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  assertEquals(0, dynamicModel.getOptionCount());
  assertNull(dynamicModel.getObjects());
 }

 @Test
 public void testDynamicSelectModelReset() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  List objects = new ArrayList();
  objects.add("object1");
  dynamicModel.setObjects(objects);
  dynamicModel.reset();
  assertEquals(0, dynamicModel.getOptionCount());
  assertNull(dynamicModel.getObjects());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetObjectsWithNullCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.setCollection(null);
  dynamicModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetObjectsWithNullTopLevelComponent() {
  component = new Component();
  topLevelComponent = null;
  dynamicModel = new DynamicSelectModel();
  dynamicModel.setTopLevelComponent(topLevelComponent);
  dynamicModel.validate();
 }

 @Test
 public void testDynamicSelectModelCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.collection("collection");
  assertEquals("collection", dynamicModel.getCollection().getExpression());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelCollectionWithNullCollectionName() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.collection(null);
 }

 @Test
 public void testDynamicSelectModelLabel() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.label("label");
  assertEquals("label", dynamicModel.getLabel().getExpression());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelLabelWithNullLabelName() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.label(null);
 }

 @Test
 public void testDynamicSelectModelValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.value("value");
  assertEquals("value", dynamicModel.getValue().getExpression());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValueWithNullValueName() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.value(null);
 }

 @Test
 public void testDynamicSelectModelEnumeration() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.enumeration("enumeration");
  assertEquals("enumeration.values()", dynamicModel.getCollection().getExpression());
  assertEquals("toString()", dynamicModel.getLabel().getExpression());
  assertEquals("ordinal()", dynamicModel.getValue().getExpression());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelEnumerationWithNullEnumerationName() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.enumeration(null);
 }

 @Test
 public void testDynamicSelectModelTranslator() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.translator("translator");
  assertEquals("translator", dynamicModel.getTranslator().getExpression());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelTranslatorWithNullTranslatorName() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.translator(null);
 }

 @Test
 public void testDynamicSelectModelEmpty() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.empty(new Object());
  assertEquals(1, dynamicModel.getOptionCount());
  assertNotNull(dynamicModel.getObjects());
  assertEquals(new Object(), dynamicModel.getObjects().toArray()[0]);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelEmptyWithNullObject() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  dynamicModel = new DynamicSelectModel();
  dynamicModel.empty(null);
 }
}