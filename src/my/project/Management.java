package my.project;

import java.util.Scanner;

import src.DBUtils;

public class Management {
	public static void main(String[] args) {
		DBUtils db = new DBUtils();
		// thong bao trang thai
		if(db.init()) {
			System.out.println("\nKet noi thanh cong toi CSDL!\n");
		};
		
		Scanner myScan = new Scanner(System.in);
		Students myStudent = new Students(db);
		Schools mySchool= new Schools(db);
		int mode;
		
		do {
			System.out.println("\t***");
			System.out.println("[1]. Quan ly sinh vien");
			System.out.println("[2]. Quan ly khoa vien");
			System.out.println("[3]. Thoat");
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
				myStudent.giaoDienChucNang();
				break;
				
			case 2:
				mySchool.giaoDienChucNang();
				break;
				
			case 3:
				System.out.println("Bye!");
				break;

			default:
				System.out.println("WARNING: Lua chon so tu 1 den 3!");
				break;
			}
		} while (mode != 3);
		
		myScan.close();
	}
}
