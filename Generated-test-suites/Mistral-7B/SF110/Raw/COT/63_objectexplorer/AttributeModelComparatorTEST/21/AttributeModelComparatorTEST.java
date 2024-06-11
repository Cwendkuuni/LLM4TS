package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private List<AttributeModel> createAttributeModelsList() {
 return new ArrayList<>(Arrays.asList(
 new AttributeModel("Model1", "Attribute1"),
 new AttributeModel("Model1", "Attribute2"),
 new AttributeModel("Model2", "Attribute1"),
 new AttributeModel("Model2", "Attribute3"),
 new AttributeModel("Model3", "Attribute1"),
 new AttributeModel("Model3", "Attribute2"),
 new AttributeModel("Model3", "Attribute3"),
 new AttributeModel("Model4", "Attribute1"),
 new AttributeModel("Model4", "Attribute2"),
 new AttributeModel("Model4", "Attribute3"),
 new AttributeModel("Model5", "Attribute1"),
 new AttributeModel("Model5", "Attribute2"),
 new AttributeModel("Model5", "Attribute3"),
 new AttributeModel("Model6", "Attribute1"),
 new AttributeModel("Model6", "Attribute2"),
 new AttributeModel("Model6", "Attribute3"),
 new AttributeModel("Model7", "Attribute1"),
 new AttributeModel("Model7", "Attribute2"),
 new AttributeModel("Model7", "Attribute3")
 ));
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
 AttributeModel o1 = new AttributeModel("Model1", "Attribute1");
 int result = comparator.compare(o1, o1);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_whenFirstObjectIsNullAndSecondIsNotNull_thenSecondObjectPrecedesFirst() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = null;
 AttributeModel o2 = new AttributeModel("Model1", "Attribute1");
 int result = comparator.compare(o1, o2);
 assertTrue(result < 0);
 }

 @Test
 public void testCompare_whenFirstObjectIsNotNullAndSecondIsNull_thenFirstObjectPrecedesSecond() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel("Model1", "Attribute1");
 AttributeModel o2 = null;
 int result = comparator.compare(o1, o2);
 assertTrue(result > 0);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameNameButDifferentAttributes_thenFirstObjectPrecedesSecond() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel("Model1", "Attribute1");
 AttributeModel o2 = new AttributeModel("Model1", "Attribute2");
 int result = comparator.compare(o1, o2);
 assertTrue(result < 0);
 }

 @Test
 public void testCompare_whenBothObjectsHaveDifferentNamesButSameAttributes_thenNamesAreCompared() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel("Model1", "Attribute1");
 AttributeModel o2 = new AttributeModel("Model2", "Attribute1");
 int result = comparator.compare(o1, o2);
 String expected = "Model1".compareToIgnoreCase("Model2");
 assertTrue(result == expected);
 }

 @Test
 public void testCompare_whenFirstObjectNameIsBeforeA_andSecondObjectNameIsAfterA_thenFirstObjectPrecedesSecond() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel("Model1", "Attribute1");
 AttributeModel o2 = new AttributeModel("ModelA", "Attribute1");
 int result = comparator.compare(o1, o2);
 assertTrue(result < 0);
 }

 @Test
 public void testCompare_whenFirstObjectNameIsAfterA_andSecondObjectNameIsBeforeA_thenSecondObjectPrecedesFirst() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel("ModelA", "Attribute1");
 AttributeModel o2 = new AttributeModel("Model1", "Attribute1");
 int result = comparator.compare(o1, o2);
 assertTrue(result > 0);
 }

 @Test
 public void testCompare_whenBothObjectsHaveSameNameAndSameAttribute_thenNamesAreCompared() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel("Model1", "Attribute1");
 AttributeModel o2 = new AttributeModel("Model1", "Attribute1");
 int result = comparator.compare(o1, o2);
 String expected = "Model1".compareToIgnoreCase("Model1");
 assertTrue(result == expected);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenFirstObjectIsNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o2 = new AttributeModel("Model1", "Attribute1");
 int result = comparator.compare(null, o2);
 // JUnit exception handling
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenSecondObjectIsNull_thenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel o1 = new AttributeModel("Model1", "Attribute1");
 int result = comparator.compare(o1, null);
 // JUnit exception handling
 }

}