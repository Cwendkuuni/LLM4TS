package de.paragon.explorer.model.test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenBothObjectsAreNotNull_thenComparisonsAreCorrect() {
        // given
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("Name2", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();

        // when
        int comparisonResult = comparator.compare(model1, model2);

        // then
        assertThat(comparator, is(notNullValue()));
        assertThat(comparisonResult, is(-1));
    }

    @Test
    public void testCompare_whenFirstObjectIsNull_thenReturnNegativeOne() {
        // given
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("Name2", "Value2");

        AttributeModelComparator comparator = new AttributeModelComparator();

        // when
        int comparisonResult = comparator.compare(model1, model2);

        // then
        assertThat(comparator, is(notNullValue()));
        assertThat(comparationResult, is(-1));
    }

    @Test
    public void testCompare_whenSecondObjectIsNull_thenReturnPositiveOne() {
        // given
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = null;

        AttributeModelComparator comparator = new AttributeModelComparator();

        // when
        int comparisonResult = comparator.compare(model1, model2);

        // then
        assertThat(comparator, is(notNullValue()));
        assertThat(comparationResult, is(1));
    }

    @Test
    public void testCompare_whenBothObjectsHaveTheSameName_thenReturnZero() {
        // given
        AttributeModel model1 = new AttributeModel("NameA", "ValueA");
        AttributeModel model2 = new AttributeModel("NameA", "ValueB");

        AttributeModelComparator comparator = new AttributeModelComparator();

        // when
        int comparisonResult = comparator.compare(model1, model2);

        // then
        assertThat(comparator, is(notNullValue()));
        assertThat(comparationResult, is(0));
    }

    @Test
    public void testCompare_whenFirstObjectHasNameLessThanAAndSecondObjectHasNameGreaterThanA_thenReturnNegativeOne() {
        // given
        AttributeModel model1 = new AttributeModel("Name1", "Value1");
        AttributeModel model2 = new AttributeModel("NameA", "ValueA");

        AttributeModelComparator comparator = new AttributeModelComparator();

        // when
        int comparisonResult = comparator.compare(model1, model2);

        // then
        assertThat(comparator, is(notNullValue()));
        assertThat(comparationResult, is(-1));
    }

    @Test
    public void testCompare_whenFirstObjectHasNameGreaterThanAAndSecondObjectHasNameLessThanA_thenReturnPositiveOne() {
        // given
        AttributeModel model1 = new AttributeModel("NameA", "ValueA");
        AttributeModel model2 = new AttributeModel("Name1", "Value1");

        AttributeModelComparator comparator = new AttributeModelComparator();

        // when
        int comparisonResult = comparator.compare(model1, model2);

        // then
        assertThat(comparator, is(notNullValue()));
        assertThat(comparationResult, is(1));
    }

    @Test
    public void testCompare_whenBothObjectsHaveNameEqualToA_thenReturnZero() {
        // given
        AttributeModel model1 = new AttributeModel("NameA", "ValueA");
        AttributeModel model2 = new AttributeModel("NameA", "ValueA");

        AttributeModelComparator comparator = new AttributeModelComparator();

        // when
        int comparisonResult = comparator.compare(model1, model2);

        // then
        assertThat(comparator, is(notNullValue()));
        assertThat(comparationResult, is(0));
    }

    @Test
    public void testComparatorSort_whenAttributeModelsAreSortedAscendingly_thenCorrectOrdering() {
        // given
        List<AttributeModel> attributeModels = Arrays.asList(
                new AttributeModel("Name3", "Value3"),
                new AttributeModel("Name1", "Value1"),
                new AttributeModel("NameA", "ValueA"),
                new AttributeModel("Name2", "Value2")
        );

        Collections.sort(attributeModels, new AttributeModelComparator());

        // then
        AttributeModel firstAttributeModel = attributeModels.get(0);
        AttributeModel lastAttributeModel = attributeModels.get(attributeModels.size() - 1);

        assertThat(firstAttributeModel.getName(), is("Name1"));
        assertThat(lastAttributeModel.getName(), is("Name3"));
    }
}