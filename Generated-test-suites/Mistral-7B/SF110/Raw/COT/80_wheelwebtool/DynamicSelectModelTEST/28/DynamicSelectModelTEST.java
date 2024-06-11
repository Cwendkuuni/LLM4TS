package wheel.util; // Import necessary packages

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private ElExpression collectionExpression, labelExpression, valueExpression;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 component = Mockito.mock(Component.class);
 collectionExpression = Mockito.mock(ElExpression.class);
 labelExpression = Mockito.mock(ElExpression.class);
 valueExpression = Mockito.mock(ElExpression.class);
 model = new DynamicSelectModel(() -> collectionExpression, labelExpression, valueExpression);
 }

 @Test
 public void testConstructorWithCollectionExpressionLabelExpressionAndValueExpression() {
 model = new DynamicSelectModel(() -> collectionExpression, labelExpression, valueExpression);
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutCollectionExpression() {
 model = new DynamicSelectModel();
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutLabelExpression() {
 model = new DynamicSelectModel();
 model.label = null;
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testConstructorWithoutValueExpression() {
 model = new DynamicSelectModel();
 model.value = null;
 model.validate();
 }

 @Test
 public void testCollectionMethod() {
 model = new DynamicSelectModel();
 model.collection("testCollection");
 Assert.assertEquals("testCollection", model.collection.getExpression());
 }

 @Test
 public void testLabelMethod() {
 model = new DynamicSelectModel();
 model.label("testLabel");
 Assert.assertEquals("testLabel", model.label.getExpression());
 }

 @Test
 public void testValueMethod() {
 model = new DynamicSelectModel();
 model.value("testValue");
 Assert.assertEquals("testValue", model.value.getExpression());
 }

 @Test
 public void testGetOptionCount() {
 Collection testCollection = Mockito.mock(Collection.class);
 when(collectionExpression.eval(component, Mockito.eq(component))).thenReturn(testCollection);
 model.collection = collectionExpression;
 model.objects = testCollection;
 int optionCount = model.getOptionCount();
 Assert.assertEquals(testCollection.size(), optionCount);
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithNullIndex() {
 model.getLabel(-1);
 }

 @Test
 public void testGetLabel() {
 Object testObject = Mockito.mock(Object.class);
 when(labelExpression.eval(testObject, component)).thenReturn("TestLabel");
 model.label = labelExpression;
 model.objects = new ArrayList<>();
 model.objects.add(testObject);
 String label = model.getLabel(0);
 Assert.assertEquals("TestLabel", label);
 }

 @Test(expected = WheelException.class)
 public void testGetValueWithNullIndex() {
 model.getValue(-1);
 }

 @Test
 public void testGetValue() {
 Object testObject = Mockito.mock(Object.class);
 when(valueExpression.eval(testObject, component)).thenReturn("TestValue");
 model.value = valueExpression;
 model.objects = new ArrayList<>();
 model.objects.add(testObject);
 String value = model.getValue(0);
 Assert.assertEquals("TestValue", value);
 }

 @Test
 public void testTranslateValue() {
 Object testObject = Mockito.mock(Object.class);
 when(valueExpression.eval(testObject, component)).thenReturn("TestValue");
 model.value = valueExpression;
 model.objects = new ArrayList<>();
 model.objects.add(testObject);
 Assert.assertEquals(testObject, model.translateValue("TestValue"));
 }

 @Test
 public void testTranslateValueWithNullValue() {
 model.translateValue(null);
 }

 @Test
 public void testGetObjects() {
 Collection testCollection = Mockito.mock(Collection.class);
 when(collectionExpression.eval(component, Mockito.eq(component))).thenReturn(testCollection);
 model.collection = collectionExpression;
 List testList = new ArrayList();
 testList.add(Mockito.mock(Object.class));
 testList.add(Mockito.mock(Object.class));
 model.objects = testList;
 model.emptyObject = Mockito.mock(Object.class);
 Collection result = model.getObjects();
 Assert.assertEquals(testList, result);
 }

 @Test
 public void testGetComponent() {
 Component result = model.getComponent();
 Assert.assertNotNull(result);
 }

 @Test
 public void testSetComponent() {
 Component newComponent = Mockito.mock(Component.class);
 model.setComponent(newComponent);
 Assert.assertEquals(newComponent, model.getComponent());
 }

 @Test
 public void testGetTopLevelComponent() {
 StandaloneComponent result = model.getTopLevelComponent();
 Assert.assertNull(result);
 }

 @Test
 public void testSetTopLevelComponent() {
 StandaloneComponent newTopLevelComponent = Mockito.mock(StandaloneComponent.class);
 model.setTopLevelComponent(newTopLevelComponent);
 Assert.assertEquals(newTopLevelComponent, model.getTopLevelComponent());
 }

 @Test
 public void testValidate() {
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullCollectionExpression() {
 model.collection = null;
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullLabelExpression() {
 model.label = null;
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullValueExpression() {
 model.value = null;
 model.validate();
 }

 @Test
 public void testReset() {
 model.reset();
 Assert.assertNull(model.objects);
 }
}