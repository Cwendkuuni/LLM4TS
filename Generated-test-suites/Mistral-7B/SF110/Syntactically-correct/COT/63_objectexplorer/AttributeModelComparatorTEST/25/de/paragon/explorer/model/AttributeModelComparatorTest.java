package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel createAttributeModel(String name) {
 return new AttributeModel(name);
 }

 @Test
 public void testCompare_whenBothObjectsAreNull_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(null, null);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsAreNotnullAndHaveSameName_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("testName");
 AttributeModel o2 = createAttributeModel("testName");
 int result = comparator.compare(o1, o2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsAreNotnullAndHaveDifferentNames_thenReturnNegativeOrPositive() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("testName1");
 AttributeModel o2 = createAttributeModel("testName2");
 int result = comparator.compare(o1, o2);
 assertNotSame(0, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenFirstObjectIsNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o2 = createAttributeModel("testName");
 int result = comparator.compare(null, o2);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenSecondObjectIsNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("testName");
 int result = comparator.compare(o1, null);
 }

 @Test
 public void testCompare_whenFirstObjectNameIsLessThanSecondObjectName_thenReturnNegative() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("testName1");
 AttributeModel o2 = createAttributeModel("testName2");
 int result = comparator.compare(o1, o2);
 assertLess(0, result);
 }

 @Test
 public void testCompare_whenFirstObjectNameIsGreaterThanSecondObjectName_thenReturnPositive() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("testName2");
 AttributeModel o2 = createAttributeModel("testName1");
 int result = comparator.compare(o1, o2);
 assertGreater(0, result);
 }

 @Test
 public void testCompare_whenFirstObjectNameIsEqualToASpecialValue_thenReturnFirstObjectIsGreaterThanSecondObject() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("testName");
 AttributeModel o2 = createAttributeModel("a");
 int result = comparator.compare(o1, o2);
 assertGreater(0, result);
 }

 @Test
 public void testCompare_whenSecondObjectNameIsEqualToASpecialValue_thenReturnFirstObjectIsLessThanSecondObject() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = createAttributeModel("a");
 AttributeModel o2 = createAttributeModel("testName");
 int result = comparator.compare(o1, o2);
 assertLess(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameNameAndFirstObjectIsLessThanSecondObjectInList_thenReturnNegative() {
 List<AttributeModel> attributeModels = new ArrayList<>(Arrays.asList(
 new AttributeModel("testName1"),
 new AttributeModel("testName"),
 new AttributeModel("testName2")
 ));
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = attributeModels.get(1);
 AttributeModel o2 = attributeModels.get(2);
 int result = comparator.compare(o1, o2);
 assertLess(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameNameAndFirstObjectIsGreaterThanSecondObjectInList_thenReturnPositive() {
 List<AttributeModel> attributeModels = new ArrayList<>(Arrays.asList(
 new AttributeModel("testName1"),
 new AttributeModel("testName2"),
 new AttributeModel("testName")
 ));
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = attributeModels.get(1);
 AttributeModel o2 = attributeModels.get(2);
 int result = comparator.compare(o1, o2);
 assertGreater(0, result);
 }

}