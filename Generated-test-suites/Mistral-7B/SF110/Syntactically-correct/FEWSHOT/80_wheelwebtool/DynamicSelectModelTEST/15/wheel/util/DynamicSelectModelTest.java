package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;
import wheel.*;

public class DynamicSelectModelTest {

 ISelectModel dynamicModel;
 Component componentMock;
 StandaloneComponent topLevelComponentMock;
 ElExpression labelMock, valueMock, collectionMock, translatorMock;
 Object emptyObject;

 @Test
 public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
  collectionMock = Mockito.mock(ElExpression.class);
  labelMock = Mockito.mock(ElExpression.class);
  valueMock = Mockito.mock(ElExpression.class);
  componentMock = Mockito.mock(Component.class);
  topLevelComponentMock = Mockito.mock(StandaloneComponent.class);

  when(collectionMock.getExpression()).thenReturn("collectionExpression");
  when(labelMock.getExpression()).thenReturn("labelExpression");
  when(valueMock.getExpression()).thenReturn("valueExpression");

  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);

  assertEquals(collectionMock, dynamicModel.getCollection());
  assertEquals(labelMock, dynamicModel.getLabel());
  assertEquals(valueMock, dynamicModel.getValue());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationWhenCollectionIsNull() {
  dynamicModel = new DynamicSelectModel(null, labelMock, valueMock);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationWhenLabelIsNull() {
  dynamicModel = new DynamicSelectModel(collectionMock, null, valueMock);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationWhenValueIsNull() {
  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, null);
 }

 @Test
 public void testDynamicSelectModelReset() {
  dynamicModel = new DynamicSelectModel();
  dynamicModel.objects = Arrays.asList("Option 1", "Option 2", "Option 3");
  dynamicModel.reset();
  assertNull(dynamicModel.objects);
 }

 @Test
 public void testDynamicSelectModelGetObjectsWhenCollectionIsCollection() {
  componentMock = Mockito.mock(Component.class);
  topLevelComponentMock = Mockito.mock(StandaloneComponent.class);
  ElExpression collectionMock = Mockito.mock(ElExpression.class);
  ElExpression labelMock = Mockito.mock(ElExpression.class);
  ElExpression valueMock = Mockito.mock(ElExpression.class);
  Object[] objects = new Object[] {"Option 1", "Option 2", "Option 3"};

  when(collectionMock.eval(topLevelComponentMock, componentMock)).thenReturn(objects);

  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);

  Collection<Object> actualObjects = dynamicModel.getObjects();
  assertEquals(Arrays.asList("Option 1", "Option 2", "Option 3"), actualObjects);
 }

 @Test
 public void testDynamicSelectModelGetObjectsWhenCollectionIsObjectArray() {
  componentMock = Mockito.mock(Component.class);
  topLevelComponentMock = Mockito.mock(StandaloneComponent.class);
  ElExpression collectionMock = Mockito.mock(ElExpression.class);
  ElExpression labelMock = Mockito.mock(ElExpression.class);
  ElExpression valueMock = Mockito.mock(ElExpression.class);
  Object[] objects = new Object[] {"Option 1", "Option 2", "Option 3"};

  when(collectionMock.eval(topLevelComponentMock, componentMock)).thenReturn(objects);

  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);

  Collection<Object> actualObjects = dynamicModel.getObjects();
  assertEquals(Arrays.asList("Option 1", "Option 2", "Option 3"), actualObjects);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetObjectsWhenCollectionIsNull() {
  dynamicModel = new DynamicSelectModel();
  dynamicModel.collection = Mockito.mock(ElExpression.class);
  dynamicModel.getObjects();
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetObjectsWhenCollectionIsNotCollection() {
  dynamicModel = new DynamicSelectModel();
  dynamicModel.collection = Mockito.mock(ElExpression.class);
  when(dynamicModel.collection.eval(Mockito.any(), Mockito.any())).thenReturn("notCollection");
  dynamicModel.getObjects();
 }

 @Test
 public void testDynamicSelectModelGetLabel() {
  componentMock = Mockito.mock(Component.class);
  topLevelComponentMock = Mockito.mock(StandaloneComponent.class);
  ElExpression collectionMock = Mockito.mock(ElExpression.class);
  ElExpression labelMock = Mockito.mock(ElExpression.class);
  ElExpression valueMock = Mockito.mock(ElExpression.class);
  Object[] objects = new Object[] {"Option 1", "Option 2", "Option 3"};

  when(labelMock.eval(objects[0], componentMock)).thenReturn("Label for Option 1");

  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
  dynamicModel.objects = Arrays.asList(objects);

  assertEquals("Label for Option 1", dynamicModel.getLabel(0));
 }

 @Test
 public void testDynamicSelectModelGetValue() {
  componentMock = Mockito.mock(Component.class);
  topLevelComponentMock = Mockito.mock(StandaloneComponent.class);
  ElExpression collectionMock = Mockito.mock(ElExpression.class);
  ElExpression labelMock = Mockito.mock(ElExpression.class);
  ElExpression valueMock = Mockito.mock(ElExpression.class);
  Object[] objects = new Object[] {"Option 1(value=Value1)", "Option 2(value=Value2)", "Option 3(value=Value3)"};

  when(valueMock.eval(objects[0], componentMock)).thenReturn("Value1");

  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
  dynamicModel.objects = Arrays.asList(objects);

  assertEquals("Value1", dynamicModel.getValue(0));
 }

 @Test
 public void testDynamicSelectModelTranslateValue() {
  componentMock = Mockito.mock(Component.class);
  topLevelComponentMock = Mockito.mock(StandaloneComponent.class);
  ElExpression collectionMock = Mockito.mock(ElExpression.class);
  ElExpression labelMock = Mockito.mock(ElExpression.class);
  ElExpression valueMock = Mockito.mock(ElExpression.class);
  ElExpression translatorMock = Mockito.mock(ElExpression.class);
  Object[] objects = new Object[] {"Option 1(value=Value1)", "Option 2(value=Value2)", "Option 3(value=Value3)"};

  when(valueMock.eval(objects[0], componentMock)).thenReturn("Value1");
  when(translatorMock.eval(objects[0], componentMock)).thenReturn("Translated Value 1");

  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, valueMock).translator("translatorMock");
  dynamicModel.objects = Arrays.asList(objects);

  assertEquals("Translated Value 1", dynamicModel.translateValue("Value1"));
 }

 @Test
 public void testDynamicSelectModelTranslateValueWhenValueNotFound() {
  componentMock = Mockito.mock(Component.class);
  topLevelComponentMock = Mockito.mock(StandaloneComponent.class);
  ElExpression collectionMock = Mockito.mock(ElExpression.class);
  ElExpression labelMock = Mockito.mock(ElExpression.class);
  ElExpression valueMock = Mockito.mock(ElExpression.class);
  ElExpression translatorMock = Mockito.mock(ElExpression.class);
  Object[] objects = new Object[] {"Option 1(value=Value1)", "Option 2(value=Value2)", "Option 3(value=Value3)"};

  when(valueMock.eval(objects[0], componentMock)).thenReturn("Value1");
  when(translatorMock.eval(objects[1], componentMock)).thenReturn("Translated Value 2");

  dynamicModel = new DynamicSelectModel(collectionMock, labelMock, valueMock).translator("translatorMock");
  dynamicModel.objects = Arrays.asList(objects);

  assertNull(dynamicModel.translateValue("Value4"));
 }
}