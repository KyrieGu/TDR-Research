FlowerReading reading1 = new FlowerReading();
temperature=kvList.getValue("AirTemperature");
fertilizer=kvList.getValue("Fertilizer");
sunshine=kvList.getValue("Light");
moisture=kvList.getValue("Moisture");
dateFP=kvList.getValue("Date");
if (temperature != null && !temperature.equals(""))
{
	reading1.temperature = Double.parseDouble(temperature);
}
else{
	reading1.temperature = 0;
}
if (fertilizer != null && !fertilizer.equals(""))
{
	reading1.fertilizer = Double.parseDouble(fertilizer);
}
else{
	reading1.fertilizer = 0;
}
if (sunshine != null && !sunshine.equals(""))
{
	reading1.sunshine = Double.parseDouble(sunshine);
}
else{
	reading1.sunshine = 0;
}
if (moisture != null && !moisture.equals(""))
{
	reading1.moisture = Double.parseDouble(moisture);
}
else{
	reading1.moisture = 0;
}
if (dateFP != null && !dateFP.equals(""))
{
	reading1.dateFP = Long.parseLong(dateFP);
}
else{
	reading1.dateFP = 0;
}

System.out.println(reading1);

readings.add(new FlowerManagerReading(sender,reading1));
				//reading.lengthOfData++;