package ai

import scala.io.Source

import ai.search.Search
import ai.search.DefaultHeuristic
import ai.sokoban.{Initializer, SokobanGraph}
import ai.sokoban.heuristic.BoxDistanceHeuristic
import ai.sokoban.heuristic.CountGoalsHeuristic

object SokobanSolver {
  // run {level} --search={search} --heuristic={heuristic}
  def main(args: Array[String]): Unit = {
    val level = args(0)

    val startTime = System.nanoTime()
    val initializer = new Initializer(Source.fromFile(level))
    //val graph = new SokobanGraph(initializer.game, BoxDistanceHeuristic(initializer.game))
    //val graph = new SokobanGraph(initializer.game, CountGoalsHeuristic(initializer.game))
    val graph = new SokobanGraph(initializer.game, DefaultHeuristic())
    val initialState = initializer.gameState

    //println("Using A* with BoxDistance Heuristic")
    println("Using Breadth First Search")
    println("Searching ...")
    val path = Search.breadthFirst(graph, graph.get(initialState)).operations
    //val path = Search.depthFirst(graph, graph.get(initialState)).operations
    //val path = Search.uniformCost(graph, graph.get(initialState)).operations
    //val path = Search.greedyBestFirst(graph, graph.get(initialState)).operations
    //val path = Search.aStar(graph, graph.get(initialState)).operations
    val endTime = System.nanoTime()

    println("Path found: " + path.map(_.name).mkString(" "))

    println("")
    println("Search time: " + ((endTime - startTime) / 1000000000.0) + " seconds")

    val mb = 1024*1024
    val runtime = Runtime.getRuntime
    println("Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb + " MB")
    println("Free Memory:  " + runtime.freeMemory / mb + " MB")
    println("Total Memory: " + runtime.totalMemory / mb + " MB")
    println("Max Memory:   " + runtime.maxMemory / mb + " MB")
  }
}
