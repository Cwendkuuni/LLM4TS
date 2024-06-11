package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
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
 public void testCompare_whenBothObjectsAreSame_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 int result = comparator.compare(attributeModel1, attributeModel1);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenFirstObjectIsNull_thenReturnNegativeValue() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 int result = comparator.compare(null, attributeModel1);
 assertNotNull(result);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_whenSecondObjectIsNull_thenReturnPositiveValue() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 int result = comparator.compare(attributeModel1, null);
 assertNotNull(result);
 assertEquals(1, result);
 }

 @Test
 public void testCompare_whenNamesAreEqualIgnoringCase() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("Name1");
 AttributeModel attributeModel2 = createAttributeModel("name1");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenBothNamesAreASmallerThanA() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("Z");
 AttributeModel attributeModel2 = createAttributeModel("A");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_whenFirstNameIsASmallerThanSecondName() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 AttributeModel attributeModel2 = createAttributeModel("name2");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_whenFirstNameIsGreaterThanSecondName() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name2");
 AttributeModel attributeModel2 = createAttributeModel("name1");
 int result = comparator.compare(attributeModel1, attributeModel2);
 assertEquals(1, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenFirstObjectIsNullAndSecondIsNotNull() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = null;
 AttributeModel attributeModel2 = createAttributeModel("name2");
 int result = comparator.compare(attributeModel1, attributeModel2);
 fail("Comparing null object should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenSecondObjectIsNullAndFirstIsNotNull() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel attributeModel1 = createAttributeModel("name1");
 AttributeModel attributeModel2 = null;
 int result = comparator.compare(attributeModel1, attributeModel2);
 fail("Comparing non-null object with null should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenBothObjectsAreNullAndComparingIsCalledMoreThanOnce() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(null, null);
 int result = comparator.compare(null, null);
 fail("Comparing null objects more than once should throw NullPointerException");
 }

 @Test
 public void testCompare_whenListIsSortedUsingComparator() {
 List<AttributeModel> attributeModels = new ArrayList<>();
 attributeModels.add(createAttributeModel("Z"));
 attributeModels.add(createAttributeModel("name1"));
 attributeModels.add(createAttributeModel("name2"));
 attributeModels.add(createAttributeModel("name3"));
 attributeModels.add(createAttributeModel("A"));
 Collections.sort(attributeModels, new AttributeModelComparator());
 List<String> names = new ArrayList<>();
 for (AttributeModel attributeModel : attributeModels) {
 names.add(attributeModel.getName());
 }
 List<String> expectedNames = Arrays.asList("A", "name1", "name2", "name3", "Z");
 assertEquals(expectedNames, names);
 }

}