package de.paragon.explorer.model.test;

import org.junit.Test;
import static org.junit.Assert.*;
import de.paragon.explorer.model.*;

public class AttributeModelComparatorTest {
   @Test
   public void testCompare_whenBothObjectsAreNotNullAndHaveDifferentNames_thenCorrectComparisonResult() {
      AttributeModel attributeModel1 = new AttributeModel("name1");
      AttributeModel attributeModel2 = new AttributeModel("name2");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = name1.compareToIgnoreCase("name2");
      assertEquals(expectedComparisonResult, comparisonResult);
   }

   @Test(expected = NullPointerException.class)
   public void testCompare_whenFirstObjectIsNull_thenThrowsNullPointerException() {
      AttributeModel attributeModel1 = null;
      AttributeModel attributeModel2 = new AttributeModel("name2");
      new AttributeModelComparator().compare(attributeModel1, attributeModel2);
   }

   @Test(expected = NullPointerException.class)
   public void testCompare_whenSecondObjectIsNull_thenThrowsNullPointerException() {
      AttributeModel attributeModel1 = new AttributeModel("name1");
      AttributeModel attributeModel2 = null;
      new AttributeModelComparator().compare(attributeModel1, attributeModel2);
   }

   @Test
   public void testCompare_whenBothObjectsHaveTheSameName_thenComparisonResultIsZero() {
      AttributeModel attributeModel1 = new AttributeModel("name1");
      AttributeModel attributeModel2 = new AttributeModel("name1");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = 0;
      assertEquals(expectedComparisonResult, comparisonResult);
   }

   @Test
   public void testCompare_whenBothObjectsHaveEmptyNames_thenComparisonResultIsZero() {
      AttributeModel attributeModel1 = new AttributeModel("");
      AttributeModel attributeModel2 = new AttributeModel("");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = 0;
      assertEquals(expectedComparisonResult, comparisonResult);
   }

   @Test
   public void testCompare_whenBothObjectsHaveLongNames_thenCorrectComparisonResult() {
      AttributeModel attributeModel1 = new AttributeModel("long_name1");
      AttributeModel attributeModel2 = new AttributeModel("long_name2");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = long_name1.compareToIgnoreCase("long_name2");
      assertEquals(expectedComparisonResult, comparisonResult);
   }

   @Test
   public void testCompare_whenFirstObjectNameIsALetter_thenCorrectComparisonResult() {
      AttributeModel attributeModel1 = new AttributeModel("A");
      AttributeModel attributeModel2 = new AttributeModel("Z");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = -1;
      assertEquals(expectedComparisonResult, comparisonResult);
   }

   @Test
   public void testCompare_whenSecondObjectNameIsALetter_thenCorrectComparisonResult() {
      AttributeModel attributeModel1 = new AttributeModel("Z");
      AttributeModel attributeModel2 = new AttributeModel("A");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = 1;
      assertEquals(expectedComparisonResult, comparisonResult);
   }

   @Test
   public void testCompare_whenFirstObjectNameIsAAndSecondObjectNameIsA_thenCorrectComparisonResult() {
      AttributeModel attributeModel1 = new AttributeModel("A");
      AttributeModel attributeModel2 = new AttributeModel("A");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = 0;
      assertEquals(expectedComparisonResult, comparisonResult);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCompare_whenFirstObjectNameIsANumber_thenThrowsIllegalArgumentException() {
      AttributeModel attributeModel1 = new AttributeModel("123");
      AttributeModel attributeModel2 = new AttributeModel("Name");
      new AttributeModelComparator().compare(attributeModel1, attributeModel2);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCompare_whenSecondObjectNameIsANumber_thenThrowsIllegalArgumentException() {
      AttributeModel attributeModel1 = new AttributeModel("Name");
      AttributeModel attributeModel2 = new AttributeModel("123");
      new AttributeModelComparator().compare(attributeModel1, attributeModel2);
   }

   @Test
   public void testCompare_whenFirstObjectNameIsEmpty_thenCorrectComparisonResult() {
      AttributeModel attributeModel1 = new AttributeModel("");
      AttributeModel attributeModel2 = new AttributeModel("Name");
      int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      int expectedComparisonResult = -1;
      assertEquals(expectedComparisonResult, comparisonResult);
   }
}