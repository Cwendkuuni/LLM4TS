package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel model1, model2, model3;

 @Test
 public void testCompareNullObjects() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertNull(comparator.compare(null, null));
 }

 @Test
 public void testCompareEqualObjects() {
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("name1", "value1");
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertEquals(0, comparator.compare(model1, model2));
 }

 @Test
 public void testCompareDifferentObjects() {
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("name2", "value2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model1, model2);
 assertTrue(comparisonResult < 0 || comparisonResult > 0);
 }

 @Test
 public void testCompareIgnoreCase() {
 model1 = new AttributeModel("Name1", "value1");
 model2 = new AttributeModel("Name2", "value2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model1, model2);
 assertEquals(-1 * model1.getName().compareToIgnoreCase(model2.getName()), comparisonResult);
 }

 @Test
 public void testCompareWithAFirst() {
 model1 = new AttributeModel("a", "value1");
 model2 = new AttributeModel("Name2", "value2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model1, model2);
 assertEquals(1, comparisonResult);
 }

 @Test
 public void testCompareWithAAfter() {
 model1 = new AttributeModel("Name1", "value1");
 model2 = new AttributeModel("a", "value2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model1, model2);
 assertEquals(-1, comparisonResult);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareWithNullObject() {
 model1 = new AttributeModel("Name1", "value1");
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(model1, null);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareWithNullAttributeModel() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(null, new AttributeModel("Name1", "value1"));
 }
}