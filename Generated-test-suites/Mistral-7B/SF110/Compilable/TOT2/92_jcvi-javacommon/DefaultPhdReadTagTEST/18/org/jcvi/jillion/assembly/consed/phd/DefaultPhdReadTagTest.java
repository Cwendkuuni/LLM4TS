package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testTypeGetter() {
        String type = "PhdReadTagType";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        assertEquals(type, phdReadTag.getType());
    }

    // Test cases from Expert 1 and 2
    // ...
}
