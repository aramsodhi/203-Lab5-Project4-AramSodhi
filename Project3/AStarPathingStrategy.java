import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    /**
     * Return a list containing a single point representing the next step toward a goal
     * If the start is within reach of the goal, the returned list is empty.
     *
     * @param start the point to begin the search from
     * @param end the point to search for a point within reach of
     * @param canPassThrough a function that returns true if the given point is traversable
     * @param withinReach a function that returns true if both points are within reach of each other
     * @param potentialNeighbors a function that returns the neighbors of a given point, as a stream
     */

    public List<Point> computePath(
            Point start,
            Point end,
            Predicate<Point> canPassThrough,
            BiPredicate<Point, Point> withinReach,
            Function<Point, Stream<Point>> potentialNeighbors
    ) {
        // sort by lowest f-score
        List<Point> openSet = new ArrayList<>();
        List<Point> closedSet = new ArrayList<>();

        Map<Point, Integer> gScores = new HashMap<>();
        Map<Point, Integer> fScores = new HashMap<>();
        Map<Point, Point> previousMappings = new HashMap<>();

        List<Point> path = new ArrayList<>();

        // record g-value of start node
        gScores.put(start, 0);

        // record f-score of start node (0 + hScore)
        Integer HScoreStart = start.manhattanDistanceTo(end);
        fScores.put(start, HScoreStart);

        // add the start node to the open set
        openSet.add(start);

        // while the closed set is not empty
        while (!openSet.isEmpty()) {
            // pick node with lowest f-score
            Point currentNode = openSet.getFirst();

            for (Point point : openSet) {
                if (fScores.get(point) < fScores.get(currentNode)) {
                    currentNode = point;
                }
            }

            // move current node from open set to closed set
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            // if the current node is within reach of the end node, reconstruct path
            if (withinReach.test(currentNode, end)) {
               Point current = currentNode;

               while (current != null) {
                   path.add(current);
                   current = previousMappings.get(current);
               }

               Collections.reverse(path);

               // remove start and goal nodes from path
               path.remove(start);
               path.remove(end);

               return path;
            }

            // get neighbors that are not occupied/obstacles and are not in the closed set
            List<Point> traversableNeighbors = potentialNeighbors.apply(currentNode)
                    .filter(canPassThrough)
                    .filter((p) -> !closedSet.contains(p))
                    .toList();

            // iterate over traversable neighbors
            for (Point currentTN : traversableNeighbors) {
                // if current traversable neighbor is not in the open set, add it
                if (!openSet.contains(currentTN)) {
                    openSet.add(currentTN);
                }

                // calculate g-score of current traversable neighbor (g-score of current node + 1)
                Integer gScoreCurrentTN = gScores.get(currentNode) + 1;

                // calculate f-score of current traversable neighbor (g-score plus h-score)
                Integer hScoreCurrentTN = currentTN.manhattanDistanceTo(end);
                Integer fScoreCurrentTN = hScoreCurrentTN + gScoreCurrentTN;

                // if this g-score is better than previously calculated or first time being calculated, update hash map
                if (!gScores.containsKey(currentTN) || gScoreCurrentTN < gScores.get(currentTN)) {
                    gScores.put(currentTN, gScoreCurrentTN);
                    fScores.put(currentTN, fScoreCurrentTN);
                    previousMappings.put(currentTN, currentNode);
                }
            }
        }

        // return path (will be empty if no path was found)
        return path;
    }
}
