import java.util.*;
import java.lang.*;

public class Car implements Comparable<Car>{

	private String vin;
	private String make;
	private String model;
	private int price;
	private int mileage;
	private String color;

	public Car(String v, String ma, String mo, int p, int mi, String c)
	{
		vin = v;
		make = ma;
		model = mo;
		price = p;
		mileage = mi;
		color = c;
	}
	@Override
	public int compareTo(Car c)
	{
		return 0;
	}

	public int compareTo(Car c, String senKey)
	{
		if(senKey.equals("price"))
		{	return this.getPrice() - c.getPrice();}
		if(senKey.equals("mileage"))
		{	return this.getMileage() - c.getMileage();}
		return -100000;
	}
	//-------Getters--------\\
	public String getVin()
	{
		return vin;
	}

	public String getMake()
	{
		return make;
	}

	public String getModel()
	{
		return model;
	}

	public int getPrice()
	{
		return price;
	}

	public int getMileage()
	{
		return mileage;
	}

	public String getcolor()
	{
		return color;
	}

	//-------Setters--------\\

	public void setVin(String str)
	{
		vin = str;
	}

	public void setMake(String str)
	{
		make = str;
	}

	public void setModel(String str)
	{
		model = str;
	}

	public void setPrice(int i)
	{
		price = i;
	}

	public void setMileage(int i)
	{
		mileage = i;
	}

	public void setColor(String str)
	{
		color = str;
	}

	public String toString()
	{
		return "VIN: " + vin + "\nMake: " + make + "\nModel: " + model + 
			"\nPrice: " +  price + "\nMileage: " + mileage + "\nColor: " + color;

	}
}