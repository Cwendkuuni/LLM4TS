package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.SimpleFormatFactory;

/**
 * Test class for ExtendedMessageFormat.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor ExtendedMessageFormat(String pattern).
     */
    @Test
    public void testExtendedMessageFormat01() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"foo", 123};
        Object result = fmt.format(args);
        assertEquals("MyCustomFormat[foo]{1}123", result.toString());
    }

    /**
     * Test constructor ExtendedMessageFormat(String pattern, Locale locale).
     */
    @Test
    public void testExtendedMessageFormat02() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern,
                Locale.US, registry);
        Object[] args = {"foo", 123};
        Object result = fmt.format(args);
        assertEquals("MyCustomFormat[foo]{1}123", result.toString());
    }

    /**
     * Test constructor ExtendedMessageFormat(String pattern, Map registry).
     */
    @Test
    public void testExtendedMessageFormat03() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"foo", 123};
        Object result = fmt.format(args);
        assertEquals("MyCustomFormat[foo]{1}123", result.toString());
    }

    /**
     * Test constructor ExtendedMessageFormat(String pattern, Locale locale,
     * Map registry).
     */
    @Test
    public void testExtendedMessageFormat04() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern,
                Locale.US, registry);
        Object[] args = {"foo", 123};
        Object result = fmt.format(args);
        assertEquals("MyCustomFormat[foo]{1}123", result.toString());
    }

    /**
     * Test method setLocale.
     */
    @Test
    public void testSetLocale() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        fmt.setLocale(Locale.US);
        Object[] args = {"foo", 123};
        Object result = fmt.format(args);
        assertEquals("MyCustomFormat[foo]{1}123", result.toString());
    }

    /**
     * Test method applyPattern.
     */
    @Test
    public void testApplyPattern01() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        fmt.applyPattern("{0,formatName=MyCustomFormat}{1}");
        Object[] args = {"foo", 123};
        Object result = fmt.format(args);
        assertEquals("MyCustomFormat[foo]{1}123", result.toString());
    }

    /**
     * Test method applyPattern with invalid format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPattern02() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        fmt.applyPattern("{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1,invalid}");
    }

    /**
     * Test method getFormat.
     */
    @Test
     @SuppressWarnings("unchecked")
    public void testGetFormat() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        Format format = fmt.getFormat(0);
        assertEquals("MyCustomFormat", format.getClass().getName());
    }

    /**
     * Test method getFormat with non existing format.
     */
    @Test
    public void testGetFormat02() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        Format format = fmt.getFormat(10);
        assertNull(format);
    }

    /**
     * Test method format.
     */
    @Test
    public void testFormat() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        Object[] args = {"foo", 123};
        Object result = fmt.format(args);
        assertEquals("MyCustomFormat[foo]{1}123", result.toString());
    }

    /**
     * Test method format with invalid format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormat01() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        Object[] args = {"foo", 123, "unknown"};
        fmt.format(args);
    }

    /**
     * Test method format with null args.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormat02() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}{1}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        fmt.format(null);
    }

    /**
     * Test method format with no args.
     */
    @Test
    public void testFormat03() {
        String pattern = "{0,formatName=MyCustomFormat,formatStyle=MyCustomFormatStyle}";
        Map<String, FormatFactory> registry = Collections.singletonMap(
                "MyCustomFormat", new SimpleFormatFactory() {
                    @Override
                    public Format getFormat(String pattern, String style,
                            Locale locale) {
                        return new Format() {
                            @Override
                            public String format(Object obj, Locale locale,
                                    String format, ParsePosition pos) {
                                return ToStringBuilder.reflectionToString(obj,
                                        ToStringStyle.SHORT_PREFIX_STYLE);
                            }
                        };
                    }
                });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null);
        Object result = fmt.format(new Object[0]);
        assertEquals("", result.toString());
    }
}