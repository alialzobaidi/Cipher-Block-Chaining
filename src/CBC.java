
import java.util.Scanner;

public class CBC {

	private static Scanner sc;

	public static void main(String[] args) {

		sc = new Scanner(System.in);
		KeyGeneration KG = new KeyGeneration();
		Encryption enc = new Encryption();
		String pt;
		String key;
//Ex Input : 1010000010
		System.out.print("Enter 10-bit Key : ");
		key = sc.next();
		System.out.println(" \n ");
		Print.msg("\n Key Generation ...\n");
		Print.msg("\n---------------------------------------\n");
		KG.GenerateKeys(key);
		Print.msg("\n---------------------------------------\n");
//Ex Input : hello
		System.out.print("Enter Plaintext : ");
		pt = sc.next();
		int[] bin_msg = char_binary(pt);
		int b;
		Print.msg("enter the number of bits to be taken at a time(CBC): ");
//must be 8,16,24,32...... to take a whole character.
//ex: 16
		b = sc.nextInt();
//Ex Input : 1010000010101010 ...for 16 bit
		Print.msg("enter the IV(CBC): ");
		boolean f = false;
		String IVstring = null;
		while (f == false) {
			IVstring = sc.next();
			if (IVstring.length() == b)
				f = true;
			else
				Print.msg("reenter the IV(must be the same length of the bits block):");

		}
//التشفیر فك في الثانیة و التشفیر في تستعمل سوف الاولى المصفوفة//
		int[] IV = new int[b];
		int[] IVoriginal = new int[b];
//int نوع من مصفوفة في سترینغ فكتور الانیشیل قیم نضع سوف//
		for (int i = 0; i < b; i++) {
			IV[i] = Character.getNumericValue(IVstring.charAt(i));
			IVoriginal[i] = Character.getNumericValue(IVstring.charAt(i));
		}
		int[] ct = new int[pt.length() * 8];
		int c = 0, ctcount = 0, blockcount = 1;
//الرسالة تقطیع مرات عدد لتحدید دوارة//
		for (int bl = 0; bl < bin_msg.length / b; bl++) {
			System.out.println(" \n ");
			System.out.println("plaintext block " + blockcount);
			blockcount++;
			int X_msg_count = 0;
			int IVcount = 0;
			int[] X_bin_msge = new int[b];

			int bitscount = 0, i = 0;
//فكتور الانیشیل و المقطعة الرسالة بین اور اكس عمل//
			while (bitscount < b && c < bin_msg.length) {
				X_bin_msge[bitscount] = IV[bitscount] ^ bin_msg[c];
				bitscount++;
				c++;
			}
//نعید و تشفیرھا و التشفیر لدالة لادخالھا بتات ثمان نأخذ سوف ھنا//
//البتات قطعة او البلوك انتھاء حد الى التالیة بتات الثمان باخذ العمل//

// اخرى قطعة ناخذ نرجع و المحددة
			while (i < b / 8 && ctcount < ct.length) {
				System.out.println("character " + (i + 1));
//take 8bits from the msg at a time and encrypt it(block cipher)
				int[] bin_msg8e = new int[8];
				int[] ct8e = new int[8];
				for (int j = 0; j < 8; j++) {
					bin_msg8e[j] = X_bin_msge[X_msg_count];
					X_msg_count++;
				}

				System.out.println(" \n ");
				ct8e = enc.encrypt(bin_msg8e, KG.k1, KG.k2);
				for (int j = 0; j < 8; j++)

				{
					ct[ctcount] = ct8e[j];
//اور الاكس لعملیة المشفر النص بال فكتور الانیشیل نبدل ھنا//

//الاصلي النص مع التالیة
					IV[IVcount] = ct8e[j];
					ctcount++;
					IVcount++;
				}

				Print.msg("\n---------------------------------------\n");
				i++;

			}
		}

		System.out.println(" \n Decryption ");

		Print.msg("\n---------------------------------------\n");
//ك المشفر النص بتقطیع اولا نقوم سوف ھنا ولكن التشفیر تشبھ التشفیر فك عملیة//

//الاصلي فكتور الانیشیل باستخدام نقوم ثم القطعة تشفیر بفك نقوم و الاصلي النص

//الاصلي النص على للحصول المشفر النص قطعة تشفیر فك من الناتج مع اور اكس لعمل//
		int ptcount = 0;
		c = 0;
		blockcount = 1;
		for (int bl = 0; bl < ct.length / b; bl++) {
			System.out.println(" \n ");
			System.out.println("ciphertext block " + blockcount);
			blockcount++;
			int X_msg_count = 0;
			int[] X_bin_msgd = new int[b];
			int bitscount = 0, i = 0;
			while (i < b / 8 && c < ct.length)

			{
				System.out.println("character " + (i + 1));
//take 8bits from the msg at a time and encrypt it(block cipher)
				int[] bin_msg8d = new int[8];
				int[] ct8d = new int[8];
				for (int j = 0; j < 8; j++) {

					bin_msg8d[j] = ct[c];
					c++;
				}

				System.out.println(" \n ");
				ct8d = enc.encrypt(bin_msg8d, KG.k2, KG.k1);
				for (int j = 0; j < 8; j++)

				{
					X_bin_msgd[X_msg_count] = ct8d[j];
					X_msg_count++;
				}

				Print.msg("\n---------------------------------------\n");
				i++;

			}

////فكتور الانیشیل و المقطعة الشفرة بین اور اكس عمل//
			while (bitscount < b && ptcount < bin_msg.length) {
				bin_msg[ptcount] = IVoriginal[bitscount] ^ X_bin_msgd[bitscount];
//اور الاكس لعملیة المشفر النص بال فكتور الانیشیل نبدل ھنا//

//الاصلي النص على للحصول تشفیرھا المفك المشفر النص قطعة مع التالیة
				IVoriginal[bitscount] = ct[ptcount];
				bitscount++;
				ptcount++;
			}
		}
		String plain_char = binary_char(bin_msg);
		System.out.println("plain as char::" + plain_char);
	}

	public static int[] char_binary(String p) {
		int count = 0;
		int[] bin_msg = new int[p.length() * 8];
		int y;
		for (int i = 0; i < p.length(); i++) {
			int[] temp2 = new int[8];
			int k = temp2.length - 1;
			int q = (int) p.charAt(i);
			System.out.println(q);
			while (q > 0) {
				y = q % 2;
				q = q / 2;
				temp2[k--] = y;
			}
			for (int j = 0; j < temp2.length; j++) {
				bin_msg[count++] = temp2[j];
			}
		}
		return bin_msg;
	}

	public static String binary_char(int[] bin_msg) {
		String binary = new String();
		int sum = 0, t = 0;
		String plain = new String();
//
		for (int i = 0; i <= bin_msg.length; i++)

		{
			if (t == 8) {
				sum = Integer.parseInt(binary, 2);
				char ciph = (char) sum;
				plain = plain + ciph;
				sum = 0;
				t = 0;
				binary = "";
				if (i != bin_msg.length)
					--i;
			} else {
				binary = binary + bin_msg[i];
				t++;
			}
		}
		return plain;
	}
}