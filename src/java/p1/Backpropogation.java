package p1;

import cern.colt.matrix.*;
import java.io.*;

public class Backpropogation {

    public static void performBackprop(DoubleMatrix2D data,double[] weights,double output) {
        NeuralNetwork nn = new NeuralNetwork(20, 5, 1);
        double[] bestWeights = null;
        try {

            System.out.println("\nBegin Neural Network Back-Propagation demo\n");

            System.out.println("Creating a 20-input, 5-hidden, 1-output neural network");
            System.out.println("Using sigmoid function for input-to-hidden activation");
            System.out.println("Using sigmoid function for hidden-to-output activation");
            System.out.println("\nInitial 111 random weights and biases");



            System.out.println("Loading neural network weights and biases");
            nn.SetWeights(weights);

            System.out.println("\nSetting inputs:");
            int columnSize = data.columns();
            int i = 0;
            
            while (i < columnSize) {
                double[] xValues = data.viewColumn(i).toArray();
                Helpers.ShowVector(xValues, 2, true);

                double[] initialOutputs = nn.ComputeOutputs(xValues);
                System.out.println("Initial outputs:");
                Helpers.ShowVector(initialOutputs, 4, true);

                double[] tValues = new double[]{output}; // target (desired) values. note these only make sense for tanh output activation
                System.out.println("Target outputs to learn are:");
                Helpers.ShowVector(tValues, 4, true);

                double eta = 0.3;  // learning rate - controls the maginitude of the increase in the change in weights. found by trial and error.
                double alpha = 0.04; // momentum - to discourage oscillation. found by trial and error.
                System.out.println("Setting learning rate (eta) = " + eta + " and momentum (alpha) = " + alpha);

                System.out.println("\nEntering main back-propagation compute-update cycle");
                System.out.println("Stopping when sum absolute error <= 0.01 or 1,000 iterations\n");
                int ctr = 0;
                double[] yValues = nn.ComputeOutputs(xValues); // prime the back-propagation loop
                double error = Error(tValues, yValues);
                while (ctr < 1000 && error > 0.01) {
                    System.out.println("===================================================");
                    System.out.println("iteration = " + ctr);
                    System.out.println("Updating weights and biases using back-propagation");
                    nn.UpdateWeights(tValues, eta, alpha);
                    System.out.println("Computing new outputs:");
                    yValues = nn.ComputeOutputs(xValues);
                    Helpers.ShowVector(yValues, 4, false);
                    System.out.println("\nComputing new error");
                    error = Error(tValues, yValues);
                    System.out.println("Error = " + error);
                    ++ctr;
                }
                System.out.println("===================================================");
                System.out.println("\nBest weights and biases found:");
                bestWeights = nn.GetWeights();
                nn.SetWeights(bestWeights);
                Helpers.ShowVector(bestWeights, 2, true);
                i++;


            }
            System.out.println("End Neural Network Back-Propagation demo\n");
            try {

                //Create files for saving objects.
                ObjectOutput neuralN = new ObjectOutputStream(new FileOutputStream("C:\\Documents\\neural.ser"));
                ObjectOutput weightN = new ObjectOutputStream(new FileOutputStream("C:\\Documents\\weights.ser"));
              
                //Write lists of clusters,termIndex and documents.
                neuralN.writeObject(nn);
                weightN.writeObject(bestWeights);

                //Close the object streams
                neuralN.close();
                weightN.close();


            } catch (IOException ex) {
                ex.printStackTrace();

            }



        } catch (Exception ex) {
            System.out.println("Fatal: " + ex.getMessage());

        }
        
    } // Main

    static double Error(double[] target, double[] output) // sum absolute error. could put into NeuralNetwork class.
    {
        double sum = 0.0;
        for (int i = 0; i < target.length; ++i) {
            sum += Math.abs(target[i] - output[i]);
        }
        return sum;
    }
} // class BackPropagation

class NeuralNetwork implements java.io.Serializable {

    private int numInput;
    private int numHidden;
    private int numOutput;
    private double[] inputs;
    private double[][] ihWeights; // input-to-hidden
    private double[] ihSums;
    private double[] ihBiases;
    private double[] ihOutputs;
    private double[][] hoWeights;  // hidden-to-output
    private double[] hoSums;
    private double[] hoBiases;
    private double[] outputs;
    private double[] oGrads; // output gradients for back-propagation
    private double[] hGrads; // hidden gradients for back-propagation
    private double[][] ihPrevWeightsDelta;  // for momentum with back-propagation
    private double[] ihPrevBiasesDelta;
    private double[][] hoPrevWeightsDelta;
    private double[] hoPrevBiasesDelta;

    public NeuralNetwork(int numInput, int numHidden, int numOutput) {
        this.numInput = numInput;
        this.numHidden = numHidden;
        this.numOutput = numOutput;

        inputs = new double[numInput];
        ihWeights = Helpers.MakeMatrix(numInput, numHidden);
        ihSums = new double[numHidden];
        ihBiases = new double[numHidden];
        ihOutputs = new double[numHidden];
        hoWeights = Helpers.MakeMatrix(numHidden, numOutput);
        hoSums = new double[numOutput];
        hoBiases = new double[numOutput];
        outputs = new double[numOutput];

        oGrads = new double[numOutput];
        hGrads = new double[numHidden];

        ihPrevWeightsDelta = Helpers.MakeMatrix(numInput, numHidden);
        ihPrevBiasesDelta = new double[numHidden];
        hoPrevWeightsDelta = Helpers.MakeMatrix(numHidden, numOutput);
        hoPrevBiasesDelta = new double[numOutput];
    }

    public void UpdateWeights(double[] tValues, double eta, double alpha) throws Exception // update the weights and biases using back-propagation, with target values, eta (learning rate), alpha (momentum)
    {
        // assumes that SetWeights and ComputeOutputs have been called and so all the internal arrays and matrices have values (other than 0.0)
        if (tValues.length != numOutput) {
            throw new Exception("target values not same Length as output in UpdateWeights");
        }

        // 1. compute output gradients
        for (int i = 0; i < oGrads.length; ++i) {
            double derivative = (1 - outputs[i]) * (1 + outputs[i]); // derivative of tanh
            oGrads[i] = derivative * (tValues[i] - outputs[i]);
        }

        // 2. compute hidden gradients
        for (int i = 0; i < hGrads.length; ++i) {
            double derivative = (1 - ihOutputs[i]) * ihOutputs[i]; // (1 / 1 + exp(-x))'  -- using output value of neuron
            double sum = 0.0;
            for (int j = 0; j < numOutput; ++j) // each hidden delta is the sum of numOutput terms
            {
                sum += oGrads[j] * hoWeights[i][j]; // each downstream gradient * outgoing weight
            }
            hGrads[i] = derivative * sum;
        }

        // 3. update input to hidden weights (gradients must be computed right-to-left but weights can be updated in any order
        for (int i = 0; i < ihWeights.length; ++i) // 0..2 (3)
        {
            for (int j = 0; j < ihWeights[0].length; ++j) // 0..3 (4)
            {
                double delta = eta * hGrads[j] * inputs[i]; // compute the new delta
                ihWeights[i][j] += delta; // update
                ihWeights[i][j] += alpha * ihPrevWeightsDelta[i][j]; // add momentum using previous delta. on first pass old value will be 0.0 but that's OK.
            }
        }

        // 3b. update input to hidden biases
        for (int i = 0; i < ihBiases.length; ++i) {
            double delta = eta * hGrads[i] * 1.0; // the 1.0 is the constant input for any bias; could leave out
            ihBiases[i] += delta;
            ihBiases[i] += alpha * ihPrevBiasesDelta[i];
        }

        // 4. update hidden to output weights
        for (int i = 0; i < hoWeights.length; ++i) // 0..3 (4)
        {
            for (int j = 0; j < hoWeights[0].length; ++j) // 0..1 (2)
            {
                double delta = eta * oGrads[j] * ihOutputs[i];  // see above: ihOutputs are inputs to next layer
                hoWeights[i][j] += delta;
                hoWeights[i][j] += alpha * hoPrevWeightsDelta[i][j];
                hoPrevWeightsDelta[i][j] = delta;
            }
        }

        // 4b. update hidden to output biases
        for (int i = 0; i < hoBiases.length; ++i) {
            double delta = eta * oGrads[i] * 1.0;
            hoBiases[i] += delta;
            hoBiases[i] += alpha * hoPrevBiasesDelta[i];
            hoPrevBiasesDelta[i] = delta;
        }
    } // UpdateWeights

    public void SetWeights(double[] weights) throws Exception {
        // copy weights and biases in weights[] array to i-h weights, i-h biases, h-o weights, h-o biases
        int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
        if (weights.length != numWeights) {
            throw new Exception("The weights array length: " + weights.length + " does not match the total number of weights and biases: " + numWeights);
        }

        int k = 0; // points into weights param

        for (int i = 0; i < numInput; ++i) {
            for (int j = 0; j < numHidden; ++j) {
                ihWeights[i][j] = weights[k++];
            }
        }

        for (int i = 0; i < numHidden; ++i) {
            ihBiases[i] = weights[k++];
        }

        for (int i = 0; i < numHidden; ++i) {
            for (int j = 0; j < numOutput; ++j) {
                hoWeights[i][j] = weights[k++];
            }
        }

        for (int i = 0; i < numOutput; ++i) {
            hoBiases[i] = weights[k++];
        }
    }

    public double[] GetWeights() {
        int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
        double[] result = new double[numWeights];
        int k = 0;
        for (int i = 0; i < ihWeights.length; ++i) {
            for (int j = 0; j < ihWeights[0].length; ++j) {
                result[k++] = ihWeights[i][j];
            }
        }
        for (int i = 0; i < ihBiases.length; ++i) {
            result[k++] = ihBiases[i];
        }
        for (int i = 0; i < hoWeights.length; ++i) {
            for (int j = 0; j < hoWeights[0].length; ++j) {
                result[k++] = hoWeights[i][j];
            }
        }
        for (int i = 0; i < hoBiases.length; ++i) {
            result[k++] = hoBiases[i];
        }
        return result;
    }

    public double[] ComputeOutputs(double[] xValues) throws Exception {
        if (xValues.length != numInput) {
            throw new Exception("Inputs array length " + inputs.length + " does not match NN numInput value " + numInput);
        }

        for (int i = 0; i < numHidden; ++i) {
            ihSums[i] = 0.0;
        }
        for (int i = 0; i < numOutput; ++i) {
            hoSums[i] = 0.0;
        }

        for (int i = 0; i < xValues.length; ++i) // copy x-values to inputs
        {
            this.inputs[i] = xValues[i];
        }

        for (int j = 0; j < numHidden; ++j) // compute input-to-hidden weighted sums
        {
            for (int i = 0; i < numInput; ++i) {
                ihSums[j] += this.inputs[i] * ihWeights[i][j];
            }
        }

        for (int i = 0; i < numHidden; ++i) // add biases to input-to-hidden sums
        {
            ihSums[i] += ihBiases[i];
        }

        for (int i = 0; i < numHidden; ++i) // determine input-to-hidden output
        {
            ihOutputs[i] = SigmoidFunction(ihSums[i]);
        }

        for (int j = 0; j < numOutput; ++j) // compute hidden-to-output weighted sums
        {
            for (int i = 0; i < numHidden; ++i) {
                hoSums[j] += ihOutputs[i] * hoWeights[i][j];
            }
        }

        for (int i = 0; i < numOutput; ++i) // add biases to input-to-hidden sums
        {
            hoSums[i] += hoBiases[i];
        }

        for (int i = 0; i < numOutput; ++i) // determine hidden-to-output result
        {
            this.outputs[i] = SigmoidFunction(hoSums[i]);
        }

        double[] result = new double[numOutput]; // could define a GetOutputs method instead
        System.arraycopy(outputs, 0, result, 0, numOutput);
        return result;
    } // ComputeOutputs

    private static double StepFunction(double x) // an activation function that isn't compatible with back-propagation bcause it isn't differentiable
    {
        if (x > 0.0) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    private static double SigmoidFunction(double x) {
        if (x < -45.0) {
            return 0.0;
        } else if (x > 45.0) {
            return 1.0;
        } else {
            return 1.0 / (1.0 + Math.exp(-x));
        }
    }

    private static double HyperTanFunction(double x) {
        if (x < -10.0) {
            return -1.0;
        } else if (x > 10.0) {
            return 1.0;
        } else {
            return Math.tanh(x);
        }
    }
} // class NeuralNetwork

// ===========================================================================
class Helpers {

    public static double[][] MakeMatrix(int rows, int cols) {
        double[][] result = new double[rows][];
        for (int i = 0; i < rows; ++i) {
            result[i] = new double[cols];
        }
        return result;
    }

    public static void ShowVector(double[] vector, int decimals, boolean blankLine) {
        for (int i = 0; i < vector.length; ++i) {
            if (i > 0 && i % 12 == 0) // max of 12 values per row 
            {
                System.out.println("");
            }
            if (vector[i] >= 0.0 ) {
                System.out.print(" ");
            }
            System.out.print(vector[i]); // n decimals
        }
        if (blankLine) {
            System.out.println("\n");
        }
    }
    public static void ShowOutputs(double[] vector, int decimals, boolean blankLine) {
        for (int i = 0; i < vector.length; ++i) {
            if (i > 0 && i % 12 == 0) // max of 12 values per row 
            {
                System.out.println("");
            }
            if (vector[i] >= 0.5 ) {
                System.out.print("Spam : ");
                System.out.print(vector[i]);
            }
            else
            {
                System.out.print("Ham : ");
                System.out.print(vector[i]);
            }
             // n decimals
           
        }
        if (blankLine) {
            System.out.println("\n");
        }
    }
    public static void ShowMatrix(double[][] matrix, int numRows, int decimals) {
        int ct = 0;
        if (numRows == -1) {
            numRows = Integer.MAX_VALUE; // if numRows == -1, show all rows
        }
        for (int i = 0; i < matrix.length && ct < numRows; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] >= 0.0) {
                    System.out.print(" "); // blank space instead of '+' sign
                }
                System.out.print(matrix[i][j]);
            }
            System.out.println("");
            ++ct;
        }
        System.out.println("");
    }
} // class Helpers

