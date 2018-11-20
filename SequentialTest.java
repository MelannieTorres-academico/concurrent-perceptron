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
class SequentialTest{
  private double[] weights;
  private int[] results;
  private double[] x_test_set;
  private int dim;
  private int n_size;


  public SequentialTest (double weights[], double[] x_test_set, int dim, int n_size, int[] results) {
     this.weights = weights;
     this.x_test_set = x_test_set;
     this.dim = dim;
     this.n_size = n_size;
     this.results = results;
  }


  public void test() {
    int i, j;
    double acum = 0;

    for (i = 0; i < this.n_size; i++) {
      acum = 0;
      for (j = 0; j < dim+1; j++) {
        acum += x_test_set[i*(dim+1) + j]*weights[j];
      }
      results[i] = (acum >= 0)? 1 : 0;
    }
   }

   public int[] getResults(){
     return results;
   }

}
