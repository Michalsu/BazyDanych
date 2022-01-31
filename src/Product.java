public class Product {
    int ID;
    String nazwa;
    String opis;
    float cena;
    int promocja;
    String kategoria;
    int ilosc;

    public Product(int ID, String nazwa, String opis, float cena, int promocja, String kategoria, int ilosc) {
        this.ID = ID;
        this.nazwa = nazwa;
        this.opis = opis;
        this.cena = cena;
        this.promocja = promocja;
        this.kategoria = kategoria;
        this.ilosc = ilosc;
    }


    @Override
    public String toString() {
        return "Product{" +
                "ID=" + ID +
                ", nazwa='" + nazwa + '\'' +
                ", opis='" + opis + '\'' +
                ", cena=" + cena +
                ", promocja=" + promocja +
                ", kategoria='" + kategoria + '\'' +
                ", ilosc=" + ilosc +
                '}'+"\n";
    }
}
