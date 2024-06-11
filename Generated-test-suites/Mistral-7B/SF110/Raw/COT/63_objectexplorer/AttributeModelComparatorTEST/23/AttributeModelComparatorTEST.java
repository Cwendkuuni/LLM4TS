package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel model1;
 private AttributeModel model2;
 private AttributeModel model3;

 @Before
 public void setup() {
 model1 = new AttributeModel("Name1", "Description1");
 model2 = new AttributeModel("Name2", "Description2");
 model3 = new AttributeModel("NameA", "DescriptionA");
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
 int result = comparator.compare(model1, model1);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenOneObjectIsNull_thenReturnNegativeNumber() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, null);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_whenNamesAreEqualIgnoreCase_thenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(new AttributeModel("Name1", "Description1"), new AttributeModel("Name1", "Description1"));
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenNamesAreDifferentIgnoreCase_thenReturnValueOfCompareTo() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertNotSame(-1, result);
 assertNotSame(0, result);
 assertNotSame(1, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenFirstObjectIsNullAndNamesAreDifferentIgnoreCase_thenThrowException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertThrows(NullPointerException.class, () -> comparator.compare(null, model2));
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenSecondObjectIsNullAndNamesAreDifferentIgnoreCase_thenThrowException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertThrows(NullPointerException.class, () -> comparator.compare(model1, null));
 }

 @Test
 public void testCompare_whenBothObjectsHaveNameA_thenCompareByName() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model3, model1);
 assertEquals(1, result);
 }

 @Test
 public void testCompare_whenListIsEmpty_thenReturnZero() {
 List<AttributeModel> list = Collections.emptyList();
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenListContainsTwoObjects_thenCompareCorrectly() {
 List<AttributeModel> list = new ArrayList<>(Arrays.asList(model1, model2));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertNotSame(-1, result);
 assertNotSame(0, result);
 assertNotSame(1, result);
 }

 @Test
 public void testCompare_whenListContainsDuplicates_thenCompareCorrectly() {
 List<AttributeModel> list = new ArrayList<>(Arrays.asList(model1, model1));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model1);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenListContainsMultipleObjects_thenCompareCorrectly() {
 List<AttributeModel> list = new ArrayList<>(Arrays.asList(model1, model2, model3));
 AttributeModelComparator comparator = new AttributeModelComparator();
 int result = comparator.compare(model1, model2);
 assertNotSame(-1, result);
 assertNotSame(0, result);
 assertNotSame(1, result);

 int result2 = comparator.compare(model2, model3);
 assertEquals(-1, result2);

 int result3 = comparator.compare(model3, model1);
 assertEquals(1, result3);
 }
}