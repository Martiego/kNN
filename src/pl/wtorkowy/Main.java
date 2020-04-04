package pl.wtorkowy;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static double[][] createMatrix(String path, int length, boolean isTest) {
        double[][] result = new double[length][5];
        String line;
        String[] separatedLine;
        int it;


        try (Scanner in = new Scanner(new File(path))) {
            it = 0;

            while (in.hasNext()) {
                line = in.nextLine();
                separatedLine = line.split(",");
                result[it][0] = Double.parseDouble(separatedLine[0]);
                result[it][1] = Double.parseDouble(separatedLine[1]);
                result[it][2] = Double.parseDouble(separatedLine[2]);
                result[it][3] = Double.parseDouble(separatedLine[3]);

                if (isTest) {
                    result[it][4] = -1;
                } else {
                    result[it][4] = Double.parseDouble(separatedLine[4]);
                }

                it++;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public static int minValueIndex(double[] tab) {
        int result = 0;
        double min = tab[0];

        for (int i = 1; i < tab.length; i++) {
            if (tab[i] < min) {
                min = tab[i];
                result = i;
            }
        }

        return result;
    }

    public static void classification(double[][] training, double[] test, int k, int... attributes) {
        double[] distance = new double[training.length];

        for (int i = 0; i < training.length; i++) {
            distance[i] = Math.sqrt(Math.pow((training[i][attributes[0] - 1] - test[attributes[0] - 1]), 2));

            for (int j = 1; j < attributes.length; j++) {
                distance[i] = Math.sqrt(Math.pow(distance[i], 2) + Math.pow((training[i][attributes[j] - 1] - test[attributes[j] - 1]), 2));
            }
        }

        test[4] = training[minValueIndex(distance)][4];
    }

    public static void writeClass(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print((i + 1) + ". ");
            if (0.0 == matrix[i][4]) {
                System.out.println("Setosa");
            } else if (1.0 == matrix[i][4]) {
                System.out.println("Versiocolor");
            } else if (2.0 == matrix[i][4]) {
                System.out.println("Virginica");
            }
        }
    }

    public static void main(String[] args) {
        double[][] training;
        double[][] test;
        double[] distance = new double[105];

        training = createMatrix("data_train.csv", 105, false);
        test = createMatrix("data_test.csv", 45, true);

        for (double[] doubles : test) {
            classification(training, doubles, 1, 1, 2, 3, 4);
        }

        writeClass(test);
    }
}
