package p1;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import com.google.common.base.CharMatcher;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import edu.stanford.nlp.trees.PennTreebankTokenizer;
import org.jsoup.Jsoup;

public class Utils {

     public static List<MailMessage> ReadHamMail() throws IOException {
        List<MailMessage> readHamMails = new ArrayList<MailMessage>();
        int readCtr = MailMessage.getCount();
        Properties props = System.getProperties();

        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com","vashisht.gunjan10@gmail.com", "bALWAZZ+ve");
            Folder ham = store.getFolder("Inbox");
            ham.open(Folder.READ_WRITE);
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN),false);
            Message[] message = ham.search(ft);
            

// Display message.

            for (int i = 0; i < message.length; i++) {

                String nameBody = "b" + readCtr + ".txt";

                File f1 = new File("C:\\Users\\MY PC\\Documents\\NetBeansProjects\\Spamdet\\ReadHam", nameBody);
                MailMessage newM = new MailMessage(MailMessage.assignId(),1);
                FileWriter fw = new FileWriter(f1);
                System.out.println("------------ Message " + (i + 1) + " ------------");

                System.out.println("SentDate : " + message[i].getSentDate());
                System.out.println("From : " + message[i].getFrom()[0]);
                newM.setSentFrom(message[i].getFrom()[0].toString());
                System.out.println("Subject : " + message[i].getSubject());
                newM.setSubject(message[i].getSubject());
                System.out.print("Message : ");


                Object content = message[i].getContent();
                if (content instanceof String) {
                    String body = (String) content;
                    body = Jsoup.parse(body).text();
                    fw.write(body);
                    fw.flush();


                } else if (content instanceof Multipart) {
                    Multipart mp = (Multipart) content;
                    int bodyParts = mp.getCount();
                    System.out.println("BODY PARTS---->" + bodyParts); // There are 2 body parts
                    for (int j = 0; j < bodyParts; j++) {
                        BodyPart bodyPart = mp.getBodyPart(j);
                        Object o2 = bodyPart.getContent();

                        if (o2 instanceof String) {
                            String body2 = o2.toString();
                            body2 = Jsoup.parse(body2).text();
                            fw.write(body2);
                            fw.flush();
                        }
                    }
                }
                //message[i].setFlag(Flags.Flag.SEEN, true);
                newM.setBody(f1);
                readHamMails.add(newM);
                readCtr++;
            }
            ham.close(true);
            store.close();
            System.out.println("Reached end of read mails");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return readHamMails;
    }
   public static List<MailMessage> ReadMail() throws IOException {
        List<MailMessage> readMails = new ArrayList<MailMessage>();
        int readCtr = MailMessage.getCount();
        Properties props = System.getProperties();

        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com","arora.aa91@gmail.com","livinlife@webof91");
            Folder spam = store.getFolder("Inbox");
            spam.open(Folder.READ_WRITE);
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN),false);
            Message[] message = spam.search(ft);
            

// Display message.

            for (int i = 0; i < message.length; i++) {

                String nameBody = "b" + readCtr + ".txt";

                File f1 = new File("C:\\Users\\MY PC\\Documents\\NetBeansProjects\\Spamdet\\ReadMails", nameBody);
                MailMessage newM = new MailMessage(MailMessage.assignId(),1);
                FileWriter fw = new FileWriter(f1);
                System.out.println("------------ Message " + (i + 1) + " ------------");

                System.out.println("SentDate : " + message[i].getSentDate());
                System.out.println("From : " + message[i].getFrom()[0]);
                newM.setSentFrom(message[i].getFrom()[0].toString());
                System.out.println("Subject : " + message[i].getSubject());
                newM.setSubject(message[i].getSubject());
                System.out.print("Message : ");


                Object content = message[i].getContent();
                if (content instanceof String) {
                    String body = (String) content;
                    body = Jsoup.parse(body).text();
                    fw.write(body);
                    fw.flush();


                } else if (content instanceof Multipart) {
                    Multipart mp = (Multipart) content;
                    int bodyParts = mp.getCount();
                    System.out.println("BODY PARTS---->" + bodyParts); // There are 2 body parts
                    for (int j = 0; j < bodyParts; j++) {
                        BodyPart bodyPart = mp.getBodyPart(j);
                        Object o2 = bodyPart.getContent();

                        if (o2 instanceof String) {
                            String body2 = o2.toString();
                            body2 = Jsoup.parse(body2).text();
                            fw.write(body2);
                            fw.flush();
                        }
                    }
                }
                //message[i].setFlag(Flags.Flag.SEEN, true);
                newM.setBody(f1);
                readMails.add(newM);
                readCtr++;
            }
            spam.close(true);
            store.close();
            System.out.println("Reached end of read mails");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return readMails;
    }
 public static List<MailMessage> ReadInbox() throws IOException {
        List<MailMessage> inboxMail = new ArrayList<MailMessage>();
        int readCtr = MailMessage.getCount();
        Properties props = System.getProperties();

        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "arora.aa91@gmail.com","livinlife@webof91");
            Folder inbox = store.getFolder("tested154_4S--6--7");
            inbox.open(Folder.READ_WRITE);
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message[] message = inbox.search(ft);
            

// Display message.

            for (int i = 0; i < message.length; i++) {

                String nameBody = "b" + readCtr + ".txt";

                File f1 = new File("C:\\Users\\MY PC\\Documents\\NetBeansProjects\\Spamdet\\ReadInbox", nameBody);
                MailMessage newM = new MailMessage(MailMessage.assignId(),1);
                FileWriter fw = new FileWriter(f1);
                System.out.println("------------ Message " + (i + 1) + " ------------");

                System.out.println("SentDate : " + message[i].getSentDate());
                System.out.println("From : " + message[i].getFrom()[0]);
                newM.setSentFrom(message[i].getFrom()[0].toString());
                System.out.println("Subject : " + message[i].getSubject());
                newM.setSubject(message[i].getSubject());
                System.out.print("Message : ");


                Object content = message[i].getContent();
                if (content instanceof String) {
                    String body = (String) content;
                    body = Jsoup.parse(body).text();
                    fw.write(body);
                    fw.flush();


                } else if (content instanceof Multipart) {
                    Multipart mp = (Multipart) content;
                    int bodyParts = mp.getCount();
                    System.out.println("BODY PARTS---->" + bodyParts); // There are 2 body parts
                    for (int j = 0; j < bodyParts; j++) {
                        BodyPart bodyPart = mp.getBodyPart(j);
                        Object o2 = bodyPart.getContent();

                        if (o2 instanceof String) {
                            String body2 = o2.toString();
                            body2 = Jsoup.parse(body2).text();
                            fw.write(body2);
                            fw.flush();
                        }
                    }
                }
                //message[i].setFlag(Flags.Flag.SEEN, true);
                newM.setBody(f1);
                inboxMail.add(newM);
                readCtr++;
            }
            inbox.close(true);
            store.close();
            System.out.println("Reached end of read mails");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return inboxMail;
    }

    public static void SendMail() throws IOException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("gSaWorld131@gmail.com", "gSa@bvcoe");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gSaWorld131@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("gSaWorld131@gmail.com"));
            message.setSubject("This is Unread Mail");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    static public List<Set<MailDTO>> TokenizeMail(List<MailMessage> mailList) throws IOException {

        List<Set<MailDTO>> mailIndex = new LinkedList<Set<MailDTO>>();
        for (MailMessage mail : mailList) {
            File f = mail.getBody();
            FileReader fr = new FileReader(f);
            List<String> tokens = new LinkedList<String>();
            Set<MailDTO> mailSet = new HashSet<MailDTO>();
            PennTreebankTokenizer ptb = new PennTreebankTokenizer(fr);
            while (ptb.hasNext()) {
                String token = ptb.next();
                token = CharMatcher.is('.').or(CharMatcher.is('!')).or(CharMatcher.is(',')).or(CharMatcher.is('?')).or(CharMatcher.is(';')).or(CharMatcher.is(':')).or(CharMatcher.is(' ')).trimTrailingFrom(token);
                token = CharMatcher.anyOf("(").or(CharMatcher.anyOf(")")).or(CharMatcher.anyOf("[")).or(CharMatcher.anyOf("]")).or(CharMatcher.anyOf("%")).or(CharMatcher.anyOf("-")).or(CharMatcher.anyOf("+")).or(CharMatcher.anyOf("=")).or(CharMatcher.anyOf("^")).or(CharMatcher.anyOf("~")).or(CharMatcher.anyOf("#")).or(CharMatcher.anyOf("_")).or(CharMatcher.anyOf("*")).removeFrom(token);
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
                
            }
            tokens = Utils.stopWordRemove(tokens);
            tokens = Utils.stem(tokens);
            mailSet = Utils.createMailDTO(tokens, mail);
            mailIndex.add(mailSet);



        }

        return mailIndex;

    }

    public static List<String> stopWordRemove(List<String> text) throws IOException {
        Set<String> stopwords = new HashSet<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getResourceAsStream("stopwords//stopwords.txt")));
        String str = br.readLine();
        while (str != null) {
            stopwords.add(str);
            str = br.readLine();
        }
        Iterator<String> textIterator = text.iterator();
        while (textIterator.hasNext()) {
            if (stopwords.contains(textIterator.next().toLowerCase())) {
                textIterator.remove();
            }

        }
        return text;
    }

    public static List<String> stem(List<String> tokens) {
        List<String> stemmedTokens = new LinkedList<String>();
        for (String token : tokens) {
            stemmedTokens.add(Stemmer.stem(token.toLowerCase())); //Stem the tokens
        }
        return stemmedTokens;
    }

    public static Set<MailDTO> createMailDTO(List<String> tokens, MailMessage m) {
        Set<MailDTO> dataSet = new HashSet<MailDTO>();

        Iterator<String> tokenIterator = tokens.iterator();

        String term = null;
        int tf = 0;
        MailDTO data = null;
        while (tokenIterator.hasNext()) {
            term = tokenIterator.next().toLowerCase();
            tf = 1;//Initial Term frequency is set to 1

            //Create a DocumentDTO object.
            data = new MailDTO(m.getMailId(), term, tf);
            boolean add = dataSet.add(data);
            if (add == false) {
                //If duplicated, the term is updated with the term frequency by incrementing it to 1.
                Utils.UpdateSet(dataSet, data);
            }
        }
        return dataSet;
    }

    public static void UpdateSet(Set<MailDTO> set, MailDTO obj) {
        Iterator<MailDTO> setIt = set.iterator(); //Set is the set of DocumentDTO objects
        while (setIt.hasNext()) {
            MailDTO next = setIt.next();
            if (next.equals(obj)) { //obj is the duplicated term whose TF is updated when its located in the set.
                int tf = next.getTf();
                tf++;
                next.setTf(tf);
            }
        }
    }

    public static double calculateIDF(TermDTO termVal, List<MailDTO> mailList) {
        double idf = 0.0;
        double n = mailList.size();
        double N = MailMessage.getCount();
        //The Total number of documents in the corpus divided by the documents 
        //containing the term represented by doclist.
        double idfVal = (N / n);
        idf = Math.log10(idfVal);
        return idf;
    }

    /*
     * Calculate TF-IDF for each term in a document.
     */
    public static double calculateTFIDF(TermDTO termVal, MailDTO mail) {
        double tfIdf = 0.0;
        //double logtf = 1 + Math.log10(doc.getTermFrequency());
        tfIdf = mail.getTf() * termVal.getInverseDocumentFrequency();
        return tfIdf;

    }

    /*
     * Normalize document vectors.
     */
    public static Map<String, Double> normalizeVector(Map<String, Double> vector) {
        double sum = 0.0;
        double mod = 0.0;
        Map<String, Double> normalizedVector = new HashMap<String, Double>();
        Set<String> keys = vector.keySet();
        for (String key : keys) {
            Double vectorValue = vector.get(key);
            sum += Math.pow(vectorValue, 2.0);//sum of squares of the vector.
        }

        mod = Math.sqrt(sum);//Root of the squares.

        for (String key : keys) {
            Double vectorValue = vector.get(key) / mod;
            normalizedVector.put(key, vectorValue);
        }
        return normalizedVector;
    }
}
