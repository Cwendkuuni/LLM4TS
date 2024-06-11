package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import wheel.*;
import wheel.components.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private StandaloneComponent topLevelComponent;
 private ElExpression labelMock;
 private ElExpression valueMock;
 private ElExpression collectionMock;
 private ElExpression translatorMock;
 private Collection objects;

 @Before
 public void setUp() {
 component = mock(Component.class);
 topLevelComponent = mock(StandaloneComponent.class);
 labelMock = mock(ElExpression.class);
 valueMock = mock(ElExpression.class);
 collectionMock = mock(ElExpression.class);
 translatorMock = mock(ElExpression.class);
 objects = new ArrayList<>();

 model = new DynamicSelectModel();
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.collection(collectionMock);
 model.label(labelMock);
 model.value(valueMock);
 }

 @Test
 public void testDynamicSelectModelConstructor() {
 model = new DynamicSelectModel(collectionMock, labelMock, valueMock);
 assertEquals(collectionMock, model.getCollection());
 assertEquals(labelMock, model.getLabel());
 assertEquals(valueMock, model.getValue());
 }

 @Test
 public void testEmptyConstructor() {
 assertNotNull(model);
 assertNull(model.getCollection());
 assertNull(model.getLabel());
 assertNull(model.getValue());
 }

 @Test(expected = WheelException.class)
 public void testInvalidCollection() {
 model.collection(null);
 }

 @Test(expected = WheelException.class)
 public void testInvalidLabel() {
 model.label(null);
 }

 @Test(expected = WheelException.class)
 public void testInvalidValue() {
 model.value(null);
 }

 @Test
 public void testGetObjects() {
 when(collectionMock.eval(topLevelComponent, component)).thenReturn(objects);
 assertEquals(objects, model.getObjects());
 }

 @Test
 public void testGetObjectsWithNullCollection() {
 when(collectionMock.eval(topLevelComponent, component)).thenReturn(null);
 assertNull(model.getObjects());
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithInvalidCollectionType() {
 when(collectionMock.eval(topLevelComponent, component)).thenReturn(123);
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithInvalidCollectionArrayType() {
 when(collectionMock.eval(topLevelComponent, component)).thenReturn(new Object[] { 1, 2, 3 });
 }

 @Test
 public void testGetLabel() {
 when(labelMock.eval(anyObject(), component)).thenAnswer(new Answer<String>() {
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Label";
 }
 });
 assertEquals("Label", model.getLabel(0));
 }

 @Test
 public void testGetValue() {
 when(valueMock.eval(anyObject(), component)).thenAnswer(new Answer<String>() {
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Value";
 }
 });
 assertEquals("Value", model.getValue(0));
 }

 @Test
 public void testTranslateValue() {
 when(labelMock.eval(anyObject(), component)).thenReturn("Label");
 when(valueMock.eval(anyObject(), component)).thenReturn("Value");
 when(translatorMock.eval(anyObject(), component)).thenReturn("Translated Value");

 assertEquals("Translated Value", model.translateValue("Value"));
 }

 @Test
 public void testTranslateValueWithNullTranslator() {
 when(labelMock.eval(anyObject(), component)).thenReturn("Label");
 when(valueMock.eval(anyObject(), component)).thenReturn("Value");
 when(translatorMock.eval(anyObject(), component)).thenReturn(null);
 assertNull(model.translateValue("Value"));
 }

 @Test
 public void testGetOptionCount() {
 when(collectionMock.eval(topLevelComponent, component)).thenReturn(objects);
 assertEquals(objects.size(), model.getOptionCount());
 }

 @Test
 public void testReset() {
 model.reset();
 assertNull(model.getObjects());
 }
}