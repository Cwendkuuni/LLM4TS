package wheel.util;

import org.junit.Assert;import org.junit.Before;import org.junit.Test;import org.mockito.Mockito;import java.util.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private ElExpression collectionElExpression;
 private ElExpression labelElExpression;
 private ElExpression valueElExpression;
 private Component component;

 @Before
 public void setUp() {
 collectionElExpression = Mockito.spy(new ElExpression("collection"));
 labelElExpression = Mockito.spy(new ElExpression("label"));
 valueElExpression = Mockito.spy(new ElExpression("value"));
 component = Mockito.mock(Component.class);
 dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testDynamicSelectModelWithNullCollection() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(null);
 dynamicSelectModel.label("Label");
 dynamicSelectModel.value("Value");

 try {
 dynamicSelectModel.getObjects();
 Assert.fail("Expected WheelException but got none.");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testDynamicSelectModelWithNullLabel() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(collectionElExpression);
 dynamicSelectModel.label(null);
 dynamicSelectModel.value("Value");

 try {
 dynamicSelectModel.getObjects();
 Assert.fail("Expected WheelException but got none.");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testDynamicSelectModelWithNullValue() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(collectionElExpression);
 dynamicSelectModel.label(labelElExpression);
 dynamicSelectModel.value(null);

 try {
 dynamicSelectModel.getObjects();
 Assert.fail("Expected WheelException but got none.");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testDynamicSelectModelWithValidArguments() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(collectionElExpression);
 dynamicSelectModel.label(labelElExpression);
 dynamicSelectModel.value(valueElExpression);

 List objects = dynamicSelectModel.getObjects();
 Assert.assertNotNull(objects);
 }

 @Test
 public void testGetLabel() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(Mockito.mock(Collection.class));
 dynamicSelectModel.label(Mockito.mock(ElExpression.class));
 dynamicSelectModel.value(Mockito.mock(ElExpression.class));

 Object label = dynamicSelectModel.getLabel(0);
 Mockito.verify(dynamicSelectModel.label).eval(Mockito.anyObject(), Mockito.eq(component));
 Mockito.verify(label).toString();
 }

 @Test
 public void testGetValue() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(Mockito.mock(Collection.class));
 dynamicSelectModel.label(Mockito.mock(ElExpression.class));
 dynamicSelectModel.value(Mockito.mock(ElExpression.class));

 Object value = dynamicSelectModel.getValue(0);
 Mockito.verify(dynamicSelectModel.value).eval(Mockito.anyObject(), Mockito.eq(component));
 Mockito.verify(value).toString();
 }

 @Test
 public void testTranslateValue() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(Mockito.mock(Collection.class));
 dynamicSelectModel.label(Mockito.mock(ElExpression.class));
 dynamicSelectModel.value(Mockito.mock(ElExpression.class));
 dynamicSelectModel.translator(Mockito.mock(ElExpression.class));

 Object translatedValue = dynamicSelectModel.translateValue("someValue");
 Mockito.verify(dynamicSelectModel.getValue(Mockito.anyInt())).equals("someValue");
 Mockito.verify(dynamicSelectModel.translator).eval(Mockito.anyObject(), Mockito.eq(component));
 }

 @Test
 public void testGetObjects() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(Mockito.mock(Collection.class));
 dynamicSelectModel.label(Mockito.mock(ElExpression.class));
 dynamicSelectModel.value(Mockito.mock(ElExpression.class));

 List objects = dynamicSelectModel.getObjects();
 Mockito.verify(collectionElExpression).eval(Mockito.eq(component), Mockito.any());
 }

 @Test
 public void testEmpty() {
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(Mockito.mock(Collection.class));
 dynamicSelectModel.label(Mockito.mock(ElExpression.class));
 dynamicSelectModel.value(Mockito.mock(ElExpression.class));
 dynamicSelectModel.empty(new Object());

 List objects = dynamicSelectModel.getObjects();
 Assert.assertNotNull(objects);
 Assert.assertEquals(1, objects.size());
 Assert.assertEquals(new Object(), objects.get(0));
 }
}