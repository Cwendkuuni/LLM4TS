import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.*;
import java.io.*;

public class ARFFHandlerTest {

 testLoadARFFNoFile() {
 File file = null;
 try {
 Dataset dataset = ARFFHandler.loadARFF(file);
 Fail("Expected FileNotFoundException not thrown");
 } catch (FileNotFoundException e) {
 // Expected
 }
}

 testLoadARFFEmptyFile() {
 File file = new File("empty.arff");
 try {
 Dataset dataset = ARFFHandler.loadARFF(file);
 Assert.assertEquals(0, dataset.numInstances());
 } catch (FileNotFoundException e) {
 Fail("File should exist");
 } finally {
 file.delete();
}
}

 testLoadARFFOneInstance() {
 File file = new File("test1.arff");
 try (PrintWriter writer = new PrintWriter(file)) {
 writer.println("@relation testRel");
 writer.println("@attribute A numeric");
 writer.println("@attribute B numeric");
 writer.println("@attribute C string");
 writer.println("@data");
 writer.println("1.0, 2.5, Apple");
 writer.println("@end");
 }
 try {
 Dataset dataset = ARFFHandler.loadARFF(file);
 Instance instance = dataset.instance(0);
 Assert.assertEquals(2, instance.numValues());
 Assert.assertEquals(1.0, instance.value(0), 0.001);
 Assert.assertEquals(2.5, instance.value(1), 0.001);
 } catch (FileNotFoundException e) {
 Fail("File should exist");
 } finally {
 FileUtils.deleteQuietly(file);
}
}

 testLoadARFFMissingAttribute() {
 File file = new File("test2.arff");
 try (PrintWriter writer = new PrintWriter(file)) {
 writer.println("@relation testRel");
 writer.println("@attribute A numeric");
 writer.println("@attribute B numeric");
 writer.println("@data");
 writer.println("1.0, 2.5");
 writer.println("@end");
 }
 try {
 Dataset dataset = ARFFHandler.loadARFF(file);
 Fail("Expected IllegalArgumentException not thrown");
 } catch (FileNotFoundException | IllegalArgumentException e) {
 // Expected
 } finally {
 FileUtils.deleteQuietly(file);
}
}

 testLoadARFFMissingClass() {
 File file = new File("test3.arff");
 try (PrintWriter writer = new PrintWriter(file)) {
 writer.println("@relation testRel");
 writer.println("@attribute A numeric");
 writer.println("@attribute B numeric");
 writer.println("@data");
 writer.println("1.0, 2.5");
 writer.println("@end");
 }
 try {
 Dataset dataset = ARFFHandler.loadARFF(file, 2);
 Fail("Expected IllegalArgumentException not thrown");
 } catch (FileNotFoundException | IllegalArgumentException e) {
 // Expected
 } finally {
 FileUtils.deleteQuietly(file);
}
}

 testLoadARFFInvalidClassIndex() {
 File file = new File("test4.arff");
 try (PrintWriter writer = new PrintWriter(file)) {
 writer.println("@relation testRel");
 writer.println("@attribute A numeric");
 writer.println("@attribute B numeric");
 writer.println("@attribute C string");
 writer.println("@data");
 writer.println("1.0, 2.5, Apple");
 writer.println("@end");
 }
 try {
 Dataset dataset = ARFFHandler.loadARFF(file, 3);
 Fail("Expected IndexOutOfBoundsException not thrown");
 } catch (FileNotFoundException | IndexOutOfBoundsException e) {
 // Expected
 } finally {
 FileUtils.deleteQuietly(file);
}
}

 testLoadARFFInvalidNumberFormat() {
 File file = new File("test5.arff");
 try (PrintWriter writer = new PrintWriter(file)) {
 writer.println("@relation testRel");
 writer.println("@attribute A numeric");
 writer.println("@attribute B numeric");
 writer.println("@data");
 writer.println("1.0, NaN, Apple");
 writer.println("@end");
 }
 try {
 Dataset dataset = ARFFHandler.loadARFF(file, 1);
 Fail("Expected NumberFormatException not thrown");
 } catch (FileNotFoundException | NumberFormatException e) {
 // Expected
 } finally {
 FileUtils.deleteQuietly(file);
}
}
}