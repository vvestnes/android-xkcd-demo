package com.example.xkcd.repository.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xkcd.repository.info.InfoEntity;

import java.util.List;

@Dao
public interface InfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(InfoEntity item);

    @Update
    void update(InfoEntity item);

    @Delete
    void delete(InfoEntity item);

    @Query("SELECT * FROM info_table WHERE num=:num")
    LiveData<InfoEntity> getByNum(int num);

    @Query("SELECT * FROM info_table WHERE num=:num")
    LiveData<InfoEntity> getByNumAsLiveData(int num);

    @Query("SELECT num FROM info_table WHERE favorite = 1 ORDER BY num DESC")
    LiveData<List<Integer>> getAllNums();

    @Query("SELECT * FROM info_table ORDER BY num DESC LIMIT 1")
    LiveData<InfoEntity> getCurrent();

}
