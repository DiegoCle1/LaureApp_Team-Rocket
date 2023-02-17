package it.uniba.dib.sms222312.modelli;

public class Tesi {
    private String id;
    private String nome;
    private String corso;
    private int oreDurata;
    private int mediaVoti;
    private String descrizione;

    public Tesi(){}
    public Tesi(String id, String nome, String corso, int oreDurata, int mediaVoti, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.corso = corso;
        this.oreDurata = oreDurata;
        this.mediaVoti = mediaVoti;
        this.descrizione = descrizione;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCorso() {
        return corso;
    }

    public int getOreDurata() {
        return oreDurata;
    }

    public int getMediaVoti() {
        return mediaVoti;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
