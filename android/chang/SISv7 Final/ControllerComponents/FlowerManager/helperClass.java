class FlowerReading {
	double sunshine;
	double temperature;
	double moisture;
	double fertilizer;
	long dateFP;
	long preDateFP;
	
	public String toString(){
		return	"Sunshine: "	+	sunshine	+ "|" +
				"Temperature: "	+	temperature	+ "|" +
				"Moisture: "	+	moisture	+ "|" +
				"Fertilizer: "	+	fertilizer	+ "|" +
				"DateFP: "		+	dateFP		+ "|" +
				"PreDateFP: "	+	preDateFP	+ "|";
	}
}

class FlowerManagerReading {
	FlowerReading reading;
	String sender;
	
	public FlowerManagerReading(String sender, FlowerReading reading){
		this.reading=reading;
		this.sender=sender;
	}
}