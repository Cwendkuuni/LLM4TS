package de.beiri22.stringincrementor.test;

import de.beiri22.stringincrementor.*;
import org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b, false);
        Assert.assertEquals("", result.getAbsolute());
        Assert.assertEquals(0, result.linksCount());
    }

    @Test
    public void testDiffIdenticalStrings() {
        String a = "foo";
        String b = "foo";
        RelativeString result = StringIncrementor.diff(a, b, false);
        Assert.assertEquals("", result.getAbsolute());
        Assert.assertEquals(0, result.linksCount());
    }

    @Test
    public void testDiffSingleCharacterDifference() {
        String a = "foo";
        String b = "bar";
        RelativeString result = StringIncrementor.diff(a, b, false);
        Assert.assertEquals("b", result.getAbsolute());
        Assert.assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        Assert.assertEquals(1, link.getLen());
        Assert.assertEquals(1, link.getPosNew());
        Assert.assertEquals(3, link.getPosOrig());
    }

    @Test
    public void testDiffMultipleCharacterDifferences() {
        String a = "foo";
        String b = "barbaz";
        RelativeString result = StringIncrementor.diff(a, b, false);
        Assert.assertEquals("barbaz", result.getAbsolute());
        Assert.assertEquals(3, result.linksCount());

        StringLink link1 = result.getLink(0);
        Assert.assertEquals(2, link1.getLen());
        Assert.assertEquals(1, link1.getPosNew());
        Assert.assertEquals(3, link1.getPosOrig());

        StringLink link2 = result.getLink(1);
        Assert.assertEquals(1, link2.getLen());
        Assert.assertEquals(5, link2.getPosNew());
        Assert.assertEquals(5, link2.getPosOrig());

        StringLink link3 = result.getLink(2);
        Assert.assertEquals(2, link3.getLen());
        Assert.assertEquals(7, link3.getPosNew());
        Assert.assertEquals(6, link3.getPosOrig());
    }

    @Test
    public void testDiffVerbose() {
        String a = "foo";
        String b = "barbaz";
        RelativeString result = StringIncrementor.diff(a, b, true);
        Assert.assertEquals("barbaz", result.getAbsolute());
        Assert.assertEquals(3, result.linksCount());

        System.out.print("Links: ");
        for (int i = 0; i < result.linksCount(); ++i) {
            StringLink link = result.getLink(i);
            System.out.print("[" + link.getLen() + "]: ");
            System.out.print("[posNew=" + link.getPosNew() + ", posOrig=" + link.getPosOrig() + "] ");
            System.out.println("@ " + Math.round(link.getPosNew() / b.length() * 1000.0) / 10.0 + "%");
        }
        System.out.println("");
    }

    @Test
    public void testPatchEmptyStrings() {
        String a = "";
        RelativeString r = new RelativeString();
        String patched = StringIncrementor.patch(a, r, false);
        Assert.assertEquals("", patched);
    }

    @Test
    public void testPatchSingleCharacterDifference() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 1, 1));
        String patched = StringIncrementor.patch(a, r, false);
        Assert.assertEquals("bar", patched);
    }

    @Test
    public void testPatchMultipleCharacterDifferences() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 1, 2));
        r.addLink(new StringLink(5, 5, 1));
        r.addLink(new StringLink(7, 6, 2));
        String patched = StringIncrementor.patch(a, r, false);
        Assert.assertEquals("barbaz", patched);
    }

    @Test
    public void testPatchVerbose() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 1, 2));
        r.addLink(new StringLink(5, 5, 1));
        r.addLink(new StringLink(7, 6, 2));
        String patched = StringIncrementor.patch(a, r, true);
        Assert.assertEquals("barbaz", patched);

        System.out.println("Patched: " + patched);
        System.out.println("Links: ");
        for (int i = 0; i < r.linksCount(); ++i) {
            StringLink link = r.getLink(i);
            System.out.print("[" + link.getLen() + "]: ");
            System.out.print("[posNew=" + link.getPosNew() + ", posOrig=" + link.getPosOrig() + "] ");
            System.out.println("add: " + a.substring(link.getPosOrig(), link.getPosOrig() + link.getLen()));
        }
        System.out.println("");
    }
}