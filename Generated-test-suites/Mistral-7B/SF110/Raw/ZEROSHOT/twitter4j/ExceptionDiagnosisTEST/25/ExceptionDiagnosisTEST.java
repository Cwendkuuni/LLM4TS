import org.junit.Test;
import org.junit.Assert.*;
import java.lang.Exception;
import java.lang.reflect.Modifier;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructor() {
 Exception exception = new Exception("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(exception);
 assertNotNull(exceptionDiagnosis);
 }

 @Test
 public void testExceptionDiagnosisConstructorWithFilter() {
 String filter = "twitter4j";
 Exception exception = new Exception("Test Exception");
 StackTraceElement[] stackTrace = exception.getStackTrace();
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(exception, new String[]{filter});
 assertNotNull(exceptionDiagnosis);

 for (StackTraceElement element : stackTrace) {
 if (element.getClassName().startsWith(filter)) {
 String className = element.getClassName();
 String methodName = element.getMethodName();
 int expectedHash = (className.hashCode() + methodName.hashCode()) * 31;
 int lineNumber = element.getLineNumber();
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

 assertEquals("Unexpected stack line hash", expectedHash, stackLineHash);
 assertEquals("Unexpected line number hash", lineNumber, lineNumberHash);
 break;
 }
 }
 }

 @Test
 public void testAsHexString() {
 Exception exception = new Exception("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(exception);
 String hexString = exceptionDiagnosis.asHexString();
 assertTrue("Hex string should not be empty", !hexString.isEmpty());
 }

 @Test
 public void testGetStackLineHash() {
 Exception exception = new Exception("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(exception);
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 int expectedHash = 0;
 StackTraceElement[] stackTrace = exception.getStackTrace();
 for (StackTraceElement element : stackTrace) {
 expectedHash = 31 * expectedHash + (element.getClassName().hashCode() + element.getMethodName().hashCode());
 }
 assertEquals("Unexpected stack line hash", expectedHash, stackLineHash);
 }

 @Test
 public void testGetLineNumberHash() {
 Exception exception = new Exception("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(exception);
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 int expectedHash = 0;
 StackTraceElement stackTraceElement = exception.getStackTrace()[0];
 assertEquals("Unexpected line number hash", stackTraceElement.getLineNumber(), lineNumberHash);
 }

 @Test(expected = AssertionError.class)
 public void testEqualsNullObject() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Exception("Test Exception"));
 assertNotSame("Unexpected equal object", exceptionDiagnosis, null);
 }

 @Test
 public void testEqualsSameObject() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Exception("Test Exception"));
 ExceptionDiagnosis exceptionDiagnosis2 = exceptionDiagnosis1;
 assertTrue("Unexpected not equal object", exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testEqualsDifferentObjects() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Exception("Test Exception1"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Exception("Test Exception2"));
 assertFalse("Unexpected equal objects", exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testHashCode() {
 Exception exception = new Exception("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(exception);
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(exception);
 int hashCode1 = exceptionDiagnosis1.hashCode();
 int hashCode2 = exceptionDiagnosis2.hashCode();
 assertEquals("Unexpected different hash codes", hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 Exception exception = new Exception("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(exception);
 String toString = exceptionDiagnosis.toString();
 String expectedString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
 assertTrue("Unexpected toString", toString.contains(expectedString));
 }

 @Test
 public void testToHexString() {
 int value = 12345678;
 String expectedHexString = "2a3b4c5d";
 String hexString = new ExceptionDiagnosis().toHexString(value);
 assertEquals("Unexpected hex string", expectedHexString, hexString);
 }

 @Test
 public void testGetCause() {
 Exception exception = new Exception("Test Exception");
 Exception cause = new Exception("Test Cause");
 exception.initCause(cause);
 ExceptionDiagnosis parentExceptionDiagnosis = new ExceptionDiagnosis(exception);
 ExceptionDiagnosis childExceptionDiagnosis = new ExceptionDiagnosis(exception, new String[]{"twitter4j"});
 String expectedHexString = parentExceptionDiagnosis.asHexString() + " " + childExceptionDiagnosis.asHexString();
 String hexString = exceptionDiagnosis.asHexString();
 assertTrue("Unexpected hex string for parent only", hexString.startsWith(parentExceptionDiagnosis.asHexString()));
 assertTrue("Unexpected hex string for child only", hexString.contains(childExceptionDiagnosis.asHexString()));
 assertTrue("Unexpected hex string for full exception chain", hexString.equals(expectedHexString));
 }
}