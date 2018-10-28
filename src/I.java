import java.math.BigInteger;
import java.util.Scanner;
/**
 * Class for main.
 */
class Main {
	/**
	 * main function takes the input from the console and
	 * returns the output back to console.
	 *
	 * @param      args  The arguments
	 *
	 * @throws     java  throws any java lang exception
	 */
	public static void main(String[] args) throws java.lang.Exception {
		// your code goes here
		Scanner scan = new Scanner(System.in);
		int t = scan.nextInt();
		for (int cs = 1; cs <= t; ++cs) {
			String a = scan.next();
			String b = scan.next();
			System.out.println(a + " " + b);
			BigInteger bg = new BigInteger(a);
			BigInteger gg = new BigInteger(b);
			BigInteger ans = pow(bg, gg);
			String an = ans.toString();
			long gt = gv(an);

			while (gt >= 10) {
				gt = sod(gt);
			}
			System.out.println("Case " + cs + ": " + gt);
		}

	}
	/**
	 * sod function.
	 *
	 * @param      x     
	 *
	 * @return     returns the answer of type long.
	 */
	public static long sod(long x) {
		long anss = 0;
		while (x != 0) {
			anss += x % 10;
			x /= 10;
		}
		return anss;
	}
	/**
	 * function for gv.
	 *
	 * @param      x     of type String.
	 *
	 * @return     returns a long type.
	 */
	public static long gv(String x) {
		long xx = 0;
		for (int i = 0; i < x.length(); ++i) {
			xx += x.charAt(i) - '0';
		}
		return xx;
	}
	/**
	 * this is function for pow.
	 *
	 * @param      base      The base
	 * @param      exponent  The exponent
	 *
	 * @return     returns an variable of type BigInteger.
	 */
	public static BigInteger pow(BigInteger base, BigInteger exponent) {
		BigInteger result = BigInteger.ONE;
		while (exponent.signum() > 0) {
			if (exponent.testBit(0)) {
				result = result.multiply(base);
			}
			base = base.multiply(base);
			exponent = exponent.shiftRight(1);
		}
		return result;
	}


	/**
	 * Class for main.
	 */
	class Main {
		/**
		 * this is Main Function.
		 * It manages the input and output from the console.
		 *
		 * @param      args  The arguments
		 *
		 * @throws     java  
		 */
		public static void main (String[] args) throws java.lang.Exception {
			// your code goes here
			Scanner scan = new Scanner(System.in);
			int t = scan.nextInt();

			for (int cs = 1; cs <= t; ++cs) {
				String a = scan.next();
				String b = scan.next();

				System.out.println(a + " " + b);

				BigInteger bg = new BigInteger(a);
				BigInteger gg = new BigInteger(b);
				BigInteger ans = pow(bg, gg);
				//System.out.println(ans.toString());

				String an = ans.toString();
				long gt = gv(an);

				while (gt >= 10) {
					gt = sod(gt);
				}
				System.out.println("Case " + cs + ": " + gt);
			}

		}


		/**
		 * sod Function.
		 *
		 * @param      x     of type long
		 *
		 * @return     a long type.
		 */
		public static long sod(long x) {
			long anss = 0;

			while (x != 0) {
				anss += x % 10;
				x /= 10;
			}

			return anss;
		}


		/**
		 * gv function.
		 *
		 * @param      x     of type String
		 *
		 * @return     a variable of type long.
		 */
		public static long gv(String x) {
			long xx = 0;

			for (int i = 0; i < x.length(); ++i) {
				xx += x.charAt(i) - '0';
			}

			return xx;
		}

		/**
		 * Funciton for pow.
		 *
		 * @param      base      The base
		 * @param      exponent  The exponent
		 *
		 * @return     a variable of type BIgInteger.
		 */
		public static BigInteger pow(BigInteger base, BigInteger exponent) {
			BigInteger result = BigInteger.ONE;

			while (exponent.signum() > 0) {
				if (exponent.testBit(0)) result = result.multiply(base);

				base = base.multiply(base);
				exponent = exponent.shiftRight(1);
			}
			return result;
		}
	}