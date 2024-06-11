package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisWithEmptyFilter() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"));
 String expectedHexString = "0*0-0";
 String actualHexString = exceptionDiagnosis.asHexString();
 assertEquals(expectedHexString, actualHexString);
 }

 @Test
 public void testExceptionDiagnosisWithClassNameFilter() {
 String className = "twitter4j.statuses.Status";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{className});
 StackTraceElement[] stackTraceElements = new TwitterException("Test Exception").getStackTrace();
 String expectedHexString = "";
 for (StackTraceElement stackTraceElement : stackTraceElements) {
 if (stackTraceElement.getClassName().startsWith(className)) {
 expectedHexString += toHexString(stackTraceElement.getClassName().hashCode()) + stackTraceElement.getMethodName().hashCode();
 break;
 }
 }
 expectedHexString += "-" + toHexString(stackTraceElements[0].getLineNumber());
 String actualHexString = exceptionDiagnosis.asHexString();
 assertEquals(expectedHexString, actualHexString);
 }

 @Test (expected = AssertionError.class)
 public void testExceptionDiagnosisWithNonMatchingClassNameFilter() {
 String className = "java.util.ArrayList";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[]{"twitter4j.statuses.Status"});
 }

 @Test
 public void testExceptionDiagnosisWithRecursiveException() {
 TwitterException twitterException = new TwitterException("Test Recursive Exception");
 twitterException.initCause(new IOException("Test IOException"));
 IOException ioException = (IOException) twitterException.getCause();
 ioException.initCause(new TwitterException("Test Exception in IOException"));
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twitterException);
 String expectedHexString = "0*0-16167726322342802998" + // twitter exception
 " 0*192832765928322402093-1517908558" + // io exception
 " 0*13026583127399363765956-33975977"; // twitter exception in io exception
 String actualHexString = exceptionDiagnosis.asHexString();
 assertEquals(expectedHexString, actualHexString);
 }

 private String toHexString(int value) {
 return "0000000" + Integer.toHexString(value).substring(6);
 }
}