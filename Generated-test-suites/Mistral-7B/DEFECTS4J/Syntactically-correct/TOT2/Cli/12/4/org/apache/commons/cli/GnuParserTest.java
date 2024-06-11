
package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testGnuParser() {
        testFlattenNormalCase();
        testFlattenSingleOption();
        testFlattenDoubleOption();
        testFlattenNoOptions();
        testFlattenStopAtNonOption();
        testFlattenEmptyArguments();
        testFlattenEmptyOptions();
        testFlattenSingleDash();
        testFlattenNoArguments();
        testFlattenLongOption();
        testFlattenLongOptionWithValue();
        testFlattenMultipleOptions();
    }
}
