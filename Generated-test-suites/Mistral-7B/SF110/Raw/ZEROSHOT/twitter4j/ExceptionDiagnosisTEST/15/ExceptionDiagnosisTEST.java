import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import java.lang.Throwable;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructor() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
 Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
 Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
 Assert.assertNotNull(exceptionDiagnosis.getHexString());
 }

 @Test
 public void testExceptionDiagnosisConstructorWithFilter() {
 String filter = "twitter4j";
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable(), new String[]{filter});
 StackTraceElement[] stackTrace = ((Throwable) exceptionDiagnosis.getTh()).getStackTrace();
 String expectedHexString = "";
 for (StackTraceElement stackTraceElement : stackTrace) {
 if (stackTraceElement.getClassName().startsWith(filter)) {
 int hash = stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
 expectedHexString += toHexString(hash) + "-";
 break;
 }
 }
 expectedHexString += toHexString(exceptionDiagnosis.getStackLineHash()) + " " + toHexString(exceptionDiagnosis.getLineNumberHash());
 Assert.assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
 }

 @Test(expected = AssertionError.class)
 public void testExceptionDiagnosisConstructorWithNullFilter() {
 new ExceptionDiagnosis(new Throwable(), null);
 }

 @Test
 public void testGetStackLineHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable());
 int stackLineHash2 = exceptionDiagnosis2.getStackLineHash();
 Assert.assertNotSame(stackLineHash, stackLineHash2);
 }

 @Test
 public void testGetLineNumberHash() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable());
 int lineNumberHash2 = exceptionDiagnosis2.getLineNumberHash();
 Assert.assertNotSame(lineNumberHash, lineNumberHash2);
 }

 @Test
 public void testAsHexString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
 String hexString = exceptionDiagnosis.asHexString();
 Assert.assertNotNull(hexString);
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable());
 Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable());
 int hashCode1 = exceptionDiagnosis1.hashCode();
 int hashCode2 = exceptionDiagnosis2.hashCode();
 Assert.assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
 String toString = exceptionDiagnosis.toString();
 Assert.assertNotNull(toString);
 }

 private String toHexString(int value) {
 return "0000000" + Integer.toHexString(value).substring(Integer.toHexString(value).length() - 8);
 }
}