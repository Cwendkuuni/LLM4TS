package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;
import wheel.*;

public class DynamicSelectModelTest {

 ISelectModel dynamicSelectModel;
 ElExpression labelMock, valueMock, collectionMock, translatorMock;
 Component componentMock, topLevelComponentMock;

 @Test
 public void testDynamicSelectModel() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel.getObjects());
 assertNull(dynamicSelectModel.getLabel(0));
 assertNull(dynamicSelectModel.getValue(0));
 }

 @Test
 public void testDynamicSelectModelWithValidArguments() {
 labelMock = mock(ElExpression.class);
 valueMock = mock(ElExpression.class);
 collectionMock = mock(ElExpression.class);
 topLevelComponentMock = mock(Component.class);
 componentMock = mock(Component.class);

 dynamicSelectModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
 dynamicSelectModel.setComponent(componentMock);
 dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);

 verify(collectionMock, times(1)).eval(topLevelComponentMock, componentMock);
 List<Object> objects = new ArrayList<>();
 when(collectionMock.eval(topLevelComponentMock, componentMock)).thenReturn(objects.toArray());

 assertEquals(objects.size(), dynamicSelectModel.getOptionCount());

 for (int i = 0; i < objects.size(); i++) {
 String label = dynamicSelectModel.getLabel(i);
 String value = dynamicSelectModel.getValue(i);

 String labelValue = labelMock.eval(objects.get(i), componentMock).toString();
 assertEquals(labelValue, label);

 String valueValue = valueMock.eval(objects.get(i), componentMock).toString();
 assertEquals(valueValue, value);
 }
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullCollection() {
 labelMock = mock(ElExpression.class);
 valueMock = mock(ElExpression.class);
 collectionMock = null;
 topLevelComponentMock = mock(Component.class);
 componentMock = mock(Component.class);

 try {
 dynamicSelectModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
 dynamicSelectModel.setComponent(componentMock);
 dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);
 }
 catch (WheelException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 collectionMock.errorMessage("Could not evaluate expression " + collectionMock + " to create a dynamic ISelectModel.");
 verify(collectionMock, times(1)).errorMessage("Could not evaluate expression " + collectionMock + " to create a dynamic ISelectModel.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullLabel() {
 labelMock = null;
 valueMock = mock(ElExpression.class);
 collectionMock = mock(ElExpression.class);
 topLevelComponentMock = mock(Component.class);
 componentMock = mock(Component.class);

 try {
 dynamicSelectModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
 dynamicSelectModel.setComponent(componentMock);
 dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);
 }
 catch (WheelException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 labelMock.errorMessage("Could not evaluate expression " + labelMock + " to create a label for dynamic ISelectModel.");
 verify(labelMock, times(1)).errorMessage("Could not evaluate expression " + labelMock + " to create a label for dynamic ISelectModel.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullValue() {
 labelMock = mock(ElExpression.class);
 valueMock = null;
 collectionMock = mock(ElExpression.class);
 topLevelComponentMock = mock(Component.class);
 componentMock = mock(Component.class);

 try {
 dynamicSelectModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
 dynamicSelectModel.setComponent(componentMock);
 dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);
 }
 catch (WheelException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 valueMock.errorMessage("Could not evaluate expression " + valueMock + " to create a value for dynamic ISelectModel.");
 verify(valueMock, times(1)).errorMessage("Could not evaluate expression " + valueMock + " to create a value for dynamic ISelectModel.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithInvalidCollection() {
 labelMock = mock(ElExpression.class);
 valueMock = mock(ElExpression.class);
 collectionMock = new ElExpression("invalidCollection");
 topLevelComponentMock = mock(Component.class);
 componentMock = mock(Component.class);

 try {
 dynamicSelectModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
 dynamicSelectModel.setComponent(componentMock);
 dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);
 }
 catch (WheelException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 collectionMock.errorMessage("Could not evaluate expression " + collectionMock + " to create a dynamic ISelectModel.");
 verify(collectionMock, times(1)).errorMessage("Could not evaluate expression " + collectionMock + " to create a dynamic ISelectModel.");
 }

 @Test
 public void testTranslateValue() {
 labelMock = mock(ElExpression.class);
 valueMock = mock(ElExpression.class);
 collectionMock = mock(ElExpression.class);
 translatorMock = mock(ElExpression.class);
 topLevelComponentMock = mock(Component.class);
 componentMock = mock(Component.class);
 Object value = new Object();
 Object translatedValue = new Object();

 dynamicSelectModel = new DynamicSelectModel(collectionMock, labelMock, valueMock).translator("translator");
 dynamicSelectModel.setComponent(componentMock);
 dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);
 dynamicSelectModel.getObjects(); // Initialize objects

 when(translatorMock.eval(anyObject(), anyObject())).thenReturn(translatedValue);
 when(valueMock.eval(anyObject(), anyObject())).thenReturn(value);

 Object translatedValueResult = dynamicSelectModel.translateValue("value");
 assertEquals(translatedValue, translatedValueResult);
 }

 @Test
 public void testTranslateValueWithNullTranslator() {
 labelMock = mock(ElExpression.class);
 valueMock = mock(ElExpression.class);
 collectionMock = mock(ElExpression.class);
 topLevelComponentMock = mock(Component.class);
 componentMock = mock(Component.class);
 Object value = new Object();

 dynamicSelectModel = new DynamicSelectModel(collectionMock, labelMock, valueMock);
 dynamicSelectModel.setComponent(componentMock);
 dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);
 dynamicSelectModel.getObjects(); // Initialize objects

 assertNull(dynamicSelectModel.translateValue("value"));
 }
}