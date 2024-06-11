package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Before;
import org.junit.Test;

public class ARFFHandlerTest {

 private File testFile;
 private LineIterator mockLineIterator;

 @Before
 public void setUp() {
 testFile = new File("testARFFFile.arff");
 mockLineIterator = mock(LineIterator.class);
 }

 @Test
 public void testLoadARFFWithClassIndexValid() throws IOException {
 Dataset expectedDataset = createTestDataset();
 when(mockLineIterator.hasNext()).thenReturn(true).thenReturn(false);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[0]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[1]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[2]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[3]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[4]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[5]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[6]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[7]);

 ARFFHandler.loadARFF(testFile, 1);
 verify(mockLineIterator, times(8)).next();

 Dataset resultDataset = ARFFHandler.loadARFF(testFile, 1);
 assertEquals(expectedDataset, resultDataset);
 }

 @Test
 public void testLoadARFFWithClassIndexInvalid() throws IOException {
 Dataset expectedDataset = createTestDataset();
 when(mockLineIterator.hasNext()).thenReturn(true).thenReturn(false);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[0]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[1]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[2]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[3]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[4]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[5]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[6]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[7]);
 when(mockLineIterator.next()).thenAnswer(i -> getTestLines()[8]);

 ARFFHandler.loadARFF(testFile, 9);
 verify(mockLineIterator, times(9)).next();

 Dataset resultDataset = ARFFHandler.loadARFF(testFile, 9);
 assertNotNull(resultDataset);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithNullFile() throws IOException {
 ARFFHandler.loadARFF(null, 1);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 testFile.delete();
 ARFFHandler.loadARFF(testFile, 1);
 }

 private Dataset createTestDataset() {
 List<Instance> instances = Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, "three"}, 3.0),
 new DenseInstance(new double[]{4.0, 5.0, "three"}, 3.0),
 new DenseInstance(new double[]{6.0, 7.0, "four"}, 4.0),
 new DenseInstance(new double[]{8.0, 9.0, "four"}, 4.0));
 return new DefaultDataset("Test Dataset", instances.toArray(new Instance[0]));
 }

 private String[] getTestLines() {
 return new String[] {"@relation testRelation",
         "@attribute Att1 double",
         "@attribute Att2 string",
         "@attribute Att3 double",
         "@attribute Att4 string",
         "@data",
         "1.0, 2.0, three, 3.0",
         "4.0, 5.0, three, 3.0",
         "6.0, 7.0, four, 4.0",
         "8.0, 9.0, four, 4.0"};
 }
}