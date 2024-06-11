import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructorWithNullThrowable() {
     try {
         new ExceptionDiagnosis(null);
         fail("Expected IllegalArgumentException with null Throwable.");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testExceptionDiagnosisConstructorWithEmptyStringArrayFilter() {
     Throwable throwable = new RuntimeException("Test Exception");
     ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
     String expectedHexString = exceptionDiagnosis.asHexString();

     String[] filter = new String[0];
     ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, filter);
     String actualHexString = exceptionDiagnosisWithFilter.asHexString();

     assertEquals(expectedHexString, actualHexString);
 }

 @Test
 public void testExceptionDiagnosisConstructorWithIncludeFilter() {
     Throwable throwable = new RuntimeException("Test Exception");
     String className = "test.MyClass";
     String methodName = "testMethod";
     ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
     String expectedHexString = exceptionDiagnosis.asHexString();

     String[] filter = {className};
     ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, filter);
     String actualHexString = exceptionDiagnosisWithFilter.asHexString();

     int stackLineHashExpected = "test.MyClass".hashCode() * 31 + methodName.hashCode();
     int stackLineHashActual = Integer.parseInt(actualHexString.split("-")[0], 16);
     int lineNumberHashExpected = -1;
     int lineNumberHashActual = Integer.parseInt(actualHexString.split(" ")[1].split("-")[0], 16);

     assertEquals(stackLineHashExpected, stackLineHashActual);
     assertEquals(lineNumberHashExpected, lineNumberHashActual);
 }

 @Test
 public void testExceptionDiagnosisConstructorWithIncludeFilterAndCause() {
     Throwable throwable = new RuntimeException("Test Exception", new IllegalArgumentException("Test Cause"));
     String className = "test.MyClass";
     String methodName = "testMethod";
     ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
     String expectedHexString = exceptionDiagnosis.asHexString();

     String[] filter = {className};
     ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, filter);
     String actualHexString = exceptionDiagnosisWithFilter.asHexString();

     int stackLineHashExpected = "test.MyClass".hashCode() * 31 + methodName.hashCode();
     int stackLineHashActual = Integer.parseInt(actualHexString.split("-")[0], 16);
     int lineNumberHashExpected = -1;
     int lineNumberHashActual = -1;

     if (expectedHexString.contains(" ")) {
         String[] hexStrings = expectedHexString.split(" ");
         stackLineHashExpected = Integer.parseInt(hexStrings[0], 16);
         lineNumberHashExpected = Integer.parseInt(hexStrings[1].split("-")[0], 16);
     }

     ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(throwable.getCause());
     String causeHexString = causeExceptionDiagnosis.asHexString();

     String expectedCauseHexString = "";
     if (null != throwable.getCause()) {
         expectedCauseHexString = new ExceptionDiagnosis(throwable.getCause(), filter).asHexString();
     }

     String actualHexStringWithCause = exceptionDiagnosisWithFilter.asHexString();
     String expectedHexStringWithCause = expectedHexString + " " + expectedCauseHexString;

     assertEquals(expectedHexStringWithCause, actualHexStringWithCause);
 }

 @Test
 public void testGetStackLineHash() {
     Throwable throwable = new RuntimeException("Test Exception");
     String className = "test.MyClass";
     String methodName = "testMethod";
     ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
     int stackLineHashExpected = "test.MyClass".hashCode() * 31 + methodName.hashCode();
     int stackLineHashActual = exceptionDiagnosis.getStackLineHash();

     assertEquals(stackLineHashExpected, stackLineHashActual);
 }

 @Test
 public void testGetLineNumberHash() {
     Throwable throwable = new RuntimeException("Test Exception");
     String className = "test.MyClass";
     String methodName = "testMethod";     ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
     int lineNumberHashExpected = -1;
     int lineNumberHashActual = exceptionDiagnosis.getLineNumberHash();

     assertEquals(lineNumberHashExpected, lineNumberHashActual);
 }

 @Test
 public void testAsHexString() {
     Throwable throwable = new RuntimeException("Test Exception");
     String className = "test.MyClass";
     String methodName = "testMethod";
     ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
     String expectedHexString = exceptionDiagnosis.asHexString();

     String[] filter = {className};
     ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, filter);
     String actualHexString = exceptionDiagnosisWithFilter.asHexString();

     assertEquals(expectedHexString, actualHexString);
 }

 @Test
 public void testEquals() {
     Throwable throwable1 = new RuntimeException("Test Exception 1");
     Throwable throwable2 = new RuntimeException("Test Exception 2");
     String className = "test.MyClass";
     String methodName = "testMethod";

     ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
     ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
     ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
     ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable2);

     ExceptionDiagnosis exceptionDiagnosisWithFilter1 = new ExceptionDiagnosis(throwable1, new String[]{"test.MyClass"});
     ExceptionDiagnosis exceptionDiagnosisWithFilter2 = new ExceptionDiagnosis(throwable2, new String[]{"test.MyClass"});
     ExceptionDiagnosis exceptionDiagnosisWithFilter3 = new ExceptionDiagnosis(throwable1, new String[]{"test.OtherClass"});
     ExceptionDiagnosis exceptionDiagnosisWithFilter4 = new ExceptionDiagnosis(throwable2, new String[]{"test.OtherClass"});

     assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
     assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
     assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosisWithFilter1));
     assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosisWithFilter2));
     assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosisWithFilter3));
     assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosisWithFilter4));
 }

 @Test
 public void testHashCode() {
     Throwable throwable1 = new RuntimeException("Test Exception 1");
     Throwable throwable2 = new RuntimeException("Test Exception 2");
     String className = "test.MyClass";
     String methodName = "testMethod";

     ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
     ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
     ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
     ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable2);

     ExceptionDiagnosis exceptionDiagnosisWithFilter1 = new ExceptionDiagnosis(throwable1, new String[]{"test.MyClass"});
     ExceptionDiagnosis exceptionDiagnosisWithFilter2 = new ExceptionDiagnosis(throwable2, new String[]{"test.MyClass"});
     ExceptionDiagnosis exceptionDiagnosisWithFilter3 = new ExceptionDiagnosis(throwable1, new String[]{"test.OtherClass"});
     ExceptionDiagnosis exceptionDiagnosisWithFilter4 = new ExceptionDiagnosis(throwable2, new String[]{"test.OtherClass"});

     int hashCode1 = exceptionDiagnosis1.hashCode();
     int hashCode2 = exceptionDiagnosis2.hashCode();
     int hashCode3 = exceptionDiagnosis3.hashCode();
     int hashCode4 = exceptionDiagnosis4.hashCode();

     int hashCodeWithFilter1 = exceptionDiagnosisWithFilter1.hashCode();
     int hashCodeWithFilter2 = exceptionDiagnosisWithFilter2.hashCode();
     int hashCodeWithFilter3 = exceptionDiagnosisWithFilter3.hashCode();
     int hashCodeWithFilter4 = exceptionDiagnosisWithFilter4.hashCode();

     assertEquals(hashCode1, hashCode3);
     assertNotEquals(hashCode1, hashCode2);
     assertNotEquals(hashCode1, hashCodeWithFilter1);
     assertNotEquals(hashCode1, hashCodeWithFilter2);
     assertNotEquals(hashCode1, hashCodeWithFilter3);
     assertNotEquals(hashCode1, hashCodeWithFilter4);
 }

 @Test
 public void testToString() {
     Throwable throwable = new RuntimeException("Test Exception");
     String className = "test.MyClass";
     String methodName = "testMethod";
     ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

     String expectedToString = "ExceptionDiagnosis{stackLineHash=" + "test.MyClass".hashCode() * 31 + methodName.hashCode() + ", lineNumberHash=" + (-1) + '}';
     String actualToString = exceptionDiagnosis.toString();

     assertEquals(expectedToString, actualToString);
 }
}