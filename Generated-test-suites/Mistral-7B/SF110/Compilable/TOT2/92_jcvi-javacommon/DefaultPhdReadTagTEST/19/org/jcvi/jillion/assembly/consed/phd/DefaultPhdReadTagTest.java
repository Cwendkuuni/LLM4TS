package org.jcvi.jillion.assembly.consed.phd;
import org.junit.Test;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "DNA";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        assertEquals(type, phdReadTag.getType());
    }
}