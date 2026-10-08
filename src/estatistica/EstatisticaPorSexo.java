package estatistica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class EstatisticaPorSexo extends Estatistica{
    private int totalMasculino;
    private int totalFeminino;
    private double percentagemMasculino;
    private double percentagemFeminino;

    public EstatisticaPorSexo(){
        this.totalMasculino = contarMasculino();
        this.totalFeminino = totalEstudantes - totalMasculino;
        this.percentagemMasculino = (double) (totalMasculino/totalEstudantes) * 100;
        this.percentagemFeminino = 100 - percentagemMasculino;
    }

    private int contarMasculino() {
        int total = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(ficheiroEstudante));
            br.readLine();
            while (br.ready()) {
                String[] tokens = br.readLine().split(",");
                    if(tokens[5].equals("M")) {
                        total += 1;
                    }
                }
            } catch(IOException e) {
            System.out.println("Não foi possível ler o ficheiro");
        }
        return total;
    }

    @Override
    public String toString(){
        return "============== Estatistíca Por Sexo ===========\n"+
                "Quantidade masculino: "+totalMasculino+"\n"+
                "Quantidade feminino: "+totalFeminino+"\n"+
                "Percentagem masculino: "+percentagemMasculino+"\n"+
                "Percentagem feminino: "+percentagemFeminino;

    }
}
