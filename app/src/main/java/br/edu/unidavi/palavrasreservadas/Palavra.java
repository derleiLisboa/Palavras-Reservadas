package br.edu.unidavi.palavrasreservadas;

/**
 * Created by Derlei on 25/10/2017.
 */

public class Palavra {

    private ExibicaoPalavra exibicaoPalavra;
    private String palavra;
    private String placeholder;

    public Palavra(String palavra) {
        this.palavra = palavra;
        exibicaoPalavra = ExibicaoPalavra.Oculta;
        setPlaceholder();
    }

    public boolean exibirPrimeiraLetra(){
        if (exibicaoPalavra == ExibicaoPalavra.PrimeiraLetraRevelada)
            return false;

        exibicaoPalavra = ExibicaoPalavra.PrimeiraLetraRevelada;
        char[] letrasPlaceholder = placeholder.toCharArray();
        char[] letrasPalavra = palavra.toCharArray();
        letrasPlaceholder[0] = letrasPalavra[0];
        placeholder = String.copyValueOf(letrasPlaceholder);
        return true;
    }

    public boolean exibirDuasPrimeirasLetras(){
        if (exibicaoPalavra == ExibicaoPalavra.DuasPrimeirasLetrasReveladas)
            return false;

        exibicaoPalavra = ExibicaoPalavra.DuasPrimeirasLetrasReveladas;
        char[] letrasPlaceholder = placeholder.toCharArray();
        char[] letrasPalavra = palavra.toCharArray();
        letrasPlaceholder[0] = letrasPalavra[0];
        letrasPlaceholder[1] = letrasPalavra[1];
        placeholder = String.copyValueOf(letrasPlaceholder);
        return true;
    }

    public boolean exibirPalavraCompleta(){
        if (exibicaoPalavra == ExibicaoPalavra.CompletamenteRevelada)
            return false;

        exibicaoPalavra = ExibicaoPalavra.CompletamenteRevelada;
        placeholder = palavra;
        return true;
    }

    private void setPlaceholder(){
        placeholder = "";
        for (char a: palavra.toCharArray()) {
            placeholder += "*";
        }
    }

    public boolean verificarPalavra(String palavra){
        if (exibicaoPalavra != ExibicaoPalavra.CompletamenteRevelada && this.palavra.equals(palavra)){
            exibirPalavraCompleta();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return placeholder;
    }
}