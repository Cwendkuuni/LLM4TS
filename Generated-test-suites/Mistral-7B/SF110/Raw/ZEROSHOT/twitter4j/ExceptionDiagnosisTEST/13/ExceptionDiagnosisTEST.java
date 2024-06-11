package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import java.lang.Throwable;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstruction() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});

 Assert.assertNotNull(ed1.getStackLineHash());
 Assert.assertNotNull(ed1.getLineNumberHash());
 Assert.assertNotNull(ed1.asHexString());

 Assert.assertEquals(ed1.getClass(), ed2.getClass());
 }

 @Test
 public void testGetStackLineHash() {
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});

 Assert.assertEquals(ed.getStackLineHash(), ed.hashCode() >>> 16);
 }

 @Test
 public void testGetLineNumberHash() {
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});

 Assert.assertEquals(ed.getLineNumberHash(), ed.hashCode() & 0xFFFF);
 }

 @Test
 public void testAsHexString() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});

 Assert.assertEquals(ed1.asHexString(), "");
 Assert.assertNotEquals(ed1.asHexString(), ed2.asHexString());
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis ed3 = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});
 ExceptionDiagnosis ed4 = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});

 Assert.assertFalse(ed1.equals(null));
 Assert.assertFalse(ed1.equals(new Object()));
 Assert.assertFalse(ed1.equals(ed2));
 Assert.assertFalse(ed1.equals(ed3));
 Assert.assertTrue(ed3.equals(ed4));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable());
 ExceptionDiagnosis ed3 = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});
 ExceptionDiagnosis ed4 = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});

 Assert.assertFalse(ed1.hashCode() == ed2.hashCode());
 Assert.assertTrue(ed3.hashCode() == ed4.hashCode());
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new Throwable(), new String[]{"twitter4j"});

 Assert.assertEquals("ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", ed.toString());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidInclusionFilter() {
 new ExceptionDiagnosis(new Throwable(), new String[]{"not.a.valid.filter"});
 }

 @Test
 public void testDiagnosisChain() {
 Throwable t = new IOException("Test IOException");
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(t);
 Throwable cause = new IllegalArgumentException("Test IllegalArgumentException");
 t.initCause(cause);
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(t, new String[]{"twitter4j"});

 Assert.assertEquals(ed1.asHexString(), ed2.asHexString().substring(0, ed2.asHexString().lastIndexOf(" ")));
 }
}