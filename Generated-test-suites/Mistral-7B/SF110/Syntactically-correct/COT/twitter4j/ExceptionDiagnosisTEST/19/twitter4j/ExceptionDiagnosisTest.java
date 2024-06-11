package twitter4j.test;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

 private ExceptionDiagnosis createExceptionDiagnosis(Throwable throwable) {
  return new ExceptionDiagnosis(throwable, new String[0]);
 }

 @Test
 public void testConstructor() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  assertNotNull(exceptionDiagnosis);
 }

 @Test
 public void testGetStackLineHash() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  int stackLineHash = exceptionDiagnosis.getStackLineHash();
  assertNotEquals(-1, stackLineHash);
 }

 @Test
 public void testGetLineNumberHash() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
  assertNotEquals(-1, lineNumberHash);
 }

 @Test
 public void testAsHexString() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  String hexString = exceptionDiagnosis.asHexString();
  assertNotNull(hexString);
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsNullObject() {
  ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new RuntimeException("Test Exception"), new String[0]);
  assertNotEquals(null, exceptionDiagnosis);
  assertFalse(exceptionDiagnosis.equals(null));
 }

 @Test
 public void testEqualsSameObject() {
  ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new RuntimeException("Test Exception"), new String[0]);
  ExceptionDiagnosis exceptionDiagnosis2 = exceptionDiagnosis1;
  assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testEqualsDifferentObjects() {
  ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new RuntimeException("Test Exception"), new String[0]);
  Object obj = new Object();
  assertFalse(exceptionDiagnosis1.equals(obj));
 }

 @Test
 public void testHashCode() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  int hashCode = exceptionDiagnosis.hashCode();
  int hashCode2 = exceptionDiagnosis.hashCode();
  assertEquals(hashCode, hashCode2);
 }

 @Test
 public void testToString() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  String toString = exceptionDiagnosis.toString();
  assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
  assertTrue(toString.contains("lineNumberHash="));
 }

 @Test
 public void testToStringNoClassNameMatch() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  String filter = "foo.bar.MyClass";
  String toString = exceptionDiagnosis.toString();
  assertFalse(toString.contains(filter));
 }

 @Test
 public void testToStringClassNameMatch() {
  Throwable throwable = new RuntimeException("Test Exception");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  String filter = "java.lang.RuntimeException";
  String toString = exceptionDiagnosis.toString();
  assertTrue(toString.contains(filter));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullThrowable() {
  String[] inclusionFilter = new String[0];
  try {
   new ExceptionDiagnosis(null, inclusionFilter);
   fail("Exception should have been thrown");
  } catch (NullPointerException e) {
   // expected
  }
  StackTraceElement[] stackTrace = new RuntimeException("TestException").getStackTrace();
  Method method = ExceptionDiagnosis.class.getDeclaredMethod("ExceptionDiagnosis", Throwable.class, Class[].class);
  method.setAccessible(true);
  try {
   Object instance = ExceptionDiagnosis.class.newInstance();
   method.invoke(instance, null, inclusionFilter);
   fail("Exception should have been thrown");
  } catch (IllegalArgumentException e) {
   // expected
  } catch (InstantiationException e) {
   fail("Should not have thrown InstantiationException");
  } catch (IllegalAccessException e) {
   fail("Should not have thrown IllegalAccessException");
  } catch (InvocationTargetException e) {
   fail("Should not have thrown InvocationTargetException");
  }
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullInclusionFilter() {
  Throwable throwable = new RuntimeException("TestException");
  String[] inclusionFilter = null;
  try {
   new ExceptionDiagnosis(throwable, inclusionFilter);
   fail("Exception should have been thrown");
  } catch (NullPointerException e) {
   // expected
  }
  StackTraceElement[] stackTrace = new RuntimeException("TestException").getStackTrace();
  Method method = ExceptionDiagnosis.class.getDeclaredMethod("ExceptionDiagnosis", Throwable.class, Class[].class);
  method.setAccessible(true);
  try {
   Object instance = ExceptionDiagnosis.class.newInstance();
   method.invoke(instance, throwable, null);
   fail("Exception should have been thrown");
  } catch (IllegalArgumentException e) {
   fail("Should not have thrown IllegalArgumentException");
  } catch (InstantiationException e) {
   fail("Should not have thrown InstantiationException");
  } catch (IllegalAccessException e) {
   fail("Should not have thrown IllegalAccessException");
  } catch (InvocationTargetException e) {
   fail("Should not have thrown InvocationTargetException");
  }
 }

 @Test
 public void testToStringEmptyInclusionFilter() {
  Throwable throwable = new RuntimeException("TestException");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  String toString = exceptionDiagnosis.toString();
  assertTrue(toString.isEmpty());
 }

 @Test
 public void testToStringWithInclusionFilter() {
  Throwable throwable = new RuntimeException("TestException");
  String filter = "my.package.Name";
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable, new String[]{filter});
  String toString = exceptionDiagnosis.toString();
  assertTrue(toString.contains(filter));
 }

 @Test
 public void testToStringMultipleLinesWithInclusionFilter() {
  Throwable throwable = new RuntimeException("TestException");
  String filter = "my.package.Name";
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable, new String[]{filter});
  String[] stackTrace = throwable.getStackTrace();
  String toString = exceptionDiagnosis.toString();
  for (int i = stackTrace.length - 1; i >= 0; --i) {
   if (stackTrace[i].getClassName().startsWith(filter)) {
      String expectedLine = String.format("ExceptionDiagnosis{stackLineHash=%s, lineNumberHash=%s}", toHexString(stackTrace[i].getHashCode()), stackTrace[i].getLineNumber());
      assertTrue(toString.contains(expectedLine));
      break;
   }
  }
 }

 @Test
 public void testToStringMultipleLinesWithoutInclusionFilter() {
  Throwable throwable = new RuntimeException("TestException");
  ExceptionDiagnosis exceptionDiagnosis = createExceptionDiagnosis(throwable);
  String[] stackTrace = throwable.getStackTrace();
  String toString = exceptionDiagnosis.toString();
  for (int i = stackTrace.length - 1; i >= 0; --i) {
   String expectedLine = String.format("ExceptionDiagnosis{stackLineHash=%s, lineNumberHash=%s}", toHexString(stackTrace[i].getHashCode()), stackTrace[i].getLineNumber());
   assertTrue(toString.contains(expectedLine));
  }
 }

 private String toHexString(int value) {
  return "0000000" + Integer.toHexString(value).substring(6);
 }

}