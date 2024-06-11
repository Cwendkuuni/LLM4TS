package org.apache.commons.compress.archivers.zip.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.compress.archivers.zip.UnixStat;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testConstants() {
        // Testing constant values through instantiation and reflection
        UnixStat unixStat = new UnixStat() {
            // to suppress "cannot instantiate interface: org.apache.commons.compress.archivers.zip.UnixStat"
            // error, we use an anonymous inner class
        };
        assertEquals(07777, unixStat.PERM_MASK);
        assertEquals(0120000, unixStat.LINK_FLAG);
        assertEquals(0100000, unixStat.FILE_FLAG);
        assertEquals(040000, unixStat.DIR_FLAG);
        assertEquals(0777, unixStat.DEFAULT_LINK_PERM);
        assertEquals(0755, unixStat.DEFAULT_DIR_PERM);
        assertEquals(0644, unixStat.DEFAULT_FILE_PERM);

        // Testing constant values through reflection
        Class<UnixStat> unixStatClass = UnixStat.class;
        assertEquals(07777, unixStatClass.getField("PERM_MASK").getInt(null));
        assertEquals(0120000, unixStatClass.getField("LINK_FLAG").getInt(null));
        assertEquals(0100000, unixStatClass.getField("FILE_FLAG").getInt(null));
        assertEquals(040000, unixStatClass.getField("DIR_FLAG").getInt(null));
        assertEquals(0777, unixStatClass.getField("DEFAULT_LINK_PERM").getInt(null));
        assertEquals(0755, unixStatClass.getField("DEFAULT_DIR_PERM").getInt(null));
        assertEquals(0644, unixStatClass.getField("DEFAULT_FILE_PERM").getInt(null));

        // Testing edge cases and exception handling (to be added in the next expert's proposal)
    }
}
//