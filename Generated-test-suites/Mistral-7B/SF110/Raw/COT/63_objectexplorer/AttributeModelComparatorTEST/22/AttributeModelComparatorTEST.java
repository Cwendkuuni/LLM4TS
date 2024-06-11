package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModel model1, model2, model3, model4;

 @Test
 public void testCompare() {
 model1 = new AttributeModel("Name1", "Value1");
 model2 = new AttributeModel("Name2", "Value2");
 model3 = new AttributeModel("Name3", "Value3");
 model4 = new AttributeModel("NameA", "ValueA");

 // Test when both objects are null
 assertNull(AttributeModelComparator.compare(null, null));

 // Test when both objects are same
 assertEquals(0, AttributeModelComparator.compare(model1, model1));

 // Test compare with different names, ignoring case
 assertEquals(-1, AttributeModelComparator.compare(model1, model2));
 assertEquals(1, AttributeModelComparator.compare(model2, model1));

 // Test compare with equal names, but different objects
 assertNotEquals(0, AttributeModelComparator.compare(model1, model3));
 assertNotEquals(0, AttributeModelComparator.compare(model3, model1));

 // Test compare with name "a"
 assertEquals(-1, AttributeModelComparator.compare(model1, model4));
 assertEquals(1, AttributeModelComparator.compare(model4, model1));

 // Test when o1 is null and o2 is not null
 assertNotEquals(0, AttributeModelComparator.compare(null, model1));

 // Test when o1 is not null and o2 is null
 assertNotEquals(0, AttributeModelComparator.compare(model1, null));

 // Test when o1 and o2 have the same name, but different values
 List<AttributeModel> list = new ArrayList<>();
 list.add(model1);
 list.add(model2);
 list.add(model3);
 Collections.sort(list, new AttributeModelComparator());
 assertEquals(model1.getName(), list.get(0).getName());
 assertEquals(model2.getName(), list.get(1).getName());
 assertEquals(model3.getName(), list.get(2).getName());
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullObject1() {
 AttributeModelComparator.compare(null, model1);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullObject2() {
 AttributeModelComparator.compare(model1, null);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareBothObjectsNull() {
 AttributeModelComparator.compare(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullNameModel1() {
 model1 = null;
 AttributeModelComparator.compare(model1, model2);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareNullNameModel2() {
 model2 = null;
 AttributeModelComparator.compare(model1, model2);
 }

 @Test
 public void testCompareSameNameDifferentValues() {
 AttributeModel model5 = new AttributeModel("NameA", "ValueB");
 List<AttributeModel> list = new ArrayList<>();
 list.add(model1);
 list.add(model5);
 list.add(model4);
 Collections.sort(list, new AttributeModelComparator());
 assertEquals(model1.getName(), list.get(0).getName());
 assertEquals(model4.getName(), list.get(1).getName());
 assertEquals(model5.getName(), list.get(2).getName());
 }
}