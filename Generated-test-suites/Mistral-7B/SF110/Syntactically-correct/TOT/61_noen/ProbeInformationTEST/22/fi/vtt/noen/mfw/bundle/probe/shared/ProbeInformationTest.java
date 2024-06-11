package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInfo = new ProbeInformation("Device1", targetType, "Temperature", "TempSensor1", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "Temp";
        ProbeInformation probeInfo = new ProbeInformation("Device1", "Sensor", "Temperature", bmName, "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testToString() {
        String expectedString = "ProbeInformation{targetName='Device1', targetType='Sensor', bmClass='Temperature', bmName='Temp', bmDescription='Temperature sensor for Device1', probeDescription='TempSensor1', precision=2}";
        ProbeInformation probeInfo = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        assertEquals(expectedString, probeInfo.toString());
    }

    @Test
    public void testGettersWithNullValues() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, null);
        assertEquals(null, probeInfo.getTargetName());
        assertEquals(null, probeInfo.getTargetType());
        assertEquals(null, probeInfo.getBmClass());
        assertEquals(null, probeInfo.getBmName());
        assertEquals(null, probeInfo.getBmDescription());
        assertEquals(null, probeInfo.getProbeName());
        assertEquals(0, probeInfo.getPrecision());
        assertEquals(null, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "Sensor";
        String targetName = "Device1";
        String bmClass = "Temperature";
        String bmName = "Temp";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, null, 2, null);
        String expectedURI = "http://localhost:8080/mfw/probe/" + targetType + "/" + targetName + "/" + bmClass + "/" + bmName;
        assertEquals(expectedURI, probeInfo.getMeasureURI());
    }

    @Test
    public void testEqualsWithSameObject() {
        ProbeInformation probeInfo1 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        ProbeInformation probeInfo2 = probeInfo1;
        assertTrue(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        ProbeInformation probeInfo1 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        ProbeInformation probeInfo2 = new ProbeInformation("Device2", "Sensor", "Temperature", "Temp", "Temperature sensor for Device2", "TempSensor2", 2, "http://localhost:8080/rpc");
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsWithDifferentPrecision() {
        ProbeInformation probeInfo1 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        ProbeInformation probeInfo2 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 3, "http://localhost:8080/rpc");
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsWithDifferentBMClass() {
        ProbeInformation probeInfo1 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        ProbeInformation probeInfo2 = new ProbeInformation("Device1", "Sensor", "Pressure", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsWithDifferentBMName() {
        ProbeInformation probeInfo1 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        ProbeInformation probeInfo2 = new ProbeInformation("Device1", "Sensor", "Temperature", "Pressure", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsWithDifferentBMDescription() {
        ProbeInformation probeInfo1 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        ProbeInformation probeInfo2 = new ProbeInformation("Device1", "Sensor", "Temperature", "Temp", "Different temperature sensor for Device1", "TempSensor1", 2, "http://localhost:8080/rpc");
        assertFalse(probeInfo1.equals(probeInfo2));
    }
}