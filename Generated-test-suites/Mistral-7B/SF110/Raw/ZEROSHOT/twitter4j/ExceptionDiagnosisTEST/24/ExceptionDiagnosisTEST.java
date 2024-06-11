import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructionWithThrowable() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new RuntimeException("Test Exception"));
 Assert.assertNotNull(exceptionDiagnosis);
 }

 @Test
 public void testExceptionDiagnosisConstructionWithThrowableAndFilter() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new RuntimeException("Test Exception"), filter);
 Assert.assertNotNull(exceptionDiagnosis);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructionWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test
 public void testGetStackLineHash() {
 Throwable throwable = new RuntimeException("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 Assert.assertNotNull(stackLineHash);
 }

 @Test
 public void testGetLineNumberHash() {
 Throwable throwable = new RuntimeException("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 Assert.assertNotNull(lineNumberHash);
 }

 @Test
 public void testAsHexString() {
 Throwable throwable = new RuntimeException("Test Exception");
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
 String hexString = exceptionDiagnosis.asHexString();
 Assert.assertNotNull(hexString);
 }

 @Test
 public void testEquals() {
 Throwable throwable1 = new RuntimeException("Test Exception 1");
 Throwable throwable2 = new RuntimeException("Test Exception 2");
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, new String[0]);
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);
 boolean equals = exceptionDiagnosis1.equals(exceptionDiagnosis2);
 Assert.assertFalse(equals);

 Throwable throwable3 = new RuntimeException("Test Exception 3");
 ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[0]);
 boolean equals2 = exceptionDiagnosis1.equals(exceptionDiagnosis3);
 Assert.assertTrue(equals2);
 }

 @Test
 public void testHashCode() {
 Throwable throwable = new RuntimeException("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
 int hashCode = exceptionDiagnosis.hashCode();
 Assert.assertNotNull(hashCode);
 }

 @Test
 public void testToString() {
 Throwable throwable = new RuntimeException("Test Exception");
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
 String toString = exceptionDiagnosis.toString();
 Assert.assertNotNull(toString);
 }

 @Test
 public void testToStringWithFilter() {
 Throwable throwable = new RuntimeException("Test Exception");
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
 String toString = exceptionDiagnosis.toString();
 Assert.assertNotNull(toString);
 }

 @Test
 public void testToHexString() {
 int value = 12345678;
 String hexString = ExceptionDiagnosis.toHexString(value);
 Assert.assertEquals("5432100A", hexString);
 }
}