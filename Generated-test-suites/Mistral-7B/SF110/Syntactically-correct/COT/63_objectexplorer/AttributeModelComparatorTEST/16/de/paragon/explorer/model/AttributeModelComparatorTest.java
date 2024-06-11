package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel model1, model2, model3, model4;

 @Test
 public void testCompare_sameName_returnsZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("name1", "value2");

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_differentNames_returnsNegativeOrPositive() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("name2", "value2");

 int result = comparator.compare(model1, model2);
 assertNotNull(result);
 if (result < 0) {
 assertEquals("name1 is less than name2", true, comparator.compare(model1, model2) < 0);
 } else {
 assertEquals("name2 is less than name1", true, comparator.compare(model2, model1) > 0);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_nullObject_throwsException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = null;
 model2 = new AttributeModel("name2", "value2");

 try {
 comparator.compare(model1, model2);
 fail();
 } catch (NullPointerException e) {
 assertEquals("First argument is null", e.getMessage());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_secondNullObject_throwsException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = null;

 try {
 comparator.compare(model1, model2);
 fail();
 } catch (NullPointerException e) {
 assertEquals("Second argument is null", e.getMessage());
 }
 }

 @Test
 public void testCompare_bothNullNames_returnsZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = null;
 model2 = null;

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_firstNameIsLessThanStringA_returnsOne() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("a", "value2");

 int result = comparator.compare(model1, model2);
 assertEquals(1, result);
 }

 @Test
 public void testCompare_secondNameIsLessThanStringA_returnsMinusOne() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("a", "value2");

 int result = comparator.compare(model2, model1);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_firstNameIsA_returnsMinusOne() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("a", "value1");
 model2 = new AttributeModel("name2", "value2");

 int result = comparator.compare(model1, model2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_secondNameIsA_returnsOne() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("a", "value2");

 int result = comparator.compare(model2, model1);
 assertEquals(1, result);
 }

 @Test
 public void testCompare_bothNamesAreA_returnsZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("a", "value1");
 model2 = new AttributeModel("a", "value2");

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_bothObjectsAreSame_returnsZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = model1;

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_bothObjectsAreDifferentButHaveSameName_returnsZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = new AttributeModel("name1", "value2");

 int result = comparator.compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_objectsWithDifferentTypes_throwsClassCastException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 model1 = new AttributeModel("name1", "value1");
 model2 = new Object();

 try {
 comparator.compare(model1, model2);
 fail();
 } catch (ClassCastException e) {
 assertEquals("Cannot cast Object to AttributeModel", e.getMessage());
 }
 }
}