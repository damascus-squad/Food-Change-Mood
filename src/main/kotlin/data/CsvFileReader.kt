package data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.io.IOException

class CsvFileReader {
    fun readLinesFromFile(): List<Map<String, String>> {
        val mealMap:MutableList<Map<String,String>> = mutableListOf()
        csvReader().readAllWithHeader(getCsvFile()).map { row ->
            try {
                mealMap.add(row)
            }catch (e:Exception){
                println("⚠️ Skipped line: ${e.message}")
            }
        }
        return mealMap
    }

    private fun getCsvFile(): File {
        val foodCsvFile = File(FILE_NAME)
        if (foodCsvFile.exists()) {
            return foodCsvFile
        }
        throw IOException("File Not Found")
    }

    companion object{
        private const val FILE_NAME = "assets/food.csv"
    }
}