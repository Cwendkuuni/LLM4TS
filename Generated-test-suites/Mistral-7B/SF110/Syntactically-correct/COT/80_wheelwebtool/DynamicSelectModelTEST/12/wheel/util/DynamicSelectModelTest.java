package wheel.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.*;
import wheel.*;
import java.util.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Test
 public void testDynamicSelectModelConstructorWithCollectionLabelValue() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression label = new ElExpression("label");
     ElExpression value = new ElExpression("value");
     ElExpression collection = new ElExpression("collection");

     dynamicSelectModel = new DynamicSelectModel(collection, label, value);
     assertNotNull(dynamicSelectModel);
     assertEquals(collection, dynamicSelectModel.getCollection());
     assertEquals(label, dynamicSelectModel.getLabel());
     assertEquals(value, dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModelEmptyConstructor() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     assertNotNull(dynamicSelectModel);
 }

 @Test
 public void testDynamicSelectModelCollectionMethod() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     ElExpression collection = new ElExpression("collection");

     dynamicSelectModel.collection("collection");
     assertEquals(collection, dynamicSelectModel.getCollection());
 }

 @Test
 public void testDynamicSelectModelLabelMethod() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     ElExpression label = new ElExpression("label");

     dynamicSelectModel.label("label");
     assertEquals(label, dynamicSelectModel.getLabel());
 }

 @Test
 public void testDynamicSelectModelValueMethod() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     ElExpression value = new ElExpression("value");

     dynamicSelectModel.value("value");
     assertEquals(value, dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModelEnumerationMethod() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     ElExpression enumeration = new ElExpression("enumeration");

     dynamicSelectModel.enumeration("enumeration");
     assertEquals(enumeration, dynamicSelectModel.getCollection());
     assertEquals(new ElExpression("toString"), dynamicSelectModel.getLabel());
     assertEquals(new ElExpression("ordinal"), dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModelTranslatorMethod() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     ElExpression translator = new ElExpression("translator");

     dynamicSelectModel.translator("translator");
     assertEquals(translator, dynamicSelectModel.getTranslator());
 }

 @Test
 public void testDynamicSelectModelEmptyMethod() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.empty(new Object());
     assertNotNull(dynamicSelectModel.getObjects());
     assertTrue(dynamicSelectModel.getObjects().contains(new Object()));
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelCollectionAttributeNull() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(null);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelLabelAttributeNull() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.label(null);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValueAttributeNull() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.value(null);
 }

 @Test
 public void testGetOptionCount() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);
     ElExpression collection = new ElExpression("collection");

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(collection);

     List objects = new ArrayList();
     objects.add(new Object());
     objects.add(new Object());
     dynamicSelectModel.setObjects(objects);

     assertEquals(2, dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithNullIndex() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression label = new ElExpression("label");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.label(label);

     List objects = new ArrayList();
     objects.add(new Object());
     objects.add(new Object());
     dynamicSelectModel.setObjects(objects);

     dynamicSelectModel.getLabel(-1);
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithInvalidIndex() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression label = new ElExpression("label");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.label(label);

     List objects = new ArrayList();
     objects.add(new Object());
     objects.add(new Object());
     dynamicSelectModel.setObjects(objects);

     dynamicSelectModel.getLabel(3);
 }

 @Test
 public void testGetLabel() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression label = new ElExpression("label");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.label(label);

     List objects = new ArrayList();
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 1";
         }
     });
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 2";
         }
     });
     dynamicSelectModel.setObjects(objects);

     assertEquals("Option 1", dynamicSelectModel.getLabel(0));
     assertEquals("Option 2", dynamicSelectModel.getLabel(1));
 }

 @Test
 public void testGetValueWithNullIndex() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression value = new ElExpression("value");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.value(value);

     List objects = new ArrayList();
     objects.add(new Object());
     objects.add(new Object());
     dynamicSelectModel.setObjects(objects);

     dynamicSelectModel.getValue(-1);
 }

 @Test(expected = WheelException.class)
 public void testGetValueWithInvalidIndex() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression value = new ElExpression("value");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.value(value);

     List objects = new ArrayList();
     objects.add(new Object());
     objects.add(new Object());
     dynamicSelectModel.setObjects(objects);

     dynamicSelectModel.getValue(3);
 }

 @Test
 public void testGetValue() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression value = new ElExpression("value");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.value(value);

     List objects = new ArrayList();
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 1";
         }

         @Override
         public int ordinal() {
             return 1;
         }
     });
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 2";
         }

         @Override
         public int ordinal() {
             return 2;
         }
     });
     dynamicSelectModel.setObjects(objects);

     assertEquals("Option 1", dynamicSelectModel.getValue(0).toString());
     assertEquals("Option 2", dynamicSelectModel.getValue(1).toString());
     assertEquals(1, dynamicSelectModel.getValue(0).ordinal());
     assertEquals(2, dynamicSelectModel.getValue(1).ordinal());
 }

 @Test
 public void testTranslateValueNullValue() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression translator = new ElExpression("translator");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.translator(translator);

     List objects = new ArrayList();
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 1";
         }
     });
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 2";
         }
     });
     dynamicSelectModel.setObjects(objects);

     assertNull(dynamicSelectModel.translateValue(null));
 }

 @Test
 public void testTranslateValueInvalidValue() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression translator = new ElExpression("translator");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.translator(translator);

     List objects = new ArrayList();
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 1";
         }

         @Override
         public int ordinal() {
             return 1;
         }
     });
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 2";
         }

         @Override
         public int ordinal() {
             return 2;
         }
     });
     dynamicSelectModel.setObjects(objects);

     assertNull(dynamicSelectModel.translateValue("InvalidValue"));
 }

 @Test
 public void testTranslateValueValidValue() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     ElExpression translator = new ElExpression("translator");
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.translator(translator);

     List objects = new ArrayList();
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 1";
         }

         @Override
         public int ordinal() {
             return 1;
         }
     });
     objects.add(new Object() {
         @Override
         public String toString() {
             return "Option 2";
         }

         @Override
         public int ordinal() {
             return 2;
         }
     });
     dynamicSelectModel.setObjects(objects);

     Object object = new Object() {
         @Override
         public String toString() {
             return "Translated Value";
         }
     };
     when(translator.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(object);

     assertEquals("Translated Value", dynamicSelectModel.translateValue("Option 1").toString());
 }

 @Test
 public void testGetObjectsEmptyList() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.setComponent(component);
     dynamicSelectModel.setTopLevelComponent(topLevelComponent);

     List objects = new ArrayList();
     dynamicSelectModel.setObjects(objects);

     assertEquals(0, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetObjectsValidCollection() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.setComponent(component);
     dynamicSelectModel.setTopLevelComponent(topLevelComponent);

     List objects = Arrays.asList("Option 1", "Option 2");
     dynamicSelectModel.setObjects(objects);

     assertEquals(2, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetComponents() {
     component = Mockito.mock(Component.class);
     topLevelComponent = Mockito.mock(StandaloneComponent.class);

     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.setComponent(component);
     dynamicSelectModel.setTopLevelComponent(topLevelComponent);

     List objects = Arrays.asList(component, topLevelComponent);
     dynamicSelectModel.setObjects(objects);

     assertEquals(objects, dynamicSelectModel.getObjects());
 }

 @Test
 public void testValidate() {
     dynamicSelectModel = new DynamicSelectModel();

     try {
         dynamicSelectModel.validate();
     } catch (WheelException e) {
         fail("Validate method should not throw exception when all attributes are set.");
     }
 }

 @Test(expected = WheelException.class)
 public void testValidateCollectionNull() {
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateLabelNull() {
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.label = null;
     dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateValueNull() {
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.value = null;
     dynamicSelectModel.validate();
 }

 @Test
 public void testReset() {
     dynamicSelectModel = new DynamicSelectModel();
     List objects = Arrays.asList("Option 1", "Option 2");
     dynamicSelectModel.setObjects(objects);

     dynamicSelectModel.reset();

     assertEquals(0, dynamicSelectModel.getOptionCount());
 }
}