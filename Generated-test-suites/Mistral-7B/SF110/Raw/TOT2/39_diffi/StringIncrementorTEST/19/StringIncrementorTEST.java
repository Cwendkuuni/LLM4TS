
package de.beiri22.stringincrementor;

import org.junit.Test;
import org.junit.Assert.*;
import static org.mockito.Mockito.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import java.io.*;
import java.lang.reflect.Method;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcabcdefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(new StringLink(0, 0, 6), new StringLink(7, 6, 1), new StringLink(13, 12, 1), new StringLink(14, 13, 1)), result);
    }

    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testDiffVerboseEmptyStrings() {
        String a = "";
        String b = "";
        String expectedVerboseOutput = "";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(new RelativeString(), result);
        // Mocking System.out to check the expected verbose output
        StringIncrementor.diff(a, b, mockStatic(System.out.class));
        verify(System.out).println("");
    }

    @Test
    public void testPatch() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString(new StringLink(0, 0, 6), new StringLink(7, 6, 1), new StringLink(13, 12, 1), new StringLink(14, 13, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("abcabcdefghijklmnopqrstuvwxyz", result);
    }

    @Test
    public void testPatchEmptyRelativeString() {
        String a = "abcabcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("abcabcdefghijklmnopqrstuvwxyz", result);
    }

    @Test
    public void testDiffVerbose() throws Exception {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcabcdefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(new RelativeString(new StringLink(0, 0, 6), new StringLink(7, 6, 1), new StringLink(13, 12, 1), new StringLink(14, 13, 1)), result);
        // Testing the verbose output by checking the System.out print statements using Mockito
        StringIncrementor.diff(a, b, mockStatic(System.out.class));
        verify(System.out, times(1)).println("Creating index...");
        verify(System.out, times(1)).println("Searching links...");
        Method diffMethod = StringIncrementor.class.getDeclaredMethod("diff", String.class, String.class, boolean.class);
        diffMethod.setAccessible(true);
        diffMethod.invoke(null, a, b, true);
        verify(System.out, times(1)).println("@ 50.0%");
    }
}
