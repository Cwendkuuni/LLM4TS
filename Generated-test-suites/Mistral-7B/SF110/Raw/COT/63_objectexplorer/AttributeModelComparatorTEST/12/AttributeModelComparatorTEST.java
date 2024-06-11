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
 return new AttributeModel(name, "value");
 }

 @Test
 public void testCompareNullAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertNull(comparator.compare(null, null));
 }

 @Test
 public void testCompareEqualAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 AttributeModel attributeModel2 = createAttributeModel("name1");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareDifferentNameAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 AttributeModel attributeModel2 = createAttributeModel("name2");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertNotEquals(0, result);
 }

 @Test
 public void testCompareSameNameFirstAAttributeModel() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("a");
 AttributeModel attributeModel2 = createAttributeModel("name2");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertLessThan(0, result);
 }

 @Test
 public void testCompareSameNameLastAAttributeModel() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 AttributeModel attributeModel2 = createAttributeModel("a");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertGreaterThan(0, result);
 }

 @Test
 public void testCompareSameNameAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("a");
 AttributeModel attributeModel2 = createAttributeModel("a");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertEquals(0, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullAttributeModel() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 AttributeModel attributeModel2 = null;
 int result = comparator.compare(attributeModel1, attributeModel2);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullAttributeName1() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = null;
 AttributeModel attributeModel2 = createAttributeModel("name2");
 int result = comparator.compare(attributeModel1, attributeModel2);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullAttributeName2() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 AttributeModel attributeModel2 = null;
 int result = comparator.compare(attributeModel1, attributeModel2);
 fail("Should have thrown NullPointerException");
 }

 @Test
 public void testCompareEmptyList() {
 List<AttributeModel> list1 = new ArrayList<>();
 List<AttributeModel> list2 = new ArrayList<>();
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1, list2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareListWithSingleElement() {
 List<AttributeModel> list1 = Arrays.asList(createAttributeModel("name1"));
 List<AttributeModel> list2 = new ArrayList<>();
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1, list2);
 assertNotEquals(0, result);
 }

 @Test
 public void testCompareListWithSingleElementInSecondList() {
 List<AttributeModel> list1 = new ArrayList<>();
 List<AttributeModel> list2 = Arrays.asList(createAttributeModel("name2"));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1, list2);
 assertNotEquals(0, result);
 }

 @Test
 public void testCompareListsWithEqualElements() {
 List<AttributeModel> list1 = Arrays.asList(createAttributeModel("name1"));
 List<AttributeModel> list2 = Arrays.asList(createAttributeModel("name1"));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1, list2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareListsWithDifferentElements() {
 List<AttributeModel> list1 = Arrays.asList(createAttributeModel("name1"), createAttributeModel("name2"));
 List<AttributeModel> list2 = Arrays.asList(createAttributeModel("name3"), createAttributeModel("name4"));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1, list2);
 assertNotEquals(0, result);
 }

 @Test
 public void testCompareListsWithMixedEqualAndDifferentElements() {
 List<AttributeModel> list1 = Arrays.asList(createAttributeModel("name1"), createAttributeModel("name2"), createAttributeModel("name1"));
 List<AttributeModel> list2 = Arrays.asList(createAttributeModel("name3"), createAttributeModel("name4"), createAttributeModel("name1"));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(list1, list2);
 assertNotEquals(0, result);
 }
}