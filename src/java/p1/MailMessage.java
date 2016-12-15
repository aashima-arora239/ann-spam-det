package p1;

import java.io.File;
import java.util.*;

public class MailMessage {

    static int count;
    int MailId;
    int label;
    String sentFrom;
    String subject;
    File body;
    Map<String,Double> mailVector;
    
    public MailMessage(int MailId)
    {
        this.MailId = MailId;
    }

    public MailMessage(int MailId,int label)
    {
        this.MailId = MailId;
        this.label = label;
    }
    public static int getCount() {
        return count;
    }

    public static int assignId() {
        int id = getCount();
        id++;
        count = id;
        return id;

    }

    public File getBody() {
        return body;
    }

    public void setBody(File body) {
        this.body = body;
    }
    
    public int getMailId() {
        return MailId;
    }

    public void setMailId(int MailId) {
        this.MailId = MailId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }
    
    public Map<String,Double> getMailVector() {
        return mailVector;
    }
    
    public void setMailVector(Map<String,Double> mailVector) {
        this.mailVector = mailVector;
    }
  
}


