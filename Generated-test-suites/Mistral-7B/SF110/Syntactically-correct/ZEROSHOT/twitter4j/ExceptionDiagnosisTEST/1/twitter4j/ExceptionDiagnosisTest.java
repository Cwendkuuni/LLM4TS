package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.ThrowableWithFilter;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructionWithThrowable() {
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new RuntimeException("Test Exception"));
 Assert.assertNotNull(ed);
 }

 @Test
 public void testExceptionDiagnosisConstructionWithThrowableAndFilters() {
 String filter = "twitter4j.test.";
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new RuntimeException("Test Exception"), new String[]{filter});
 Assert.assertNotNull(ed);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructionWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testGetStackLineHash() {
 Throwable e = new RuntimeException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(e);
 int stackLineHash = ed.getStackLineHash();
 StackTraceElement[] stackTrace = e.getStackTrace();
 StackTraceElement lastElement = stackTrace[stackTrace.length - 1];
 String lastClassName = lastElement.getClassName();
 String lastMethodName = lastElement.getMethodName();
 int expectedHash = lastClassName.hashCode() + lastMethodName.hashCode();
 int calculatedHash = 1;
 for (int i = stackTrace.length - 1; i >= 0; --i) {
 final StackTraceElement line = stackTrace[i];
 final String filter = "twitter4j.";
 if (line.getClassName().startsWith(filter)) {
 calculatedHash = 31 * calculatedHash + line.getClassName().hashCode() + line.getMethodName().hashCode() + line.getLineNumber();
 }
 }
 Assert.assertEquals(expectedHash, calculatedHash);
 Assert.assertEquals(stackLineHash, calculatedHash);
 }

 @Test
 public void testGetLineNumberHash() {
 Throwable e = new RuntimeException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(e);
 int lineNumberHash = ed.getLineNumberHash();
 StackTraceElement[] stackTrace = e.getStackTrace();
 StackTraceElement lastElement = stackTrace[stackTrace.length - 1];
 int lineNumber = lastElement.getLineNumber();
 Assert.assertEquals(lineNumber, lineNumberHash);
 }

 @Test
 public void testAsHexString() {
 Throwable e = new RuntimeException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(e);
 String hexString = ed.asHexString();
 String expectedHexString = ed.toHexString(ed.getStackLineHash()) + "-" + ed.toHexString(ed.getLineNumberHash());
 if (e.getCause() != null) {
 ExceptionDiagnosis causeEd = new ExceptionDiagnosis(e.getCause());
 expectedHexString += " " + causeEd.asHexString();
 }
 Assert.assertEquals(expectedHexString, hexString);
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"));
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"));
 Assert.assertTrue(ed1.equals(ed2));
 Assert.assertTrue(ed2.equals(ed1));

 ExceptionDiagnosis ed3 = new ExceptionDiagnosis(new RuntimeException("Test Exception 2"));
 Assert.assertFalse(ed1.equals(ed3));
 Assert.assertFalse(ed3.equals(ed1));

 ExceptionDiagnosis ed4 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"), new String[]{"twitter4j."});
 ExceptionDiagnosis ed5 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"), new String[]{"java."});
 Assert.assertFalse(ed4.equals(ed5));
 Assert.assertFalse(ed5.equals(ed4));

 ExceptionDiagnosis ed6 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"), new String[]{"twitter4j."});
 ed6.th.setStackTrace(new StackTraceElement[0]);
 ExceptionDiagnosis ed7 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"), new String[]{"twitter4j."});
 ed7.th.setStackTrace(new StackTraceElement[0]);
 Assert.assertTrue(ed6.equals(ed7));
 Assert.assertTrue(ed7.equals(ed6));
}

 @Test
 public void testHashCode() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"));
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"));
 int hashCode1 = ed1.hashCode();
 int hashCode2 = ed2.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);

 ExceptionDiagnosis ed3 = new ExceptionDiagnosis(new RuntimeException("Test Exception 2"));
 int hashCode3 = ed3.hashCode();
 Assert.assertNotEquals(hashCode1, hashCode3);

 ExceptionDiagnosis ed4 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"), new String[]{"twitter4j."});
 ExceptionDiagnosis ed5 = new ExceptionDiagnosis(new RuntimeException("Test Exception 1"), new String[]{"java."});
 Assert.assertNotEquals(ed4.hashCode(), ed5.hashCode());
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new RuntimeException("Test Exception"));
 String expectedString = "ExceptionDiagnosis{stackLineHash=" + ed.getStackLineHash() + ", lineNumberHash=" + ed.getLineNumberHash() + '}';
 String actualString = ed.toString();
 Assert.assertEquals(expectedString, actualString);
 }
}