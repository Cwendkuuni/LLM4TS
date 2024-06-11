
import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", null, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertNull(probeInformation.getTargetType());

        probeInformation = new ProbeInformation("TargetName", "", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("", probeInformation.getTargetType());

        probeInformation = new ProbeInformation("TargetName", "TargetType@#%^&", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("TargetType@#%^&", probeInformation.getTargetType());

        ProbeInformation probeInformationValid = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("TargetType", probeInformationValid.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", null, "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertNull(probeInformation.getBmName());

        probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("", probeInformation.getBmName());

        probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName@#%^&", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("BMName@#%^&", probeInformation.getBmName());

        ProbeInformation probeInformationValid = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("BMName", probeInformationValid.getBmName());
    }

    // Test other methods similarly

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArguments() {
        new ProbeInformation(null, null, null, null, null, null, 0, null);
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("http://localhost:8080/measure?type=TargetType&name=TargetName&class=BMClass&name=BMName", probeInformation.getMeasureURI());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        assertEquals("ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}", probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation3 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 1, "XmlRpcUrl2");

        assertTrue(probeInformation1.equals(probeInformation1));
        assertTrue(probeInformation1.equals(probeInformation2));
        assertFalse(probeInformation1.equals(probeInformation3));
        assertFalse(probeInformation1.equals(null));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation3 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 1, "XmlRpcUrl2");

        assertEquals(probeInformation1.hashCode(), probeInformation1.hashCode());
        assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
        assertNotEquals(probeInformation1.hashCode(), probeInformation3.hashCode());
    }
}
