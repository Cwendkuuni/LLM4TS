package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 assertEquals(null, HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterSingleCharacter() {
 char[] content = new char[1];
 content[0] = 'A';
 String input = new String(content);
 String output = HTMLFilter.filter(input);
 assertEquals("A", output);
 }

 @Test
 public void testFilterSimpleHTMLTags() {
 char[] content = new char[15];
 content[0] = '<';
 content[1] = 'p';
 content[2] = '>';
 content[3] = 'H';
 content[4] = 'e';
 content[5] = 'l';
 content[6] = 'l';
 content[7] = 'o';
 content[8] = '<';
 content[9] = '/';
 content[10] = 'p';
 content[11] = '>';
 String input = new String(content);
 String output = HTMLFilter.filter(input);
 assertEquals("&lt;p&gt;Hell&lt;p&gt;", output);
 }

 @Test
 public void testFilterComplexHTMLTags() {
 char[] content = new char[56];
 content[0] = '<';
 content[1] = 'h';
 content[2] = '1';
 content[3] = ' ';
 content[4] = 'i';
 content[5] = 'd';
 content[6] = '=';
 content[7] = '"';
 content[8] = 't';
 content[9] = 'e';
 content[10] = 'm';
 content[11] = 'p';
 content[12] = 'l';
 content[13] = 'e';
 content[14] = '=';
 content[15] = '"';
 content[16] = 'i';
 content[17] = 's';
 content[18] = ' ';
 content[19] = 't';
 content[20] = 'e';
 content[21] = 'm';
 content[22] = 'p';
 content[23] = 'l';
 content[24] = 'e';
 content[25] = '>';
 content[26] = 'H';
 content[27] = 'i';
 content[28] = ' ';
 content[29] = 't';
 content[30] = 'e';
 content[31] = 'm';
 content[32] = 'p';
 content[33] = 'l';
 content[34] = 'e';
 content[35] = '<';
 content[36] = '/';
 content[37] = 'h';
 content[38] = '1';
 content[39] = ' ';
 content[40] = '/';
 content[41] = 'i';
 content[42] = 'd';
 content[43] = '>';
 String input = new String(content);
 String output = HTMLFilter.filter(input);
 assertEquals("&lt;h1 id=&quot;temple&quot;&gt;Hitempl&lt;/h1&gt;", output);
 }

 @Test
 public void testFilterComplexHTMLWithScriptsAndStyles() {
 char[] content = new char[154];
 content[0] = '<';
 content[1] = 'h';
 content[2] = '1';
 content[3] = ' ';
 content[4] = 'i';
 content[5] = 'd';
 content[6] = '=';
 content[7] = '"';
 content[8] = 't';
 content[9] = 'e';
 content[10] = 'm';
 content[11] = 'p';
 content[12] = 'l';
 content[13] = 'e';
 content[14] = '>';
 content[15] = '<';
 content[16] = 's';
 content[17] = 'c';
 content[18] = 'r';
 content[19] = 'i';
 content[20] = 'p';
 content[21] = 't';
 content[22] = '>';
 content[23] = '<';
 content[24] = 's';
 content[25] = 't';
 content[26] = 'y';
 content[27] = 'l';
 content[28] = 'e';
 content[29] = '>';
 content[30] = 's';
 content[31] = 'c';
 content[32] = 'r';
 content[33] = 'i';
 content[34] = 'p';
 content[35] = 't';
 content[36] = ' ';
 content[37] = 's';
 content[38] = 'o';
 content[39] = 'u';
 content[40] = 'r';
 content[41] = 'c';
 content[42] = 'e';
 content[43] = '>';
 content[44] = 'S';
 content[45] = 'C';
 content[46] = 'R';
 content[47] = 'i';
 content[48] = 'p';
 content[49] = ' ';
 content[50] = 't';
 content[51] = 'o';
 content[52] = ' ';
 content[53] = 'S';
 content[54] = 'C';
 content[55] = 'R';
 content[56] = 'i';
 content[57] = 'p';
 content[58] = ' ';
 content[59] = 't';
 content[60] = 'o';
 content[61] = ' ';
 content[62] = 'J';
 content[63] = 'a';
 content[64] = 'v';
 content[65] = 'a';
 content[66] = 's';
 content[67] = 'c';
 content[68] = 'r';
 content[69] = 'i';
 content[70] = 'p';
 content[71] = ' ';
 content[72] = 't';
 content[73] = 'o';
 content[74] = ' ';
 content[75] = 'H';
 content[76] = 'T';
 content[77] = 'M';
 content[78] = 'L';
 content[79] = ' ';
 content[80] = 's';
 content[81] = 'o';
 content[82] = 'u';
 content[83] = 'r';
 content[84] = 'c';
 content[85] = 'e';
 content[86] = '>';
 content[87] = '<';
 content[88] = '/';
 content[89] = 'h';
 content[90] = '1';
 content[91] = ' ';
 content[92] = '/';
 content[93] = 's';
 content[94] = 't';
 content[95] = 'y';
 content[96] = 'l';
 content[97] = 'e';
 content[98] = '>';
 content[99] = 'H';
 content[100] = 'T';
 content[101] = 'M';
 content[102] = 'L';
 content[103] = ' ';
 content[104] = '/';
 content[105] = 's';
 content[106] = 'c';
 content[107] = 'r';
 content[108] = 'i';
 content[109] = 'p';
 content[110] = ' ';
 content[111] = 't';
 content[112] = 'o';
 content[113] = ' ';
 content[114] = 'J';
 content[115] = 'a';
 content[116] = 'v';
 content[117] = 'a';
 content[118] = 's';
 content[119] = 'c';
 content[120] = 'r';
 content[121] = 'i';
 content[122] = 'p';
 content[123] = ' ';
 content[124] = 't';
 content[125] = 'o';
 content[126] = ' ';
 content[127] = '/';
 content[128] = 'h';
 content[129] = '1';
 content[130] = ' ';
 content[131] = '/';
 content[132] = 's';
 content[133] = 't';
 content[134] = 'y';
 content[135] = 'l';
 content[136] = 'e';
 content[137] = '>';
 content[138] = 'H';
 content[139] = 'T';
 content[140] = 'M';
 content[141] = 'L';
 content[142] = ' ';
 content[143] = '/';
 content[144] = 's';
 content[145] = 'c';
 content[146] = 'r';
 content[147] = 'i';
 content[148] = 'p';
 content[149] = ' ';
 content[150] = 't';
 content[151] = 'o';
 content[152] = ' ';
 content[153] = 'J';
 content[154] = 'a';
 content[155] = 'v';
 content[156] = 'a';
 content[157] = 's';
 content[158] = 'c';
 content[159] = 'r';
 content[160] = 'i';
 content[161] = 'p';
 content[162] = ' ';
 content[163] = 't';
 content[164] = 'o';
 content[165] = ' ';
 content[166] = '/';
 content[167] = 'h';
 content[168] = '1';
 content[169] = ' ';
 content[170] = '/';
 content[171] = 's';
 content[172] = 't';
 content[173] = 'y';
 content[174] = 'l';
 content[175] = 'e';
 content[176] = '>';
 String input = new String(content);
 String output = HTMLFilter.filter(input);
 assertEquals("&lt;h1 id=&quot;template&quot;&gt;&lt;script src=&quot;script.js&quot;&gt;&lt;/script&gt;&lt;style src=&quot;style.css&quot;&gt;&lt;/style&gt;&lt;/h1&gt;H1HTMLL&lt;script src=&quot;script.js&quot;&gt;&lt;/script&gt;&lt;style src=&quot;style.css&quot;&gt;&lt;/style&gt;", output);
 }

 @Test
 public void testFilterLongInput() {
 char[] content = new char[1024 * 10];
 for (int i = 0; i < content.length; ++i) {
 content[i] = (char) ('A' + (Math.random() * 26));
 }
 String input = new String(content);
 String output = HTMLFilter.filter(input);
 assertEquals(input, output); // This test is to ensure that filtering doesn't change the length of input
 }

 @Test
 public void testFilterInputWithSpecialChars() {
 char[] content = new char[15];
 content[0] = '&';
 content[1] = '#';
 content[2] = '<';
 content[3] = '>';
 content[4] = '=';
 content[5] = '"';
 content[6] = "'";
 content[7] = '&';
 content[8] = 'a';
 content[9] = 'm';
 content[10] = 'p';
 content[11] = ';';
 content[12] = ' ';
 content[13] = 'a';
 content[14] = 'm';
 String input = new String(content);
 String output = HTMLFilter.filter(input);
 assertEquals("&amp;&#35;&lt;&gt;&amp;=&quot;&apos;&amp;amp;am; am", output);
 }
}