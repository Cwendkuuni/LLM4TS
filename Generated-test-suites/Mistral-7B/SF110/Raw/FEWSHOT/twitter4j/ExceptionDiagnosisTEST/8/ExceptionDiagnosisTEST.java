package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExceptionDiagnosisTest {

    private final Throwable throwable;
    private final String[] inclusionFilter;
    private final int expectedStackLineHash;
    private final int expectedLineNumberHash;
    private final String expectedHexString;

    public ExceptionDiagnosisTest(Throwable throwable, String[] inclusionFilter) {
        this.throwable = throwable;
        this.inclusionFilter = inclusionFilter;
        this.expectedStackLineHash = calculateStackLineHash(throwable, inclusionFilter);
        this.expectedLineNumberHash = calculateLineNumberHash(throwable);
        this.expectedHexString = this.expectedStackLineHash + "-" + this.expectedLineNumberHash;
    }

    @Parameters
    public static Object[] data() {
        return new Object[][] {
            {null, new String[0]},
            {new RuntimeException("test"), new String[0]},
            {new RuntimeException("test"), new String[]{"java.lang"}},
            {new RuntimeException("test"), new String[]{"org.apache"}}
        };
    }

    @Test
    public void testExceptionDiagnosisConstruction() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(this.throwable);
        ExceptionDiagnosis diagnosisWithFilter = new ExceptionDiagnosis(this.throwable, this.inclusionFilter);

        assertEquals(this.throwable.getClass().getName(), diagnosis.getClass().getName());
        assertEquals(this.throwable, diagnosis.getTh());
        assertEquals(this.expectedStackLineHash, diagnosis.getStackLineHash());
        assertEquals(this.expectedLineNumberHash, diagnosis.getLineNumberHash());
        assertEquals(this.expectedHexString, diagnosis.asHexString());

        assertEquals(this.throwable.getClass().getName(), diagnosisWithFilter.getClass().getName());
        assertEquals(this.throwable, diagnosisWithFilter.getTh());
        assertEquals(this.expectedStackLineHash, diagnosisWithFilter.getStackLineHash());
        assertEquals(this.expectedLineNumberHash, diagnosisWithFilter.getLineNumberHash());
        assertEquals(this.expectedHexString, diagnosisWithFilter.asHexString());

        // Test for serialization
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        diagnosisWithFilter.writeExternal(ps);
        ExceptionDiagnosis deserializedDiagnosis = new ExceptionDiagnosis();
        deserializedDiagnosis.readExternal(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(this.throwable.getClass().getName(), deserializedDiagnosis.getTh().getClass().getName());
        assertEquals(this.expectedStackLineHash, deserializedDiagnosis.getStackLineHash());
        assertEquals(this.expectedLineNumberHash, deserializedDiagnosis.getLineNumberHash());
        assertEquals(this.expectedHexString, deserializedDiagnosis.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstruction_NullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetters() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(this.throwable, this.inclusionFilter);

        assertEquals(this.throwable, diagnosis.getTh());
        assertEquals(this.expectedStackLineHash, diagnosis.getStackLineHash());
        assertEquals(this.expectedLineNumberHash, diagnosis.getLineNumberHash());
        assertEquals(this.expectedHexString, diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(this.throwable, this.inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(this.throwable, this.inclusionFilter);

        assertTrue(diagnosis1.equals(diagnosis2));
        assertTrue(diagnosis2.equals(diagnosis1));

        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(this.throwable);
        assertFalse(diagnosis1.equals(diagnosis3));
        assertFalse(diagnosis3.equals(diagnosis1));

        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(new RuntimeException("test"), this.inclusionFilter);
        assertFalse(diagnosis1.equals(diagnosis4));
        assertFalse(diagnosis4.equals(diagnosis1));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(this.throwable, this.inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(this.throwable, this.inclusionFilter);

        int hashCode1 = diagnosis1.hashCode();
        int hashCode2 = diagnosis2.hashCode();

        assertEquals(hashCode1, hashCode2);

        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(this.throwable);
        int hashCode3 = diagnosis3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(new RuntimeException("test"), this.inclusionFilter);
        int hashCode4 = diagnosis4.hashCode();

        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(this.throwable, this.inclusionFilter);

        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + this.expectedStackLineHash + ", lineNumberHash="
                + this.expectedLineNumberHash + '}';

        assertEquals(expectedToString, diagnosis.toString());
    }

    private int calculateStackLineHash(Throwable throwable, String[] inclusionFilter) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();

        int stackLineHash = 0;

        for (int i = stackTrace.length - 1; i >= 0; --i) {
            final StackTraceElement line = stackTrace[i];
            for (final String filter : inclusionFilter) {
                if (line.getClassName().startsWith(filter)) {
                    int hash = line.getClassName().hashCode() + line.getMethodName().hashCode();
                    stackLineHash = 31 * stackLineHash + hash;
                    break;
                }
            }
        }

        return stackLineHash;
    }

    private int calculateLineNumberHash(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();

        int lineNumberHash = 0;

        for (final StackTraceElement line : stackTrace) {
            lineNumberHash = 31 * lineNumberHash + line.getLineNumber();
        }

        return lineNumberHash;
    }
}