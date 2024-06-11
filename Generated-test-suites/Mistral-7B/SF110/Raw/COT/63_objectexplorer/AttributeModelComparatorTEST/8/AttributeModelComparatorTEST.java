package de.paragon.explorer.model.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    private AttributeModel modelA;
    private AttributeModel modelB;
    private AttributeModel modelC;
    private AttributeModel modelD;

    @Before
    public void setUp() {
        modelA = new AttributeModel("NameA", "ValueA");
        modelB = new AttributeModel("NameB", "ValueB");
        modelC = new AttributeModel("NameC", "ValueC");
        modelD = new AttributeModel("NameD", "ValueD");
    }

    // Testing compare method
    @Test
    public void testCompareMethod() {
        assertEquals(-1, new AttributeModelComparator().compare(modelB, modelA));
        assertEquals(0, new AttributeModelComparator().compare(modelA, modelA));
        assertEquals(1, new AttributeModelComparator().compare(modelA, modelB));
        assertEquals(-1, new AttributeModelComparator().compare(modelD, modelC));
        assertEquals(0, new AttributeModelComparator().compare(modelC, modelC));
        assertEquals(1, new AttributeModelComparator().compare(modelC, modelD));
    }

    // Testing compare method with null values
    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithNullValues() {
        new AttributeModelComparator().compare(null, modelB);
        new AttributeModelComparator().compare(modelA, null);
    }

    // Testing compare method with different object types
    @Test
    public void testCompareMethodWithDifferentObjectTypes() {
        assertNotSame(1, new AttributeModelComparator().compare(modelA, new Object()));
    }

    // Testing compare method with same object
    @Test
    public void testCompareMethodWithSameObject() {
        assertEquals(0, new AttributeModelComparator().compare(modelA, modelA));
    }

    // Testing compare method with empty names
    @Test
    public void testCompareMethodWithEmptyNames() {
        AttributeModel emptyModelA = new AttributeModel("", "");
        AttributeModel emptyModelB = new AttributeModel("", "");
        assertEquals(0, new AttributeModelComparator().compare(emptyModelA, emptyModelB));
    }

    // Testing compare method with equal names but different case
    @Test
    public void testCompareMethodWithEqualNamesButDifferentCase() {
        AttributeModel equalCaseModelA = new AttributeModel("NameA", "ValueA");
        AttributeModel notEqualCaseModelB = new AttributeModel("Namea", "ValueB");
        assertEquals(0, new AttributeModelComparator().compare(equalCaseModelA, notEqualCaseModelB));
    }

    // Testing compare method with equal names and "a"
    @Test
    public void testCompareMethodWithEqualNamesAndASpecialCase() {
        AttributeModel equalCaseModelA = new AttributeModel("NameA", "ValueA");
        AttributeModel modelE = new AttributeModel("NameA", "ValueE");
        AttributeModel modelF = new AttributeModel("a", "ValueF");
        assertEquals(0, new AttributeModelComparator().compare(equalCaseModelA, modelE));
        assertEquals(-1, new AttributeModelComparator().compare(modelE, modelF));
        assertEquals(1, new AttributeModelComparator().compare(modelF, modelE));
        assertEquals(0, new AttributeModelComparator().compare(equalCaseModelA, modelF));
    }

    // Testing compare method with empty list
    @Test
    public void testCompareMethodWithEmptyList() {
        List<AttributeModel> listA = new ArrayList<>();
        List<AttributeModel> listB = Arrays.asList(modelB);
        assertFalse(new AttributeModelComparator().compare(listA, listB) >= 0);
    }

    // Testing compare method with null list
    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithNullList() {
        new AttributeModelComparator().compare(null, listB);
    }

    // Testing compare method with empty list and null value
    @Test(expected = NullPointerException.class)
    public void testCompareMethodWithEmptyListAndNullValue() {
        List<AttributeModel> listA = new ArrayList<>();
        AttributeModel nullValue = null;
        new AttributeModelComparator().compare(listA, Arrays.asList(nullValue));
    }

    // Testing serializable method
    @Test
    public void testSerializableMethod() throws IOException, ClassNotFoundException {
        AttributeModelComparator comparator = new AttributeModelComparator();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(comparator);
        ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        AttributeModelComparator deserializedComparator = (AttributeModelComparator) objectInput.readObject();
        assertEquals(comparator, deserializedComparator);
    }

    // Testing equals method
    @Test
    public void testEqualsMethod() {
        AttributeModelComparator comparator1 = new AttributeModelComparator();
        AttributeModelComparator comparator2 = new AttributeModelComparator();
        assertEquals(comparator1, comparator2);
    }

    // Testing hashCode method
    @Test
    public void testHashCodeMethod() {
        AttributeModelComparator comparator1 = new AttributeModelComparator();
        AttributeModelComparator comparator2 = new AttributeModelComparator();
        assertEquals(comparator1.hashCode(), comparator2.hashCode());
    }
}