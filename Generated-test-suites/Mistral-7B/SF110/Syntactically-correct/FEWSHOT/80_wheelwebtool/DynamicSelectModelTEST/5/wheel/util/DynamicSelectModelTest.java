package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private ElExpression collectionExpr;
 private ElExpression labelExpr;
 private ElExpression valueExpr;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 collectionExpr = mock(ElExpression.class);
 labelExpr = mock(ElExpression.class);
 valueExpr = mock(ElExpression.class);
 component = mock(Component.class);
 topLevelComponent = mock(StandaloneComponent.class);
 model = new DynamicSelectModel();
 model.collection(collectionExpr);
 model.label(labelExpr);
 model.value(valueExpr);
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testEmptyModel() {
 model = new DynamicSelectModel();
 assertEquals(0, model.getOptionCount());
 }

 @Test
 public void testValidModel() {
 model = new DynamicSelectModel(collectionExpr, labelExpr, valueExpr);
 assertEquals(3, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testInvalidCollection() {
 model = new DynamicSelectModel();
 model.collection(null);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testInvalidLabel() {
 model = new DynamicSelectModel();
 model.label(null);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testInvalidValue() {
 model = new DynamicSelectModel();
 model.value(null);
 model.validate();
 }

 @Test
 public void testGetLabel() {
 String label = "Test Label";
 when(labelExpr.eval(any(Object.class), any(Component.class))).thenReturn(label);
 Object option = new Object() {};
 when(collectionExpr.eval(topLevelComponent, component)).thenReturn(new Object[] { option });
 String result = model.getLabel(0);
 assertEquals(label, result);
 }

 @Test
 public void testGetValue() {
 String value = "Test Value";
 when(valueExpr.eval(any(Object.class), any(Component.class))).thenReturn(value);
 Object option = new Object() {};
 when(collectionExpr.eval(topLevelComponent, component)).thenReturn(new Object[] { option });
 String result = model.getValue(0);
 assertEquals(value, result);
 }

 @Test
 public void testTranslateValue() {
 String value = "Test Value";
 Object translatedValue = new Object() {};
 when(valueExpr.eval(any(Object.class), any(Component.class))).thenReturn(value);
 when(translator.eval(option, component)).thenReturn(translatedValue);
 Object result = model.translateValue(value);
 assertEquals(translatedValue, result);
 }

 @Test
 public void testGetObjects() {
 Object[] objects = new Object[] { new Object() {}, new Object() {}, new Object() {} };
 when(collectionExpr.eval(topLevelComponent, component)).thenReturn(objects);
 Collection result = model.getObjects();
 assertEquals(3, result.size());
 }

 @Test
 public void testReset() {
 model.reset();
 assertEquals(0, model.getOptionCount());
 }
}