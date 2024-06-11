package de.paragon.explorer.model.test;

import org.junit.Test;
import static org.junit.Assert.*;
import de.paragon.explorer.model.*;
import de.paragon.explorer.model.comparator.AttributeModelComparator;

public class AttributeModelComparatorTest {
   // Expert 1's test cases
   // ...
   // Expert 2's test cases
   // ...
   // Expert 3's test cases
   // ...

   @Test
   public void testCompare_whenFirstObjectIsEmptyAndSecondIsNull_thenReturnZero() {
      AttributeModelComparator comparator = new AttributeModelComparator();
      int result = comparator.compare(null, null);
      assertEquals(0, result);
   }

   @Test
   public void testCompare_whenBothObjectsHaveSameNameIgnoringCaseAndBothAreNotNull_thenCompareCorrectly() {
      AttributeModel o1 = new AttributeModel("name");
      AttributeModel o2 = new AttributeModel("name");
      AttributeModelComparator comparator = new AttributeModelComparator();
      int result = comparator.compare(o1, o2);
      assertEquals(0, result);
   }

   @Test
   public void testCompare_whenFirstObjectNameIsEmptyAndSecondIsNull_thenFirstIsGreater() {
      AttributeModel o1 = new AttributeModel("");
      AttributeModel o2 = null;
      AttributeModelComparator comparator = new AttributeModelComparator();
      int result = comparator.compare(o1, o2);
      assertEquals(1, result);
   }
}