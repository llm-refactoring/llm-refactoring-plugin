package ca.ualberta.cs.serl.wikidev.clustering;

import java.util.ArrayList;

public class MultidimensionalScaling {
	
	private double[][] betaMatrix;
	private double[][] alphaMatrix;
	private double[][] hMatrix;
	private double[][] distanceMatrix;
	private double[][] squareDistanceMatrix;
	private int dimensions;
	
	/*public MultidimensionalScaling(ArrayList<Entity> entities) {
		alphaMatrix = new double[entities.size()][entities.size()];
		distanceMatrix = new double[entities.size()][entities.size()];
		squareDistanceMatrix = new double[entities.size()][entities.size()];
		for(int i=0;i<entities.size();i++) {
			for(int j=0;j<entities.size();j++) {
				
				if (i != j) {
					distanceMatrix[i][j] = DistanceCalculator.getDistance(
							entities.get(i).getEntitySet(), entities.get(j)
									.getFullEntitySet());
					squareDistanceMatrix[i][j] = Math.pow(DistanceCalculator
							.getDistance(entities.get(i).getEntitySet(),
									entities.get(j).getFullEntitySet()), 2);
				}
				else {
					distanceMatrix[i][j] = 0.0;
					squareDistanceMatrix[i][j] = 0.0;
				}
			}
		}
		getAlphaMatrix();
		betaMatrix = new double[entities.size()][entities.size()];
	}*/
	
	public MultidimensionalScaling(double[][] distanceMatrix) {
		hMatrix = new double[distanceMatrix.length][distanceMatrix.length];
		this.distanceMatrix = distanceMatrix;
		squareDistanceMatrix = new double[distanceMatrix.length][distanceMatrix.length];
		for(int i=0;i<distanceMatrix.length;i++) {
			for(int j=0;j<distanceMatrix.length;j++) {
					squareDistanceMatrix[i][j] = Math.pow(this.distanceMatrix[i][j],2);
			}
		}
		getHMatrix();
		betaMatrix = new double[distanceMatrix.length][distanceMatrix.length];
	}
	
	public double[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public double[][] getAlphaMatrix() {
		for(int i=0;i<distanceMatrix.length;i++) {
			for(int j=0;j<distanceMatrix.length;j++) {
				alphaMatrix[i][j] = -(1.0/2.0)*squareDistanceMatrix[i][j];
			}
		}
		return alphaMatrix;
	}
	
	private void getHMatrix() {
		double[][] identity = MatrixOperator.identity(distanceMatrix.length);
		double[] one = MatrixOperator.oneVector(distanceMatrix.length);
		//double[][] transposeOne = MatrixOperator.transpose(one);
		double[] scaled = MatrixOperator.scaleVector(one, (1.0/distanceMatrix.length));
		double oneProduct = MatrixOperator.vectorProduct(scaled, one);
		for(int i=0; i<distanceMatrix.length; i++) {
			for(int j=0; j<distanceMatrix.length; j++) {
				hMatrix[i][j] = identity[i][j] - oneProduct;
			}
		}
	}
	
	public double[][] getBetaMatrix() {
		for(int i=0;i<alphaMatrix.length;i++) {
			for(int j=0;j<alphaMatrix.length;j++) {
				betaMatrix[i][j] = alphaMatrix[i][j];
			}
		}
		for(int i=0;i<alphaMatrix.length;i++) {
			for(int j=0;j<alphaMatrix.length;j++) {
				for(int k=0;k<alphaMatrix.length;k++) {
					betaMatrix[i][j] -= alphaMatrix[i][k];
				}
				
			}
		}
		for(int j=0;j<alphaMatrix.length;j++) {
			for(int i=0;i<alphaMatrix.length;i++) {
				for(int k=0;k<alphaMatrix.length;k++) {
					betaMatrix[i][j] -= alphaMatrix[k][j];
				}
				
			}
		}
		for(int i=0;i<alphaMatrix.length;i++) {
			for(int j=0;j<alphaMatrix.length;j++) {
				for(int k=0;k<alphaMatrix.length;k++) {
					for(int m=0; m<alphaMatrix.length;m++) {
						betaMatrix[i][j] += alphaMatrix[k][m];
					}
				}
				
			}
		}
		return betaMatrix;
	}
	
	public boolean findNegativeEigenValue(double[] eigenValues) {
		for(int i=0; i<eigenValues.length;i++) {
			if(eigenValues[i]<0.0) {
				return true;
			}
		}
		return false;
	}
	
	private double getConstant(double[] eigenValues) {
		double eigenValue = eigenValues[0];
		return -2.0*eigenValue;
	}
	
	public void changeDistances(double[] eigenValues) {
		for(int i=0;i<alphaMatrix.length;i++) {
			for(int j=0;j<alphaMatrix.length;j++) {
				if(i != j) {
					squareDistanceMatrix[i][j] += getConstant(eigenValues);
				}
			}
		}
	}
	
	public float[][] mds() {
		dimensions = 2;
		float[][] coordinates = new float[distanceMatrix.length][dimensions];
		while(true) {
			getAlphaMatrix();
			double[][] B = getBetaMatrix();
			//double[] eigenValues = MatrixOperator.getEigenValues(B);
			//if(findNegativeEigenValue(eigenValues)) {
			//	changeDistances(eigenValues);
			//}
			//else {
				double[][] eigenVectors = MatrixOperator.getEigenVectors(B);
				for(int i=0;i<distanceMatrix.length;i++) {
					for(int j=0;j<dimensions;j++) {
						if(eigenVectors[j][i] >= 0.0) {
							coordinates[i][j] = (float)eigenVectors[j][i];
						}
						else {
							coordinates[i][j] = (float)-eigenVectors[j][i];
						}
					}
				}
				return coordinates;
			//}
			
		}
	}
	
	private double[][] getNewBetaMatrix() {
		/*double[] rowSums = new double[distanceMatrix.length];
		for(int i=0; i<distanceMatrix.length; i++) {
			rowSums[i] = MatrixOperator.getRowSum(distanceMatrix, i);
		}
		double[] rowAvgs = MatrixOperator.scaleVector(rowSums, 1.0/distanceMatrix.length);
		double[] colSums = new double[distanceMatrix.length];
		for(int i=0; i<distanceMatrix.length; i++) {
			colSums[i] = MatrixOperator.getColumnSum(distanceMatrix, i);
		}
		double[] colAvgs = MatrixOperator.scaleVector(colSums, 1.0/distanceMatrix.length);
		double totalAvg = (1.0/Math.pow(distanceMatrix.length, 2))*MatrixOperator.getTotalSum(distanceMatrix);
		for(int i=0; i<distanceMatrix.length; i++) {
			for(int j=0; j<distanceMatrix.length; j++) {
				betaMatrix[i][j] = -(1.0/2.0)*(distanceMatrix[i][j] - rowAvgs[i] - colAvgs[j] + totalAvg);
			}
		}*/
		double[][] scaled = MatrixOperator.scaleMatrix(hMatrix, -1.0/2.0);
		betaMatrix = MatrixOperator.matrixProduct(scaled, squareDistanceMatrix);
		betaMatrix = MatrixOperator.matrixProduct(betaMatrix, hMatrix);
		return betaMatrix;
	}
	
	public double[][] cMDS() {
		//dimensions = 2;
		
		getHMatrix();
		double[][] B = getNewBetaMatrix();
		double[] eigenValues = MatrixOperator.getEigenValues(B);
		double[][] eigenVectors = MatrixOperator.getEigenVectors(B);
		for(int k=0; k<eigenValues.length; k++) {
			if(eigenValues[k] < 0) {
				eigenValues[k] = 0;
			}
		}
		for(int k=0; k<eigenValues.length; k++) {
			if(eigenValues[k] > 0) {
				dimensions++;
			}
		}
		//dimensions = 2;
		double[][] coordinates = new double[distanceMatrix.length][dimensions];
		double[] sqrtEigenValues = MatrixOperator.vectorPower(eigenValues, 1.0/2.0);
		double[][] eigenValuesDiagonal = MatrixOperator.diagonal(sqrtEigenValues);
		double[][] x = MatrixOperator.matrixProduct(eigenVectors, eigenValuesDiagonal);
		/*for(int i=0;i<distanceMatrix.length;i++) {
			for(int j=0;j<dimensions;j++) {
				if(eigenVectors[j][i] >= 0.0) {
					coordinates[i][j] = (float)eigenVectors[j][i];
				}
				else {
					coordinates[i][j] = (float)-eigenVectors[j][i];
				}
			}
		}*/
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int counter = 0;
		for(int i=0; i<x.length;i++) {
			if(x[0][i] != 0.0) {
				indices.add(i);
				counter++;
			}
			if(counter == dimensions) break;
		}
		if (!indices.isEmpty()) {
			for (int i = 0; i < x.length; i++) {
				for (int j = 0; j < indices.size(); j++) {
					coordinates[i][j] = x[i][indices.get(j)];
				}
			}
			return coordinates;
		}
		else {
			return coordinates;
		}
			
	}

}
