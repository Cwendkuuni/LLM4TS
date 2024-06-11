import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwelfareException;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructorWithThrowable() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwelfareException("Test Exception"));
 assertEquals("", exceptionDiagnosis.getHexString());
 assertNull(exceptionDiagnosis.th.getCause());

 StackTraceElement[] stackTrace = exceptionDiagnosis.th.getStackTrace();
 int expectedStackLineHash = 0;
 int expectedLineNumberHash = 0;

 for (StackTraceElement stackTraceElement : stackTrace) {
 expectedStackLineHash = 31 * expectedStackLineHash + (stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode());
 expectedLineNumberHash = 31 * expectedLineNumberHash + stackTraceElement.getLineNumber();
 }

 assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
 assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
 }

 @Test
 public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
 String filter = "twitter4j";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwelfareException("Test Exception"), new String[]{filter});

 StackTraceElement[] stackTrace = exceptionDiagnosis.th.getStackTrace();
 int expectedStackLineHash = 0;
 int expectedLineNumberHash = 0;
 int i = stackTrace.length - 1;

 while (i >= 0) {
 if (stackTrace[i].getClassName().startsWith(filter)) {
 expectedStackLineHash = 31 * expectedStackLineHash + (stackTrace[i].getClassName().hashCode() + stackTrace[i].getMethodName().hashCode());
 expectedLineNumberHash = 31 * expectedLineNumberHash + stackTrace[i].getLineNumber();
 break;
 }
 i--;
 }

 assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
 assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
 }

 @Test(expected = AssertionError.class)
 public void testExceptionDiagnosisConstructorWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwelfareException("Test Exception1"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwelfareException("Test Exception2"));
 ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(exceptionDiagnosis1.th);

 assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));

 ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(new TwelfareException("Test Exception4"));
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis4));

 ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(exceptionDiagnosis1.th, new String[]{"twitter4j"});
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis5));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwelfareException("Test Exception1"));
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwelfareException("Test Exception2"));
 ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(exceptionDiagnosis1.th);

 int expectedHashCode1 = exceptionDiagnosis1.hashCode();
 int expectedHashCode2 = exceptionDiagnosis2.hashCode();
 int expectedHashCode3 = exceptionDiagnosis3.hashCode();

 assertEquals(expectedHashCode1, exceptionDiagnosis1.hashCode());
 assertEquals(expectedHashCode2, exceptionDiagnosis2.hashCode());
 assertEquals(expectedHashCode1, exceptionDiagnosis3.hashCode());
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwelfareException("Test Exception"));
 String expectedToString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + "}";
 assertEquals(expectedToString, exceptionDiagnosis.toString());
 }
}