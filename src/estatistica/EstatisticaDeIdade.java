package estatistica;

import model.Estudante;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class EstatisticaDeIdade extends Estatistica{
    private int idadeMedia;
    private int[] idadeExtremo; // 0 - extremo inferior e 1- extremo superior
    private int[] idadeIntervalo;

    public EstatisticaDeIdade(){
        this.idadeMedia = calcularIdadeMedia();
        this.idadeExtremo = new int[2];
        this.idadeIntervalo = new int[5];
        ArrayInitializer(idadeExtremo);
        ArrayInitializer(idadeIntervalo);
        idadesPorIntervalo(idadeExtremo, idadeIntervalo);
    }

    private int calcularIdadeMedia(){
        int idadeMedia = 0;
        try(BufferedReader br = ficheiroEstudante.abrirFicheiro()){
            br.readLine();
            while(br.ready()){
                String[] token = br.readLine().split(",");
                idadeMedia += Integer.parseInt(token[4]);
            }
            idadeMedia /= totalEstudantes;
        }catch(IOException e){
            System.out.println("Não foi possivel abrir o ficheiro");
        }
        return idadeMedia;
    }

    private void idadesPorIntervalo(int[] idadeExtremo,int[] idadeIntervalo){
        int maiorIdade = 0, menorIdade = 99,indexExtremo,indexIntervalo;
        try(BufferedReader br = ficheiroEstudante.abrirFicheiro()){
            br.readLine();
            while(br.ready()){
                String[] token = br.readLine().split(",");
                int idade = Integer.parseInt(token[4]);
                if(idade < menorIdade){
                    menorIdade = idade;
                } else if(idade > maiorIdade){
                    maiorIdade = idade;
                }

                if(idade < 18){
                    indexIntervalo = IdadeIntervalo.MENOR18.ordinal();
                }else if(18 <= idade && idade <= 20){
                    indexIntervalo = IdadeIntervalo.ENTRE18E20.ordinal();
                }else if(20 < idade && idade <= 23){
                    indexIntervalo = IdadeIntervalo.ENTRE21E23.ordinal();
                }else if(23 < idade && idade <= 26){
                    indexIntervalo = IdadeIntervalo.ENTRE24E26.ordinal();
                }else{
                    indexIntervalo = IdadeIntervalo.MAIOR26.ordinal();
                }
                idadeIntervalo[indexIntervalo] += 1;
            }
            idadeExtremo[0] = menorIdade;
            idadeExtremo[1] = maiorIdade;
        }catch(IOException e){
            System.out.println("O ficheiro não foi encontrado");
        }
    }

    private void ArrayInitializer(int[] array){
        Arrays.fill(array, 0);
    }

    @Override
    public String toString(){
        StringBuilder build = new StringBuilder();
        for(IdadeIntervalo l:IdadeIntervalo.values()){
            build.append("Quantidade no intervalo "+l.designacao+" : "+idadeIntervalo[l.ordinal()]+"\n");
        }
        return "============= Estatística de Idade =============\n"+
                "Idade Média: "+idadeMedia+"\n"+
                "Menor idade: "+idadeExtremo[0]+"\n"+
                "Maior idade: "+idadeExtremo[1]+"\n"+
                "\nQuantidade por intervalo \n"+build.toString();
    }
}
enum IdadeIntervalo{
    MENOR18("< 18"),
    ENTRE18E20("18-20"),
    ENTRE21E23("21-23"),
    ENTRE24E26("24-26"),
    MAIOR26("> 26");
    String designacao;

   IdadeIntervalo(String designacao){
        this.designacao = designacao;
    }

}
