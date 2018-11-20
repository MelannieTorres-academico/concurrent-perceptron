import java.util.Scanner;
import java.lang.String;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

/* To run:
*  javac *.java
*  java MainForkJoinPerceptron < ./tests/ruben_or.txt -true
*  java MainForkJoinPerceptron < ./tests/linearly_separable.txt
*  java MainForkJoinPerceptron < ./tests/breast_cancer.txt
*  java MainForkJoinPerceptron < ./tests/ruben_no_solution.txt
*  java MainForkJoinPerceptron < ./tests/ruben_no_solution_2.txt
*/

class MainForkJoinPerceptron {
  private static final int MAXTHREADS = Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) {
    ForkJoinPool pool;
    Perceptron perceptron = new Perceptron();
    double weights[];
    double learning_rate = 0.01;
    double acum1 = 0;
    double acum2 = 0;
    double[] x_test_set;
    int result_fork_join[];
    int result_sequential[];
    int result_threads[];
    int limit = 100000;
    int iterations = 10;
    int n_size, dim, i;
    boolean hasSolution;
    long startTime, stopTime;

    perceptron.parse();

    if (perceptron.train(learning_rate, limit)){

      weights = perceptron.getWeights();
      x_test_set = perceptron.getXTestSet();
      n_size = perceptron.getNSize();
      dim = perceptron.getDim();
      result_fork_join = new int[n_size];
      result_sequential = new int[n_size];
      result_threads = new int[n_size];

      SequentialTest sequential = new SequentialTest(weights, x_test_set, dim, n_size, result_sequential);
      System.out.println("\nSequential: ");

      for (i = 0; i < iterations; i++) {
        startTime = System.currentTimeMillis();
        sequential.test();
        stopTime = System.currentTimeMillis();
        acum1 +=  (stopTime - startTime);
      }
      System.out.println("  avg time: "+(acum1/iterations));

      if(args.length>0 && args[0].equals("-true")){
        result_sequential = sequential.getResults();
        System.out.print("  ");
        for(i = 0; i < n_size; i++){
          System.out.print(result_sequential[i]+" ");
        }
        System.out.println();
      }
      System.out.println();

      pool = new ForkJoinPool(MAXTHREADS);
      System.out.println("Fork Join: ");
      ForkJoinTest forkjoin = new ForkJoinTest(weights, x_test_set, dim, 0, n_size, result_fork_join);

      for (i = 0; i < iterations; i++){
        startTime = System.currentTimeMillis();
        pool.invoke(forkjoin);
        stopTime = System.currentTimeMillis();
  			acum2 +=  (stopTime - startTime);
      }

      System.out.println("  avg time: "+(acum2/iterations));
      if (args.length>0 && args[0].equals("-true")) {
        result_sequential = sequential.getResults();
        System.out.print("  ");
        for(i = 0; i < n_size ;i++){
          System.out.print(result_sequential[i]+" ");
        }
      }

      System.out.println("\nSpeed up: "+acum1/acum2+"\n");

    } else {
      System.out.println("There's no solution");
    }
  }
}
