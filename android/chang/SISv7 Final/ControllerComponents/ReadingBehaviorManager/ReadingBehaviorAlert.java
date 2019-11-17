ReadingBehaviorReading reading2 = new ReadingBehaviorReading();
x=kvList.getValue("GazeX");
y=kvList.getValue("GazeY");
t=kvList.getValue("Timestamp");
if (x != null && !x.equals(""))
{
	reading2.x = Double.parseDouble(x);
}
else{
	reading2.x = 0;
}
if (y != null && !y.equals(""))
{
	reading2.y = Double.parseDouble(y);
}
else{
	reading2.y = 0;
}
if (t != null && !t.equals(""))
{
	reading2.t = Long.parseLong(t);
}
else{
	reading2.t = 0;
}

System.out.println(reading2);

readings.add(new ReadingBehaviorManagerReading(sender,reading2));