package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.SimpleFormat;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstruction() {
        // Test default constructor
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
        assertNull(emf.registry);

        // Test constructor with pattern
        emf = new ExtendedMessageFormat("{0} {1} {2}");
        assertEquals("{0} {1} {2}", emf.toPattern());
        assertNull(emf.registry);

        // Test constructor with pattern and locale
        emf = new ExtendedMessageFormat("{0} {1} {2}", Locale.US);
        assertEquals("{0} {1} {2}", emf.toPattern());
        assertNull(emf.registry);

        // Test constructor with pattern, locale, and registry
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        emf = new ExtendedMessageFormat("{0} ({1}) {2}", Locale.US, registry);
        assertEquals("{0} ({1}) {2}", emf.toPattern());
        assertEquals(registry, emf.registry);
    }

    @Test
    public void testApplyPattern() {
        // Test with empty pattern
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("{0} {1} {2}");
        assertEquals("{0} {1} {2}", emf.toPattern());
        assertNotSame(registry, emf.registry);

        // Test with existing format in registry
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        emf = new ExtendedMessageFormat("{format.date,date} {0} {1}", Locale.US, registry);
        emf.applyPattern("{format.date,date,time} {0} {1}");
        assertEquals("{format.date,date,time} {0} {1}", emf.toPattern());
        assertEquals(registry, emf.registry);

        // Test with unknown format in registry
        registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        emf = new ExtendedMessageFormat("{format.invalid,date} {0} {1}", Locale.US, registry);
        emf.applyPattern("{format.invalid,date,format} {0} {1}");
        assertEquals("{format.invalid,date,format} {0} {1}", emf.toPattern());
        assertSame(registry, emf.registry);

        // Test with multiple formats in registry
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        registry.put("time", new SimpleDateFormat("HH:mm:ss"));
        emf = new ExtendedMessageFormat("{format.date,date} {format.time,time} {0} {1}", Locale.US, registry);
        emf.applyPattern("{format.date,date,format.time,time,zone,zone_time} {format.time,time,format.date,date} {0} {1}");
        assertEquals("{format.date,date,format.time,time,zone,zone_time} {format.time,time,format.date,date}", emf.toPattern());
        assertEquals(registry, emf.registry);
    }

    @Test
    public void testSetFormat() {
        // Test with unsupported operation
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0} {1}", Locale.US);
        try {
            emf.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            emf.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            Format[] formats = new Format[2];
            formats[0] = new SimpleDateFormat("dd-MM-yyyy");
            emf.setFormats(formats);
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            Format[] formats = new Format[2];
            formats[0] = new SimpleDateFormat("dd-MM-yyyy");
            emf.setFormatsByArgumentIndex(formats);
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("{0} {1}", Locale.US);
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("{0} {1}", Locale.US);
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("{1} {0}", Locale.US);
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("{0} {1}", Locale.US);
        ExtendedMessageFormat emf5 = new ExtendedMessageFormat("{0} {1}", Locale.US, new HashMap<>());
        ExtendedMessageFormat emf6 = new ExtendedMessageFormat("{0} {1}", Locale.US, new HashMap<>());
        ExtendedMessageFormat emf7 = new ExtendedMessageFormat("{0} {1}", Locale.US, new HashMap<>());
        emf7.applyPattern("{format.date,date} {format.time,time} {0} {1}");

        assertEquals(emf1, emf1);
        assertEquals(emf1, emf2);
        assertNotEquals(emf1, emf3);
        assertNotEquals(emf1, emf4);
        assertNotEquals(emf1, emf5);
        assertNotEquals(emf1, emf6);
        assertNotEquals(emf1, emf7);

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat emf8 = new ExtendedMessageFormat("{format.date,date} {0} {1}", Locale.US, registry);
        assertEquals(emf1, emf8);

        registry.put("time", new SimpleDateFormat("HH:mm:ss"));
        ExtendedMessageFormat emf9 = new ExtendedMessageFormat("{format.date,date} {format.time,time} {0} {1}", Locale.US, registry);
        assertEquals(emf8, emf9);

        assertEquals(emf1.hashCode(), emf2.hashCode());
        assertEquals(emf1.hashCode(), emf8.hashCode());
        assertNotEquals(emf1.hashCode(), emf3.hashCode());
        assertNotEquals(emf1.hashCode(), emf4.hashCode());
        assertNotEquals(emf1.hashCode(), emf5.hashCode());
        assertNotEquals(emf1.hashCode(), emf6.hashCode());
        assertNotEquals(emf1.hashCode(), emf7.hashCode());

        assertEquals(emf1.toString(), emf2.toString());
        assertEquals(emf1.toString(), emf8.toString());
        assertNotEquals(emf1.toString(), emf3.toString());
        assertNotEquals(emf1.toString(), emf4.toString());
        assertNotEquals(emf1.toString(), emf5.toString());
        assertNotEquals(emf1.toString(), emf6.toString());
        assertNotEquals(emf1.toString(), emf7.toString());
    }

    @Test
    public void testGetFormat() {
        // Test with null registry
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0} {1}", Locale.US);
        assertNull(efm.getFormat(null));

        // Test with empty registry
        Map<String, FormatFactory> registry = new HashMap<>();
        emf = new ExtendedMessageFormat("{format.date,date} {0} {1}", Locale.US, registry);
        assertNull(efm.getFormat("date"));

        // Test with format in registry
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        emf = new ExtendedMessageFormat("{format.date,date} {0} {1}", Locale.US, registry);
        SimpleDateFormat result = (SimpleDateFormat) emf.getFormat("date");
        assertEquals("dd-MM-yyyy", result.toPattern());

        // Test with format not in registry
        registry.clear();
        emf = new ExtendedMessageFormat("{format.date,date} {0} {1}", Locale.US, registry);
        assertNull(efm.getFormat("date"));
    }
}