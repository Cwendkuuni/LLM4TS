package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.StringWriter;
import java.io.IOException;
import static java.lang.String.valueOf;

public class NumericEntityUnescaperTest {
 private CharSequence input;
 private Writer out;

 @Before
 public void setUp() {
 this.input = new StringBuilder();
 this.out = new StringWriter();
 }

 // Test cases go here

 @After
 public void tearDown() {
 try {
 this.input.flush();
 } catch (IOException e) {
 // Ignore
 }
 try {
 this.out.close();
 } catch (IOException e) {
 // Ignore
 }
 }
}