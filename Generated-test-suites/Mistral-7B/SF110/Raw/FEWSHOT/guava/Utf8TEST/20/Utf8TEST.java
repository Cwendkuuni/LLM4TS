package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Beta
@GwtCompatible
public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int utf8Length = Utf8.encodedLength(sequence);
        int expectedUtf8Length = sequence.length() * 3;
        Assert.assertEquals(expectedUtf8Length, utf8Length);

        sequence = "\u0123\u0456\u0800\uD800\uDCAF";
        utf8Length = Utf8.encodedLength(sequence);
        int expectedUtf8Length2 = sequence.length() * 3;
        Assert.assertEquals(expectedUtf8Length2, utf8Length);

        sequence = "";
        utf8Length = Utf8.encodedLength(sequence);
        Assert.assertEquals(0, utf8Length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthIllegalArgumentException() {
        CharSequence sequence = "";
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormedByteArray() {
        byte[] bytes = new byte[0];
        boolean actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-73, -73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-73, -73, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{73, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{'foo'.codeUnitAt(0), 'foo'.codeUnitAt(0), 'foo'.codeUnitAt(0)};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-1, -63, -62, 0, 0, 0, 0, 0, 0, 0};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{72, 194, 195, 199, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-61, -81, -81, 0, 0, 0, 0, 0, 0, 0};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{72, 194, 194, -128, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-62, -127, 199, 128, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-63, 224, 163, 172, -128, 191, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{72, 194, 195, 199, -128, 191, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertFalse(actualResult);

        bytes = new byte[]{-63, 224, 163, 173, 191, 73};
        actualResult = Utf8.isWellFormed(bytes);
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testIsWellFormedByteArrayWithStartIndexAndLength() {
        byte[] bytes = new byte[0];
        boolean actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-73, -73};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-73, -73, 73};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{'foo'.codeUnitAt(0), 'foo'.codeUnitAt(0), 'foo'.codeUnitAt(0)};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{72, 194, 195, 199, 73};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-62, -127, 199, 128, 73};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{-63, 224, 163, 172, 191, 73};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertTrue(actualResult);

        bytes = new byte[]{72, 194, 195, 199, -128, 191, 73};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertFalse(actualResult);

        bytes = new byte[]{-63, 224, 163, 173, 191, 73};
        actualResult = Utf8.isWellFormed(bytes, 0, bytes.length);
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testEncodedLengthAndIsWellFormedWithRandomData() {
        final int length = 100;
        byte[] randomBytes = new byte[length];
        new Random().nextBytes(randomBytes);
        int encodedLength = Utf8.encodedLength(new String(randomBytes, StandardCharsets.UTF_8));
        boolean isWellFormed = Utf8.isWellFormed(randomBytes);
        Assert.assertEquals(encodedLength, randomBytes.length);
        Assert.assertTrue(isWellFormed);
    }
}