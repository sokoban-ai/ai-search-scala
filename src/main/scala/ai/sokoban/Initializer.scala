package ai.sokoban

import scala.io.Source

import ai.search.Search

class Initializer(serializedBoard: Source) {
  private val iter = serializedBoard.getLines

  val size = toPoint(parseLine(iter.next))
  val walls = toPoints(parseLine(iter.next)).toSet
  val boxes = toPoints(parseLine(iter.next)).toSet
  val storage = toPoints(parseLine(iter.next)).toSet
  val player = toPoint(parseLine(iter.next))

  private def toPoint(point: Array[Int]): Point = Point(point(0), point(1))
  private def parseLine(line: String): Array[Int] = line.split(" ").map(_.toInt)
  private def toPoints(input: Array[Int]): Iterator[Point] = input.tail.sliding(2, 2).map(toPoint)

  val reachable = {
    val game = PullGame(walls)
    val graph = SokobanPullGraph(game)
    val state = PullGameState(player, null)

    Search.breadthFirst(graph, graph.get(state)).visited.map { case PullGameState(player, _) => player }
  }

  val restricted = {
    def boxVisited(box: Point) = {
      val game = PullGame(walls)
      val graph = SokobanPullGraph(game)
      val initialNodes = for (neighbor <- box.neighbors if !game.isWall(neighbor)) yield
        graph.get(PullGameState(neighbor, box))

      Search.breadthFirst(graph, initialNodes).visited.map { case PullGameState(_, box) => box }
    }

    val visited = for (box <- storage; point <- boxVisited(box)) yield point

    reachable -- visited
  }

  val game = Game(walls, storage intersect reachable, restricted, reachable)
  val gameState = GameState(player, boxes intersect reachable)
}
