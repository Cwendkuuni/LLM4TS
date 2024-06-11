package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private List<AttributeModel> createAttributeModelsList() {
 return new ArrayList<>(Arrays.asList(
 new AttributeModel("model1"),
 new AttributeModel("model2"),
 new AttributeModel("model3"),
 new AttributeModel("modelA"),
 new AttributeModel("modelA1")
 ));
 }

 @Test
 public void testCompare_WhenBothObjectsAreNull_ThenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 assertSame(0, comparator.compare(null, null));
 }

 @Test
 public void testCompare_WhenBothObjectsAreSame_ThenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = new AttributeModel("model1");
 assertSame(0, comparator.compare(model1, model1));
 }

 @Test
 public void testCompare_WhenFirstObjectIsNull_ThenReturnNegativeOne() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model2 = new AttributeModel("model2");
 assertSame(-1, comparator.compare(null, model2));
 }

 @Test
 public void testCompare_WhenSecondObjectIsNull_ThenReturnPositiveOne() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = new AttributeModel("model1");
 assertSame(1, comparator.compare(model1, null));
 }

 @Test
 public void testCompare_WhenNamesAreEqual_ThenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = new AttributeModel("modelA");
 AttributeModel model2 = new AttributeModel("modelA");
 assertSame(0, comparator.compare(model1, model2));
 }

 @Test
 public void testCompare_WhenNamesAreDifferent_ThenReturnCorrectValue() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = new AttributeModel("model1");
 AttributeModel modelA = new AttributeModel("modelA");
 AttributeModel modelA1 = new AttributeModel("modelA1");
 AttributeModel model2 = new AttributeModel("model2");

 List<AttributeModel> attributeModels = createAttributeModelsList();

 assertSame(-1, comparator.compare(modelA, model1));
 assertSame(1, comparator.compare(model1, modelA));
 assertSame(-1, comparator.compare(modelA, modelA1));
 assertSame(1, comparator.compare(modelA1, modelA));
 assertSame(-1, comparator.compare(model1, model2));
 assertSame(1, comparator.compare(model2, model1));
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_WhenFirstObjectIsNullAndSecondObjectIsNotNull_ThenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model2 = new AttributeModel("model2");
 assertThrows(NullPointerException.class, () -> comparator.compare(null, model2));
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_WhenSecondObjectIsNullAndFirstObjectIsNotNull_ThenThrowNullPointerException() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = new AttributeModel("model1");
 assertThrows(NullPointerException.class, () -> comparator.compare(model1, null));
 }

 @Test
 public void testCompare_WhenFirstObjectNameIsASmallLetterA_ThenReturnNegativeOneWhenFirstObjectIsSortedBeforeSecondObject() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = new AttributeModel("a");
 AttributeModel model2 = new AttributeModel("model2");
 assertSame(-1, comparator.compare(model1, model2));
 }

 @Test
 public void testCompare_WhenFirstObjectNameIsASmallLetterA_ThenReturnPositiveOneWhenFirstObjectIsSortedAfterSecondObject() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model2 = new AttributeModel("model2");
 AttributeModel model1 = new AttributeModel("a");
 assertSame(1, comparator.compare(model1, model2));
 }

 @Test
 public void testCompare_WhenBothObjectsHaveNameASmallLetterA_ThenReturnComparingNames() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 AttributeModel model1 = new AttributeModel("a");
 AttributeModel model2 = new AttributeModel("a");
 assertSame(model1.getName().compareToIgnoreCase(model2.getName()), comparator.compare(model1, model2));
 }

 @Test
 public void testCompare_WhenListIsEmpty_ThenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 List<AttributeModel> emptyList = Collections.emptyList();
 assertSame(0, comparator.compare(emptyList.get(0), emptyList.get(0)));
 }

 @Test
 public void testCompare_WhenListContainsTwoObjectsWithSameName_ThenReturnZero() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("modelA"), new AttributeModel("modelA"));
 assertSame(0, comparator.compare(attributeModels.get(0), attributeModels.get(1)));
 }

 @Test
 public void testCompare_WhenListContainsTwoObjectsWithDifferentNames_ThenReturnCorrectValue() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 List<AttributeModel> attributeModels = Arrays.asList(new AttributeModel("model1"), new AttributeModel("model2"));
 assertSame(-1, comparator.compare(attributeModels.get(0), attributeModels.get(1)));
 }
}