package org.mockito.internal.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SameTest {

 private Object wanted;

 @Test
 public void testSameMatchesSameObject() {
 wanted = new Object();
 Object actual = new Object();

 assertThat(new Same(wanted), is(new Same(actual)));
 assertThat(new Same(wanted), is(new Same(wanted)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSameThrowsExceptionForNullWanted() {
 wanted = null;
 new Same(wanted);
 }

 @Test
 public void testSameMatchesNullWithNull() {
 wanted = null;
 Object actual = null;

 assertThat(new Same(wanted), is(new Same(actual)));
 }

 @Test
 public void testSameMatchesDifferentClasses() {
 wanted = new Object();
 Object actual = new String("test");

 assertThat(new Same(wanted), is(not(new Same(actual))));
 }

 @Test
 public void testSameMatchesDifferentInstancesOfSameClass() {
 wanted = new String("test");
 Object actual = new String("test");

 assertThat(new Same(wanted), is(not(new Same(actual))));
 }

 @Test
 public void testSameMatchesStringWithSameQuotedString() {
 wanted = "test";
 Object actual = "test";

 assertThat(new Same(wanted), is(new Same(actual)));
 }

 @Test
 public void testSameMatchesCharacterWithSameQuotedCharacter() {
 wanted = 'a';
 Object actual = 'a';

 assertThat(new Same(wanted), is(new Same(actual)));
 }

 @Test
 public void testSameMatchesStringWithDifferentQuotedString() {
 wanted = "test";
 Object actual = "tester";

 assertThat(new Same(wanted), is(not(new Same(actual))));
 }

 @Test
 public void testSameMatchesCharacterWithDifferentQuotedCharacter() {
 wanted = 'a';
 Object actual = 'b';

 assertThat(new Same(wanted), is(not(new Same(actual))));
 }

 @Test
 public void testDescribeToAddsQuotesToStrings() {
 Description description = new Description();
 Same same = new Same("test");
 same.describeTo(description);

 String expectedDescription = "same(\"test\")";
 String actualDescription = description.toString();

 assertThat(actualDescription, is(expectedDescription));
 }

 @Test
 public void testDescribeToAddsQuotesToCharacters() {
 Description description = new Description();
 Same same = new Same('a');
 same.describeTo(description);

 String expectedDescription = "same('a')";
 String actualDescription = description.toString();

 assertThat(actualDescription, is(expectedDescription));
 }

 @Test
 public void testDescribeToDoesNotAddQuotesToObjectsNotStringsOrCharacters() {
 Description description = new Description();
 Object wanted = new Object();
 Same same = new Same(wanted);
 same.describeTo(description);

 String expectedDescription = "same(" + wanted.getClass().getName() + ")";
 String actualDescription = description.toString();

 assertThat(actualDescription, is(expectedDescription));
 }
}