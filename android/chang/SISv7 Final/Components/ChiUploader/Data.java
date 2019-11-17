import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class Data implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	public ArrayList<String> columns = new ArrayList<String>();

	public Data(String s) {

	}

	public Data(String table, String QueryIn) {
		String UrlIn = "http://ksiresearch.org/chronobot/PHP_Post.php";
		String q = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '" + table + "'";
		this.getColumns(UrlIn, q);
		this.getRecords(UrlIn, QueryIn);
	}

	public String getRecord(int index, String attribute) {
		return data.get(index).get(attribute);
	}

	public String getRecord(int index, int i) {
		return data.get(index).get(columns.get(i));
	}

	public String getRecord(int index) {
		String s = "";
		for (int i = 0; i < columns.size(); i++) {
			s = s + data.get(index).get(columns.get(i)) + ", ";
		}
		return s.substring(0, s.length() - 2);
	}

	public int size() {
		return data.size();
	}

	public ArrayList<String> getColumns() {
		return columns;
	}

	private void getColumns(String UrlIn, String QueryIn) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(UrlIn);
			String agent = "Applet";
			String query = "query=" + QueryIn;
			String type = "application/x-www-form-urlencoded";
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", agent);
			conn.setRequestProperty("Content-Type", type);
			conn.setRequestProperty("Content-Length", "" + query.length());
			OutputStream out = conn.getOutputStream();
			out.write(query.getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				columns.add(inputLine.trim());
				// System.out.print(inputLine+"\n");
			}
			in.close();
			int grc = conn.getResponseCode();
			// System.out.print("ResponseCode = "+ grc +"\n");
			String grm = conn.getResponseMessage();
			// System.out.print("ResponseMessage = "+ grm +"\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
	}

	private void getRecords(String UrlIn, String QueryIn) {
		String[] datas = null;
		HashMap<String, String> r = new HashMap<String, String>();
		HttpURLConnection conn = null;
		try {
			URL url = new URL(UrlIn);
			String agent = "Applet";
			String query = "query=" + QueryIn;
			String type = "application/x-www-form-urlencoded";
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", agent);
			conn.setRequestProperty("Content-Type", type);
			conn.setRequestProperty("Content-Length", "" + query.length());
			OutputStream out = conn.getOutputStream();
			out.write(query.getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				datas = inputLine.split("\t");
				for (int i = 1; i < datas.length; i++) {
					r.put(columns.get(i - 1), datas[i]);
				}
				data.add(r);
				r = new HashMap<String, String>();
				// System.out.print(inputLine+"\n");
			}
			in.close();
			int grc = conn.getResponseCode();
			// System.out.print("ResponseCode = "+ grc +"\n");
			String grm = conn.getResponseMessage();
			// System.out.print("ResponseMessage = "+ grm +"\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
	}

	/** Read the object from Base64 string. */
	private static Object fromString(String s) throws IOException, ClassNotFoundException {
		byte[] data = Base64.getDecoder().decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	/** Write the object to a Base64 string. */
	public static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Data data = new Data("records",
				"Select * From records where DATEDIFF(NOW(),datetime)<7 and type='spo2' and value > 0 and uid='376896'  order by rid desc");
		for (int i = 0; i < data.size(); i++) {
			// System.out.println(data.getRecord(i, "uid"));
			// System.out.println(data.getRecord(i, 0));
			// System.out.println(data.getRecord(i));
		}
		String s = Data.toString(data);
		
		
		// You should get a string from my message, attribute "result"
		Data data2 = (Data) Data.fromString(s);
		for (int i = 0; i < data2.size(); i++) {
			System.out.println(data2.getRecord(i, "uid"));
			// System.out.println(data.getRecord(i, 0));
			// System.out.println(data.getRecord(i));
		}
	}

}
