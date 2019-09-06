package my.project;

import java.sql.SQLException;
import java.util.Scanner;

import src.DBUtils;

public class Schools {
	DBUtils db;
	String id, ten, diachi;
	
	Schools(DBUtils db){
		this.db = db;
	}
	
	public boolean isIdValid(String id) {
		if(id.isEmpty()) {
			System.out.println("ID khoa/vien khong hop le!");
			return false;
		}
		
		if(db.isIdentify("schools", "id", "'" + id + "'")) {
			System.out.println("Khoa/Vien nay da ton tai!");
			return false;
		}
		
		return true;
	}
	
	public boolean isNameValid(String ten) {
		if(ten.isEmpty()) {
			System.out.println("Ten khoa/vien khong hop le!");
			return false;
		}
		
		if(db.isIdentify("schools", "ten", "'" + ten + "'")) {
			System.out.println("Khoa/Vien nay da ton tai!");
			return false;
		}
		
		return true;
	}
	
	public boolean isAddressValid(String diachi) {
		if(diachi.isEmpty()) {
			System.out.println("Khong duoc de trong dia chi!");
			return false;
		}
		
		return true;
	}

	public void giaoDienChucNang() {
		@SuppressWarnings("resource")
		Scanner myScan = new Scanner(System.in);
		int mode;
		
		do {
			System.out.println("\t***");
			System.out.println("[1]. Xem danh sach khoa/vien");
			System.out.println("[2]. Them khoa/vien");
			System.out.println("[3]. Sua khoa/vien");
			System.out.println("[4]. Xoa khoa/vien");
			System.out.println("[5]. Tim kiem khoa/vien");
			System.out.println("[6]. Tro ve");
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
				db.selectCol("schools", "id", "ten", "diachi");
				break;
				
			case 2:
				// them 
				do {
					System.out.print("ID khoa/vien: ");
					id = myScan.nextLine();
				} while (!isIdValid(id));
				
				do {
					System.out.print("Ten khoa/vien: ");
					ten = myScan.nextLine();
				} while (!isNameValid(ten));
				
				do {
					System.out.print("Dia chi: ");
					diachi = myScan.nextLine();
				} while(!isAddressValid(diachi));
				
				try {
					db.executeUpdate("INSERT INTO schools VALUES (?, ?, ?)", new String[] {id, ten, diachi});
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Xay ra loi khi them!");
				}
				
				System.out.println("Them thanh cong!");
				
				break;
				
			case 3:
				// sua
				boolean isUpdate = false;
				String sql = "UPDATE schools SET";
				
				do {
					System.out.print("ID khoa/vien: ");
					id = myScan.nextLine();
				} while (id.isEmpty());
				
				if(!db.isUnique("schools", "id", "'" + id + "'")) {
					System.out.println("Khong ton tai ID nay!");
					break;
				}
				
				db.selectColLike("schools", "id", id, "id", "ten", "diachi");
				
				System.out.print("Ten khoa/vien: ");
				ten = myScan.nextLine();
				if(!ten.isEmpty()) {
					while(!isNameValid(ten)) {
						System.out.print("Ten khoa/vien: ");
						ten = myScan.nextLine();
						
						if(ten.isEmpty()) break;
					}
					
					if(!ten.isEmpty()) {
						sql = sql.concat(" ten = '" + ten + "'");
						isUpdate = true;
					}
				}
				
				System.out.print("Dia chi: ");
				diachi = myScan.nextLine();
				if(!diachi.isEmpty()) {
					if(isUpdate) sql = sql.concat(",");
					sql = sql.concat(" diachi = '" + diachi + "'");
				}
				
				sql = sql.concat(" WHERE id = ?");
				
				try {
					db.executeUpdate(sql, new String[] {id});
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("Loi Update!");
					break;
				}
				System.out.println("Sua thanh cong!");
				
				break;
				
			case 4:
				// xoa
				String isDelete;
				do {
					System.out.print("ID khoa/vien: ");
					id = myScan.nextLine();
				} while (id.isEmpty());
				
				if(!db.isIdentify("schools", "id", "'" + id + "'")) {
					System.out.println("Khong ton tai ID nay!");
					break;
				}
				
				db.selectColLike("schools", "id", id, "id", "ten", "diachi");
				
				do {
					System.out.print("Ban chac chan muon xoa chu? (y/n): ");
					isDelete = myScan.nextLine();
				} while (!isDelete.equals("y") && !isDelete.equals("n"));
				
				if(isDelete.equals("n")) {
					break;
				}
				
				try {
					db.executeUpdate("DELETE FROM schools WHERE id = ?", new String[] {id});
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Khong ton tai ID nay!");
					break;
				}
				System.out.println("Xoa thanh cong!");
				break;
				
			case 5:
				System.out.print("Nhap ten khoa vien can tim kiem: ");
				ten = myScan.nextLine();
				
				if(!db.isExist("schools", "ten", ten)) {
					System.out.println("Khong co ket qua tim kiem!");
					break;
				}
				
				db.selectColLike("schools", "ten", ten, "id", "ten", "diachi");
				break;
				
			case 6:
				break;
				
			default:
				System.out.println("WARNING: Lua chon cac so tu 1 den 6!");
				break;
			}
		} while (mode != 6);
	}

}
