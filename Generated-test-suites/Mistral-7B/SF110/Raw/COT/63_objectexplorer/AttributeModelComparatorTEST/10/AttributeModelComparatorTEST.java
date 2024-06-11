package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel getAttributeModel(String name) {
 return new AttributeModel(name);
 }

 // Test for compare method with valid inputs
 @Test
 public void testCompareValidInputs() {
 AttributeModel model1 = getAttributeModel("A");
 AttributeModel model2 = getAttributeModel("B");
 AttributeModel model3 = getAttributeModel("A");
 AttributeModel model4 = getAttributeModel("C");

 List<AttributeModel> list = new ArrayList<>(Arrays.asList(model1, model2, model3, model4));
 AttributeModelComparator comparator = new AttributeModelComparator();

 int result = comparator.compare(model1, model2);
 assertEquals(-1, result);

 result = comparator.compare(model1, model1);
 assertEquals(0, result);

 result = comparator.compare(model3, model1);
 assertEquals(0, result);

 result = comparator.compare(model2, model4);
 assertEquals(-1, result);
 }

 // Test for compare method with null inputs
 @Test(expected = NullPointerException.class)
 public void testCompareNullInputs() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(null, getAttributeModel("B"));
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullInputs2() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(getAttributeModel("A"), null);
 }

 // Test for compare method with same name but different case
 @Test
 public void testCompareSameNameDifferentCase() {
 AttributeModel model1 = getAttributeModel("a");
 AttributeModel model2 = getAttributeModel("A");
 AttributeModelComparator comparator = new AttributeModelComparator();

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 // Test for compare method with name "a"
 @Test
 public void testCompareNameAA() {
 AttributeModel model1 = getAttributeModel("a");
 AttributeModel model2 = getAttributeModel("a");
 AttributeModelComparator comparator = new AttributeModelComparator();

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 // Test for compare method with name "a" and o1 < o2
 @Test
 public void testCompareNameAAO1LessThanO2() {
 AttributeModel model1 = getAttributeModel("a");
 AttributeModel model2 = getAttributeModel("B");
 AttributeModelComparator comparator = new AttributeModelComparator();

 int result = comparator.compare(model1, model2);
 assertEquals(-1, result);
 }

 // Test for compare method with name "a" and o2 < o1
 @Test
 public void testCompareNameAO2LessThanO1() {
 AttributeModel model1 = getAttributeModel("B");
 AttributeModel model2 = getAttributeModel("a");
 AttributeModelComparator comparator = new AttributeModelComparator();

 int result = comparator.compare(model1, model2);
 assertEquals(1, result);
 }

 // Test for compare method with name "a" and both have the same position
 @Test
 public void testCompareNameAASamePosition() {
 AttributeModel model1 = getAttributeModel("a");
 AttributeModel model2 = getAttributeModel("a");
 AttributeModelComparator comparator = new AttributeModelComparator();

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }
}