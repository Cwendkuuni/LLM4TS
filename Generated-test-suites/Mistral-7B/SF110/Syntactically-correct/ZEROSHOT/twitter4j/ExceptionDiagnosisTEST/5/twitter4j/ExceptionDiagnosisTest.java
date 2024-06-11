import org.junit.Test;
import org.junit.Assert.*;
import java.lang.Throwable;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructor() {
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable());
 assertTrue(ed instanceof ExceptionDiagnosis);
 assertNull(ed.getHexString());
 assertNull(ed.getTh());
 }

 @Test
 public void testExceptionDiagnosisConstructorWithFilter() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), filter);
 assertNotNull(ed.getHexString());
 assertNotNull(ed.getTh());
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructorWithNullFilter() {
 String[] filter = null;
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), filter);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetStackLineHash() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), filter);
 int stackLineHash = ed.getStackLineHash();
 int expectedStackLineHash = 0;
 for (StackTraceElement stackTraceElement : ((Throwable)ed.getTh()).getStackTrace()) {
 if (stackTraceElement.getClassName().startsWith("twitter4j")) {
 expectedStackLineHash = 31 * expectedStackLineHash + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
 break;
 }
 }
 assertEquals(expectedStackLineHash, stackLineHash);
 }

 @Test
 public void testGetLineNumberHash() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), filter);
 int lineNumberHash = ed.getLineNumberHash();
 int expectedLineNumberHash = 0;
 for (StackTraceElement stackTraceElement : ((Throwable)ed.getTh()).getStackTrace()) {
 if (stackTraceElement.getClassName().startsWith("twitter4j") && stackTraceElement.getLineNumber() > 0) {
 expectedLineNumberHash = 31 * expectedLineNumberHash + stackTraceElement.getLineNumber();
 break;
 }
 }
 assertEquals(expectedLineNumberHash, lineNumberHash);
 }

 @Test
 public void testAsHexString() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable(), filter);
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable(), filter);
 String hexString1 = ed1.asHexString();
 String hexString2 = ed2.asHexString();
 assertNotSame(hexString1, hexString2);
 }

 @Test
 public void testToString() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), filter);
 String toString = ed.toString();
 String expectedToString = "ExceptionDiagnosis{stackLineHash=" + ed.getStackLineHash() + ", lineNumberHash=" + ed.getLineNumberHash() + '}';
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testEquals() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable(), filter);
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable(), filter);
 ExceptionDiagnosis ed3 = new ExceptionDiagnosis(new Throwable(), new String[]{"java"});
 assertTrue(ed1.equals(ed2));
 assertFalse(ed1.equals(ed3));
 }

 @Test
 public void testHashCode() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable(), filter);
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable(), filter);
 int hashCode1 = ed1.hashCode();
 int hashCode2 = ed2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

}