package it.uniba.dib.sms222312.modelli;

public class Docente {
    private String id;
    private String email;
    private String nome;
    private String cognome;

    public Docente(String email, String nome, String cognome) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }
}
