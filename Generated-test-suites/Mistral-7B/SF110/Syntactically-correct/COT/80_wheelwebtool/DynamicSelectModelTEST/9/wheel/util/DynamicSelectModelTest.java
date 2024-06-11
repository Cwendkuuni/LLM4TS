package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.*;
import wheel.exceptions.WheelException;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private ElExpression collection;
 private ElExpression label;
 private ElExpression value;

 @Test
 public void testDynamicSelectModelConstructorWithCollectionLabelValue() {
  component = new StandaloneComponent();
  collection = new ElExpression("new ArrayList() { add(\"Option 1\"); add(\"Option 2\"); }");
  label = new ElExpression("toString()");
  value = new ElExpression("\"Value\"");

  model = new DynamicSelectModel(collection, label, value);

  assertEquals(2, model.getOptionCount());
  assertEquals("Option 1", model.getLabel(0));
  assertEquals("Option 1", model.getValue(0));
  assertEquals("Option 2", model.getLabel(1));
  assertEquals("Option 2", model.getValue(1));
 }

 @Test
 public void testDynamicSelectModelEmptyConstructor() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  assertNull(model.getObjects());
 }

 @Test
 public void testDynamicSelectModelCollectionMethod() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.collection("new ArrayList() { add(\"Option 1\"); add(\"Option 2\"); }");

  assertEquals(2, model.getOptionCount());
  assertEquals("Option 1", model.getLabel(0));
  assertEquals("Option 1", model.getValue(0));
  assertEquals("Option 2", model.getLabel(1));
  assertEquals("Option 2", model.getValue(1));
 }

 @Test
 public void testDynamicSelectModelLabelMethod() {
  component = new StandaloneComponent();
  collection = new ElExpression("new ArrayList() { add(\"Option 1\"); add(\"Option 2\"); }");
  model = new DynamicSelectModel(collection, null, null);
  model.label("toString()");

  assertEquals(2, model.getOptionCount());
  assertEquals("Option 1", model.getLabel(0));
  assertEquals("Option 1", model.getValue(0));
  assertEquals("Option 2", model.getLabel(1));
  assertEquals("Option 2", model.getValue(1));
 }

 @Test
 public void testDynamicSelectModelValueMethod() {
  component = new StandaloneComponent();
  collection = new ElExpression("new ArrayList() { add(\"Option 1\"); add(\"Option 2\"); }");
  model = new DynamicSelectModel(collection, null, null);
  model.value("\"Value 1\"");

  assertEquals(2, model.getOptionCount());
  assertEquals("Option 1", model.getLabel(0));
  assertEquals("Value 1", model.getValue(0));
  assertEquals("Option 2", model.getLabel(1));
  assertEquals("Option 2", model.getValue(1));
 }

 @Test
 public void testDynamicSelectModelEnumsMethod() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.enumeration("java.lang.Enum.class");

  assertEquals(72, model.getOptionCount());
  // Only testing the first few enum labels and values
  assertEquals("EAGER", model.getLabel(0));
  assertEquals("EAGER", model.getValue(0));
  assertEquals("LAZY", model.getLabel(1));
  assertEquals("LAZY", model.getValue(1));
 }

 @Test
 public void testDynamicSelectModelTranslatorMethod() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.translator("toString()");

  Collection objects = model.getObjects();
  Object firstOption = objects.toArray()[0];
  assertEquals("" + firstOption, model.translateValue("" + firstOption));
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelCollectionError() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.collection(null);

  try {
   model.getObjects();
  } catch (NullPointerException e) {
   fail("Expected WheelException but got NullPointerException instead.");
  }
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelEmptyList() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.collection("new ArrayList() {}");

  try {
   model.getObjects();
  } catch (WheelException e) {
   assertEquals("Could not evaluate expression new ArrayList() {} to create a dynamic ISelectModel.", e.getMessage());
  }
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelArrayError() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.collection(new ElExpression("new String[1]"));

  try {
   model.getObjects();
  } catch (WheelException e) {
   assertEquals("Expression new String[1] evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", e.getMessage());
  }
 }

 @Test
 public void testDynamicSelectModelObjectsGet() {
  component = new StandaloneComponent();
  collection = new ElExpression("new ArrayList() { add(\"Option 1\"); add(\"Option 2\"); }");
  model = new DynamicSelectModel(collection, null, null);
  Collection objects = model.getObjects();

  List<Object> expectedList = new ArrayList<>();
  expectedList.add("Option 1");
  expectedList.add("Option 2");

  assertEquals(expectedList.size(), objects.size());
  for (int i = 0; i < objects.size(); ++i) {
   assertEquals(expectedList.get(i).toString(), objects.toArray()[i].toString());
  }
 }

 @Test
 public void testDynamicSelectModelEmpty() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.reset();

  assertNull(model.getObjects());
 }

 @Test
 public void testDynamicSelectModelValidate() {
  component = new StandaloneComponent();
  model = new DynamicSelectModel();
  model.collection(null);
  model.validate();

  try {
   model.getObjects();
  } catch (NullPointerException e) {
   fail("Expected WheelException but got NullPointerException instead.");
  }
 }
}