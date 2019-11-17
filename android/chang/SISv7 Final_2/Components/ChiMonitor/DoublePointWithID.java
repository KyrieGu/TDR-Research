import org.apache.commons.math3.ml.clustering.DoublePoint;

public class DoublePointWithID extends DoublePoint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = "";
	private String uid = "";
	private String datetime = "";
	private String source = "";
	private String originator = "";

	public DoublePointWithID(double[] point, String id) {
		super(point);
		this.id = id;
	}

	public DoublePointWithID(double[] point, String uid, String datetime, String source, String originator) {
		super(point);
		this.uid = id;
		this.datetime = datetime;
		this.source = source;
		this.originator = originator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	
}
