package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.Test;

import corina.ui.I18n;
import corina.formats.TucsonSimple;
import corina.formats.Filetype;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.Sample;
import corina.util.Range;
import corina.exception.WrongFiletypeException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple = new TucsonSimple();

 @Test
 public void testToString() {
     assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
     assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
     BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("test data".getBytes())));
     tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
     Sample sample = new Sample("sampleId", new Range(new Year(2001), new Year(2001)), new double[1]);
     assertEquals("000001", tucsonSimple.make6digitCode(sample));

     sample.setMeta("id", "sampleId2");
     assertEquals("sampleId2", tucsonSimple.make6digitCode(sample));

     sample = new Sample("sampleId3", new Range(new Year(2001), new Year(2001)), new double[1]);
     assertEquals("000006", tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
     String code = "code";
     Year year = new Year(2001);
     tucsonSimple.saveRowHeader(writer, new Sample("sampleId", new Range(year, year), new double[1]), code, 6, year);
     assertEquals(code + "2001", writer.toString());

     writer.close();

     year = year.subtract(1);
     tucsonSimple.saveRowHeader(writer, new Sample("sampleId2", new Range(year, year), new double[1]), code, 4, year);
     assertEquals(code + " 2000", writer.toString());

     writer.close();
 }

 @Test
 public void testSaveData() throws IOException {
     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
     String data = "123.45";
     double[] sampleData = {new Double(Double.parseDouble(data))};
     Sample sample = new Sample("sampleId", new Range(new Year(2001), new Year(2001)), sampleData);
     String code = "code";
     Year start = new Year(2001);
     Year end = new Year(2002);

     tucsonSimple.saveData(sample, writer);

     assertEquals("code2001\n123.45\n", writer.toString());

     writer.close();

     start = start.add(1);
     end = end.add(1);

     writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
     tucsonSimple.saveData(sample, writer);

     assertEquals("code2001\n123.45\ncode2002    \n", writer.toString());

     writer.close();
 }

 @Test
 public void testComputeRange() {
     Sample sample = new Sample("sampleId", new Range(new Year(1995), new Year(1995)), new double[1]);
     Range range = tucsonSimple.computeRange(sample);
     assertEquals(new Range(new Year(1995), new Year(1995)), range);

     sample.setRange(new Range(new Year(1994), new Year(1995)));
     range = tucsonSimple.computeRange(sample);
     assertEquals(new Range(new Year(1994), new Year(1995)), range);

     sample.setRange(new Range(new Year(1980), new Year(1980)));
     range = tucsonSimple.computeRange(sample);
     assertEquals(new Range(new Year(1980), new Year(1980)), range);

     sample.setRange(new Range(new Year(1800), new Year(1800)));
     range = tucsonSimple.computeRange(sample);
     assertEquals(new Range(new Year(1801), new Year(1801)), range);
 }

 @Test
 public void testSave() throws IOException {
     BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("test data".getBytes()))) {
         BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));

         @Override
         public BufferedWriter newWriter(String suffix) throws IOException {
             return bufferedWriter;
         }
     };
     Sample sample = new Sample("sampleId", new Range(new Year(2001), new Year(2001)), new double[1]);
     String code = "code";
     tucsonSimple.save(sample, writer);

     assertEquals("code2001\n123.45\n", writer.toString());
 }
}