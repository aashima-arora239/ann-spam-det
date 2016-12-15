package p1;

public class MailDTO {
    
    int MailId;
    String term;
    int tf;
    private double tfidf;
    
    public MailDTO(int MailId, String term,int tf) {
        this.MailId = MailId;
        this.term = term;
        this.tf = tf;
    }
    
    public int getMailId() {
        return MailId;
    }
    
    public void setMailId(int MailId) {
        this.MailId = MailId;
    }
    
    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    public int getTf() {
        return tf;
    }
    
    public void setTf(int tf) {
        this.tf = tf;
    }
    
    public double getTfidf() {
        return tfidf;
    }
    
    public void setTfidf(double tfidf) {
        this.tfidf = tfidf;
    }
    
    @Override
    public boolean equals(Object obj) {
        MailDTO mailObj = null;
        if (obj instanceof MailDTO) {
            mailObj = (MailDTO) obj;
        }
        if (this.term.equals(mailObj.term)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.term != null ? this.term.hashCode() : 0);
        return hash;
    }
}

