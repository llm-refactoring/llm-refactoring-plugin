package ca.ualberta.cs.serl.wikidev.clustering;

import static org.math.array.DoubleArray.sum;
import static org.math.array.LinearAlgebra.eigen;
import static org.math.array.LinearAlgebra.minus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.math.array.DoubleArray;
import org.math.array.LinearAlgebra;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MatrixOperator {
	private static double[][] adjacencyMatrix;

	public static double[] getFiedlerVector(double[][] A) {
        //D - degree matrix
        double[][] D = new double[A.length][A[0].length];
        //L - Laplacian matrix
        double[][] L = new double[A.length][A[0].length];

        //sumRow is row matrix, each column i in sumRow contains
        //the sum value of column i in the adjacency matrix
        double[] sumRow = sum(A);
        //fill the degree matrix with the degrees of each node.
        for(int i = 0; i < D[0].length;i++) {
            D[i][i] = sumRow[i];
        }
        //L = D - A
        L = minus(D,A);

        EigenvalueDecomposition LEigenDec = eigen(L);

        //diagonal eigenvalue matrix
        Matrix eigenValues = LEigenDec.getD();
        //eigenvector matrix
        Matrix eigenVectors = LEigenDec.getV();

        //keys are the Eigen values sorted in ascending order 
        //values are the corresponding positions in eigenValues matrix
        TreeMap<Double,Integer> sortedEigenValues = new TreeMap<Double,Integer>();

        for(int i = 0; i <eigenValues.getColumnDimension(); i++) {
            //\E1\EB\EB\DC\E6\EF\F5\EC\E5 \F0\F1\FC\F3\E7\EC\EF \F3\F4\E9\F2 \E1\F1\ED\E7\F4\E9\EA\DD\F2 \F4\E9\EC\DD\F2
            double value;
            if(eigenValues.get(i,i) < 0.0)
                value = -eigenValues.get(i,i);
            else
                value = eigenValues.get(i,i);

            sortedEigenValues.put(value,i);
        }

        Set<Double> keySet = sortedEigenValues.keySet();
        Iterator<Double> keyIt = keySet.iterator();
        int minIndex = 0;
        double threshold = 0.00001;
        while(keyIt.hasNext()) {
            Double key = keyIt.next();
            if(key> threshold) {
                minIndex = sortedEigenValues.get(key);
                break;
            }
        }
        double[][] eigenVectors2=new double[eigenVectors.getRowDimension()][eigenVectors.getColumnDimension()];
        for(int i=0;i<eigenVectors.getRowDimension();i++) {
        	for(int j=0;j<eigenVectors.getColumnDimension();j++) {
        		eigenVectors2[i][j] = eigenVectors.get(i,j);
        	}
        }
        return DoubleArray.getColumnCopy(eigenVectors2,minIndex);
    }
	
	public static double[] getEigenValues(double[][] A) {
		
		EigenvalueDecomposition LEigenDec = eigen(A);
		//diagonal eigenvalue matrix
        Matrix eigenValues = LEigenDec.getD();
        double[] eigenValues2 = new double[eigenValues.getColumnDimension()];
        for(int i = 0; i <eigenValues.getColumnDimension(); i++) {
        	eigenValues2[i] = eigenValues.get(i, i);
        }
        return eigenValues2;
	}
	
	public static double[][] getEigenVectors(double[][] A) {
		EigenvalueDecomposition LEigenDec = eigen(A);
		//eigenvector matrix
        Matrix eigenVectors = LEigenDec.getV();
        double[][] eigenVectors2 = new double[eigenVectors.getRowDimension()][eigenVectors.getColumnDimension()];
        for(int i=0;i<eigenVectors.getRowDimension();i++) {
        	for(int j=0;j<eigenVectors.getColumnDimension();j++) {
        		eigenVectors2[i][j] = eigenVectors.get(i,j);
        	}
        }
        return eigenVectors2;
	}
	
	private static double[][] multiplyMatrixes(double[][] A, double[][] B) {
		double[][] product = new double[A.length][A.length];
		for(int i=0;i<A.length;i++) {
			for(int j=0;j<A.length;j++) {
				for(int k=0;k<A.length;k++)
				product[i][j] = product[i][j] + A[i][k]*B[k][j];
			}
		}
		return product;
	}
	
	public static double[] getAuthorityVector(double[][] adjacency) {
		double[][] transpose = transpose(adjacency);
		double[][] product = multiplyMatrixes(transpose, adjacency);
		return getPrincipalEigenVector(product);
	}
	
	/*private static double[][] transpose(double[][] adjacency) {
		double[][] transpose = new double[adjacency.length][adjacency[0].length];
		for(int i=0;i<adjacency.length;i++) {
			for(int j=0;j<adjacency[0].length;j++) {
				transpose[i][j] = adjacency[j][i];
			}
		}
		return transpose;
	}*/
	
	public static double[] getHubVector(double[][] adjacency) {
		double[][] transpose = transpose(adjacency);
		double[][] product = multiplyMatrixes(adjacency, transpose);
		return getPrincipalEigenVector(product);
	}
	
	public static double[] getMeanVector(double[][] adjacency) {
		double[] authority = getAuthorityVector(adjacency);
		double[] hub = getHubVector(adjacency);
		double[] mean = new double[hub.length];
		for(int i=0;i<hub.length;i++) {
			mean[i] = (authority[i]+hub[i])/2;
		}
		return mean;
	}
	
	private static double[] getPrincipalEigenVector(double[][] A) {
        //D - degree matrix
        double[][] D = new double[A.length][A[0].length];
        //L - Laplacian matrix
        double[][] L = new double[A.length][A[0].length];

        //sumRow is row matrix, each column i in sumRow contains
        //the sum value of column i in the adjacency matrix
        double[] sumRow = DoubleArray.sum(A);
        //fill the degree matrix with the degrees of each node.
        for(int i = 0; i < D[0].length;i++) {
            D[i][i] = sumRow[i];
        }
        //L = D - A
        L = minus(D,A);

        EigenvalueDecomposition LEigenDec = eigen(L);

        //diagonal eigenvalue matrix
        Matrix eigenValues = LEigenDec.getD();
        //eigenvector matrix
        Matrix eigenVectors = LEigenDec.getV();

        //keys are the Eigen values sorted in ascending order 
        //values are the corresponding positions in eigenValues matrix
        TreeMap<Double,Integer> sortedEigenValues = new TreeMap<Double,Integer>();

        for(int i = 0; i <eigenValues.getColumnDimension(); i++) {
            //\E1\EB\EB\DC\E6\EF\F5\EC\E5 \F0\F1\FC\F3\E7\EC\EF \F3\F4\E9\F2 \E1\F1\ED\E7\F4\E9\EA\DD\F2 \F4\E9\EC\DD\F2
            double value;
            if(eigenValues.get(i,i) < 0.0)
                value = -eigenValues.get(i,i);
            else
                value = eigenValues.get(i,i);

            sortedEigenValues.put(value,i);
        }

        /*Set<Double> keySet = sortedEigenValues.keySet();
        Iterator<Double> keyIt = keySet.iterator();
        int maxIndex = 0;*/
        Double lastKey = sortedEigenValues.lastKey();
        int maxIndex = sortedEigenValues.get(lastKey);
        /*while(keyIt.hasNext()) {
            Double key = keyIt.next();
            maxIndex = sortedEigenValues.get(key);
            if(maxIndex > sortedEigenValues.get(key)) {
            	maxIndex = sortedEigenValues.get(key);
            }
        }*/
        double[][] eigenVectors2=new double[eigenVectors.getRowDimension()][eigenVectors.getColumnDimension()];
        for(int i=0;i<eigenVectors.getRowDimension();i++) {
        	for(int j=0;j<eigenVectors.getColumnDimension();j++) {
        		eigenVectors2[i][j] = eigenVectors.get(i,j);
        	}
        }
        return DoubleArray.getColumnCopy(eigenVectors2,maxIndex);
	}
	
	public static double getMean(double[] vector) {
		double sum=0;
		double mean=0;
		for(int i=0;i<vector.length;i++) {
			sum += vector[i];
		}
		if(vector.length>0) {
			mean = sum/vector.length;
		}
		return mean;
	}
	
	public static double getStandardDeviation(double[] vector) {
		double mean = getMean(vector);
		double sum = 0;
		double sd = 0;
		for(int i=0;i<vector.length;i++) {
			sum += Math.pow((vector[i] - mean),2);
		}
		if(vector.length>0) {
			sd = Math.sqrt(sum/vector.length);
		}
		return sd;
	}
	
	public static void saveResults(String project, double[] authority, double[] hub, double[] meanVector, double mean, double sd) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("C://results"+"("+project+")"+".txt"));
			out.write(project);
			out.newLine();
			out.write('[');
			for(int i=0; i<authority.length; i++) {
					out.write(authority[i]+", ");
			}	
			out.newLine();
			for(int i=0; i<hub.length; i++) {
				out.write(hub[i]+", ");
			}	
			out.newLine();
			for(int i=0; i<meanVector.length; i++) {
				out.write(meanVector[i]+", ");
			}	
			out.newLine();
			out.write(""+mean);
			out.newLine();
			out.write(""+sd);
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void setAdjacencyMatrix(double[][] adjacencyMatrix) {
		MatrixOperator.adjacencyMatrix = adjacencyMatrix;
	}

	public static TreeMap<Integer, ArrayList<Integer>> getConnectedComponents() {
	       int[] component = new int[adjacencyMatrix.length];
	       int cn = 0;

	       for(int i=0; i<component.length; i++) {
	           if(component[i] == 0) {
	               cn++;
	               dfs(component,i,cn);
	           }
	       }

	       TreeMap<Integer, ArrayList<Integer>> map = new TreeMap<Integer, ArrayList<Integer>>();
	       for(int i=0; i<component.length; i++) {
	           if(!map.containsKey(component[i])) {
	               ArrayList<Integer> l = new ArrayList<Integer>();
	               l.add(i);
	               map.put(component[i],l);
	           }
	           else {
	               ArrayList<Integer> l = map.get(component[i]);
	               l.add(i);
	           }
	       }
	       return map;
	   }

	   private static void dfs(int[] component, int v, int cn) {
	       component[v] = cn;
	       for(int w=0; w<adjacencyMatrix[v].length; w++) {
	           if(adjacencyMatrix[v][w] != 0)
	               if(component[w] == 0)
	                   dfs(component,w,cn);
	       }
	   }
	   
	   private static double getVectorNorm(double[] vector) {
		   double sum=0.0;
		   for(int i=0;i<vector.length;i++) {
			   sum += Math.pow(vector[i],2);
		   }
		   return Math.sqrt(sum);
	   }
	   
	   public static double[] normalizeVector(double[] vector) {
		   double norm = getVectorNorm(vector);
		   for(int i=0;i<vector.length;i++) {
			   vector[i] /= norm;
		   }
		   return vector;
	   }
	   
	   public static double[][] identity(int m) {
		   return DoubleArray.identity(m);
	   }
	   
	   public static double[][] one(int m, int n) {
		   return DoubleArray.fill(m, n, 1.0);
	   }
	   
	   public static double[][] scaleMatrix(double[][] matrix, double v) {
		   return LinearAlgebra.times(matrix, v);
	   }
	   
	   public static double[][] transpose(double[][] matrix) {
		   return DoubleArray.transpose(matrix);
	   }
	   
	   public static double[][] matrixProduct(double[][] A, double[][] B) {
		   return LinearAlgebra.times(A, B);
	   }
	   
	   public static double[] vectorPower(double[] v, double n) {
		   return LinearAlgebra.raise(v, n);
	   }
	   
	   public static double[] matrixVectorProduct(double[][] matrix, double[] vector) {
		   return LinearAlgebra.times(matrix, vector);
	   }
	   
	   public static double[][] diagonal(double[] c) {
		   double[][] I = new double[c.length][c.length];
		   for (int i = 0; i < I.length; i++)
			   I[i][i] = c[i];
		   	return I;

	   }
	   
	   public static double getRowSum(double[][] m, int i) {
		   return DoubleArray.sum(DoubleArray.getRowCopy(m, i));
	   }
	   
	   public static double getColumnSum(double[][] m, int j) {
		   return DoubleArray.sum(DoubleArray.getColumnCopy(m, j));
	   }
	   
	   public static double getTotalSum(double[][] m) {
		   double sum=0;
		   for(int i=0; i<m.length; i++) {
			   for(int j=0; j<m.length; j++) {
				   sum += m[i][j];
			   }
		   }
		   return sum;
	   }
	   
	   public static double[] scaleVector(double[] vector, double v) {
		   return LinearAlgebra.times(vector, v);
	   }
	   
	   public static double[] oneVector(int m) {
		   return DoubleArray.fill(m, 1.0);
	   }
	   
	   public static double vectorProduct(double[] v1, double[] v2) {
		   double product=0;
		   for(int i=0; i<v1.length; i++) {
			   product += v1[i]*v2[i];
		   }
		   return product;
	   }
	   
	   public static double[] getColumn(double[][] matrix, int i) {
		   return DoubleArray.getColumnCopy(matrix, i);
	   }
}
