// Add any additional test cases as needed

package fi.vtt.noen.mfw.bundle.probe.shared.test;

import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String expectedTargetType = "ExpectedTargetType";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", expectedTargetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualTargetType = probeInfo.getTargetType();

        assertEquals(expectedTargetType, actualTargetType);
    }

    @Test
    public void testGetBmName() {
        String expectedBmName = "ExpectedBMName";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TargetType", "BMClass", expectedBmName, "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualBmName = probeInfo.getBmName();

        assertEquals(expectedBmName, actualBmName);
    }

    @Test
    public void testToString() {
        String expectedString = "ProbeInformation{targetName='TestTargetName', targetType='TargetType', bmClass='BMClass', bmName='ExpectedBMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "ExpectedBMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualString = probeInfo.toString();

        assertEquals(expectedString, actualString);
    }

    // Add more test cases as necessary

    @Test
    public void testGettersWithNullValues() {
        String nullTargetName = null;
        String nullTargetType = null;
        String nullBMClass = null;
        String nullBmName = null;
        String nullBmDescription = null;
        String nullProbeDescription = null;

        ProbeInformation probeInfoWithNullTargetName = new ProbeInformation(nullTargetName, "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualTargetNameWithNull = probeInfoWithNullTargetName.getTargetName();
        String expectedTargetNameWithNull = null;

        assertEquals(expectedTargetNameWithNull, actualTargetNameWithNull);

        ProbeInformation probeInfoWithNullTargetType = new ProbeInformation("TestTargetName", nullTargetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualTargetTypeWithNull = probeInfoWithNullTargetType.getTargetType();
        String expectedTargetTypeWithNull = null;

        assertEquals(expectedTargetTypeWithNull, actualTargetTypeWithNull);

        ProbeInformation probeInfoWithNullBmName = new ProbeInformation("TestTargetName", "TargetType", nullBMClass, nullBmName, "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualBmNameWithNull = probeInfoWithNullBmName.getBmName();
        String expectedBmNameWithNull = null;

        assertEquals(expectedBmNameWithNull, actualBmNameWithNull);

        ProbeInformation probeInfoWithNullBmDescription = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", nullBmDescription, "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualBmDescriptionWithNull = probeInfoWithNullBmDescription.getBmDescription();
        String expectedBmDescriptionWithNull = null;

        assertEquals(expectedBmDescriptionWithNull, actualBmDescriptionWithNull);

        ProbeInformation probeInfoWithNullProbeDescription = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", "BMDescription", nullProbeDescription, 1, "http://localhost:8080/xmlrpc");
        String actualProbeDescriptionWithNull = probeInfoWithNullProbeDescription.getProbeDescription();
        String expectedProbeDescriptionWithNull = null;

        assertEquals(expectedProbeDescriptionWithNull, actualProbeDescriptionWithNull);
    }

    @Test
    public void testGettersWithEmptyStrings() {
        String emptyTargetName = "";
        String emptyTargetType = "";
        String emptyBMClass = "";
        String emptyBmName = "";
        String emptyBmDescription = "";
        String emptyProbeDescription = "";

        ProbeInformation probeInfoWithEmptyTargetName = new ProbeInformation(emptyTargetName, "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualTargetNameWithEmpty = probeInfoWithEmptyTargetName.getTargetName();
        String expectedTargetNameWithEmpty = "";

        assertEquals(expectedTargetNameWithEmpty, actualTargetNameWithEmpty);

        ProbeInformation probeInfoWithEmptyTargetType = new ProbeInformation("TestTargetName", emptyTargetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualTargetTypeWithEmpty = probeInfoWithEmptyTargetType.getTargetType();
        String expectedTargetTypeWithEmpty = "";

        assertEquals(expectedTargetTypeWithEmpty, actualTargetTypeWithEmpty);

        ProbeInformation probeInfoWithEmptyBmName = new ProbeInformation("TestTargetName", "TargetType", "BMClass", emptyBmName, "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualBmNameWithEmpty = probeInfoWithEmptyBmName.getBmName();
        String expectedBmNameWithEmpty = "";

        assertEquals(expectedBmNameWithEmpty, actualBmNameWithEmpty);

        ProbeInformation probeInfoWithEmptyBmDescription = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", emptyBmDescription, "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualBmDescriptionWithEmpty = probeInfoWithEmptyBmDescription.getBmDescription();
        String expectedBmDescriptionWithEmpty = "";

        assertEquals(expectedBmDescriptionWithEmpty, actualBmDescriptionWithEmpty);

        ProbeInformation probeInfoWithEmptyProbeDescription = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", "BMDescription", emptyProbeDescription, 1, "http://localhost:8080/xmlrpc");
        String actualProbeDescriptionWithEmpty = probeInfoWithEmptyProbeDescription.getProbeDescription();
        String expectedProbeDescriptionWithEmpty = "";

        assertEquals(expectedProbeDescriptionWithEmpty, actualProbeDescriptionWithEmpty);
    }

    @Test
    public void testGettersWithInvalidValues() {
        String invalidTargetName = "InvalidTargetName";
        String invalidTargetType = "InvalidTargetType";
        String invalidBMClass = "InvalidBMClass";
        String invalidBmName = "InvalidBMName";
        String invalidBmDescription = "InvalidBmDescription";
        String invalidProbeDescription = "InvalidProbeDescription";

        ProbeInformation probeInfoWithInvalidTargetName = new ProbeInformation(invalidTargetName, "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualTargetNameWithInvalid = probeInfoWithInvalidTargetName.getTargetName();
        String expectedTargetNameWithInvalid = "InvalidTargetName";

        assertEquals(expectedTargetNameWithInvalid, actualTargetNameWithInvalid);

        ProbeInformation probeInfoWithInvalidTargetType = new ProbeInformation("TestTargetName", invalidTargetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualTargetTypeWithInvalid = probeInfoWithInvalidTargetType.getTargetType();
        String expectedTargetTypeWithInvalid = "InvalidTargetType";

        assertEquals(expectedTargetTypeWithInvalid, actualTargetTypeWithInvalid);

        ProbeInformation probeInfoWithInvalidBmName = new ProbeInformation("TestTargetName", "TargetType", "BMClass", invalidBmName, "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualBmNameWithInvalid = probeInfoWithInvalidBmName.getBmName();
        String expectedBmNameWithInvalid = "InvalidBMName";

        assertEquals(expectedBmNameWithInvalid, actualBmNameWithInvalid);

        ProbeInformation probeInfoWithInvalidBmDescription = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", invalidBmDescription, "ProbeDescription", 1, "http://localhost:8080/xmlrpc");
        String actualBmDescriptionWithInvalid = probeInfoWithInvalidBmDescription.getBmDescription();
        String expectedBmDescriptionWithInvalid = "InvalidBmDescription";

        assertEquals(expectedBmDescriptionWithInvalid, actualBmDescriptionWithInvalid);

        ProbeInformation probeInfoWithInvalidProbeDescription = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", "BMDescription", invalidProbeDescription, 1, "http://localhost:8080/xmlrpc");
        String actualProbeDescriptionWithInvalid = probeInfoWithInvalidProbeDescription.getProbeDescription();
        String expectedProbeDescriptionWithInvalid = "InvalidProbeDescription";

        assertEquals(expectedProbeDescriptionWithInvalid, actualProbeDescriptionWithInvalid);
    }
}