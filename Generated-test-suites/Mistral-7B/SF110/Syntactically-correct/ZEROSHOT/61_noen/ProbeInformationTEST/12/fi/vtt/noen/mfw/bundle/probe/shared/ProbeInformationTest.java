package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNotNull(probeInformation.getTargetName());
        assertEquals(targetName, probeInformation.getTargetName());

        assertNotNull(probeInformation.getTargetType());
        assertEquals(targetType, probeInformation.getTargetType());

        assertNotNull(probeInformation.getBmClass());
        assertEquals(bmClass, probeInformation.getBmClass());

        assertNotNull(probeInformation.getBmName());
        assertEquals(bmName, probeInformation.getBmName());

        assertNotNull(probeInformation.getBmDescription());
        assertEquals(bmDescription, probeInformation.getBmDescription());

        assertNotNull(probeInformation.getProbeName());
        assertEquals(probeDescription, probeInformation.getProbeName());

        assertEquals(precision, probeInformation.getPrecision());

        String measureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(measureURI, probeInformation.getMeasureURI());

        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertSame(targetName, probeInformation.getTargetName());
        assertSame(targetType, probeInformation.getTargetType());
        assertSame(bmClass, probeInformation.getBmClass());
        assertSame(bmName, probeInformation.getBmName());
        assertSame(bmDescription, probeInformation.getBmDescription());
        assertSame(probeDescription, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());

        // Test getters with null values
        ProbeInformation probeInformationNull = new ProbeInformation(null, null, null, null, null, null, 0, null);
        assertNull(probeInformationNull.getTargetName());
        assertNull(probeInformationNull.getTargetType());
        assertNull(probeInformationNull.getBmClass());
        assertNull(probeInformationNull.getBmName());
        assertNull(probeInformationNull.getBmDescription());
        assertNull(probeInformationNull.getProbeName());
    }

    @Test
    public void testEquals() {
        String targetName1 = "targetName1";
        String targetName2 = "targetName2";
        String targetType1 = "targetType1";
        String targetType2 = "targetType2";
        String bmClass1 = "bmClass1";
        String bmClass2 = "bmClass2";
        String bmName1 = "bmName1";
        String bmName2 = "bmName2";
        String bmDescription1 = "bmDescription1";
        String bmDescription2 = "bmDescription2";
        String probeDescription1 = "probeDescription1";
        String probeDescription2 = "probeDescription2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);
        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        // Test equal objects
        assertTrue(probeInformation1.equals(probeInformation3));

        // Test not equal objects with different values
        assertFalse(probeInformation1.equals(probeInformation2));

        // Test not equal objects with different classes
        ProbeInformation probeInformation4 = new Object() {
            private final String targetName;
            private final String targetType;
            private final String bmClass;
            private final String bmName;
            private final String bmDescription;
            private final String probeName;
            private final int precision;
            private final String xmlRpcUrl;

            public ProbeInformation(final String targetName, final String targetType, final String bmClass, final String bmName, final String bmDescription, final String probeName, final int precision, final String xmlRpcUrl) {
                this.targetName = targetName;
                this.targetType = targetType;
                this.bmClass = bmClass;
                this.bmName = bmName;
                this.bmDescription = bmDescription;
                this.probeName = probeName;
                this.precision = precision;
                this.xmlRpcUrl = xmlRpcUrl;
            }

            // Getters
            public String getTargetName() {
                return this.targetName;
            }

            public String getTargetType() {
                return this.targetType;
            }

            public String getBmClass() {
                return this.bmClass;
            }

            public String getBmName() {
                return this.bmName;
            }

            public String getBmDescription() {
                return this.bmDescription;
            }

            public String getProbeName() {
                return this.probeName;
            }

            public int getPrecision() {
                return this.precision;
            }

            public String getXmlRpcUrl() {
                return this.xmlRpcUrl;
            }

            public String toString() {
                return "ProbeInformation{targetName='" + this.targetName + '\'' + ", targetType='" + this.targetType + '\'' + ", bmClass='" + this.bmClass + '\'' + ", bmName='" + this.bmName + '\'' + ", bmDescription='" + this.bmDescription + '\'' + ", probeDescription='" + this.probeName + '\'' + ", precision=" + this.precision + '}';
            }
        };

        assertFalse(probeInformation1.equals(probeInformation4));
    }

    @Test
    public void testHashCode() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;

        ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();

        assertEquals(hashCode1, hashCode2);

        // Test different objects with the same values
        ProbeInformation probeInformation3 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
        int hashCode3 = probeInformation3.hashCode();

        assertNotSame(hashCode1, hashCode3);
    }
}