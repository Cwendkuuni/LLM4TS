package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.apache.commons.compress.archivers.zip.UnixStat.*;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int expected = 07777;
        int actual = PERM_MASK;
        assertEquals(expected, actual);

        assertEquals(0777, (PERM_MASK & 0777));
        assertEquals(0, PERM_MASK << 8);
        // Add test for IllegalArgumentException here
    }

    @Test
    public void testLinkFlag() {
        int expected = 0120000;
        int actual = LINK_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void testFileFlag() {
        int expected = 0100000;
        int actual = FILE_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void testDirFlag() {
        int expected = 040000;
        int actual = DIR_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int expected = 0777;
        int actual = DEFAULT_LINK_PERM;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int expected = 0755;
        int actual = DEFAULT_DIR_PERM;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int expected = 0644;
        int actual = DEFAULT_FILE_PERM;
        assertEquals(expected, actual);
    }
}