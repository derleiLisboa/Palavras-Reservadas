package br.edu.unidavi.palavrasreservadas;

import java.util.ArrayList;
import java.util.List;

public final class Storage {
    public Palavra[] getPalavrasReservadas(){
        String[] strings = {"abstract","assert","boolean","break","byte","case","catch","char","class","const",
                "continue","default","do","double","else","enum","extends","final","finally","float",
                "for","goto","if","implements","import","instanceof","int","interface","long","native",
                "new","package","private","protected","public","return","short","static","strictfp",
                "super","switch","synchronized","this","throw","throws","transient","try","void",
                "volatile","while"};

        List<Palavra> palavras = new ArrayList<>();

        for (String s: strings) {
            palavras.add(new Palavra(s));
        }

        return palavras.toArray(new Palavra[50]);
    }
}
