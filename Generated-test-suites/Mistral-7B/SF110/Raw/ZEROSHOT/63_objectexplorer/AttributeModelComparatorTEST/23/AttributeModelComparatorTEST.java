package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

private AttributeModel model1;
private AttributeModel model2;
private AttributeModel model3;
private List<AttributeModel> list1;
private List<AttributeModel> list2;
private List<AttributeModel> list3;

@Before
public void setUp() {
 model1 = new AttributeModel("Attribute1", "Value1");
 model2 = new AttributeModel("Attribute2", "Value2");
 model3 = new AttributeModel("Attribute3", "Value3");
 list1 = new ArrayList<>();
 list1.add(model1);
 list1.add(model2);
 list1.add(model3);
 list2 = new ArrayList<>(Collections.singletonList(model1));
 list3 = new ArrayList<>();
}

@Test
public void testNullComparator() {
 Comparator<AttributeModel> comparator = null;
 List<AttributeModel> list = new ArrayList<>();
 list.add(model1);
 list.add(model2);

 try {
 Collections.sort(list, comparator);
 } catch (NullPointerException e) {
 assertTrue(true);
 }
}

@Test
public void testNullAttributeModel1() {
 Comparator<AttributeModel> comparator = new AttributeModelComparator();
 List<AttributeModel> list = new ArrayList<>();
 list.add(null);
 list.add(model2);

 try {
 Collections.sort(list, comparator);
 } catch (NullPointerException e) {
 assertTrue(true);
 }
}

@Test
public void testNullAttributeModel2() {
 Comparator<AttributeModel> comparator = new AttributeModelComparator();
 List<AttributeModel> list = new ArrayList<>();
 list.add(model1);
 list.add(null);

 try {
 Collections.sort(list, comparator);
 } catch (NullPointerException e) {
 assertTrue(true);
 }
}

@Test
public void testCompareSameObjects() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model1, model1);
 assertTrue(comparisonResult == 0);
}

@Test
public void testCompareDifferentObjects() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model1, model2);
 int expectedComparisonResult = model1.getName().compareToIgnoreCase(model2.getName());
 assertTrue(comparisonResult == expectedComparisonResult);
}

@Test
public void testCompareAFirst() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model1, model3);
 assertTrue(comparisonResult < 0);
}

@Test
public void testCompareASecond() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model3, model1);
 assertTrue(comparisonResult > 0);
}

@Test
public void testCompareAAfter() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model2, model1);
 int expectedComparisonResult = model1.getName().compareToIgnoreCase(model2.getName());
 assertTrue(comparisonResult == expectedComparisonResult);
}

@Test
public void testCompareAAfterANotA() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model2, model3);
 assertTrue(comparisonResult < 0);
}

@Test
public void testCompareANotAAfter() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 int comparisonResult = comparator.compare(model3, model2);
 assertTrue(comparisonResult > 0);
}

@Test
public void testSortListWithOneItem() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 List<AttributeModel> list = list2;
 Collections.sort(list, comparator);
 AttributeModel expectedAttributeModel = model1;
 AttributeModel actualAttributeModel = list.get(0);
 assertNotSame(null, actualAttributeModel);
 assertTrue(expectedAttributeModel.equals(actualAttributeModel));
}

@Test
public void testSortListWithTwoItems() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 List<AttributeModel> list = list1;
 Collections.sort(list, comparator);
 AttributeModel expectedAttributeModel1 = model1;
 AttributeModel actualAttributeModel1 = list.get(0);
 AttributeModel expectedAttributeModel2 = model2;
 AttributeModel actualAttributeModel2 = list.get(1);
 AttributeModel expectedAttributeModel3 = model3;
 AttributeModel actualAttributeModel3 = list.get(2);

 assertNotSame(null, actualAttributeModel1);
 assertTrue(expectedAttributeModel1.equals(actualAttributeModel1));
 assertNotSame(null, actualAttributeModel2);
 assertTrue(expectedAttributeModel2.equals(actualAttributeModel2));
 assertNotSame(null, actualAttributeModel3);
 assertTrue(expectedAttributeModel3.equals(actualAttributeModel3));

 assertTrue(expectedAttributeModel1.getName().compareToIgnoreCase(expectedAttributeModel2.getName()) < 0);
 assertTrue(expectedAttributeModel1.getName().compareToIgnoreCase("a") < 0);
 assertTrue(expectedAttributeModel2.getName().compareToIgnoreCase("a") >= 0);

 assertTrue(comparator.compare(actualAttributeModel1, actualAttributeModel2) > 0);
 assertTrue(comparator.compare(actualAttributeModel2, actualAttributeModel1) < 0);
}

@Test
public void testSortEmptyList() {
 AttributeModelComparator comparator = new AttributeModelComparator();
 List<AttributeModel> list = list3;
 Collections.sort(list, comparator);
 assertNull(list.get(0));
}

}