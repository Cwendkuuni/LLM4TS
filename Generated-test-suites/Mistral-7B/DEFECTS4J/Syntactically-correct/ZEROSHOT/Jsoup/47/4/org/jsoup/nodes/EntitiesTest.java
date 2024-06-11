package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Entities;

import org.junit.Test;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
       assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertTrue(entities.isNamedEntity("quot"));
        assertFalse(entities.isNamedEntity("xxxx"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("quot"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertEquals('"', entities.getCharacterByName("quot"));
        assertNull(entities.getCharacterByName("xxxx"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&lt;<>&quot;\"&amp;<gt;&#160;";
        Document.OutputSettings out = new Document.OutputSettings();

        StringBuilder expected = new StringBuilder();
        escape(expected, input, out, false, false, false);
        String expectedString = expected.toString();

        StringBuilder actual = new StringBuilder();
        String result = entities.escape(input, out);
        escape(actual, result, out, false, false, false);

        assertEquals(expectedString, actual.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;<>&quot;\"&amp;gt;&#160;";

        String expected = "&lt;<>&quot;\"&gt;&#160;";
        String result = entities.unescape(input);

        assertEquals(expected, result);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        assertTrue(entities.canEncode("ascii", ' ', entities.ascii));
        assertTrue(entities.canEncode("utf", ' ', entities.utf));
        assertFalse(entities.canEncode("utf", '\u00E9', entities.utf));
    }

    private void escape(StringBuilder accum, String string, Document.OutputSettings out,
                        boolean inAttribute, boolean normaliseWhite, boolean stripLeadingWhite) {

        Entities entities = new Entities();
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;
        final EscapeMode escapeMode = out.escapeMode();
        final CharsetEncoder encoder = out.encoder();
        final CoreCharset coreCharset = CoreCharset.byName(encoder.charset().name());
        final Map<Character, String> map = escapeMode.getMap();

        int codePoint;
        for (int offset = 0; offset < string.length(); offset += Character.charCount(codePoint)) {
            codePoint = Character.codePointAt(string, offset);

            if (normaliseWhite) {
                if (StringUtil.isWhitespace(codePoint)) {
                    if ((stripLeadingWhite && !reachedNonWhite) || lastWasWhite)
                        continue;
                    accum.append(' ');
                    lastWasWhite = true;
                    continue;
                } else {
                    lastWasWhite = false;
                    reachedNonWhite = true;
                }
            }

            // surrogate pairs, split implementation for efficiency on single char common case (saves creating strings, char[]):
            if (codePoint < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
                final char c = (char) codePoint;
                switch (c) {
                    case '&':
                        accum.append("&amp;");
                        break;
                    case 0xA0:
                        if (escapeMode != Entities.EscapeMode.xhtml)
                            accum.append("&nbsp;");
                        else
                            accum.append("&#a0;");
                        break;
                    case '<':
                        // escape when in character data or when in a xml attrib
                        if (!inAttribute)
                            accum.append("&lt;");
                        else
                            accum.append(c);
                        break;
                    case '>':
                        if (!inAttribute)
                            accum.append("&gt;");
                        else
                            accum.append(c);
                        break;
                    case '"':
                        if (inAttribute)
                            accum.append("&quot;");
                        else
                            accum.append(c);
                        break;
                    default:
                        if (entities.canEncode(coreCharset, c, encoder))
                            accum.append(c);
                        else if (map.containsKey(c))
                            accum.append('&').append(map.get(c)).append(';');
                        else
                            accum.append("&#x").append(Integer.toHexString(codePoint)).append(';');
                }
            } else {
                final String c = new String(Character.toChars(codePoint));
                if (encoder.canEncode(c)) // uses fallback encoder for simplicity
                    accum.append(c);
                else
                    accum.append("&#x").append(Integer.toHexString(codePoint)).append(';');
            }
        }
    }
}