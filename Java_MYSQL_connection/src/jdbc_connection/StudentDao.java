package jdbc_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
	private Connection conn; //DB커넥션(연결)객체
	private static final String USERNAME = "root";	//DB 접속시 ID 
	private static final String PASSWORD = "12341234";	//DB 접속시 패스워드 
//	private static final String URL = "jdbc:mysql://localhost/sqldb"; //DB접속 경로(스키마=데이터베이스명)설정
	private static final String URL ="jdbc:mysql://localhost/sqldb?characterEncoding=UTF-8&serverTimezone=UTC";
	
	public StudentDao() {
		// connection객체를 생성해서 디비에 연결해줌..
		try {
//			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
			System.out.println("드라이버 로딩 성공 !!");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("드라이버 로드 실패");
		}
	}
	
	public void insertStudent(Student student) {
		String sql = "insert into student value(?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, student.getId());
			pstmt.setString(2, student.getName());
			pstmt.setString(3, student.getGrade());
			pstmt.executeUpdate();
			System.out.println("Student 데이터 삽입 성공");
		}catch(SQLException e) {
			System.out.println("Student 데이터 삽입 실패");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null && !pstmt.isClosed())
					pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateStudent(Student student) {
		String sql = "update student set name=?, grade=? where id =?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, student.getName());
			pstmt.setString(2, student.getGrade());
			pstmt.setString(3, student.getId());
			pstmt.executeUpdate();
			System.out.println("Student 데이터 업데이트 성공");
		}catch(SQLException e) {
			System.out.println("Student 데이터 업데이트 실패");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null && !pstmt.isClosed())
					pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteStudent(Student student) {
		String sql = "delete from student where id=?;";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, student.getId());
			ps.executeUpdate();
			System.out.println("삭제 성공");
		}catch(SQLException e) {
			System.out.println("삭제 실패");
		}
	}
	
	public Student selectOne(String id) {
		String sql = "select * from student where id = ?;";
		PreparedStatement ps = null;
		Student resultStu = new Student();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			if(resultSet.next()) {
				resultStu.setId(resultSet.getString("id"));
				resultStu.setName(resultSet.getString("name"));
				resultStu.setGrade(resultSet.getString("grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultStu;
	}
	
	public List<Student> selectAll() {
		String sql = "select * from student;";
		PreparedStatement ps = null;
		List<Student> list = new ArrayList<Student>();
		try {
			ps = conn.prepareStatement(sql);
			ResultSet re=ps.executeQuery();
			while(re.next()) {
				Student s = new Student();
				s.setId(re.getString("id"));
				s.setName(re.getString("name"));
				s.setGrade(re.getString("grade"));
				list.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(ps != null && !ps.isClosed())
					ps.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
