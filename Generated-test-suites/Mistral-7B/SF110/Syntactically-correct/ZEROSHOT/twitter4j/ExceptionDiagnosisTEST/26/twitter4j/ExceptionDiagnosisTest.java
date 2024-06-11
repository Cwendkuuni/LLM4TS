package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructor() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException());
 Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
 Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
 Assert.assertNotNull(exceptionDiagnosis.asHexString());
 Assert.assertNotNull(exceptionDiagnosis.th);
 }

 @Test
 public void testExceptionDiagnosisConstructorWithFilter() {
 String[] inclusionFilter = new String[]{"twitter4j"};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException(), inclusionFilter);
 Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
 Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
 Assert.assertNotNull(exceptionDiagnosis.asHexString());
 Assert.assertNotNull(exceptionDiagnosis.th);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructorWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testGetStackLineHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException());
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 Assert.assertNotSame(0, stackLineHash);
 }

 @Test
 public void testGetLineNumberHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException());
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 Assert.assertNotSame(0, lineNumberHash);
 }

 @Test
 public void testAsHexString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException());
 String hexString = exceptionDiagnosis.asHexString();
 Assert.assertNotNull(hexString);
 Assert.assertTrue(hexString.contains("-"));
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException());
 Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException());
 Assert.assertNotSame(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException());
 String toString = exceptionDiagnosis.toString();
 Assert.assertNotNull(toString);
 }

 @Test
 public void testToStringWithFilter() {
 String[] inclusionFilter = new String[]{"twitter4j"};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException(), inclusionFilter);
 String toString = exceptionDiagnosis.toString();
 Assert.assertNotNull(toString);
 Assert.assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
 Assert.assertTrue(toString.contains("lineNumberHash="));
 }

 @Test
 public void testStackTraceElementFiltering() {
 TwitterException twitterException = new TwitterException();
 StackTraceElement[] stackTrace = twitterException.getStackTrace();
 String[] inclusionFilter = new String[]{"twitter4j"};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twitterException, inclusionFilter);
 StackTraceElement stackTraceElement = stackTrace[stackTrace.length - 1];
 String className = stackTraceElement.getClassName();
 String methodName = stackTraceElement.getMethodName();
 Assert.assertTrue(className.startsWith(inclusionFilter[0]));
 Assert.assertTrue(methodName.contains(className));
 }

 @Test
 public void testCause() {
 TwitterException mainException = new TwitterException();
 TwitterException causeException = new TwitterException();
 mainException.initCause(causeException);
 String[] inclusionFilter = new String[]{"twitter4j"};
 ExceptionDiagnosis mainExceptionDiagnosis = new ExceptionDiagnosis(mainException, inclusionFilter);
 String hexString = mainExceptionDiagnosis.asHexString();
 String[] causeHexStrings = hexString.split(" ");
 Assert.assertNotNull(causeHexStrings);
 Assert.assertFalse(causeHexStrings.length == 0);
 ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(causeException, inclusionFilter);
 String causeHexString = causeExceptionDiagnosis.asHexString();
 Assert.assertEquals(causeHexStrings[0], causeHexString);
 }
}