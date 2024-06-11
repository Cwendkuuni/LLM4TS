package de.paragon.explorer.model;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class AttributeModelComparatorTest {

 @Test
 public void testCompare_SameName_DifferentModels() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = new AttributeModel("name1", "value2");
 int result = new AttributeModelComparator().compare(model1, model2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_DifferentNames_SameModel() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = new AttributeModel("name2", "value1");
 int result = new AttributeModelComparator().compare(model1, model2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_SameName_SameModel() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = new AttributeModel("name1", "value1");
 int result = new AttributeModelComparator().compare(model1, model2);
 assertEquals(0, result);
 }

 @Test
 public void testCompare_SameName_FirstModelBeforeA() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = new AttributeModel("a", "value1");
 int result = new AttributeModelComparator().compare(model1, model2);
 assertEquals(-1, result);
 }

 @Test
 public void testCompare_SameName_FirstModelAfterA() {
 AttributeModel model1 = new AttributeModel("a", "value1");
 AttributeModel model2 = new AttributeModel("name1", "value1");
 int result = new AttributeModelComparator().compare(model1, model2);
 assertEquals(1, result);
 }

 @Test
 public void testCompare_NullModels() {
 AttributeModel model1 = null;
 AttributeModel model2 = new AttributeModel("name", "value");
 int result = new AttributeModelComparator().compare(model1, model2);
 assertNotEquals(0, result);
 }

 @Test
 public void testCompare_NullModel1() {
 AttributeModel model1 = null;
 AttributeModel model2 = new AttributeModel("name", "value");
 int result = new AttributeModelComparator().compare(model1, model2);
 assertNotEquals(0, result);
 }

 @Test
 public void testCompare_NullModel2() {
 AttributeModel model1 = new AttributeModel("name", "value");
 AttributeModel model2 = null;
 int result = new AttributeModelComparator().compare(model1, model2);
 assertEquals(1, result);
 }

 @Test
 public void testCompare_EmptyList() {
 List<AttributeModel> list = Collections.emptyList();
 int result = new AttributeModelComparator().compare(new AttributeModel("name1", "value1"), new AttributeModel("name2", "value2"));
 assertNotEquals(0, result);
 }

 @Test
 public void testCompare_ListWithOneElement() {
 List<AttributeModel> list = Arrays.asList(new AttributeModel("name1", "value1"));
 int result = new AttributeModelComparator().compare(new AttributeModel("name1", "value1"), new AttributeModel("name2", "value2"));
 assertNotEquals(0, result);
 }

 @Test
 public void testCompare_ListWithTwoElements() {
 List<AttributeModel> list = Arrays.asList(new AttributeModel("name1", "value1"), new AttributeModel("name2", "value2"));
 int result = new AttributeModelComparator().compare(list.get(0), list.get(1));
 assertNotEquals(0, result);
 }

 @Test
 public void testCompare_ListWithTwoIdenticalElements() {
 List<AttributeModel> list = Arrays.asList(new AttributeModel("name1", "value1"), new AttributeModel("name1", "value1"));
 int result = new AttributeModelComparator().compare(list.get(0), list.get(1));
 assertEquals(0, result);
 }

 @Test
 public void testCompare_ListWithTwoIdenticalElementsInFirstPosition() {
 List<AttributeModel> list = Arrays.asList(new AttributeModel("a", "value1"), new AttributeModel("name1", "value1"));
 int result = new AttributeModelComparator().compare(list.get(0), list.get(1));
 assertEquals(1, result);
 }

 @Test
 public void testCompare_ListWithTwoIdenticalElementsInSecondPosition() {
 List<AttributeModel> list = Arrays.asList(new AttributeModel("name1", "value1"), new AttributeModel("a", "value1"));
 int result = new AttributeModelComparator().compare(list.get(0), list.get(1));
 assertEquals(-1, result);
 }
}