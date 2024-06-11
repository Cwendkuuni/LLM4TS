package wheel.util; // Import the necessary packages for JUnit 4 testing

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.*;
import wheel.exceptions.WheelException;

public class DynamicSelectModelTest {

 private Component component;
 private StandaloneComponent topLevelComponent;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private ElExpression translator;
 private Collection objects;

 @Test
 public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(1, 2, 3)");;
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

     assertEquals(3, dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationFailureWhenCollectionIsNull() {
     component = mock(Component.class);
     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(null, new ElExpression("toString()"), new ElExpression("ordinal()"));
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationFailureWhenLabelIsNull() {
     component = mock(Component.class);
     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(mock(ElExpression.class), null, new ElExpression("ordinal()"));
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationFailureWhenValueIsNull() {
     component = mock(Component.class);
     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(mock(ElExpression.class), new ElExpression("toString()"), null);
 }

 @Test
 public void testDynamicSelectModelGetObjectsReturnsEmptyListWhenEmptyObjectIsSet() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Collections.emptyList()");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
     dynamicSelectModel.empty(new Object());

     assertNull(dynamicSelectModel.getObjects());
 }

 @Test
 public void testDynamicSelectModelGetObjectsReturnsListWhenCollectionIsEvaluatedToList() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(1, 2, 3)");;
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

     List<Object> expectedList = Arrays.asList(1, 2, 3);
     Collection actualList = dynamicSelectModel.getObjects();

     assertEquals(expectedList, actualList);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetObjectsThrowsExceptionWhenCollectionEvaluationFails() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.lang.String.class");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 }

 @Test
 public void testDynamicSelectModelGetLabelReturnsStringRepresentationOfEvaluatedExpression() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(new String[] { \"One\", \"Two\", \"Three\" })");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

     String expectedLabel = "One";
     String actualLabel = dynamicSelectModel.getLabel(0);

     assertEquals(expectedLabel, actualLabel);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetLabelThrowsExceptionWhenIndexIsOutOfBounds() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(1, 2, 3)");;
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

     dynamicSelectModel.getLabel(-1);
 }

 @Test
 public void testDynamicSelectModelGetValueReturnsStringRepresentationOfEvaluatedExpression() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(new String[] { \"One\", \"Two\", \"Three\" })");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

     String expectedValue = "One";
     String actualValue = dynamicSelectModel.getValue(0);

     assertEquals(expectedValue, actualValue);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetValueThrowsExceptionWhenIndexIsOutOfBounds() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(new String[] { \"One\", \"Two\", \"Three\" })");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);

     dynamicSelectModel.getValue(-1);
 }

 @Test
 public void testDynamicSelectModelTranslateValueReturnsCorrectlyTranslatedValue() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(new String[] { \"One\", \"Two\", \"Three\" })");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");
     translator = new ElExpression("java.lang.Integer.toString(ordinal() + 1)");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value).translator("java.lang.Integer");

     String expectedTranslatedValue = "2";
     String actualTranslatedValue = dynamicSelectModel.translateValue("One").toString();

     assertEquals(expectedTranslatedValue, actualTranslatedValue);
 }

 @Test
 public void testDynamicSelectModelTranslateValueReturnsNullWhenNoMatchingValueIsFound() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(new String[] { \"One\", \"Two\", \"Three\" })");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");
     translator = new ElExpression("java.lang.Integer.toString(ordinal() + 1)");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value).translator("java.lang.String");

     assertNull(dynamicSelectModel.translateValue("Four"));
 }

 @Test
 public void testDynamicSelectModelResetResetsObjectsToNull() {
     component = mock(Component.class);
     topLevelComponent = mock(StandaloneComponent.class);
     collection = new ElExpression("java.util.Arrays.asList(new String[] { \"One\", \"Two\", \"Three\" })");
     label = new ElExpression("toString()");
     value = new ElExpression("ordinal()");

     DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
     Collection objects = dynamicSelectModel.getObjects();

     dynamicSelectModel.reset();

     assertNull(dynamicSelectModel.getObjects());
 }
}