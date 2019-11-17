import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class ApplyModel {
	Classifier c = null;
	Instances data = null;
	public ApplyModel(String dataName, String modelFile) {
		try {
			data = new Instances(new BufferedReader(new FileReader(dataName + ".arff")));
			data.setClassIndex(data.numAttributes() - 1);
			c = loadModel(modelFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int numAttributes() {
		return data.numAttributes();
	}
	
	public Instance createInstance(ArrayList<double[]> alData) {
		int step = alData.size();
		int channel = alData.get(0).length;
		Instance instance = new Instance(numAttributes());
		double[][] data = new double[channel][step];
		
		for (int i = 0; i < step; i++) {
			double[] datas = alData.get(i);
			for (int j = 0; j < channel; j++) {
				instance.setValue(i*channel+j, datas[j]);
				data[j][i] = datas[j];
			}
		}
		
		
		for(int m = 0; m < channel; m++) {
			Statistics s = new Statistics(data[m]);
			instance.setValue(step*channel+m*3+0, s.getMean());
			instance.setValue(step*channel+m*3+1, s.getVariance());
			instance.setValue(step*channel+m*3+2, s.getStdDev());
		}
		instance.setValue(numAttributes()-1,0);
		return instance;
	}
	
	public double apply(Instance instance) throws Exception {
		double[] result = c.distributionForInstance(instance);
		return result[1];
	}
	
	private Classifier loadModel(String modelFile) throws Exception {

		Classifier classifier;

	    FileInputStream fis = new FileInputStream(modelFile);
	    ObjectInputStream ois = new ObjectInputStream(fis);

	    classifier = (Classifier) ois.readObject();
	    ois.close();

	    return classifier;
	}
}
