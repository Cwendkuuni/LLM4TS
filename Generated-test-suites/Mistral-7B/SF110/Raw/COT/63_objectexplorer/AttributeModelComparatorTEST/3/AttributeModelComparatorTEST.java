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
 public void testCompare_WhenBothObjectsAreNotNullAndHaveDifferentNames_ReturnCorrectValue() {
 AttributeModel o1 = createAttributeModel("Attribute1");
 AttributeModel o2 = createAttributeModel("Attribute2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(o1, o2);
 assertTrue(comparisonResult < 0);
 }

 @Test
 public void testCompare_WhenBothObjectsAreNotNullAndHaveSameName_ReturnZero() {
 AttributeModel o1 = createAttributeModel("Attribute1");
 AttributeModel o2 = createAttributeModel("Attribute1");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(o1, o2);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testCompare_WhenFirstObjectIsNull_ReturnNegativeValue() {
 AttributeModel o1 = null;
 AttributeModel o2 = createAttributeModel("Attribute2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(o1, o2);
 assertNotNull(comparisonResult);
 assertTrue(comparisonResult < 0);
 }

 @Test
 public void testCompare_WhenSecondObjectIsNull_ReturnPositiveValue() {
 AttributeModel o1 = createAttributeModel("Attribute1");
 AttributeModel o2 = null;
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(o1, o2);
 assertNotNull(comparator);
 assertTrue(comparationResult > 0);
 }

 @Test
 public void testCompare_WhenBothObjectsHaveNameAsStringA_ReturnSpecificValue() {
 AttributeModel o1 = createAttributeModel("a");
 AttributeModel o2 = createAttributeModel("Attribute2");
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(o1, o2);
 assertTrue(comparisonResult > 0);

 o1 = createAttributeModel("Attribute1");
 o2 = createAttributeModel("a");
 comparisonResult = comparator.compare(o1, o2);
 assertTrue(comparisonResult < 0);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_WhenBothObjectsAreNull_ThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 comparator.compare(null, null);
 }
}