import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.*;

public class Calculations {
    // Метод для подсчета выборочного среднего для данной выборки
    public static double average(double[] arr) {
        double result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += arr[i];
        }
        result /= arr.length;
        return result;
    }
    // Метод для подсчета дисперсии для данной выборки
    public static double dispersion (double[] arr, double average) {
        double result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += (arr[i] - average) * (arr[i] - average);
        }
        result /= arr.length;
        return result;
    }
    // Метод для подсчета корреляционной функции заданного порядка
    public static double CorrelationFunction (double[] arr, int m) {
        if (m < 0)
            m *= (-1);
        double[] arr1 = new double[arr.length - m];
        double[] arr2 = new double[arr.length - m];
        double result = 0;
        for (int i = 0; i < arr.length - m; i++) {
            arr1[i] = arr[i];
            arr2[i] = arr[i+m];
        }
        double arr1_sr = average(arr1);
        double arr2_sr = average(arr2);
        for (int i = 0; i < arr.length - m; i++) {
            result += (arr1[i] - arr1_sr) * (arr2[i] - arr2_sr);
        }
        result /= arr.length - m;
        return result;
    }
    // Метод для подсчета всех корреляционных функций для m = 0,...,10
    public static double[] CorrelationFunction(double[] arr) {
        double[] res = new double[11];
        for (int i = 0; i < 11; i++)
            res[i] = CorrelationFunction(arr, i);
        return res;
    }
    // Метод для подсчета нормированной корреляционной функции
    public static double NCF(double[] arr, int m) {
        if (m < 0)
            m *= (-1);
        double[] arr1 = new double[arr.length - m];
        double[] arr2 = new double[arr.length - m];

        for (int i = 0; i < arr.length - m; i++) {
            arr1[i] = arr[i];
            arr2[i] = arr[i+m];
        }
        double d1 = dispersion(arr1, average(arr1));
        double d2 = dispersion(arr2, average(arr2));
        double c = CorrelationFunction(arr, m);
        return c / sqrt(d1 * d2);
    }
    // Метод для подсчета всех нормированных
    // корреляционных функций для m = 0,..,10
    public static double[] NCF(double[] arr) {
        double[] res = new double[11];
        for (int i = 0; i < 11; i++)
            res[i] = NCF(arr, i);
        return res;
    }
    // Метод для подсчета теоретических НКФ (формула 4.2)
    public static double[] theory_NCF(double[] r0, double[] beta, int M, int N){
        // r0 - массив НКФ исходного процесса
        // beta - массив со значениями парметров бетта
        // M, N - порядок модели
        // rt - массив теоретических НКФ
        double[] rt = new double[11];
        for (int i = 0; i < 11; i++) {
            if (i <= M + N)
                rt[i] = r0[i];
            else {
                for (int j = 1; j <= M; j++) {
                    rt[i] += beta[j] * rt[i - j];
                }
            }
        }
        return rt;
    }
    // Метод для подсчета погрешности модели (формула 4.1)
    public static double eps_2(double[] r0, double[] rt) {
        // r0 - массив НКФ исходного процесса
        // rt - массив теоретических НКФ
        double eps = 0;
        for (int i = 1; i <= 10; i++) {
            eps += pow(rt[i] - r0[i], 2);
        }
        return eps;
    }

    public static void AR(double[] r0) {
        System.out.println("------------------------------");
        System.out.println("МОДЕЛИ АВТОРЕГРЕССИИ");
        double[][] parameters_beta = {{0, 0, 0, 0},
                {0, 0.40576, 0, 0},
                {0, 0.63839, -0.57333, 0},
                {0, 0.49660, -0.41545, -0.24730}};
        double[] rt;
        for(int i = 0; i<=3; i++) {
            rt = theory_NCF(r0, parameters_beta[i], i, 0);
            System.out.println("M = " + i + "; eps^2 = " +
                    eps_2(r0, rt));
            if (i == 3) { // АР(3)
                System.out.println("- - - - - - - - - -");
                for(int j = 0; j < rt.length; j++) {
                    System.out.println("rt[" + j + "] = " + rt[j]);
                }
                System.out.println("- - - - - - - - - -");
            }
        }
    }

    public static void MA(double[] r0) {
        System.out.println("------------------------------");
        System.out.println("МОДЕЛИ СКОЛЬЗЯЩЕГО-СРЕДНЕГО");
        double[][] parameters_beta = {{0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}};
        System.out.println("N = " + 0 + "; eps^2 = " +
                eps_2(r0, theory_NCF(r0, parameters_beta[0], 0 ,0)));
        // СС(1)
        double[] v = theory_NCF(r0, parameters_beta[1], 0, 1);
        System.out.println("N = " + 1 + "; eps^2 = " + eps_2(r0, v));
        System.out.println("- - - - - - - - - -");
        for (int i = 0; i <= 10; i++) {
            System.out.println("rt(" + i + ") = " + v[i]);
        }
        System.out.println("- - - - - - - - - -");
    }

    public static void ARMA(double[] r0) {
        System.out.println("------------------------------");
        System.out.println("МОДЕЛИ АРСС");
        double[][] parameters_beta = {{0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},

                {0, 0.92795, -0.69082, 0},
                {0, 0.93619, -0.68444, 0},
                {0, 0.93734, -0.68653, 0},

                {0, 0.97639, -0.72174, 0.02777},
                {0, 0.81309, -0.57022, -0.08504},
                {0, 0, 0, 0}};
        int count = 1;
        for(int m = 1; m<=3; m++) {
            for (int n = 1; n <=3; n++) {
                System.out.println("M = " + m + ", N = " + n + "; eps^2 = " +
                        eps_2(r0, theory_NCF(r0, parameters_beta[count], m, n)));
                if ((m == 2 && n == 1) || (m == 2 && n == 3)) { // АРСС(2, 1) и АРСС(2, 3)
                    System.out.println("- - - - - - - - - -");
                    double[] v = theory_NCF(r0, parameters_beta[count], m, n);
                    for (int i = 0; i <= 10; i++) {
                        System.out.println("rt(" + i + ") = " + v[i]);
                    }
                    System.out.println("- - - - - - - - - -");
                }
                count++;
            }
        }

    }

    public static void AR3_modelling(double M) {
        System.out.println("------------------------------");
        System.out.println("МОДЕЛИРОВАНИЕ АР(3)");
        double[] arr0 = new double[11000];
        double[] arr = new double[10000];
        double[] beta = {0, 0.49660, -0.41545, -0.24730};
        double[] a = {15.90081, 0, 0, 0};
        double E;
        Random r = new Random();
        for (int i = 3; i < arr0.length; i++) {
            E = r.nextGaussian();
            arr0[i] = beta[1] * arr0[i - 1] + beta[2] * arr0[i - 2] + beta[3] * arr0[i - 3] +
                    a[0] * E + (1 - (beta[1] + beta[2] + beta[3])) * M;
        }
        try (FileWriter writer = new FileWriter("AR3_modelling_array.txt", false)) {
            for (int i = 1000; i < arr0.length; i++) {
                arr[i - 1000] = arr0[i];
                writer.write(Double.toString(arr0[i]));
                writer.append('\n');
            }
        } catch (IOException exc) {
            System.err.println(exc.getMessage());
            System.exit(-1);
        }
        try (FileWriter writer = new FileWriter("AR3_modelling_info.txt", false)) {
            double avrg = average(arr);
            writer.write("Среднее: " + avrg);
            writer.append('\n');
            System.out.println("Среднее:" + avrg);

            double D = dispersion(arr, avrg);
            writer.write("Дисперсия: " + D);
            writer.append('\n');
            System.out.println("Дисперсия:" + D);

            double sqrt_D = sqrt(D);
            writer.write("СКО: " + sqrt_D);
            writer.append('\n');
            System.out.println("СКО:" + sqrt_D);

            double[] ncf = NCF(arr);
            writer.write("m" + '\t' + '\t' + "r(m)");
            writer.append('\n');
            System.out.println("m" + '\t' + '\t' + "r(m)");
            for (int i = 0; i < ncf.length; i++) {
                writer.write(Integer.toString(i) + '\t' + ncf[i]);
                writer.write('\n');
                System.out.println(Integer.toString(i) + '\t' + ncf[i]);
            }

            double eps = eps_2(ncf, theory_NCF(ncf, beta, 3, 0));
            writer.write("eps^2: " + eps);
            writer.append('\n');
            System.out.println("eps^2: " + eps);
        }
        catch (IOException exc) {
            System.err.println(exc.getMessage());
            System.exit(-1);
        }
    }

    public static void MA1_modelling(double M){
        System.out.println("------------------------------");
        System.out.println("МОДЕЛИРОВАНИЕ СС(1)");
        double[] arr0 = new double[11000];
        double[] arr = new double[10000];
        double[] beta = {0, 0, 0, 0};
        double[] a = {9.99050, 19.50460, 0, 0};
        double[] E = new double[11000];
        Random r = new Random();
        for (int i = 1; i < arr0.length; i++) {
            E[i] = r.nextGaussian();
            arr0[i] = a[0] * E[i] + a[1] * E[i - 1] + M;
        }
        try(FileWriter writer = new FileWriter("MA1_modelling_array.txt", false)) {
            for (int i = 1000; i < arr0.length; i++) {
                arr[i-1000] = arr0[i];
                writer.write(Double.toString(arr0[i]));
                writer.append('\n');
            }
        }
        catch(IOException exc) {
            System.err.println(exc.getMessage());
            System.exit(-1);
        }
        try (FileWriter writer = new FileWriter("MA1_modelling_info.txt", false)) {
            double avrg = average(arr);
            writer.write("Среднее: " + avrg);
            writer.append('\n');
            System.out.println("Среднее:" + avrg);

            double D = dispersion(arr, avrg);
            writer.write("Дисперсия: " + D);
            writer.append('\n');
            System.out.println("Дисперсия:" + D);

            double sqrt_D = sqrt(D);
            writer.write("СКО: " + sqrt_D);
            writer.append('\n');
            System.out.println("СКО:" + sqrt_D);

            double[] ncf = NCF(arr);
            writer.write("m" + '\t' + '\t' + "r(m)");
            writer.append('\n');
            System.out.println("m" + '\t' + '\t' + "r(m)");
            for (int i = 0; i < ncf.length; i++) {
                writer.write(Integer.toString(i) + '\t' + ncf[i]);
                writer.write('\n');
                System.out.println(Integer.toString(i) + '\t' + ncf[i]);
            }
            double eps = eps_2(ncf, theory_NCF(ncf, beta, 0, 1));
            writer.write("eps^2: " + eps);
            writer.append('\n');
            System.out.println("eps^2: " + eps);
        }
        catch (IOException exc) {
            System.err.println(exc.getMessage());
            System.exit(-1);
        }
    }

    public static void ARMA23_modelling(double M){
        System.out.println("------------------------------");
        System.out.println("МОДЕЛИРОВАНИЕ АРСС(2, 3)");
        double[] arr0 = new double[11000];
        double[] arr = new double[10000];
        double[] beta = {0, 0.93734, -0.68653, 0};
        double[] a = {15.73052, -7.59405, -0.26407, 0.03688};
        double[] E = new double[11000];
        Random r = new Random();
        for (int i = 3; i < arr0.length; i++) {
            E[i] = r.nextGaussian();
            arr0[i] = beta[1] * arr0[i - 1] + beta[2] * arr0[i - 2]
                    + a[0] * E[i] + a[1] * E[i - 1] + a[2] * E[i - 2] + a[3] * E[i - 3] +
                    M * (1 - (beta[1] + beta[2]));
        }
        try(FileWriter writer = new FileWriter("ARMA23_modelling_array.txt", false)) {
            for (int i = 1000; i < arr0.length; i++) {
                arr[i-1000] = arr0[i];
                writer.write(Double.toString(arr0[i]));
                writer.append('\n');
            }
        }
        catch(IOException exc) {
            System.err.println(exc.getMessage());
            System.exit(-1);
        }
        try (FileWriter writer = new FileWriter("ARMA23_modelling_info.txt", false)) {
            double avrg = average(arr);
            writer.write("Среднее: " + avrg);
            writer.append('\n');
            System.out.println("Среднее:" + avrg);

            double D = dispersion(arr, avrg);
            writer.write("Дисперсия: " + D);
            writer.append('\n');
            System.out.println("Дисперсия:" + D);

            double sqrt_D = sqrt(D);
            writer.write("СКО: " + sqrt_D);
            writer.append('\n');
            System.out.println("СКО:" + sqrt_D);

            double[] ncf = NCF(arr);
            writer.write("m" + '\t' + '\t' + "r(m)");
            writer.append('\n');
            System.out.println("m" + '\t' + '\t' + "r(m)");
            for (int i = 0; i < ncf.length; i++) {
                writer.write(Integer.toString(i) + '\t' + ncf[i]);
                writer.write('\n');
                System.out.println(Integer.toString(i) + '\t' + ncf[i]);
            }
            double eps = eps_2(ncf, theory_NCF(ncf, beta, 2, 3));
            writer.write("eps^2: " + eps);
            writer.append('\n');
            System.out.println("eps^2: " + eps);
        }
        catch (IOException exc) {
            System.err.println(exc.getMessage());
            System.exit(-1);
        }
    }

} // Конец класса Calculations
