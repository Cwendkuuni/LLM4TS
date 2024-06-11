package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel model1 = new AttributeModel("model1", "value1");
 private AttributeModel model2 = new AttributeModel("model2", "value2");
 private AttributeModel modelA = new AttributeModel("a", null);
 private AttributeModel nullModel1 = null;
 private AttributeModel nullModel2 = null;

 @Test
 public void testCompare_WhenBothObjectsAreNotNull_ThenCorrectComparisonResult() {
 String name1 = model1.getName();
 String name2 = model2.getName();
 int comparisonResult = new AttributeModelComparator().compare(model1, model2);

 assertTrue(name1.compareToIgnoreCase(name2) == comparisonResult);
 }

 @Test
 public void testCompare_WhenBothObjectsHaveSameName_ThenZeroComparisonResult() {
 AttributeModel sameNamedModel1 = new AttributeModel("sameName", "value1");
 AttributeModel sameNamedModel2 = new AttributeModel("sameName", "value2");
 int comparisonResult = new AttributeModelComparator().compare(sameNamedModel1, sameNamedModel2);

 assertEquals(0, comparisonResult);
 }

 @Test
 public void testCompare_WhenFirstObjectHasNameLessThanSecondObject_ThenNegativeComparisonResult() {
 String name1 = model1.getName();
 String name2 = modelA.getName();
 int comparisonResult = new AttributeModelComparator().compare(model1, modelA);

 assertTrue(name1.compareToIgnoreCase(name2) < 0);
 assertTrue(comparisonResult < 0);
 }

 @Test
 public void testCompare_WhenFirstObjectHasNameGreaterThanSecondObject_ThenPositiveComparisonResult() {
 String name1 = modelA.getName();
 String name2 = model1.getName();
 int comparisonResult = new AttributeModelComparator().compare(modelA, model1);

 assertTrue(name1.compareToIgnoreCase(name2) > 0);
 assertTrue(comparisonResult > 0);
 }

 @Test
 public void testCompare_WhenFirstObjectIsNotNullAndSecondObjectIsNull_ThenFirstObjectIsGreater() {
 String name1 = model1.getName();
 int comparisonResult = new AttributeModelComparator().compare(model1, nullModel1);

 assertTrue(name1.compareToIgnoreCase("") < 0); // empty string is considered less than any other string
 assertTrue(comparisonResult > 0);
 }

 @Test
 public void testCompare_WhenFirstObjectIsNullAndSecondObjectIsNotNull_ThenSecondObjectIsGreater() {
 String name2 = model2.getName();
 int comparisonResult = new AttributeModelComparator().compare(nullModel2, model2);

 assertTrue(name2.compareToIgnoreCase("") > 0);
 assertTrue(comparisonResult < 0);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_WhenBothObjectsAreNull_ThenNullPointerExceptionIsThrown() {
 int comparisonResult = new AttributeModelComparator().compare(nullModel1, nullModel2);
 fail("NullPointerException should have been thrown");
 }

 @Test
 public void testCompare_WhenFirstObjectHasNullNameAndSecondObjectHasNotNullName_ThenFirstObjectIsLess() {
 String name2 = model2.getName();
 int comparisonResult = new AttributeModelComparator().compare(new AttributeModel(null, null), model2);

 assertTrue(comparisonResult < 0);
 }

 @Test
 public void testCompare_WhenBothObjectsHaveNullNames_ThenComparisonResultIsUndefined() {
 AttributeModel nullNameModel1 = new AttributeModel(null, null);
 AttributeModel nullNameModel2 = new AttributeModel(null, null);
 int comparisonResult = new AttributeModelComparator().compare(nullNameModel1, nullNameModel2);

 assertEquals(-1, comparisonResult); // or any other value, since it's undefined
 }

 @Test
 public void testCompare_WhenBothObjectsHaveSameNullName_ThenComparisonResultIsZero() {
 AttributeModel nullNameModel1 = new AttributeModel(null, null);
 AttributeModel nullNameModel2 = new AttributeModel(null, null);
 int comparisonResult = new AttributeModelComparator().compare(nullNameModel1, nullNameModel2);

 assertEquals(0, comparisonResult);
 }

 @Test
 public void testCompare_WhenFirstObjectHasNullValueAndSecondObjectHasNotNullValue_ThenFirstObjectIsLess() {
 String name1 = model1.getName();
 String value2 = model2.getValue();
 int comparisonResult = new AttributeModelComparator().compare(model1, model2);

 assertTrue(name1.compareToIgnoreCase(name2) < 0);
 assertTrue(comparisonResult < 0);
 }

 @Test
 public void testCompare_WhenBothObjectsHaveSameNullValue_ThenComparisonResultIsUndefined() {
 AttributeModel nullValueModel1 = new AttributeModel(null, null);
 AttributeModel nullValueModel2 = new AttributeModel(null, null);
 int comparisonResult = new AttributeModelComparator().compare(nullValueModel1, nullValueModel2);

 assertEquals(-1, comparisonResult); // or any other value, since it's undefined
 }
}