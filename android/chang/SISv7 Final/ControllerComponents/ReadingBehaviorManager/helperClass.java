class ReadingBehaviorReading {
	double x;
	double y;
	long t;
	
	public String toString(){
		return	"GazeX: "	+	x	+ "|" +
				"GazeY: "	+	y	+ "|" +
				"Timestamp: "	+	t	+ "|";
	}
}

class ReadingBehaviorManagerReading {
	ReadingBehaviorReading reading;
	String sender;
	
	public ReadingBehaviorManagerReading(String sender, FlowerReading reading){
		this.reading=reading;
		this.sender=sender;
	}
}