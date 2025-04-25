package data

import org.damascus.data.MealDataParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MealDataParserTest {
 private lateinit var parser: MealDataParser

 @BeforeEach
 fun setup() {
  parser = MealDataParser()
 }

 @Test
 fun `test parseLine with valid data`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(1, meal.id)
  assertEquals("Recipe Name", meal.name)
  assertEquals(30, meal.minutes)
  assertEquals(123, meal.contributorId)
  assertEquals("2023-01-01", meal.submitted)
  assertEquals(listOf("tag1", "tag2"), meal.tags)
  assertEquals(100.0, meal.nutrition.calories)
  assertEquals(10.0, meal.nutrition.totalFat)
  assertEquals(5.0, meal.nutrition.sugar)
  assertEquals(500.0, meal.nutrition.sodium)
  assertEquals(20.0, meal.nutrition.protein)
  assertEquals(3.0, meal.nutrition.saturatedFat)
  assertEquals(30.0, meal.nutrition.carbohydrates)
  assertEquals(2, meal.stepsCount)
  assertEquals(listOf("step1", "step2"), meal.steps)
  assertEquals("Description", meal.description)
  assertEquals(listOf("ing1", "ing2"), meal.ingredients)
  assertEquals(2, meal.ingredientsCount)
 }

 @Test
 fun `test parseLine with missing required fields`() {
  //given
  val invalidRow =
   "Recipe Name,30,123,2023-01-01,[tag1,tag2],[100,5,10,200,20,3,30],5,[step1,step2],Description,[ing1,ing2],2"
  // when && then
  assertThrows<IllegalArgumentException> {
   parser.parseLine(invalidRow)
  }
 }

 @Test
 fun `test parseLine with empty nutrition values`() {
  //given
  val row = "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[,,,,,,],5,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(0.0, meal.nutrition.calories)
  assertEquals(0.0, meal.nutrition.totalFat)
  assertEquals(0.0, meal.nutrition.sugar)
  assertEquals(0.0, meal.nutrition.sodium)
  assertEquals(0.0, meal.nutrition.protein)
  assertEquals(0.0, meal.nutrition.saturatedFat)
  assertEquals(0.0, meal.nutrition.carbohydrates)
 }

 @Test
 fun `test parseLine with empty lists`() {
  //given
  val row = "Recipe Name,1,30,123,2023-01-01,[],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],0,[],Description,[],0"
  //when
  val meal = parser.parseLine(row)
  //then
  assertTrue(meal.tags.isEmpty())
  assertTrue(meal.steps.isEmpty())
  assertTrue(meal.ingredients.isEmpty())
 }

 @Test
 fun `test parseLine with quoted values`() {
  //given
  val row =
   "\"Recipe, Name\",1,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals("Recipe, Name", meal.name)
 }

 @Test
 fun `test parseLine with special characters`() {
  //given
  val row =
   "\"Recipe!@# Name\",1,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals("Recipe!@# Name", meal.name)
 }

 @Test
 fun `test parseLine with Unicode characters`() {
  //given
  val row =
   "レシピ名,1,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals("レシピ名", meal.name)
 }

 @Test
 fun `test parseLine with invalid numeric values`() {
  //given
  val row =
   "Recipe Name,1,invalid,123,2023-01-01,[tag1,tag2],[invalid,5,10,200,20,3,30],5,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(0, meal.minutes)
  assertEquals(0.0, meal.nutrition.calories)
 }

 @Test
 fun `test parseLine with malformed list values`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertTrue(meal.tags.isNotEmpty())
  assertTrue(meal.steps.isNotEmpty())
  assertTrue(meal.ingredients.isNotEmpty())
 }

 @Test
 fun `test parseLine with missing ID`() {
  //given
  val row =
   "Recipe Name,1,30,invalid,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when && then
  assertFailsWith<IllegalArgumentException> {
   parser.parseLine(row)
  }
 }

 @Test
 fun `test parseLine with empty string`() {
  //given && when && then
  assertFailsWith<IllegalArgumentException> {
   parser.parseLine("")
  }
 }

 @Test
 fun `test parseLine with only commas`() {
  //given
  val row = ",,,,,,,,,,,"
  //when && then
  assertThrows<IllegalArgumentException> {
   parser.parseLine(row)
  }
 }

 @Test
 fun `test parseLine with unbalanced quotes`() {
  // given
  val row =
   "\"Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[100,5,10,200,20,3,30],5,[step1,step2],Description,[ing1,ing2],2"
  //when && then
  assertThrows<IllegalArgumentException> {
   parser.parseLine(row)
  }
 }

 @Test
 fun `test parseLine with list containing only whitespace`() {
  // given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[  ,  ],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[  ,  ],Description,[  ,  ],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertTrue(meal.tags.isEmpty())
  assertTrue(meal.steps.isEmpty())
  assertTrue(meal.ingredients.isEmpty())
 }

 @Test
 fun `test parseLine with list containing mixed whitespace`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[  tag1  ,  tag2  ],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[  step1  ,  step2  ],Description,[  ing1  ,  ing2  ],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(listOf("tag1", "tag2"), meal.tags)
  assertEquals(listOf("step1", "step2"), meal.steps)
  assertEquals(listOf("ing1", "ing2"), meal.ingredients)
 }

 @Test
 fun `test parseLine with list containing single quotes`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,['tag1','tag2'],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,['step1','step2'],Description,['ing1','ing2'],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(listOf("tag1", "tag2"), meal.tags)
  assertEquals(listOf("step1", "step2"), meal.steps)
  assertEquals(listOf("ing1", "ing2"), meal.ingredients)
 }

 @Test
 fun `test parseLine with list containing double quotes`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[\"tag1\",\"tag2\"],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[\"step1\",\"step2\"],Description,[\"ing1\",\"ing2\"],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(listOf("tag1", "tag2"), meal.tags)
  assertEquals(listOf("step1", "step2"), meal.steps)
  assertEquals(listOf("ing1", "ing2"), meal.ingredients)
 }

 @Test
 fun `test parseLine with list containing mixed quotes`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,['tag1',\"tag2\"],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,['step1',\"step2\"],Description,['ing1',\"ing2\"],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(listOf("tag1", "tag2"), meal.tags)
  assertEquals(listOf("step1", "step2"), meal.steps)
  assertEquals(listOf("ing1", "ing2"), meal.ingredients)
 }

 @Test
 fun `test parseLine with nutrition values containing spaces`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[100.0 , 10.0 , 5.0 , 500.0 , 20.0 , 3.0 , 30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(100.0, meal.nutrition.calories)
  assertEquals(10.0, meal.nutrition.totalFat)
  assertEquals(5.0, meal.nutrition.sugar)
  assertEquals(500.0, meal.nutrition.sodium)
  assertEquals(20.0, meal.nutrition.protein)
  assertEquals(3.0, meal.nutrition.saturatedFat)
  assertEquals(30.0, meal.nutrition.carbohydrates)
 }

 @Test
 fun `test parseLine with invalid id`() {
  //given
  val row =
   "Recipe Name,invalid,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when && then
  assertFailsWith<IllegalArgumentException> {
   parser.parseLine(row)
  }
 }

 @Test
 fun `test parseLine with insufficient columns`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description"
  //when && then
  assertFailsWith<IllegalArgumentException> {
   parser.parseLine(row)
  }
 }

 @Test
 fun `test parseLine with invalid nutrition values`() {
  //given
  val row =
   "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[invalid,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(0.0, meal.nutrition.calories)
  assertEquals(10.0, meal.nutrition.totalFat)
 }

 @Test
 fun `test parseLine with missing nutrition values`() {
  //given
  val row = "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[100.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(100.0, meal.nutrition.calories)
  assertEquals(0.0, meal.nutrition.totalFat)
  assertEquals(0.0, meal.nutrition.sugar)
  assertEquals(0.0, meal.nutrition.sodium)
  assertEquals(0.0, meal.nutrition.protein)
  assertEquals(0.0, meal.nutrition.saturatedFat)
  assertEquals(0.0, meal.nutrition.carbohydrates)
 }

 @Test
 fun `test parseLine with optional int values`() {
  //given
  val row =
   "Recipe Name,1,invalid,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],invalid,[step1,step2],Description,[ing1,ing2],invalid"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(0, meal.minutes)
  assertEquals(0, meal.stepsCount)
  assertEquals(0, meal.ingredientsCount)
 }

 @Test
 fun `test parseLine with nested quotes`() {
  //given
  val row =
   "\"Recipe 'Name'\",1,30,123,2023-01-01,[tag1,tag2],[100.0,10.0,5.0,500.0,20.0,3.0,30.0],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals("Recipe 'Name'", meal.name)
 }

 @Test
 fun `test parseLine with empty nutrition list`() {
  //given
  val row = "Recipe Name,1,30,123,2023-01-01,[tag1,tag2],[],2,[step1,step2],Description,[ing1,ing2],2"
  //when
  val meal = parser.parseLine(row)
  //then
  assertEquals(0.0, meal.nutrition.calories)
  assertEquals(0.0, meal.nutrition.totalFat)
  assertEquals(0.0, meal.nutrition.sugar)
  assertEquals(0.0, meal.nutrition.sodium)
  assertEquals(0.0, meal.nutrition.protein)
  assertEquals(0.0, meal.nutrition.saturatedFat)
  assertEquals(0.0, meal.nutrition.carbohydrates)
 }
}