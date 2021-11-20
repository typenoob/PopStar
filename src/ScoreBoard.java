import javax.swing.*;
import java.sql.*;

public class ScoreBoard extends JDialog {
    private JTable table;
    private JScrollPane scrollPane;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://101.132.79.142:3306/Ranking?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "password";
    private ResultSet rs;
    private String sql;
    private static Connection conn = null;
    private static Statement stmt = null;

    ScoreBoard(JFrame jf) {
        super(jf);
        String columns[] = { "name", "score", "date" };
        Object cells[][] = new Object[10][3];
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "SELECT * FROM rec" + " ORDER BY score desc";
            rs = stmt.executeQuery(sql);
            int count = 0;
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                cells[count][0] = rs.getString("name");
                cells[count][1] = rs.getInt("score");
                cells[count][2] = rs.getDate("his");
                count++;
                if (count == 10)
                    break;
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        table = new JTable(cells, columns);
        scrollPane = new JScrollPane(table);
        this.add(scrollPane);
        this.setTitle("高分榜");
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
