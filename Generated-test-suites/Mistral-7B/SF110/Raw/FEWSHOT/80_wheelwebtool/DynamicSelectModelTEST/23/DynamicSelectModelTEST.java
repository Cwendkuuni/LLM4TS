package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Test;
import wheel.*;

public class DynamicSelectModelTest {

 Collection objects = null;
 DynamicSelectModel model = null;
 ElExpression collectionExpression = null;
 ElExpression labelExpression = null;
 ElExpression valueExpression = null;
 Component component = null;
 StandaloneComponent topLevelComponent = null;

 @Test
 public void testDynamicSelectModel() {
 model = new DynamicSelectModel();
 assertNull(model.getObjects());
 model.collection("testCollection");
 model.label("testLabel");
 model.value("testValue");
 assertNull(model.getObjects());
 model = model.build();
 assertNotNull(model.getObjects());
 }

 @Test
 public void testDynamicSelectModelWithCollectionExpression() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 assertNull(model.getObjects());
 model = model.build();
 assertNotNull(model.getObjects());
 }

 @Test
 public void testDynamicSelectModelWithEmptyCollection() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 model.empty(null);
 model = model.build();
 assertTrue(model.getObjects().isEmpty());
 }

 @Test
 public void testDynamicSelectModelWithCollection() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 Collection testCollection = new ArrayList();
 testCollection.add("Test1");
 testCollection.add("Test2");
 testCollection.add("Test3");
 model.setTopLevelComponent(new StandaloneComponent());
 model.setComponent(new Component());
 model = model.build();
 assertEquals(3, model.getOptionCount());
 assertEquals("Test1", model.getLabel(0));
 assertEquals("Test1", model.getValue(0));
 assertEquals("Test2", model.getLabel(1));
 assertEquals("Test2", model.getValue(1));
 assertEquals("Test3", model.getLabel(2));
 assertEquals("Test3", model.getValue(2));
 }

 @Test
 public void testDynamicSelectModelWithTranslator() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 translatorExpression = new ElExpression("testTranslator");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 model.translator("testTranslator");
 model.setTopLevelComponent(new StandaloneComponent());
 model.setComponent(new Component());
 model = model.build();
 Collection testCollection = new ArrayList();
 testCollection.add(new MyObject("Test1"));
 testCollection.add(new MyObject("Test2"));
 testCollection.add(new MyObject("Test3"));
 for (Object obj : testCollection) {
 assertEquals("Test" + obj.getValue(), model.getLabel(testCollection.indexOf(obj)));
 assertEquals(obj.getValue(), model.getValue(testCollection.indexOf(obj)));
 }
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullCollectionExpression() {
 model = new DynamicSelectModel();
 model.collection(null);
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullLabelExpression() {
 model = new DynamicSelectModel();
 model.label(null);
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullValueExpression() {
 model = new DynamicSelectModel();
 model.value(null);
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithInvalidCollectionExpression() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 model.setTopLevelComponent(new StandaloneComponent());
 model.setComponent(new Component());
 model = model.build();
 collectionExpression.setExpression("invalidCollectionExpression");
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithInvalidLabelExpression() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 model.setTopLevelComponent(new StandaloneComponent());
 model.setComponent(new Component());
 model = model.build();
 labelExpression.setExpression("invalidLabelExpression");
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithInvalidValueExpression() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 model.setTopLevelComponent(new StandaloneComponent());
 model.setComponent(new Component());
 model = model.build();
 valueExpression.setExpression("invalidValueExpression");
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullCollection() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 model.setTopLevelComponent(new StandaloneComponent());
 model.setComponent(new Component());
 model = model.build();
 collectionExpression.setExpression("testCollection.isEmpty() ? null : testCollection");
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithInvalidCollectionType() {
 collectionExpression = new ElExpression("testCollection");
 labelExpression = new ElExpression("testLabel");
 valueExpression = new ElExpression("testValue");
 model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 model.setTopLevelComponent(new StandaloneComponent());
 model.setComponent(new Component());
 model = model.build();
 collectionExpression.setExpression("testCollection.toArray()[0]");
 model = model.build();
 fail("Should have thrown an WheelException.");
 }

 @Test
 public void testReset() {
 model = new DynamicSelectModel().collection("testCollection").label("testLabel").value("testValue").build();
 assertEquals(3, model.getOptionCount());
 model.reset();
 assertNull(model.getObjects());
 }
}

 class MyObject {
 String value;

 public MyObject(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
}