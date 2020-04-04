package pl.wtorkowy;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static double[][] createMatrix(String path, int length, boolean isTest) {
        double[][] result = new double[length][9];
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
                result[it][4] = Double.parseDouble(separatedLine[4]);

                it++;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public static void classification(double[][] training, double[] test, int k, int... attributes) {
        Distance[] distance = new Distance[training.length];

        int setosa = 0;
        int versicolor = 0;
        int virginica = 0;

        double setosaDist = 0;
        double versicolorDist = 0;
        double virginicaDist = 0;

        for (int i = 0; i < training.length; i++) {
            distance[i] = new Distance(i, Math.sqrt(Math.pow((training[i][attributes[0] - 1] - test[attributes[0] - 1]), 2)));

            for (int j = 1; j < attributes.length; j++) {
                distance[i].setDistance(Math.sqrt(Math.pow(distance[i].getDistance(), 2) + Math.pow((training[i][attributes[j] - 1] - test[attributes[j] - 1]), 2)));
            }
        }

        Arrays.sort(distance);

        for (int i = 0; i < k; i++) {
            if (0.0 == training[distance[i].getId()][4]) {
                setosa++;
                setosaDist += distance[i].getDistance();
            } else if (1.0 == training[distance[i].getId()][4]) {
                versicolor++;
                versicolorDist += distance[i].getDistance();
            } else if (2.0 == training[distance[i].getId()][4]) {
                virginica++;
                virginicaDist += distance[i].getDistance();
            }
        }

        test[5] = setosa;
        test[6] = versicolor;
        test[7] = virginica;

        if (setosa > versicolor && setosa > virginica) {
            test[8] = 0.0;
        } else if (versicolor > setosa && versicolor > virginica) {
            test[8] = 1.0;
        } else if (virginica > setosa && virginica > versicolor) {
            test[8] = 2.0;
        } else if (setosa == versicolor) {
            if (setosaDist < versicolorDist) {
                test[8] = 0.0;
            } else {
                test[8] = 1.0;
            }
        } else if (setosa == virginica) {
            if (setosaDist < virginicaDist) {
                test[8] = 0.0;
            } else {
                test[8] = 2.0;
            }
        } else if (versicolor == virginicaDist) {
            if (versicolorDist < virginicaDist) {
                test[8] = 1.0;
            } else {
                test[8] = 2.0;
            }
        } else {
            test[8] = 0.0;
        }
    }

    public static void writeClass(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print((i + 1) + ". ");
            if (0.0 == matrix[i][8]) {
                System.out.println("Setosa");
            } else if (1.0 == matrix[i][8]) {
                System.out.println("Versiocolor");
            } else if (2.0 == matrix[i][8]) {
                System.out.println("Virginica");
            }
        }
    }

    public static void main(String[] args) {
        double[][] training;
        double[][] test;

        int k = 11;
        String path = "2_4_k_11.txt";

        training = createMatrix("data_train.csv", 105, false);
        test = createMatrix("data_test.csv", 45, true);

        for (double[] doubles : test) {
            classification(training, doubles, k, 2, 4);
        }

        writeClass(test);

        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(path))) {
            int tmp = 0;
            int bad = 0;

            dataOutputStream.writeChars("K: " + k + "\n");

            for (int i = 1; i <= test.length; i++) {
                dataOutputStream.writeChars(Arrays.toString(test[i - 1]) + "\n");

                if (test[i - 1][4] == test[i - 1][8]) {
                    tmp++;
                }

                if (i%15 == 0) {
                    dataOutputStream.writeChars("Setosa/Versicolor/Virginica: " + (tmp/15.0 * 100.0) + "% \n");
                    dataOutputStream.writeChars("Zle: " + (15 - tmp) + "\n");
                    bad += (15 - tmp);
                    tmp = 0;
                }
            }

            dataOutputStream.writeChars("Zle: " + (bad/45.0 * 100.0) + "%");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
