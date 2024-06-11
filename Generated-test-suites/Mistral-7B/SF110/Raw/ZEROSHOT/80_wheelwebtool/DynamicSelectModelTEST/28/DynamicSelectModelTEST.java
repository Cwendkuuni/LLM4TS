package wheel.util;

import org.junit.Test;
import static org.junit.Assert.*;
import wheel.components.*;
import java.util.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private ElExpression labelExpression;
 private ElExpression valueExpression;
 private ElExpression collectionExpression;
 private ElExpression enumerationExpression;
 private StandaloneComponent topLevelComponent;

 @Test
 public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
 dynamicSelectModel = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 assertNotNull(dynamicSelectModel);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructionWithoutCollection() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel);
 }

 @Test
 public void testDynamicSelectModelLabelCollectionValueSetters() {
 dynamicSelectModel = new DynamicSelectModel();
 collectionExpression = new ElExpression("['apple', 'banana', 'orange']");
 labelExpression = new ElExpression("'Fruit'");
 valueExpression = new ElExpression("'apple'");
 dynamicSelectModel.collection(collectionExpression).label(labelExpression).value(valueExpression);
 assertEquals(collectionExpression, dynamicSelectModel.getCollectionExpression());
 assertEquals(labelExpression, dynamicSelectModel.getLabelExpression());
 assertEquals(valueExpression, dynamicSelectModel.getValueExpression());
 }

 @Test
 public void testDynamicSelectModelEnumerationSetter() {
 dynamicSelectModel = new DynamicSelectModel();
 enumerationExpression = new ElExpression("java.lang.Enum.class.getEnumConstants()");
 dynamicSelectModel.enumeration(enumerationExpression);
 assertEquals(enumerationExpression, dynamicSelectModel.getCollectionExpression());
 assertEquals(new ElExpression("toString()"), dynamicSelectModel.getLabelExpression());
 assertEquals(new ElExpression("ordinal()"), dynamicSelectModel.getValueExpression());
 }

 @Test
 public void testDynamicSelectModelTranslatorSetter() {
 dynamicSelectModel = new DynamicSelectModel();
 labelExpression = new ElExpression("'French'");
 valueExpression = new ElExpression("'apple'");
 dynamicSelectModel.translator("'(French) -> 'Fruit' + ' ' + .toString()'");
 assertEquals("Fruit apple", dynamicSelectModel.translateValue("French"));
 }

 @Test
 public void testDynamicSelectModelEmptySetter() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.empty("'null'");
 assertEquals("null", dynamicSelectModel.getObjects().toArray()[0].toString());
 }

 @Test
 public void testDynamicSelectModelGetOptionCount() {
 dynamicSelectModel = new DynamicSelectModel();
 collectionExpression = new ElExpression("['apple', 'banana', 'orange']");
 dynamicSelectModel.collection(collectionExpression);
 assertEquals(3, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testDynamicSelectModelGetLabel() {
 dynamicSelectModel = new DynamicSelectModel();
 labelExpression = new ElExpression("'Fruit'");
 dynamicSelectModel.label(labelExpression);
 collectionExpression = new ElExpression("['apple', 'banana', 'orange']");
 dynamicSelectModel.collection(collectionExpression);
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 component = new Component();
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.validate();
 dynamicSelectModel.reset();
 dynamicSelectModel.getObjects(); // Force initialization
 assertEquals("Fruit apple", dynamicSelectModel.getLabel(0));
 }

 @Test
 public void testDynamicSelectModelGetValue() {
 dynamicSelectModel = new DynamicSelectModel();
 labelExpression = new ElExpression("'Fruit'");
 valueExpression = new ElExpression("'apple'");
 dynamicSelectModel.label(labelExpression).value(valueExpression);
 collectionExpression = new ElExpression("['apple', 'banana', 'orange']");
 dynamicSelectModel.collection(collectionExpression);
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 component = new Component();
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.validate();
 dynamicSelectModel.reset();
 dynamicSelectModel.getObjects(); // Force initialization
 assertEquals("apple", dynamicSelectModel.getValue(0));
 }

 @Test
 public void testDynamicSelectModelTranslateValue() {
 dynamicSelectModel = new DynamicSelectModel();
 labelExpression = new ElExpression("'French'");
 valueExpression = new ElExpression("'apple'");
 translatorExpression = new ElExpression("'(French) -> 'Fruit' + ' ' + .toString()'");
 dynamicSelectModel.label(labelExpression).value(valueExpression).translator(translatorExpression);
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 component = new Component();
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.validate();
 dynamicSelectModel.reset();
 dynamicSelectModel.getObjects(); // Force initialization
 assertEquals("Fruit apple", dynamicSelectModel.translateValue("French"));
 }

 @Test
 public void testDynamicSelectModelGetObjects() {
 dynamicSelectModel = new DynamicSelectModel();
 collectionExpression = new ElExpression("['apple', 'banana', 'orange']");
 dynamicSelectModel.collection(collectionExpression);
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 component = new Component();
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.validate();
 dynamicSelectModel.reset();
 List<Object> objects = new ArrayList<>();
 objects.add("null");
 objects.addAll(dynamicSelectModel.getObjects());
 assertEquals(3, objects.size());
 }

 @Test
 public void testDynamicSelectModelGetComponent() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel.getComponent());
 dynamicSelectModel.setComponent(component);
 assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModelGetTopLevelComponent() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel.getTopLevelComponent());
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationFailure() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.validate();
 dynamicSelectModel.collection(null);
 dynamicSelectModel.validate();
 }
}