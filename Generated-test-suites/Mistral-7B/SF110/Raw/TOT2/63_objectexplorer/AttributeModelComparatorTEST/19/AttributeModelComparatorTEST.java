package de.paragon.explorer.model.test;

import org.junit.Test;
import static org.junit.Assert.*;
import de.paragon.explorer.model.AttributeModelComparator;
import de.paragon.explorer.model.AttributeModel;

public class AttributeModelComparatorTest {

 @Test
 public void testCompare_DifferentNames() {
 AttributeModel attributeModel1 = new AttributeModel("name1");
 AttributeModel attributeModel2 = new AttributeModel("name2");
 int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
 assertEquals(-1, comparisonResult);
 }

 @Test
 public void testCompare_SameName() {
 AttributeModel attributeModel1 = new AttributeModel("name1");
 AttributeModel attributeModel2 = new AttributeModel("name1");
 int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testCompare_NullObjects() {
 AttributeModel attributeModel1 = null;
 AttributeModel attributeModel2 = new AttributeModel("name2");
 int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
 assertEquals(Integer.MIN_VALUE, comparisonResult);
 }

 @Test
 public void testCompare_SameNameA() {
 AttributeModel attributeModel1 = new AttributeModel("a");
 AttributeModel attributeModel2 = new AttributeModel("a");
 int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
 assertEquals(1, comparisonResult);
 }

 @Test
 public void testCompare_NullNames() {
 AttributeModel attributeModel1 = new AttributeModel(null);
 AttributeModel attributeModel2 = new AttributeModel(null);
 int comparisonResult = new AttributeModelComparator().compare(attributeModel1, attributeModel2);
 assertEquals(0, comparisonResult);
 }
}