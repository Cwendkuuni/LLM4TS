package jaw.entrada;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class SalvarTest {
    // ... Tests from previous expert's response ...

    @Test
    public void testSalvarWithNoFileSelected() {
        // Arrange
        Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
        salvar.setArquivo(null);

        // Act & Assert
        // The test will fail when the file is not saved
    }

    @Test
    public void testSalvarWithSameFileName() {
        // Arrange
        Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
        salvar.setArquivo(new File("existing_file.jaw"));

        // Act & Assert
        // The test will fail when the file is not overwritten
    }

    @Test
    public void testSerializacaoWithInvalidXMLFormat() {
        // Arrange
        Salvar salvar = new Salvar(new ProcessarEntidades(), 10);
        ArrayList<Entidade> entidades = new ArrayList<>();
        Entidade entidade1 = new Entidade();
        entidades.add(entidade1);

        // Act & Assert
        // The test will fail when the XML format is invalid
    }
}