package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private ElExpression enumeration;
 private ElExpression translator;

 @Before
 public void setUp() {
 this.component = Mockito.mock(Component.class);
 this.label = Mockito.mock(ElExpression.class);
 this.value = Mockito.mock(ElExpression.class);
 this.collection = Mockito.mock(ElExpression.class);
 this.enumeration = Mockito.mock(ElExpression.class);
 this.translator = Mockito.mock(ElExpression.class);
 this.dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, this.value);
 assertSame(this.collection, this.dynamicSelectModel.getCollection());
 assertSame(this.label, this.dynamicSelectModel.getLabelExpression());
 assertSame(this.value, this.dynamicSelectModel.getValueExpression());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithoutCollectionLabelValue() {
 this.dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testCollectionMethod() {
 this.dynamicSelectModel.collection("testCollection");
 assertSame(this.collection, this.dynamicSelectModel.getCollection());
 }

 @Test
 public void testLabelMethod() {
 this.dynamicSelectModel.label("testLabel");
 assertSame(this.label, this.dynamicSelectModel.getLabelExpression());
 }

 @Test
 public void testValueMethod() {
 this.dynamicSelectModel.value("testValue");
 assertSame(this.value, this.dynamicSelectModel.getValueExpression());
 }

 @Test
 public void testEnumerationMethod() {
 this.dynamicSelectModel.enumeration("testEnumeration");
 assertSame(this.collection, this.dynamicSelectModel.getCollection());
 assertSame(this.label, this.dynamicSelectModel.getLabelExpression());
 assertSame(this.value, this.dynamicSelectModel.getValueExpression());
 }

 @Test
 public void testTranslatorMethod() {
 this.dynamicSelectModel.translator("testTranslator");
 assertSame(this.translator, this.dynamicSelectModel.getTranslatorExpression());
 }

 @Test
 public void testEmptyMethod() {
 this.dynamicSelectModel.empty(new Object());
 assertSame(new Object(), this.dynamicSelectModel.getEmptyObject());
 }

 @Test
 public void testGetOptionCount() {
 List list = Arrays.asList(new Object[]{"one", "two", "three"});
 when(this.collection.eval(this.component, this.component)).thenReturn(list);
 assertEquals(3, this.dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 List list = Arrays.asList(new Object[]{"one", "two", "three"});
 when(this.label.eval(anyObject(), anyObject())).thenAnswer(i -> ((Object)list.get(i)).toString());
 assertEquals("two", this.dynamicSelectModel.getLabel(1));
 }

 @Test
 public void testGetValue() {
 List list = Arrays.asList(new Object[]{"one", 1, "two"});
 when(this.value.eval(anyObject(), anyObject())).thenAnswer(i -> ((Object)list.get(i)).toString());
 assertEquals("one", this.dynamicSelectModel.getValue(0));
 }

 @Test
 public void testTranslateValue() {
 List list = Arrays.asList(new Object[]{"one", 1, "two"});
 when(this.value.eval(anyObject(), anyObject())).thenAnswer(i -> ((Object)list.get(i)).toString());
 when(this.translator.eval(anyObject(), anyObject())).thenAnswer(i -> ((Object)list.get(i)).toString() + " translated");
 assertEquals("one translated", this.dynamicSelectModel.translateValue("one"));
 }

 @Test
 public void testGetObjects() {
 List list = Arrays.asList(new Object[]{"one", "two", "three"});
 when(this.collection.eval(this.component, this.component)).thenReturn(list);
 List objects = this.dynamicSelectModel.getObjects();
 assertEquals(Collections.unmodifiableList(list), objects);
 }

 @Test
 public void testGetComponent() {
 assertSame(this.component, this.dynamicSelectModel.getComponent());
 }

 @Test
 public void testSetComponent() {
 Component newComponent = Mockito.mock(Component.class);
 this.dynamicSelectModel.setComponent(newComponent);
 assertSame(newComponent, this.dynamicSelectModel.getComponent());
 }

 @Test
 public void testGetTopLevelComponent() {
 assertNull(this.dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testSetTopLevelComponent() {
 StandaloneComponent newComponent = Mockito.mock(StandaloneComponent.class);
 this.dynamicSelectModel.setTopLevelComponent(newComponent);
 assertSame(newComponent, this.dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testValidate() {
 this.dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithoutCollection() {
 this.dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithoutLabel() {
 this.dynamicSelectModel.label(null);
 this.dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithoutValue() {
 this.dynamicSelectModel.value(null);
 this.dynamicSelectModel.validate();
 }

 @Test
 public void testReset() {
 this.dynamicSelectModel.reset();
 assertNull(this.dynamicSelectModel.getObjects());
 }
}