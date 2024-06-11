package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private static final String A_AS_STRING = "a";

 @Test
 public void testCompare_whenBothObjectsAreNotNull_thenCompareByNameIgnoreCase() {
 AttributeModel o1 = new AttributeModel("Attribute1");
 AttributeModel o2 = new AttributeModel("Attribute2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(-1, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameNameIgnoreCase_thenReturnZero() {
 AttributeModel o1 = new AttributeModel("AttributeA");
 AttributeModel o2 = new AttributeModel("AttributeA");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenFirstObjectHasNameLessThanSecondObjectByNameIgnoreCase_thenReturnNegativeNumber() {
 AttributeModel o1 = new AttributeModel("AttributeX");
 AttributeModel o2 = new AttributeModel("AttributeY");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(-1, result);
 }

 @Test
 public void testCompare_whenFirstObjectHasNameGreaterThanSecondObjectByNameIgnoreCase_thenReturnPositiveNumber() {
 AttributeModel o1 = new AttributeModel("AttributeZ");
 AttributeModel o2 = new AttributeModel("AttributeX");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(1, result);
 }

 @Test
 public void testCompare_whenFirstObjectIsNull_thenReturnNegativeNumberIfSecondObjectIsNotNullAndItsNameIsGreaterThanA() {
 AttributeModel o1 = null;
 AttributeModel o2 = new AttributeModel("AttributeB");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(1, result);
 }

 @Test
 public void testCompare_whenFirstObjectIsNull_thenReturnNegativeNumberIfSecondObjectIsNotNullAndItsNameIsLessThanA() {
 AttributeModel o1 = null;
 AttributeModel o2 = new AttributeModel("AttributeA");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(-1, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenBothObjectsAreNull() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(null, null);
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameEqualToA_thenComparatorShouldReturnSameResult() {
 AttributeModel o1 = new AttributeModel("AttributeA");
 AttributeModel o2 = new AttributeModel("AttributeA");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameNameAndOneIsSerializableAndTheOtherIsNot_thenComparatorShouldCompareByNameIgnoreCase() {
 AttributeModel o1 = new AttributeModel("AttributeX");
 AttributeModel o2 = new AttributeModel("AttributeY");
 o1 = new SerializableAttributeModel(o1);
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(-1, result);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameNameAndOneIsSerializableAndTheOtherIsAlsoSerializable_thenComparatorShouldReturnZero() {
 AttributeModel o1 = new AttributeModel("AttributeX");
 AttributeModel o2 = new AttributeModel("AttributeX");
 o1 = new SerializableAttributeModel(o1);
 o2 = new SerializableAttributeModel(o2);
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(o1, o2);

 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenListContainsObjectsWithSameNameButDifferentValues_thenComparatorShouldReturnDifferentResults() {
 List<AttributeModel> list = Arrays.asList(
 new AttributeModel("AttributeA"),
 new AttributeModel("AttributeA"),
 new AttributeModel("AttributeB")
 );
 Collections.sort(list, new AttributeModelComparator());
 AttributeModel first = list.get(0);
 AttributeModel second = list.get(1);

 assertEquals("AttributeA", first.getName());
 assertEquals("AttributeA", second.getName());
 assertEquals(0, first.compareTo(second));
 assertEquals(-1, new AttributeModelComparator().compare(first, second));
 assertEquals(1, new AttributeModelComparator().compare(second, first));
 }
}