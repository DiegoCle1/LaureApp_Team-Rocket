package it.uniba.dib.sms222312.modelli;

public class Studente {
    private String id;
    private String matricola;
    private String email;
    private String nome;
    private String cognome;
    private String corso;

    public Studente(String id, String matricola, String email, String nome, String cognome, String corso) {
        this.id = id;
        this.matricola = matricola;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.corso = corso;
    }

    public String getId() {
        return id;
    }

    public String getMatricola() {
        return matricola;
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
