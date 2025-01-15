package com.ll.simpleDb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql {
    private final StringBuilder sqlBuilder;
    private final SimpleDb simpleDb;

    public Sql(SimpleDb simpleDb) {
        this.simpleDb = simpleDb;
        this.sqlBuilder = new StringBuilder();
    }

    public Sql append(String sqlLine) {
        sqlBuilder.append(sqlLine);
        sqlBuilder.append(" ");
        return this; // Sql 객체(인스턴스 => 생성자)를 반환
    }

    public Sql append(String sqlLine, Object... args) {
        sqlBuilder.append(sqlLine);
        return this;
    }

    public long insert() {
        return 1;
    }

    public int update() {
        return 3;
    }

    public int delete() {
        return 2;
    }

    public List<Map<String, Object>> selectRows() {
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> row1 = new HashMap<>();

        row1.put("id", 1L);
        row1.put("title", "제목1");
        row1.put("body", "내용1");
        row1.put("createdDate", LocalDateTime.now());
        row1.put("modifiedDate", LocalDateTime.now());
        row1.put("isBlind", false);

        Map<String, Object> row2 = new HashMap<>();

        row2.put("id", 2L);
        row2.put("title", "제목2");
        row2.put("body", "내용2");
        row2.put("createdDate", LocalDateTime.now());
        row2.put("modifiedDate", LocalDateTime.now());
        row2.put("isBlind", false);

        Map<String, Object> row3 = new HashMap<>();

        row3.put("id", 3L);
        row3.put("title", "제목3");
        row3.put("body", "내용3");
        row3.put("createdDate", LocalDateTime.now());
        row3.put("modifiedDate", LocalDateTime.now());
        row3.put("isBlind", false);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        return rows;
    }

    public Map<String, Object> selectRow() {
        return simpleDb.selectRow(sqlBuilder.toString());
    }

    public LocalDateTime selectDatetime() {
        return simpleDb.selectDatetime(sqlBuilder.toString());
    }

    public Long selectLong() {
        //return 1L;
        return simpleDb.selectLong(sqlBuilder.toString());
    }

    public String selectString() {
        return simpleDb.selectString(sqlBuilder.toString());
    }

    public Boolean selectBoolean() {
        /*String sql = sqlBuilder.toString();

        if("SELECT 1 = 1".equals(sql)) {
            return true;
        } else if("SELECT 1 = 0".equals(sql)) {
            return false;
        }

        return false;*/

        return simpleDb.selectBoolean(sqlBuilder.toString());
    }
}