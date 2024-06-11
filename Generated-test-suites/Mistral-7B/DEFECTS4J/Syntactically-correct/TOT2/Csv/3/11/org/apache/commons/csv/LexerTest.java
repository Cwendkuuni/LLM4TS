package org.junit.test;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@RunWith(Suite.class)
@Suite.SuiteClasses({LexerTest1.class, LexerTest2.class, LexerTest3.class})
public class LexerTestSuite {
    // Empty test case to make the test suite run
    @Test
    public void emptyTest() {

    }
}