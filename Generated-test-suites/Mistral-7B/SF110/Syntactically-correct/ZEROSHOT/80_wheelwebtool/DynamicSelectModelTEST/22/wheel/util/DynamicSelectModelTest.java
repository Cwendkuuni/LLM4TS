import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private ElExpression labelExpression;
 private ElExpression valueExpression;
 private ElExpression collectionExpression;
 private ElExpression enumerationExpression;
 private Collection objects;

 @Test
 public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
 dynamicSelectModel = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 assertNotNull(dynamicSelectModel);
 }

 @Test
 public void testDynamicSelectModelConstructionWithEmptyArgs() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNotNull(dynamicSelectModel);
 }

 @Test
 public void testDynamicSelectModelCollection() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("testLabel");
 dynamicSelectModel.value("testValue");
 assertEquals("testCollection", dynamicSelectModel.getCollection().getExpression().toString());
 }

 @Test
 public void testDynamicSelectModelLabel() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("testLabel");
 dynamicSelectModel.value("testValue");
 ElExpression labelElExpression = new ElExpression("testLabel");
 assertEquals(labelElExpression.toString(), dynamicSelectModel.getLabelExpression().toString());
 }

 @Test
 public void testDynamicSelectModelValue() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("testLabel");
 dynamicSelectModel.value("testValue");
 ElExpression valueElExpression = new ElExpression("testValue");
 assertEquals(valueElExpression.toString(), dynamicSelectModel.getValueExpression().toString());
 }

 @Test
 public void testDynamicSelectModelEnumeration() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("testEnum.values()");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 dynamicSelectModel.translator("testTranslator");
 ElExpression labelElExpression = new ElExpression("toString()");
 ElExpression valueElExpression = new ElExpression("ordinal()");
 ElExpression translatorElExpression = new ElExpression("testTranslator");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("ONE");
 expectedValues.add("TWO");
 expectedValues.add("THREE");
 Collection expectedObjects = new LinkedList<>(expectedValues);
 when(component.eval(anyString(), any())).thenReturn(expectedObjects);
 Collection actualObjects = dynamicSelectModel.getObjects();
 assertEquals(expectedObjects, actualObjects);
 assertEquals("ONE", dynamicSelectModel.getLabel(0));
 assertEquals("0", dynamicSelectModel.getValue(0));
 assertEquals("ONE", dynamicSelectModel.translateValue("ONE"));
 assertEquals("testTranslator.testTranslator(ONE)", dynamicSelectModel.translateValue("ONE").toString());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidation() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(null);
 dynamicSelectModel.label(null);
 dynamicSelectModel.value(null);
 }

 @Test
 public void testDynamicSelectModelGetObjects() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("testLabel");
 dynamicSelectModel.value("testValue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
 Collection expectedObjects = new ArrayList<>();
 expectedObjects.add(new String("TEST1"));
 expectedObjects.add(new String("TEST2"));
 Collection actualObjects = dynamicSelectModel.getObjects();
 assertEquals(expectedObjects, actualObjects);
 }

 @Test
 public void testDynamicSelectModelReset() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("testLabel");
 dynamicSelectModel.value("testValue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
 dynamicSelectModel.reset();
 assertNull(dynamicSelectModel.getObjects());
 }
}