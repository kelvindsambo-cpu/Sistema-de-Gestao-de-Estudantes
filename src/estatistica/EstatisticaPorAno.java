package estatistica;
import java.io.*;
public class EstatisticaPorAno extends Estatistica{
    private int []ano;
    public EstatisticaPorAno(){
        this.ano = contarAno();
    }
    private int[] contarAno(){
        int [] year = {0,0,0,0};
        try(BufferedReader br = ficheiroEstudante.abrirFicheiro()){
            br.readLine();
            while (br.ready()){
                String[] linha = br.readLine().split(",");
                int ano = Integer.parseInt(linha[3]);
                int index;
                switch(ano){
                    case 1 -> index = 0;
                    case 2 -> index = 1;
                    case 3 -> index = 2;
                    case 4 -> index = 3;
                    default -> index = -1;
                }
                if(index != -1) {
                    year[index]++;
                }
            }
        }catch(IOException e){
            System.out.println("Impossível ler o ficheiro");
        }
        return year;
    }
    @Override
    public String toString(){
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("============= Estatística Por Ano ==============\n");
        for(int i = 0; i < ano.length;i++){
            relatorio.append("Estudantes do  "+(i+1)+"ºAno: "+ano[i]+"\n");
        }
        return relatorio.toString();
    }
}
