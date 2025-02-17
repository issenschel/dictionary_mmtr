package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.entity.BaseDictionary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class DictionaryRepository {

    private final JdbcTemplate jdbcTemplate;


    public void createDictionaryTables(String dictionaryName) {
        DictionaryTableNames tableNames = getTableNames(dictionaryName);
        createKeysTable(tableNames.getKeysTableName());
        createValuesTable(tableNames.getValuesTableName(), tableNames.getKeysTableName());
    }


    private void createKeysTable(String keysTableName) {
        String createKeysTableSql = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "id SERIAL PRIMARY KEY, " +
                "key VARCHAR(255) NOT NULL UNIQUE)", keysTableName);
        jdbcTemplate.execute(createKeysTableSql);
    }


    private void createValuesTable(String valuesTableName, String keysTableName) {
        String createValuesTableSql = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "id SERIAL PRIMARY KEY, " +
                "key_id INT NOT NULL, " +
                "value VARCHAR(255) NOT NULL, " +
                "FOREIGN KEY (key_id) REFERENCES %s(id) ON DELETE CASCADE)", valuesTableName, keysTableName);
        jdbcTemplate.execute(createValuesTableSql);
    }

    public BaseDictionary addDictionaryEntry(String dictionaryName, String key, String value) {
        DictionaryTableNames tableNames = getTableNames(dictionaryName);

        String insertKeySql = String.format("INSERT INTO %s (key) VALUES (?) RETURNING id", tableNames.getKeysTableName());
        Integer keyId = jdbcTemplate.queryForObject(insertKeySql, Integer.class, key);

        String insertValueSql = String.format("INSERT INTO %s (key_id, value) VALUES (?, ?) RETURNING *", tableNames.getValuesTableName());

        return jdbcTemplate.queryForObject(insertValueSql, new BeanPropertyRowMapper<>(BaseDictionary.class), keyId, value);
    }

    public Optional<BaseDictionary> findDictionaryEntryByKey(String key, String sql) {
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToBaseDictionary(rs), key).stream().findFirst();
    }

    public Page<BaseDictionary> findAllDictionaryEntries(String dictionaryName, PageRequest pageRequest) {
        DictionaryTableNames tableNames = getTableNames(dictionaryName);

        String sql = String.format(
                "SELECT k.id AS key_id, k.key, ARRAY_AGG(v.value) AS values " +
                "FROM %s k " +
                "JOIN %s v ON k.id = v.key_id " +
                "GROUP BY k.id, k.key " +
                "LIMIT ? OFFSET ?", tableNames.getKeysTableName(), tableNames.getValuesTableName());

        List<BaseDictionary> content = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToBaseDictionary(rs), pageRequest.getPageSize(), pageRequest.getOffset());

        String countSql = String.format("SELECT COUNT(*) FROM %s", tableNames.getKeysTableName());
        long totalElements = jdbcTemplate.queryForObject(countSql, Long.class);

        return new PageImpl<>(content, pageRequest, totalElements);
    }

    public Stream<BaseDictionary> streamAllDictionaryEntries(String dictionaryName) {
        DictionaryTableNames tableNames = getTableNames(dictionaryName);

        String sql = String.format(
                "SELECT k.id AS key_id, k.key, ARRAY_AGG(v.value) AS values " +
                "FROM %s k " +
                "JOIN %s v ON k.id = v.key_id " +
                "GROUP BY k.id, k.key", tableNames.getKeysTableName(), tableNames.getValuesTableName());

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToBaseDictionary(rs)).stream();
    }

    public void deleteDictionaryEntry(String dictionaryName, BaseDictionary entity) {
        DictionaryTableNames tableNames = getTableNames(dictionaryName);
        String deleteKeySql = String.format("DELETE FROM %s WHERE id = ?", tableNames.getKeysTableName());
        jdbcTemplate.update(deleteKeySql, entity.getId());
    }

    private BaseDictionary mapRowToBaseDictionary(java.sql.ResultSet rs) throws java.sql.SQLException {
        BaseDictionary baseDictionary = new BaseDictionary();
        baseDictionary.setId(rs.getInt("key_id"));
        baseDictionary.setKey(rs.getString("key"));

        Array valuesArray = rs.getArray("values");
        String[] values = (String[]) valuesArray.getArray();
        baseDictionary.setValues(Arrays.asList(values));

        return baseDictionary;
    }

    private DictionaryTableNames getTableNames(String dictionaryName) {
        String keysTableName = dictionaryName + "_key";
        String valuesTableName = dictionaryName + "_value";
        return new DictionaryTableNames(keysTableName, valuesTableName);
    }
}

@Getter
class DictionaryTableNames {
    private final String keysTableName;
    private final String valuesTableName;

    public DictionaryTableNames(String keysTableName, String valuesTableName) {
        this.keysTableName = keysTableName;
        this.valuesTableName = valuesTableName;
    }

}