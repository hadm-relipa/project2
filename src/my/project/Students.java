package my.project;
// xxx

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import src.DBUtils;

public class Students {

	DBUtils db;

	private String ten, mssv, dob, khoavien;

	Students(DBUtils db) {
		this.db = db;
	}

	public boolean isThisDateValid(String dateToValidate, String dateFormat) {
		if (dateToValidate.isEmpty()) {
			// System.out.println("Khong duoc de trong!");
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);

		try {
			sdf.parse(dateToValidate);
		} catch (ParseException e) {
			System.out.println("Nhap sai dinh dang ngay!");
			return false;
		}

		return true;
	}

	public boolean isNameValid(String name) {
		if (name.isEmpty()) {
			System.out.println("Khong duoc de trong ten!");
			return false;
		}

		return true;
	}

	public boolean isIdValid(String id) {
		if (id.length() != 8) {
			System.out.println("MSSV phai co 8 so!");
			return false;
		}

		if (!id.matches("\\d+")) {
			System.out.println("MSSV chi gom cac chu so!");
			return false;
		}

		if (db.isExist("students", "mssv", id)) {
			System.out.println("MSSV da ton tai!");
			return false;
		}

		return true;
	}

	public boolean isSchoolsValid(String id) {
		if (id.isEmpty()) {
			return false;
		}

		if (!db.isIdentify("schools", "id", "'" + id + "'")) {
			System.out.println("Khoa/Vien nay khong ton tai!");
			return false;
		}

		return true;
	}

	public void giaoDienChucNang() {

		int mode;
		@SuppressWarnings("resource")
		Scanner myScan = new Scanner(System.in);

		// ham xu ly chuc nang
		do {
			System.out.println("\t*** Quan ly sinh vien ***");
			System.out.println("[1]. Xem danh sach sinh vien");
			System.out.println("[2]. Them sinh vien");
			System.out.println("[3]. Sua thong tin sinh vien");
			System.out.println("[4]. Xoa sinh vien");
			System.out.println("[5]. Tra cuu theo MSSV");
			System.out.println("[6]. Tra cuu theo ho ten sinh vien");
			System.out.println("[7]. Tro ve");

			System.out.print("Lua chon: ");

			try {
				mode = myScan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
				myScan.nextLine();
				mode = 0;
			}
			if(mode != 0) myScan.nextLine();

			switch (mode) {
			case 1:
				// hien thi danh sach sinh vien
				db.selectCol("students INNER JOIN schools ON khoavien = id", "mssv", "ten", "dob", "schools.ten");
				// db.innerJoinAndSelect("students", "schools", "khoavien", "id", "mssv", "ten",
				// "dob", "schools.ten");
				break;

			case 2:
				// them moi sinh vien
				// yeu cau du lieu phai hop le, khong duoc de trong

				// ten
				do {
					System.out.print("Ten: ");
					ten = myScan.nextLine();
				} while (!isNameValid(ten));

				// dob
				do {
					System.out.print("D.O.B (yyyy-mm-dd): ");
					dob = myScan.nextLine();
				} while (!isThisDateValid(dob, "yyyy-MM-dd"));

				// id
				do {
					System.out.print("MSSV: ");
					mssv = myScan.nextLine();
				} while (!isIdValid(mssv));

				// khoa/vien
				do {
					System.out.println("Khoa/Vien quan ly: ");
					db.selectCol("schools", "id", "ten", "diachi");
					System.out.print("Lua chon: ");
					khoavien = myScan.nextLine();
				} while (!isSchoolsValid(khoavien));

				// thuc hien truy van
				try {
					db.executeUpdate("INSERT INTO students VALUES (?, ?, ?, ?)",
							new String[] { ten, mssv, dob, khoavien });
				} catch (SQLException e) {
					// TODO: handle exception
					break;

				}
				System.out.println("Them thanh cong!");

				break;

			case 3:
				// sua thong tin sinh vien
				boolean isUpdate = false;
				String sql = "UPDATE students SET";

				// neu mssv nhap vao sai thi ket thuc ham
				do {
					System.out.print("MSSV can sua: ");
					mssv = myScan.nextLine();
				} while (mssv.isEmpty());

				if (!db.isUnique("students", "mssv", mssv)) {
					System.out.println("MSSV khong hop le!");
					break;
				}

				// hien thi thong tin sinh vien
				db.selectColLike("students INNER JOIN schools ON khoavien = id", "mssv", mssv, "mssv", "ten", "dob",
						"schools.ten");

				System.out.print("Ten moi: ");
				ten = myScan.nextLine();
				if (!ten.isEmpty()) {
					sql = sql.concat(" ten = " + "'" + ten + "'");
					isUpdate = true;
				}

				// nhap dung dinh dang dob de sua, de trong de bo qua
				System.out.print("D.O.B (yyyy-mm-dd): ");
				dob = myScan.nextLine();
				if (!dob.isEmpty()) {
					while (!isThisDateValid(dob, "yyyy-MM-dd")) {
						System.out.print("D.O.B (yyyy-mm-dd): ");
						dob = myScan.nextLine();

						if (dob.isEmpty())
							break;
					}
					// khi lay duoc dob moi...
					if (isThisDateValid(dob, "yyyy-mm-dd")) {
						if (!ten.isEmpty()) {
							sql = sql.concat(",");
						}
						sql = sql.concat(" dob = '" + dob + "'");
						isUpdate = true;
					}
				}

				// nhap dung dinh dang school de sua, de trong de bo qua
				System.out.println("Lua chon 1 trong cac so duoi day: ");
				db.selectCol("schools", "id", "ten", "diachi");
				System.out.print("Khoa/Vien moi: ");
				khoavien = myScan.nextLine();
				if (!khoavien.isEmpty()) {
					while (!isSchoolsValid(khoavien)) {
						System.out.println("Lua chon 1 trong cac so duoi day: ");
						db.selectCol("schools", "id", "ten", "diachi");
						System.out.print("Khoa/Vien moi: ");
						khoavien = myScan.nextLine();

						if (khoavien.isEmpty())
							break;
					}

					if (isSchoolsValid(khoavien)) {
						if (isThisDateValid(dob, "yyyy-mm-dd")) {
							sql = sql.concat(",");
						}
						sql = sql.concat(" khoavien = '" + khoavien + "'");
						isUpdate = true;
					}
				}

				sql = sql.concat(" WHERE mssv = ? ");

				// thuc hien truy van neu co thay doi
				if (isUpdate) {
					try {
						db.executeUpdate(sql, new String[] { mssv });

					} catch (Exception e) {
						// TODO: handle exception
						break;
					}
					System.out.println("Sua thanh cong!");
				}

				break;

			case 4:
				// xoa sinh vien theo ID
				String isDelete;

				do {
					System.out.print("MSSV can xoa: ");
					mssv = myScan.nextLine();
				} while (mssv.isEmpty());

				if (!db.isUnique("students", "mssv", mssv)) {
					System.out.println("MSSV khong hop le!");
					break;
				}

				db.selectColLike("students", "mssv", mssv, "mssv", "ten", "dob", "khoavien");

				do {
					System.out.print("Ban chac chan muon xoa chu? (y/n): ");
					isDelete = myScan.nextLine();
				} while (!isDelete.equals("y") && !isDelete.equals("n"));

				if (isDelete.equals("n")) {
					break;
				}

				try {
					db.executeUpdate("DELETE FROM students WHERE mssv = ?", new String[] { mssv });
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("MSSV nay khong ton tai!");
					break;
				}

				System.out.println("Xoa thanh cong!");
				break;

			case 5:
				System.out.print("MSSV can tim: ");
				mssv = myScan.nextLine();

				if (!db.isExist("students", "mssv", mssv)) {
					System.out.println("MSSV nay khong ton tai!");
					break;
				}

				db.selectColLike("students INNER JOIN schools ON khoavien = id", "mssv", mssv, "mssv", "ten", "dob",
						"schools.ten");

				break;

			case 6:
				System.out.print("Nhap ten sinh vien: ");
				ten = myScan.nextLine();
				if (!db.isExist("students", "ten", ten)) {
					System.out.println("Sinh vien nay khong ton tai!");
					break;
				}

				db.selectColLike("students INNER JOIN schools ON khoavien = id", "students.ten", ten, "mssv", "ten",
						"dob", "schools.ten");

				break;

			case 7:
				break;

			default:
				System.out.println("WARNING: Lua chon cac so tu 1 den 7!");
				break;
			}

		} while (mode != 7);

	}

}
