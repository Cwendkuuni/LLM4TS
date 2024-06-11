package wheel.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class DynamicSelectModelTest {

 private ISelectModel dynamicModel;
 private Component component;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private ElExpression translator;
 private Object emptyObject;
 private Collection objects;

 @Before
 public void setUp() {
 this.component = Mockito.mock(Component.class);
 this.label = Mockito.mock(ElExpression.class);
 this.value = Mockito.mock(ElExpression.class);
 this.collection = Mockito.mock(ElExpression.class);
 this.translator = Mockito.mock(ElExpression.class);
 this.emptyObject = new Object();
 this.dynamicModel = new DynamicSelectModel();
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 this.dynamicModel = new DynamicSelectModel(this.collection, this.label, this.value);
 assertEquals(this.collection, this.dynamicModel.getCollection());
 assertEquals(this.label, this.dynamicModel.getLabel());
 assertEquals(this.value, this.dynamicModel.getValue());
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutCollection() {
 this.dynamicModel = new DynamicSelectModel();
 PowerMockito.mockStatic(ElExpression.class);
 PowerMockito.when(ElExpression.class, "new", Mockito.anyString()).thenThrow(new IllegalArgumentException("expecting collection el-expression"));
 this.dynamicModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutLabel() {
 this.dynamicModel = new DynamicSelectModel();
 PowerMockito.mockStatic(ElExpression.class);
 PowerMockito.when(ElExpression.class, "new", Mockito.anyString()).thenReturn(this.label);
 PowerMockito.when(this.label.eval(Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException("expecting label el-expression"));
 this.dynamicModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutValue() {
 this.dynamicModel = new DynamicSelectModel();
 PowerMockito.mockStatic(ElExpression.class);
 PowerMockito.when(ElExpression.class, "new", Mockito.anyString()).thenReturn(this.value);
 PowerMockito.when(this.value.eval(Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException("expecting value el-expression"));
 this.dynamicModel.validate();
 }

 @Test
 public void testCollectionMethod() {
 this.dynamicModel = new DynamicSelectModel();
 this.dynamicModel.collection("mockCollectionExpression");
 assertEquals("mockCollectionExpression", this.dynamicModel.getCollection().getExpression());
 }

 @Test
 public void testLabelMethod() {
 this.dynamicModel = new DynamicSelectModel();
 this.dynamicModel.label("mockLabelExpression");
 assertEquals("mockLabelExpression", this.dynamicModel.getLabel().getExpression());
 }

 @Test
 public void testValueMethod() {
 this.dynamicModel = new DynamicSelectModel();
 this.dynamicModel.value("mockValueExpression");
 assertEquals("mockValueExpression", this.dynamicModel.getValue().getExpression());
 }

 @Test
 public void testEnumerationMethod() {
 this.dynamicModel = new DynamicSelectModel();
 this.dynamicModel.enumeration("mockEnumerationExpression");
 assertEquals("mockEnumerationExpression.values()", this.dynamicModel.getCollection().getExpression());
 assertEquals("toString()", this.dynamicModel.getLabel().getExpression());
 assertEquals("ordinal()", this.dynamicModel.getValue().getExpression());
 }

 @Test
 public void testTranslatorMethod() {
 this.dynamicModel = new DynamicSelectModel();
 this.dynamicModel.translator("mockTranslatorExpression");
 assertEquals("mockTranslatorExpression", this.dynamicModel.getTranslator().getExpression());
 }

 @Test
 public void testEmptyMethod() {
 this.dynamicModel = new DynamicSelectModel();
 this.dynamicModel.empty(this.emptyObject);
 assertSame(this.emptyObject, this.dynamicModel.getEmptyObject());
 }

 @Test
 public void testGetOptionCount() {
 this.objects = new ArrayList();
 this.objects.add("Option 1");
 this.objects.add("Option 2");
 this.dynamicModel.setObjects(this.objects);
 assertEquals(2, this.dynamicModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 this.objects = new ArrayList();
 this.objects.add("Option 1");
 this.objects.add("Option 2");
 this.dynamicModel.setObjects(this.objects);
 assertEquals("Option 1", this.dynamicModel.getLabel(0));
 }

 @Test
 public void testGetValue() {
 this.objects = new ArrayList();
 this.objects.add("Option 1");
 this.objects.add("Option 2");
 this.dynamicModel.setObjects(this.objects);
 assertEquals("Option 1", this.dynamicModel.getValue(0));
 }

 @Test
 public void testTranslateValue() {
 this.objects = new ArrayList();
 this.objects.add("Option 1");
 this.objects.add("Option 2");
 this.dynamicModel.setObjects(this.objects);
 assertEquals("Option 1", this.dynamicModel.translateValue("0"));
 }

 @Test
 public void testGetObjects() {
 this.objects = new ArrayList();
 this.objects.add("Option 1");
 this.objects.add("Option 2");
 this.dynamicModel.setObjects(this.objects);
 assertEquals(this.objects, this.dynamicModel.getObjects());
 }

 @Test
 public void testReset() {
 this.objects = new ArrayList();
 this.objects.add("Option 1");
 this.objects.add("Option 2");
 this.dynamicModel.setObjects(this.objects);
 this.dynamicModel.reset();
 assertNull(this.dynamicModel.getObjects());
 }
}