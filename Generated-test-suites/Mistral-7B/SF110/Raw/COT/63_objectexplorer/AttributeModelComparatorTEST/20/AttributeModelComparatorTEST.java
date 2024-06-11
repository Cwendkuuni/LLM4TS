package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel getAttributeModel(String name) {
 return new AttributeModel(name);
 }

 @Test
 public void testCompare_whenBothObjectsNull_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(null, null);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsNotNullAndHaveSameName_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("test");
 AttributeModel model2 = getAttributeModel("test");
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenFirstObjectNullAndSecondObjectNotNull_thenReturnNegativeNumber() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = null;
 AttributeModel model2 = getAttributeModel("test");
 int result = comparator.compare(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompare_whenFirstObjectNotNullAndSecondObjectNull_thenReturnPositiveNumber() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("test");
 AttributeModel model2 = null;
 int result = comparator.compare(model1, model2);
 assertGreaterThan(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsNotNullAndHaveDifferentNames_thenReturnNegativeOrPositiveNumberBasedOnAlphabeticalOrder() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("Apple");
 AttributeModel model2 = getAttributeModel("Banana");
 int result = comparator.compare(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsNotNullAndHaveSameNameIgnoreCase_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("test");
 AttributeModel model2 = getAttributeModel("Test");
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenFirstObjectIsNullAndSecondObjectNotNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model2 = getAttributeModel("Test");
 int result = comparator.compare(null, model2);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenSecondObjectIsNullAndFirstObjectNotNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("Test");
 int result = comparator.compare(model1, null);
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameEqualToAAndFirstObjectNameIsBeforeSecondObjectNameInAlphabeticalOrder_thenReturnPositiveNumber() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("a");
 AttributeModel model2 = getAttributeModel("A");
 int result = comparator.compare(model1, model2);
 assertGreaterThan(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameEqualToAAndFirstObjectNameIsBeforeSecondObjectNameInAlphabeticalOrder_thenReturnNegativeNumber() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("A");
 AttributeModel model2 = getAttributeModel("a");
 int result = comparator.compare(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameEqualToANotEqualsAA_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("A");
 AttributeModel model2 = getAttributeModel("B");
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameContainingASubstring_thenCompareBasedOnWholeName() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("AppleA");
 AttributeModel model2 = getAttributeModel("BananaA");
 int result = comparator.compare(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameContainingASubstring_thenCompareBasedOnWholeNameWithCaseInsensitivity() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("AppleA");
 AttributeModel model2 = getAttributeModel("bananaA");
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenObjectsHaveMixedNames_thenCompareBasedOnWholeNameWithCaseInsensitivity() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("AppleA");
 AttributeModel model2 = getAttributeModel("BananaB");
 int result = comparator.compare(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenObjectsAreIdentical_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("test");
 AttributeModel model2 = getAttributeModel("test");
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenObjectsHaveDifferentNames_thenReturnNegativeOrPositiveNumberBasedOnAlphabeticalOrder() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("Apple");
 AttributeModel model2 = getAttributeModel("Banana");
 int result = comparator.compareToIgnoreCase(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenObjectsHaveSameNameIgnoreCase_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("Test");
 AttributeModel model2 = getAttributeModel("test");
 int result = comparator.compareToIgnoreCase(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenObjectsHaveSameNameButDifferentCase_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("Test");
 AttributeModel model2 = getAttributeModel("test");
 int result = comparator.compareToIgnoreCase(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenObjectsHaveDifferentNamesButSameSubstring_thenCompareBasedOnWholeNameWithCaseInsensitivity() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("AppleA");
 AttributeModel model2 = getAttributeModel("BananaA");
 int result = comparator.compareToIgnoreCase(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenObjectsHaveDifferentNamesButBothContainSubstring_thenCompareBasedOnWholeNameWithCaseInsensitivity() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("AppleA");
 AttributeModel model2 = getAttributeModel("BananaB");
 int result = comparator.compareToIgnoreCase(model1, model2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenObjectsHaveTheSameName_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("test");
 AttributeModel model2 = getAttributeModel("test");
 int result = comparator.compareToIgnoreCase(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToIgnoreCase_whenBothObjectsAreNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertNull(comparator.compareToIgnoreCase(null, null));
 }

 @Test
 public void testCompareToIgnoreCase_whenFirstObjectIsNullAndSecondObjectIsNotNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model2 = getAttributeModel("test");
 assertNull(comparator.compareToIgnoreCase(null, model2));
 }

 @Test
 public void testCompareToIgnoreCase_whenSecondObjectIsNullAndFirstObjectIsNotNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = getAttributeModel("test");
 assertNull(comparator.compareToIgnoreCase(model1, null));
 }

}