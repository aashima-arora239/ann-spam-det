package p1;

/*
 * Term DTO class defines a term having an inverseDocumentfrequency  and a collection frequency .IDF being per term.
 * 
 */
public class TermDTO {

    private String term;
    private double inverseDocumentFrequency;

    public String getTerm() {
        return term;
    }

    public double getInverseDocumentFrequency() {
        return inverseDocumentFrequency;
    }


    public void setTerm(String term) {
        this.term = term;
        
    }

    public void setInverseDocumentFrequency(double inverseDocumentFrequency) {
        this.inverseDocumentFrequency = inverseDocumentFrequency;
    }


    public TermDTO() {
    }

    public TermDTO(String term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object o) {
        TermDTO match = null;

        if (o instanceof TermDTO) {
            match = (TermDTO) o;

        }
        String s1 = this.term;
        String s2 = match.term;
        if (s1.equals(s2)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.term != null ? this.term.hashCode() : 0);
        return hash;
    }
}
