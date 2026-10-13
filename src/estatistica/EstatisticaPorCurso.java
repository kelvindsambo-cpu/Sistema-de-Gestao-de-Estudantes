package estatistica;

import java.io.BufferedReader;
import java.io.IOException;

public class EstatisticaPorCurso extends Estatistica{
    private Curso[] cursos ;

    public EstatisticaPorCurso(){
        this.cursos = new Curso[4];
        for (int i = 0; i < cursos.length; i++) {
            cursos[i] = new Curso();
        }
        totalPorCurso(cursos);
        percentagemPorCurso(cursos);
    }

    private void totalPorCurso(Curso[] cursos){
        try(BufferedReader br = ficheiroEstudante.abrirFicheiro()){
            br.readLine();
            while (br.ready()) {
                String[] tokens = br.readLine().split(",");
                double media = Double.parseDouble(tokens[6]);
                int index = switch(tokens[2]){
                    case "Engenharia Informatica" -> NumCurso.INFORMATICA.ordinal();
                    case "Engenharia Electronica" -> NumCurso.ELECTRONICA.ordinal();
                    case "Engenharia Telecomunicacoes" -> NumCurso.TELECOMUNICACOES.ordinal();
                    case "Engenharia de Redes" -> NumCurso.REDES.ordinal();
                    default -> -1;
                };
                cursos[index].total++;
                cursos[index].media += media;
            }
            for(NumCurso i : NumCurso.values()){
                Curso curso = cursos[i.ordinal()];
                curso.media /= curso.total;
                curso.media = (double) Math.round(curso.media*100) /100;
            }
        } catch(IOException e) {
            System.out.println("Não foi possível ler o ficheiro");
        }
    }

    private void percentagemPorCurso(Curso[] cursos){
        for(NumCurso i : NumCurso.values()){
            Curso curso = cursos[i.ordinal()];
            curso.nome = i.nome;
            curso.percentagem = (double) curso.total / totalEstudantes * 100 ;
        }
    }
    @Override
    public String toString(){
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("============= Estatistica por Curso ==================\n");
        for(Curso c: cursos){
            relatorio.append(
                    "Engenharia "+c.nome+"\n"+
                            "------------------------------\n"+
                            "Estudantes: "+c.total+"\n"+
                            "Percentagem: "+c.percentagem+"%\n"+
                            "Média: "+c.media+"\n\n"

            );
        }
        return relatorio.toString();
    }
}

class Curso{
     String nome;
     double media;
     int total;
     double percentagem;

     public Curso(){
         this.media = 0;
         this.total = 0;
         this.percentagem = 0;
     }

}

enum NumCurso{
    INFORMATICA("Informática"),
    ELECTRONICA("Electrônica"),
    TELECOMUNICACOES("Telecomunicações"),
    REDES("De Redes");
    String nome;
    NumCurso(String nome){
        this.nome = nome;
    }
}
