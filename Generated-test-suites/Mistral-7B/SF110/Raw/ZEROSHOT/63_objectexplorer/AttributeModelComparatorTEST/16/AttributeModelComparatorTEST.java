package de.paragon.explorer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

	@Test
	public void testCompareNullAttributeModels() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		Assert.assertNull(comparator.compare(null, null));
	}

	@Test
	public void testCompareSameAttributeModels() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		AttributeModel attributeModel1 = new AttributeModel("name1", "value1");
		AttributeModel attributeModel2 = new AttributeModel("name1", "value1");
		Assert.assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
	}

	@Test
	public void testCompareDifferentAttributeModelsWithSameNameIgnoreCase() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
		AttributeModel attributeModel2 = new AttributeModel("name1", "value2");
		Assert.assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
	}

	@Test
	public void testCompareDifferentAttributeModelsWithDifferentNamesIgnoreCase() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
		AttributeModel attributeModel2 = new AttributeModel("Name2", "value2");
		Assert.assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));
	}

	@Test
	public void testCompareDifferentAttributeModelsWithNameA() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
		AttributeModel attributeModel2 = new AttributeModel("a", "value2");
		Assert.assertEquals(1, comparator.compare(attributeModel1, attributeModel2));
	}

	@Test
	public void testCompareDifferentAttributeModelsWithNameADifferentOrder() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		AttributeModel attributeModel1 = new AttributeModel("a", "value1");
		AttributeModel attributeModel2 = new AttributeModel("Name1", "value2");
		Assert.assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));
	}

	@Test
	public void testCompareNullAttributeModelAndNotNull() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
		Assert.assertNull(comparator.compare(null, attributeModel1));
	}

	@Test
	public void testCompareNotNullAndNull() {
		AttributeModelComparator comparator = new AttributeModelComparator();
		AttributeModel attributeModel1 = new AttributeModel("Name1", "value1");
		Assert.assertNull(comparator.compare(attributeModel1, null));
	}

	@Test
	public void testSortList() {
		List<AttributeModel> attributeModels = Arrays.asList(
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name4", "value4")
		);
		Collections.sort(attributeModels, new AttributeModelComparator());
		List<AttributeModel> expectedAttributeModels = Arrays.asList(
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name4", "value4")
		);
		assertEquals(expectedAttributeModels, attributeModels);
	}

	@Test
	public void testSortListWithNulls() {
		List<AttributeModel> attributeModels = Arrays.asList(
				null,
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name4", "value4"),
				null
		);
		Collections.sort(attributeModels, new AttributeModelComparator());
		List<AttributeModel> expectedAttributeModels = Arrays.asList(
				null,
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name4", "value4"),
				null
		);
		assertEquals(expectedAttributeModels, attributeModels);
	}

	@Test
	public void testSortArray() {
		AttributeModel[] attributeModels = new AttributeModel[] {
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name4", "value4")
		};
		AttributeModel[] expectedAttributeModels = new AttributeModel[] {
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name4", "value4")
		};
		Arrays.sort(attributeModels, new AttributeModelComparator());
		assertArrayEquals(expectedAttributeModels, attributeModels);
	}

	@Test
	public void testSortArrayWithNulls() {
		AttributeModel[] attributeModels = new AttributeModel[] {
				null,
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name4", "value4"),
				null
		};
		AttributeModel[] expectedAttributeModels = new AttributeModel[] {
				null,
				new AttributeModel("a", "valueA"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name1", "value1"),
				new AttributeModel("Name2", "value2"),
				new AttributeModel("Name3", "value3"),
				new AttributeModel("Name4", "value4"),
				null
		};
		Arrays.sort(attributeModels, new AttributeModelComparator());
		assertArrayEquals(expectedAttributeModels, attributeModels);
	}
}