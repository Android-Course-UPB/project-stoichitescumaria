package com.example.favoritebooksapp;
import android.app.Application;
import com.example.inventory.data.AppDataContainer

class FavoriteBooksApplication : Application() {
    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}