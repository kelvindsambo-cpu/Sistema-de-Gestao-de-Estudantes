package estatistica;
import utils.Rounder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class EstatisticaDasMedias extends Estatistica{
        private double mediaGeral;
        private Student[] estudanteExtremo;
        private int[] quantidade;

        public EstatisticaDasMedias() {
            this.mediaGeral = calcularMediaGeral();
            this.estudanteExtremo = new Student[2];
            this.quantidade = new int[4];
            Arrays.fill(estudanteExtremo, null);
            Arrays.fill(quantidade, 0);
            mediaPorIntervalo(estudanteExtremo, quantidade);
    }

    private double calcularMediaGeral(){
            double media = 0;
            try(BufferedReader br = ficheiroEstudante.abrirFicheiro()){
                br.readLine();
                while(br.ready()){
                    String[] tokens = br.readLine().split(",");
                    media += Double.parseDouble(tokens[6]);
                }
            }catch(IOException e){
                System.out.println("Não foi possível abrir o ficheiro");
            }

            return Rounder.round(media/totalEstudantes,1);
    }

    private void mediaPorIntervalo(Student[] extremo,int[] quantidade){
            int indexIntervalo;
            double menorMedia = 99, maiorMedia = 0,media;
            String nome;
            try(BufferedReader br = ficheiroEstudante.abrirFicheiro()){
                br.readLine();
                while(br.ready()){
                    String[] tokens = br.readLine().split(",");
                    media = Double.parseDouble(tokens[6]);
                    nome = tokens[1];
                    if(media < menorMedia){
                        menorMedia = media;
                        extremo[0] = new Student(nome,media);
                    }else if(media > maiorMedia){
                        maiorMedia = media;
                        extremo[1] = new Student(nome,media);
                    }

                    if(media < 10){
                        indexIntervalo = MediaIntervalo.MENOR10.ordinal();
                    } else if (media >= 10 && media < 14 ) {
                        indexIntervalo = MediaIntervalo.ENTRE10E14.ordinal();
                    } else if (media >= 14 && media < 17){
                        indexIntervalo = MediaIntervalo.ENTRE14E17.ordinal();
                    } else{
                        indexIntervalo = MediaIntervalo.MAIOR17.ordinal();
                    }
                    quantidade[indexIntervalo] += 1;
                }
            }catch(IOException e){
                System.out.println("Não foi possível abrir o ficheiro.");
            }
    }

    @Override
    public String toString() {
            StringBuilder relatorio = new StringBuilder();
        for (MediaIntervalo i: MediaIntervalo.values()) {
            relatorio.append("Quantidade no intervalo "+i.nome+": "+quantidade[i.ordinal()]+"\n");
        }
        return "============= Estatísticas das médias ==========\n"+
                "Média académica geral: "+mediaGeral+"\n"+
                "Estudante com maior média: "+estudanteExtremo[1].nome+"   Média: "+estudanteExtremo[1].media+"\n"+
                "Estudante com menor média: "+estudanteExtremo[0].nome+"   Média: "+estudanteExtremo[0].media+"\n"+
                "Medias por intervalo\n"+
                relatorio;

    }
}

class Student{
    String nome;
    double media;
    public Student(String nome,double media){
        this.nome = nome;
        this.media = media;
    }
}

enum MediaIntervalo{
    MENOR10("< 10"),
    ENTRE10E14("10-13,9"),
    ENTRE14E17("14-16,9"),
    MAIOR17("> 17");
    String nome;
    MediaIntervalo(String nome){this.nome = nome;}
}

