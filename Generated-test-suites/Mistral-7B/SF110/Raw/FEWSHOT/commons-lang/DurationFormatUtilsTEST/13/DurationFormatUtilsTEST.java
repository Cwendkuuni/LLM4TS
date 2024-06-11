package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.apache.commons.lang3.time.format.DateFormatUtils;
import org.apache.commons.lang3.time.format.ParsePosition;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("02:04:23.700", result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("P2D2H4M23.7S", result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("2 days 4 hours 23 minutes 47.700 seconds", result);
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        long durationMillis = 1234567L;
        String format = "d'  days  'H' hours  'm' minutes  's' seconds.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(" 2   days   4    hours   23 minutes   47.700 seconds", result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals("2 days, 4 hours, 23 minutes, 47 seconds", result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070459000L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("PT59M", result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070459000L;
        String format = "P{y}Y{M}M{d}DT{H}H{m}M{s}.{S}S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("PT1h1m59s", result);
    }

    @Test
    public void testFormatPeriodWithPadZeros() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070459000L;
        String format = "P{y}Y{M0}M{d0}DT{H0}H{m0}M{s0}.{S0}S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals("PT1h PT1m PT59s", result);
    }

    @Test
    public void testParseDurationHMS() {
        String input = "02:04:23.700";
        long durationMillis = DurationFormatUtils.parseDurationHMS(input);
        assertEquals(1234567L, durationMillis);
    }

    @Test
    public void testParseDurationISO() {
        String input = "P2D2H4M23.7S";
        long durationMillis = DurationFormatUtils.parseDurationISO(input);
        assertEquals(1234567L, durationMillis);
    }

    @Test
    public void testParseDuration() {
        String input = "2 days 4 hours 23 minutes 47 seconds.700";
        long durationMillis = DurationFormatUtils.parseDuration(input);
        assertEquals(1234567L, durationMillis);
    }

    @Test
    public void testParsePeriodISO() {
        String input = "PT59M";
        long startMillis = DurationFormatUtils.parsePeriodISO(input, null).getTime();
        assertEquals(1420070459000L, startMillis);
    }

    @Test
    public void testParsePeriod() {
        String input = "PT1h1m59s";
        long startMillis = DurationFormatUtils.parsePeriod(new Date(1420070400000L), input).getTime();
        assertEquals(1420070459000L, startMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsePeriodWithInvalidFormat() {
        String input = "PT1h1m59sa";
        DurationFormatUtils.parsePeriod(new Date(1420070400000L), input);
    }

    @Test
    public void testParsePeriodWithNullInput() {
        String input = null;
        assertNull(DurationFormatUtils.parsePeriod(new Date(1420070400000L), input));
    }

    @Test
    public void testFormatWithTokens() {
        long durationMillis = 1234567L;
        List<Token> tokens = lexx("d' days 'H' hours 'm' minutes 's' seconds.SSS");
        String result = DurationFormatUtils.format(tokens, durationMillis, 0L, 0L, 0L, 0L, 0L, 0L, true);
        assertEquals(" 2   days   4    hours   23 minutes   47.700 seconds", result);
    }

    @Test
    public void testFormatWithTokensWithNegativeDuration() {
        long durationMillis = -1234567L;
        List<Token> tokens = lexx("d' days 'H' hours 'm' minutes 's' seconds.SSS");
        String result = DurationFormatUtils.format(tokens, durationMillis, 0L, 0L, 0L, 0L, 0L, 0L, true);
        assertEquals("- 2   days  -5 hours -23 minutes -47.700 seconds", result);
    }

    @Test
    public void testFormatWithTokensWithZeroDuration() {
        long durationMillis = 0L;
        List<Token> tokens = lexx("d' days 'H' hours 'm' minutes 's' seconds.SSS");
        String result = DurationFormatUtils.format(tokens, durationMillis, 0L, 0L, 0L, 0L, 0L, 0L, true);
        assertEquals("                          ", result);
    }

    private static List<Token> lexx(String format) {
        return new ArrayList<Token>(lex(format).subList(0, lex(format).size()));
    }

    private static List<Token> lex(String format) {
        return DurationFormatUtils.lexx(format);
    }

    @Test
    public void testLexxWithEmptyFormat() {
        List<Token> tokens = DurationFormatUtils.lexx("");
        assertEquals(0, tokens.size());
    }

    @Test
    public void testLexxWithLiteralFormat() {
        List<Token> tokens = DurationFormatUtils.lexx("'''");
        assertEquals(1, tokens.size());
        Token token = tokens.get(0);
        assertEquals("'", token.getValue());
        assertEquals(1, token.getCount());
    }

    @Test
    public void testLexxWithSimpleFormat() {
        List<Token> tokens = DurationFormatUtils.lexx("y M d H m s S");
        assertEquals(7, tokens.size());
        Token yToken = tokens.get(0);
        Token yTokenCount = tokens.get(1);
        Token mToken = tokens.get(2);
        Token mTokenCount = tokens.get(3);
        Token sToken = tokens.get(4);
        Token sTokenCount = tokens.get(5);
        Token stToken = tokens.get(6);
        assertEquals("y", yToken.getValue());
        assertEquals(1, yToken.getCount());
        assertEquals("M", mToken.getValue());
        assertEquals(1, mToken.getCount());
        assertEquals("d", sToken.getValue());
        assertEquals(1, sToken.getCount());
        assertEquals("H", hToken.getValue());
        assertEquals(1, hToken.getCount());
        assertEquals("m", mToken.getValue());
        assertEquals(1, mToken.getCount());
        assertEquals("s", sToken.getValue());
        assertEquals(1, sToken.getCount());
        assertEquals("S", stToken.getValue());
        assertEquals(1, stToken.getCount());
    }

    @Test
    public void testLexxWithComplexFormat() {
        List<Token> tokens = DurationFormatUtils.lexx("P{y}Y{M0}M{d0}DT{H0}H{m0}M{s0}.{S0}S");
        assertEquals(13, tokens.size());
        Token pToken = tokens.get(0);
        Token yToken = tokens.get(1);
        Token yTokenCount = tokens.get(2);
        Token MToken = tokens.get(3);
        Token MToken0 = tokens.get(4);
        Token dToken = tokens.get(5);
        Token dToken0 = tokens.get(6);
        Token TToken = tokens.get(7);
        Token TToken1 = tokens.get(8);
        Token HToken = tokens.get(9);
        Token HToken0 = tokens.get(10);
        Token mToken = tokens.get(11);
        Token mToken0 = tokens.get(12);
        Token sToken = tokens.get(13);
        Token sToken0 = tokens.get(14);
        assertEquals("P", pToken.getValue());
        assertEquals(1, pToken.getCount());
        assertEquals("{y}", yToken.getValue());
        assertEquals(1, yToken.getCount());
        assertEquals("{y}", yTokenCount.getValue());
        assertEquals(1, yTokenCount.getCount());
        assertEquals("{M}", MToken.getValue());
        assertEquals(1, MToken.getCount());
        assertEquals("{M0}", MToken0.getValue());
        assertEquals(1, MToken0.getCount());
        assertEquals("{d}", dToken.getValue());
        assertEquals(1, dToken.getCount());
        assertEquals("{d0}", dToken0.getValue());
        assertEquals(1, dToken0.getCount());
        assertEquals("T", TToken.getValue());
        assertEquals(1, TToken.getCount());
        assertEquals("{H}", HToken.getValue());
        assertEquals(1, HToken.getCount());
        assertEquals("{H0}", HToken0.getValue());
        assertEquals(1, HToken0.getCount());
        assertEquals("{m}", mToken.getValue());
        assertEquals(1, mToken.getCount());
        assertEquals("{m0}", mToken0.getValue());
        assertEquals(1, mToken0.getCount());
        assertEquals("{s}", sToken.getValue());
        assertEquals(1, sToken.getCount());
        assertEquals("{s0}", sToken0.getValue());
        assertEquals(1, sToken0.getCount());
    }
}