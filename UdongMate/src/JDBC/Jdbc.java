package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;

public class Jdbc {

   public static Connection get() {
      Connection con = null;
      
      try {
         String id = "당시의 계정";
         String pw = "당신의 비밀번호";
         String url = "jdbc:mysql://localhost:3306/당신의 데이터베이스 명";
         
         Class.forName("com.mysql.cj.jdbc.Driver");
         // Class 클래스로 mysql 드라이버를 로딩하는 코드 -> DriverManager 등록됨
         
         con = DriverManager.getConnection(url, id, pw);
         // Connection 객체를 얻음
         // con은 실제로 데이터베이스와 연결하여 작업을 수행할 수 있는 통로를 작용하는
         // 중요한 객체 변수로 사용된다!!!
         
         System.out.println("연결성공");
      }catch(Exception e) {
         System.out.println("연결실패");
      }
      return con;
   }
}
