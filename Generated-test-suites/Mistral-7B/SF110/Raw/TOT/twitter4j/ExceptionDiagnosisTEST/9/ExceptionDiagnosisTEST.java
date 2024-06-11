package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

 private static final String expectedClassName = "test.ExceptionDiagnosisTest";
 private static final String expectedMethodName = "testExceptionDiagnosisWithNormalStacktrace";
 private static final int expectedStackLineHash = 12345678;
 private static final int expectedLineNumberHash = 987654321;
 private static final String expectedHexString = "12345678-987654321";

 @Test
 public void testExceptionDiagnosisWithNormalStacktrace() {
 Throwable throwable = new IOException("Test IOException");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
 assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
 assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
 assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
 }

 @Test
 public void testExceptionDiagnosisWithEmptyInclusionFilter() {
 Throwable throwable = new IllegalArgumentException("Test IllegalArgumentException");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
 assertNull(exceptionDiagnosis.hexString);
 }

 @Test(expected = AssertionError.class)
 public void testExceptionDiagnosisWithNullThrowable() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
 fail("Expected NullPointerException, but got AssertionError");
 }

}