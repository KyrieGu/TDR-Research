FlowerReading reading2 = new FlowerReading();
temperature=kvList.getValue("AirTemperature");
fertilizer=kvList.getValue("Fertilizer");
sunshine=kvList.getValue("Light");
moisture=kvList.getValue("Moisture");
dateFP=kvList.getValue("Date");
if (temperature != null && !temperature.equals(""))
{
	reading2.temperature = Double.parseDouble(temperature);
}
else{
	reading2.temperature = 0;
}
if (fertilizer != null && !fertilizer.equals(""))
{
	reading2.fertilizer = Double.parseDouble(fertilizer);
}
else{
	reading2.fertilizer = 0;
}
if (sunshine != null && !sunshine.equals(""))
{
	reading2.sunshine = Double.parseDouble(sunshine);
}
else{
	reading2.sunshine = 0;
}
if (moisture != null && !moisture.equals(""))
{
	reading2.moisture = Double.parseDouble(moisture);
}
else{
	reading2.moisture = 0;
}
if (dateFP != null && !dateFP.equals(""))
{
	reading2.dateFP = Long.parseLong(dateFP);
}
else{
	reading2.dateFP = 0;
}

System.out.println(reading2);

readings.add(new FlowerManagerReading(sender,reading2));