package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private List<AttributeModel> createAttributeModelsList() {
  List<AttributeModel> list = new ArrayList<>();
  list.add(new AttributeModel("Name1", "Value1"));
  list.add(new AttributeModel("Name2", "Value2"));
  list.add(new AttributeModel("Name3", "Value3"));
  list.add(new AttributeModel("Name4", "Value4"));
  list.add(new AttributeModel("Name5", "Value5"));
  list.add(new AttributeModel("Name6", "Value6"));
  list.add(new AttributeModel("Name7", "Value7"));
  list.add(new AttributeModel("Name8", "Value8"));
  list.add(new AttributeModel("Name9", "Value9"));
  list.add(new AttributeModel("Name10", "Value10"));
  return list;
 }

 @Test
 public void testCompare_WhenBothObjectsNull_ShouldReturnZero() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  int result = comparator.compare(null, null);
  assertEquals(0, result);
 }

 @Test
 public void testCompare_WhenBothObjectsNotNullAndSameName_ShouldReturnZero() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  AttributeModel o1 = new AttributeModel("Name1", "Value1");
  AttributeModel o2 = new AttributeModel("Name1", "Value1");
  int result = comparator.compare(o1, o2);
  assertEquals(0, result);
 }

 @Test
 public void testCompare_WhenBothObjectsNotNullAndDifferentNames_ShouldReturnNegativeOrPositive() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  AttributeModel o1 = new AttributeModel("Name1", "Value1");
  AttributeModel o2 = new AttributeModel("Name2", "Value2");
  int result = comparator.compare(o1, o2);
  assertTrue(result < 0);

  o1 = new AttributeModel("NameA", "ValueA");
  o2 = new AttributeModel("NameB", "ValueB");
  result = comparator.compare(o1, o2);
  assertTrue(result < 0);

  o1 = new AttributeModel("NameX", "ValueX");
  o2 = new AttributeModel("NameY", "ValueY");
  result = comparator.compare(o1, o2);
  assertTrue(result < 0);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_WhenFirstObjectNull_ShouldThrowNullPointerException() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  int result = comparator.compare(null, new AttributeModel("Name1", "Value1"));
  fail("Expected NullPointerException was not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_WhenSecondObjectNull_ShouldThrowNullPointerException() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  int result = comparator.compare(new AttributeModel("Name1", "Value1"), null);
  fail("Expected NullPointerException was not thrown");
 }

 @Test
 public void testCompare_WhenBothObjectsNotNullAndSameNameIgnoreCase_ShouldReturnZero() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  AttributeModel o1 = new AttributeModel("Name1", "Value1");
  AttributeModel o2 = new AttributeModel("name1", "Value1");
  int result = comparator.compare(o1, o2);
  assertEquals(0, result);
 }

 @Test
 public void testCompare_WhenBothObjectsNotNullAndSameNameIgnoreCaseWithASpecificName_ShouldReturnNegativeOrPositive() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  AttributeModel o1 = new AttributeModel("Name1", "Value1");
  AttributeModel o2 = new AttributeModel("NameA", "ValueA");
  int result = comparator.compare(o1, o2);
  assertTrue(result < 0);

  o1 = new AttributeModel("NameA", "ValueA");
  o2 = new AttributeModel("Name1", "Value1");
  result = comparator.compare(o1, o2);
  assertTrue(result > 0);
 }

 @Test
 public void testSort_WhenListNotNull_ShouldSortAttributeModelsByNamesIgnoreCase() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  List<AttributeModel> list = createAttributeModelsList();
  Collections.sort(list, comparator);
  AttributeModel expectedFirst = new AttributeModel("Name1", "Value1");
  assertEquals(expectedFirst, list.get(0));

  AttributeModel expectedLast = new AttributeModel("Name10", "Value10");
  assertEquals(expectedLast, list.get(list.size() - 1));
 }

 @Test
 public void testSort_WhenListNotNullWithDuplicates_ShouldSortAttributeModelsByNamesIgnoreCaseMaintainingOrder() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  List<AttributeModel> list = new ArrayList<>();
  list.add(new AttributeModel("Name1", "Value1"));
  list.add(new AttributeModel("Name1", "Value1"));
  list.add(new AttributeModel("Name2", "Value2"));
  list.add(new AttributeModel("Name1", "Value1"));
  Collections.sort(list, comparator);
  AttributeModel expectedFirst = new AttributeModel("Name1", "Value1");
  assertEquals(expectedFirst, list.get(0));

  AttributeModel expectedLast = new AttributeModel("Name2", "Value2");
  assertEquals(expectedLast, list.get(list.size() - 1));

  int count = 0;
  for (AttributeModel model : list) {
   if (model.getName().equalsIgnoreCase("Name1")) {
    count++;
   }
   assertEquals("Count of Name1 should be equal to 3", 3, count);
  }
 }

 @Test
 public void testSort_WhenListNull_ShouldNotChangeList() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  List<AttributeModel> list = null;
  Collections.sort(list, comparator);
  assertNull("List should be null", list);
 }

 @Test
 public void testSort_WhenListEmpty_ShouldNotChangeList() {
  AttributeModelComparator comparator = new AttributeModelComparator();
  List<AttributeModel> list = new ArrayList<>();
  Collections.sort(list, comparator);
  assertTrue("List should be empty", list.isEmpty());
 }
}