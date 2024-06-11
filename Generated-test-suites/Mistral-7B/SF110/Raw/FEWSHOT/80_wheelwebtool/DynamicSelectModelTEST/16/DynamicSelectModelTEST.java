package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Test;

public class DynamicSelectModelTest {

Component component = mock(Component.class);
StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);

@Test
public void testDynamicSelectModelInitialization() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
assertNull(dynamicSelectModel.getObjects());
assertNull(dynamicSelectModel.label);
assertNull(dynamicSelectModel.value);
assertNull(dynamicSelectModel.collection);
assertNull(dynamicSelectModel.translator);
assertNull(dynamicSelectModel.objects);
assertNull(dynamicSelectModel.emptyObject);
}

@Test
public void testDynamicSelectModelInitializationWithCollectionLabelValue() {
ElExpression collection = new ElExpression("collectionExpression");
ElExpression label = new ElExpression("labelExpression");
ElExpression value = new ElExpression("valueExpression");

DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
assertEquals(collection, dynamicSelectModel.collection);
assertEquals(label, dynamicSelectModel.label);
assertEquals(value, dynamicSelectModel.value);
assertNull(dynamicSelectModel.translator);
assertNull(dynamicSelectModel.objects);
assertNull(dynamicSelectModel.emptyObject);
}

@Test
public void testDynamicSelectModelInitializationWithEmpty() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
dynamicSelectModel.empty("emptyObject");
assertEquals("emptyObject", dynamicSelectModel.emptyObject.toString());
}

@Test
public void testGetOptionCount() {
Collection objects = new ArrayList();
objects.add(new Object());
objects.add(new Object());
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(objects);
assertEquals(2, dynamicSelectModel.getOptionCount());
}

@Test
public void testGetLabel() {
Collection objects = new ArrayList();
objects.add(new Object() {
@Override
public String toString() {
return "Label1";
}
});
objects.add(new Object() {
@Override
public String toString() {
return "Label2";
}
});
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(objects);
assertEquals("Label1", dynamicSelectModel.getLabel(0));
assertEquals("Label2", dynamicSelectModel.getLabel(1));
}

@Test
public void testGetValue() {
Collection objects = new ArrayList();
objects.add(new Object() {
@Override
public String toString() {
return "Value1";
}
});
objects.add(new Object() {
@Override
public String toString() {
return "Value2";
}
});
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(objects);
assertEquals("Value1", dynamicSelectModel.getValue(0));
assertEquals("Value2", dynamicSelectModel.getValue(1));
}

@Test
public void testTranslateValue() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(new ArrayList());
assertNull(dynamicSelectModel.translateValue("Value1"));

Collection objects = new ArrayList();
objects.add(new Object() {
@Override
public String toString() {
return "Value1";
}
});
objects.add(new Object() {
@Override
public String toString() {
return "Value2";
}
});
dynamicSelectModel.objects = objects;
assertEquals(objects.get(0), dynamicSelectModel.translateValue("Value1"));
assertEquals(objects.get(1), dynamicSelectModel.translateValue("Value2"));
}

@Test
public void testTranslateValueWithNullValue() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(new ArrayList());
assertNull(dynamicSelectModel.translateValue(null));
}

@Test
public void testTranslateValueWithNullCollection() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(null).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(new ArrayList());
assertNull(dynamicSelectModel.translateValue("Value1"));
}

@Test
public void testTranslateValueWithNullLabel() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(null).value(new ElExpression("valueExpression")).objects(new ArrayList());
assertNull(dynamicSelectModel.translateValue("Value1"));
}

@Test
public void testGetObjects() {
Collection objects = new ArrayList();
objects.add(new Object());
objects.add(new Object());
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(objects);
assertEquals(objects, dynamicSelectModel.getObjects());
}

@Test
public void testGetObjectsWithEmpty() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(new ArrayList());
dynamicSelectModel.empty("emptyObject");
Collection newCollection = new LinkedList();
newCollection.add(dynamicSelectModel.emptyObject);
newCollection.addAll(dynamicSelectModel.objects);
assertEquals(newCollection, dynamicSelectModel.getObjects());
}

@Test
public void testGetObjectsWithNullCollection() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(null);
assertNull(dynamicSelectModel.getObjects());
}

@Test
public void testGetObjectsWithNullCollectionExpression() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(null).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(new ArrayList());
assertNull(dynamicSelectModel.getObjects());
}

@Test
public void testGetObjectsWithNullLabelExpression() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(null).value(new ElExpression("valueExpression")).objects(new ArrayList());
assertNull(dynamicSelectModel.getObjects());
}

@Test
public void testGetObjectsWithNullValueExpression() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(null).objects(new ArrayList());
assertNull(dynamicSelectModel.getObjects());
}

@Test
public void testValidate() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
try {
dynamicSelectModel.validate();
fail();
} catch (WheelException e) {
assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
}

DynamicSelectModel dynamicSelectModel1 = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(null).value(new ElExpression("valueExpression"));
try {
dynamicSelectModel1.validate();
fail();
} catch (WheelException e) {
assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
}

DynamicSelectModel dynamicSelectModel2 = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(null);
try {
dynamicSelectModel2.validate();
fail();
} catch (WheelException e) {
assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
}
}

@Test
public void testReset() {
DynamicSelectModel dynamicSelectModel = new DynamicSelectModel().collection(new ElExpression("collectionExpression")).label(new ElExpression("labelExpression")).value(new ElExpression("valueExpression")).objects(new ArrayList());
dynamicSelectModel.reset();
assertNull(dynamicSelectModel.getObjects());
}
}