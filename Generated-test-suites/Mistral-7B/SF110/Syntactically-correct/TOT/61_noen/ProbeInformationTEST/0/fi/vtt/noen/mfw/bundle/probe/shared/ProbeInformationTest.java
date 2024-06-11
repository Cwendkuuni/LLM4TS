import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInfo = new ProbeInformation("MyTarget", targetType, "MyBMClass", "MyBMName", "MyBMDescription", "MyProbeName", 5, "http://localhost:8080/xmlrpc");
        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "Temperature";
        ProbeInformation probeInfo = new ProbeInformation("MyTarget", "Sensor", "MyBMClass", bmName, "MyBMDescription", "MyProbeName", 5, "http://localhost:8080/xmlrpc");
        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testToString() {
        String targetName = "MyTarget";
        String targetType = "Sensor";
        String bmClass = "MyBMClass";
        String bmName = "Temperature";
        String bmDescription = "This is a BM description";
        String probeName = "MyProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        String expected = String.format("ProbeInformation{targetName='%s', targetType='%s', bmClass='%s', bmName='%s', bmDescription='%s', probeDescription='%s', precision=%d}", targetName, targetType, bmClass, bmName, bmDescription, probeName, precision);
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testGetters_NullValues() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, null);
        assertNull(probeInfo.getTargetName());
        assertNull(probeInfo.getTargetType());
        assertNull(probeInfo.getBmClass());
        assertNull(probeInfo.getBmName());
        assertNull(probeInfo.getBmDescription());
        assertNull(probeInfo.getProbeName());
        assertEquals(0, probeInfo.getPrecision());
        assertNull(probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetters_EmptyStrings() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "", "", 0, "");
        assertEquals("", probeInfo.getTargetName());
        assertEquals("", probeInfo.getTargetType());
        assertEquals("", probeInfo.getBmClass());
        assertEquals("", probeInfo.getBmName());
        assertEquals("", probeInfo.getBmDescription());
        assertEquals("", probeInfo.getProbeName());
        assertEquals(0, probeInfo.getPrecision());
        assertEquals("", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetters_InvalidValues() {
        ProbeInformation probeInfo = new ProbeInformation("MyTarget", "Sensor", null, "MyBMName", null, "MyProbeName", -1, "http://localhost:8080/xmlrpc");
        assertNotNull(probeInfo.getTargetName());
        assertNotNull(probeInfo.getTargetType());
        assertNull(probeInfo.getBmClass());
        assertNotNull(probeInfo.getBmName());
        assertNull(probeInfo.getBmDescription());
        assertNotNull(probeInfo.getProbeName());
        assertEquals(-1, probeInfo.getPrecision());
        assertNotNull(probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        String targetName1 = "MyTarget1";
        String targetType1 = "Sensor1";
        String bmClass1 = "MyBMClass1";
        String bmName1 = "MyBMName1";
        String bmDescription1 = "MyBMDescription1";
        String probeName1 = "MyProbeName1";
        int precision1 = 5;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc1";
        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "MyTarget2";
        String targetType2 = "Sensor2";
        String bmClass2 = "MyBMClass2";
        String bmName2 = "MyBMName2";
        String bmDescription2 = "MyBMDescription2";
        String probeName2 = "MyProbeName2";
        int precision2 = 5;
        String xmlRpcUrl2 = "http://localhost:8080/xmlrpc2";
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        String targetName3 = "MyTarget1";
        String targetType3 = "Sensor1";
        String bmClass3 = "MyBMClass3";
        String bmName3 = "MyBMName3";
        String bmDescription3 = "MyBMDescription3";
        String probeName3 = "MyProbeName3";
        int precision3 = 5;
        String xmlRpcUrl3 = "http://localhost:8080/xmlrpc3";
        ProbeInformation probeInfo3 = new ProbeInformation(targetName3, targetType3, bmClass3, bmName3, bmDescription3, probeName3, precision3, xmlRpcUrl3);

        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo3)); // equal values

        assertFalse(probeInfo1.equals(null));
        assertFalse(probeInfo1.equals(new Object()));
        assertFalse(probeInfo1.equals(new Integer(0)));

        assertFalse(probeInfo1.equals(probeInfo2)); // different values
    }

    @Test
    public void testHashCode() {
        String targetName1 = "MyTarget1";
        String targetType1 = "Sensor1";
        String bmClass1 = "MyBMClass1";
        String bmName1 = "MyBMName1";
        String bmDescription1 = "MyBMDescription1";
        String probeName1 = "MyProbeName1";
        int precision1 = 5;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc1";
        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "MyTarget2";
        String targetType2 = "Sensor2";
        String bmClass2 = "MyBMClass2";
        String bmName2 = "MyBMName2";
        String bmDescription2 = "MyBMDescription2";
        String probeName2 = "MyProbeName2";
        int precision2 = 5;
        String xmlRpcUrl2 = "http://localhost:8080/xmlrpc2";
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertEquals(hashCode1, probeInfo3.hashCode());
    }
}