
package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithValidFile() throws FileNotFoundException, IOException {
        File file = new File("test.arff");
        Dataset expectedDataset = new DefaultDataset();
        Dataset actualDataset = ARFFHandler.loadARFF(file);
        Assert.assertEquals(expectedDataset, actualDataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistentFile() {
        File file = new File("nonExistentFile.arff");
        Assert.assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws FileNotFoundException, IOException {
        File file = new File("emptyFile.arff");
        Dataset expectedDataset = new DefaultDataset();
        Dataset actualDataset = ARFFHandler.loadARFF(file);
        Assert.assertEquals(expectedDataset, actualDataset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadARFFWithMissingDataModeLine() throws FileNotFoundException, IOException {
        File file = new File("missingDataModeLine.arff");
        Assert.assertThrows(IllegalArgumentException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadARFFWithMissingAttributeOrClassDeclaration() throws FileNotFoundException, IOException {
        File file = new File("missingAttributeOrClassDeclaration.arff");
        Assert.assertThrows(IllegalArgumentException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadARFFWithInvalidDataFormat() throws FileNotFoundException, IOException {
        File file = new File("invalidDataFormat.arff");
        Assert.assertThrows(NumberFormatException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test
    public void testLoadARFFWithValidFileAndClassIndex() throws FileNotFoundException, IOException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset expectedDataset = new DefaultDataset();
        Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
        Assert.assertEquals(expectedDataset, actualDataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistentFileAndClassIndex() throws FileNotFoundException, IOException {
        File file = new File("nonExistentFile.arff");
        int classIndex = 2;
        Assert.assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file, classIndex));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadARFFWithNegativeClassIndex() throws FileNotFoundException, IOException {
        File file = new File("test.arff");
        int classIndex = -3;
        Assert.assertThrows(IllegalArgumentException.class, () -> ARFFHandler.loadARFF(file, classIndex));
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadARFFWithInvalidDataFormatAndClassIndex() throws FileNotFoundException, IOException {
        File file = new File("test.arff");
        int classIndex = 2;
        Assert.assertThrows(NumberFormatException.class, () -> ARFFHandler.loadARFF(file, classIndex));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadARFFWithNullFileAndClassIndex() {
        File file = null;
        int classIndex = 2;
        Assert.assertThrows(NullPointerException.class, () -> ARFFHandler.loadARFF(file, classIndex));
    }
}