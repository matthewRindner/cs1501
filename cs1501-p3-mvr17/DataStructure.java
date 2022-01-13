import java.util.*;
import java.lang.*;

public class DataStructure {
	//String sentinelKey = "";
	IndexMinPQ<Car> mileagePQ = new IndexMinPQ<Car>(50000, "mileage");
	IndexMinPQ<Car> pricePQ = new IndexMinPQ<Car>(50000, "price");	
	TST<Integer> priceTST = new TST<Integer>();
	TST<Integer> mileageTST = new TST<Integer>();
	TST<IndexMinPQ<Car>> make_model_priceTST = new TST<IndexMinPQ<Car>>();
	TST<IndexMinPQ<Car>> make_model_mileageTST = new TST<IndexMinPQ<Car>>();
	int car_amount;	//amount of cars added
	int  priceCurrSize = 50000;
	int mileCurrSize = 50000;
	public DataStructure()
	{
		int car_amount = 0;
	}

	public void add(Car newCar)
	{
		if(newCar ==  null) throw new NullPointerException();
		//concatinates the make and model of new car in one varialble
		String makeModel = newCar.getMake() + newCar.getModel();	

		//calls to add newCar and attributes either IndexMinPQ or TSTs
		priceTST.put(newCar.getVin(), car_amount);
		mileageTST.put(newCar.getVin(), car_amount);
		mileagePQ.insert(car_amount, newCar);
		pricePQ.insert(car_amount, newCar);
		//priceCurrSize = pricePQ.capacity();
		//mileCurrSize = mileagePQ.capacity();
		if(!make_model_priceTST.contains(makeModel))			
		{
			//if m/mPTST does not have a PQ of Car of same m/m, create a PQ of car object and set it to newCar at index car_amount
			//add newly created PQ of Car object to m/mPTST
			IndexMinPQ<Car> tempPQ = new IndexMinPQ<Car>(priceCurrSize, "price"); //size 5 , seintenal value
			tempPQ.insert(car_amount, newCar);
			make_model_priceTST.put(makeModel, tempPQ);	//(key, value)
		}
		else
		{
			//if m/mPTST has a PQ of Car of same m/m, we dont need to create a another new PQ of car object
			//so same as above with the sifferent price
			IndexMinPQ<Car> tempPQ = make_model_priceTST.get(makeModel);
			tempPQ.insert(car_amount, newCar);
			make_model_priceTST.put(makeModel, tempPQ);
		}

		if(!make_model_mileageTST.contains(makeModel))
		{
			IndexMinPQ<Car> tempPQ = new IndexMinPQ<Car>(mileCurrSize, "mileage");
			tempPQ.insert(car_amount, newCar);
			make_model_mileageTST.put(makeModel, tempPQ);
		}
		else
		{
			IndexMinPQ<Car> tempPQ = make_model_mileageTST.get(makeModel);
			tempPQ.insert(car_amount, newCar);
			make_model_mileageTST.put(makeModel, tempPQ);
		}
		car_amount++;

	}

	public void remove(String vin)
	{
		//we need to remove a car with same VIN in ALL TSTs and IndexMinPQs

		// if(priceTST.size() ==  0) throw new IndexOutOfBoundsException("size is zero");
		// if(vin == null) throw new NullPointerException();

		// int priceIndex = priceTST.get(vin);
		// pricePQ.delete(priceIndex);
		// priceTST.delete(vin);

		// //Car tempPriceCar = pricePQ.keyOf(index);

		// int mileIndex = mileTST.get(vin);
		// milePQ.delete(mileIndex);
		// mileTST.delete(vin);
		// //Car tempMileCar = milePQ.keyOf(index);

		// //remove the car with matching VIN from m/mPTST and m/mMTST by String key
		// //m/mPTST stores PQ of Car
		// Car tempCar = new Car();
		// tempCar.setVin(vin);
		// IndexMinPQ<Car> tempPQ_1 = new IndexMinPQ<Car>(5, "mileage");
		// tempPQ_1.insert(car_amount, tempCar);
		// IndexMinPQ<Car> tempPQ = new IndexMinPQ<Car>(5, "price");
		// tempPQ_2.insert(car_amount, tempCar);

		// if(make_model_priceTST.contains(get(makeModel).getVin()).equals(vin))	
		// if(make_model_priceTST.get(makeModel).keyOf(car_amount).getVin().equals(vin))
		// if(make_model_priceTST.contains(get.makeModel))
		// {
		// 	if(make_model_priceTST,get(makeModel).)
		// }

		//------------------------------------------------------------------------
		int found_carAmount = priceTST.get(vin).intValue();
		priceTST.put(vin, null);
		mileageTST.put(vin, null);
		//we now have the bridge btw TSTs and IndexMinPQ
		String makeModel = "";

		if(mileagePQ.contains(found_carAmount))
		{
			//before we remove from PQ, we get the makeModel, our index for TST<PQ<Car>>, and store it 
			Car carObject = mileagePQ.keyOf(found_carAmount);
			makeModel = carObject.getMake() + carObject.getModel();
			mileagePQ.delete(found_carAmount);
			IndexMinPQ<Car> tempPQ = make_model_mileageTST.get(makeModel);
			if(tempPQ.isEmpty())
			{
				System.out.println("OOF");
			}
			tempPQ.delete(found_carAmount);
			
			make_model_mileageTST.put(makeModel, tempPQ);

		}
		if(pricePQ.contains(found_carAmount))
		{
			pricePQ.delete(found_carAmount);
			IndexMinPQ<Car> tempPQ = make_model_priceTST.get(makeModel);
			if(tempPQ.isEmpty())
			{
				System.out.println("OOF");
			}
			tempPQ.delete(found_carAmount);

			//pricePQ.delete(found_carAmount);

			make_model_priceTST.put(makeModel, tempPQ);
		}
		//now we can remove car with matching Vin from TST<PQ<Car>>

		make_model_priceTST.put(makeModel, null);
		make_model_mileageTST.put(makeModel, null);
		car_amount--;



	}

	public Car getLowestMileage()
	{
		if(mileageTST.size() == 0) return null;

		Car lowesetMileage = mileagePQ.minKey();
		//using the mileage from the mileagePQ, find that car with the same mileage in the carsTST 
		return lowesetMileage;
	}

	public Car getLowestPrice()
	{
		if(priceTST.size() == 0) return null;

		Car lowesetPrice = pricePQ.minKey();
		//using the price from the pricePQ, find that car with the same price in the carsTST 
		return lowesetPrice;
	}

	public Car getLowestPriceBy_MM(String make, String model)
	{
		
		//---------------------------------------------
		// String makeModel = newCar.getMake() + newCar.getModel();	//key
		// //IndexMinPQ<Car> possibleValidCarMatchingMM = make_model_priceTST.get(makeModel);	//we need ot iterate throght TST for all keys with matching MM
		// Car carFound = new Car();
		// Car possibleValidCarLowestPrice = pricePQ.minKey();//********************
		// Queue<IndexMinPQ<Car>> allPossibleCarsMatchingMM = new Queue<IndexMinPQ<Car>>();
		// allPossibleCarsMatchingMM = make_model_priceTST.collect(makeModel);
		// boolean foundFlag = false;

		// while(!allPossibleCarsMatchingMM.isEmpty() && !foundFlag)
		// {
		// 	if(!possibleValidCarLowestPrice.equals((Car)allPossibleCarsMatchingMM.peek().getPrice())) //calling getPrice on an MinIndexPQ of car object, not car
		// 	{
		// 		allPossibleCarsMatchingMM.remove();
		// 		break;
		// 	}
		// 	else
		// 	{
		// 		carFound = (Car)allPossibleCarsMatchingMM.remove();
		// 		foundFlag = true;
		// 	}
		// }
		//--------------------------------------------
		String makeModel = make + model;	//key
		IndexMinPQ<Car> possibleValidCarMatchingMM = make_model_priceTST.get(makeModel);	//we need ot iterate throght TST for all keys with matching MM
		Car possibleValidCarLowestPrice = possibleValidCarMatchingMM.minKey();
		//priceTST.

		//if(possibleValidCarLowestPrice.getPrice() == possibleValidCarMatchingMM.keyOf(car_amout).getPrice())
		return possibleValidCarLowestPrice;
	}

	public Car getLowestMileageBy_MM(String make, String model)
	{
		
		String makeModel = make + model;	//key
		IndexMinPQ<Car> possibleValidCarMatchingMM = make_model_mileageTST.get(makeModel);	//we need ot iterate throght TST for all keys with matching MM
		Car possibleValidCarLowestPrice = possibleValidCarMatchingMM.minKey();

		return possibleValidCarLowestPrice;
	}

	public void updateCar(String vin, String updatedAttribute, int whichAttribute)
	{
		if(priceTST.size() == 0) return;

		int index = priceTST.get(vin).intValue();	//gets the car_amount
			
		//else System.out.print("cannot find car with VIN: " + vin);

		Car oldCar = pricePQ.keyOf(index);			//the car user chose to update
		String makeModel = oldCar.getMake() + oldCar.getModel();
		//boolean foundFlag = false;

		if(whichAttribute == 1)	
		{
			//updating the price
			int newPrice = Integer.parseInt(updatedAttribute);

			mileagePQ.keyOf(index).setPrice(newPrice);
			pricePQ.keyOf(index).setPrice(newPrice);

			//we dont need to update the car in our price/mileageTST since it stores only VINs and the car_amount (index)

			IndexMinPQ<Car> temp = make_model_mileageTST.get(makeModel);
			IndexMinPQ<Car> tempAnother = make_model_priceTST.get(makeModel);
			temp.keyOf(index).setPrice(newPrice);
			tempAnother.keyOf(index).setPrice(newPrice);
		}
		else if(whichAttribute == 2)
		{
			//updating the mileage
			int newMileage = Integer.parseInt(updatedAttribute);

			mileagePQ.keyOf(index).setMileage(newMileage);
			pricePQ.keyOf(index).setMileage(newMileage);

			//we dont need to update the car in our price/mileageTST since it stores only VINs and the car_amount (index)

			IndexMinPQ<Car> temp = make_model_mileageTST.get(makeModel);
			IndexMinPQ<Car> tempAnother = make_model_priceTST.get(makeModel);
			temp.keyOf(index).setMileage(newMileage);
			tempAnother.keyOf(index).setMileage(newMileage);

		}
		else if(whichAttribute == 3)
		{
			//updating the color
			String newColor = updatedAttribute;

			mileagePQ.keyOf(index).setColor(newColor);
			pricePQ.keyOf(index).setColor(newColor);

			//we dont need to update the car in our price/mileageTST since it stores only VINs and the car_amount (index)

			IndexMinPQ<Car> temp = make_model_mileageTST.get(makeModel);
			IndexMinPQ<Car> tempAnother = make_model_priceTST.get(makeModel);
			temp.keyOf(index).setColor(newColor);
			tempAnother.keyOf(index).setColor(newColor);
		}


	}
	public void printCars()
	{
		System.out.println("-------------------PRICE PQ-----------------");
		pricePQ.printCars();
		System.out.println("--------------------------------------------");
		System.out.println("------------------MILEAGE PQ----------------");
		mileagePQ.printCars();
		System.out.println("--------------------------------------------");
	}
	public boolean contains(String vin)
	{
		return priceTST.contains(vin);

	}
}