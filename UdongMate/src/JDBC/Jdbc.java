package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;

public class Jdbc {

   public static Connection get() {
      Connection con = null;
      
      try {
         String id = "����� ����";
         String pw = "����� ��й�ȣ";
         String url = "jdbc:mysql://localhost:3306/����� �����ͺ��̽� ��";
         
         Class.forName("com.mysql.cj.jdbc.Driver");
         // Class Ŭ������ mysql ����̹��� �ε��ϴ� �ڵ� -> DriverManager ��ϵ�
         
         con = DriverManager.getConnection(url, id, pw);
         // Connection ��ü�� ����
         // con�� ������ �����ͺ��̽��� �����Ͽ� �۾��� ������ �� �ִ� ��θ� �ۿ��ϴ�
         // �߿��� ��ü ������ ���ȴ�!!!
         
         System.out.println("���Ἲ��");
      }catch(Exception e) {
         System.out.println("�������");
      }
      return con;
   }
}
