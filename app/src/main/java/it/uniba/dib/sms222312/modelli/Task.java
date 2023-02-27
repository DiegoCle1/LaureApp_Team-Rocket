package it.uniba.dib.sms222312.modelli;

import java.util.List;

public class Task {
    private String tesista;
    private String nome;
    private String descrizione;
    private String stato;
    private String scadenza;
    private List<String> file;

    public Task(){}

    public Task(String tesista, String nome, String descrizione, String stato, String scadenza, List<String> file) {
        this.tesista = tesista;
        this.nome = nome;
        this.descrizione = descrizione;
        this.stato = stato;
        this.scadenza = scadenza;
        this.file = file;
    }

    public String getTesista() {
        return tesista;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getStato() {
        return stato;
    }

    public String getScadenza() {
        return scadenza;
    }

    public List<String> getFile() {
        return file;
    }
}
