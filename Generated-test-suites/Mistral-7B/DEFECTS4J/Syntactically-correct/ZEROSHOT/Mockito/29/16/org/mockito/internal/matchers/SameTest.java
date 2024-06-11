package org.mockito.internal.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

 private Object stringObject;
 private Object sameStringObject;
 private Object differentObject;
 private Object arrayOfStrings;
 private Object arrayOfSameStrings;

 @Before
 public void setUp() {
 stringObject = "Mockito is a testing framework for mocking.";
 sameStringObject = stringObject;
 differentObject = new Object();
 arrayOfStrings = new String[]{"Mockito", "is", "a", "testing", "framework", "for", "mocking"};
 arrayOfSameStrings = arrayOfStrings.clone();
 }

 @Test
 public void testMatchesSameObject() {
 Object testObject = sameStringObject;
 Matcher<Object> sameMatcher = new Same(testObject);
 Object testObject2 = testObject;

 assertThat(sameMatcher.matches(testObject2), is(true));
 }

 @Test
 public void testMatchesDifferentObjects() {
 assertThat(new Same(stringObject).matches(differentObject), is(false));
 }

 @Test
 public void testMatchesDifferentTypes() {
 Object charObject = 'a';
 Matcher<Object> sameCharMatcher = new Same(charObject);
 Object intObject = 1;

 assertThat(sameCharMatcher.matches(intObject), is(false));
 }

 @Test
 public void testMatchesDifferentArrayWithSameObjects() {
 Matcher<Object> sameArrayMatcher = new Same(arrayOfStrings);
 Object clonedArray = arrayOfSameStrings.clone();

 assertThat(sameArrayMatcher.matches(clonedArray), is(true));
 }

 @Test
 public void testMatchesDifferentArrays() {
 Object[] differentArray = arrayOfStrings.clone();
 differentArray[0] = "JUnit";

 Matcher<Object> sameArrayMatcher = new Same(arrayOfStrings);

 assertThat(sameArrayMatcher.matches(differentArray), is(false));
 }

 @Test
 public void testMatchesNullObjects() {
 Object nullObject = null;
 Matcher<Object> sameNullMatcher = new Same(nullObject);
 Object anotherNullObject = null;

 assertThat(sameNullMatcher.matches(anotherNullObject), is(true));
 }

 @Test(expected = NullPointerException.class)
 public void testMatchesNullWanted() {
 Matcher<Object> sameNullMatcher = new Same(null);
 }

 @Test(expected = NullPointerException.class)
 public void testDescribeToNullWanted() {
 Description description = new Description();
 new Same(null).describeTo(description);
 }
}