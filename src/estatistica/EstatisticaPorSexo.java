package estatistica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EstatisticaPorSexo extends Estatistica{
    private int totalMasculino;
    private int totalFeminino;

    public EstatisticaPorSexo(){
        this.totalMasculino = contarMasculino();
    }

    private int contarMasculino() {
        int total = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(ficheiroEstudante));
            while (br.ready()) {
                total += 1;
            }
        } catch(IOException e) {
            System.out.println("Não foi possível ler o ficheiro");
        }
        return total;
    }
}
