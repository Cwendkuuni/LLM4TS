import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = "TargetType1";
        String actual = probeInformation.getTargetType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = "BMName1";
        String actual = probeInformation.getBmName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = "ProbeDescription1";
        String actual = probeInformation.getProbeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = "TargetName1";
        String actual = probeInformation.getTargetName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = "BMClass1";
        String actual = probeInformation.getBmClass();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = "BMDescription1";
        String actual = probeInformation.getBmDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        int expected = 1;
        int actual = probeInformation.getPrecision();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = Const.createMeasureURI("TargetType1", "TargetName1", "BMClass1", "BMName1");
        String actual = probeInformation.getMeasureURI();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = "XmlRpcUrl1";
        String actual = probeInformation.getXmlRpcUrl();
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        assertNotEquals(probeInformation, null);
    }

    @Test
    public void testEqualsSameObject() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = probeInformation1;
        assertTrue(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCodeSameObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        int expected = probeInformation1.hashCode();
        int actual = probeInformation2.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        int expected1 = probeInformation1.hashCode();
        int expected2 = probeInformation2.hashCode();
        assertNotEquals(expected1, expected2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTargetName() {
        new ProbeInformation(null, "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTargetType() {
        new ProbeInformation("TargetName1", null, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmClass() {
        new ProbeInformation("TargetName1", "TargetType1", null, "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmName() {
        new ProbeInformation("TargetName1", "TargetType1", "BMClass1", null, "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmDescription() {
        new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", null, "ProbeDescription1", 1, "XmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullProbeDescription() {
        new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", null, 1, "XmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativePrecision() {
        new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", -1, "XmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyXmlRpcUrl() {
        new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "");
    }
}