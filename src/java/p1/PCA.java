package p1;

import cern.colt.list.DoubleArrayList;
import cern.colt.matrix.*;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import cern.jet.stat.Descriptive;
import java.util.*;

public class PCA {

    public static DoubleMatrix2D calculatePCA(double[][] data) {
        DoubleMatrix2D dataMat = new DenseDoubleMatrix2D(data);
        
        int dim = 20 ; 
        int rows = dataMat.rows();
        int cols = dataMat.columns();

        double[] mean = new double[rows];
        double sum = 0.0;
        for (int i = 0; i < rows; i++) {
            sum = 0.0;
            for (int j = 0; j < cols; j++) {
                sum += dataMat.getQuick(i, j);

            }
            mean[i] = sum / cols;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dataMat.setQuick(i, j, dataMat.getQuick(i, j) - mean[i]);

            }
        }

        DoubleMatrix2D cov = calcCovariance(dataMat);
        // get eigenvectors and eigenvalues
        EigenvalueDecomposition matEig = new EigenvalueDecomposition(cov);
        DoubleMatrix2D eigenVecs = matEig.getV();
        System.out.println("\nEigenvectors: \n" + eigenVecs);
        DoubleMatrix1D realEigVals = matEig.getRealEigenvalues();
        System.out.println("\nEigenvalues: \n" + realEigVals);
        
        TreeMap<Double,Integer> sortedVals = new TreeMap<Double,Integer>();
        for(int i = 0 ; i< realEigVals.size() ; i++)
        {
            sortedVals.put(realEigVals.get(i),i);
        }
        
        List<Integer> indices = new ArrayList<Integer>();
        Set<Double> sorted = sortedVals.descendingKeySet();
        
        for(Double key : sorted)
        {
            indices.add(sortedVals.get(key));
        }
        
        DoubleMatrix2D sortMat = new DenseDoubleMatrix2D(eigenVecs.rows(),dim);
        
        for(int i = 0 ; i < dim ; i++)
        {
            DoubleMatrix1D columnMat = eigenVecs.viewColumn(indices.get(i));
            for(int j = 0 ; j < eigenVecs.rows() ; j++)
            {
                sortMat.setQuick(j, i, columnMat.get(j) );
            }
            
        }
        System.out.println("\nSorted Matrix: \n" + sortMat);
        
        Algebra a = new Algebra();
        DoubleMatrix2D feature = a.mult(a.transpose(sortMat), dataMat);
        
         System.out.println("\nfeature: \n" + feature);
        
         return feature;
        
    }

    public static DoubleMatrix2D calcCovariance(DoubleMatrix2D dataMat) {
        int numRows = dataMat.rows();
        DoubleMatrix2D matCov = new DenseDoubleMatrix2D(numRows, numRows);
        for (int i = 0; i < numRows; i++) {
            DoubleArrayList iRow = new DoubleArrayList(dataMat.viewRow(i).toArray());
            double variance = Descriptive.covariance(iRow, iRow);
            matCov.setQuick(i, i, variance); // main diagonal value
            // fill values symmetrically around main diagonal
            for (int j = i + 1; j < numRows; j++) {
                double cov = Descriptive.covariance(iRow,
                        new DoubleArrayList(dataMat.viewRow(i).toArray()));
                matCov.setQuick(i, j, cov); // fill to the right
                matCov.setQuick(j, i, cov); // fill below
            }
        }
        return matCov;


    }

}
