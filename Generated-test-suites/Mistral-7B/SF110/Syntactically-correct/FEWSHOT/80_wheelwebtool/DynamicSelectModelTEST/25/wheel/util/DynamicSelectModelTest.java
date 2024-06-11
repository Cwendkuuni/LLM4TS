package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;
import wheel.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private ElExpression translator;
 private Object emptyObject;
 private Collection objects;

 @Test
 public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
 this.label = Mockito.mock(ElExpression.class);
 this.value = Mockito.mock(ElExpression.class);
 this.collection = Mockito.mock(ElExpression.class);
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, this.value);
 assertNotNull(this.dynamicSelectModel);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructionWithNullCollection() {
 this.dynamicSelectModel = new DynamicSelectModel(null, this.label, this.value);
 fail("Should have thrown WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructionWithNullLabel() {
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, null, this.value);
 fail("Should have thrown WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructionWithNullValue() {
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, null);
 fail("Should have thrown WheelException.");
 }

 @Test
 public void testDynamicSelectModelEmptyConstruction() {
 this.dynamicSelectModel = new DynamicSelectModel();
 assertNotNull(this.dynamicSelectModel);
 }

 @Test
 public void testDynamicSelectModelCollectionMethod() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection("collection");
 assertEquals("collection", this.dynamicSelectModel.collection.getExpression());
 }

 @Test
 public void testDynamicSelectModelLabelMethod() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.label("label");
 assertEquals("label", this.dynamicSelectModel.label.getExpression());
 }

 @Test
 public void testDynamicSelectModelValueMethod() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.value("value");
 assertEquals("value", this.dynamicSelectModel.value.getExpression());
 }

 @Test
 public void testDynamicSelectModelEnumerationMethod() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.enumeration("enumeration");
 assertEquals("enumeration.values()", this.dynamicSelectModel.collection.getExpression());
 assertEquals("toString()", this.dynamicSelectModel.label.getExpression());
 assertEquals("ordinal()", this.dynamicSelectModel.value.getExpression());
 }

 @Test
 public void testDynamicSelectModelTranslatorMethod() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.translator("translator");
 assertEquals("translator", this.dynamicSelectModel.translator.getExpression());
 }

 @Test
 public void testDynamicSelectModelEmptyMethod() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.empty(new Object());
 assertNotNull(this.dynamicSelectModel.emptyObject);
 }

 @Test
 public void testDynamicSelectModelGetOptionCount() {
 this.component = Mockito.mock(Component.class);
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.collection = Mockito.mock(ElExpression.class);
 this.objects = Mockito.mock(Collection.class);
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, this.value);
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(this.objects);
 when(this.objects.size()).thenReturn(5);
 assertEquals(5, this.dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testDynamicSelectModelGetLabel() {
 this.component = Mockito.mock(Component.class);
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.label = Mockito.mock(ElExpression.class);
 this.value = Mockito.mock(ElExpression.class);
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, this.value);
 when(this.label.eval(anyObject(), anyObject())).thenReturn("Label 1");
 when(this.value.eval(anyObject(), anyObject())).thenReturn(1);
 assertEquals("Label 1", this.dynamicSelectModel.getLabel(0));
 }

 @Test
 public void testDynamicSelectModelGetValue() {
 this.component = Mockito.mock(Component.class);
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.label = Mockito.mock(ElExpression.class);
 this.value = Mockito.mock(ElExpression.class);
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, this.value);
 when(this.label.eval(anyObject(), anyObject())).thenReturn("Label 1");
 when(this.value.eval(anyObject(), anyObject())).thenReturn(1);
 assertEquals("1", this.dynamicSelectModel.getValue(0));
 }

 @Test
 public void testDynamicSelectModelTranslateValue() {
 this.component = Mockito.mock(Component.class);
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.collection = Mockito.mock(ElExpression.class);
 this.label = Mockito.mock(ElExpression.class);
 this.value = Mockito.mock(ElExpression.class);
 this.translator = Mockito.mock(ElExpression.class);
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, this.value);
 this.dynamicSelectModel.translator("translator");
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(this.objects);
 when(this.objects.toArray()[0].toString()).thenReturn("Value 1");
 when(this.translator.eval(anyObject(), anyObject())).thenReturn("Translated Value 1");
 assertEquals("Translated Value 1", this.dynamicSelectModel.translateValue("Value 1"));
 }

 @Test
 public void testDynamicSelectModelGetObjects() {
 this.component = Mockito.mock(Component.class);
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.collection = Mockito.mock(ElExpression.class);
 this.objects = Mockito.mock(Collection.class);
 this.emptyObject = Mockito.mock(Object.class);
 this.dynamicSelectModel = new DynamicSelectModel(this.collection, this.label, this.value);
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(this.objects);
 when(this.objects.toArray()).thenReturn(new Object[]{this.emptyObject, "Option 1", "Option 2"});
 assertEquals(Arrays.asList(this.emptyObject, "Option 1", "Option 2"), this.dynamicSelectModel.getObjects());
 }

 @Test
 public void testDynamicSelectModelGetComponent() {
 this.component = Mockito.mock(Component.class);
 this.dynamicSelectModel = new DynamicSelectModel();
 assertEquals(this.component, this.dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModelSetComponent() {
 this.component = Mockito.mock(Component.class);
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.setComponent(this.component);
 assertEquals(this.component, this.dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModelGetTopLevelComponent() {
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.dynamicSelectModel = new DynamicSelectModel();
 assertEquals(this.topLevelComponent, this.dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testDynamicSelectModelSetTopLevelComponent() {
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.setTopLevelComponent(this.topLevelComponent);
 assertEquals(this.topLevelComponent, this.dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testDynamicSelectModelValidate() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidateWithNullCollection() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection = null;
 this.dynamicSelectModel.validate();
 fail("Should have thrown WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidateWithNullLabel() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.label = null;
 this.dynamicSelectModel.validate();
 fail("Should have thrown WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidateWithNullValue() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.value = null;
 this.dynamicSelectModel.validate();
 fail("Should have thrown WheelException.");
 }

 @Test
 public void testDynamicSelectModelReset() {
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.objects = Mockito.mock(Collection.class);
 this.dynamicSelectModel.reset();
 assertNull(this.dynamicSelectModel.objects);
 }
}