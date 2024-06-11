package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import wheel.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private ElExpression collection;
 private ElExpression label;
 private ElExpression value;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.collection = mock(ElExpression.class);
 this.label = mock(ElExpression.class);
 this.value = mock(ElExpression.class);
 this.component = mock(Component.class);
 this.topLevelComponent = mock(StandaloneComponent.class);
 this.model = new DynamicSelectModel();
 this.model.setCollection(this.collection);
 this.model.setLabel(this.label);
 this.model.setValue(this.value);
 this.model.setComponent(this.component);
 this.model.setTopLevelComponent(this.topLevelComponent);
 }

 @Test
 public void testGetOptionCount_whenCollectionHasSize_thenReturnSize() {
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(new ArrayList());
 when(new ArrayList().size()).thenReturn(3);
 assertEquals(3, this.model.getOptionCount());
 }

 @Test
 public void testGetLabel_whenLabelExpressionEvaluatesToString_thenReturnString() {
 when(this.label.eval(eq(any(Object.class)), eq(this.component))).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 Object object = invocation.getArgumentAt(0, 0);
 return "label_" + object.toString();
 }
 });
 Object[] options = new Object[]{new Object(), new Object(), new Object()};
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(options);
 String label1 = this.model.getLabel(0);
 String label2 = this.model.getLabel(1);
 assertEquals("label_0", label1);
 assertEquals("label_1", label2);
 }

 @Test(expected = WheelException.class)
 public void testGetLabel_whenLabelExpressionEvaluatesToException_thenThrowException() {
 when(this.label.eval(eq(any(Object.class)), eq(this.component))).thenThrow(new RuntimeException());
 Object[] options = new Object[]{new Object(), new Object(), new Object()};
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(options);
 this.model.getLabel(0);
 }

 @Test
 public void testgetValue_whenValueExpressionEvaluatesToString_thenReturnString() {
 when(this.value.eval(eq(any(Object.class)), eq(this.component))).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 Object object = invocation.getArgumentAt(0, 0);
 return "value_" + object.toString();
 }
 });
 Object[] options = new Object[]{new Object(){@Override public String toString() { return "option1"; }}, new Object(){@Override public String toString() { return "option2"; }}, new Object(){@Override public String toString() { return "option3"; }}};
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(options);
 String value1 = this.model.getValue(0);
 String value2 = this.model.getValue(1);
 assertEquals("value_0", value1);
 assertEquals("value_1", value2);
 }

 @Test(expected = WheelException.class)
 public void testgetValue_whenValueExpressionEvaluatesToException_thenThrowException() {
 when(this.value.eval(eq(any(Object.class)), eq(this.component))).thenThrow(new RuntimeException());
 Object[] options = new Object[]{new Object(){@Override public String toString() { return "option1"; }}, new Object(){@Override public String toString() { return "option2"; }}, new Object(){@Override public String toString() { return "option3"; }}};
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(options);
 this.model.getValue(0);
 }

 @Test
 public void testTranslateValue_whenValueMatches_thenReturnMatchingObject() {
 Object[] options = new Object[]{new Object(){@Override public String toString() { return "option1"; }}, new Object(){@Override public String toString() { return "option2"; }}, new Object(){@Override public String toString() { return "option3"; }}};
 when(this.value.eval(eq(any(Object.class)), eq(this.component))).thenReturn("option2");
 when(this.translator.eval(eq(options[1]), eq(this.component))).thenReturn("translated_option2");
 assertEquals("translated_option2", this.model.translateValue("option2"));
 }

 @Test
 public void testTranslateValue_whenValueDoesNotMatch_thenReturnNull() {
 Object[] options = new Object[]{new Object(){@Override public String toString() { return "option1"; }}, new Object(){@Override public String toString() { return "option2"; }}, new Object(){@Override public String toString() { return "option3"; }}};
 when(this.value.eval(eq(any(Object.class)), eq(this.component))).thenReturn("option4");
 assertEquals(null, this.model.translateValue("option4"));
 }

 @Test
 public void testGetObjects_whenCollectionEvaluatesToCollection_thenReturnCollection() {
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(new ArrayList<Object>());
 List<Object> objects = this.model.getObjects();
 assertEquals(0, objects.size());
 }

 @Test
 public void testGetObjects_whenCollectionEvaluatesToObjectsArray_thenReturnArrayListWrappingObjects() {
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(new Object[]{new Object(), new Object(), new Object()});
 List<Object> objects = this.model.getObjects();
 assertEquals(3, objects.size());
 }

 @Test
 public void testGetObjects_whenCollectionEvaluatesToNull_thenReturnEmptyList() {
 when(this.collection.eval(this.topLevelComponent, this.component)).thenReturn(null);
 List<Object> objects = this.model.getObjects();
 assertEquals(0, objects.size());
 }

 @Test(expected = WheelException.class)
 public void testGetObjects_whenCollectionEvaluatesToException_thenThrowException() {
 when(this.collection.eval(this.topLevelComponent, this.component)).thenThrow(new RuntimeException());
 this.model.getObjects();
 }
}