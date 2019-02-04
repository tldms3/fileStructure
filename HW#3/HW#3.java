import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
			int u_size; // update size

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
							if (p.gender != '*') {
								p.printOneStudent();
							}
						}
						break;
					}
					int index = 0;
					int count = 0;
					try {
						while (true) {
							if (student.get(index).gender != '*') {
								count++;
							}
							if (Integer.parseInt(args[2]) == count) {
								student.get(index).printOneStudent();
								break;
							}
							index++;
						}
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

					for (Student p : student) { // p �� txt_dat ���� �� �л������� �ϳ���
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

							byte[] o_bytes = new byte[size];
							byte[] u_bytes = u_text.getBytes("utf-8");
							u_size = u_bytes.length;

							din.read(o_bytes);
							String o_str = new String(o_bytes, "utf-8"); //
							Scanner o_buffer = new Scanner(o_str);//
							o_buffer.useDelimiter("\\||\n");
							p.getOneStudent(o_buffer);//

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
									dout.seek(studentFilePointer.get(p.number)); // ���
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
				case "i":

					String text_out_file = args[2]; // insert ���� text����
					try {
						File text_out_f = new File(text_out_file); // tex����
						if (!text_out_f.exists()) {
							System.out.println(text_out_file + " does not exist");
							System.exit(0);
						}

						BufferedReader br = new BufferedReader(new FileReader(text_out_f));
						RandomAccessFile textout = new RandomAccessFile(text_out_f, "rw");

						Student text_s = new Student();

						ArrayList<Student> textStudent = new ArrayList<Student>();
						String line;

						try {
							while ((line = br.readLine()) != null) {

								textout.read(line.getBytes());
								line.split("\\s\n");
								Scanner textbuffer = new Scanner(line);
								text_s.getOneStudent(textbuffer);

								textStudent.add(text_s);
								text_s = new Student();

							}
							System.out.println(textStudent.size());

						} catch (IOException err) {
							err.printStackTrace();
							System.out.println("file I/O error..");
						}

						for (Student t : textStudent) { // text���� �л�

							for (Student p : student) {// txt_dat ���� �л�

								din.seek(studentFilePointer.get(p.number));

								if (t.number.equals(p.number) && p.gender != '*') {

									din.seek(studentFilePointer.get(p.number));
									String u_text = t.toString();
									byte[] u_bytes = u_text.getBytes("utf-8");
									u_size = u_bytes.length;
									int o_size = din.readInt();
									byte[] o_bytes = new byte[o_size];
									din.read(o_bytes);
									Scanner u_buffer = new Scanner(u_text);
									u_buffer.useDelimiter("\\||\n");

									while (u_buffer.hasNext()) {

										if (u_size > o_size) {
											din.seek(studentFilePointer.get(p.number));
											p.gender = '*'; // ������ ����
											p.storeOneStudent(din);
											dout.seek(din.length());

											t.getOneStudent(u_buffer);
											t.storeOneStudent(dout);

										} else if (u_size < o_size) {
											dout.seek(studentFilePointer.get(p.number));
											int length = o_size - u_size;
											for (int i = 0; i < length; i++) {
												t.address += " ";
											}

											u_text = t.toString(); // ����

											u_buffer = new Scanner(u_text);
											u_buffer.useDelimiter("\\||\n");
											t.getOneStudent(u_buffer);
											t.storeOneStudent(dout);

										} else { // ũ�� �������
											dout.seek(studentFilePointer.get(p.number)); // ���
											t.getOneStudent(u_buffer);
											t.storeOneStudent(dout);

										}
									}
									break;

								}

							} // dat ���� �л�

							if (din.getFilePointer() == studentFilePointer
									.get(student.get(student.size() - 1).number)) {

								for (Student q : student) {

									din.seek(studentFilePointer.get(q.number));

									if (q.gender == '*') {
										din.seek(studentFilePointer.get(q.number));

										String u_text = t.toString();

										int o_size = din.readInt();

										System.out.println(o_size);
										byte[] o_bytes = new byte[o_size];
										byte[] u_bytes = u_text.getBytes("utf-8");
										u_size = u_bytes.length;
										din.read(o_bytes);
										Scanner u_buffer = new Scanner(u_text);
										u_buffer.useDelimiter("\\||\n");
										while (u_buffer.hasNext()) {
											if (u_size > o_size) {

												dout.seek(din.length());
												t.getOneStudent(u_buffer);
												t.storeOneStudent(dout);

											} else if (u_size < o_size) {
												dout.seek(studentFilePointer.get(q.number));
												int length = o_size - u_size;
												for (int i = 0; i < length; i++) {
													t.address += " ";
												}

												u_text = t.toString(); // ����
												u_buffer = new Scanner(u_text); // �÷���
												u_buffer.useDelimiter("\\||\n");

												t.getOneStudent(u_buffer);
												t.storeOneStudent(dout);

											} else { // ũ�� �������
												dout.seek(studentFilePointer.get(q.number)); // ���
												t.getOneStudent(u_buffer);
												t.storeOneStudent(dout);
											}
										}
										break;
									}
								}

								if (din.getFilePointer() == studentFilePointer
										.get(student.get(student.size() - 1).number)) {

									String u_text = t.toString();

									int o_size = din.readInt();

									byte[] o_bytes = new byte[o_size];
									byte[] u_bytes = u_text.getBytes("utf-8");
									u_size = u_bytes.length;
									din.read(o_bytes);
									Scanner u_buffer = new Scanner(u_text);
									u_buffer.useDelimiter("\\||\n");
									while (u_buffer.hasNext()) {
										dout.seek(din.length());
										System.out.println("�������� ����");
										t.getOneStudent(u_buffer);
										t.storeOneStudent(dout);
									}

								}

							}

						} // txt ���� �л�

						br.close();
						// text_out.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				default:
					System.out.println("p,s,n,a �߿��� �Է��� �ּ���");
				}

			} catch (IOException err) {
				err.printStackTrace();
				System.out.println("file I/O error..");
			}
			din.close();
			dout.close();

		} catch (IOException e) {
			System.out.println("file error ...");
		}
	}
}
