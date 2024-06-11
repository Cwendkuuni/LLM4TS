package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

 // Testing the diff method with basic input
 @Test
 public void testDiffBasic() {
 String a = "HelloWorld";
 String b = "HelloWorldHello";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(0, 0, 6));
 expectedResult.addLink(new StringLink(7, 7, 6));
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 // Testing the diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expectedResult = new RelativeString();
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 // Testing the diff method with null inputs
 @Test(expected = NullPointerException.class)
 public void testDiffNullInputs() {
 String a = null;
 String b = "HelloWorld";
 StringIncrementor.diff(a, b);
 }

 // Testing the diff method with large strings
 @Test
 public void testDiffLargeStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed commodo, magna quis bibendum rhoncus, sem quam semper nibh, ut commodo urna mauris eget nunc. Nullam id felis eu ipsum bibendum ultrices. Donec ullamcorper nulla non mauris varius commodo. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam erat volutpat. Ut posuere, magna sed commodo rhoncus, quam quis risus, ut bibendum odio quam quis nunc.";
 String b = a + " Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(a.length(), a.length(), a.length()));
 assertEquals(expectedResult, StringIncrementor.diff(a, b));
 }

 // Testing the diff method with verbose flag
 @Test
 public void testDiffVerbose() {
 String a = "HelloWorld";
 String b = "HelloWorldHello";
 StringIncrementor.diff(a, b, true);
 }

 // Testing the patch method with basic input
 @Test
 public void testPatchBasic() {
 String a = "HelloWorld";
 String b = "HelloWorldHello";
 RelativeString r = StringIncrementor.diff(b, a);
 String expectedResult = "HelloWorldHello";
 assertEquals(expectedResult, StringIncrementor.patch(a, r));
 }

 // Testing the patch method with null inputs
 @Test(expected = NullPointerException.class)
 public void testPatchNullInputs() {
 String a = null;
 RelativeString r = new RelativeString();
 StringIncrementor.patch(a, r);
 }

 // Testing the patch method with empty strings
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expectedResult = "";
 assertEquals(expectedResult, StringIncrementor.patch(a, r));
 }

 // Testing the patch method with large strings
 @Test
 public void testPatchLargeStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed commodo, magna quis bibendum rhoncus, sem quam semper nibh, ut commodo urna mauris eget nunc. Nullam id felis eu ipsum bibendum ultrices. Donec ullamcorper nulla non mauris varius commodo. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam erat volutpat. Ut posuere, magna sed commodo rhoncus, quam quis risus, ut bibendum odio quam quis nunc.";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(a.length(), a.length(), a.length()));
 String expectedResult = a;
 assertEquals(expectedResult, StringIncrementor.patch(a, r));
 }

}