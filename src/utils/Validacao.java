package utils;

import model.Estudante;

public class Validacao {

    public static final int ANO_MIN = 1;
    public static final int ANO_MAX = 4;
    public static final int IDADE_MIN = 17;
    public static final int IDADE_MAX = 70;
    public static final double MEDIA_MIN = 0;
    public static final double MEDIA_MAX = 20;

    public static Resposta validarEstudante(Estudante estudante) {

        if (estudante == null) {
            return invalido("Estudante nulo.");
        }
        if (vazio(estudante.getCodigo())) {
            return invalido("O codigo nao pode estar vazio.");
        }
        if (vazio(estudante.getNome())) {
            return invalido("O nome nao pode estar vazio.");
        }
        if (vazio(estudante.getCurso())) {
            return invalido("O curso nao pode estar vazio.");
        }
        if (vazio(estudante.getSexo())
                || (!estudante.getSexo().trim().equalsIgnoreCase("M")
                    && !estudante.getSexo().trim().equalsIgnoreCase("F"))) {
            return invalido("O sexo deve ser M ou F.");
        }
        if (estudante.getAno() < ANO_MIN || estudante.getAno() > ANO_MAX) {
            return invalido("O ano deve estar entre " + ANO_MIN + " e " + ANO_MAX + ".");
        }
        if (estudante.getIdade() < IDADE_MIN || estudante.getIdade() > IDADE_MAX) {
            return invalido("A idade deve estar entre " + IDADE_MIN + " e " + IDADE_MAX + ".");
        }
        if (!(estudante.getMedia() >= MEDIA_MIN && estudante.getMedia() <= MEDIA_MAX)) {
            return invalido("A media deve estar entre 0 e 20.");
        }

        return new Resposta("Os dados do estudante sao validos", EstadoValidacao.VALIDO);
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private static Resposta invalido(String mensagem) {
        return new Resposta(mensagem, EstadoValidacao.INVALIDO);
    }

}
