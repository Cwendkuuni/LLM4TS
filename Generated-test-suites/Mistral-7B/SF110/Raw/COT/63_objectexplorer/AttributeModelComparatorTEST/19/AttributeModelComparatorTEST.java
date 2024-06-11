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

 // Test for compare method with valid inputs
 @Test
 public void testCompareValidInputs() {
 AttributeModel attrModel1 = createAttributeModel("attr1");
 AttributeModel attrModel2 = createAttributeModel("attr2");
 AttributeModel attrModel3 = createAttributeModel("attr3");
 AttributeModel attrModelA = createAttributeModel("a");

 AttributeModelComparator comparator = new AttributeModelComparator();

 int result1 = comparator.compare(attrModel1, attrModel2);
 assertEquals(-1, result1);

 int result2 = comparator.compare(attrModel2, attrModel1);
 assertEquals(1, result2);

 int result3 = comparator.compare(attrModel1, attrModel1);
 assertEquals(0, result3);

 int result4 = comparator.compare(attrModelA, attrModel1);
 assertEquals(-1, result4);

 int result5 = comparator.compare(attrModel1, attrModelA);
 assertEquals(1, result5);
 }

 // Test for compare method with null inputs
 @Test(expected = NullPointerException.class)
 public void testCompareNullInputs() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(null, createAttributeModel("attr1"));
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullInputs1() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(createAttributeModel("attr1"), null);
 }

 // Test for compare method with same name inputs
 @Test
 public void testCompareSameNameInputs() {
 AttributeModel attrModel1 = createAttributeModel("attr1");
 AttributeModel attrModel2 = createAttributeModel("attr1");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(attrModel1, attrModel2);
 assertEquals(0, result);
 }

 // Test for compare method with "a" name input
 @Test
 public void testCompareAInputs() {
 AttributeModel attrModel1 = createAttributeModel("a");
 AttributeModel attrModel2 = createAttributeModel("attr1");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(attrModel1, attrModel2);
 assertEquals(-1, result);

 attrModel1 = createAttributeModel("attr1");
 attrModel2 = createAttributeModel("a");
 result = comparator.compare(attrModel1, attrModel2);
 assertEquals(1, result);
 }

 // Test for compare method with empty list
 @Test
 public void testCompareEmptyList() {
 List<AttributeModel> list1 = new ArrayList<>();
 List<AttributeModel> list2 = new ArrayList<>();
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1.get(0), list2.get(0));
 assertNull(result);
 }

 // Test for compare method with different list sizes
 @Test
 public void testCompareDifferentListSizes() {
 List<AttributeModel> list1 = Arrays.asList(createAttributeModel("attr1"), createAttributeModel("attr2"));
 List<AttributeModel> list2 = Arrays.asList(createAttributeModel("attr1"), createAttributeModel("attr2"), createAttributeModel("attr3"));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1.get(0), list2.get(0));
 assertEquals(-1, result);
 }

 // Test for compare method with different list orders
 @Test
 public void testCompareDifferentListOrders() {
 List<AttributeModel> list1 = Arrays.asList(createAttributeModel("attr2"), createAttributeModel("attr1"));
 List<AttributeModel> list2 = Arrays.asList(createAttributeModel("attr1"), createAttributeModel("attr2"));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1.get(0), list2.get(0));
 assertEquals(1, result);
 }
}