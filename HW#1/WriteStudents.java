import java.io.*;
import java.util.Scanner;

public class WriteStudents {
	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {

/*			if (args[i].length() != 1) {
				System.out.println("USAGE: java WriteStudents INPUT_TEXT_FILE");
				System.exit(0);
			}*/

			String input_file = args[i];

			try {
				File in_f = new File(input_file);
				if (!in_f.exists()) {
					System.out.println(input_file + " does not exist");
					System.exit(0);
				}

				FileReader fr = new FileReader(in_f);
				BufferedReader br = new BufferedReader(fr);
				Scanner in = new Scanner(br);

				RandomAccessFile dout = new RandomAccessFile(input_file + "_dat", "rw");
				Student s = new Student();

				while (in.hasNext()) {
					s.getOneStudent(in);
					try {
						byte[] bytes = s.toString().getBytes("utf-8");

						System.out.print("[" + dout.getFilePointer() + "," + bytes.length + "] ");
						s.printOneStudent();
						dout.writeInt(bytes.length);
						dout.write(bytes);
					} catch (UnsupportedEncodingException e) {
						System.out.println("Unsupported charset");
					}
				}
				br.close();
				dout.close();
			} catch (IOException e) {
				System.out.println("file error ...");
			}
		}
	}
}

class Student {
	public String number;
	public String name;
	public char gender;
	public String phone_no;
	public String address;

	public void getOneStudent(Scanner in) {
		number = in.next();
		name = in.next();
		gender = in.next().charAt(0);
		phone_no = in.next();
		address = in.next();

		in.nextLine();
	}

	public void printOneStudent() {
		System.out.println("[" + number + "] " + name + "(" + ((gender == 'm') ? "남자" : "여자") + ") 전화번호: " + phone_no
				+ ", 주소: " + address);
	}

	public String toString() {

		return number + "|" + name + "|" + gender + "|" + phone_no + "|" + address + "|";
	}
}