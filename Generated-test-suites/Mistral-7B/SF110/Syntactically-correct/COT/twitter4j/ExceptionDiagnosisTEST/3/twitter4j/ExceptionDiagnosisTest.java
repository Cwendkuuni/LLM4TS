import twitter4j.ExceptionDiagnosis;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilterArray() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        
        assertNotNull(stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        
        assertNotNull(lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();
        
        assertNotNull(hexString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        
        Throwable throwable3 = new IOException("Test IOException 3");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable3, new String[]{"test"});
        
        assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));
        
        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis5));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        
        int hashcode1 = exceptionDiagnosis1.hashCode();
        int hashcode2 = exceptionDiagnosis2.hashCode();
        
        assertNotEquals(hashcode1, hashcode2);

        Throwable throwable3 = new IOException("Test IOException 3");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable3, new String[]{"test"});
        
        assertEquals(exceptionDiagnosis3.hashCode(), exceptionDiagnosis4.hashCode());
        
        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis5.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        
        String toString = exceptionDiagnosis.toString();
        
        assertNotNull(toString);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullObject() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }
}