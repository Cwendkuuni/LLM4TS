import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import wheel.components.*;
import java.util.*;

public class DynamicSelectModelTest {

 @Test
 public void testDynamicSelectModelConstructorWithCollectionLabelValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
 Assert.assertNotNull(dynamicSelectModel);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelInvalidConstructor() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testDynamicSelectModelCollectionMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
 dynamicSelectModel.collection("newCollection");
 Assert.assertEquals("newCollection", dynamicSelectModel.collection().getExpression());
 }

 @Test
 public void testDynamicSelectModelLabelMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
 dynamicSelectModel.label("newLabel");
 Assert.assertEquals("newLabel", dynamicSelectModel.label().getExpression());
 }

 @Test
 public void testDynamicSelectModelValueMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
 dynamicSelectModel.value("newValue");
 Assert.assertEquals("newValue", dynamicSelectModel.value().getExpression());
 }

 @Test
 public void testDynamicSelectModelEnumerationMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("enumValues");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.enumeration("MyEnum");
 Assert.assertEquals("MyEnum.values().toString()", dynamicSelectModel.getLabelExpression().getExpression());
 Assert.assertEquals("MyEnum.values()[ordinal()].ordinal()", dynamicSelectModel.getValueExpression().getExpression());
 }

 @Test
 public void testDynamicSelectModelTranslatorMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.translator("myTranslator");
 Assert.assertEquals("myTranslator", dynamicSelectModel.translator().getExpression());
 }

 @Test
 public void testDynamicSelectModelEmptyMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.empty(new Object());
 }

 @Test
 public void testDynamicSelectModelGetOptionCount() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("enumValues");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.enumeration("MyEnum");
 List<Object> options = new ArrayList<>();
 options.add(MyEnum.VALUE1);
 options.add(MyEnum.VALUE2);
 when(dynamicSelectModel.getObjects()).thenReturn(options);
 Assert.assertEquals(2, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testDynamicSelectModelGetLabel() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("enumValues");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.enumeration("MyEnum");
 List<Object> options = new ArrayList<>();
 options.add(MyEnum.VALUE1);
 options.add(MyEnum.VALUE2);
 when(dynamicSelectModel.getObjects()).thenReturn(options);
 Assert.assertEquals("VALUE1", dynamicSelectModel.getLabel(0));
 }

 @Test
 public void testDynamicSelectModelGetValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("enumValues");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.enumeration("MyEnum");
 List<Object> options = new ArrayList<>();
 options.add(MyEnum.VALUE1);
 options.add(MyEnum.VALUE2);
 when(dynamicSelectModel.getObjects()).thenReturn(options);
 Assert.assertEquals("0", dynamicSelectModel.getValue(0));
 }

 @Test
 public void testDynamicSelectModelTranslateValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("options"));
 dynamicSelectModel.label(new ElExpression("labelExpression"));
 dynamicSelectModel.value(new ElExpression("valueExpression"));
 dynamicSelectModel.translator(new ElExpression("translatorExpression"));
 List<Object> options = new ArrayList<>();
 options.add(new Option("Option1", "Label1"));
 options.add(new Option("Option2", "Label2"));
 when(dynamicSelectModel.getObjects()).thenReturn(options);
 when(dynamicSelectModel.translatorExpression.eval(options.get(0), mock(Component.class))).thenReturn("TranslatedOption1");
 Assert.assertEquals("TranslatedOption1", dynamicSelectModel.translateValue("Label1"));
 }

 @Test
 public void testDynamicSelectModelGetObjects() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("options"));
 dynamicSelectModel.label(new ElExpression("labelExpression"));
 dynamicSelectModel.value(new ElExpression("valueExpression"));
 dynamicSelectModel.translator(new ElExpression("translatorExpression"));
 List<Object> options = new ArrayList<>();
 options.add(new Option("Option1", "Label1"));
 options.add(new Option("Option2", "Label2"));
 when(dynamicSelectModel.getObjects()).thenReturn(options);
 Collection collection = dynamicSelectModel.getObjects();
 Assert.assertEquals(2, collection.size());
 Assert.assertTrue(collection.contains(new Option("Option1", "Label1")));
 Assert.assertTrue(collection.contains(new Option("Option2", "Label2")));
 }

 @Test
 public void testDynamicSelectModelSetComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 Component component = mock(Component.class);
 dynamicSelectModel.setComponent(component);
 Assert.assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModelSetTopLevelComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 Assert.assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testDynamicSelectModelReset() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 Collection objects = new ArrayList<>();
 dynamicSelectModel.objects = objects;
 dynamicSelectModel.reset();
 Assert.assertNull(dynamicSelectModel.objects);
 }
}