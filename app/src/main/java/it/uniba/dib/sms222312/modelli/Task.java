package it.uniba.dib.sms222312.modelli;

import java.io.Serializable;
import java.util.List;

public class Task implements Serializable {
    private String tesista;
    private String nome;
    private String descrizione;
    private String stato;
    private String scadenza;
    private List<String> file;

    public Task(){}

    public Task(String nome) {
        this.nome = nome;
    }

    public Task(String tesista, String nome, String descrizione, String scadenza, String stato, List<String> file) {
        this.tesista = tesista;
        this.nome = nome;
        this.descrizione = descrizione;
        this.stato = scadenza;
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
