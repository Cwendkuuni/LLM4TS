package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
 public void testCompareNormalCase() {
 AttributeModel a1 = createAttributeModel("AttributeA");
 AttributeModel a2 = createAttributeModel("AttributeB");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(a1, a2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompareSameNameCase() {
 AttributeModel a1 = createAttributeModel("AttributeA");
 AttributeModel a2 = createAttributeModel("AttributeA");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(a1, a2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareNullCase() {
 AttributeModel a1 = null;
 AttributeModel a2 = createAttributeModel("AttributeB");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(a1, a2);
 assertNull(result);

 a1 = createAttributeModel("AttributeA");
 a2 = null;
 result = comparator.compare(a1, a2);
 assertEquals(1, result);
 }

 @Test
 public void testCompareWithAName() {
 AttributeModel a1 = createAttributeModel("AttributeA");
 AttributeModel a2 = createAttributeModel("a");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(a1, a2);
 assertEquals(-1, result);

 a1 = createAttributeModel("a");
 a2 = createAttributeModel("AttributeA");
 result = comparator.compare(a1, a2);
 assertEquals(1, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareWithNullAttributeModels() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareWithNullAttributeModel1() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(null, createAttributeModel("AttributeB"));
 }

 @Test(expected = NullPointerException.class)
 public void testCompareWithNullAttributeModel2() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(createAttributeModel("AttributeA"), null);
 }

 @Test
 public void testCompareEdgeCases() {
 AttributeModel a1 = createAttributeModel("Z");
 AttributeModel a2 = createAttributeModel("aaaA");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(a1, a2);
 assertEquals(-1, result);

 a1 = createAttributeModel("aaaA");
 a2 = createAttributeModel("Z");
 result = comparator.compare(a1, a2);
 assertEquals(1, result);
 }

 @Test
 public void testCompareCaseInsensitive() {
 AttributeModel a1 = createAttributeModel("AttributeA");
 AttributeModel a2 = createAttributeModel("attributeB");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(a1, a2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareEmptyList() {
 List<AttributeModel> list1 = new ArrayList<>();
 List<AttributeModel> list2 = new ArrayList<>();
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = Collections.sort(list1, comparator).get(list1.size() - 1).compareTo(Collections.sort(list2, comparator).get(list2.size() - 1));
 assertEquals(0, result);
 }

 @Test
 public void testCompareMixedList() {
 List<AttributeModel> list1 = new ArrayList<>();
 list1.add(createAttributeModel("AttributeA"));
 list1.add(createAttributeModel("a"));

 List<AttributeModel> list2 = new ArrayList<>();
 list2.add(createAttributeModel("a"));
 list2.add(createAttributeModel("AttributeA"));

 AttributeModelComparator comparator = new AttributeModelComparator();
 Collections.sort(list1, comparator);
 Collections.sort(list2, comparator);

 AttributeModel lastFromList1 = list1.get(list1.size() - 1);
 AttributeModel lastFromList2 = list2.get(list2.size() - 1);
 int result = lastFromList1.compareTo(lastFromList2);
 assertEquals(0, result);
 }
}