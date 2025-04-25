package data

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MealFileReaderTest {
 private lateinit var mealFileReader: MealFileReader
 private lateinit var testCsvFile: File
 private val testFilePath = "test_food.csv"

 @BeforeEach
 fun setUp() {
  testCsvFile = File(testFilePath)
  mealFileReader = MealFileReader(testFilePath)
 }

 @AfterEach
 fun deleteTestCsvFileIfExists() {
  if (testCsvFile.exists()) {
   testCsvFile.delete()
  }
 }

 private fun createTestFile(content: String) {
  testCsvFile.writeText(content)
 }

 @Test
 fun `test successful file reading with simple data`() {
  // Given a CSV file with two simple data rows
  createTestFile("""
        Header1,Header2,Header3
        value1,value2,value3
        simple,row,data
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then the correct number of lines should be returned
  assertEquals(2, lines.size)
  assertEquals("value1,value2,value3", lines[0])
  assertEquals("simple,row,data", lines[1])
 }

 @Test
 fun `test handling quoted values with newlines`() {
  // Given a CSV file with quoted value containing a newline
  createTestFile("""
        Header1,Header2,Header3
        value1,"value2
        with newline",value3
        simple,row,data
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then the quoted value should preserve the newline and count as one record
  assertEquals(2, lines.size)
  assertTrue(lines[0].contains("value2\nwith newline"))
 }

 @Test
 fun `test handling multiple quoted values with newlines`() {
  // Given a CSV with multiple quoted values that contain newlines
  createTestFile("""
        Header1,Header2,Header3
        "multi
        line1","value2
        with
        newlines","last
        value"
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then the entire row should be read as one record with preserved newlines
  assertEquals(1, lines.size)
  val line = lines[0]
  assertTrue(line.contains("multi\nline1"))
  assertTrue(line.contains("value2\nwith\nnewlines"))
  assertTrue(line.contains("last\nvalue"))
 }

 @Test
 fun `test empty file with only header`() {
  // Given a CSV with only header
  createTestFile("Header1,Header2,Header3")

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then the result should be an empty list
  assertTrue(lines.isEmpty())
 }

 @Test
 fun `test file with empty lines between data`() {
  // Given a CSV file with empty lines between data
  createTestFile("""
        Header1,Header2,Header3
        value1,value2,value3

        simple,row,data
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then only valid data rows should be returned
  assertEquals(2, lines.size)
  assertEquals("value1,value2,value3", lines[0])
  assertEquals("simple,row,data", lines[1])
 }

 @Test
 fun `test handling commas within quoted values`() {
  // Given a CSV file with commas inside quoted value
  createTestFile("""
        Header1,Header2,Header3
        value1,"value2, with, commas",value3
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then the value should include commas inside quotes
  assertEquals(1, lines.size)
  assertTrue(lines[0].contains("\"value2, with, commas\""))
 }

 @Test
 fun `test file with different number of columns`() {
  // Given a CSV file with rows of varying column counts
  createTestFile("""
        Header1,Header2,Header3
        value1,value2
        value1,value2,value3,value4
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then all non-header rows should be returned regardless of column count
  assertEquals(2, lines.size)
  assertEquals("value1,value2", lines[0])
  assertEquals("value1,value2,value3,value4", lines[1])
 }

 @Test
 fun `test file with empty values`() {
  // Given a CSV file with some empty values
  createTestFile("""
        Header1,Header2,Header3
        value1,,value3
        ,value2,
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then empty values should be preserved in output
  assertEquals(2, lines.size)
  assertEquals("value1,,value3", lines[0])
  assertEquals(",value2,", lines[1])
 }

 @Test
 fun `test file not found throws IOException`() {
  // Given a MealFileReader for a non-existing file
  // When reading the file
  // Then IOException should be thrown
  assertThrows<IOException> {
   mealFileReader.readLinesFromFile()
  }
 }

 @Test
 fun `test file with special characters`() {
  // Given a CSV file with special characters
  createTestFile("""
        Header1,Header2,Header3
        value1,"Special chars: !@#$%^&*()",value3
        "Tab\tchar","New\nline","Carriage\rreturn"
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then special characters should be included in output
  assertEquals(2, lines.size)
  assertTrue(lines[0].contains("Special chars: !@#$%^&*()"))
  assertTrue(lines[1].contains("Tab\tchar"))
 }

 @Test
 fun `test file with Unicode characters`() {
  // Given a CSV file with Unicode characters
  createTestFile("""
        Header1,Header2,Header3
        "こんにちは","Café","🌟 Star"
        "русский","español","中文"
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then Unicode characters should be preserved
  assertEquals(2, lines.size)
  assertTrue(lines[0].contains("こんにちは"))
  assertTrue(lines[0].contains("🌟"))
  assertTrue(lines[1].contains("中文"))
 }

 @Test
 fun `test blank line inside quoted block is not skipped`() {
  // Given a CSV with a quoted value containing a blank line
  createTestFile("""
        Header1,Header2
        "value1
          
        still inside quote",value2
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then blank lines inside quotes should not split the record
  assertEquals(1, lines.size)
  assertTrue(lines[0].contains("still inside quote"))
 }

 @Test
 fun `test quoted line with odd quote count toggles block mode`() {
  // Given a CSV with quotes starting and ending on different lines
  createTestFile("""
        Header1
        "Unclosed quote
        still unclosed
        then closing"
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then the entire quoted block should be treated as one record
  assertEquals(1, lines.size)
  assertTrue(lines[0].contains("Unclosed quote"))
 }

 @Test
 fun `test escape sequences are converted correctly`() {
  // Given a CSV with special escape sequences
  createTestFile("""
        Header1
        value with\t tab and\n newline and\r carriage and\"quote\"
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then all escape sequences should appear in the parsed value
  assertEquals(1, lines.size)
  val result = lines[0]
  assertTrue(result.contains("\t"))
  assertTrue(result.contains("\n"))
  assertTrue(result.contains("\r"))
  assertTrue(result.contains("\"quote\""))
 }

 @Test
 fun `test non-blank record is added`() {
  // Given a CSV file with one data record
  createTestFile("""
        Header1,Header2,Header3
        value1,value2,value3
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then the record should be added to the result
  assertEquals(1, lines.size)
  assertEquals("value1,value2,value3", lines[0])
 }

 @Test
 fun `test blank record is ignored`() {
  // Given a CSV file with blank lines between records
  createTestFile("""
        Header1,Header2,Header3
        value1,value2,value3

        
        value4,value5,value6
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then only non-blank records should be returned
  assertEquals(2, lines.size)
  assertEquals("value1,value2,value3", lines[0])
  assertEquals("value4,value5,value6", lines[1])
 }

 @Test
 fun `test record with escaped special characters is processed correctly`() {
  // Given a CSV file with escaped quotes
  createTestFile("""
        Header1,Header2,Header3
        value1,"This is a ""quoted"" text",value3
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then escaped quotes should be interpreted correctly
  assertEquals(1, lines.size)
  assertTrue(lines[0].contains("This is a \"quoted\" text"))
 }

 @Test
 fun `test buffer is reset after each record`() {
  // Given a CSV with multiple records
  createTestFile("""
        Header1,Header2,Header3
        value1,value2,value3
        value4,value5,value6
    """.trimIndent())

  // When reading the file
  val lines = mealFileReader.readLinesFromFile()

  // Then each record should be processed independently
  assertEquals(2, lines.size)
  assertEquals("value1,value2,value3", lines[0])
  assertEquals("value4,value5,value6", lines[1])
 }

}
