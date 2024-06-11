package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.*;
import org.mockito.annotations.Mock;

import wheel.components.*;
import wheel.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 @Mock
 private Component component;
 @Mock
 private ElExpression labelMock;
 @Mock
 private ElExpression valueMock;
 @Mock
 private ElExpression collectionMock;
 @Mock
 private ElExpression translatorMock;
 @Mock
 private Collection objectsMock;

 @Before
 public void setUp() {
 this.model = new DynamicSelectModel();
 when(labelMock.getExpression()).thenReturn("label expression");
 when(valueMock.getExpression()).thenReturn("value expression");
 when(collectionMock.getExpression()).thenReturn("collection expression");
 when(translatorMock.getExpression()).thenReturn("translator expression");
 when(objectsMock.size()).thenReturn(3);
 }

 @Test
 public void testGetOptionCountWithCollectionAndLabelsAndValues() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value("value expression");

 assertEquals(3, this.model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCountWithNullCollection() {
 this.model.collection(null);
 this.model.label("label expression");
 this.model.value("value expression");

 try {
 this.model.getOptionCount();
 fail("Expected WheelException to be thrown");
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCountWithNullLabel() {
 this.model.collection("collection expression");
 this.model.label(null);
 this.model.value("value expression");

 try {
 this.model.getOptionCount();
 fail("Expected WheelException to be thrown");
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCountWithNullValue() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value(null);

 try {
 this.model.getOptionCount();
 fail("Expected WheelException to be thrown");
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test
 public void testGetLabelWithValidIndex() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value("value expression");
 this.model.setComponent(this.component);
 this.model.getObjects();

 String label = this.model.getLabel(0);
 assertEquals("label expression", label);
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithInvalidIndex() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value("value expression");
 this.model.setComponent(this.component);
 this.model.getObjects();

 try {
 this.model.getLabel(5);
 fail("Expected WheelException to be thrown");
 } catch (WheelException e) {
 assertEquals("Could not evaluate expression label expression to create a label for dynamic ISelectModel. Index was 5", e.getMessage());
 }
 }

 @Test
 public void testGetValueWithValidIndex() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value("value expression");
 this.model.setComponent(this.component);
 this.model.getObjects();

 String value = this.model.getValue(0);
 assertEquals("value expression", value);
 }

 @Test(expected = WheelException.class)
 public void testGetValueWithInvalidIndex() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value("value expression");
 this.model.setComponent(this.component);
 this.model.getObjects();

 try {
 this.model.getValue(5);
 fail("Expected WheelException to be thrown");
 } catch (WheelException e) {
 assertEquals("Could not evaluate expression value expression to create a value for dynamic ISelectModel. Index was 5", e.getMessage());
 }
 }

 @Test
 public void testTranslateValueWithValidValue() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value("value expression");
 this.model.translator("translator expression");
 this.model.getObjects();

 Object translatedValue = this.model.translateValue("value expression");
 assertEquals(this.model.getObjects().toArray()[0], translatedValue);
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueWithInvalidValue() {
 this.model.collection("collection expression");
 this.model.label("label expression");
 this.model.value("value expression");
 this.model.translator("translator expression");
 this.model.getObjects();

 try {
 this.model.translateValue("invalid value expression");
 fail("Expected WheelException to be thrown");
 } catch (WheelException e) {
 assertEquals("Could not translate value 'invalid value expression'.", e.getMessage());
 }
 }
}