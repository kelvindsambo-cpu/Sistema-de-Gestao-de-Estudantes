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
                switch(ano){
                    case 1: year[0]++;break;
                    case 2: year[1]++;break;
                    case 3: year[2]++;break;
                    case 4: year[3]++;break;
                }
            }
        }catch(IOException e){
            System.out.println("Impossível ler o ficheiro");
        }
        return year;
    }
    @Override
    public String toString(){
        return "============= Estatística Por Ano ==============\n"+
                "Estudantes do  1ºAno: "+ano[0]+"\n"+
                "Estudantes do  2ºAno: "+ano[1]+"\n"+
                "Estudantes do  3ºAno: "+ano[2]+"\n"+
                "Estudantes do  4ºAno: "+ano[3]+"\n"
                ;
    }
}
