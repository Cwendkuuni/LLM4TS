package de.paragon.explorer.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import de.paragon.explorer.model.*;

public class AttributeModelComparatorTest {
   @Test
   public void testCompareSameNamedAttributes() {
      AttributeModel attributeModel1 = new AttributeModel("attributeA", "valueA");
      AttributeModel attributeModel2 = new AttributeModel("attributeA", "valueB");
      int compareResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      assertEquals(-1, compareResult);
   }

   @Test
   public void testCompareDifferentNamedAttributes() {
      AttributeModel attributeModel1 = new AttributeModel("attributeA", "valueA");
      AttributeModel attributeModel2 = new AttributeModel("attributeB", "valueB");
      int compareResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      assertEquals(0, compareResult);
   }

   @Test(expected = NullPointerException.class)
   public void testCompareNullAttributeModel() {
      AttributeModel attributeModel1 = null;
      AttributeModel attributeModel2 = new AttributeModel("attributeName", "valueB");
      int compareResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
   }

   @Test
   public void testCompareEmptyStringAttributes() {
      AttributeModel attributeModel1 = new AttributeModel("", "");
      AttributeModel attributeModel2 = new AttributeModel("attributeName", "valueB");
      int compareResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      assertEquals(-1, compareResult);
   }

   @Test
   public void testCompareSerializableAndNonSerializableAttributeModel() {
      AttributeModel attributeModel1 = new AttributeModel("serializableAttribute", new Object());
      AttributeModel attributeModel2 = new AttributeModel("nonSerializableAttribute", "valueB");
      int compareResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
      assertEquals(0, compareResult);
   }

   @Test(expected = FileNotFoundException.class)
   public void testCompareWithIOException() {
      AttributeModel attributeModel1 = new AttributeModel("attributeName", "valueA");
      AttributeModel attributeModel2 = new AttributeModel("attributeName", null);
      AttributeModelComparator comparator = new AttributeModelComparator();
      comparator.compare(attributeModel1, attributeModel2);
   }

   @Test(expected = ClassNotFoundException.class)
   public void testCompareWithClassNotFoundException() {
      AttributeModel attributeModel1 = new AttributeModel("attributeName", "valueA");
      AttributeModel attributeModel2 = new AttributeModel("attributeName", new Object());
      AttributeModelComparator comparator = new AttributeModelComparator();
      comparator.compare(attributeModel1, attributeModel2);
   }
}