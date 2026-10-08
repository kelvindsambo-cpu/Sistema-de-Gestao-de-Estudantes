package estatistica;
import java.io.*;

public abstract class Estatistica {
    protected File ficheiroEstudante = new File("./src/dados/estudantes.csv");
    protected int totalLinhas = contarLinhas();
    protected int totalEstudantes = totalLinhas - 1;



   // this.ficheiroEstudante
    //this.totalLinhas
    //this.totalEstudantes


    private int contarLinhas() {
        int total = 0;
        try {
        BufferedReader br = new BufferedReader(new FileReader(ficheiroEstudante));
            while (br.ready()) {
                br.readLine();
                total += 1;
            }
        } catch(IOException e) {
            System.out.println("Não foi possível ler o ficheiro");
        }
        return total;
    }

    public int getTotalEstudantes() {
        return totalEstudantes;
    }

    public int getTotalLinhas() {
        return totalLinhas;
    }

    @Override
    public  String toString(){
        return "============= Estatistica Geral ===========\n"+
                "Total de Linhas: "+totalLinhas+"\n"+
                "Estudantes Inseridos: "+totalEstudantes+"\n";
    }

}

