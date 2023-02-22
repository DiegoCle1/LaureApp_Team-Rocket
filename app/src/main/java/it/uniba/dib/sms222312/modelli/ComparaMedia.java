package it.uniba.dib.sms222312.modelli;

import java.util.Comparator;

public class ComparaMedia implements Comparator<Classifica> {
    @Override
    public int compare(Classifica classifica, Classifica t1) {
        return classifica.getMedia().compareTo(t1.getMedia());
    }
}
