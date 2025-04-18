package com.example.waterbalance.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.waterbalance.data.WaterRepository
import com.example.waterbalance.data.room.AppDatabase
import com.example.waterbalance.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Добавляем новый столбец с дефолтным значением
        database.execSQL("ALTER TABLE daily_water_entry ADD COLUMN glassSize INTEGER NOT NULL DEFAULT 250")
    }
}

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "your_database_name"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    single { get<AppDatabase>().waterDao() }
    single { WaterRepository(get()) }
    viewModel { MainViewModel(get()) }
}