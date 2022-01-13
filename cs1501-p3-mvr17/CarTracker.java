import java.util.*;
import java.lang.*;
import java.io.*;

public class CarTracker{

	static DataStructure thingy = new DataStructure();
	
	static Scanner scanner = new Scanner(System.in);	

	public static void main(String[] args)
	{
		//String v, String ma, String mo, int p, int mi, String c
		readFromFile();
		System.out.println("Cars.txt has been read in successfully");

		int optionNum;
		//boolean continueFlag = true;

		

		while(true)
		{
			System.out.println();
			System.out.println("---------------Car Tracker Program---------------");
			System.out.println("Please enter the number of the option you want from the list below:");
			System.out.println("1.	Add a car");
			System.out.println("2.	Update a car");
			System.out.println("3.	Remove a car");
			System.out.println("4.	Get the lowest price of a car");
			System.out.println("5.	Get the lowest mileage of a car");
			System.out.println("6.	Get the lowest price of a car by make and model");
			System.out.println("7.	Get the lowest mileage of a car by make and model");
			System.out.println("8. 	Quit");
			optionNum = scanner.nextInt();

			if(optionNum == 1)
			{
				addCar();
				System.out.println("car has been added");
			}
			
			else if(optionNum == 2)
			{
				update();
				
			}
			
			else if(optionNum == 3)
			{
				System.out.print("Enter the VIN of the car you want to remove: ");
				String vin = scanner.next();
				//NEED TO CHECK IF VIN OF CAR IS ALREADY IN SYSTEM
				if(!thingy.contains(vin))
				{
					System.out.println("Car with that VIN is not in the system");
					return;
				}

				thingy.remove(vin);
				System.out.println("car has been removed");

			}
			
			else if(optionNum == 4)
			{
				System.out.print(" the lowest price of a car is: ");
				Car carFound = thingy.getLowestPrice();
				System.out.println(carFound.toString());
			}

			else if(optionNum == 5)
			{		
				System.out.print(" the lowest price of a mileage is: ");
				Car carFound = thingy.getLowestMileage();
				System.out.println(carFound.toString());

			}
			
			else if(optionNum == 6)
			{
				System.out.println("Enter the car make: ");
				String make = scanner.next();
				System.out.println("Enter the car model: ");
				String model = scanner.next();

				Car carFound = thingy.getLowestPriceBy_MM(make, model);
				System.out.print(" the lowest price of a car by make and model is: ");
				System.out.print(carFound.toString());
			}

			else if(optionNum == 7)
			{
				System.out.println("Enter the car make: ");
				String make = scanner.next();
				System.out.println("Enter the car model: ");
				String model = scanner.next();

				Car carFound = thingy.getLowestMileageBy_MM(make, model);
				System.out.print(" the lowest mileage of a car by make and model is: ");
				System.out.print(carFound.toString());
			}
			
			else if(optionNum == 8)
			{
				System.exit(0);
			}
			else
			{
				System.out.println("Invalied option");
			}
		}


	}

	
	public static void update()
	{
			System.out.print("Enter the VIN of the car you want to change: ");
			String vin = scanner.next();
			//NEED TO CHECK IF VIN OF CAR IS ALREADY IN SYSTEM
			if(!thingy.contains(vin))
			{
				System.out.println("Car with that VIN is not in the system");
				return;
			}
			System.out.println("Car found");
			System.out.println();

			System.out.println("Enter the option you want to change as a number: ");
			System.out.println("1. Update the price");
			System.out.println("2. Update the mileage");
			System.out.println("3. Update the color");

			
			int  updateOptionNum = Integer.parseInt(scanner.next());
			String newValue;

			if(updateOptionNum == 1)
			{
				System.out.println("Please enter the new price as a whole number: ");
				newValue = scanner.next();
				thingy.updateCar(vin, newValue, 1);
				System.out.println("car has been updated");

			}
			else if(updateOptionNum == 2)
			{
				System.out.println("Please enter the new mileage as a whole number: ");
				newValue = scanner.next();
				thingy.updateCar(vin, newValue, 2);
				System.out.println("car has been updated");
			}
			else if(updateOptionNum == 3)
			{

				System.out.println("Please enter the new color: ");
				newValue = scanner.next();
				thingy.updateCar(vin, newValue, 3);
				System.out.println("car has been updated");
			}
			else
			{
				System.out.print("invalid option");
			}


	}
	
	public static void addCar()
	{
		Car newCar = new Car(null, null, null, -1, -1, null);
		String vin = "";
		String make = "";
		String model = "";
		int price;
		int mileage;
		String color = "";



		System.out.print("Please enter VIN number: ");
		vin = scanner.next();
		//NEED TO CHECK IF VIN OF CAR IS ALREADY IN SYSTEM
		if(thingy.contains(vin))
		{
			System.out.println("Car with that VIN is already in the system");
			return;
		}

		newCar.setVin(vin);
		System.out.println("vin: " + vin);

		System.out.println("Please enter the car make: ");
		make = scanner.next();
		newCar.setMake(make);
		System.out.println("make: " + make);

		System.out.println("Please enter the car model: ");
		model = scanner.next();
		newCar.setModel(model);
		System.out.println("model: " + model);

		System.out.println("Please enter the car price USD as a whole number: ");
		price = Integer.parseInt(scanner.next());
		newCar.setPrice(price);
		System.out.println("price: " + price);

		System.out.println("Please enter the car mileage as a whole number: ");
		mileage = Integer.parseInt(scanner.next());
		newCar.setMileage(mileage);	
		System.out.println("mileage: " + mileage);

		System.out.println("Please enter the car color: ");
		color = scanner.next();
		newCar.setColor(color);
		System.out.println("color: " + color);	

		thingy.add(newCar);

	}

	public static void readFromFile()
		{
			try{
					BufferedReader input = new BufferedReader(new FileReader("cars.txt"));
					boolean read = true;

					while(input.ready())
					{
						String carSpec_line = input.readLine();
						if(carSpec_line.charAt(0) == '#')	//skips the first line
						{
							read = false;
							//System.out.println("OOP");
							continue;
						}	
						if(read == false)
						{
							String[] attribute = carSpec_line.split(":");
						//System.out.println("damn");
						//System.out.println(attribute[0]);
						Car newCar = new Car(attribute[0], attribute[1], attribute[2], Integer.parseInt(attribute[3]), Integer.parseInt(attribute[4]), attribute[5]);
						thingy.add(newCar);
						//thingy.printCars();

						}

						
					}
				}
				catch(IOException e )
				{
					System.out.println("failed to read in file");
				}
				
		}
}