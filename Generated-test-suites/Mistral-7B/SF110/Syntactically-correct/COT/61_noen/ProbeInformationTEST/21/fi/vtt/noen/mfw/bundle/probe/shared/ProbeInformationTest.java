import org.junit.Test;
import org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", targetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, "XMLRpcUrl");
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(targetType, actualTargetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = "BMName";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", bmName, "BMDescription", "ProbeDescription", 5, "XMLRpcUrl");
        String actualBmName = probeInformation.getBmName();
        assertEquals(bmName, actualBmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = "ProbeDescription";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", "BMName", "BMDescription", probeName, 5, "XMLRpcUrl");
        String actualProbeName = probeInformation.getProbeName();
        assertEquals(probeName, actualProbeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = "ProbeName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "Sensor", "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, "XMLRpcUrl");
        String actualTargetName = probeInformation.getTargetName();
        assertEquals(targetName, actualTargetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "BMClass";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", bmClass, "BMName", "BMDescription", "ProbeDescription", 5, "XMLRpcUrl");
        String actualBmClass = probeInformation.getBmClass();
        assertEquals(bmClass, actualBmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "BMDescription";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", "BMName", bmDescription, "ProbeDescription", 5, "XMLRpcUrl");
        String actualBmDescription = probeInformation.getBmDescription();
        assertEquals(bmDescription, actualBmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = 5;
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", "BMName", "BMDescription", "ProbeDescription", precision, "XMLRpcUrl");
        int actualPrecision = probeInformation.getPrecision();
        assertEquals(precision, actualPrecision);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "Sensor";
        String targetName = "ProbeName";
        String bmClass = "BMClass";
        String bmName = "BMName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "BMDescription", "ProbeDescription", 5, "XMLRpcUrl");
        String expectedMeasureURI = "http://localhost:8080/mfw/measure?type=" + targetType + "&name=" + targetName + "&class=" + bmClass + "&name=" + bmName;
        String actualMeasureURI = probeInformation.getMeasureURI();
        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testGetXmlRpcUrl_whenUrlIsEmptyString_thenReturnEmptyString() {
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, "");
        String actualXmlRpcUrl = probeInformation.getXmlRpcUrl();
        assertEquals("", actualXmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testGetXmlRpcUrl_whenBmClassIsNull_thenThrowNullPointerException() {
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", null, "BMName", "BMDescription", "ProbeDescription", 5, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testGetXmlRpcUrl_whenBmNameIsNull_thenThrowNullPointerException() {
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", null, "BMDescription", "ProbeDescription", 5, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testGetXmlRpcUrl_whenBmDescriptionIsNull_thenThrowNullPointerException() {
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", "BMName", null, "ProbeDescription", 5, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testGetXmlRpcUrl_whenProbeNameIsNull_thenThrowNullPointerException() {
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", "Sensor", "BMClass", "BMName", "BMDescription", null, 5, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testGetXmlRpcUrl_whenTargetNameIsNull_thenThrowNullPointerException() {
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(null, "Sensor", "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testGetXmlRpcUrl_whenTargetTypeIsNull_thenThrowNullPointerException() {
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", null, "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, xmlRpcUrl);
    }

    @Test
    public void testToString() {
        String targetName = "ProbeName";
        String targetType = "Sensor";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeDescription";
        int precision = 5;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        String expectedString = "ProbeInformation{targetName='ProbeName', targetType='Sensor', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=5}";
        String actualString = probeInformation.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals_whenProbeInformationObjectsAreEqual_thenReturnTrue() {
        String targetName1 = "ProbeName1";
        String targetType1 = "Sensor1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeDescription1";
        int precision1 = 5;
        String xmlRpcUrl1 = "XMLRpcUrl1";
        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "ProbeName1";
        String targetType2 = "Sensor1";
        String bmClass2 = "BMClass1";
        String bmName2 = "BMName1";
        String bmDescription2 = "BMDescription1";
        String probeName2 = "ProbeDescription1";
        int precision2 = 5;
        String xmlRpcUrl2 = "XMLRpcUrl1";
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        boolean actualResult = probeInformation1.equals(probeInformation2);
        assertTrue(actualResult);
    }

    @Test
    public void testEquals_whenProbeInformationObjectsAreNotEqual_thenReturnFalse() {
        String targetName1 = "ProbeName1";
        String targetType1 = "Sensor1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeDescription1";
        int precision1 = 5;
        String xmlRpcUrl1 = "XMLRpcUrl1";
        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "ProbeName2";
        String targetType2 = "Sensor2";
        String bmClass2 = "BMClass2";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeName2 = "ProbeDescription2";
        int precision2 = 6;
        String xmlRpcUrl2 = "XMLRpcUrl2";
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        boolean actualResult = probeInformation1.equals(probeInformation2);
        assertFalse(actualResult);
    }

    @Test
    public void testHashCode() {
        String targetName = "ProbeName";
        String targetType = "Sensor";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeDescription";
        int precision = 5;
        int expectedHashCode = targetName.hashCode() + 31 * (targetType != null ? targetType.hashCode() : 0)
                + 31 * (bmClass != null ? bmClass.hashCode() : 0)
                + 31 * (bmName != null ? bmName.hashCode() : 0)
                + 31 * (bmDescription != null ? bmDescription.hashCode() : 0)
                + 31 * probeName.hashCode() + precision;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "");
        int actualHashCode = probeInformation.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}