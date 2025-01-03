package data;

public class Proverb {
    private int id;
    private String proverb;
    private String author;

    public Proverb(int id, String proverb, String author) {
        this.id = id;
        this.proverb = proverb;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProverb() {
        return proverb;
    }

    public void setProverb(String proverb) {
        this.proverb = proverb;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Proverb{" +
                "id=" + id +
                ", proverb='" + proverb + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
