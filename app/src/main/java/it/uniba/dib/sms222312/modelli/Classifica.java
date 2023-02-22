package it.uniba.dib.sms222312.modelli;

public class Classifica {
    private String utente;
    private String nome;
    private String durata;
    private String media;

    public Classifica(String utente, String nome, String durata, String media) {
        this.utente = utente;
        this.nome = nome;
        this.durata = durata;
        this. media = media;
    }

    public String getDurata() {
        return durata;
    }

    public String getMedia() {
        return media;
    }

    public Classifica() {
    }

    @Override
    public String toString() {
        return "Classifica{" +
                "utente='" + utente + '\'' +
                ", nome='" + nome + '\'' +
                ", durata='" + durata + '\'' +
                ", media='" + media + '\'' +
                '}';
    }

    public String getUtente() {
        return utente;
    }

    public String getNome() {
        return nome;
    }
}
