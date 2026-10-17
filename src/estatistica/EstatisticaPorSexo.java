package estatistica;

import utils.Rounder;

import java.io.BufferedReader;
import java.io.IOException;


public class EstatisticaPorSexo extends Estatistica{
    private int totalMasculino;
    private int totalFeminino;
    private double percentagemMasculino;
    private double percentagemFeminino;

    public EstatisticaPorSexo(){
        this.totalMasculino =  contarMasculino();
        this.totalFeminino = totalEstudantes - totalMasculino;
        this.percentagemMasculino = Rounder.round((double) totalMasculino / totalEstudantes,3);
        this.percentagemFeminino = 100 - percentagemMasculino;

    }

    private int contarMasculino() {
        int total = 0;
        try(BufferedReader br = ficheiroEstudante.abrirFicheiro()){
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
                "Quantidade masculina: "+totalMasculino+"\n"+
                "Quantidade feminina: "+totalFeminino+"\n"+
                "Percentagem masculina: "+percentagemMasculino+"%\n"+
                "Percentagem feminina: "+percentagemFeminino+"%";

    }
}
