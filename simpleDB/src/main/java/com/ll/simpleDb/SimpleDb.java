package com.ll.simpleDb;

import lombok.Setter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleDb {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private Connection connection;
    @Setter
    private boolean devMode = false;

    // 생성자: 데이터베이스 연결 정보 초기화
    public SimpleDb(String host, String user, String password, String dbName) {
        this.dbUrl = "jdbc:mysql://" + host + ":3307/" + dbName; // JDBC URL
        this.dbUser = user;                                    // 사용자 이름
        this.dbPassword = password;                            // 비밀번호

        // 연결 초기화
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            if (devMode) {
                System.out.println("데이터베이스에 성공적으로 연결되었습니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 실패: " + e.getMessage());
        }
    }

    public String selectString(String sql) {
        return _run(sql, String.class);
    }

    public Long selectLong(String sql) {
        return _run(sql, Long.class);
    }

    public boolean selectBoolean(String sql) {
        System.out.println("sql : " + sql);
        // return (boolean) _run(sql, 0);
        return _run(sql, Boolean.class);
    }

    public LocalDateTime selectDatetime(String sql) {
        return _run(sql, LocalDateTime.class);
    }

    public void run(String sql, Object... params) {
        _run(sql, Integer.class, params);
    }

    public Sql genSql() {
        return new Sql(this);
    }

    // SQL 실행 (PreparedStatement와 파라미터)
    public <T> T _run(String sql, Class<T> cls, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (sql.startsWith("SELECT")) {
                ResultSet rs = stmt.executeQuery(); // 실제 반영된 로우 수. insert, update, delete
                return parseResultSet(rs, cls);
                /*rs.next(); // 행을 처음부터 차례로 탐색하며 데이터를 가져와야 하는데 이 코드 때문에 힘듦 -> 분리 진행
                *//*if (type == Boolean.class) return rs.getBoolean(1);
                else if (type == Boolean.class) return rs.getString(1);*//*
                *//*if(type == Boolean.class) return (T) (Boolean)rs.getBoolean(1);
                else if(type == String.class) return (T) rs.getString(1);*//*

                if (cls == Boolean.class) return cls.cast((rs.getBoolean(1)));
                else if (cls == String.class) return cls.cast(rs.getString(1));
                else if (cls == Long.class) return cls.cast(rs.getLong(1));
                else if (cls == LocalDateTime.class)
                    return cls.cast(rs.getTimestamp(1).toLocalDateTime());
                else if (cls == Map.class) {
                    Map<String, Object> row = new HashMap<>();

                    *//*row.put("id", 1L);
                    row.put("title", "제목1");
                    row.put("body", "내용1");
                    row.put("createdDate", LocalDateTime.now());
                    row.put("modifiedDate", LocalDateTime.now());
                    row.put("isBlind", false);*//*

                    // TODO : 일반화 과정 : DB(컬럼명)이 변할 때 마다 코드 수정이 필요하지 않게 하기 위함
                    ResultSetMetaData metaData = rs.getMetaData(); // 결과 집합(ResultSet)의 열(컬럼)에 대한 정보를 제공하는 객체
                                                                   // 컬럼의 수, 이름, 타입 등을 알 수 있음
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String cname = metaData.getColumnName(i);
                        row.put(cname, rs.getObject(i));
                    }

                    return cls.cast(row);
                }*/
            }
            setParams(stmt, params); // PreparedStatement에 파라미터 바인딩

            return cls.cast(stmt.executeUpdate()); // 실제 반영된 로우 수. insert, update, delete

        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 실패: " + e.getMessage());
        }
    }

    private <T> T parseResultSet(ResultSet rs, Class<T> cls) throws SQLException {
        if(cls == Boolean.class) {
            rs.next();
            return cls.cast((rs.getBoolean(1)));
        }
        else if(cls == String.class){
            rs.next();
            return cls.cast(rs.getString(1));
        }
        else if(cls == Long.class){
            rs.next();
            return cls.cast(rs.getLong(1));
        }
        else if(cls == LocalDateTime.class){
            rs.next();
            return cls.cast(rs.getTimestamp(1).toLocalDateTime());
        }
        else if(cls == Map.class) {
            rs.next();
            return cls.cast(rsRowToMap(rs));
        } else if(cls == List.class) {
            List<Map<String, Object>> rows = new ArrayList<>();
            while(rs.next()) {
                rows.add(rsRowToMap(rs));
            }
            return cls.cast(rows);
        }
        throw new RuntimeException();
    }
    private Map<String, Object> rsRowToMap(ResultSet rs) throws SQLException {
        Map<String, Object> row = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String cname = metaData.getColumnName(i);
            row.put(cname, rs.getObject(i)); // rs.getObject(i) : i번째 컬럼의 값
        }
        return row;
    }

    // PreparedStatement에 파라미터 바인딩
    private void setParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]); // '?' 위치에 값 설정
        }
    }

    /*// 데이터베이스 연결 종료
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                if (devMode) {
                    System.out.println("데이터베이스 연결 종료.");
                }
            } catch (SQLException e) {
                throw new RuntimeException("데이터베이스 연결 종료 실패: " + e.getMessage());
            }
        }
    }*/

    public Map<String, Object> selectRow(String sql) {
        return _run(sql, Map.class);
    }
}