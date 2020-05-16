package com.louis.spark

import java.sql.DriverManager

object Test {
  def main(args: Array[String]): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    val connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data?serverTimezone=PST","root","12345678")
    val sql = "insert into wordcount(word,wordcount) values('a',1)"
    connection.createStatement().execute(sql)
    connection.close()
  }
}
