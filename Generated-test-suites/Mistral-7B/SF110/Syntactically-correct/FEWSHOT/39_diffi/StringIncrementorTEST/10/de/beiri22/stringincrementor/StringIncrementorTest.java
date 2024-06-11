package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class StringIncrementorTest {

    @Test
    public void testDiffEmpty() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testDiffIdentity() {
        String a = "foo";
        String b = "foo";
        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testDiffSingleCharacterInsertion() {
        String a = "foo";
        String b = "fop";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> expectedLinks = new ArrayList<>();
        expectedLinks.add(new StringLink(3, 2, 1));
        assertEquals(expectedLinks.toString(), result.getLinks().toString());
    }

    @Test
    public void testDiffSingleCharacterDeletion() {
        String a = "foo";
        String b = "of";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> expectedLinks = new ArrayList<>();
        expectedLinks.add(new StringLink(-3, 2, 1));
        assertEquals(expectedLinks.toString(), result.getLinks().toString());
    }

    @Test
    public void testDiffMultipleCharacterInsertions() {
        String a = "foo";
        String b = "fobar";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> expectedLinks = new ArrayList<>();
        expectedLinks.add(new StringLink(3, 1, 1));
        expectedLinks.add(new StringLink(5, 4, 2));
        assertEquals(expectedLinks.toString(), result.getLinks().toString());
    }

    @Test
    public void testDiffMultipleCharacterDeletions() {
        String a = "foobar";
        String b = "foo";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> expectedLinks = new ArrayList<>();
        expectedLinks.add(new StringLink(-3, 3, 3));
        expectedLinks.add(new StringLink(-1, 5, 2));
        assertEquals(expectedLinks.toString(), result.getLinks().toString());
    }

    @Test
    public void testDiffSingleCharacterSubstitution() {
        String a = "foo";
        String b = "opf";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> expectedLinks = new ArrayList<>();
        expectedLinks.add(new StringLink(0, 1, 1));
        assertEquals(expectedLinks.toString(), result.getLinks().toString());
    }

    @Test
    public void testDiffMultipleCharacterSubstitutions() {
        String a = "foobar";
        String b = "quuxbar";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> expectedLinks = new ArrayList<>();
        expectedLinks.add(new StringLink(-3, 3, 3));
        expectedLinks.add(new StringLink(-1, 5, 3));
        expectedLinks.add(new StringLink(5, 8, 3));
        assertEquals(expectedLinks.toString(), result.getLinks().toString());
    }

    @Test
    public void testDiffVerbose() {
        String a = "foobar";
        String b = "quuxbar";
        RelativeString result = StringIncrementor.diff(a, b, true);
        List<StringLink> expectedLinks = new ArrayList<>();
        expectedLinks.add(new StringLink(-3, 3, 3));
        expectedLinks.add(new StringLink(-1, 5, 3));
        expectedLinks.add(new StringLink(5, 8, 3));

        StringBuilder outContent = new StringBuilder();
        PrintStream out = new ByteArrayOutputStream();
        System.setOut(out);

        StringIncrementor.diff(a, b, true);

        String expectedOutput = "@ 33.33333333333333333%\n" +
                "Adding 3 absolute Bytes from pos#0\n" +
                "Adding Link: -3 3 3\n" +
                "Adding Link: -1 5 3\n" +
                "Adding Link: 5 8 3\n" +
                "foobar -> quuxbar\n";

        assertEquals(expectedOutput, out.toString());

        System.setOut(System.out);

        assertEquals(expectedLinks.toString(), result.getLinks().toString());
    }

    @Test
    public void testPatchEmpty() {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatchIdentity() {
        String a = "foo";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("foo", result);
    }

    @Test
    public void testPatchSingleCharacterInsertion() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 2, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("fop", result);
    }

    @Test
    public void testPatchSingleCharacterDeletion() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(-3, 2, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("of", result);
    }

    @Test
    public void testPatchMultipleCharacterInsertions() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 1, 1));
        r.addLink(new StringLink(5, 4, 2));
        String result = StringIncrementor.patch(a, r);
        assertEquals("fobar", result);
    }

    @Test
    public void testPatchMultipleCharacterDeletions() {
        String a = "foobar";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(-3, 3, 3));
        r.addLink(new StringLink(-1, 5, 2));
        String result = StringIncrementor.patch(a, r);
        assertEquals("foo", result);
    }

    @Test
    public void testPatchMultipleCharacterSubstitutions() {
        String a = "foobar";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(-3, 3, 3));
        r.addLink(new StringLink(-1, 5, 2));
        r.addLink(new StringLink(5, 8, 3));
        String result = StringIncrementor.patch(a, r);
        assertEquals("quuxbar", result);
    }
}