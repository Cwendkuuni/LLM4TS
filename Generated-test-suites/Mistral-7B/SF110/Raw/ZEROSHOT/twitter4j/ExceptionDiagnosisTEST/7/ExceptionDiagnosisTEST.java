import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.Throwable;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstruction() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"));
 assertNotNull(exceptionDiagnosis.getStackLineHash());
 assertNotNull(exceptionDiagnosis.getLineNumberHash());
 assertNotNull(exceptionDiagnosis.getHexString());
 }

 @Test
 public void testExceptionDiagnosisConstructionWithFilter() {
 String filter = "java.io.";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"), new String[]{filter});
 StackTraceElement[] stackTrace = new IOException("Test IOException").getStackTrace();
 int expectedStackLineHash = 0;
 int expectedLineNumberHash = 0;
 for (StackTraceElement element : stackTrace) {
 if (element.getClassName().startsWith(filter)) {
 expectedStackLineHash = 31 * expectedStackLineHash + element.getClassName().hashCode() + element.getMethodName().hashCode();
 expectedLineNumberHash = 31 * expectedLineNumberHash + element.getLineNumber();
 break;
 }
 }
 ExceptionDiagnosis expectedExceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"), new String[]{filter});
 assertEquals(expectedStackLineHash, expectedExceptionDiagnosis.getStackLineHash());
 assertEquals(expectedLineNumberHash, expectedExceptionDiagnosis.getLineNumberHash());
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructionWithNullFilter() {
 new ExceptionDiagnosis(new IOException("Test IOException"), null);
 }

 @Test
 public void testGetStackLineHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"));
 IOException cause = new IOException("Test Cause IOException");
 exceptionDiagnosis.th.initCause(cause);
 ExceptionDiagnosis expectedExceptionDiagnosis = new ExceptionDiagnosis(cause);
 int expectedStackLineHash = exceptionDiagnosis.getStackLineHash();
 int expectedLineNumberHash = exceptionDiagnosis.getLineNumberHash();
 int stackLineHash = expectedExceptionDiagnosis.getStackLineHash();
 int lineNumberHash = expectedExceptionDiagnosis.getLineNumberHash();
 assertEquals(expectedStackLineHash, stackLineHash);
 assertEquals(expectedLineNumberHash, lineNumberHash);
 }

 @Test
 public void testGetLineNumberHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"));
 IOException cause = new IOException("Test Cause IOException");
 exceptionDiagnosis.th.initCause(cause);
 ExceptionDiagnosis expectedExceptionDiagnosis = new ExceptionDiagnosis(cause);
 int expectedStackLineHash = exceptionDiagnosis.getStackLineHash();
 int expectedLineNumberHash = exceptionDiagnosis.getLineNumberHash();
 int stackLineHash = expectedExceptionDiagnosis.getStackLineHash();
 int lineNumberHash = expectedExceptionDiagnosis.getLineNumberHash();
 assertEquals(expectedStackLineHash, stackLineHash);
 assertEquals(expectedLineNumberHash, lineNumberHash);
 }

 @Test
 public void testAsHexString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"));
 IOException cause = new IOException("Test Cause IOException");
 exceptionDiagnosis.th.initCause(cause);
 ExceptionDiagnosis expectedExceptionDiagnosis = new ExceptionDiagnosis(cause);
 String actualHexString = exceptionDiagnosis.asHexString();
 String expectedHexString = expectedExceptionDiagnosis.asHexString();
 assertEquals(actualHexString, expectedHexString);
 }

 @Test
 public void testHexStringFormat() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"));
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 String expectedHexString = exceptionDiagnosis.toHexString(stackLineHash) + "-" + exceptionDiagnosis.toHexString(lineNumberHash);
 String actualHexString = exceptionDiagnosis.asHexString();
 assertEquals(expectedHexString, actualHexString);
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new IOException("Test IOException"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new IOException("Test IOException"));
 assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testNotEqualsDifferentObjects() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new IOException("Test IOException"));
 Object anotherObject = new Object();
 assertFalse(exceptionDiagnosis1.equals(anotherObject));
 }

 @Test
 public void testNotEqualsDifferentExceptionDiagnosisObjects() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new IOException("Test IOException"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new IOException("Test IOException2"));
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new IOException("Test IOException"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new IOException("Test IOException"));
 int expectedHashCode1 = exceptionDiagnosis1.hashCode();
 int expectedHashCode2 = exceptionDiagnosis2.hashCode();
 assertEquals(expectedHashCode1, expectedHashCode2);
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException("Test IOException"));
 String expectedString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
 String actualString = exceptionDiagnosis.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testExceptionDiagnosisWithCause() {
 IOException ioException = new IOException("Test IOException");
 IOException cause = new IOException("Test Cause IOException");
 ioException.initCause(cause);
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(ioException);
 String expectedHexString = exceptionDiagnosis.toHexString(exceptionDiagnosis.getStackLineHash()) + "-" + exceptionDiagnosis.toHexString(exceptionDiagnosis.getLineNumberHash()) + " " + new ExceptionDiagnosis(cause).asHexString();
 String actualHexString = exceptionDiagnosis.asHexString();
 assertEquals(expectedHexString, actualHexString);
 }
}