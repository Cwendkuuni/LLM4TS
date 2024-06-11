package de.paragon.explorer.model;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {

    private AttributeModel modelA;
    private AttributeModel modelB;
    private AttributeModel modelC;

    @Before
    public void setUp() {
        modelA = new AttributeModel("AttributeA", "DescriptionA");
        modelB = new AttributeModel("AttributeB", "DescriptionB");
        modelC = new AttributeModel("a", "DescriptionC");
    }

    @Test
    public void testCompareNullAttributeModels() {
        assertNull(compare(null, null));
        assertNull(compare(null, modelA));
        assertNull(compare(modelA, null));
    }

    @Test
    public void testCompareEqualAttributeModels() {
        int value = compare(modelA, modelA);
        assertEquals(0, value);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithDifferentNames() {
        int value = compare(modelA, modelB);
        assertEquals(-modelA.getName().compareToIgnoreCase(modelB.getName()), value);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameFirst() {
        int value = compare(modelC, modelA);
        assertEquals(1, value);
    }

    @Test
    public void testCompareDifferentAttributeModelsWithSameNameSecond() {
        int value = compare(modelA, modelC);
        assertEquals(-1, value);
    }

    @Test
    public void testCompareNullAttributeModelWithValidOne() {
        assertNull(compare(null, modelA));
    }

    @Test
    public void testCompareValidAttributeModelsWithSameNameFirst() {
        AttributeModel modelD = new AttributeModel("a", "DescriptionD");
        int value = compare(modelD, modelA);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithSameNameSecond() {
        AttributeModel modelD = new AttributeModel("a", "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithDifferentNames() {
        AttributeModel modelD = new AttributeModel("AttributeD", "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(-modelA.getName().compareToIgnoreCase(modelD.getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithDifferentNamesAndAFirst() {
        AttributeModel modelD = new AttributeModel("AttributeD", "DescriptionD");
        int value = compare(modelD, modelA);
        assertEquals(1, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithDifferentNamesAndASecond() {
        AttributeModel modelD = new AttributeModel("AttributeD", "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(-1, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithSameNameAndDifferentCases() {
        AttributeModel modelD = new AttributeModel("a", "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithSameNameAndDifferentCasesFirst() {
        AttributeModel modelD = new AttributeModel("A", "DescriptionD");
        int value = compare(modelD, modelA);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithSameNameAndDifferentCasesSecond() {
        AttributeModel modelD = new AttributeModel("A", "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullDescriptionFirst() {
        AttributeModel modelD = new AttributeModel("AttributeD", null);
        int value = compare(modelD, modelA);
        assertEquals(-modelA.getName().compareToIgnoreCase(modelD.getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullDescriptionSecond() {
        AttributeModel modelD = new AttributeModel("AttributeD", null);
        int value = compare(modelA, modelD);
        assertEquals(modelA.getName().compareToIgnoreCase(modelD.getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameFirst() {
        AttributeModel modelD = new AttributeModel(null, "DescriptionD");
        int value = compare(modelD, modelA);
        assertNotSame(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameSecond() {
        AttributeModel modelD = new AttributeModel("AttributeD", null);
        int value = compare(modelA, modelD);
        assertNotSame(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameAndDescriptionFirst() {
        AttributeModel modelD = new AttributeModel(null, null);
        int value = compare(modelD, modelA);
        assertNotSame(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameAndDescriptionSecond() {
        AttributeModel modelD = new AttributeModel(null, null);
        int value = compare(modelA, modelD);
        assertNotSame(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameAndDifferentDescriptions() {
        AttributeModel modelD = new AttributeModel(null, "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(-modelA.getName().compareToIgnoreCase(modelD.getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullDescriptionAndDifferentNames() {
        AttributeModel modelD = new AttributeModel("AttributeD", null);
        int value = compare(modelA, modelD);
        assertEquals(-modelA.getName().compareToIgnoreCase(modelD.getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameAndDifferentDescriptionsAndSameNames() {
        AttributeModel modelD = new AttributeModel("a", "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameAndDifferentDescriptionsAndSameNamesFirst() {
        AttributeModel modelD = new AttributeModel("A", "DescriptionD");
        int value = compare(modelD, modelA);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullNameAndDifferentDescriptionsAndSameNamesSecond() {
        AttributeModel modelD = new AttributeModel("A", "DescriptionD");
        int value = compare(modelA, modelD);
        assertEquals(0, value);
    }

    @Test
    public void testCompareValidAttributeModelsWithEmptyList() {
        List<AttributeModel> emptyList = Collections.emptyList();
        AttributeModel modelD = new AttributeModel("AttributeD", "DescriptionD");
        int value = compare(modelD, emptyList.get(0));
        assertEquals(-modelA.getName().compareToIgnoreCase(modelD.getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithEmptyListFirst() {
        List<AttributeModel> emptyList = Collections.emptyList();
        int value = compare(emptyList.get(0), modelA);
        assertEquals(modelA.getName().compareToIgnoreCase(emptyList.get(0).getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithEmptyListSecond() {
        List<AttributeModel> emptyList = Arrays.asList(new AttributeModel("empty", "empty"), modelA);
        int value = compare(modelA, emptyList.get(1));
        assertEquals(-modelA.getName().compareToIgnoreCase(emptyList.get(1).getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithEmptyListThird() {
        List<AttributeModel> emptyList = Arrays.asList(new AttributeModel("empty", "empty"), modelA);
        int value = compare(emptyList.get(0), modelA);
        assertEquals(modelA.getName().compareToIgnoreCase(emptyList.get(0).getName()), value);
    }

    @Test
    public void testCompareValidAttributeModelsWithNullList() {
        int value = compare(modelA, null);
        assertNull(value);
    }

    private int compare(AttributeModel model1, AttributeModel model2) {
        return new AttributeModelComparator().compare(model1, model2);
    }
}