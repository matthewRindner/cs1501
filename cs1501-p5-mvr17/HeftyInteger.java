import java.util.*;
import java.lang.*;

public class HeftyInteger {

	private final byte[] ONE = {(byte) 1};
	private final byte[] ZERO = {(byte) 0};

	private byte[] val;

	/**
	 * Construct the HeftyInteger from a given byte array
	 * @param b the byte array that this HeftyInteger should represent
	 */
	public HeftyInteger(byte[] b) {
		val = b;
	}

	/**
	 * Return this HeftyInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other HeftyInteger to sum with this
	 */
	public HeftyInteger add(HeftyInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public HeftyInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		HeftyInteger neg_li = new HeftyInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new HeftyInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other HeftyInteger to subtract from this
	 * @return difference of this and other
	 */
	public HeftyInteger subtract(HeftyInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other HeftyInteger to multiply by this
	 * @return product of this and other
	 */
	public HeftyInteger multiply(HeftyInteger other) {
		// // YOUR CODE HERE (replace the return, too...)
		HeftyInteger product = new HeftyInteger(ZERO);

		boolean negate_me = false;

		HeftyInteger multiplicand = new HeftyInteger(this.getVal());	//bigger or the top
		HeftyInteger multiplier =  new HeftyInteger(other.getVal());	//smaller or the botttom

		if(multiplicand.isNegative() ^ multiplier.isNegative())
		{
			if(multiplicand.isNegative())
			{
				multiplicand = multiplicand.negate();
			}
			if(multiplier.isNegative())
			{
				multiplier = multiplier.negate();
			}
			negate_me = true;
		}

		if(multiplicand.isNegative() && multiplier.isNegative())
		{
			multiplicand = multiplicand.negate();
			multiplier = multiplier.negate();
		}
	

		// if(other.length() <= this.length())
		// {
		// 	multiplicand = other;
		// 	multiplier = this;
		// }
		// else
		// {
		// 	multiplicand = this;
		// 	multiplier = other;
		// }

		// if(multiplier.isZero() || multiplicand.isZero())
		// 	return zero;

		// if(multiplier.isNegative())
		// {
		// 	multiplier = multiplier.negate();
		// }
		// else if(multiplicand.isNegative())
		// {
		// 	multiplicand = multiplicand.negate();
		// }

		// boolean is_result_neg;
		// if(multiplicand.isNegative() && !multiplier.isNegative() || !multiplicand.isNegative() && multiplier.isNegative())
		// 	is_result_neg = true;
		// else
		// 	is_result_neg = false;

		
		//the actual gradeschool algorithm
		//for(int i = 0; i == multiplier.length(); i++)
		//{
		while(!multiplier.isZero(multiplier.getVal())){
			if(!isLSB_zero(multiplier))		//isLSB_zero(multiplier) //multiplier & one) != 0
			{
				byte[] temp = new byte[multiplicand.getVal().length + 1];
				System.arraycopy(multiplicand.getVal(), 0, temp, 1, multiplicand.getVal().length);
				//product = product.add(new HeftyInteger(Arrays.copyOfRange(multiplicand.getVal(), 1, multiplicand.getVal().length + 1)));
				//System.arraycopy(multiplicand.getVal(), 1, multiplicand.getVal(), multiplicand.getVal().length, multiplicand.getVal().length+1);
				product = product.add(new HeftyInteger(temp));
				//product = product.add(new HeftyInteger(padBytes(multiplicand.getVal())));
			}
				//product = product.add(new HeftyInteger(Arrays.copyOfRange(multiplicand.getVal(), 1, multiplicand.getVal().length + 1)));	//product += multiplicand;

			multiplicand =  shiftLeft(multiplicand.getVal()); 
			multiplier = shiftRight(multiplier.getVal());
		}
		//}

		if(negate_me) 
			product = product.negate();


		return product;
	}

	private boolean isLSB_zero(HeftyInteger num)
	{
		return (num.getVal()[num.getVal().length - 1] & 0x1) == 0;
	}
	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another HeftyInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public HeftyInteger[] XGCD(HeftyInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		
		//this = x   other = y
		//i = gcd(a, b) = ax + by

	 	/*
		HeftyInteger one = new HeftyInteger(ONE);
		HeftyInteger zero = new HeftyInteger(ZERO);

		if(other.isZero())				//if(q== 0)
			return new int[] {this, one, zero}

		byte[] vals = other.XGCD(this % other);
		HeftyInteger i = vals[0];
		HeftyInteger a = vals[1];
		HeftyInteger b = vals[2].subtract((this.divide(other)).multiply(a))

		return new byte[] {i, a, b}
		*/

		return null;
	 }

	 private boolean isZero(byte[] other)
	 {
	 	for(int i = 0; i < other.length; i++)
	 	{
	 		if(other[i] != (byte)0)
	 			return false;
	 	}
	 	return true;
	 }

	 public HeftyInteger shiftLeft(byte[] arr)
	 {
	 	boolean flag = false;
	 	boolean padding = (byte)(arr[0] & 0x80) != 0;

	 	if(padding)
 		{
 			arr = padBytes(arr);
 			//Arrays.copyOfRange(arr, 1, arr.length + 1);

 		}
	 	for(int i = val.length-1; i >= 0; i--)
	 	{
	 		boolean mask = (byte)(arr[i] & 0x80) == 0;
	 		//arr[i] &= 0x80;
	 		arr[i] <<= 1;

	 		if(flag) arr[i] |= 1; //ob1 lol 

	 		if(!mask) flag = true;
 			else flag = false;
	 	}
	 	return new HeftyInteger(arr);
	 }

	 public HeftyInteger shiftRight(byte[] arr)
	 {
	 	//byte[] newval = new byte[this.length()];
	 	
		boolean flag = false; //caryout

 	
 		for(int i = 0; i < arr.length; i++)
 		{
 			boolean mask = (byte)(arr[i] & 0x1) == 0;

 			arr[i] = (byte)((arr[i] & 0xFF) >> 1);

 			if(flag)
 			{
 				arr[i] |= 0x80; //ob1000000

 			}
 			if(!mask) flag = true;
 			else flag = false;
 		}
 	

	 	arr[0] &= 0x7F;
	 	// int signBit = (msb >> 7) & 1;

	 	// for(int i = 0; i < shiftNum; i++)
	 	// {
	 	// 	newval[i] = (byte)signBit;
	 	// }
	 	return new HeftyInteger(arr);
	 }
public byte[] padBytes(byte[] original)
	{
		byte[] paddedArray = new byte[ original.length + 1];
		for(int bit = 0; bit < original.length; bit++)
		{
			paddedArray[bit + 1] = original[bit];
		}
		return paddedArray;	
	}
	
}
