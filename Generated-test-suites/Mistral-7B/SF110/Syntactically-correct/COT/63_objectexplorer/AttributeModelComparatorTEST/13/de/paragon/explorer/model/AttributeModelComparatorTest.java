package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

 // Testing compare method
 @Test
 public void testCompare() {
 AttributeModel model1 = new AttributeModel("Attribute1", "Value1");
 AttributeModel model2 = new AttributeModel("Attribute2", "Value2");
 AttributeModel model3 = new AttributeModel("Attribute3", "Value3");
 AttributeModel modelA = new AttributeModel("a", "ValueA");
 AttributeModel modelNull = null;

 List<AttributeModel> attributeModels = new ArrayList<>(Arrays.asList(model1, model2, model3, modelA, modelNull));
 Collections.sort(attributeModels, new AttributeModelComparator());

 // Basic functionality testing
 AttributeModel expectedFirst = modelA;
 AttributeModel actualFirst = attributeModels.get(0);
 assertEquals(expectedFirst.getName(), actualFirst.getName());

 // Testing when both objects are null
 AttributeModel nullModel1 = null;
 AttributeModel nullModel2 = null;
 int comparisonResult = new AttributeModelComparator().compare(nullModel1, nullModel2);
 assertEquals(0, comparisonResult);

 // Testing when both objects have the same name
 AttributeModel modelSameName = new AttributeModel("Attribute1", "Value1");
 int comparisonResultSameName = new AttributeModelComparator().compare(model1, modelSameName);
 assertEquals(0, comparisonResultSameName);

 // Testing when the first object's name is less than the second object's name
 AttributeModel modelLessName = new AttributeModel("AttributeB", "ValueB");
 int comparisonResultLessName = new AttributeModelComparator().compare(modelLessName, model1);
 assertLessThan(0, comparisonResultLessName);

 // Testing when the first object's name is greater than the second object's name and both names are not "a"
 AttributeModel modelGreaterName1 = new AttributeModel("AttributeX", "ValueX");
 AttributeModel modelGreaterName2 = new AttributeModel("AttributeY", "ValueY");
 int comparisonResultGreaterName = new AttributeModelComparator().compare(modelGreaterName1, modelGreaterName2);
 assertGreaterThan(0, comparisonResultGreaterName);

 // Testing when the first object's name is greater than the second object's name and both names are "a"
 int comparisonResultGreaterNameA = new AttributeModelComparator().compare(modelA, model1);
 assertLessThan(0, comparisonResultGreaterNameA);

 // Testing when the first object's name is less than "a" and the second object's name is greater than "a"
 int comparisonResultLessThanAA = new AttributeModelComparator().compare(new AttributeModel("AttributeX", "ValueX"), modelA);
 assertGreaterThan(0, comparisonResultLessThanAA);
 }

 // Testing equals and hashCode methods (not shown due to space constraints)

}