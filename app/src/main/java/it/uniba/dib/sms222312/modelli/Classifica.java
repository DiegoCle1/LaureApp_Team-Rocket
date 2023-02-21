package it.uniba.dib.sms222312.modelli;

public class Classifica {
    private String utente;
    private String nome;

    public Classifica(String utente, String nome) {
        this.utente = utente;
        this.nome = nome;
    }

    public Classifica() {
    }

    public String getUtente() {
        return utente;
    }

    public String getNome() {
        return nome;
    }
}
