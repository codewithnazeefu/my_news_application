package com.codewithnazeefo.mynewsapp.db

import androidx.room.TypeConverter

class Converter {
    @TypeConverter
    fun fromSource (source: com.codewithnazeefo.mynewsapp.Model.Source):String{

        return source.name
    }
    @TypeConverter
    fun toSource(name :String) :com.codewithnazeefo.mynewsapp.Model.Source{

        return com.codewithnazeefo.mynewsapp.Model.Source(name,name)
    }

}