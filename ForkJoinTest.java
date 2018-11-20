/*    Fork-Join-Perceptron uses the java framework "fork-join" to test a perceptron
    Copyright (C) 2018  Melannie Torres

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    If you want to contact the author you may write an email to: melannie.torres.soto@gmail.com
    As a subject please write: Fork-Join-Perceptron: additional info
*/
import java.util.concurrent.RecursiveAction;

class ForkJoinTest extends RecursiveAction{
  private double[] weights;
  private double[] x_test_set;
  private int[] results;
  private int start;
  private int end;
  private int dim;
  int middle;

  public ForkJoinTest(double weights[], double[] x_test_set, int dim, int start, int end, int[] results) {
     this.weights = weights;
     this.x_test_set = x_test_set;
     this.dim = dim;
     this.start = start;
     this.end = end;
     this.results = results;
  }


  public void computeDirectly() {
    int i, j;
    double acum = 0;

    for (i = this.start; i < this.end; i++) {
      acum = 0;
      for (j = 0; j < dim+1; j++) {
        acum += x_test_set[i*(dim+1) + j]*weights[j]; //i*dim + j
      }
      results[i] = (acum >= 0)? 1 : 0;
    }
   }

   @Override
   protected void compute() {
     if ( (this.end - this.start) <= 6000 ) {
       computeDirectly();
     } else {
       middle = (end + start) / 2;

       invokeAll(new ForkJoinTest(weights, x_test_set, dim, start, middle, results),
            new ForkJoinTest(weights, x_test_set, dim, middle, end, results));
     }
   }

   public int[] getResults(){
     return results;
   }
}
