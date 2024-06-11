package de.paragon.explorer.model.test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class AttributeModelComparatorTest {

    private AttributeModel comparatorUnderTest;

    @Before
    public void setUp() {
        comparatorUnderTest = new AttributeModelComparator();
    }

    @Test
    public void testCompareWithNullAttributeModels() {
        // Given: null AttributeModel 1
        AttributeModel attributeModel1 = null;

        // Given: null AttributeModel 2
        AttributeModel attributeModel2 = null;

        // When: compare method is called
        int comparisonResult = comparatorUnderTest.compare(attributeModel1, attributeModel2);

        // Then: comparison result should be equal to 0
        assertThat(comparisonResult, is(0));
    }

    @Test
    public void testCompareWithSameAttributeModels() {
        // Given: AttributeModel 1 with the same name as AttributeModel 2
        String attributeName = "testName";
        AttributeModel attributeModel1 = new AttributeModel(attributeName);
        AttributeModel attributeModel2 = new AttributeModel(attributeName);

        // When: compare method is called
        int comparisonResult = comparatorUnderTest.compare(attributeModel1, attributeModel2);

        // Then: comparison result should be equal to 0
        assertThat(comparisonResult, is(0));
    }

    @Test
    public void testCompareWithDifferentAttributeModelsWithSameNameIgnored() {
        // Given: AttributeModel 1 with name "testName1"
        String attributeName1 = "testName1";
        AttributeModel attributeModel1 = new AttributeModel(attributeName1);

        // Given: AttributeModel 2 with name "testName2"
        String attributeName2 = "testName2";
        AttributeModel attributeModel2 = new AttributeModel(attributeName2);

        // Given: AttributeModel 3 with name "a"
        String nameA = "a";
        AttributeModel attributeModel3 = new AttributeModel(nameA);

        // When: compare method is called with AttributeModel 1 and AttributeModel 2
        int comparisonResult12 = comparatorUnderTest.compare(attributeModel1, attributeModel2);

        // Then: comparison result should be less than 0 if AttributeModel 1 name comes before AttributeModel 2 name in alphabetical order, ignoring the case
        assertThat(comparisonResult12, is(comparisonResultLessThan(attributeName1, attributeName2)));

        // When: compare method is called with AttributeModel 1 and AttributeModel 3
        int comparisonResult13 = comparatorUnderTest.compare(attributeModel1, attributeModel3);

        // Then: comparison result should be greater than 0 if AttributeModel 3 name comes before AttributeModel 1 name in alphabetical order, ignoring the case
        assertThat(comparisonResult13, is(comparisonResultGreaterThan(attributeName1, nameA)));

        // When: compare method is called with AttributeModel 2 and AttributeModel 3
        int comparisonResult23 = comparatorUnderTest.compare(attributeModel2, attributeModel3);

        // Then: comparison result should be less than 0 if AttributeModel 2 name comes before AttributeModel 3 name in alphabetical order, ignoring the case
        assertThat(comparisonResult23, is(comparisonResultLessThan(attributeName2, nameA)));
    }

    @Test
    public void testCompareWithDifferentAttributeModelsWithSameNameAndAIsFirst() {
        // Given: AttributeModel 1 with name "testName1"
        String attributeName1 = "testName1";
        AttributeModel attributeModel1 = new AttributeModel(attributeName1);

        // Given: AttributeModel 2 with name "testName2"
        String attributeName2 = "testName2";
        AttributeModel attributeModel2 = new AttributeModel(attributeName2);

        // Given: AttributeModel 3 with name "a"
        String nameA = "a";
        AttributeModel attributeModel3 = new AttributeModel(nameA);

        // Given: AttributeModel 4 with name "testName1"
        AttributeModel attributeModel4 = new AttributeModel(attributeName1);

        // When: compare method is called with AttributeModel 1 and AttributeModel 2
        int comparisonResult12 = comparatorUnderTest.compare(attributeModel1, attributeModel2);

        // Then: comparison result should be less than 0 if AttributeModel 1 name comes before AttributeModel 2 name in alphabetical order, ignoring the case
        assertThat(comparisonResult12, is(comparisonResultLessThan(attributeName1, attributeName2)));

        // When: compare method is called with AttributeModel 1 and AttributeModel 3
        int comparisonResult13 = comparatorUnderTest.compare(attributeModel1, attributeModel3);

        // Then: comparison result should be less than 0 if AttributeModel 1 name comes before "a" in alphabetical order, ignoring the case
        assertThat(comparisonResult13, is(comparisonResultLessThan(attributeName1, nameA)));

        // When: compare method is called with AttributeModel 1 and AttributeModel 4
        int comparisonResult14 = comparatorUnderTest.compare(attributeModel1, attributeModel4);

        // Then: comparison result should be equal to 0 if both names are the same
        assertThat(comparisonResult14, is(0));

        // When: compare method is called with AttributeModel 2 and AttributeModel 3
        int comparisonResult23 = comparatorUnderTest.compare(attributeModel2, attributeModel3);

        // Then: comparison result should be less than 0 if AttributeModel 2 name comes before "a" in alphabetical order, ignoring the case
        assertThat(comparisonResult23, is(comparisonResultLessThan(attributeName2, nameA)));

        // When: compare method is called with AttributeModel 2 and AttributeModel 4
        int comparisonResult24 = comparatorUnderTest.compare(attributeModel2, attributeModel4);

        // Then: comparison result should be equal to 0 if both names are the same
        assertThat(comparisonResult24, is(0));

        // When: compare method is called with AttributeModel 3 and AttributeModel 4
        int comparisonResult34 = comparatorUnderTest.compare(attributeModel3, attributeModel4);

        // Then: comparison result should be greater than 0 if "a" comes before AttributeModel 4 name in alphabetical order, ignoring the case
        assertThat(comparisonResult34, is(comparisonResultGreaterThan(nameA, attributeName1)));
    }

    @Test
    public void testCompareWithEmptyList() {
        // Given: empty list of AttributeModel objects
        List<AttributeModel> emptyAttributeModelList = new ArrayList<>();

        // When: compare method is called with two null AttributeModel objects
        int comparisonResultNullNull = comparatorUnderTest.compare(null, null);

        // Then: comparison result should be equal to 0
        assertThat(comparisonResultNullNull, is(0));

        // When: compare method is called with two different null AttributeModel objects
        AttributeModel nullAttributeModel1 = null;
        AttributeModel nullAttributeModel2 = null;
        int comparisonResultNullNull12 = comparatorUnderTest.compare(nullAttributeModel1, nullAttributeModel2);

        // Then: comparison result should be equal to 0
        assertThat(comparisonResultNullNull12, is(0));

        // When: compare method is called with null AttributeModel object and an empty list
        int comparisonResultNullEmpty = comparatorUnderTest.compare(null, emptyAttributeModelList.toArray(new AttributeModel[0]));

        // Then: comparison result should be equal to Integer.MIN_VALUE
        // Note: According to the JavaDoc, the compare method must return a negative integer, zero, or a positive integer.
        //       In this test case, we consider Integer.MIN_VALUE as negative, even though it is technically negative infinity.
        assertThat(comparisonResultNullEmpty, is(Integer.MIN_VALUE));

        // When: compare method is called with an empty list and a non-null AttributeModel object
        int comparisonResultEmptyNull = comparatorUnderTest.compare(emptyAttributeModelList.toArray(new AttributeModel[0]), new AttributeModel("testName"));

        // Then: comparison result should be equal to Integer.MIN_VALUE
        assertThat(comparisonResultEmptyNull, is(Integer.MIN_VALUE));

        // When: compare method is called with two AttributeModel objects in the empty list
        AttributeModel attributeModel1EmptyList = new AttributeModel("testName1");
        AttributeModel attributeModel2EmptyList = new AttributeModel("testName2");
        emptyAttributeModelList.add(attributeModel1EmptyList);
        emptyAttributeModelList.add(attributeModel2EmptyList);
        int comparisonResultEmptyEmpty = comparatorUnderTest.compare(attributeModel1EmptyList, attributeModel2EmptyList);

        // Then: comparison result should be equal to 0
        assertThat(comparisonResultEmptyEmpty, is(0));
    }

    @Test
    public void testCompareWithDifferentLists() {
        // Given: List 1 with two AttributeModel objects: "testName1" and "testName2"
        List<AttributeModel> list1 = Arrays.asList(new AttributeModel("testName1"), new AttributeModel("testName2"));

        // Given: List 2 with two AttributeModel objects: "testName3" and "testName4"
        List<AttributeModel> list2 = Arrays.asList(new AttributeModel("testName3"), new AttributeModel("testName4"));

        // When: compare method is called with List 1 and List 2
        int comparisonResult12 = comparatorUnderTest.compare(list1.toArray(new AttributeModel[0]), list2.toArray(new AttributeModel[0]));

        // Then: comparison result should be less than 0 if the first AttributeModel in List 1 comes before the first AttributeModel in List 2 in alphabetical order, ignoring the case
        int comparisonResultList1FirstVsList2First = comparisonResultLessThan(list1.get(0).getName(), list2.get(0).getName());
        assertThat(comparisonResult12, is(comparisonResultList1FirstVsList2First));

        // When: compare method is called with List 2 and List 1
        int comparisonResult21 = comparatorUnderTest.compare(list2.toArray(new AttributeModel[0]), list1.toArray(new AttributeModel[0]));

        // Then: comparison result should be greater than 0 if the first AttributeModel in List 2 comes before the first AttributeModel in List 1 in alphabetical order, ignoring the case
        int comparisonResultList2FirstVsList1First = comparisonResultGreaterThan(list2.get(0).getName(), list1.get(0).getName());
        assertThat(comparisonResult21, is(comparisonResultList2FirstVsList1First * -1));

        // Given: List 3 with two AttributeModel objects: "testName1" and "a"
        List<AttributeModel> list3 = Arrays.asList(new AttributeModel("testName1"), new AttributeModel("a"));

        // Given: List 4 with two AttributeModel objects: "a" and "testName2"
        List<AttributeModel> list4 = Arrays.asList(new AttributeModel("a"), new AttributeModel("testName2"));

        // When: compare method is called with List 3 and List 4
        int comparisonResult34 = comparatorUnderTest.compare(list3.toArray(new AttributeModel[0]), list4.toArray(new AttributeModel[0]));

        // Then: comparison result should be less than 0 if the first AttributeModel in List 3 comes before "a" in alphabetical order, ignoring the case
        int comparisonResultList3FirstVsList4A = comparisonResultLessThan(list3.get(0).getName(), "a");
        assertThat(comparisonResult34, is(comparisonResultList3FirstVsList4A));

        // When: compare method is called with List 4 and List 3
        int comparisonResult43 = comparatorUnderTest.compare(list4.toArray(new AttributeModel[0]), list3.toArray(new AttributeModel[0]));

        // Then: comparison result should be greater than 0 if "a" comes before the first AttributeModel in List 3 in alphabetical order, ignoring the case
        int comparisonResultList4AFirstVsList3First = comparisonResultGreaterThan("a", list3.get(0).getName());
        assertThat(comparisonResult43, is(comparisonResultList4AFirstVsList3First * -1));
    }

    private int comparisonResultLessThan(String string1, String string2) {
        return string1.compareToIgnoreCase(string2) < 0 ? -1 : 0;
    }

    private int comparisonResultGreaterThan(String string1, String string2) {
        return string1.compareToIgnoreCase(string2) > 0 ? 1 : 0;
    }
}