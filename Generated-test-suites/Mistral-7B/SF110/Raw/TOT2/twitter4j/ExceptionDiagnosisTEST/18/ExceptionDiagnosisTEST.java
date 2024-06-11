import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExceptionDiagnosisTest {

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructionWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testExceptionDiagnosisConstructionWithEmptyFilter() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        assertNotNull(exceptionDiagnosis);
        assertNotNull(exceptionDiagnosis.getTh());
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithEmptyStackTrace() {
        Throwable throwable = new IOException("Test IOException") {
            @Override
            public StackTraceElement[] getStackTrace() {
                return new StackTraceElement[0];
            }
        };
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        assertNotNull(exceptionDiagnosis);
        assertNotNull(exceptionDiagnosis.getTh());
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithMultipleMatches() {
        Throwable throwable = new IOException("Test IOException");
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable, new String[]{"test"});
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable, new String[]{"testPackage.test"});
        assertNotNull(exceptionDiagnosis1);
        assertNotNull(exceptionDiagnosis2);
        assertEquals(exceptionDiagnosis1.asHexString(), exceptionDiagnosis2.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithNegativeLineNumber() {
        Throwable throwable = new IOException("Test IOException");
        StackTraceElement stackTraceElement = new StackTraceElement("test", "test", "-1", -1);
        throwable.setStackTrace(new StackTraceElement[]{stackTraceElement});
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        assertNotNull(exceptionDiagnosis);
        assertEquals("-1", exceptionDiagnosis.getLineNumberHash() + "");
    }

    @Test(expected = StackOverflowError.class)
    public void testExceptionDiagnosisConstructionWithInfinitelyLongHexString() {
        Throwable throwable = new IOException("Test IOException");
        String infinitelyLongHexString = "";
        for (int i = 0; i < 10000; i++) {
            infinitelyLongHexString += "0";
        }
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        exceptionDiagnosis.hexString = infinitelyLongHexString;
    }

    @Test
    public void testExceptionDiagnosisConstructionWithCauseEmptyStackTrace() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(throwable.getCause(), new String[0]);
        Throwable nullThrowable = null;
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable("Test Throwable"), new String[0]);
        assertNotNull(causeExceptionDiagnosis);
        assertNull(causeExceptionDiagnosis.getTh());
        assertNotNull(exceptionDiagnosis);
        assertNotNull(exceptionDiagnosis.getTh());
        assertNotEquals(exceptionDiagnosis.asHexString(), causeExceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisEquals() {
        Throwable throwable1 = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new IOException("Test IOException 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable3, new String[0]);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));

        Throwable throwable4 = new IOException("Test IOException 1");
        StackTraceElement stackTraceElement4 = new StackTraceElement("test", "test", 1, 1);
        throwable4.setStackTrace(new StackTraceElement[]{stackTraceElement4});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable4, new String[0]);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis4));
    }

    @Test
    public void testExceptionDiagnosisHashcode() {
        Throwable throwable1 = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        Throwable throwable3 = new IOException("Test IOException 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable3, new String[0]);
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());

        Throwable throwable4 = new IOException("Test IOException 1");
        StackTraceElement stackTraceElement4 = new StackTraceElement("test", "test", 1, 1);
        throwable4.setStackTrace(new StackTraceElement[]{stackTraceElement4});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable4, new String[0]);
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis4.hashCode());
    }

    @Test
    public void testExceptionDiagnosisToString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        assertEquals("ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + "}", exceptionDiagnosis.toString());
    }
}