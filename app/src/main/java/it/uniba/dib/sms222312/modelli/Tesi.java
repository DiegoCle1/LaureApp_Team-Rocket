package it.uniba.dib.sms222312.modelli;

public class Tesi {
    private String docente;
    private String nome;
    private String corso;
    private String ore;
    private String media;
    private String descrizione;

    public Tesi(){}
    public Tesi(String docente, String nome, String corso, String oreDurata, String mediaVoti, String descrizione) {
        this.docente = docente;
        this.nome = nome;
        this.corso = corso;
        this.ore = oreDurata;
        this.media = mediaVoti;
        this.descrizione = descrizione;
    }

    public String getDocente() {
        return docente;
    }

    public String getNome() {
        return nome;
    }

    public String getCorso() {
        return corso;
    }

    public String getOre() {
        return ore;
    }

    @Override
    public String toString() {
        return "Tesi{" +
                "docente='" + docente + '\'' +
                ", nome='" + nome + '\'' +
                ", corso='" + corso + '\'' +
                ", ore='" + ore + '\'' +
                ", media='" + media + '\'' +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }

    public String getMedia() {
        return media;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
