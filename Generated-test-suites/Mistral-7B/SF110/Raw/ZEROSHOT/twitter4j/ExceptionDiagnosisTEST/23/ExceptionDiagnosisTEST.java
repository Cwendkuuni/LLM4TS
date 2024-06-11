package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructionWithThrowable() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"));
 assertNotNull(exceptionDiagnosis.getStackLineHash());
 assertNotNull(exceptionDiagnosis.getLineNumberHash());
 assertNotNull(exceptionDiagnosis.asHexString());
 }

 @Test
 public void testExceptionDiagnosisConstructionWithThrowableAndFilter() {
 String filter = "twitter4j.";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[] { filter });
 StackTraceElement[] stackTrace = new TwitterException("Test Exception").getStackTrace();
 StackTraceElement filterElement = null;
 for (StackTraceElement element : stackTrace) {
 if (element.getClassName().startsWith(filter)) {
 filterElement = element;
 break;
 }
 }
 int expectedStackLineHash = filterElement.getClassName().hashCode() + filterElement.getMethodName().hashCode();
 int expectedLineNumberHash = filterElement.getLineNumber();
 ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(new TwitterException("Test Exception"), new String[] { filter });
 int actualStackLineHash = exceptionDiagnosisWithFilter.getStackLineHash();
 int actualLineNumberHash = exceptionDiagnosisWithFilter.getLineNumberHash();
 String actualHexString = exceptionDiagnosisWithFilter.asHexString();
 String[] hexs = actualHexString.split("-");
 assertEquals(String.format("%08x", expectedStackLineHash), hexs[0]);
 assertEquals(String.format("%08x", expectedLineNumberHash), hexs[1]);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructionWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testGetStackLineHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"));
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 StackTraceElement[] stackTrace = new TwitterException("Test Exception").getStackTrace();
 StackTraceElement filterElement = null;
 for (StackTraceElement element : stackTrace) {
 if (filterElement == null) {
 filterElement = element;
 }
 }
 int expectedStackLineHash = filterElement.getClassName().hashCode() + filterElement.getMethodName().hashCode();
 assertEquals(expectedStackLineHash, stackLineHash);
 }

 @Test
 public void testGetLineNumberHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"));
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 StackTraceElement[] stackTrace = new TwitterException("Test Exception").getStackTrace();
 StackTraceElement filterElement = null;
 for (StackTraceElement element : stackTrace) {
 if (filterElement == null) {
 filterElement = element;
 }
 }
 int expectedLineNumberHash = filterElement.getLineNumber();
 assertEquals(expectedLineNumberHash, lineNumberHash);
 }

 @Test
 public void testAsHexString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"));
 String hexString = exceptionDiagnosis.asHexString();
 assertNotNull(hexString);
 String[] hexs = hexString.split("-");
 int stackLineHash = Integer.parseInt(hexs[0], 16);
 int lineNumberHash = Integer.parseInt(hexs[1], 16);
 StackTraceElement[] stackTrace = new TwitterException("Test Exception").getStackTrace();
 StackTraceElement filterElement = null;
 for (StackTraceElement element : stackTrace) {
 if (filterElement == null) {
 filterElement = element;
 }
 }
 int expectedStackLineHash = filterElement.getClassName().hashCode() + filterElement.getMethodName().hashCode();
 int expectedLineNumberHash = filterElement.getLineNumber();
 assertEquals(String.format("%08x", expectedStackLineHash), hexs[0]);
 assertEquals(String.format("%08x", expectedLineNumberHash), hexs[1]);
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test Exception1"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test Exception2"));
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(new TwitterException("Test Exception1"));
 assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test Exception1"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test Exception2"));
 int exceptionDiagnosis1HashCode = exceptionDiagnosis1.hashCode();
 int exceptionDiagnosis2HashCode = exceptionDiagnosis2.hashCode();
 assertNotEquals(exceptionDiagnosis1HashCode, exceptionDiagnosis2HashCode);
 ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(new TwitterException("Test Exception1"));
 assertEquals(exceptionDiagnosis1HashCode, exceptionDiagnosis3.hashCode());
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test Exception"));
 String toString = exceptionDiagnosis.toString();
 String expectedString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
 assertEquals(expectedString, toString);
 }

}