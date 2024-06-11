package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 private AttributeModelComparator comparator;

 @Before
 public void setup() {
 comparator = new AttributeModelComparator();
 }

 @Test
 public void testCompare_whenBothObjectsAreNotNullAndHaveDifferentNames_shouldReturnTheCorrectValue() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = new AttributeModel("name2", "value2");

 int comparisonResult = comparator.compare(model1, model2);

 assertThat(comparisonResult, is(-1));
 }

 @Test
 public void testCompare_whenBothObjectsAreNotNullAndHaveTheSameName_shouldReturnZero() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = new AttributeModel("name1", "value2");

 int comparisonResult = comparator.compare(model1, model2);

 assertThat(comparationResult, is(0));
 }

 @Test
 public void testCompare_whenBothObjectsAreNotNullAndOneHasNameA_shouldReturnTheCorrectValue() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = new AttributeModel("a", "value2");

 int comparisonResult = comparator.compare(model1, model2);

 assertThat(comparisonResult, is(1));
 }

 @Test
 public void testCompare_whenBothObjectsAreNotNullAndOneHasNameAAndTheOtherHasNameAasWell_shouldReturnZero() {
 AttributeModel model1 = new AttributeModel("a", "value1");
 AttributeModel model2 = new AttributeModel("a", "value2");

 int comparisonResult = comparator.compare(model1, model2);

 assertThat(comparisonResult, is(0));
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenFirstObjectIsNull_shouldThrowNullPointerException() {
 AttributeModel model1 = null;
 AttributeModel model2 = new AttributeModel("name2", "value2");

 comparator.compare(model1, model2);
 }

 @Test(expected = NullPointerException.class)
 public void testCompare_whenSecondObjectIsNull_shouldThrowNullPointerException() {
 AttributeModel model1 = new AttributeModel("name1", "value1");
 AttributeModel model2 = null;

 comparator.compare(model1, model2);
 }

 @Test
 public void testCompare_whenBothObjectsAreNull_shouldReturnZero() {
 AttributeModel model1 = null;
 AttributeModel model2 = null;

 int comparisonResult = comparator.compare(model1, model2);

 assertThat(comparisonResult, is(0));
 }

 @Test
 public void testCompare_whenListOfAttributeModelsIsSortedUsingComparator_shouldReturnCorrectOrdering() {
 List<AttributeModel> attributeModels = new ArrayList<>();
 attributeModels.add(new AttributeModel("name3", "value3"));
 attributeModels.add(new AttributeModel("name2", "value2"));
 attributeModels.add(new AttributeModel("name1", "value1"));
 attributeModels.add(new AttributeModel("a", "valueA"));
 attributeModels.add(new AttributeModel("name4", "value4"));
 attributeModels.add(new AttributeModel("name3", "value3"));
 attributeModels.add(new AttributeModel("a", "valueB"));

 Collections.sort(attributeModels, comparator);
 List<String> names = new ArrayList<>();
 for (AttributeModel attributeModel : attributeModels) {
 names.add(attributeModel.getName());
 }
 List<String> expectedOrder = Arrays.asList("a", "a", "name1", "name2", "name3", "name3", "name4");

 assertThat(names, is(expectedOrder));
 }
}