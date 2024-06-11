package de.paragon.explorer.model;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributeModels() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertNull( comparator.compare(null, null) );
    }

    @Test
    public void testCompareSameAttributeModels() {
        AttributeModel a1 = new AttributeModel("Name1", "Value1");
        AttributeModel a2 = new AttributeModel("Name1", "Value1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals( 0, comparator.compare(a1, a2) );
    }

    @Test
    public void testCompareDifferentAttributeModels() {
        AttributeModel a1 = new AttributeModel("Name1", "Value1");
        AttributeModel a2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue( result < 0 );
    }

    @Test
    public void testCompareAttributeModelWithNameA() {
        AttributeModel a1 = new AttributeModel("a", "Value1");
        AttributeModel a2 = new AttributeModel("Name2", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue( result < 0 );
    }

    @Test
    public void testCompareAttributeModelWithNameAAndNameB() {
        AttributeModel a1 = new AttributeModel("a", "Value1");
        AttributeModel a2 = new AttributeModel("a", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertFalse( result < 0 );
        assertNotSame( a1, a2 ); // Check that they are not the same object
        assertEquals( 0, result );
    }

    @Test
    public void testCompareAttributeModelWithNameBAndNameA() {
        AttributeModel a1 = new AttributeModel("Name2", "Value1");
        AttributeModel a2 = new AttributeModel("a", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue( result > 0 );
    }

    @Test
    public void testCompareAttributeModelWithNameC() {
        AttributeModel a1 = new AttributeModel("Name1", "Value1");
        AttributeModel a3 = new AttributeModel("NameC", "ValueC");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a3);
        assertTrue( result < 0 );
    }

    @Test
    public void testCompareAttributeModelsWithSameNameButDifferentValues() {
        AttributeModel a1 = new AttributeModel("Name1", "Value1");
        AttributeModel a2 = new AttributeModel("Name1", "Value2");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue( result < 0 );
    }

    @Test
    public void testCompareAttributeModelsWithSameNameButSameValues() {
        AttributeModel a1 = new AttributeModel("Name1", "Value1");
        AttributeModel a2 = new AttributeModel("Name1", "Value1");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals( 0, comparator.compare(a1, a2) );
    }

    @Test
    public void testCompareAttributeModelsSortedByName() {
        AttributeModel a1 = new AttributeModel("NameC", "ValueC");
        AttributeModel a2 = new AttributeModel("Name1", "Value1");
        AttributeModel a3 = new AttributeModel("NameA", "ValueA");
        AttributeModel a4 = new AttributeModel("a", "ValueA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        List<AttributeModel> models = new ArrayList<>(Arrays.asList(a1, a2, a3, a4));
        Collections.sort(models, comparator);
        assertEquals("NameA", models.get(0).getName());
        assertEquals("a", models.get(1).getName());
        assertEquals("Name1", models.get(2).getName());
        assertEquals("NameC", models.get(3).getName());
    }
}