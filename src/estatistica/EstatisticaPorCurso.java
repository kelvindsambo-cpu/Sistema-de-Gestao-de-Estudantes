package estatistica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Estudante;
import utils.Rounder;

public class EstatisticaPorCurso extends Estatistica{
    private final List<Curso> cursos;

    public EstatisticaPorCurso(List<Estudante> estudantes){
        super(estudantes);
        this.cursos = new ArrayList<>();
        totalPorCurso();
        cursos.sort(Comparator.comparing(c -> c.nome));
    }

    //A classe faz calculo das médias e conta o total de alunos por curso
    private void totalPorCurso(){
        for (Estudante e : estudantes) {
            Curso curso = procurarCurso(e.getCurso());
            if (curso == null) {
                curso = new Curso(e.getCurso());
                cursos.add(curso);
            }
            curso.total++;
            curso.somaMedias += e.getMedia();
        }
    }

    private Curso procurarCurso(String nome) {
        for (Curso c : cursos) {
            if (c.nome.equals(nome)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("============= Estatistica por Curso ==================\n");
        for(Curso c: cursos){
            relatorio.append(
                    c.nome+"\n"+
                            "--------------------------------\n"+
                            "Estudantes : "+c.total+"\n"+
                            "Percentagem : "+Rounder.formatar(percentagem(c.total),2)+"%\n"+
                            "Media : "+Rounder.formatar(c.somaMedias / c.total,2)+"\n\n"

            );
        }
        return relatorio.toString();
    }
}

//Classes auxiliares
class Curso{
     final String nome;
     double somaMedias;
     int total;

     public Curso(String nome){
         this.nome = nome;
         this.somaMedias = 0;
         this.total = 0;
     }

}
