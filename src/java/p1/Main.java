package p1;

import cern.colt.matrix.DoubleMatrix2D;
import java.io.*;
import java.util.*;

public class Main {

    static List<MailMessage> mailList = new ArrayList<MailMessage>();
    static List<Set<MailDTO>> mailIndex = new ArrayList<Set<MailDTO>>();
    static Set<TermDTO> termIndex = new HashSet<TermDTO>();
    static HashMap<TermDTO, List<MailDTO>> postingList = new HashMap<TermDTO, List<MailDTO>>();
    static double[][] termMailMat;

    public static void LoadMails() throws Exception {
        Main.CreateMailIndex();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Main.SetTermDTO();  //Setting term Index - Thread 1
            }
        };
        Runnable r1 = new Runnable() {

            @Override
            public void run() {
                Main.ConstructPostingList(); //Constructing posting list - Thread 2

            }
        };
        Runnable r2 = new Runnable() {

            @Override
            public void run() {
                Main.SetVector(); //Setting mail vectors - Thread 3
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r1);
        Thread t3 = new Thread(r2);

        //Wait for thread 1 to complete.
        t1.start();
        t1.join();
        //Wait for thread 2 to complete.
        t2.start();
        t2.join();
        //Wait for thread 3 to complete.
        t3.start();
        t3.join();

        Main.ConstructMatrix();
        DoubleMatrix2D feature = PCA.calculatePCA(termMailMat);
        Random a = new Random();
        List<Double> random = new ArrayList<Double>();
        
        int num = 111;
        while(num > 0 )
        {
            random.add(a.nextDouble() * 2 - 1);
            num--;
        }
        double[] weights = new double[random.size()];
        for(int i = 0 ; i < random.size() ; i++)
        {
            weights[i] = random.get(i).doubleValue();
        }
        
        
        double[] updatedWeights = null;
        try {

            File f1 = new File("C:\\Documents\\weights.ser");
            if (f1.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f1));
                updatedWeights = (double[]) in.readObject();
                in.close();
                Backpropogation.performBackprop(feature, updatedWeights,1);
            } else {
                Backpropogation.performBackprop(feature, weights,1);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }



    }

    public static void CreateMailIndex() throws IOException {
        mailList = Utils.ReadMail();
        System.out.println("Read Mail Called");
        mailIndex = Utils.TokenizeMail(mailList);
        System.out.println("MailIndex created");

    }

    public static void SetTermDTO() {
        Iterator<Set<MailDTO>> mailIndexIterator = mailIndex.iterator();


        while (mailIndexIterator.hasNext()) {
            Set<MailDTO> setVal = mailIndexIterator.next(); //Retrieve set from mailIndex
            Iterator<MailDTO> mailVal = setVal.iterator();
            while (mailVal.hasNext()) {
                MailDTO mail = mailVal.next(); //Retrieve one MailDTO object from set of MailDTO objects.
                String term = mail.getTerm();

                TermDTO termVal = new TermDTO(term);//Create a term object with each MailDTO object 
                termIndex.add(termVal); //Add it to term val.

            }

        }
    }

    public static void ConstructPostingList() {

        TermDTO termValue = null; //Key of the hashmap
        List<MailDTO> mails = null; //Object of the hashmap - Linked list of mails containg the term.

        Iterator<TermDTO> termIterator = termIndex.iterator();

        while (termIterator.hasNext()) {
            termValue = termIterator.next();
            mails = new LinkedList<MailDTO>();
            Iterator<Set<MailDTO>> mailIndexIterator = mailIndex.iterator();
            //Iterate over mailIndex to retrieve a set and iterate over that set to match the term index.
            while (mailIndexIterator.hasNext()) {
                Set<MailDTO> setVal = mailIndexIterator.next();
                Iterator<MailDTO> mailVal = setVal.iterator();
                while (mailVal.hasNext()) {
                    MailDTO mail = mailVal.next();
                    if (mail.getTerm().equals(termValue.getTerm())) {
                        mails.add(mail); //Update the mailList if term found.
                    }

                }

            }
            //Update idf(per term) for each term.
            termValue.setInverseDocumentFrequency(Utils.calculateIDF(termValue, mails));
            for (MailDTO mailValue : mails) {
                //Update TF-IDF(per term per mail) for each mail.
                mailValue.setTfidf(Utils.calculateTFIDF(termValue, mailValue));
            }
            postingList.put(termValue, mails);

        }

    }

    /*
     * Make mail Vectors for each mail.
     */
    public static void SetVector() {
        for (MailMessage m : mailList) {
            Map<String, Double> mailVector = new HashMap<String, Double>(); //Store the mail vector.
            List<MailDTO> mails = new LinkedList<MailDTO>(); //Iterate over MailDTO objects
            boolean termAdded = false;

            //Get ID for current iteration
            int id = m.getMailId();

            for (TermDTO term : termIndex) {
                mails = postingList.get(term);//Retrieve a term from posting list.
                for (MailDTO dVal : mails) {
                    if (dVal.getMailId() == id) {
                        //If the posting list of the term contains the mailID , retrieve the tf-idf and add it to the list.
                        mailVector.put(term.getTerm(), dVal.getTfidf());
                        termAdded = true;
                    }
                }
                if (termAdded == false) {
                    //Add 0 to the list to maintain the order of the term indez if the term is not present.
                    mailVector.put(term.getTerm(), 0.0);
                }
                termAdded = false;
            }

            //Normalize the vector and set it to the current mail object.
            m.setMailVector(mailVector);
        }

    }

    public static void ConstructMatrix() {
        int rows = termIndex.size();
        int columns = mailList.size();
        double[][] result = new double[rows][];
        for (int i = 0; i < rows; ++i) {
            result[i] = new double[columns];
        }

        Iterator<TermDTO> terms = termIndex.iterator();
        int i = 0, j;
        while (terms.hasNext()) {
            j = 0;
            TermDTO termObj = terms.next();
            String term = termObj.getTerm();
            for (MailMessage m : mailList) {

                Map<String, Double> vector = m.getMailVector();
                double value = vector.get(term);
                result[i][j] = value;
                j++;
            }
            i++;

        }

        termMailMat = result;

    }

    public static void main(String[] args) {
        try {
            Main.LoadMails();
            System.out.println("Loaded Mails");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
