package wheel.util.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;
import wheel.util.ElExpression;
import wheel.util.WheelException;

public class DynamicSelectModelTest {

 private Component component;
 private StandaloneComponent topLevelComponent;
 private DynamicSelectModel model;

 @Test
 public void testConstructorWithCollectionLabelValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  ElExpression label = new ElExpression("toString()");
  ElExpression value = new ElExpression("ordinal()");

  model = new DynamicSelectModel(collection, label, value);

  assertEquals(3, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();

  new DynamicSelectModel(null, new ElExpression("toString()"), new ElExpression("ordinal()"));
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutLabel() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");

  new DynamicSelectModel(collection, null, new ElExpression("ordinal()"));
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  ElExpression label = new ElExpression("toString()");

  new DynamicSelectModel(collection, label, null);
 }

 @Test
 public void testCollectionMethodWithStringValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model = model.collection("['apple', 'banana', 'cherry']");

  assertEquals(3, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testCollectionMethodWithNullCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model.collection(null);
 }

 @Test
 public void testLabelMethodWithStringValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model = model.label("toString()");

  assertEquals("toString", model.label.getExpression());
 }

 @Test(expected = WheelException.class)
 public void testLabelMethodWithNullLabel() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model.label(null);
 }

 @Test
 public void testValueMethodWithStringValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model = model.value("ordinal()");

  assertEquals("ordinal", model.value.getExpression());
 }

 @Test(expected = WheelException.class)
 public void testValueMethodWithNullValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model.value(null);
 }

 @Test
 public void testEnumerationMethod() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model = model.enumeration("java.lang.Enum");

  assertEquals(3, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testEnumerationMethodWithNullEnumeration() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model.enumeration(null);
 }

 @Test
 public void testTranslatorMethodWithStringValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model = model.translator("toString()");

  assertEquals("toString", model.translator.getExpression());
 }

 @Test(expected = WheelException.class)
 public void testTranslatorMethodWithNullTranslator() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model.translator(null);
 }

 @Test
 public void testEmptyMethodWithObjectValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  model = model.empty(new Object());

  assertNotNull(model.getObjects());
  assertTrue(model.getObjects().size() > 0);
 }

 @Test
 public void testGetOptionCount() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  assertEquals(3, model.getOptionCount());
 }

 @Test
 public void testGetLabelWithValidIndex() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  assertEquals("apple", model.getLabel(0));
  assertEquals("banana", model.getLabel(1));
  assertEquals("cherry", model.getLabel(2));
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithInvalidIndex() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  model.getLabel(-1);
 }

 @Test
 public void testGetValueWithValidIndex() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  assertEquals("apple", model.getValue(0));
  assertEquals("banana", model.getValue(1));
  assertEquals("cherry", model.getValue(2));
 }

 @Test(expected = WheelException.class)
 public void testGetValueWithInvalidIndex() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  model.getValue(-1);
 }

 @Test
 public void testTranslateValueWithValidValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  assertEquals("apple", model.translateValue("1"));
  assertEquals("banana", model.translateValue("2"));
  assertEquals("cherry", model.translateValue("3"));
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueWithInvalidValue() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  model.translateValue("4");
 }

 @Test
 public void testGetObjectsWithEmptyCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  model = new DynamicSelectModel();

  assertEquals(0, model.getObjects().size());
 }

 @Test
 public void testGetObjectsWithValidCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  List<Object> expected = new ArrayList<Object>();
  expected.add("apple");
  expected.add("banana");
  expected.add("cherry");

  List<Object> actual = new ArrayList<Object>();
  for (Object o : model.getObjects()) {
   actual.add(o);
  }

  assertEquals(expected, actual);
 }

 @Test
 public void testGetObjectsWithMixedCollection() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 1, 'banana', 2, 'cherry', 3]");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  List<Object> expected = new LinkedList<Object>();
  expected.add("apple");
  expected.add(1);
  expected.add("banana");
  expected.add(2);
  expected.add("cherry");
  expected.add(3);

  List<Object> actual = new ArrayList<Object>();
  for (Object o : model.getObjects()) {
   actual.add(o);
  }

  assertEquals(expected, actual);
 }

 @Test
 public void testResetMethod() {
  component = new Component();
  topLevelComponent = new StandaloneComponent();
  ElExpression collection = new ElExpression("['apple', 'banana', 'cherry']");
  model = new DynamicSelectModel(collection, new ElExpression("toString()"), new ElExpression("ordinal()"));

  model.reset();

  assertEquals(0, model.getObjects().size());
 }
}