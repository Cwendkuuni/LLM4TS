package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.cli.ParsePosition;

public class ExtendedMessageFormatTest {

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals("Hello {0}, your age is {1}.", messageFormat.toPattern());

        pattern = "{0,date,MM-dd-yyyy}, {1}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
        assertEquals("{0,date,MM-dd-yyyy}, {1}", messageFormat.toPattern());

        pattern = "{0,date,MM-dd-yyyy} {1}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
        assertEquals("{0,date,MM-dd-yyyy} {1}", messageFormat.toPattern());

        pattern = "{0,date,MM-dd-yyyy} {1,number,number} {2}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
        assertEquals("{0,date,MM-dd-yyyy} {1,number,number} {2}", messageFormat.toPattern());

        pattern = "{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid} {2}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        assertEquals("{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid} {2}", messageFormat.toPattern());

        pattern = "{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid} {2,number,number}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), null);
        assertEquals("{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid} {2,number,number}", messageFormat.toPattern());

        pattern = "{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid} {2,choice,foo|bar}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
        assertEquals("{0,date,MM-dd-yyyy} {1,choice,foo|bar} {2,choice,foo|bar}", messageFormat.toPattern());

        pattern = "{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid} {2,choice,foo|bar}, {3,date,MM-dd-yyyy}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
        assertEquals("{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid} {2,choice,foo|bar}, {3,date,MM-dd-yyyy}", messageFormat.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternFormat() {
        String pattern = "Hello {0,{1},2}";
        new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElement() {
        String pattern = "Hello {0} {1";
        new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatArgumentIndex() {
        String pattern = "Hello {-1} {0}";
        new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        String pattern = "Hello {0} {1,invalidArg}";
        new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescriptionWithArgument() {
        String pattern = "Hello {0} {1,invalidArg,arg}";
        new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatDescription() {
        String pattern = "Hello {0} {1}";
        new ExtendedMessageFormat(pattern, Locale.getDefault(), Collections.emptyMap());
    }

    @Test
    public void testGetFormat() {
        MessageFormatFactory dateFormatFactory = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        MessageFormatFactory choiceFormatFactory = new MessageFormat("{0,choice,0#Invalid|1#Valid}", Locale.getDefault());
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0,date,MM-dd-yyyy} {1,choice,0#Invalid|1#Valid}", Locale.getDefault(), Map.of("date", dateFormatFactory, "choice", choiceFormatFactory));
        assertEquals(dateFormatFactory, messageFormat.getFormat("date"));
        assertEquals(choiceFormatFactory, messageFormat.getFormat("choice"));
        assertNull(messageFormat.getFormat("unknown"));
    }
}