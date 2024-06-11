package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameObject() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        Same sameObject = new Same(obj1);

        assertThat(sameObject, sameObject);
    }

    @Test
    public void testSameString() {
        String str1 = "foo";
        String str2 = "foo";

        Same sameString = new Same(str1);

        assertThat(sameString, sameString);
    }

    @Test
    public void testSameCharacter() {
        Character c1 = 'a';
        Character c2 = 'a';

        Same sameCharacter = new Same(c1);

        assertThat(sameCharacter, sameCharacter);
    }

    @Test
    public void testDifferentObjectsOfDifferentTypes() {
        Object obj1 = new Object();
        String str1 = "foo";

        Same sameObject = new Same(obj1);
        Same sameString = new Same(str1);

        assertThat(sameObject, is(not(sameString)));
    }

    @Test
    public void testDifferentObjectsOfSameType() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Same sameObject = new Same(obj1);

        assertThat(sameObject, is(not(sameObject.new Same(obj2))));
    }

    @Test
    public void testNullObject() {
        Object obj1 = null;
        Object obj2 = null;

        Same sameObject = new Same(obj1);

        assertThat(sameObject, sameObject);
    }

    @Test
    public void testNullAndOtherObject() {
        Object obj1 = new Object();
        Object obj2 = null;

        Same sameObject = new Same(obj1);

        assertThat(sameObject, is(not(sameObject.new Same(obj2))));
    }

    @Test
    public void testDescribeTo() {
        Object obj = new Object();
        Same sameObject = new Same(obj);

        Description description = new Description();
        sameObject.describeTo(description);

        String expectedDescription = "same(" + obj.toString() + ")";
        String actualDescription = description.toString();

        assertThat(expectedDescription, equalTo(actualDescription));
    }
}