import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

public class DBSCAN {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		DBSCAN d = new DBSCAN();
		System.out.println(d.isMajor(3, 2, 5, 5, 5, 5, 5, "records", "Select * From records where DATEDIFF(NOW(),datetime)<30 and source='SocialNetwork' order by rid desc"));


	}
	
	public boolean isMajor(double eps, int minPoints, int tongue, int fatigue, int weakBreadth, int pulse, int sweaty, String table, String sql) {
		int dimension = 5;
		DBSCANClusterer<DoublePoint> dbscan = new DBSCANClusterer<DoublePoint>(eps, minPoints);
		// List<DoublePoint> points = readFile(file, dimension;
		List<DoublePoint> points = readDatabase(dimension, table, sql);

		List<Cluster<DoublePoint>> cluster = dbscan.cluster(points);
		//System.out.println(points.size());
		//System.out.println(cluster.size());

		Cluster<DoublePoint> c;
		int maxSize = 0;
		for (int i = 0; i < cluster.size(); i++) {
			c = cluster.get(i);
			if (c.getPoints().size() > maxSize) {
				maxSize = i;
			}
		}
		c = cluster.get(maxSize);
		List<DoublePoint> list = c.getPoints();
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			s = "";
			for (int j = 0; j < dimension; j++) {
				s = s + (int)list.get(i).getPoint()[j];
			}
			result.put(s, 0);
		}
		
//		for (Cluster<DoublePoint> c : cluster) {
//		List<DoublePoint> list = c.getPoints();
//		System.out.println(list.size());
//		for (int i = 0; i < list.size(); i++) {
//			System.out.print(((DoublePointWithID) list.get(i)).getDatetime() + "\t");
//			for (int j = 0; j < dimension; j++) {
//				System.out.print(list.get(i).getPoint()[j] + "\t");
//			}
//			System.out.println();
//		}
//		System.out.println();
//
//		}
		return result.containsKey(""+tongue+fatigue+weakBreadth+pulse+sweaty);
	}

	private static List<DoublePoint> readDatabase(int dimension, String table, String sql) {
		List<DoublePoint> points = new ArrayList<DoublePoint>();

		Data data = new Data(table, sql);
		ArrayList<HashMap<String, String>> records = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> record = new HashMap<String, String>();
		String oldDate = "";
		String date = "";
		for (int i = 0; i < data.size(); i++) {
			// System.out.println(data.getRecord(i, "uid"));
			// System.out.println(data.getRecord(i, 0));
			// System.out.println(data.getRecord(i));
			date = data.getRecord(i, "datetime");
			if (oldDate.equals("") || !oldDate.equals(date)) {
				if (record.size() == 9) {
					records.add(record);
				}
				oldDate = date;
				record = new HashMap<String, String>();
				record.put("uid", data.getRecord(i, "uid"));
				record.put("datetime", data.getRecord(i, "datetime"));
				record.put("source", data.getRecord(i, "source"));
				record.put("originator", data.getRecord(i, "originator"));
			}

			if (oldDate.equals(date)) {
				if (!data.getRecord(i, "type").equals("chiTotal")) {
					record.put(data.getRecord(i, "type"), data.getRecord(i, "value"));
				}

			}
		}

		// so far I get all data in records
		String types[] = { "tongue", "fatigue", "weakBreadth", "pulse", "sweaty", };
		for (int i = 0; i < records.size(); i++) {
			record = records.get(i);
			double[] d = new double[dimension];
			System.out.print(record.get("datetime") + "\t");
			for (int j = 0; j < dimension; j++) {
				// System.out.println(j);
				// System.out.println(types[j]);
				// System.out.println(record.get(types[j]));
				d[j] = Double.parseDouble(record.get(types[j]));
				System.out.print(d[j] + "\t");
			}
			System.out.println();
			points.add(new DoublePointWithID(d, record.get("uid"), record.get("datetime"), record.get("source"),
					record.get("originator")));
		}

		return points;
	}

	private static List<DoublePoint> readFile(String file, int dimension) throws FileNotFoundException, IOException {

		List<DoublePoint> points = new ArrayList<DoublePoint>();
		File f = new File(file);
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line;

		while ((line = in.readLine()) != null) {
			try {
				double[] d = new double[dimension];
				String[] s = line.split("\t");
				for (int i = 0; i < dimension; i++) {
					d[i] = Double.parseDouble(s[i]);
				}
				points.add(new DoublePointWithID(d, "id is here"));
			} catch (ArrayIndexOutOfBoundsException e) {
			} catch (NumberFormatException e) {
			}
		}
		in.close();
		return points;
	}

}
