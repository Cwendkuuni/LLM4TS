package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructorWithThrowable() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"));
 Assert.assertNotNull(exceptionDiagnosis);
 }

 @Test
 public void testExceptionDiagnosisConstructorWithThrowableAndEmptyIncludeFilter() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[0]);
 Assert.assertNotNull(exceptionDiagnosis);
 }

 @Test
 public void testExceptionDiagnosisConstructorWithThrowableAndIncludeFilter() {
 String filter = "twitter4j";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 Assert.assertNotNull(exceptionDiagnosis);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testGetStackLineHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 Assert.assertNotNull(stackLineHash);
 }

 @Test
 public void testGetLineNumberHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 Assert.assertNotNull(lineNumberHash);
 }

 @Test
 public void testAsHexString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 String hexString = exceptionDiagnosis.asHexString();
 Assert.assertNotNull(hexString);
 }

 @Test
 public void testEqualsObject() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testNotEqualsObjectDifferentClasses() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 Object obj = new Object();
 assertFalse(exceptionDiagnosis.equals(obj));
 }

 @Test
 public void testNotEqualsObjectDifferentValues() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test Exception2"), new String[]{"twitter4j"});
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test Exception2"), new String[]{"twitter4j"});
 int hashCode1 = exceptionDiagnosis1.hashCode();
 int hashCode2 = exceptionDiagnosis2.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);

 ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j2"});
 int hashCode3 = exceptionDiagnosis3.hashCode();
 Assert.assertNotSame(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j"});
 String toString = exceptionDiagnosis.toString();
 Assert.assertNotNull(toString);
 }

 @Test
 public void testToStringWithCausedByException() {
 TwitterException twitterException = new TwitterException("Test Exception");
 twitterException.initCause(new IOException("Test IOException"));
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twitterException, new String[]{"twitter4j"});
 String toString = exceptionDiagnosis.toString();
 Assert.assertNotNull(toString);
 }
}