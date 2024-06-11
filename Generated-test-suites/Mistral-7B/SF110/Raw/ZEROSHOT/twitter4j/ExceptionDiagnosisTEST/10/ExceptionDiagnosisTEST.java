package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructorWithThrowable() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Error message"));
 assertTrue(exceptionDiagnosis instanceof ExceptionDiagnosis);
 assertNotNull(exceptionDiagnosis.getStackLineHash());
 assertNotNull(exceptionDiagnosis.getLineNumberHash());
 assertNotNull(exceptionDiagnosis.getHexString());
 }

 @Test
 public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
 String filter = "twitter4j.";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Error message"), new String[]{filter});
 assertTrue(exceptionDiagnosis instanceof ExceptionDiagnosis);
 assertNotNull(exceptionDiagnosis.getStackLineHash());
 assertNotNull(exceptionDiagnosis.getLineNumberHash());
 assertNotNull(exceptionDiagnosis.getHexString());
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructorWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testGetStackLineHash() {
 Throwable throwable = new Throwable();
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
 Throwable throwable1 = new Throwable();
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable1);
 assertNotSame(exceptionDiagnosis1.getStackLineHash(), exceptionDiagnosis2.getStackLineHash());
 }

 @Test
 public void testGetLineNumberHash() {
 Throwable throwable = new Throwable();
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
 Throwable throwable1 = new Throwable();
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable1);
 assertNotSame(exceptionDiagnosis1.getLineNumberHash(), exceptionDiagnosis2.getLineNumberHash());
 }

 @Test
 public void testAsHexString() {
 Throwable throwable = new Throwable();
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
 String hexString = exceptionDiagnosis.asHexString();
 assertNotNull(hexString);
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable());
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable());
 assertNotSame(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
 String toString = exceptionDiagnosis.toString();
 assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
 assertTrue(toString.contains("lineNumberHash="));
 }

 @Test
 public void testGetCauseWithExceptionDiagnosis() {
 Throwable throwable = new Throwable();
 Throwable cause = new Throwable();
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable, new String[]{"twitter4j."});
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(cause, new String[]{"twitter4j."});
 String hexString = exceptionDiagnosis1.asHexString();
 String hexString1 = exceptionDiagnosis2.asHexString();
 String[] hexStrings = hexString.split(" ");
 String[] hexStrings1 = hexString1.split(" ");
 assertEquals(hexStrings[hexStrings.length - 1], hexStrings1[hexStrings1.length - 1]);
 }
}