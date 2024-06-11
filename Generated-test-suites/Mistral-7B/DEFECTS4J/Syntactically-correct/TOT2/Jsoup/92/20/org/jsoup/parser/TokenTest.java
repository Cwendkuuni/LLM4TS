
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        String nameBefore = doctype.getName();
        String pubSysKeyBefore = doctype.getPubSysKey();
        String publicIdentifierBefore = doctype.getPublicIdentifier();
        String systemIdentifierBefore = doctype.getSystemIdentifier();
        boolean forceQuirksBefore = doctype.isForceQuirks();

        doctype.reset();

        String nameAfter = doctype.getName();
        String pubSysKeyAfter = doctype.getPubSysKey();
        String publicIdentifierAfter = doctype.getPublicIdentifier();
        String systemIdentifierAfter = doctype.getSystemIdentifier();
        boolean forceQuirksAfter = doctype.isForceQuirks();

        assertEquals("Reset should return a new instance", null, nameBefore);
        assertEquals("Reset should clear name", null, nameAfter);
        assertEquals("Reset should clear pubSysKey", null, pubSysKeyBefore);
        assertEquals("Reset should clear publicIdentifier", null, publicIdentifierBefore);
        assertEquals("Reset should clear systemIdentifier", null, systemIdentifierBefore);
        assertFalse("Reset should reset forceQuirks to false", forceQuirksBefore);
        assertTrue("Reset should reset forceQuirks to false", !forceQuirksAfter);
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        String tagNameBefore = startTag.name();
        Attributes attributesBefore = startTag.getAttributes();

        startTag.reset();

        String tagNameAfter = startTag.name();
        Attributes attributesAfter = startTag.getAttributes();

        assertNull("Reset should return a new instance", tagNameBefore);
        assertNull("Reset should clear tagName", tagNameAfter);
        assertNull("Reset should clear attributes", attributesBefore);
        assertNull("Reset should clear attributes", attributesAfter);
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        String tagNameBefore = endTag.name();

        endTag.reset();

        String tagNameAfter = endTag.name();

        assertNull("Reset should return a new instance", tagNameBefore);
        assertNull("Reset should clear tagName", tagNameAfter);
    }

    @Test
    public void testTagNewAttribute() {
        StartTag startTag = new StartTag();
        startTag.newAttribute().name("testName").value("testValue");

        assertEquals("New attribute should be correctly added", "testValue", startTag.getAttributes().get("testName"));
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.newAttribute().name("testName").value("testValue");

        startTag.finaliseTag();

        assertEquals("Finalise tag should keep the attribute", "testValue", startTag.getAttributes().get("testName"));
    }
}