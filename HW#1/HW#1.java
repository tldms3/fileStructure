import java.io.*;
import java.util.Scanner;

import javax.crypto.ExemptionMechanismSpi;

public class ReadStudents {

	public static void main(String[] args) {
        
        
        
		/*
		 * if (args.length != 1) {
		 * System.out.println("USAGE: java ReadStudents INPUT_DAT_FILE");
		 * System.exit(0); }
		 */
		switch (args[1]) {
		case "p":
			Print_POSITION(args[0], args[1], Integer.parseInt(args[2]));
			break;
		case "s":
			Print_STDNUM(args[0], args[1], Integer.parseInt(args[2]));
			break;

		case "n":
			Print_NAME(args[0], args[1], args[2]);
			break;

		case "a":
			Print_KEY(args[0], args[1], args[2]);
			break;
		default:
			System.out.println("ERROR");
			break;
		}

	}

	public static void Print_POSITION(String file, String select, int position) {
		int count = 0;

		try {
			File in_f = new File(file);

			if (!in_f.exists()) {
				System.out.println(file + " does not exist");
				System.exit(0);
			}

			RandomAccessFile din = new RandomAccessFile(in_f, "r");
			Student s = new Student();
			int size;
			try {
				while (true) {
					try {
						if (din.length() == din.getFilePointer()) { // i
																	// //
																	// EOF
							break;
						}

						size = din.readInt();
						byte[] bytes = new byte[size];
						din.read(bytes);
						String str = new String(bytes, "utf-8");
						Scanner buffer = new Scanner(str);
						buffer.useDelimiter("\\||\n");
						count++;

						if (count == position) {
							s.getOneStudent(buffer);
							s.printOneStudent();
							break;
						}

					} catch (UnsupportedEncodingException e) {
						System.out.println("Unsupported Char-set");
					}
				}
				if (position > count || position < 1)
					System.out.println("범위 내의 학생이 존재하지않음");
			} catch (IOException err) {
				System.out.println("file I/O error..");
			}

			din.close();
		} catch (IOException e) {
			System.out.println("file error ...");
		}
	}

	public static void Print_STDNUM(String file, String select, int std_num) {
		String student;
		try {
			File in_f = new File(file);

			if (!in_f.exists()) {
				System.out.println(file + " does not exist");
				System.exit(0);
			}

			RandomAccessFile din = new RandomAccessFile(in_f, "r");
			Student s = new Student();
			int size;
			String str = null;
			try {
				while (true) {
					try {
						if (din.length() == din.getFilePointer()) { // i
																	// //
																	// EOF
							break;
						}

						size = din.readInt();
						byte[] bytes = new byte[size];
						din.read(bytes);
						str = new String(bytes, "utf-8");
						Scanner buffer = new Scanner(str);
						buffer.useDelimiter("\\||\n");
						// student = s.number;
						if (str.contains(Integer.toString(std_num))) {
							s.getOneStudent(buffer);
							s.printOneStudent();
							break;
						}
					} catch (UnsupportedEncodingException e) {
						System.out.println("Unsupported Char-set");
					}
				}

				if (!str.contains(Integer.toString(std_num))) {
					System.out.println("존재하는 학생이 없습니다.");
				}

			} catch (IOException err) {
				System.out.println("file I/O error..");
			}

			din.close();
		} catch (IOException e) {
			System.out.println("file error ...");
		}
	}

	public static void Print_NAME(String file, String select, String std_name) {
		try {
			File in_f = new File(file);
			if (!in_f.exists()) {
				System.out.println(file + " does not exist");
				System.exit(0);
			}

			RandomAccessFile din = new RandomAccessFile(in_f, "r");
			Student s = new Student();
			int size;
			String str = null;
			try {
				while (true) {
					try {
						if (din.length() == din.getFilePointer()) // if EOF
							break;

						size = din.readInt();
						byte[] bytes = new byte[size];
						din.read(bytes);
						str = new String(bytes, "utf-8");
						Scanner buffer = new Scanner(str);
						buffer.useDelimiter("\\||\n");

						if (str.contains(std_name) && std_name.length() == 3) {
							s.getOneStudent(buffer);
							s.printOneStudent();
						}
						continue;
					} catch (UnsupportedEncodingException e) {
						System.out.println("Unsupported Char-set");
					}
				}
				if (!str.contains(std_name) && std_name.length() <= 2) {
					System.out.println("존재하는 학생이 없습니다.");
				} else if (!str.contains(std_name) && std_name.length() > 3)
					System.out.println("존재하는 학생이 없습니다.");

			} catch (IOException err) {
				System.out.println("file I/O error..");
			}

			din.close();
		} catch (IOException e) {
			System.out.println("file error ...");
		}
	}

	public static void Print_KEY(String file, String select, String address) {
		try {
			File in_f = new File(file);
			if (!in_f.exists()) {
				System.out.println(file + " does not exist");
				System.exit(0);
			}

			RandomAccessFile din = new RandomAccessFile(in_f, "r");
			Student s = new Student();
			int size;
			String str = null;
			try {
				while (true) {
					try {
						if (din.length() == din.getFilePointer()) // if EOF
							break;

						size = din.readInt();
						byte[] bytes = new byte[size];
						din.read(bytes);
						str = new String(bytes, "utf-8");
						Scanner buffer = new Scanner(str);
						buffer.useDelimiter("\\||\n");

						if (str.contains(address)) {
							s.getOneStudent(buffer);
							s.printOneStudent();
						}
						continue;
					} catch (UnsupportedEncodingException e) {
						System.out.println("Unsupported Char-set");
					}
				}
				/*if (!str.contains(address) ) {
					System.out.println("존재하는 학생이 없습니다.");
				}*/
			} catch (IOException err) {
				System.out.println("file I/O error..");
			}

			din.close();
		} catch (IOException e) {
			System.out.println("file error ...");
		}
	}
}