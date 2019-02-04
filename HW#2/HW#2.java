import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ManageStudents {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String input_file = args[0];

		try {
			File in_f = new File(input_file);
			if (!in_f.exists()) {
				System.out.println(input_file + " does not exist");
				System.exit(0);
			}

			RandomAccessFile din = new RandomAccessFile(in_f, "rw");
			RandomAccessFile dout = new RandomAccessFile(in_f, "rw");
			Student s = new Student();
			ArrayList<Student> student = new ArrayList<Student>();
			HashMap<String, Long> studentFilePointer = new HashMap<>();
			Long pointer;
			int size;
			int u_size;

			try {
				while (true) {
					try {

						if (din.length() == din.getFilePointer()) // if EOF
							break;

						pointer = din.getFilePointer();
						size = din.readInt();
						byte[] bytes = new byte[size];
						din.read(bytes); // din���� bytes ũ�⸸ŭ �о �迭�� ����
						String str = new String(bytes, "utf-8");
						Scanner buffer = new Scanner(str);
						buffer.useDelimiter("\\||\n");

						s.getOneStudent(buffer);
						student.add(s);
						studentFilePointer.put(s.number, pointer);
						// System.out.println("["+s.number+"]" +
						// "["+din.getFilePointer()+"]" + bytes.length);
						s = new Student();
					} catch (UnsupportedEncodingException e) {
						System.out.println("Unsupported Char-set");
					}

				}

				Student find = null;
				switch (args[1]) {

				case "p": // ��ȣ
					if (args[2].equals("0")) {
						for (Student p : student) {
							p.printOneStudent();
						}
						break;
					}
					int index = Integer.parseInt(args[2]) - 1;
					try {
						while (true) {
							if (student.get(Integer.parseInt(args[2]) - 1).gender == '*') {

								index = Integer.parseInt(args[2]) + 1;

								if (index == student.size()) {
									break;
								}
							break;	
							}
							

						}
						student.get(index).printOneStudent();
						break;

					} catch (IndexOutOfBoundsException e) {
						System.out.println("�ش��л��� �����ϴ�");
						// TODO: handle exception
					}

					break;
				case "s": // �й�
					for (Student p : student) {
						if (p.number.equals(args[2])) {
							if (p.gender == '*') {
								break;
							}
							find = p;
							find.printOneStudent();
						}
					}
					if (find == null) {
						System.out.println("�ش����");
					}
					break;
				case "n": // �̸�
					for (Student p : student) {
						if (p.name.equals(args[2])) {
							if (p.gender == '*') {

								break;
							}
							find = p;
							find.printOneStudent();
						}
					}
					if (find == null) {
						System.out.println("�ش����");
					}
					break;
				case "a":
					for (Student p : student) {
						if (p.address.contains(args[2])) {

							find = p;
							find.printOneStudent();
						}

					}
					if (find == null) {
						System.out.println("�ش����");
					}
					break;

				case "d": // ����
					// BufferedWriter bw = new BufferedWriter(new FileWriter(new
					// File(input_file)));

					if (args[2].equals("0")) {
						din.seek(0);
						int i = 1;
						for (Student p : student) {
							find = p;
							byte[] bytes = p.toString().getBytes("utf-8");
							if (p.gender == '*') {
								System.out.println("[" + i + "]" + "������ ���� " + bytes.length + " ����Ʈ");
							}
							i++;
							if (din.getFilePointer() == din.length()) {
								break;
							}
						}
					}

					for (Student p : student) {
						if (p.number.equals(args[2])) {
							find = p;
							din.seek(studentFilePointer.get(p.number));
							find.gender = '*';
							String d_txt = find.toString();

							Scanner d_buffer = new Scanner(d_txt);
							d_buffer.useDelimiter("\\||\n");
							while (d_buffer.hasNext()) {
								find.getOneStudent(d_buffer);
								din.seek(studentFilePointer.get(find.number));
								find.storeOneStudent(din);
							}
							break;
						}

					}
					if (find == null)
						System.out.println("�ش��й��� �����ϴ�");

					break;
				case "u":

					for (Student p : student) { // p �� txt_dat ���� �� �л�������
												// �ϳ���
												// ����
						if (p.number.equals(args[2])) {

							find = new Student();
							find.number = args[2];
							if (args[3].equals("-"))
								find.name = p.name;
							else
								find.name = args[3];

							if (args[4].equals("-"))
								find.gender = p.gender;
							else
								find.gender = args[4].charAt(0);
							if (args[5].equals("-"))
								find.phone_no = p.phone_no;
							else
								find.phone_no = args[5];
							if (args[6].equals("-"))
								find.address = p.address;
							else
								find.address = args[6];

							din.seek(studentFilePointer.get(p.number));
							String u_text = find.toString();

							size = din.readInt();

							/*
							 * System.out.println(size);
							 * System.out.println(u_size);
							 */

							byte[] o_bytes = new byte[size];
							byte[] u_bytes = u_text.getBytes("utf-8");
							u_size = u_bytes.length;

							din.read(o_bytes);
							String o_str = new String(o_bytes, "utf-8");
							Scanner o_buffer = new Scanner(o_str);
							o_buffer.useDelimiter("\\||\n");
							p.getOneStudent(o_buffer);

							Scanner u_buffer = new Scanner(u_text);
							u_buffer.useDelimiter("\\||\n");

							while (u_buffer.hasNext()) {
								if (u_size > size) {
									din.seek(studentFilePointer.get(p.number));
									p.gender = '*'; // ������ ����
									p.storeOneStudent(din);
									dout.seek(din.length());
									find.getOneStudent(u_buffer);
									find.storeOneStudent(dout);

								} else if (u_size < size) {
									dout.seek(studentFilePointer.get(p.number));
									int length = size - u_size;
									for (int i = 0; i < length; i++) {
										find.address += " ";
									}

									System.out.println(find.toString());
									u_text = find.toString(); // ���� �÷��� �ʱ�ȭ
									u_bytes = u_text.getBytes("utf-8"); // �ʱ�ȭ
									String str = new String(u_bytes, "utf-8");
									u_buffer = new Scanner(str);
									u_buffer.useDelimiter("\\||\n");
									find.getOneStudent(u_buffer);
									find.storeOneStudent(dout);
								} else {
									dout.seek(studentFilePointer.get(p.number));
									find.getOneStudent(u_buffer);
									find.storeOneStudent(dout);

								}
							}

							p.printOneStudent();
							break;
						}

					}

					if (find == null)
						System.out.println("err : �Է��Ͻ� �й��� �����ϴ�");

					break;
				default:
					System.out.println("p,s,n,a �߿��� �Է��� �ּ���");
				}

			} catch (IOException err) {
				err.printStackTrace();
				System.out.println("file I/O error..");
			}
			din.close();
		} catch (IOException e) {
			System.out.println("file error ...");
		}
	}
}
