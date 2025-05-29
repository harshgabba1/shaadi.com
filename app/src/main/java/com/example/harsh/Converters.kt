package com.example.harsh

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromName(value: Name): String = gson.toJson(value)

    @TypeConverter
    fun toName(value: String): Name = gson.fromJson(value, object : TypeToken<Name>() {}.type)

    @TypeConverter
    fun fromLocation(value: Location): String = gson.toJson(value)

    @TypeConverter
    fun toLocation(value: String): Location = gson.fromJson(value, object : TypeToken<Location>() {}.type)

    @TypeConverter
    fun fromLogin(value: Login): String = gson.toJson(value)

    @TypeConverter
    fun toLogin(value: String): Login = gson.fromJson(value, object : TypeToken<Login>() {}.type)

    @TypeConverter
    fun fromDob(value: Dob): String = gson.toJson(value)

    @TypeConverter
    fun toDob(value: String): Dob = gson.fromJson(value, object : TypeToken<Dob>() {}.type)

    @TypeConverter
    fun fromRegistered(value: Registered): String = gson.toJson(value)

    @TypeConverter
    fun toRegistered(value: String): Registered = gson.fromJson(value, object : TypeToken<Registered>() {}.type)

    @TypeConverter
    fun fromId(value: Id): String = gson.toJson(value)

    @TypeConverter
    fun toId(value: String): Id = gson.fromJson(value, object : TypeToken<Id>() {}.type)

    @TypeConverter
    fun fromPicture(value: Picture): String = gson.toJson(value)

    @TypeConverter
    fun toPicture(value: String): Picture = gson.fromJson(value, object : TypeToken<Picture>() {}.type)
}
