import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Math.*;

public class Main {
    public static void main(String[] arg) {
        System.out.println("------------------------------");
        System.out.println("ИСХОДНЫЙ ПРОЦЕСС");
        double[] p = new double[10000];
        int size_p = 0;
        try {
            // В файле с именем "process.txt" содержится выборка из 10000 значений
            // временного ряда исходного СП, по одному значению в каждой строке
            FileReader reader = new FileReader("process.txt");
            Scanner scan = new Scanner(reader);
            while (scan.hasNextLine()) {
                for (int i = 0; i < 10000; i++) {
                    p[i] = Double.parseDouble(scan.nextLine());
                    size_p++;
                }
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        System.out.println("Размер выборки: " + size_p);
        double aver_p = Calculations.average(p);
        System.out.println("Среднее: " + aver_p);
        double D_p = Calculations.dispersion(p, aver_p);
        System.out.println("Дисперсия: " + D_p);
        System.out.println("СКО: " + sqrt(D_p) + '\n');
        double[] cor_p = Calculations.CorrelationFunction(p);
        double[] ncf_p = Calculations.NCF(p);
        int T = -1;
        System.out.println("m" + '\t' + '\t' + "R(m)" + '\t' + '\t' + '\t' + "r(m)");
        for (int i = 0; i < 11; i++) {
            System.out.println(Double.toString(i) + '\t'+ '\t' +
                    cor_p[i] + '\t'+ '\t' + '\t' + ncf_p[i]);
            if (abs(ncf_p[i]) > exp(-1))
                T = i;
        }
        System.out.println("Интервал корреляции: " + T);
        Calculations.AR(ncf_p);
        Calculations.MA(ncf_p);
        Calculations.ARMA(ncf_p);
        Calculations.AR3_modelling(aver_p);
        Calculations.MA1_modelling(aver_p);
        Calculations.ARMA23_modelling(aver_p);
    }
}
