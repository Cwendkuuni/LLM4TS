package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

 @Test
 public void testNewExceptionDiagnosis() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException());
 assertNotNull(exceptionDiagnosis.getStackLineHash());
 assertNotNull(exceptionDiagnosis.getLineNumberHash());
 assertNotNull(exceptionDiagnosis.getHexString());
 }

 @Test(expected = NullPointerException.class)
 public void testNewExceptionDiagnosisWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testNewExceptionDiagnosisWithEmptyFilter() {
 String[] inclusionFilter = new String[0];
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException(), inclusionFilter);
 assertNotNull(exceptionDiagnosis.getStackLineHash());
 assertNotNull(exceptionDiagnosis.getLineNumberHash());
 assertNotNull(exceptionDiagnosis.getHexString());
 }

 @Test
 public void testNewExceptionDiagnosisWithFilter() {
 String className = "com.example.MyClass";
 String methodName = "myMethod";
 String[] inclusionFilter = {className};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException(), inclusionFilter);
 StackTraceElement stackTraceElement = new ExceptionInInitializerError("test error").getStackTrace()[0];
 String expectedClassName = stackTraceElement.getClassName();
 String expectedMethodName = stackTraceElement.getMethodName();
 assertTrue(expectedClassName.startsWith(className) || expectedClassName.equals(className));
 assertTrue(expectedMethodName.equals(methodName) || expectedMethodName.startsWith(methodName));
 int expectedStackLineHash = 31 * expectedClassName.hashCode() ^ expectedMethodName.hashCode() ^ stackTraceElement.getLineNumber();
 assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
 int expectedLineNumberHash = stackTraceElement.getLineNumber();
 assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
 }

 @Test
 public void testExceptionDiagnosisEquality() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException());
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(new TwitterException(), new String[]{"com.example.MyClass"});
 ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(new TwitterException(), new String[]{"com.example.MyClass"});
 assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));
 }

 @Test
 public void testExceptionDiagnosisHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException(), new String[]{"com.example.MyClass"});
 assertNotSame(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException());
 String expected = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
 assertEquals(expected, exceptionDiagnosis.toString());
 }
}