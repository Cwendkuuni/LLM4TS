package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabledFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);

        // Test disabled by default
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test enabling features individually
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase generatorEnabled = new GeneratorBase(features, null);
        assertTrue(generatorEnabled.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorEnabled.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generatorEnabled.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test enabling multiple features at once
        int featuresEnabled = Feature.DEFAULT_FEATURE_SET.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        GeneratorBase generatorEnabledAll = new GeneratorBase(featuresEnabled, null);
        assertTrue(generatorEnabledAll.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorEnabledAll.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generatorEnabledAll.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertTrue(generatorEnabledAll.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        // ... add other features as needed

        // Test disabling features individually
        int featuresDisabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.ESCAPE_NON_ASCII.getMask();
        GeneratorBase generatorDisabled = new GeneratorBase(featuresDisabled, null);
        assertFalse(generatorDisabled.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generatorDisabled.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        // ... add other features as needed

        // Test disabling all features
        int featuresDisabledAll = ~Feature.DEFAULT_FEATURE_SET.getMask();
        GeneratorBase generatorDisabledAll = new GeneratorBase(featuresDisabledAll, null);
        assertFalse(generatorDisabledAll.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generatorDisabledAll.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generatorDisabledAll.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        // ... add other features as needed
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        int mask = generator.getFeatureMask();
        assertEquals(0, mask);

        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase generatorEnabled = new GeneratorBase(features, null);
        int maskEnabled = generatorEnabled.getFeatureMask();
        assertEquals(features, maskEnabled);
    }

    @Test
    public void testEnableFeature() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);

        // Test enabling features that are disabled by default
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test enabling features that are already enabled
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test enabling multiple features at once
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        generator.enable(features);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test enabling a feature that is already enabled by a parent feature
        int features2 = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator.enable(features2);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling a feature that is not enabled
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling a feature that is already disabled
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling multiple features at once
        int featuresDisabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.ESCAPE_NON_ASCII.getMask();
        generator.disable(featuresDisabled);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling all features
        int featuresDisabledAll = ~Feature.DEFAULT_FEATURE_SET.getMask();
        generator.disable(featuresDisabledAll);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        // ... add other features as needed
    }

    @Test
    public void testDisableFeature() throws IOException {
        GeneratorBase generator = new GeneratorBase(Feature.DEFAULT_FEATURE_SET.getMask(), null);

        // Test disabling features that are enabled by default
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test disabling features that are already disabled
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test disabling multiple features at once
        int featuresDisabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.ESCAPE_NON_ASCII.getMask();
        generator.disable(featuresDisabled);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling a feature that is not enabled
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling all features
        int featuresDisabledAll = ~Feature.DEFAULT_FEATURE_SET.getMask();
        generator.disable(featuresDisabledAll);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        // ... add other features as needed
    }

    @Test
    public void testSetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);

        // Test setting a feature mask that is the same as the current one
        int features = generator.getFeatureMask();
        generator.setFeatureMask(features);
        assertEquals(features, generator.getFeatureMask());

        // Test setting a feature mask that is different from the current one
        int featuresEnabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        generator.setFeatureMask(featuresEnabled);
        assertEquals(featuresEnabled, generator.getFeatureMask());

        // Test setting a feature mask that disables features that were enabled
        int featuresDisabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.ESCAPE_NON_ASCII.getMask();
        generator.setFeatureMask(featuresDisabled);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test setting a feature mask that enables features that were disabled
        int featuresEnabled2 = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask();
        generator.setFeatureMask(featuresEnabled2);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test setting a feature mask that disables all features
        int featuresDisabledAll = ~Feature.DEFAULT_FEATURE_SET.getMask();
        generator.setFeatureMask(featuresDisabledAll);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        // ... add other features as needed
    }

    @Test
    public void testOverrideStdFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(Feature.DEFAULT_FEATURE_SET.getMask(), null);

        // Test overriding features that are disabled by default
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        generator.overrideStdFeatures(features, Feature.DEFAULT_FEATURE_SET.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test overriding features that are already enabled
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.DEFAULT_FEATURE_SET.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test overriding multiple features at once
        int featuresEnabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        int featuresDisabled = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.ESCAPE_NON_ASCII.getMask();
        generator.overrideStdFeatures(featuresEnabled, featuresDisabled);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test overriding all features
        int featuresDisabledAll = ~Feature.DEFAULT_FEATURE_SET.getMask();
        generator.overrideStdFeatures(featuresDisabledAll, Feature.DEFAULT_FEATURE_SET.getMask());
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    // Add more test cases for other methods and features as needed
}